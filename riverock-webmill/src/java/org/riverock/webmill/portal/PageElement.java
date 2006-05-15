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

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.common.tools.MainTools;
import org.riverock.interfaces.portal.template.PortalTemplateItem;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.bean.SitePortletData;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.container.portlet.PortletEntry;
import org.riverock.webmill.container.portlet.bean.SecurityRoleRef;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.portal.impl.ActionRequestImpl;
import org.riverock.webmill.portal.impl.ActionResponseImpl;
import org.riverock.webmill.portal.impl.RenderRequestImpl;
import org.riverock.webmill.portal.impl.RenderResponseImpl;
import org.riverock.webmill.portal.namespace.Namespace;
import org.riverock.webmill.utils.PortletUtils;

/**
 * User: SergeMaslyukov
 * Date: 09.01.2005
 * Time: 19:29:20
 * $Id$
 */
public final class PageElement {
    private final static Logger log = Logger.getLogger( PageElement.class );

    private static final String ACCESS_DISABLED_FOR_PORTLET = "Access disabled in portlet.xml";

    private Throwable exception = null;
    private String errorString = null;

    private ActionRequestImpl actionRequest = null;
    private ActionResponseImpl actionResponse = null;
    private RenderRequestImpl renderRequest = null;
    private RenderResponseImpl renderResponse = null;
    private SitePortletData data = null;

    private PortletEntry portletEntry = null;
    private PortalTemplateItem portalTemplateItem = null;
//    private boolean isUrl = false;
    private boolean isXml = false;
    private PortletParameters parameters = null;
    private boolean isRedirected = false;
    private String redirectUrl = null;
    private boolean isAccessPermit = true;
    private Namespace namespace = null;

    /**
     * renderParameter used for set parameters in action
     */
    private Map<String, List<String>> renderParameters = new HashMap<String, List<String>>();
    private PortletContainer portletContainer = null;

    public PageElement(PortletContainer portletContainer, Namespace namespace,
                       PortalTemplateItem portalTemplateItem, PortletParameters portletParameters
    ) {
        this.portletContainer = portletContainer;
        this.namespace = namespace;
        this.portalTemplateItem = portalTemplateItem;
        this.parameters = portletParameters;

    }

    public void destroy() {
        exception = null;
        errorString = null;

        actionRequest = null;
        if ( actionResponse!=null ) {
            actionResponse.destroy();
            actionResponse = null;
        }
        if ( renderRequest!=null ) {
            renderRequest.destroy();
            renderRequest = null;
        }
        if ( renderResponse!=null ) {
            renderResponse.destroy();
            renderResponse = null;
        }
        data = null;
        portletEntry = null;
        portalTemplateItem = null;
        parameters = null;
        redirectUrl = null;
        if (log.isDebugEnabled()) {
            log.debug("#13.1");
        }
        if ( renderParameters!=null ) {
            renderParameters.clear();
            renderParameters = null;
        }
        namespace = null;
        if (log.isDebugEnabled()) {
            log.debug("#13.2");
        }
    }

    void processActionPortlet() {

//        if (isUrl || exception!=null || errorString!=null) {
        if (exception!=null || errorString!=null) {
            if ( log.isDebugEnabled() ) {
//                log.debug("isUrl" + isUrl + ", exception: " + exception + ", errorString: "+ errorString );
                log.debug("exception: " + exception + ", errorString: "+ errorString +", isAccessPermit: " + isAccessPermit );
            }
            return;
        }
        try {
            ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
            try {
                final ClassLoader classLoader = portletEntry.getClassLoader();
                Thread.currentThread().setContextClassLoader( classLoader );

                portletEntry.getPortlet().processAction( actionRequest, actionResponse );
            }
            finally {
                Thread.currentThread().setContextClassLoader( oldLoader );
            }

            if ( actionResponse.getIsRedirected() ) {
                isRedirected = actionResponse.getIsRedirected();
                redirectUrl = actionResponse.getRedirectUrl();
            }
        }
        catch( Throwable e ) {
            final String notImpl = "processAction method not implemented";

            if ( e.getMessage()==null || !e.getMessage().contains(notImpl) ) {
                log.error("Exception: ", e);
	    }
        }
    }

