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

package org.riverock.webmill.portal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.interfaces.portlet.menu.MenuItemInterface;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.container.portlet.PortletContainerException;
import org.riverock.webmill.container.portlet.PortletEntry;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.schema.site.types.TemplateItemTypeTypeType;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.exception.PortalPersistenceException;
import org.riverock.webmill.port.PortalInfoImpl;
import org.riverock.webmill.portal.context.CtxContextFactory;
import org.riverock.webmill.portal.context.PageContextFactory;
import org.riverock.webmill.portal.context.PageidContextFactory;
import org.riverock.webmill.portal.context.UrlContextFactory;
import org.riverock.webmill.portal.menu.MenuItem;
import org.riverock.webmill.schema.core.WmPortalCatalogItemType;
import org.riverock.webmill.schema.core.WmPortalCatalogLanguageItemType;
import org.riverock.webmill.schema.core.WmPortalSiteLanguageItemType;
import org.riverock.webmill.schema.core.WmPortalPortletNameItemType;
import org.riverock.webmill.core.GetWmPortalCatalogItem;
import org.riverock.webmill.core.GetWmPortalCatalogLanguageItem;
import org.riverock.webmill.core.GetWmPortalSiteLanguageItem;
import org.riverock.webmill.core.GetWmPortalPortletNameItem;

/**
 * $Id$
 */
public abstract class ContextFactory {

    private static Logger log = Logger.getLogger( ContextFactory.class );

    public final static class PortletParameters {
        private String namespace = null;
        private Map<String, Object> parameters = null;

        public PortletParameters( final String namespace, final Map<String, Object> parameters ) {
            this.namespace = namespace;
            this.parameters = parameters;
        }

        public String getNamespace() {
            return namespace;
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }
    }

    public final static class ContextFactoryParameter {
        private DatabaseAdapter adapter = null;
        private HttpServletRequest request = null;
        private PortalInfoImpl portalInfo = null;
        private PortletContainer portletContainer = null;
        private Map<String, Object> httpRequestParameter = null;

        public DatabaseAdapter getAdapter() {
            return adapter;
        }

        public HttpServletRequest getRequest() {
            return request;
        }

        public PortalInfoImpl getPortalInfo() {
            return portalInfo;
        }

        public PortletContainer getPortletContainer() {
            return portletContainer;
        }

        public Map<String, Object> getHttpRequestParameter() {
            return httpRequestParameter;
        }
    }

    public final static class DefaultCtx {
        private WmPortalCatalogItemType ctx = null;
        private WmPortalCatalogLanguageItemType langMenu = null;
        private WmPortalSiteLanguageItemType siteLang = null;
        private PortletDefinition portlet = null;
        private String namePortletId = null;

        private DefaultCtx(){}

        public WmPortalCatalogItemType getCtx() {
            return ctx;
        }

        public PortletDefinition getPortletDefinition_() {
            return portlet;
        }

        public String getNamePortletId_() {
            return namePortletId;
        }

        public static DefaultCtx getInstance( ContextFactoryParameter factoryParameter, final Long ctxId )
            throws PortalException {

            try {
                DefaultCtx defaultCtx = new DefaultCtx();
                defaultCtx.ctx = GetWmPortalCatalogItem.getInstance(factoryParameter.adapter, ctxId).item;
                if (defaultCtx.ctx==null) {
                    log.error("Context with id "+ctxId+" not found. process as 'index' page");
                    return null;
                }

                defaultCtx.langMenu = GetWmPortalCatalogLanguageItem.getInstance(factoryParameter.adapter, defaultCtx.ctx.getIdSiteCtxLangCatalog()).item;
                if (defaultCtx.langMenu==null){
                    log.error("Lang Catalog with id "+defaultCtx.ctx.getIdSiteCtxLangCatalog()+" not found. process as 'index' page");
                    return null;
                }

                defaultCtx.siteLang = GetWmPortalSiteLanguageItem.getInstance(factoryParameter.adapter, defaultCtx.langMenu.getIdSiteSupportLanguage()).item;
                if (defaultCtx.siteLang==null){
                    log.error("Site language with id "+defaultCtx.langMenu.getIdSiteSupportLanguage()+" not found. process as 'index' page");
                    return null;
                }

                if (log.isDebugEnabled()) {
                    log.debug( "portalInfo: " + factoryParameter.portalInfo );
                    if (factoryParameter.portalInfo!=null) {
                        log.debug("portalInfo.getSiteId(): "+factoryParameter.portalInfo.getSiteId());
                    }
                    log.debug("siteLang: "+defaultCtx.siteLang);
                    if (defaultCtx.siteLang!=null) {
                        log.debug("siteLang.getIdSite(): "+defaultCtx.siteLang.getIdSite());
                    }
                }

                if (!factoryParameter.portalInfo.getSiteId().equals(defaultCtx.siteLang.getIdSite())) {
                    log.error("Requested context with id "+defaultCtx.ctx.getIdSiteCtxCatalog()+" is from others site. Process as 'index' page");
                    return null;
                }

                if (defaultCtx.ctx.getIdSiteCtxType()==null) {
                    log.error( "idSiteCtxCatalog: " + defaultCtx.ctx.getIdSiteCtxCatalog() );
                    log.error( "ctxId: " + ctxId );
                    throw new PortalException("contextTypeId is null, unknown portlet");
                }

                WmPortalPortletNameItemType ctxType = GetWmPortalPortletNameItem.getInstance( factoryParameter.adapter, defaultCtx.ctx.getIdSiteCtxType() ).item;
                if (ctxType==null) {
                    throw new PortalException("portletName for id "+defaultCtx.ctx.getIdSiteCtxType()+" not found");
                }

                initPortletDefinition( factoryParameter, defaultCtx, ctxType.getType() );
                return defaultCtx;
            }
            catch (Exception e) {
                log.error("Error", e);
                throw new PortalException("error", e);
            }
        }

