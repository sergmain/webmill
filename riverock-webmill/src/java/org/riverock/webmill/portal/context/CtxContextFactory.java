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
package org.riverock.webmill.portal.context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;

import org.riverock.common.collections.MapWithParameters;
import org.riverock.common.tools.SimpleStringTokenizer;
import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portlet.menu.MenuLanguage;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.portal.ContextFactory;
import org.riverock.webmill.portal.namespace.Namespace;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * $Id$
 */
public final class CtxContextFactory extends ContextFactory {
    private final static Logger log = Logger.getLogger(CtxContextFactory.class);

    // add for compatible with jsr168 TCK
    private static final String INVOKE_PORTLET_NAME = "portletName";

    private CtxContextFactory(ContextFactoryParameter factoryParameter) {
        super(factoryParameter);
    }

    public static ContextFactory getInstance(ContextFactoryParameter factoryParameter) {
        CtxContextFactory factory = new CtxContextFactory(factoryParameter);
        if (factory.getDefaultCtx()!=null) {
            return factory;
        }

        // If not found context, processing as 'index_page'
        MenuLanguage menu = factoryParameter.getPortalInfo().getMenu(factory.realLocale.toString());
        if (menu==null){
            log.error( "menu is null, locale: "+factory.realLocale.toString() );
            return factory;
        }

        if (menu == null || menu.getIndexMenuItem() == null) {
            log.warn("menu: " + menu);
            log.warn("locale: " + factory.realLocale.toString());
            if (menu != null) {
                log.warn("menu.getIndexMenuItem(): " + menu.getIndexMenuItem());
                if (menu.getIndexMenuItem() != null) {
                    log.warn("menu.getIndexMenuItem().getId(): " + menu.getIndexMenuItem().getId());
                }
                else {
                    log.warn("menu.getIndexMenuItem() is null");
                }
            }
            else {
                log.warn("menu is null");
            }
            return factory;
        }

        factory.defaultCtx = DefaultCtx.getInstance(factoryParameter, menu.getIndexMenuItem().getId());
        factory.initFromContext();
        factory.setTemplateName(menu.getIndexMenuItem().getNameTemplate());

        return factory;
    }


        private static final String requestFormat = 
		"<PORTAL_CONTEXT>/ctx/<LOCALE>,<TEMPLATE_NAME>,[PORTLET_NAME],[REQUEST_STATE]/<PARAMETERS_OF_OTHER_PORTLETS>/ctx?";
	// REQUEST_STATE: a-actionRequest, in other case-renderRequest

    public static StringBuilder encodeUrl( final PortletRequest portletRequest, final String portletName, final String templateName, Locale locale, boolean isActionReqeust, Namespace namespace ) {
        StringBuilder b = new StringBuilder();
        String portalContextPath = portletRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PORTAL_CONTEXT_PATH );
        if (portalContextPath.equals("/") || portalContextPath.equals("") )
            b.append( ContainerConstants.URI_CTX_MANAGER );
        else
            b.append( portalContextPath ).append( ContainerConstants.URI_CTX_MANAGER );

        b.append( '/' ).append( locale.toString() );
        b.append( ',' );
        if (templateName!=null)
            b.append( templateName );

        b.append( ',' );
        if (portletName!=null)
            b.append( portletName );

        b.append( ',' );
        if (isActionReqeust) 
            b.append('a');
        else
            b.append('r');
        b.append( ',' );
        b.append( namespace.getNamespace() );

        b.append( "/ctx" );

