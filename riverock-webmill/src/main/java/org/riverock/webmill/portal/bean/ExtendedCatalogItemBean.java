/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.webmill.portal.bean;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.common.collections.MapWithParameters;
import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.bean.CatalogItem;
import org.riverock.interfaces.portal.bean.CatalogLanguageItem;
import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.template.PortalTemplate;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.container.portlet.PortletContainerException;
import org.riverock.webmill.container.portlet.PortletEntry;
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
    private Map<String, List<String>> portletMetadata = null;
    private List<String> roleList = null;
    private String fullPortletName=null;
    private Long catalogId=null;

    private ExtendedCatalogItemBean() {
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
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

    public Map<String, List<String>> getPortletMetadata() {
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

        if (ctx==null) {
            if (log.isInfoEnabled()) {
                log.info("Catalog record for id " + ctxId + " not found. process as 'index' page");
            }
            return null;
        }

        // Dont include menuItem with not defined template
        if (ctx.getTemplateId()==null) {
            return null;
        }

        catalogItem.catalogId = ctx.getCatalogId();
        catalogItem.concretePortletIdValue = ctx.getContextId();
        catalogItem.templateId = ctx.getTemplateId();
        catalogItem.portletMetadata = initMetadata(ctx);
        List<String> roles = new ArrayList<String>();
        if (StringUtils.isNotBlank(ctx.getPortletRole())) {
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
        if (portletName==null) {
            return null;
        }
        
        PortalTemplate template = factoryParameter.getPortalInfo().getPortalTemplateManager().getTemplate( templateName, locale.toString());
        if (template==null) {
            return null;
        }
        ExtendedCatalogItemBean extendedCatalogItem = new ExtendedCatalogItemBean();
        extendedCatalogItem.portletMetadata = new HashMap<String, List<String>>();
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

    private static Map<String, List<String>> initMetadata( CatalogItem defaultCtx ) {
        if (log.isDebugEnabled()) {
            log.debug("defaultCtx: " + defaultCtx);
            if (defaultCtx!=null) {
                log.debug("defaultCtx.getMetadata(): " + defaultCtx.getMetadata());
            }
        }
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        if (defaultCtx==null || defaultCtx.getMetadata()==null) {
            return map;
        }

/*
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
*/
        BufferedReader reader = new BufferedReader( new InputStreamReader( new ByteArrayInputStream(defaultCtx.getMetadata().getBytes())) );
        try {
            String s;
            while ((s=reader.readLine())!=null) {
                if (log.isDebugEnabled()) {
                    log.debug("Line int metadata: " + s);
                }
                int idx = s.indexOf('=');
                if (idx==-1) {
                    continue;
                }
                String key = s.substring(0, idx).trim();
                String value = s.substring(idx+1).trim();
                if (log.isDebugEnabled()) {
                    log.debug("    key: " + key +", " +value);
                }

                if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
                    MapWithParameters.putInStringList(map, key, value);
                }
            }
            return map;
        }
        catch( IOException e ) {
            String es = "Error load properties";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
    }

}
