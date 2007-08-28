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
package org.riverock.webmill.portal.page_element;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.MainTools;
import org.riverock.interfaces.ContainerConstants;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.webmill.container.bean.SitePortletData;
import org.riverock.webmill.container.portlet.PortletContainerFactory;
import org.riverock.webmill.container.portlet.PortletEntry;
import org.riverock.webmill.container.portlet.bean.SecurityRoleRef;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.port.PortalInfoImpl;
import org.riverock.webmill.portal.PortalConstants;
import org.riverock.webmill.portal.PortalInstance;
import org.riverock.webmill.portal.PortalSessionManagerImpl;
import org.riverock.webmill.portal.PortalRequest;
import org.riverock.webmill.portal.impl.ActionRequestImpl;
import org.riverock.webmill.portal.impl.ActionResponseImpl;
import org.riverock.webmill.portal.impl.PortalContextImpl;
import org.riverock.webmill.portal.impl.RenderRequestImpl;
import org.riverock.webmill.portal.impl.RenderResponseImpl;
import org.riverock.webmill.portal.namespace.Namespace;
import org.riverock.webmill.portal.preference.PortletPreferencePersistencer;
import org.riverock.webmill.portal.preference.PortletPreferencesImpl;
import org.riverock.webmill.portal.url.interpreter.PortletParameters;
import org.riverock.webmill.template.schema.ElementParameter;
import org.riverock.webmill.utils.PortletUtils;

/**
 * User: SergeMaslyukov
 * Date: 09.01.2005
 * Time: 19:29:20
 * $Id$
 */
public final class PageElementPortlet implements PageElement {
    private final static Logger log = Logger.getLogger( PageElementPortlet.class );

    private static final int NUM_LINES = 100;

    private static final String ACCESS_DISABLED_FOR_PORTLET = "Access denied";

    private Throwable exception = null;
    private String errorString = null;

    private ActionRequestImpl actionRequest = null;
    private ActionResponseImpl actionResponse = null;
    private RenderRequestImpl renderRequest = null;
    private RenderResponseImpl renderResponse = null;
    private SitePortletData data = null;

    private PortletEntry portletEntry = null;
    private Boolean isXml = null;
    private PortletParameters parameters = null;
    private boolean isRedirected = false;
    private String redirectUrl = null;
    private boolean isAccessPermit = true;
    private Namespace namespace = null;
    private String fullPortletName=null;
    private Map<String, List<String>> renderRequestParamMap = new HashMap<String, List<String>>();
    private String contextPath=null;
    private PortalRequest portalRequest =null;
    private PortletPreferences portletPreferences=null;
    private PortletPreferencePersistencer persistencer=null;
    private Map<String, List<String>> portletMetadata=null;
    private List<String> roleList = null;
    private String targetTemplateName=null;

    private String xmlRoot=null;
    private String code=null;
    private List<ElementParameter> templateParameters=null;

    /**
     * renderParameter used for set parameters in action
     */
    private Map<String, List<String>> renderParameters = new HashMap<String, List<String>>();

    private PortalInstance portalInstance;

    public PageElementPortlet(
        PortalInstance portalInstance,
        Namespace namespace,
        PortletParameters portletParameters,
        String targetTemplateName,
        String xmlRoot,
        String code,
        List<ElementParameter> templateParameters,
        PortalRequest portalRequest,
        String fullPortletName,
        List<String> roleList,
        PortletPreferencePersistencer persistencer
    ) {
        this.portalInstance = portalInstance;
        this.namespace = namespace;
        this.parameters = portletParameters;
        this.targetTemplateName = targetTemplateName;
        this.xmlRoot=xmlRoot;
        this.code=code;
        if (templateParameters!=null) {
            this.templateParameters=templateParameters;
        }
        else {
            this.templateParameters=new ArrayList<ElementParameter>();
        }
        this.portalRequest = portalRequest;
        this.fullPortletName=fullPortletName;
        this.persistencer = persistencer;
        this.roleList = roleList;
    }

    public void destroy() {
        exception = null;
        errorString = null;

        if ( actionRequest!=null ) {
            actionRequest.destroy();
            actionRequest = null;
        }
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
        if ( parameters!=null ) {
            parameters.destroy();
            parameters = null;
        }
        redirectUrl = null;
        namespace = null;
        fullPortletName=null;
        if ( renderRequestParamMap!=null ) {
            renderRequestParamMap.clear();
            renderRequestParamMap = null;
        }
        contextPath=null;
        portalRequest =null;
        portletPreferences=null;
        persistencer=null;
        portletMetadata=null;
        roleList = null;
        targetTemplateName=null;

        xmlRoot=null;
        code=null;

        if ( templateParameters!=null ) {
            templateParameters.clear();
            templateParameters = null;
        }

        if ( renderParameters!=null ) {
            renderParameters.clear();
            renderParameters = null;
        }
    }

