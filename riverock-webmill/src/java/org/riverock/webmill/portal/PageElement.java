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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.portlet.PortletRequestDispatcher;
import javax.servlet.RequestDispatcher;

import org.apache.log4j.Logger;

import org.riverock.common.tools.MainTools;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.bean.SitePortletData;
import org.riverock.webmill.container.impl.PortletRequestDispatcherImpl;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.container.portlet.PortletEntry;
import org.riverock.webmill.container.portlet.bean.SecurityRoleRef;
import org.riverock.webmill.container.schema.site.TemplateItemType;
import org.riverock.webmill.container.schema.site.types.TemplateItemTypeTypeType;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.portlet.ContextNavigator;
import org.riverock.webmill.portal.impl.ActionRequestImpl;
import org.riverock.webmill.portal.impl.ActionResponseImpl;
import org.riverock.webmill.portal.impl.RenderRequestImpl;
import org.riverock.webmill.portal.impl.RenderResponseImpl;
import org.riverock.webmill.schema.core.WmPortalCatalogItemType;

/**
 * User: SergeMaslyukov
 * Date: 09.01.2005
 * Time: 19:29:20
 * $Id$
 */
public final class PageElement {
    private final static Logger log = Logger.getLogger( PageElement.class );

    private Throwable exception = null;
    private String errorString = null;

    private ActionRequestImpl actionRequest = null;
    private ActionResponseImpl actionResponse = null;
    private RenderRequestImpl renderRequest = null;
    private RenderResponseImpl renderResponse = null;
    private String namespace = null;
    private SitePortletData data = null;

    private PortletEntry portletEntry = null;
    private TemplateItemType templateItemType = null;
    private boolean isUrl = false;
    private boolean isXml = false;
    private ContextFactory.PortletParameters params = null;
    private boolean isRedirected = false;
    private String redirectUrl = null;
    private Properties properties = null;
    private boolean isAccessPermit = true;
    private String securityMessage = null;

    /*
     * renderParameter used for set parameters in action
     */
    private Map renderParameters = new HashMap();
    /*
     *
     */
    private Map portletAttributes = new HashMap();
    private static final String ACCESS_DISABLED_FOR_PORTLET = "Access disabled in portlet.xml";

    private PortletContainer portletContainer = null;

    public PageElement(PortletContainer portletContainer) {
        this.portletContainer = portletContainer;
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
        namespace = null;
        data = null;
        portletEntry = null;
        templateItemType = null;
        params = null;
        redirectUrl = null;
        if (log.isDebugEnabled()) {
            log.debug("#13.1");
        }
        if ( renderParameters!=null ) {
            renderParameters.clear();
            renderParameters = null;
        }
        if (log.isDebugEnabled()) {
            log.debug("#13.2");
        }
    }

