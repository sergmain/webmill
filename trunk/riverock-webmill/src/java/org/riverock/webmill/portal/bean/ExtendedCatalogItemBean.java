/*
 * org.riverock.webmill -- Portal framework implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.webmill.portal.bean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.template.PortalTemplate;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.PortletContainerException;
import org.riverock.webmill.container.portlet.PortletEntry;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.portal.context.RequestContextParameter;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * @author Sergei Maslyukov
 *         Date: 20.04.2006
 *         Time: 13:12:45
 */
public final class ExtendedCatalogItemBean {
    private static Logger log = Logger.getLogger(ExtendedCatalogItemBean.class);

    private PortletDefinition portlet = null;
    private String namePortletId = null;
    private Locale locale = null;
    private Long concretePortletIdValue = null;
    private Long templateId = null;
    private Map<String, String> portletMetadata = null;
    private List<String> roleList = null;
    private String fullPortletName=null;

    private ExtendedCatalogItemBean() {
    }

    public String getFullPortletName() {
        return fullPortletName;
    }

    public void setFullPortletName(String fullPortletName) {
        String portletName = fullPortletName;
        if ( portletName.indexOf( PortletContainer.PORTLET_ID_NAME_SEPARATOR )==-1 ) {
            portletName = PortletContainer.PORTLET_ID_NAME_SEPARATOR + fullPortletName;
        }
        this.fullPortletName = portletName;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public Map<String, String> getPortletMetadata() {
        return portletMetadata;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public Long getConcretePortletIdValue() {
        return concretePortletIdValue;
    }

    public Locale getLocale() {
        return locale;
    }

    public PortletDefinition getPortletDefinition() {
        return portlet;
    }

    public String getNamePortletId() {
        return namePortletId;
    }

    public static ExtendedCatalogItemBean getInstance(RequestContextParameter factoryParameter, final Long ctxId) {
        if (log.isDebugEnabled()) {
            log.debug("ExtendedCatalogItemBean.getInstance() ctxId: " + ctxId);
        }

        if (ctxId == null) {
            return null;
        }

        ExtendedCatalogItemBean catalogItem = new ExtendedCatalogItemBean();
        CatalogItem ctx = InternalDaoFactory.getInternalCatalogDao().getCatalogItem(ctxId);
        // Dont include menuItem with not defined template
        if (ctx.getTemplateId()==null) {
            ctx=null;
        }

        if (ctx == null) {
            log.warn("Catalog record for id " + ctxId + " not found. process as 'index' page");
            return null;
        }
        catalogItem.concretePortletIdValue = ctx.getContextId();
        catalogItem.templateId = ctx.getTemplateId();
        catalogItem.portletMetadata = initMetadata(ctx);
        List<String> roles = new ArrayList<String>();
        if (!StringUtils.isEmpty(ctx.getPortletRole())) {
            StringTokenizer st = new StringTokenizer(ctx.getPortletRole());
            while (st.hasMoreElements()) {
                roles.add( st.nextToken() );
            }
            catalogItem.roleList = Collections.unmodifiableList(roles);
        }

        CatalogLanguageItem langMenu = InternalDaoFactory.getInternalCatalogDao().getCatalogLanguageItem(
            ctx.getCatalogLanguageId()
        );
        if (langMenu == null) {
            log.error("Lang Catalog with id " + ctx.getCatalogLanguageId() + " not found. process as 'index' page");
            return null;
        }

        SiteLanguage siteLanguage = InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguage(langMenu.getSiteLanguageId());
        if (siteLanguage == null) {
            log.error("Site language with id " + langMenu.getSiteLanguageId() + " not found. process as 'index' page");
            return null;
        }
        catalogItem.locale = StringTools.getLocale( siteLanguage.getCustomLanguage() );

        if (log.isDebugEnabled()) {
            log.debug("portalInfo: " + factoryParameter.getPortalInfo());
            if (factoryParameter.getPortalInfo() != null) {
                log.debug("portalInfo.getSiteId(): " + factoryParameter.getPortalInfo().getSiteId());
            }
            log.debug("siteLanguage: " + siteLanguage);
            log.debug("siteLanguage.getSiteId(): " + siteLanguage.getSiteId());
        }

        if (!factoryParameter.getPortalInfo().getSiteId().equals(siteLanguage.getSiteId())) {
            log.error("Requested context with id " + ctx.getCatalogId() + " is from others site. Process as 'index' page");
            return null;
        }

        if (ctx.getPortletId() == null) {
            log.error("defaultCatalogItemItem.ctx.getPortletId() is null, unknown portlet");
            log.error("idSiteCtxCatalog: " + ctx.getCatalogId());
            log.error("ctxId: " + ctxId);
            return null;
        }

        PortletName portletName = InternalDaoFactory.getInternalPortletNameDao().getPortletName(ctx.getPortletId());
        if (portletName.getPortletName() == null) {
            log.error("portletName for id " + ctx.getPortletId() + " not found");
            return null;
        }

        if (log.isDebugEnabled()) {
            log.debug("Portlet name, id "+ctx.getPortletId()+", name: " +portletName.getPortletName());
        }

        catalogItem.setFullPortletName(portletName.getPortletName());

        initPortletDefinition(factoryParameter, catalogItem);
        return catalogItem;
    }

    public static ExtendedCatalogItemBean getInstance(RequestContextParameter factoryParameter, String templateName, String portletName, Locale locale) {
        PortalTemplate template = factoryParameter.getPortalInfo().getPortalTemplateManager().getTemplate( templateName, locale.toString());
        if (template==null) {
            return null;
        }
        ExtendedCatalogItemBean extendedCatalogItem = new ExtendedCatalogItemBean();
        extendedCatalogItem.portletMetadata = new HashMap<String, String>();
        extendedCatalogItem.roleList = new ArrayList<String>();
        extendedCatalogItem.templateId = template.getTemplateId();
        extendedCatalogItem.locale = locale;
        extendedCatalogItem.setFullPortletName(portletName);

        initPortletDefinition(factoryParameter, extendedCatalogItem);
        return extendedCatalogItem;
    }

    private static void initPortletDefinition(RequestContextParameter contextParameter, ExtendedCatalogItemBean extendedCatalogItem) {
        if (contextParameter == null || contextParameter.getPortletContainer() == null) {
            return;
        }
        PortletEntry entry = null;
        try {
            entry = contextParameter.getPortletContainer().getPortletInstance(extendedCatalogItem.getFullPortletName());
        }
        catch (PortletContainerException e) {
            log.error("Error get portlet '" + extendedCatalogItem.getFullPortletName() + "'", e);
        }
        if (entry == null) {
            log.warn("Instance for portlet name "+extendedCatalogItem.getFullPortletName()+" not found");
            return;
        }

        extendedCatalogItem.portlet = entry.getPortletDefinition();
        if (extendedCatalogItem.portlet != null) {
            extendedCatalogItem.namePortletId = PortletService.getStringParam(
                extendedCatalogItem.portlet, ContainerConstants.name_portlet_id
            );
            extendedCatalogItem.setFullPortletName(extendedCatalogItem.portlet.getFullPortletName());
        }
    }

    private static Map<String, String> initMetadata( CatalogItem defaultCtx ) {
        if (log.isDebugEnabled()) {
            log.debug("defaultCtx: " + defaultCtx);
            if (defaultCtx!=null) {
                log.debug("defaultCtx.getMetadata(): " + defaultCtx.getMetadata());
            }
        }
        HashMap<String, String> map = new HashMap<String, String>();
        if (defaultCtx==null || defaultCtx.getMetadata()==null) {
            return map;
        }

        InputStream stream = new ByteArrayInputStream( defaultCtx.getMetadata().getBytes() );
        try {
            Properties p = new Properties();
            p.load( stream );
            for (Map.Entry entry : p.entrySet()) {
                map.put((String)entry.getKey(), (String)entry.getValue());
            }
            return Collections.unmodifiableMap( map );
        }
        catch( IOException e ) {
            String es = "Error load properties";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
    }

}