    public void processAction() {
        initPortlet();

        if (portletEntry==null) {
            log.debug("portletEntry is null. terminate execution of processActionPortlet().");
            return;
        }

        initAction();

        if (exception!=null || errorString!=null) {
            if ( log.isDebugEnabled() ) {
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

    public void render() {
        initPortlet();
        initRender();

        if (exception!=null || errorString!=null) {
            if ( log.isDebugEnabled() ) {
                log.debug("exception: " + exception + ", errorString: "+ errorString );
            }
            return;
        }

        try {

            SitePortletData cacheData = portalInstance.getPortletContainer().getContentCache().getContent( portletEntry.getPortletDefinition() );
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
                portletEntry.getPortlet().render(renderRequest, renderResponse);
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

                if ( isXml ) {
                    log.debug( "XmlRoot - "+xmlRoot );
                }

                log.debug( "portletBytes - " + new String(portletBytes) );
            }

            if (portletBytes==null || portletBytes.length==0) {
                data = PageElementUtils.setData( "", false, false );
            }
            else {
                if ( isXml ) {
                    // write all without XML header - <? ..... ?>
                    int idx = MainTools.indexOf( portletBytes, (byte)'>' );
                    if ( idx==-1 ){
                        if ( StringUtils.isNotBlank(new String(portletBytes) )) {
                            final String es = "Array of bytes with xml'ized data is wrong - not start with <?xml ...?> ";
                            log.error(es);
                            errorString = es;
                            return;
                        }
                        else {
                            data = PageElementUtils.setData( "", false, false );
                        }
                    }
                    else {
                        data = PageElementUtils.setData(
                                MainTools.getBytes( portletBytes, idx+1 ), false, true );
                    }
                }
                else {
                    data = PageElementUtils.setData( portletBytes, false, false );
                }
            }

            // cache content
            portalInstance.getPortletContainer().getContentCache().setContent( portletEntry.getPortletDefinition(), data, renderRequest );
        }
        catch( javax.portlet.UnavailableException ue ) {
            PortletContainerFactory.destroy( portletEntry.getPortletDefinition().getPortletName(), portletEntry.getPortalPath() );
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

    void initAction() {
        try {
            if ( parameters!=null && parameters.getRequestState().isActionRequest() ) {

                if (log.isDebugEnabled()) {
                    log.debug("portletMetadata: " + portletMetadata);
                    log.debug("portletEntry: " + portletEntry);
                    if (portletEntry!=null) {
                        log.debug("portletEntry.getPortletDefinition(): " + portletEntry.getPortletDefinition());
                    }
                }

                Map<String, List<String>> actionRequestParamMap = new HashMap<String, List<String>>();
                // The portlet-container must not propagate parameters received
                // in an action request to subsequent render requests of the portlet.
                if (parameters.getParameters()!=null) {
                    actionRequestParamMap.putAll(parameters.getParameters());
                }

                this.portletPreferences = new PortletPreferencesImpl(
                    new HashMap<String, List<String>>(portletMetadata),
                    persistencer,
                    portletEntry.getPortletDefinition().getPreferences(),
                    isStandardPortletMode(parameters.getRequestState().getPortletMode()),
                    false
                );

                PortalInfo portalInfo = PortalInfoImpl.getInstance( portalRequest.getHttpRequest().getServerName() );
                actionRequest = new ActionRequestImpl(
                    actionRequestParamMap,
                    portalRequest,
                    portletEntry.getServletConfig().getServletContext(),
                    contextPath,
                    portletPreferences,
                    portletEntry.getPortletProperties(),
                    portletEntry.getPortletConfig().getPortletContext(),
                    portletEntry.getPortletDefinition(),
                    namespace,
                    portalInfo,
                    portalInstance
                );
                actionRequest.setAttribute(
                    ContainerConstants.PORTAL_PORTAL_SESSION_MANAGER,
                    new PortalSessionManagerImpl( Thread.currentThread().getContextClassLoader(), actionRequest )
                );
                actionRequest.setAttribute( ContainerConstants.PORTAL_TEMPLATE_NAME_ATTRIBUTE, targetTemplateName );

                actionResponse = new ActionResponseImpl(
                    portalRequest.getHttpResponse(),
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
                }
            }
        }
        catch (Exception e) {
            errorString = portletUnavailable(fullPortletName);
            log.error(errorString, e);
        }
    }

    void initRender() {
        try {
            if (portletEntry==null) {
                throw new IllegalStateException("portletEntry is null");
            }

            if (portletEntry.getIsPermanent()) {
                errorString = "Portlet '"+ fullPortletName + "' permanent unavailable.";
                log.error(errorString);
                log.error("Exception message: " + portletEntry.getExceptionMessage());
                return;
            }

            if (portletEntry.getIsWait()) {
                errorString = "Portlet "+ fullPortletName + " unavailable for "+ portletEntry.getInterval() ;
                log.error(errorString);
                log.error("Exception message: " + portletEntry.getExceptionMessage());
                return;
            }


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

            for (ElementParameter p : templateParameters) {
                List<String> list = new ArrayList<String>(1);
                list.add(p.getValue());
                renderRequestParamMap.put(p.getName(), list);
            }

            this.portletPreferences = new PortletPreferencesImpl(
                new HashMap<String, List<String>>(portletMetadata),
                persistencer,
                portletEntry.getPortletDefinition().getPreferences(),
                isStandardPortletMode(parameters.getRequestState().getPortletMode()),
                true
            );

            if (log.isDebugEnabled()) {
                log.debug("    portalRequestInstance: "+ portalRequest);
                log.debug("    portletEntry: "+portletEntry);
                log.debug("    parameters: "+parameters);
                log.debug("    renderRequestParamMap: "+renderRequestParamMap);
                log.debug("    namespace: "+namespace);
                if (portletEntry!=null) {
                    log.debug("    portletEntry.getServletConfig(): "+portletEntry.getServletConfig());
                    log.debug("    portletEntry.getPortletConfig(): "+portletEntry.getPortletConfig());
                    log.debug("    portletEntry.getPortletDefinition(): "+portletEntry.getPortletDefinition());
                }
            }

            PortalInfo portalInfo = PortalInfoImpl.getInstance( portalRequest.getHttpRequest().getServerName() );
            renderRequest = new RenderRequestImpl(
                renderRequestParamMap,
                portalRequest,
                renderParameters,
                portletEntry.getServletConfig().getServletContext(),
                contextPath,
                portletPreferences,
                portletEntry.getPortletProperties(),
                portletEntry.getPortletConfig().getPortletContext(),
                portletEntry.getPortletDefinition(),
                namespace,
                portalInfo,
                portalInstance
            );

            // set portlet specific attribute
            renderRequest.setAttribute(ContainerConstants.PORTAL_PORTLET_CODE_ATTRIBUTE, code);
            renderRequest.setAttribute(ContainerConstants.PORTAL_PORTLET_XML_ROOT_ATTRIBUTE, xmlRoot);
            renderRequest.setAttribute(ContainerConstants.PORTAL_PORTLET_CONFIG_ATTRIBUTE, portletEntry.getPortletConfig());
            renderRequest.setAttribute( ContainerConstants.PORTAL_TEMPLATE_NAME_ATTRIBUTE, targetTemplateName );

            // todo current implementation not support 'current catalog ID'
//            renderRequest.setAttribute(ContainerConstants.PORTAL_CURRENT_CATALOG_ID_ATTRIBUTE, portalRequestInstance.getDefaultCtx().getCtx().getCatalogItemId() );

            PortalContext portalContext = new PortalContextImpl(
                portalInstance.getPortalName(), portalRequest.getHttpRequest().getContextPath(), portalInfo
            );
            renderResponse = new RenderResponseImpl(
                portalRequest,
                renderRequest,
                portalRequest.getHttpResponse(),
                namespace,
                portletEntry.getPortletProperties(),
                parameters.getRequestState(),
                portletEntry.getPortletDefinition().getFullPortletName(),
                portalContext,
                portalInstance.getPortletContainer()
            );
            PortletUtils.setContentType(renderResponse);

            renderRequest.setAttribute( ContainerConstants.jAVAX_PORTLET_CONFIG, portletEntry.getPortletConfig() );
            renderRequest.setAttribute( ContainerConstants.jAVAX_PORTLET_REQUEST, renderRequest );
            renderRequest.setAttribute( ContainerConstants.jAVAX_PORTLET_RESPONSE, renderResponse );

            if (log.isDebugEnabled()) {
                Enumeration e = renderRequest.getParameterNames();
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
        }
        catch (Throwable e) {
            errorString = portletUnavailable(fullPortletName);
            log.error(errorString, e);
        }
    }

    private boolean isStandardPortletMode(PortletMode portletMode) {
        return portletMode==PortletMode.VIEW ||portletMode==PortletMode.EDIT ||portletMode==PortletMode.HELP; 
    }

    private void initPortlet() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Start init page element. Portlet name: '" + fullPortletName + "'");
                log.debug("Start create instance of portlet '" + fullPortletName + "'");
            }

            // load portlet preferences
            portletMetadata = persistencer.load();
            portletEntry = portalInstance.getPortletContainer().getPortletInstance(fullPortletName);

            if (portletEntry == null) {
                errorString = portletUnavailable(fullPortletName);
                return;
            }

            if ( portletEntry.getIsPermanent() ) {
                log.error( "portlet permanent unavailable, message: " + portletEntry.getExceptionMessage() );
                errorString = portletUnavailable(fullPortletName);
                return;
            }

            if ( portletEntry.getIsWait() ) {
                log.error( "portlet unavailable for "+portletEntry.getInterval()+" seconds");
                errorString = portletUnavailable(fullPortletName);
                return;
            }

            if (log.isDebugEnabled()) {
                log.debug("portletDefinition: " + portletEntry.getPortletDefinition());
            }

            if (portletEntry.getPortletDefinition() == null) {
                errorString = "Definition for portlet '" + fullPortletName + "' not found.";
                return;
            }

            isXml = PortletService.getBooleanParam(portletEntry.getPortletDefinition(), ContainerConstants.is_xml, Boolean.FALSE);

            contextPath = getContextPath(portalRequest);

            if (portletEntry.getPortletDefinition() != null) {
                if (log.isDebugEnabled()) {
                    log.debug("check security for portlet: " + portletEntry.getPortletDefinition().getPortletName());
                }
                if (!portletEntry.getPortletDefinition().getSecurityRoleRef().isEmpty()) {
                    isAccessPermit = false;
                    errorString = ACCESS_DISABLED_FOR_PORTLET;
                    for (SecurityRoleRef roleRef : portletEntry.getPortletDefinition().getSecurityRoleRef()) {

                        if (log.isDebugEnabled()) {
                            log.debug("SecurityRoleRef.roleName: " + roleRef.getRoleName() + ", SecurityRoleRef.roleLink: " + roleRef.getRoleLink());
                        }

                        boolean userInRole = isUserInRole(roleRef.getRoleLink() != null ? roleRef.getRoleLink() : roleRef.getRoleName());
                        if (log.isDebugEnabled()) {
                            log.debug("isUserInRole: " + userInRole);
                        }
                        if (userInRole) {
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
                        if (isUserInRole(role)) {
                            isAccessPermit = true;
                            errorString= null;
                            break;
                        }
                    }
                }
            }

        }
        catch (Throwable e) {
            errorString = portletUnavailable(fullPortletName);
            log.error(errorString, e);
        }
    }

    private boolean isUserInRole( String role ) {
        if (role==null) {
            return false;
        }

        if (role.equals(PortalConstants.WEBMILL_GUEST_ROLE)) {
            return true;
        }

        if (portalRequest.getHttpRequest().getServerName()==null || portalRequest.getAuth()==null) {
            return role.equals(PortalConstants.WEBMILL_ANONYMOUS_ROLE);
        }

        boolean status = portalRequest.getAuth().checkAccess( portalRequest.getHttpRequest().getServerName() );
        if ( !status ) {
            return false;
        }

        //noinspection SimplifiableIfStatement
        if (role.equals(PortalConstants.WEBMILL_AUTHENTIC_ROLE)) {
            return true;
        }

        return portalRequest.getAuth().isUserInRole( role );
    }

    private static String portletUnavailable(final String portletName) {
        return "Portlet '" + portletName + "' unavailable.";
    }

    private String getContextPath(final PortalRequest portalRequest) {
        String contextPath;
        final String portalRealPath = portalInstance.getPortalServletConfig().getServletContext().getRealPath("/");
        final String portletRealPath = portletEntry.getServletConfig().getServletContext().getRealPath("/");
        if (log.isDebugEnabled()) {
            log.debug( "portalRealPath: " + portalRealPath );
            log.debug( "portletRealPath: " + portletRealPath );
        }
        if (portalRealPath.equals( portletRealPath ) ) {
            contextPath = portalRequest.getHttpRequest().getContextPath();
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

    public SitePortletData getData() {
        if ( getException()!=null || getErrorString()!=null ) {
            String es = "";
            if (getErrorString()!=null) {
                es += getErrorString();
            }
            if (getException()!=null) {
                es += (
                    "<br>" +
                    ExceptionTools.getStackTrace( getException(), NUM_LINES, "<br>")
                    );
            }

            data = PageElementUtils.setData( es.getBytes(), true, false );
        }

        return data;
    }

    public boolean isXml() {
        if (isXml==null) {
            throw new PortalException("isXml not initialized");
        }
        return isXml;
    }

    public boolean isAction() {
        return (parameters!=null && parameters.getRequestState().isActionRequest());
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

    public String getFullPortletName() {
        return fullPortletName;
    }

}