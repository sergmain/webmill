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
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.template.PortalTemplateItemType;
import org.riverock.interfaces.portlet.menu.MenuItem;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.container.portlet.PortletContainerException;
import org.riverock.webmill.container.portlet.PortletEntry;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.portal.bean.CatalogBean;
import org.riverock.webmill.portal.bean.CatalogLanguageBean;
import org.riverock.webmill.portal.bean.PortletNameBean;
import org.riverock.webmill.portal.bean.SiteLanguageBean;
import org.riverock.webmill.portal.context.*;
import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.webmill.portal.menu.PortalMenuItem;

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
        private HttpServletRequest request = null;
        private PortalInfo portalInfo = null;
        private PortletContainer portletContainer = null;
        private Map<String, Object> httpRequestParameter = null;

        public HttpServletRequest getRequest() {
            return request;
        }

        public PortalInfo getPortalInfo() {
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
        private CatalogBean ctx = null;
        private CatalogLanguageBean langMenu = null;
        private SiteLanguageBean siteLang = null;
        private PortletDefinition portlet = null;
        private String namePortletId = null;

        private DefaultCtx(){}

        public CatalogBean getCtx() {
            return ctx;
        }

        public PortletDefinition getPortletDefinition_() {
            return portlet;
        }

        public String getNamePortletId_() {
            return namePortletId;
        }

        public static DefaultCtx getInstance( ContextFactoryParameter factoryParameter, final Long ctxId ) {

                DefaultCtx defaultCtx = new DefaultCtx();
                defaultCtx.ctx = InternalDaoFactory.getInternalDao().getCatalogBean( ctxId );

                if (defaultCtx.ctx==null) {
                    log.error("Catalog record for id "+ctxId+" not found. process as 'index' page");
                    return null;
                }

                defaultCtx.langMenu = InternalDaoFactory.getInternalDao().getCatalogLanguageBean(
                    defaultCtx.ctx.getCatalogLanguageId());

                if (defaultCtx.langMenu==null){
                    log.error("Lang Catalog with id "+defaultCtx.ctx.getCatalogLanguageId()+" not found. process as 'index' page");
                    return null;
                }

                defaultCtx.siteLang = InternalDaoFactory.getInternalDao().getSiteLanguageBean(defaultCtx.langMenu.getSiteLanguageId());

                if (defaultCtx.siteLang==null){
                    log.error("Site language with id "+defaultCtx.langMenu.getSiteLanguageId()+" not found. process as 'index' page");
                    return null;
                }

                if (log.isDebugEnabled()) {
                    log.debug( "portalInfo: " + factoryParameter.portalInfo );
                    if (factoryParameter.portalInfo!=null) {
                        log.debug("portalInfo.getSiteId(): "+factoryParameter.portalInfo.getSiteId());
                    }
                    log.debug("siteLang: "+defaultCtx.siteLang);
                    if (defaultCtx.siteLang!=null) {
                        log.debug("siteLang.getIdSite(): "+defaultCtx.siteLang.getSiteId());
                    }
                }

                if (!factoryParameter.portalInfo.getSiteId().equals(defaultCtx.siteLang.getSiteId())) {
                    log.error("Requested context with id "+defaultCtx.ctx.getCatalogId()+" is from others site. Process as 'index' page");
                    return null;
                }

                if (defaultCtx.ctx.getPortletId()==null) {
                    log.error( "defaultCtx.ctx.getPortletId() is null, unknown portlet");
                    log.error( "idSiteCtxCatalog: " + defaultCtx.ctx.getCatalogId() );
                    log.error( "ctxId: " + ctxId );
                    return null;
                }

                PortletNameBean portletNameBean = InternalDaoFactory.getInternalDao().getPortletNameBean( defaultCtx.ctx.getPortletId() );
                if (portletNameBean.getName()==null) {
                    log.error("portletName for id "+defaultCtx.ctx.getPortletId()+" not found");
                    return null;
                }

                initPortletDefinition( factoryParameter, defaultCtx, portletNameBean.getName() );
                return defaultCtx;
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
    protected RequestState requestState = new RequestState();

    public RequestState getRequestState() {
        return requestState;
    }

    protected abstract Long initPortalParameters( ContextFactoryParameter factoryParameter);
    protected abstract void prepareParameters( final HttpServletRequest httpRequest, final Map<String, Object> httpRequestParameter );

    public PortletParameters getParameters( final String namespace, final PortalTemplateItemType type ) {
        if (type!=null && type.getType()==PortalTemplateItemType.DYNAMIC_TYPE) {
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

        return defaultCtx.ctx.getContextId();
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

    protected ContextFactory( ContextFactoryParameter factoryParameter ) {

        this.realLocale = ContextLocaleUtils.prepareLocale(factoryParameter);

        Long ctxId = initPortalParameters(factoryParameter);

        if (log.isDebugEnabled()) {
            log.debug("ctxId - "+ctxId);
        }

        if (ctxId==null)
            return;

        defaultCtx = DefaultCtx.getInstance( factoryParameter, ctxId );
        initFromContext();

    }

    protected void setTemplateName( final String nameTemplate ) {
        this.nameTemplate = nameTemplate;
    }

    public static final int UNKNOWN_SERVLET_IDX = 0;
    public static final int PAGEID_SERVLET_IDX = 1;
    public static final int PAGE_SERVLET_IDX = 2;
    public static final int CTX_SERVLET_IDX = 3;
    private static Map<String, Integer> contextTypeHash = new HashMap<String, Integer>();
    static {
        contextTypeHash.put(ContainerConstants.PAGEID_SERVLET_NAME, PAGEID_SERVLET_IDX);
        contextTypeHash.put(ContainerConstants.PAGE_SERVLET_NAME, PAGE_SERVLET_IDX);
        contextTypeHash.put(ContainerConstants.URI_CTX_MANAGER, CTX_SERVLET_IDX);
    }

    /**
     * init context type and name of template,
     * if type of context is null, set it to 'index_page'
     */
    public static ContextFactory initTypeContext( final HttpServletRequest request, final PortalInfo portalInfo, final Map<String, Object> httpRequestParameter, final PortletContainer portletContainer ) {

        ContextFactory contextFactoryTemp = null;
        ContextFactoryParameter factoryParameter = new ContextFactoryParameter();
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
                default:
                    throw new IllegalStateException("Unknown servlet path: "+servletPath+". Check servelt mapping in WEB-INF/web.xml file");
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

    protected void initFromContext() {

        if (defaultCtx.ctx==null)
            return;

        MenuItem menuItem = new PortalMenuItem( defaultCtx.ctx );
        setTemplateName( menuItem.getNameTemplate() );

        if (defaultCtx.siteLang!=null) {
            realLocale = StringTools.getLocale( defaultCtx.siteLang.getCustomLanguage() );
        }
    }
}
