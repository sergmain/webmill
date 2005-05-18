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
import java.util.Map;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.portlet.PortletRequestDispatcher;
import javax.servlet.RequestDispatcher;

import org.riverock.common.tools.MainTools;
import org.riverock.common.config.ConfigException;
import org.riverock.generic.exception.FileManagerException;
import org.riverock.interfaces.schema.javax.portlet.PortletType;
import org.riverock.interfaces.schema.javax.portlet.SecurityRoleRefType;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.portal.PortalConstants;
import org.riverock.webmill.portlet.impl.PortletRequestDispatcherImpl;
import org.riverock.webmill.portlet.wrapper.ActionRequestImpl;
import org.riverock.webmill.portlet.wrapper.ActionResponseImpl;
import org.riverock.webmill.portlet.wrapper.RenderRequestImpl;
import org.riverock.webmill.portlet.wrapper.RenderResponseImpl;
import org.riverock.webmill.schema.site.SitePortletDataType;
import org.riverock.webmill.schema.site.TemplateItemType;
import org.riverock.webmill.schema.site.types.TemplateItemTypeTypeType;
import org.riverock.webmill.schema.core.SiteCtxCatalogItemType;

import org.apache.log4j.Logger;

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
    private SitePortletDataType data = null;

    private PortletContainer.PortletEntry portletEntry = null;
    private TemplateItemType templateItemType = null;
    private PortletType portletDefinition = null;
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
        portletDefinition = null;
        params = null;
        redirectUrl = null;
if (log.isDebugEnabled()) {
log.debug("#13.1");
ContextNavigator.putResourceDebug();
}
        if ( renderParameters!=null ) {
            renderParameters.clear();
            renderParameters = null;
        }
