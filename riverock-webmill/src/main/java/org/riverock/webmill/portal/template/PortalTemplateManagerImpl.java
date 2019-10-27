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
package org.riverock.webmill.portal.template;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.webmill.portal.bean.TemplateBean;
import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.webmill.portal.template.parser.TemplateParserFactory;

/**
 * $Id: PortalTemplateManagerImpl.java 1377 2007-08-29 16:09:14Z serg_main $
 */
public final class PortalTemplateManagerImpl implements PortalTemplateManager {
    private final static Logger log = Logger.getLogger( PortalTemplateManagerImpl.class );

    static final int INIT_COUNT_OF_SITE = 10;

    private Map<Long, PortalTemplate> hashId = new ConcurrentHashMap<Long, PortalTemplate>(INIT_COUNT_OF_SITE);
    private Long siteId;
    private final Object syncObject = new Object();

    public void destroy() {
        if (hashId!=null) {
            hashId.clear();
            hashId=null;
        }
        siteId=null;
    }

    public void destroy(Long templateId) {
        synchronized(syncObject) {
            hashId.remove(templateId);
        }
    }

    public PortalTemplate getTemplate( final long templateId) {
        if (log.isTraceEnabled()) {
            log.trace("search template for id - "+ templateId);
            for (Map.Entry<Long, PortalTemplate> entry : hashId.entrySet() ) {
                log.trace("template id: " + entry.getKey()+", template: " +entry.getValue());
            }
        }

        //noinspection UnnecessaryBoxing
        Long templateIdAsLong = new Long(templateId);

        PortalTemplate template = hashId.get(templateIdAsLong);
        if (template!=null) {
            return template;
        }

        synchronized(syncObject) {
            template = hashId.get(templateIdAsLong);
            if (template!=null) {
                return template;
            }
            // get full template (with blob)
            TemplateBean templateBean = InternalDaoFactory.getInternalTemplateDao().getTemplate(templateId);
            if (templateBean==null) {
                return null;
            }
            template = prepareTemplate(templateBean);
            hashId.put(templateBean.getTemplateId(), template);

            return template;
        }
    }

    public PortalTemplate getTemplate( final String nameTemplate, final String lang ) {
        if ( log.isDebugEnabled() ) {
            log.debug( "search template for name: '" + nameTemplate + "', lang: '" + lang + "'" );
        }

        if ( nameTemplate==null || lang==null ) {
            return null;
        }

        TemplateBean template = InternalDaoFactory.getInternalTemplateDao().getTemplate(siteId, nameTemplate, lang);
        if (template==null) {
            return null;
        }

        Long templateId = template.getTemplateId();
        
        PortalTemplate portalTemplate = hashId.get( templateId );
        if (portalTemplate!=null && template.getVersion()==portalTemplate.getVersion()) {
            return portalTemplate;
        }
        else {
            synchronized (syncObject) {
                template = InternalDaoFactory.getInternalTemplateDao().getTemplate(templateId);
                if (template==null) {
                    hashId.remove(templateId);
                    return null;
                }
                portalTemplate = hashId.get(templateId);
                if (portalTemplate!=null && template.getVersion()==portalTemplate.getVersion()) {
                    return portalTemplate;
                }

                PortalTemplate st = prepareTemplate(template);
                hashId.put(templateId, st);
                return st;
            }
        }
    }

    PortalTemplateManagerImpl(Long siteId) {
        this.siteId=siteId;
        log.debug("Start PortalTemplateManagerImpl()");
    }

    static PortalTemplate prepareTemplate(TemplateBean template) {
        if (log.isDebugEnabled()) {
            log.debug("Digest template:\n" + template.getTemplateData());
        }
        if (StringUtils.isBlank(template.getTemplateData()) ) {
            final PortalTemplateImpl portalTemplate = new PortalTemplateImpl();
            portalTemplate.setTemplateName( template.getTemplateName() );
            return portalTemplate;
        }
        return parseNotEmptyTemplate(template);
    }

    static PortalTemplateImpl parseNotEmptyTemplate(TemplateBean template) {
        if (log.isDebugEnabled()) {
            log.debug("Digest template:\n" + template.getTemplateData());
        }
        PortalTemplateImpl portalTemplate = new PortalTemplateImpl();
        portalTemplate.setTemplate( TemplateParserFactory.getTemplateParser().parse(template.getTemplateData().getBytes()) );
        portalTemplate.setRole(template.getTemplateRole());
        portalTemplate.setTemplateName( template.getTemplateName() );
        portalTemplate.setTemplateId( template.getTemplateId() );
        portalTemplate.setVersion( template.getVersion() );

        return portalTemplate;
    }
}