/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.webmill.site;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.digester.Digester;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.portal.template.PortalTemplate;
import org.riverock.interfaces.portal.template.PortalTemplateManager;
import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.webmill.portal.template.bean.PortalTemplateImpl;
import org.riverock.webmill.portal.template.bean.PortalTemplateItemImpl;
import org.riverock.webmill.portal.template.bean.PortalTemplateParameterImpl;

/**
 * $Id$
 */
public final class PortalTemplateManagerImpl implements PortalTemplateManager {
    private final static Logger log = Logger.getLogger( PortalTemplateManagerImpl.class );

    private static final int INIT_COUNT_OF_SITE = 10;

    private static Map<Long, PortalTemplateManager> managers = new HashMap<Long, PortalTemplateManager>(INIT_COUNT_OF_SITE);

    private static Digester digester = null;
    static {
        digester = new Digester();
        digester.setValidating(false);

        digester.addObjectCreate("SiteTemplate", PortalTemplateImpl.class);
        digester.addSetProperties("SiteTemplate", "role", "role");

        digester.addObjectCreate("SiteTemplate/SiteTemplateItem", PortalTemplateItemImpl.class);
        digester.addSetProperties("SiteTemplate/SiteTemplateItem", "type", "type");
        digester.addSetProperties("SiteTemplate/SiteTemplateItem", "value", "value");
        digester.addSetProperties("SiteTemplate/SiteTemplateItem", "code", "code");
        digester.addSetProperties("SiteTemplate/SiteTemplateItem", "xmlRoot", "xmlRoot");
        digester.addSetProperties("SiteTemplate/SiteTemplateItem", "role", "role");
        digester.addSetNext("SiteTemplate/SiteTemplateItem", "addSiteTemplateItem" );

        digester.addObjectCreate("SiteTemplate/SiteTemplateItem/Parameter", PortalTemplateParameterImpl.class);
        digester.addSetProperties("SiteTemplate/SiteTemplateItem/Parameter", "name", "name");
        digester.addSetProperties("SiteTemplate/SiteTemplateItem/Parameter", "value", "value");
        digester.addSetNext("SiteTemplate/SiteTemplateItem/Parameter", "addParameter");

    }

    private Map<Long, PortalTemplate> hashId = new ConcurrentHashMap<Long, PortalTemplate>(INIT_COUNT_OF_SITE);
    private Long siteId;

    public static PortalTemplateManager getInstance(Long siteId) {
        PortalTemplateManager manager = managers.get(siteId);
        if (manager==null) {
            synchronized(PortalTemplateManagerImpl.class) {
                manager = managers.get(siteId);
                if (manager!=null) {
                    return null;
                }
                manager = new PortalTemplateManagerImpl(siteId);
                managers.put(siteId, manager);
                return manager;
            }
        }
        return manager;
    }

    public PortalTemplate getTemplate( final long id ) {
        if (log.isDebugEnabled()) {
            log.debug("search template for id - "+ id);
            for (Map.Entry<Long, PortalTemplate> entry : hashId.entrySet() ) {
                log.debug("template id: " + entry.getKey()+", template: " +entry.getValue());
            }
        }

        return hashId.get(id);
    }

    public PortalTemplate getTemplate( final String nameTemplate, final String lang ) {
        if ( log.isDebugEnabled() ) {
            log.debug( "search template for name: '" + nameTemplate + "', lang: '" + lang + "'" );
        }

        if ( nameTemplate == null || lang == null )
            return null;

        Template template = InternalDaoFactory.getInternalTemplateDao().getTemplate(siteId, nameTemplate, lang); 
        if (template==null) {
            return null;
        }
        PortalTemplate portalTemplate = hashId.get( template.getTemplateId());
        if (portalTemplate!=null) {
            if (template.getVersion()==portalTemplate.getVersion()) {
                return portalTemplate;
            }
            else {
                // get full template (with blob)
                template = InternalDaoFactory.getInternalTemplateDao().getTemplate(template.getTemplateId());
                PortalTemplate st = digestSiteTemplate(template);
                hashId.put(template.getTemplateId(), st);
                return st;
            }
        }
        else {
            // get full template (with blob)
            template = InternalDaoFactory.getInternalTemplateDao().getTemplate(template.getTemplateId());
            PortalTemplate st = digestSiteTemplate(template);
            hashId.put(template.getTemplateId(), st);
            return st;
        }
    }

    private PortalTemplateManagerImpl(Long siteId) {
        this.siteId=siteId;
        log.debug("Start PortalTemplateManagerImpl()");
        for (Template template : InternalDaoFactory.getInternalTemplateDao().getTemplateList( siteId )) {
            try {
                PortalTemplate st = digestSiteTemplate(template);
                // dont add broken template 
                if (st!=null) {
                    hashId.put(template.getTemplateId(), st);
                }
            }
            catch (Exception e) {
                // Todo. Add to template text data. If we get error, then create template with error message
                String es = "Error get templates";
                log.error(es, e);
                throw new IllegalStateException(es, e);
            }
        }
    }

    static PortalTemplate digestSiteTemplate(Template template) {
        if (StringUtils.isBlank(template.getTemplateData()) ) {
            final PortalTemplateImpl portalTemplate = new PortalTemplateImpl();
            portalTemplate.setTemplateName( template.getTemplateName() );
            return portalTemplate;
        }
        if (log.isDebugEnabled()) {
            log.debug("Digest template:\n" + template.getTemplateData());
        }
        PortalTemplateImpl st = null;
        try {
            st = (PortalTemplateImpl) digester.parse( new ByteArrayInputStream( template.getTemplateData().getBytes() ));
            st.setTemplateName( template.getTemplateName() );
            st.setTemplateId( template.getTemplateId() );
            st.setVersion( template.getVersion() );
        } catch (IOException e) {
            String es = "Error digest template, data:\n"+template.getTemplateData();
            log.error(es, e);
        } catch (SAXException e) {
            String es = "Error digest template, data:\n"+template.getTemplateData();
            log.error(es, e);
        }

        return st;
    }
}