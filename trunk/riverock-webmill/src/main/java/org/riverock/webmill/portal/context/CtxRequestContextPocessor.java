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
package org.riverock.webmill.portal.context;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.common.collections.MapWithParameters;
import org.riverock.common.tools.SimpleStringTokenizer;
import org.riverock.common.tools.StringTools;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.portal.PortletParameters;
import org.riverock.webmill.portal.bean.ExtendedCatalogItemBean;
import org.riverock.webmill.portal.dao.InternalDaoFactory;
import org.riverock.webmill.portal.namespace.Namespace;

/**
 * $Id$
 */
public final class CtxRequestContextPocessor implements RequestContextProcessor {
    private final static Logger log = Logger.getLogger(CtxRequestContextPocessor.class);

    // add for compatible with jsr168 TCK
    private static final String INVOKE_PORTLET_NAME = "portletName";

    public CtxRequestContextPocessor() {
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

    public RequestContext parseRequest(RequestContextParameter factoryParameter) {

        log.debug("Start process as page, format request: "+ requestFormat );

        RequestContext bean = new RequestContext();

        String path = factoryParameter.getRequest().getPathInfo();
        if (path == null || path.equals("/")) {
            return null;
        }

        if (log.isDebugEnabled()) log.debug("path: " + path);

        int idxSlash = path.indexOf('/', 1);
        if (log.isDebugEnabled()) log.debug("idxSlash: " + idxSlash);
        if (idxSlash == -1)
            return null;

        String localeFromUrl = path.substring(1, idxSlash);
        StringTokenizer st = new StringTokenizer(localeFromUrl, ",", false);
        if (log.isDebugEnabled()) {
            log.debug("st.countTokens(): " + st.countTokens());
        }

        if (st.countTokens() < 2)
            return null;

        // init locale
        Locale locale = StringTools.getLocale(st.nextToken());
        if (log.isDebugEnabled()) {
            log.debug("token with locale: " + locale);
        }

        String localeNameTemp = factoryParameter.getRequest().getParameter(ContainerConstants.NAME_LANG_PARAM);
        if (localeNameTemp != null) {
            locale = StringTools.getLocale(localeNameTemp);
        }
        bean.setLocale( locale );
        if (log.isDebugEnabled()) {
            log.debug("real locale: " + locale);
        }

        // init template name
        bean.setTemplateName( st.nextToken() );

        // init portlet name
        String portletName = null;
        if (st.hasMoreTokens()) {
            portletName = st.nextToken();
        }

        if (log.isDebugEnabled()) {
            log.debug("portletName");
            log.debug("     portletName from path: " + portletName);
            log.debug("     ContainerConstants.NAME_TYPE_CONTEXT_PARAM: " + factoryParameter.getRequest().getParameter(ContainerConstants.NAME_TYPE_CONTEXT_PARAM));
            log.debug("     portletName for compatible with TCK: " + factoryParameter.getRequest().getParameter(INVOKE_PORTLET_NAME));
        }

        // In 1st case we get portlet name from 'portletName' parameter. For compatible with TCK
        // in 2nd case we get portlet name from 'mill.context' parameter.
        //    This extension allow us create URL to other portlet
        // In last case we get portlet name from URI
        String pn = factoryParameter.getRequest().getParameter(INVOKE_PORTLET_NAME);
        if (pn==null) {
            pn = factoryParameter.getRequest().getParameter(ContainerConstants.NAME_TYPE_CONTEXT_PARAM);
        }

        if (pn!=null) {
            portletName = pn;
        }

        if ( portletName!=null && portletName.indexOf( PortletContainer.PORTLET_ID_NAME_SEPARATOR )==-1 ) {
            portletName = PortletContainer.PORTLET_ID_NAME_SEPARATOR + portletName;
        }

        if (log.isDebugEnabled()) {
            log.debug("Final portletName: " + portletName);
        }
        bean.setDefaultPortletName( portletName );

        // Init request state: action/render, windows state, portlet mode
        RequestState requestState = new RequestState();
        if (st.hasMoreElements() ) {
            String state = st.nextToken();
            if (state!=null && state.equalsIgnoreCase("a") )
                requestState.setActionRequest( true );
        }
        bean.setDefaultRequestState( requestState );

        // set namespace of current(active) portlet
        if (st.hasMoreElements() ) {
            String ns = st.nextToken();
            if (StringUtils.isNotBlank(ns)) {
                bean.setDefaultNamespace( ns );
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Result of parsing path");
            log.debug("     path: " + path );
            log.debug("     locale: " + bean.getLocale().toString() );
            log.debug("     templateName: " + bean.getTemplateName());
            log.debug("     portletName: " + bean.getDefaultPortletName() );
            log.debug("     isAction: " + requestState.isActionRequest() );
            log.debug("     default ns: " + bean.getDefaultNamespace() );
        }

        if (bean.getDefaultPortletName()==null) {
            return null;
        }

        Long ctxId = InternalDaoFactory.getInternalCatalogDao().getCatalogItemId(factoryParameter.getPortalInfo().getSiteId(), bean.getLocale(), bean.getDefaultPortletName(), bean.getTemplateName());
        if (log.isDebugEnabled()) {
            log.debug("ctxId: " + ctxId );
        }
        ExtendedCatalogItemBean extendedBean = ExtendedCatalogItemBean.getInstance(factoryParameter, ctxId);
        if (extendedBean==null) {
            extendedBean = ExtendedCatalogItemBean.getInstance(factoryParameter, bean.getTemplateName(), bean.getDefaultPortletName(), bean.getLocale());
        }
        if (extendedBean==null) {
            return null;
        }
        bean.setExtendedCatalogItem( extendedBean );

        RequestContextUtils.initParametersMap(bean, factoryParameter);

        // prepare parameters for others portlets
        prepareParameters( bean.getParameters(), factoryParameter.getRequest().getServletPath() );
        return bean;
    }

    /**
     *  prepare parameters for others portlets
     *
     * @param map
     * @param s - HttpServletRequest.getServletPath
     */
    private void prepareParameters( Map<String, PortletParameters> map, String s ) {

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

            Map<String, List<String>> param = new HashMap<String, List<String>>();
            while ( p.hasMoreTokens() ) {
                String temp = p.nextToken();

                idx = temp.indexOf( ',' );
                if (idx==-1)
                    continue;

                String key = temp.substring( 0, idx );
                String value = temp.substring( idx+1 );

                MapWithParameters.putInStringList( param, key, value );
            }

            //todo implement request state for all portlets
            RequestState requestState = new RequestState();
            PortletParameters pp = new PortletParameters( namespace, requestState, param );
            map.put( namespace, pp );
        }
    }
}
