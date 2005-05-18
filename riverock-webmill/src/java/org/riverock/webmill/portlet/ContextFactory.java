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

package org.riverock.webmill.portlet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.exception.FileManagerException;
import org.riverock.interfaces.portlet.menu.MenuItemInterface;
import org.riverock.interfaces.schema.javax.portlet.PortletType;
import org.riverock.webmill.core.GetSiteCtxCatalogItem;
import org.riverock.webmill.core.GetSiteCtxLangCatalogItem;
import org.riverock.webmill.core.GetSiteSupportLanguageItem;
import org.riverock.webmill.core.GetSiteCtxTypeItem;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.exception.PortalPersistenceException;
import org.riverock.webmill.main.Constants;
import org.riverock.webmill.port.PortalInfo;
import org.riverock.webmill.portal.menu.MenuItem;
import org.riverock.webmill.portlet.context.CtxContextFactory;
import org.riverock.webmill.portlet.context.PageContextFactory;
import org.riverock.webmill.portlet.context.PageidContextFactory;
import org.riverock.webmill.portlet.context.UrlContextFactory;
import org.riverock.webmill.schema.core.SiteCtxCatalogItemType;
import org.riverock.webmill.schema.core.SiteCtxLangCatalogItemType;
import org.riverock.webmill.schema.core.SiteSupportLanguageItemType;
import org.riverock.webmill.schema.core.SiteCtxTypeItemType;
import org.riverock.webmill.schema.site.types.TemplateItemTypeTypeType;

import org.apache.log4j.Logger;

/**
 * $Id$
 */
public abstract class ContextFactory {

    private static Logger log = Logger.getLogger( ContextFactory.class );

    public final static class PortletParameters {
        private String namespace = null;
        private Map parameters = null;

        public PortletParameters( final String namespace, final Map parameters ) {
            this.namespace = namespace;
            this.parameters = parameters;
        }

        public String getNamespace() {
            return namespace;
        }

        public Map getParameters() {
            return parameters;
        }
    }


    public final static class DefaultCtx {
        private SiteCtxCatalogItemType ctx = null;
        private SiteCtxLangCatalogItemType langMenu = null;
        private SiteSupportLanguageItemType siteLang = null;
        private PortletType portlet = null;
        private String namePortletId = null;

        private DefaultCtx(){}

        public SiteCtxCatalogItemType getCtx() {
            return ctx;
        }

        public PortletType getPortletDefinition() {
            return portlet;
        }

        public String getNamePortletId() {
            return namePortletId;
        }

        public static DefaultCtx getInstance( final DatabaseAdapter db, final PortalInfo portalInfo, final Long ctxId )
            throws PortalPersistenceException, PortalException, FileManagerException {

            try {
                DefaultCtx defaultCtx = new DefaultCtx();
                defaultCtx.ctx = GetSiteCtxCatalogItem.getInstance(db, ctxId).item;
                if (defaultCtx.ctx==null) {
                    log.error("Context with id "+ctxId+" not found. process as 'index' page");
                    return null;
                }

                defaultCtx.langMenu = GetSiteCtxLangCatalogItem.getInstance(db, defaultCtx.ctx.getIdSiteCtxLangCatalog()).item;
                if (defaultCtx.langMenu==null){
                    log.error("Lang Catalog with id "+defaultCtx.ctx.getIdSiteCtxLangCatalog()+" not found. process as 'index' page");
                    return null;
                }

                defaultCtx.siteLang = GetSiteSupportLanguageItem.getInstance(db, defaultCtx.langMenu.getIdSiteSupportLanguage()).item;
                if (defaultCtx.siteLang==null){
                    log.error("Site language with id "+defaultCtx.langMenu.getIdSiteSupportLanguage()+" not found. process as 'index' page");
                    return null;
                }

                if (log.isDebugEnabled()) {
                    log.debug( "portalInfo: " + portalInfo );
                    if (portalInfo!=null) {
                        log.debug("portalInfo.getSiteId(): "+portalInfo.getSiteId());
                    }
                    log.debug("siteLang: "+defaultCtx.siteLang);
                    if (defaultCtx.siteLang!=null) {
                        log.debug("siteLang.getIdSite(): "+defaultCtx.siteLang.getIdSite());
                    }
                }

                if (!portalInfo.getSiteId().equals(defaultCtx.siteLang.getIdSite())) {
                    log.error("Requested context with id "+defaultCtx.ctx.getIdSiteCtxCatalog()+" is from others site. Process as 'index' page");
                    return null;
                }

                if (defaultCtx.ctx.getIdSiteCtxType()==null) {
                    log.error( "idSiteCtxCatalog: " + defaultCtx.ctx.getIdSiteCtxCatalog() );
                    log.error( "ctxId: " + ctxId );
                    throw new PortalException("contextTypeId is null, unknown portlet");
                }

                SiteCtxTypeItemType ctxType = GetSiteCtxTypeItem.getInstance( db, defaultCtx.ctx.getIdSiteCtxType() ).item;
                if (ctxType==null) {
                    throw new PortalException("portletName for id "+defaultCtx.ctx.getIdSiteCtxType()+" not found");
                }

                initPortletDefinition( defaultCtx, ctxType.getType() );
                return defaultCtx;
            }
            catch (Exception e) {
                log.error("Error", e);
                throw new PortalException("error", e);
            }
        }