    void renderPortlet() {

        if (exception!=null || errorString!=null) {
            if ( log.isDebugEnabled() ) {
                log.debug("exception: " + exception + ", errorString: "+ errorString );
            }
            return;
        }

        try {

            SitePortletData cacheData = portletContainer.getContentCache().getContent( portletEntry.getPortletDefinition() );
            if (cacheData != null) {
                data = cacheData;
                return;
            }

            if ( log.isDebugEnabled() ) {
                log.debug( "Start render portlet '" + portletEntry.getPortletDefinition().getPortletName() + "'" );
                log.debug( "portlet request: " + renderRequest + ", response: " + renderResponse );
                log.debug( "portlet code: "+renderRequest.getAttribute(ContainerConstants.PORTAL_PORTLET_CODE_ATTRIBUTE ) );
                log.debug( "portlet xml root: "+renderRequest.getAttribute(ContainerConstants.PORTAL_PORTLET_XML_ROOT_ATTRIBUTE ) );
                log.debug("#10.1");
            }

            ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
            try {
                final ClassLoader classLoader = portletEntry.getClassLoader();
                Thread.currentThread().setContextClassLoader(classLoader);

//                if (!isUrl) {
                    portletEntry.getPortlet().render(renderRequest, renderResponse);
//                }
//                else {
//
//                    RequestDispatcher rd = portletEntry.getServletConfig().getServletContext().getRequestDispatcher(portletEntry.getPortletDefinition().getPortletClass());
//
//                    if (log.isDebugEnabled()) {
//                        log.debug("#91.1");
//                        log.debug("process url: " + portletEntry.getPortletDefinition().getPortletClass());
//                        log.debug("ServletContext: " + portletEntry.getServletConfig().getServletContext());
//                        log.debug("RequestDispatcher: " + rd);
//                    }
//
//                    PortletRequestDispatcher dispatcher = new PortletRequestDispatcherImpl(rd);
//
//                    if (log.isDebugEnabled()) {
//                        log.debug("#91.4");
//                    }
//                    dispatcher.include(renderRequest, renderResponse);
//
//                    if (log.isDebugEnabled()) {
//                        log.debug("#91.5");
//                    }
//                }

            }
            finally {
                Thread.currentThread().setContextClassLoader(oldLoader);
            }

            renderResponse.flushBuffer();

            if (log.isDebugEnabled()) {
                log.debug("#10.2");
            }

            if ( renderResponse.getIsRedirected() ) {
                isRedirected = renderResponse.getIsRedirected();
                redirectUrl = renderResponse.getRedirectUrl();
            }

            byte portletBytes[] = renderResponse.getBytes();
            if ( log.isDebugEnabled() ){
                log.debug( "Portlet object successfull created" );
                log.debug( "isXml() - "+isXml );

                if ( isXml )
                    log.debug( "XmlRoot - "+portalTemplateItem.getXmlRoot() );

                log.debug( "portletBytes - " + portletBytes );
            }

            if ( isXml ) {
                // write all without XML header - <? ..... ?>
                int idx = MainTools.indexOf( portletBytes, (byte)'>' );
                if ( idx==-1 ){
                    final String es = "Array of bytes with xml'ized data is wrong - not start with <?xml ...?> ";
                    log.error(es);
                    errorString = es;
                    return;
                }
                else
                    data = PortalRequestProcessor.setData(
                        MainTools.getBytes( portletBytes, idx+1 ), false, true );
            }
            else {
                data = PortalRequestProcessor.setData( portletBytes, false, false );
            }

            // cache content
            portletContainer.getContentCache().setContent( portletEntry.getPortletDefinition(), data, renderRequest );
        }
        catch( javax.portlet.UnavailableException ue ) {
            PortletContainer.destroy( portletEntry.getPortletDefinition().getPortletName() );
            errorString = portletUnavailable(portletEntry.getPortletDefinition().getPortletName());
            log.error( errorString, ue );
        }
        catch( java.lang.ThreadDeath e) {
            errorString = portletUnavailable(portletEntry.getPortletDefinition().getPortletName());
            exception = e;
            log.error(errorString, e);
        }
        catch ( Throwable e ) {
            errorString = portletUnavailable(portletEntry.getPortletDefinition().getPortletName());
            log.error( errorString, e );
        }
    }