        return b;
    }

    protected Long initPortalParameters(ContextFactoryParameter factoryParameter) {

        log.debug("Start process as page, format request: "+ requestFormat );

        String path = factoryParameter.getRequest().getPathInfo();
        if (path == null || path.equals("/")) {
            return null;
        }

        if (log.isDebugEnabled()) log.debug("path: " + path);

        int idxSlash = path.indexOf('/', 1);
        if (log.isDebugEnabled()) log.debug("idxSlash: " + idxSlash);
        if (idxSlash == -1)
            return null;

        String localeFromUrl = null;
        localeFromUrl = path.substring(1, idxSlash);
        StringTokenizer st = new StringTokenizer(localeFromUrl, ",", false);
        if (log.isDebugEnabled()) {
            log.debug("st.countTokens(): " + st.countTokens());
        }

        if (st.countTokens() < 2)
            return null;


        realLocale = StringTools.getLocale(st.nextToken());
        if (log.isDebugEnabled()) {
            log.debug("token with locale: " + realLocale);
        }

        String localeNameTemp = factoryParameter.getRequest().getParameter(ContainerConstants.NAME_LANG_PARAM);
        if (localeNameTemp != null) {
            realLocale = StringTools.getLocale(localeNameTemp);
        }

        if (log.isDebugEnabled()) {
            log.debug("real locale: " + realLocale);
        }


        String templateName = st.nextToken();
        setTemplateName(templateName);

        String portletName = null;
        if (st.hasMoreTokens()) {
            portletName = st.nextToken();
        }

        if (StringTools.isEmpty(portletName)) {
            portletName = factoryParameter.getRequest().getParameter(ContainerConstants.NAME_TYPE_CONTEXT_PARAM);
            if (portletName == null) {
                portletName = factoryParameter.getRequest().getParameter(INVOKE_PORTLET_NAME);
            }
        }

        if (st.hasMoreElements() ) {
            String state = st.nextToken();
            if (state!=null && state.equalsIgnoreCase("a") )
                getRequestState().setActionRequest( true );
        }
        if (st.hasMoreElements() ) {
            String ns = st.nextToken();
            if (!StringTools.isEmpty(ns)) {
                activeNamespace = ns;
            }
        }

        // Todo parse request for parameters of others portlets

//        PortletDefinition portlet = null;
//        try {
//            portlet = PortletManager.getPortletDescription( portletName );
//        } catch (FileManagerException e) {
//            log.error("Error getPortletDescription for type "+portletName);
//        }
//        String namePortletId = null;
//        if (portlet!=null){
//            namePortletId = PortletService.getStringParam(portlet, ContainerConstants.name_portlet_id);
//        }
//
//        Long id = null;
//        if (namePortletId!=null)
//            id = ServletTools.getLong(request, namePortletId);

        if (log.isDebugEnabled()) {
            log.debug("realLocale: " + realLocale);
            log.debug("templateName: " + templateName);
            log.debug("portletName: " + portletName);
        }

        Long ctxId = InternalDaoFactory.getInternalDao().getCatalogId(factoryParameter.getPortalInfo().getSiteId(), realLocale, portletName, templateName);
        if (ctxId != null) {
            return ctxId;
        }

        defaultCtx = DefaultCtx.getInstance(factoryParameter, portletName);
        return null;
    }

    protected void prepareParameters( final HttpServletRequest httpRequest, final Map<String, Object> httpRequestParameter ) {
        dynamicParameter = httpRequestParameter;

        if ( log.isDebugEnabled() ) {
            log.debug( "dynamicParameter: "+dynamicParameter );
            if ( dynamicParameter!=null ) {
                Iterator it = dynamicParameter.keySet().iterator();
                while (it.hasNext()) {
                    String key = (String)it.next();
                    Object obj = dynamicParameter.get( key );
                    if (obj instanceof List) {
                        log.debug( "Parameter, key: "+key );
                        Iterator ii = ((List)obj).iterator();
                        while (ii.hasNext()) {
                            log.debug( "               value: "+ii.next() );
                        }
                    }
                    else {
                        log.debug( "Parameter, key: "+key+", value: "+obj.toString() );
                    }
                }
            }
        }

        String s = httpRequest.getServletPath();
        int idx = s.lastIndexOf( '/' );
        if (idx==-1)
            return;

        s = s.substring( 0, idx );

        String namespace = null;
        StringTokenizer st = new StringTokenizer( s, "/", false );

        while ( st.hasMoreTokens() ) {
            String element = st.nextToken();

            SimpleStringTokenizer p = new SimpleStringTokenizer( element, new String[]{",,"} );
            // get portlet namespace
            if ( p.hasMoreTokens() )
                namespace = p.nextToken();

            Map<String, Object> param = new HashMap<String, Object>();
            while ( p.hasMoreTokens() ) {
                String temp = p.nextToken();

                idx = temp.indexOf( ',' );
                if (idx==-1)
                    continue;

                String key = temp.substring( 0, idx );
                String value = temp.substring( idx+1 );

                MapWithParameters.put( param, key, value );
            }
            PortletParameters pp = new PortletParameters( namespace, param );
            portletsParameter.add( pp );
        }
    }
}