        public static DefaultCtx getInstance( final String portletName )
            throws FileManagerException {

            DefaultCtx defaultCtx = new DefaultCtx();
            initPortletDefinition( defaultCtx, portletName );
            return defaultCtx;
        }

        protected static void initPortletDefinition( DefaultCtx defaultCtx, String portletName ) throws FileManagerException {

            defaultCtx.portlet = PortletManager.getPortletDescription( portletName );
            if (defaultCtx.portlet!=null)  {
                defaultCtx.namePortletId =
                    PortletTools.getStringParam(defaultCtx.portlet, PortletTools.name_portlet_id);
            }
        }
    }



//    protected Long portletId = null;
//    protected String portletType = null;
    protected String nameTemplate = null;
    protected Locale realLocale = null;
    protected PortalInfo portalInfo = null;
//    protected String namePortletId = null;
    protected Map dynamicParameter = null;
    protected List portletsParameter = new LinkedList();
    protected String urlResource = null;
//    protected SiteCtxCatalogItemType defaultCtx = null;
    protected DefaultCtx defaultCtx = null;

    protected abstract Long initPortalParameters( final DatabaseAdapter db, final HttpServletRequest request) throws PortalException;
    protected abstract void prepareParameters( final HttpServletRequest httpRequest, final Map httpRequestParameter ) throws PortalException;

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


    public String getDefaultPortletType() {
        if (log.isDebugEnabled()){
            if (defaultCtx!=null)
                log.debug("defaultCtx.portlet: " + defaultCtx.portlet);
            else
                log.debug("defaultCtx is null");
        }

        if (defaultCtx==null || defaultCtx.portlet==null)
            return null;

        return defaultCtx.portlet.getPortletName().getContent();
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

    protected ContextFactory( final DatabaseAdapter adapter, final HttpServletRequest request, final PortalInfo portalInfo )
        throws PortalException, PortalPersistenceException {

        this.portalInfo = portalInfo;
        this.realLocale = ContextLocaleUtils.prepareLocale(adapter, request, portalInfo);

        try {
            Long ctxId = initPortalParameters(adapter, request);

            if (log.isDebugEnabled()) {
                log.debug("ctxId - "+ctxId);
            }

            if (ctxId==null)
                return;

            defaultCtx = DefaultCtx.getInstance( adapter, portalInfo, ctxId );
            initFromContext(adapter);

        } catch (FileManagerException e) {
            String es = "Error get portlet definition";
            log.error( es, e );
            throw new PortalException( es, e );
        }
    }

    protected void setPortletInfo( final String nameTemplate ) {
        this.nameTemplate = nameTemplate;
    }

    public static final int UNKNOWN_SERVLET_IDX = 0;
    public static final int PAGEID_SERVLET_IDX = 1;
    public static final int PAGE_SERVLET_IDX = 2;
    public static final int CTX_SERVLET_IDX = 3;
    public static final int URL_SERVLET_IDX = 4;
    private static Map contextTypeHash = new HashMap();
    static {
        contextTypeHash.put(Constants.PAGEID_SERVLET_NAME, new Integer(PAGEID_SERVLET_IDX));
        contextTypeHash.put(Constants.PAGE_SERVLET_NAME, new Integer(PAGE_SERVLET_IDX));
        contextTypeHash.put(Constants.CTX_SERVLET_NAME, new Integer(CTX_SERVLET_IDX));
        contextTypeHash.put(Constants.URL_SERVLET_NAME, new Integer(URL_SERVLET_IDX));
    }

    /**
     * init context type and name of template,
     * if type of context is null, set it to 'index_page'
     */
    public static ContextFactory initTypeContext( final DatabaseAdapter db, final HttpServletRequest request, final PortalInfo portalInfo, final Map httpRequestParameter )
        throws PortalException, PortalPersistenceException {

        ContextFactory contextFactoryTemp = null;

        String servletPath = request.getServletPath();
        Integer idx = (Integer)contextTypeHash.get(servletPath);
        if (idx!=null) {
            switch(idx.intValue()) {
                case PAGEID_SERVLET_IDX:
                    contextFactoryTemp = PageidContextFactory.getInstance(db, request, portalInfo);
                    break;
                case PAGE_SERVLET_IDX:
                    contextFactoryTemp = PageContextFactory.getInstance(db, request, portalInfo);
                    break;
                case CTX_SERVLET_IDX:
                    contextFactoryTemp = CtxContextFactory.getInstance(db, request, portalInfo);
                    break;
                case URL_SERVLET_IDX:
                    contextFactoryTemp = UrlContextFactory.getInstance(db, request, portalInfo);
                    break;
                default:
                    throw new PortalException("Unknown servlet path: "+servletPath);
            }
        }

        if ( log.isDebugEnabled() )
            log.debug( "#1 contextFactoryTemp: "+contextFactoryTemp );

        // unknown servletPath or error processing, process as 'index_page'
        if (contextFactoryTemp==null) {
            contextFactoryTemp = CtxContextFactory.getInstance(db, request, portalInfo);
        }


        if ( log.isDebugEnabled() ) {
            log.debug( "#2 contextFactoryTemp: "+contextFactoryTemp );
            if (contextFactoryTemp!=null) {
                log.debug( "contextFactoryTemp.getDefaultPortletType(): "+contextFactoryTemp.getDefaultPortletType() );
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