    void initPortlet( final String portletName,  final PortalRequestInstance portalRequestInstance, Map<String, String> portletMetadata, List<String> roleList ) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("portalContext: " + portalRequestInstance.getPortalContext() );
                log.debug("Start init page element. Portlet name: '" + portletName + "'");
            }

            portletEntry = portletContainer.getPortletInstance(portletName);

            if (portletEntry == null) {
                errorString = portletUnavailable(portletName);
                return;
            }

            if ( portletEntry.getIsPermanent() ) {
                log.error( "portlet permanent unavailable, message: " + portletEntry.getExceptionMessage() );
                errorString = portletUnavailable(portletName);
                return;
            }

            if ( portletEntry.getIsWait() ) {
                log.error( "portlet permanent unavailable for "+portletEntry.getInterval()+" seconds");
                errorString = portletUnavailable(portletName);
                return;
            }

            if (log.isDebugEnabled()) {
                log.debug("portletDefinition: " + portletEntry.getPortletDefinition());
            }

            if (portletEntry.getPortletDefinition() == null) {
                errorString = "Definition for portlet '" + portletName + "' not found.";
                return;
            }

            isXml = PortletService.getBooleanParam(portletEntry.getPortletDefinition(), ContainerConstants.is_xml, Boolean.FALSE);
//            isUrl = PortletService.getBooleanParam(portletEntry.getPortletDefinition(), ContainerConstants.is_url, Boolean.FALSE);

//            if (!isUrl) {
                if (log.isDebugEnabled())
                    log.debug("Start create instance of portlet '" + portletName + "'");

                portletEntry = portletContainer.getPortletInstance(portletName);

                if (portletEntry.getIsPermanent()) {
                    errorString = "Portlet '" + portletName + "' permanently unavailable.";
                }
                else if (portletEntry.getInterval() > 0) {
                    errorString = "Portlet '" + portletName + "' unavailable for " + portletEntry.getInterval() + " seconds.";
                }
                if (portletEntry.getPortlet() == null) {
                    errorString = "Portlet '" + portletName + "' not created for unknown reason.";
                }