    void processActionPortlet() {

        if (isUrl || exception!=null || errorString!=null) {
            if ( log.isDebugEnabled() ) {
                log.debug("isUrl" + isUrl + ", exception: " + exception + ", errorString: "+ errorString );
                log.debug("isAccessPermit" + isAccessPermit );
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

            if ( !e.getMessage().contains(notImpl) ) {
                log.error("Exception: ", e);
            }
//            exception = e;
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
                log.debug( "portlet code: "+renderRequest.getAttribute(
                    ContainerConstants.PORTAL_PORTLET_CODE_ATTRIBUTE ) );
                log.debug( "portlet xml root: "+renderRequest.getAttribute(
                    ContainerConstants.PORTAL_PORTLET_XML_ROOT_ATTRIBUTE ) );
            }

            if (log.isDebugEnabled()) {
                log.debug("#10.1");
            }

            if ( !isUrl ){
                ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
                try {
                    final ClassLoader classLoader = portletEntry.getClassLoader();
                    Thread.currentThread().setContextClassLoader( classLoader );

                    portletEntry.getPortlet().render( renderRequest, renderResponse );
                }
                finally {
                    Thread.currentThread().setContextClassLoader( oldLoader );
                }
            }
            else {

                ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
                try {
                    final ClassLoader classLoader = portletEntry.getClassLoader();
                    Thread.currentThread().setContextClassLoader( classLoader );

                    RequestDispatcher rd = portletEntry.getServletConfig().getServletContext().getRequestDispatcher( portletEntry.getPortletDefinition().getPortletClass() );

                    if (log.isDebugEnabled()) {
                        log.debug("#91.1");
                        log.debug( "process url: " + portletEntry.getPortletDefinition().getPortletClass() );
                        log.debug( "ServletContext: " + portletEntry.getServletConfig().getServletContext() );
                        log.debug( "RequestDispatcher: " + rd );
                    }

                    PortletRequestDispatcher dispatcher = new PortletRequestDispatcherImpl( rd );

                    if (log.isDebugEnabled()) {
                        log.debug("#91.4");
                    }
                    dispatcher.include( renderRequest, renderResponse );

                    if (log.isDebugEnabled()) {
                        log.debug("#91.5");
                    }
                }
                finally {
                    Thread.currentThread().setContextClassLoader( oldLoader );
                }
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
                    log.debug( "XmlRoot - "+templateItemType.getXmlRoot() );

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
        catch( java.lang.ThreadDeath e) {
            errorString = "Portlet '" + portletEntry.getPortletDefinition().getPortletName() + "' unavailable.";
            exception = e;
            log.error(errorString, e);
        }
        catch ( Throwable e ) {
            errorString = "Portlet '" + portletEntry.getPortletDefinition().getPortletName() + "' unavailable.";
            log.error( errorString, e );
            return;
        }
    }

    void initPortlet( final String portletName,  final PortalRequestInstance portalRequestInstance ) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("portalContext: " + portalRequestInstance.getPortalContext() );
                log.debug("Start init page element. Portlet name: '" + portletName + "'");
            }


            portletEntry = portletContainer.getPortletInstance(portletName);

            if (portletEntry == null) {
                errorString = "Portlet '" + portletName + "' unavailable.";
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
            isUrl = PortletService.getBooleanParam(portletEntry.getPortletDefinition(), ContainerConstants.is_url, Boolean.FALSE);

            if (!isUrl) {
                if (log.isDebugEnabled())
                    log.debug("Start create instance of portlet '" + portletName + "'");

                portletEntry = portletContainer.getPortletInstance(portletName);
                // Todo
/*
If a permanent unavailability is indicated by the UnavailableException, the portlet
container must remove the portlet from service immediately, call the portlet’s destroy
method, and release the portlet object.xviii A portlet that throws a permanent
UnavailableException must be considered unavailable until the portlet application
containing the portlet is restarted.
*/

                if (portletEntry.getIsPermanent()) {
                    errorString = "Portlet '" + portletName + "' permanently unavailable.";
                }
                else if (portletEntry.getInterval() > 0) {
                    errorString = "Portlet '" + portletName + "' unavailable for " + portletEntry.getInterval() + " seconds.";
                }
                if (portletEntry.getPortlet() == null) {
                    errorString = "Portlet '" + portletName + "' not created for unknown reason.";
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("Result of create portlet instance: " + errorString);
            }

            String contextPath = getContextPath(portalRequestInstance);

            Map<String, Object> map = null;
            if (templateItemType.getTypeObject().getType() == TemplateItemTypeTypeType.DYNAMIC_TYPE) {
                map = preparePortletParameters(portalRequestInstance);
            }
            else {
                map = new HashMap<String, Object>();
            }

            if (params != null && params.getParameters() != null) {
                map.putAll(params.getParameters());
            }
            renderRequest = new RenderRequestImpl(
                map,
                portalRequestInstance,
                renderParameters,
                portletEntry.getServletConfig().getServletContext(),
                portletAttributes,
                contextPath,
                portalRequestInstance.getHttpRequest().getContextPath(),
                portletEntry.getPortletDefinition().getPortletPreferences(),
                portletEntry.getPortletProperties(),
                portalRequestInstance.getPortalContext() );

            // if current portlet is dynamic - set metadata
            if (templateItemType.getTypeObject().getType() == TemplateItemTypeTypeType.DYNAMIC_TYPE) {
                properties = initMetadata(portalRequestInstance.getDefaultCtx().getCtx());
            }
            else {
                //todo write code of init metadata for others portlet
            }
            if (properties != null) {
                renderRequest.setAttribute(ContainerConstants.PORTAL_PORTLET_METADATA_ATTRIBUTE, properties);
            }

            // set portlet specific attribute
            renderRequest.setAttribute(ContainerConstants.PORTAL_TEMPLATE_PARAMETERS_ATTRIBUTE, templateItemType.getParameterAsReference());
            renderRequest.setAttribute(ContainerConstants.PORTAL_PORTLET_CODE_ATTRIBUTE, templateItemType.getCode());
            renderRequest.setAttribute(ContainerConstants.PORTAL_PORTLET_XML_ROOT_ATTRIBUTE, templateItemType.getXmlRoot());
            renderRequest.setAttribute(ContainerConstants.PORTAL_CURRENT_PORTLET_NAME_ATTRIBUTE, portletEntry.getPortletDefinition().getFullPortletName());
            renderRequest.setAttribute(ContainerConstants.PORTAL_PORTLET_CONFIG_ATTRIBUTE, portletEntry.getPortletConfig());
            renderRequest.setAttribute(ContainerConstants.PORTAL_CURRENT_CONTAINER, portletContainer );

            if (portalRequestInstance.getDefaultCtx() != null) {
                renderRequest.setAttribute(ContainerConstants.PORTAL_DEFAULT_CTX_ATTRIBUTE, portalRequestInstance.getDefaultCtx());
            }

            // Todo after remove all member module, you can delete next line
            renderRequest.setAttribute(ContainerConstants.PORTAL_RESOURCE_BUNDLE_ATTRIBUTE, portletEntry.getPortletConfig().getResourceBundle(renderRequest.getLocale()) );

            renderResponse = new RenderResponseImpl(portalRequestInstance, renderRequest, portalRequestInstance.getHttpResponse(), namespace, portletEntry.getPortletProperties() );

            ContextNavigator.setContentType(renderResponse);

            actionRequest = new ActionRequestImpl(
                map,
                portalRequestInstance,
                portletEntry.getServletConfig().getServletContext(),
                portletAttributes,
                contextPath,
                portalRequestInstance.getHttpRequest().getContextPath(),
                portletEntry.getPortletDefinition().getPortletPreferences(),
                portletEntry.getPortletProperties(),
                portalRequestInstance.getPortalContext()
            );

            actionResponse = new ActionResponseImpl(portalRequestInstance, actionRequest, portalRequestInstance.getHttpResponse(), namespace, renderParameters, portletEntry.getPortletProperties() );

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

            if (!isUrl && portletEntry.getPortletDefinition() != null) {
                int roleRefCount = portletEntry.getPortletDefinition().getSecurityRoleRefCount();
                if (roleRefCount > 0) {
                    isAccessPermit = false;
                    securityMessage = ACCESS_DISABLED_FOR_PORTLET;
                    errorString = securityMessage;
                    for (int i = 0; i < roleRefCount; i++) {
                        SecurityRoleRef roleRef = portletEntry.getPortletDefinition().getSecurityRoleRef(i);

                        if (log.isDebugEnabled()) {
                            log.debug("check right " + roleRef.getRoleName() + ", portlet: " + portletEntry.getPortletDefinition().getPortletName());
                        }
                        if (actionRequest.isUserInRole(roleRef.getRoleName())) {
                            isAccessPermit = true;
                            securityMessage = null;
                            errorString = null;
                            break;
                        }
/*
                    RoleLinkType roleLink = roleRef.getRoleLink();
                    if (roleLink!=null) {
                        if (actionRequest.isUserInRole( roleLink.getContent() ) ) {
                            isAccessPermit = true;
                            break;
                        }
                    }
                    else
                    {
*/
                    }
                }
            }

            if (isAccessPermit) {
                WmPortalCatalogItemType item = portalRequestInstance.getDefaultCtx().getCtx();
                if (item != null && item.getPortletRole() != null) {
                    isAccessPermit = false;
                    securityMessage = "Access disabled in context item";
                    errorString = securityMessage;

                    if (log.isDebugEnabled()) {
                        log.debug("Start check access for roles: " + item.getPortletRole());
                    }
                    StringTokenizer st = new StringTokenizer(item.getPortletRole());
                    while (st.hasMoreElements()) {
                        String role = st.nextToken();
                        if (actionRequest.isUserInRole(role)) {
                            isAccessPermit = true;
                            break;
                        }
                    }
                }
            }
        }
        catch (Throwable e) {
            errorString = "Portlet '" + portletName + "' unavailable.";
            log.error(errorString, e);
            return;
        }
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
            final String realPath = portletRealPath;
            File dir = new File(realPath);
            contextPath = "/" + dir.getName();
        }
        return contextPath;
    }