        public static DefaultCtx getInstance( ContextFactoryParameter factoryParameter, final String portletName ) {

            DefaultCtx defaultCtx = new DefaultCtx();
            initPortletDefinition( factoryParameter, defaultCtx, portletName );
            return defaultCtx;
        }

        protected static void initPortletDefinition( ContextFactoryParameter factoryParameter, DefaultCtx defaultCtx, String portletName ) {
            if (factoryParameter == null || factoryParameter.portletContainer==null) {
                return;
            }
            PortletEntry entry = null;
            try {
                entry = factoryParameter.portletContainer.getPortletInstance( portletName );
            }
            catch (PortletContainerException e) {
                log.error("Error get portlet '"+portletName+"'", e);
            }
            if (entry==null) {
                return;
            }

            defaultCtx.portlet = entry.getPortletDefinition();
            if (defaultCtx.portlet!=null)  {
                defaultCtx.namePortletId =
                    PortletService.getStringParam(defaultCtx.portlet, ContainerConstants.name_portlet_id);
            }
        }
    }

    protected String nameTemplate = null;
    protected Locale realLocale = null;
    protected Map<String, Object> dynamicParameter = null;
    protected List<PortletParameters> portletsParameter = new LinkedList<PortletParameters>();
    protected String urlResource = null;
    protected DefaultCtx defaultCtx = null;

    protected abstract Long initPortalParameters( ContextFactoryParameter factoryParameter) throws PortalException;
    protected abstract void prepareParameters( final HttpServletRequest httpRequest, final Map<String, Object> httpRequestParameter ) throws PortalException;

    public PortletParameters getParameters( final String namespace, final TemplateItemTypeTypeType type ) {
        if (type!=null && type.getType()==TemplateItemTypeTypeType.DYNAMIC_TYPE) {
            return new PortletParameters(namespace, dynamicParameter);
        }

        if ( namespace==null || portletsParameter==null ) {
            return null;
        }

        Iterator it = portletsParameter.iterator();
        while (it.hasNext()) {
            PortletParameters p = (PortletParameters)it.next();
            if (p.getNamespace().equals( namespace )) {
                return p;
            }
        }

        return null;
    }

    public Locale getRealLocale() {
        return realLocale;
    }

    protected String getNamePortletId() {
        return defaultCtx.namePortletId;
    }

    protected Long getPortletId() {
        if (log.isDebugEnabled()){
            if (defaultCtx!=null)
                log.debug("defaultCtx.ctx: " + defaultCtx.ctx);
            else
                log.debug("defaultCtx is null");
        }

        if (defaultCtx==null || defaultCtx.ctx==null)
            return null;

        return defaultCtx.ctx.getIdContext();
    }


    public String getDefaultPortletName() {
        if (log.isDebugEnabled()){
            if (defaultCtx!=null)
                log.debug("defaultCtx.portlet: " + defaultCtx.portlet);
            else
                log.debug("defaultCtx is null");
        }

        if (defaultCtx==null || defaultCtx.portlet==null)
            return null;

        return defaultCtx.portlet.getFullPortletName();
    }

    public String getNameTemplate() {
        return nameTemplate;
    }