if (log.isDebugEnabled()) {
log.debug("#13.2");
ContextNavigator.putResourceDebug();
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
            portletEntry.getPortlet().processAction( actionRequest, actionResponse );
            if ( actionResponse.getIsRedirected() ) {
                isRedirected = actionResponse.getIsRedirected();
                redirectUrl = actionResponse.getRedirectUrl();
            }
        }
        catch( Throwable e ) {
            exception = e;
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

            SitePortletDataType cacheData = PortletContentCache.getContent( portletDefinition );
            if (cacheData != null) {
                data = cacheData;
                return;
            }


            if ( log.isDebugEnabled() ) {
                log.debug( "Start render portlet '" + portletDefinition.getPortletName() + "'" );
                log.debug( "portlet request: " + renderRequest + ", response: " + renderResponse );
                log.debug( "portlet code: "+renderRequest.getAttribute(
                    PortalConstants.PORTAL_PORTLET_CODE_ATTRIBUTE ) );
                log.debug( "portlet xml root: "+renderRequest.getAttribute(
                    PortalConstants.PORTAL_PORTLET_XML_ROOT_ATTRIBUTE ) );
            }

if (log.isDebugEnabled()) {
log.debug("#10.1");
ContextNavigator.putResourceDebug();
}
            if ( !isUrl ){
                portletEntry.getPortlet().render( renderRequest, renderResponse );
            }
            else {
                RequestDispatcher rd = ContextNavigator.portalServletConfig.getServletContext().getRequestDispatcher( portletDefinition.getPortletClass() );

                if (log.isDebugEnabled()) {
                    log.debug("#91.1");
                    ContextNavigator.putResourceDebug();
                }

                if ( log.isDebugEnabled() ) {
                    log.debug( "process url: " + portletDefinition.getPortletClass() );
                    log.debug( "ServletContext: " + ContextNavigator.portalServletConfig.getServletContext() );
                    log.debug( "RequestDispatcher: " + rd );
                }

                if (log.isDebugEnabled()) {
                    log.debug("#91.2");
                    ContextNavigator.putResourceDebug();
                }

                if (log.isDebugEnabled()) {
                    log.debug("#91.3");
                    ContextNavigator.putResourceDebug();
                }
                PortletRequestDispatcher dispatcher = new PortletRequestDispatcherImpl( rd );
                if (log.isDebugEnabled()) {
                    log.debug("#91.4");
                    ContextNavigator.putResourceDebug();
                }
                dispatcher.include( renderRequest, renderResponse );
                if (log.isDebugEnabled()) {
                    log.debug("#91.5");
                    ContextNavigator.putResourceDebug();
                }
            }
            renderResponse.flushBuffer();

            if (log.isDebugEnabled()) {
                log.debug("#10.2");
                ContextNavigator.putResourceDebug();
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
            PortletContentCache.setContent( portletDefinition, data, renderRequest );
        }
        catch ( Throwable e ) {
            errorString = "Error process portlet '" + portletDefinition.getPortletName().getContent() + "'";
            log.error( errorString, e );
            exception = e;
            return;
        }
    }

    void initPortlet( final String portletName,  final PortalRequestInstance portalRequestInstance )
        throws FileManagerException, PortalException, ConfigException {

        if (log.isDebugEnabled())
            log.debug( "Start init page element. Portlet name: '"+portletName+"'");

        portletDefinition = PortletManager.getPortletDescription( portletName );

        if (log.isDebugEnabled())
            log.debug( "portletDefinition: " + portletDefinition );

        if (portletDefinition==null) {
            errorString = "Definition for portlet '" + portletName + "' not found.";
            return;
        }

        isXml = PortletTools.getBooleanParam( portletDefinition, PortletTools.is_xml, Boolean.FALSE ).booleanValue();
        isUrl = PortletTools.getBooleanParam( portletDefinition, PortletTools.is_url, Boolean.FALSE ).booleanValue();

        if ( !isUrl ) {
            if (log.isDebugEnabled())
                log.debug( "Start create instance of portlet '"+portletName+"'");

            portletEntry = PortletContainer.getPortletInstance( portletName );
            // Todo
/*
If a permanent unavailability is indicated by the UnavailableException, the portlet
container must remove the portlet from service immediately, call the portlet’s destroy
method, and release the portlet object.xviii A portlet that throws a permanent
UnavailableException must be considered unavailable until the portlet application
containing the portlet is restarted.
*/

            if ( portletEntry.getIsPermanent() ) {
                errorString = "Portlet '"+portletName+"' permanently unavailable.";
            }
            else if ( portletEntry.getInterval()>0 ) {
                errorString = "Portlet '"+portletName+"' unavailable for "+portletEntry.getInterval()+" seconds.";
            }
            if ( portletEntry.getPortlet()==null ) {
                errorString = "Portlet '"+portletName+"' not created for unknown reason";
            }
        }

        if (log.isDebugEnabled())
            log.debug( "Result of create portlet instance: " + errorString );

        Map map = null;
        if ( templateItemType.getType().getType()==TemplateItemTypeTypeType.DYNAMIC_TYPE ) {
            map = preparePortletParameters( portalRequestInstance );
        }
        else {
            map = new HashMap();
        }

        if ( params!=null && params.getParameters()!=null) {
            map.putAll( params.getParameters() );
        }
        renderRequest = new RenderRequestImpl( map, portalRequestInstance, renderParameters,
            ContextNavigator.portalServletConfig.getServletContext(),
            portletAttributes);

        // if current portlet is dynamic - set metadata
        if ( templateItemType.getType().getType()==TemplateItemTypeTypeType.DYNAMIC_TYPE ) {
            properties = initMetadata( portalRequestInstance.getDefaultCtx().getCtx() );
        }
        else {
            //todo write code of init metadata for others portlet
        }
        if (properties!=null) {
            renderRequest.setAttribute( PortalConstants.PORTAL_PORTLET_METADATA_ATTRIBUTE, properties );
        }

        // set portlet specific attribute
        renderRequest.setAttribute(
            PortalConstants.PORTAL_TEMPLATE_PARAMETERS_ATTRIBUTE, templateItemType.getParameterAsReference());
        renderRequest.setAttribute(
            PortalConstants.PORTAL_PORTLET_CODE_ATTRIBUTE, templateItemType.getCode() );
        renderRequest.setAttribute(
            PortalConstants.PORTAL_PORTLET_XML_ROOT_ATTRIBUTE, templateItemType.getXmlRoot() );
        renderRequest.setAttribute(
            PortalConstants.PORTAL_CURRENT_PORTLET_NAME_ATTRIBUTE, portletDefinition.getPortletName().getContent() );

        if (portalRequestInstance.getDefaultCtx()!=null) {
            renderRequest.setAttribute(
                PortalConstants.PORTAL_DEFAULT_CTX_ATTRIBUTE, portalRequestInstance.getDefaultCtx() );
        }


        renderResponse = new RenderResponseImpl( portalRequestInstance, renderRequest, portalRequestInstance.getHttpResponse(), namespace );
        ContextNavigator.setContentType( renderResponse );

        actionRequest = new ActionRequestImpl( map, portalRequestInstance,
            ContextNavigator.portalServletConfig.getServletContext(),
            portletAttributes);
        // set portlet specific attribute
//            portalRequestInstance.actionRequest.setAttribute(
//                PortalConstants.PORTAL_TEMPLATE_PARAMETERS_ATTRIBUTE, templateItem.getParameterAsReference());
//            portalRequestInstance.actionRequest.setAttribute(
//                PortalConstants.PORTAL_PORTLET_CODE_ATTRIBUTE, templateItem.getCode() );
//            portalRequestInstance.actionRequest.setAttribute(
//                PortalConstants.PORTAL_PORTLET_XML_ROOT_ATTRIBUTE, templateItem.getXmlRoot() );
        actionResponse = new ActionResponseImpl( portalRequestInstance, actionRequest, portalRequestInstance.getHttpResponse(), namespace, renderParameters );

        if (log.isDebugEnabled()) {

            Enumeration e = actionRequest.getParameterNames();
            if ( e.hasMoreElements() ) {
                for (; e.hasMoreElements();) {
                    String s = (String) e.nextElement();
                    log.debug("actionRequest attr - " + s + ", value - " + actionRequest.getParameter(s) );
                }                           }
            else {
                log.debug("actionRequest map is empty" );
            }

            e = renderRequest.getParameterNames();
            if ( e.hasMoreElements() ) {
                for (; e.hasMoreElements();) {
                    String s = (String) e.nextElement();
                    log.debug("renderRequest attr - " + s + ", value - " + renderRequest.getParameter(s) );
                }                           }
            else {
                log.debug("renderRequest map is empty" );
            }

            log.debug( "Done init page element ");
        }

        if (!isUrl && portletEntry.getPortletDefinition()!=null) {
            int roleRefCount = portletEntry.getPortletDefinition().getSecurityRoleRefCount();
            if (roleRefCount>0) {
                isAccessPermit = false;
                securityMessage = ACCESS_DISABLED_FOR_PORTLET;
                errorString = securityMessage;
                for( int i=0; i<roleRefCount; i++) {
                    SecurityRoleRefType roleRef = portletEntry.getPortletDefinition().getSecurityRoleRef(i);

                    if (log.isDebugEnabled()) {
                        log.debug("check right "+roleRef.getRoleName()+", portlet: "+portletEntry.getPortletDefinition().getPortletName() );
                    }
                    if (actionRequest.isUserInRole( roleRef.getRoleName() ) ) {
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

        if (isAccessPermit ) {
            SiteCtxCatalogItemType item = portalRequestInstance.getDefaultCtx().getCtx();
            if (item!=null && item.getPortletRole()!=null) {
                isAccessPermit = false;
                securityMessage = "Access disabled in context item";
                errorString = securityMessage;

                if (log.isDebugEnabled()) {
                    log.debug( "Start check access for roles: "+item.getPortletRole());
                }
                StringTokenizer st = new StringTokenizer( item.getPortletRole() );
                while (st.hasMoreElements()) {
                    String role = st.nextToken();
                    if (actionRequest.isUserInRole( role ) ) {
                        isAccessPermit = true;
                        break;
                    }
                }
            }
        }
    }

    private Properties initMetadata( SiteCtxCatalogItemType defaultCtx ) throws PortalException {
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

    private Map preparePortletParameters( final PortalRequestInstance portalRequestInstance ) {
        Map map = new HashMap();

        String nameId = PortletTools.getStringParam( portletDefinition, PortletTools.name_portlet_id );
        if ( log.isDebugEnabled() ) {
            log.debug( "nameId: "+nameId );
            log.debug( "Id: "+portalRequestInstance.getDefaultPortletId() );
        }
        if (nameId!=null)
            map.put( nameId, portalRequestInstance.getDefaultPortletId() );

        return map;
    }

    public PortletContainer.PortletEntry getPortletEntry() {
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

    public SitePortletDataType getData() {
        return data;
    }

    public void setData( SitePortletDataType data ) {
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