    private Properties initMetadata( WmPortalCatalogItemType defaultCtx ) throws PortalException {
        if (defaultCtx==null || defaultCtx.getMetadata()==null)
            return null;

        Properties p = new Properties();
        InputStream stream = new ByteArrayInputStream( defaultCtx.getMetadata().getBytes() );
        try {
            p.load( stream );
        }
        catch( IOException e ) {
            String es = "Error load properties";
            log.error( es, e );
            throw new PortalException( es, e );
        }
        return p;
    }

    private Map<String, Object> preparePortletParameters( final PortalRequestInstance portalRequestInstance ) {
        Map<String, Object> map = new HashMap<String, Object>();

        String nameId = PortletService.getStringParam( portletEntry.getPortletDefinition(), ContainerConstants.name_portlet_id );
        if ( log.isDebugEnabled() ) {
            log.debug( "nameId: "+nameId );
            log.debug( "Id: "+portalRequestInstance.getDefaultPortletId() );
        }
        if (nameId!=null)
            map.put( nameId, portalRequestInstance.getDefaultPortletId() );

        return map;
    }

    public PortletEntry getPortletEntry() {
        return portletEntry;
    }

    public boolean getIsUrl() {
        return isUrl;
    }

    public TemplateItemType getTemplateItemType() {
        return templateItemType;
    }

    public void setTemplateItemType( TemplateItemType templateItemType ) {
        this.templateItemType = templateItemType;
    }

    public SitePortletData getData() {
        return data;
    }

    public void setData( SitePortletData data ) {
        this.data = data;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace( String namespace ) {
        this.namespace = namespace;
    }

    public ContextFactory.PortletParameters getParams() {
        return params;
    }

    public void setParams( ContextFactory.PortletParameters params ) {
        this.params = params;
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