    public String getUrlResource() {
        return urlResource;
    }

    public DefaultCtx getDefaultCtx() {
        return defaultCtx;
    }

    protected ContextFactory( ContextFactoryParameter factoryParameter )
        throws PortalException, PortalPersistenceException {

        this.realLocale = ContextLocaleUtils.prepareLocale(factoryParameter);

        Long ctxId = initPortalParameters(factoryParameter);

        if (log.isDebugEnabled()) {
            log.debug("ctxId - "+ctxId);
        }

        if (ctxId==null)
            return;

        defaultCtx = DefaultCtx.getInstance( factoryParameter, ctxId );
        initFromContext(factoryParameter.adapter);

    }

    protected void setPortletInfo( final String nameTemplate ) {
        this.nameTemplate = nameTemplate;
    }

    public static final int UNKNOWN_SERVLET_IDX = 0;
    public static final int PAGEID_SERVLET_IDX = 1;
    public static final int PAGE_SERVLET_IDX = 2;
    public static final int CTX_SERVLET_IDX = 3;
    public static final int URL_SERVLET_IDX = 4;
    private static Map<String, Integer> contextTypeHash = new HashMap<String, Integer>();
    static {
        contextTypeHash.put(ContainerConstants.PAGEID_SERVLET_NAME, PAGEID_SERVLET_IDX);
        contextTypeHash.put(ContainerConstants.PAGE_SERVLET_NAME, PAGE_SERVLET_IDX);
        contextTypeHash.put(ContainerConstants.URI_CTX_MANAGER, CTX_SERVLET_IDX);
        contextTypeHash.put(ContainerConstants.URL_SERVLET_NAME, URL_SERVLET_IDX);
    }

    /**
     * init context type and name of template,
     * if type of context is null, set it to 'index_page'
     */
    public static ContextFactory initTypeContext( final DatabaseAdapter db, final HttpServletRequest request, final PortalInfoImpl portalInfo, final Map<String, Object> httpRequestParameter, final PortletContainer portletContainer )
        throws PortalException, PortalPersistenceException {

        ContextFactory contextFactoryTemp = null;
        ContextFactoryParameter factoryParameter = new ContextFactoryParameter();
        factoryParameter.adapter = db;
        factoryParameter.request = request;
        factoryParameter.portalInfo = portalInfo;
        factoryParameter.httpRequestParameter = httpRequestParameter;
        factoryParameter.portletContainer = portletContainer;


        String servletPath = request.getServletPath();
        Integer idx = contextTypeHash.get(servletPath);
        if (idx!=null) {
            int index = idx;
            switch(index) {
                case PAGEID_SERVLET_IDX:
                    contextFactoryTemp = PageidContextFactory.getInstance(factoryParameter);
                    break;
                case PAGE_SERVLET_IDX:
                    contextFactoryTemp = PageContextFactory.getInstance(factoryParameter);
                    break;
                case CTX_SERVLET_IDX:
                    contextFactoryTemp = CtxContextFactory.getInstance(factoryParameter);
                    break;
                case URL_SERVLET_IDX:
                    contextFactoryTemp = UrlContextFactory.getInstance(factoryParameter);
                    break;
                default:
                    throw new PortalException("Unknown servlet path: "+servletPath);
            }
        }

        if ( log.isDebugEnabled() )
            log.debug( "#1 contextFactoryTemp: "+contextFactoryTemp );

        // unknown servletPath or error processing, process as 'index_page'
        if (contextFactoryTemp==null) {
            contextFactoryTemp = CtxContextFactory.getInstance(factoryParameter);
        }


        if ( log.isDebugEnabled() ) {
            log.debug( "#2 contextFactoryTemp: "+contextFactoryTemp );
            if (contextFactoryTemp!=null) {
                log.debug( "contextFactoryTemp.getDefaultPortletName(): "+contextFactoryTemp.getDefaultPortletName() );
                log.debug( "contextFactoryTemp.getNameTemplate(): "+contextFactoryTemp.getNameTemplate() );
            }
        }

        contextFactoryTemp.prepareParameters(request, httpRequestParameter );

        return contextFactoryTemp;
    }

    protected void initFromContext( final DatabaseAdapter db ) throws PortalException {

        if (defaultCtx.ctx==null)
            return;

        MenuItemInterface menuItem = new MenuItem(db, defaultCtx.ctx);
        setPortletInfo( menuItem.getNameTemplate() );

        if (defaultCtx.siteLang!=null) {
            realLocale = StringTools.getLocale( defaultCtx.siteLang.getCustomLanguage() );
        }
    }
}