//            }

            if (log.isDebugEnabled()) {
                log.debug("Error message of create portlet instance: " + errorString);
            }

            String contextPath = getContextPath(portalRequestInstance);

            Map<String, List<String>> renderRequestParamMap = new HashMap<String, List<String>>();

            if (parameters != null && parameters.getParameters() != null) {
                // The portlet-container must not propagate parameters received
                // in an action request to subsequent render requests of the portlet.
                if ( !parameters.getRequestState().isActionRequest() ) {
                    renderRequestParamMap.putAll(parameters.getParameters());
                }
                else {
                    log.debug("Request is action. Do not send parameters to render request");
                }
            }

            renderRequest = new RenderRequestImpl(
                renderRequestParamMap,
                portalRequestInstance,
                renderParameters,
                portletEntry.getServletConfig().getServletContext(),
                contextPath,
                portletEntry.getPortletDefinition().getPortletPreferences(),
                portletEntry.getPortletProperties(),
                portalRequestInstance.getPortalContext(),
                portletEntry.getPortletConfig().getPortletContext(),
                portletEntry.getPortletDefinition(),
                namespace
            );

            // portlet metadata
            renderRequest.setAttribute(ContainerConstants.PORTAL_PORTLET_METADATA_ATTRIBUTE, portletMetadata);
            // set portlet specific attribute
            renderRequest.setAttribute(ContainerConstants.PORTAL_PORTLET_CODE_ATTRIBUTE, portalTemplateItem.getCode());
            renderRequest.setAttribute(ContainerConstants.PORTAL_PORTLET_XML_ROOT_ATTRIBUTE, portalTemplateItem.getXmlRoot());
            renderRequest.setAttribute(ContainerConstants.PORTAL_PORTLET_CONFIG_ATTRIBUTE, portletEntry.getPortletConfig());
            renderRequest.setAttribute(ContainerConstants.PORTAL_CURRENT_CONTAINER, portletContainer );
            renderRequest.setAttribute(ContainerConstants.PORTAL_TEMPLATE_PARAMETERS_ATTRIBUTE, portalTemplateItem.getParameters() );

            // todo current implementation not support 'current catalog ID'
//            renderRequest.setAttribute(ContainerConstants.PORTAL_CURRENT_CATALOG_ID_ATTRIBUTE, portalRequestInstance.getDefaultCtx().getCtx().getCatalogItemId() );

            // Todo after rewrite(delete) member portlet, you can delete next line
            renderRequest.setAttribute(ContainerConstants.PORTAL_RESOURCE_BUNDLE_ATTRIBUTE, portletEntry.getPortletConfig().getResourceBundle(renderRequest.getLocale()) );

            if (log.isDebugEnabled()) {
                log.debug("portalRequestInstance: "+portalRequestInstance);
                log.debug("portletEntry: "+portletEntry);
                log.debug("parameters: "+parameters);
            }
            renderResponse = new RenderResponseImpl(
                portalRequestInstance,
                renderRequest,
                portalRequestInstance.getHttpResponse(),
                namespace,
                portletEntry.getPortletProperties(),
                parameters.getRequestState(),
                portletEntry.getPortletDefinition().getFullPortletName()
            );
            PortletUtils.setContentType(renderResponse);

            renderRequest.setAttribute( ContainerConstants.jAVAX_PORTLET_CONFIG, portletEntry.getPortletConfig() );
            renderRequest.setAttribute( ContainerConstants.jAVAX_PORTLET_REQUEST, renderRequest );
            renderRequest.setAttribute( ContainerConstants.jAVAX_PORTLET_RESPONSE, renderResponse );

            Map<String, List<String>> actionRequestParamMap = new HashMap<String, List<String>>( renderRequestParamMap );

            if (parameters != null && parameters.getParameters() != null) {
                // The portlet-container must not propagate parameters received
                // in an action request to subsequent render requests of the portlet.
                if ( parameters.getRequestState().isActionRequest() )
                    actionRequestParamMap.putAll(parameters.getParameters());
            }
            actionRequest = new ActionRequestImpl(
                actionRequestParamMap,
                portalRequestInstance,
                portletEntry.getServletConfig().getServletContext(),
                contextPath,
                portletEntry.getPortletDefinition().getPortletPreferences(),
                portletEntry.getPortletProperties(),
                portalRequestInstance.getPortalContext(),
                portletEntry.getPortletConfig().getPortletContext(),
                portletEntry.getPortletDefinition(),
                namespace
            );
            actionRequest.setAttribute(
                ContainerConstants.PORTAL_PORTAL_SESSION_MANAGER,
                new PortalSessionManagerImpl( Thread.currentThread().getContextClassLoader(), actionRequest )
            );

            actionResponse = new ActionResponseImpl(
                portalRequestInstance.getHttpResponse(),
                renderParameters,
                portletEntry.getPortletProperties()
            );

            if (log.isDebugEnabled()) {

                Enumeration e = actionRequest.getParameterNames();
                if (e.hasMoreElements()) {
                    for (; e.hasMoreElements();) {
                        String s = (String) e.nextElement();
                        log.debug("actionRequest attr - " + s + ", value - " + actionRequest.getParameter(s));
                    }
                }
                else {
                    log.debug("actionRequest map is empty");
                }

                e = renderRequest.getParameterNames();
                if (e.hasMoreElements()) {
                    for (; e.hasMoreElements();) {
                        String s = (String) e.nextElement();
                        log.debug("renderRequest attr - " + s + ", value - " + renderRequest.getParameter(s));
                    }
                }
                else {
                    log.debug("renderRequest map is empty");
                }

                log.debug("Done init page element ");
            }

//            if (!isUrl && portletEntry.getPortletDefinition() != null) {
            if (portletEntry.getPortletDefinition() != null) {
                if (log.isDebugEnabled()) {
                    log.debug("portlet: " + portletEntry.getPortletDefinition().getPortletName());
                }
                if (!portletEntry.getPortletDefinition().getSecurityRoleRefList().isEmpty()) {
                    isAccessPermit = false;
                    errorString = ACCESS_DISABLED_FOR_PORTLET;
                    for (SecurityRoleRef roleRef : portletEntry.getPortletDefinition().getSecurityRoleRefList()) {

                        if (log.isDebugEnabled()) {
                            log.debug("SecurityRoleRef.roleName: " + roleRef.getRoleName() + ", SecurityRoleRef.roleLink: " + roleRef.getRoleLink());
                        }

                        if (renderRequest.isUserInRole(roleRef.getRoleLink()!=null ? roleRef.getRoleLink() : roleRef.getRoleName())) {
                            isAccessPermit = true;
                            errorString = null;
                            break;
                        }
                    }
                }
            }

            if (isAccessPermit) {
                if (roleList!=null && !roleList.isEmpty()) {
                    isAccessPermit = false;
                    errorString = "Access disabled in context item";

                    for(String role : roleList) {
                        if (log.isDebugEnabled()) {
                            log.debug("Check access for role: " + role);
                        }
                        if (renderRequest.isUserInRole(role)) {
                            isAccessPermit = true;
                            errorString= null;
                            break;
                        }
                    }
                }
            }
        }
        catch (Throwable e) {
            errorString = portletUnavailable(portletName);
            log.error(errorString, e);
        }
    }

    private static String portletUnavailable(final String portletName) {
        return "Portlet '" + portletName + "' unavailable.";
    }

    private String getContextPath(final PortalRequestInstance portalRequestInstance) {
        String contextPath;
        final String portalRealPath = portalRequestInstance.getPortalServletConfig().getServletContext().getRealPath("/");
        final String portletRealPath = portletEntry.getServletConfig().getServletContext().getRealPath("/");
        if (log.isDebugEnabled()) {
            log.debug( "portalRealPath: " + portalRealPath );
            log.debug( "portletRealPath: " + portletRealPath );
        }
        if (portalRealPath.equals( portletRealPath ) ) {
            contextPath = portalRequestInstance.getHttpRequest().getContextPath();
        }
        else {
            File dir = new File(portletRealPath);
            contextPath = "/" + dir.getName();
        }
        return contextPath;
    }

    public PortletEntry getPortletEntry() {
        return portletEntry;
    }

    public PortalTemplateItem getPortalTemplateItem() {
        return portalTemplateItem;
    }

    public SitePortletData getData() {
        return data;
    }

    public void setData( SitePortletData data ) {
        this.data = data;
    }

    public PortletParameters getParameters() {
        return parameters;
    }

    public String getErrorString() {
        return errorString;
    }

    public Throwable getException() {
        return exception;
    }

    public boolean getIsXml() {
        return isXml;
    }

    public boolean getIsRedirected() {
        return isRedirected;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}