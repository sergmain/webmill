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
package org.riverock.webmill.portal.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletSecurityException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import org.apache.log4j.Logger;

import org.riverock.common.collections.MapWithParameters;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.container.portlet.PortletContainerException;
import org.riverock.webmill.container.portlet.PortletEntry;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.portal.PortalRequestInstance;
import org.riverock.webmill.portal.context.CtxRequestContextPocessor;
import org.riverock.webmill.portal.context.RequestState;
import org.riverock.webmill.portal.namespace.Namespace;

/**
 * User: serg_main
 * Date: 20.05.2004
 * Time: 21:14:18
 * @author Serge Maslyukov
 * $Id$
 */
public final class PortletURLImpl implements PortletURL {
    private final static Logger log = Logger.getLogger( PortletURLImpl.class );

    protected PortletMode mode = null;
    private WindowState state = null;

    private Map<String, List<String>> parameters = new HashMap<String, List<String>>();

    private boolean secure;
    private PortalRequestInstance portalRequestInstance = null;
    private RenderRequest portletRequest = null;
    private boolean isActionReqeust = false;
    private Namespace namespace = null;
    private String portletName = null;
    private PortalContext portalContext = null;

    public PortletURLImpl(
        PortalRequestInstance portalRequestInstance, RenderRequest renderRequest,
        boolean isActionReqeust, Namespace namespace, RequestState requestState,
        String portletName, PortalContext portalContext) {
        this.portalRequestInstance = portalRequestInstance;
        this.portletRequest = renderRequest;
        this.secure = portalRequestInstance.getHttpRequest().isSecure();
        this.isActionReqeust = isActionReqeust;
        this.state = requestState.getWindowState();
        this.mode = requestState.getPortletMode();
        this.namespace = namespace;
        this.portletName = portletName;
        this.portalContext = portalContext;
    }

    /**
     * Indicates the window state the portlet should be in, if this
     * portlet URL triggers a request.
     * <p>
     * A URL can not have more than one window state attached to it.
     * If more than one window state is set only the last one set
     * is attached to the URL.
     *
     * @param windowState
     *               the portlet window state
     *
     * @exception WindowStateException
     *                   if the portlet cannot switch to this state,
     *                   because the portal does not support this state, the portlet has not
     *                   declared in its deployment descriptor that it supports this state, or the current
     *                   user is not allowed to switch to this state.
     *                   The <code>PortletRequest.isWindowStateAllowed()</code> method can be used
     *                   to check if the portlet can set a given window state.
     * @see javax.portlet.PortletRequest#isWindowStateAllowed
     */
    public void setWindowState( WindowState windowState ) throws WindowStateException {
        Enumeration supportedWindowStates = portalContext.getSupportedWindowStates();
        if (windowState != null)
        {
            while (supportedWindowStates.hasMoreElements())
            {
                WindowState supportedWindowState = (WindowState) supportedWindowStates.nextElement();
                if (windowState.equals(supportedWindowState))
                {
                    state = windowState;
                    return;
                }
            }
        }
        throw new WindowStateException( "unsupported Window State used: " + windowState, windowState );
    }

    /**
     * Indicates the portlet mode the portlet must be in, if this
     * portlet URL triggers a request.
     * <p>
     * A URL can not have more than one portlet mode attached to it.
     * If more than one portlet mode is set only the last one set
     * is attached to the URL.
     *
     * @param portletMode
     *               the portlet mode
     *
     * @exception PortletModeException
     *                   if the portlet cannot switch to this mode,
     *                   because the portal does not support this mode, the portlet has not
     *                   declared in its deployment descriptor that it supports this mode for the current markup,
     *                   or the current user is not allowed to switch to this mode.
     *                   The <code>PortletRequest.isPortletModeAllowed()</code> method can be used
     *                   to check if the portlet can set a given portlet mode.
     * @see javax.portlet.PortletRequest#isPortletModeAllowed
     */
    public void setPortletMode( PortletMode portletMode ) throws PortletModeException {
        if ( isPortletModeSupported( portletMode ) ) {
            mode = portletMode;
            return;
        }
        throw new PortletModeException( "Unsupported portlet mode. Used: " + portletMode, portletMode );
    }

    public void  addParameter( String name, String value) {
        setParameter(name, value);
    }

    /**
     * Sets the given String parameter to this URL.
     * <p>
     * This method replaces all parameters with the given key.
     * <p>
     * The <code>PortletURL</code> implementation 'x-www-form-urlencoded' encodes
     * all  parameter names and values. Developers should not encode them.
     * <p>
     * A portlet container may prefix the attribute names internally
     * in order to preserve a unique namespace for the portlet.
     *
     * @param   name
     *          the parameter name
     * @param   value
     *          the parameter value
     *
     * @exception  java.lang.IllegalArgumentException
     *                            if name or value are <code>null</code>.
     */
    public void setParameter( String name, String value ) {
        if ( name == null || value == null ) {
            throw new IllegalArgumentException( "name and value must not be null" );
        }
        MapWithParameters.putInStringList( parameters, name, value );
    }

    /**
     * Sets the given String array parameter to this URL.
     * <p>
     * This method replaces all parameters with the given key.
     * <p>
     * The <code>PortletURL</code> implementation 'x-www-form-urlencoded' encodes
     * all  parameter names and values. Developers should not encode them.
     * <p>
     * A portlet container may prefix the attribute names internally
     * in order to preserve a unique namespace for the portlet.
     *
     * @param   name
     *          the parameter name
     * @param   values
     *          the parameter values
     *
     * @exception  java.lang.IllegalArgumentException
     *                            if name or values are <code>null</code>.
     */
    public void setParameter( String name, String[] values ) {
        if ( name == null || values == null || values.length == 0 ) {
            throw new IllegalArgumentException( "name and values must not be null or values be an empty array" );
        }
        MapWithParameters.putInStringList(parameters, name, values);
    }

    /**
     * Sets a parameter parameters for this URL.
     * <p>
     * All previously set parameters are cleared.
     * <p>
     * The <code>PortletURL</code> implementation 'x-www-form-urlencoded' encodes
     * all  parameter names and values. Developers should not encode them.
     * <p>
     * A portlet container may prefix the attribute names internally,
     * in order to preserve a unique namespace for the portlet.
     *
     * @param  parameters   Map containing parameter names for
     *                      the render phase as
     *                      keys and parameter values as parameters
     *                      values. The keys in the parameter
     *                      parameters must be of type String. The values
     *                      in the parameter parameters must be of type
     *                      String array (<code>String[]</code>).
     *
     * @exception	java.lang.IllegalArgumentException
     *                      if parameters is <code>null</code>, if
     *                      any of the key/values in the Map are <code>null</code>,
     *                      if any of the keys is not a String, or if any of
     *                      the values is not a String array.
     */
    public void setParameters( Map parameters) {
        if ( parameters == null ) {
            throw new IllegalArgumentException( "Parameters must not be null." );
        }

        Map<String, List<String>> temp = new HashMap<String, List<String>>( 2* parameters.size() );
        for (Object o : parameters.entrySet()) {
            Map.Entry<String, List<String>> entry = (Map.Entry<String, List<String>>) o;

            Object obj = entry.getValue();
            if (obj == null) {
                throw new IllegalArgumentException("Value must not be null");
            }
            if (log.isDebugEnabled()) {
                log.debug("Object: " + obj.getClass().getName());
            }
            if (!(obj instanceof String) && !(obj instanceof String[]) && !(obj instanceof List)) {
                throw new IllegalArgumentException("Value must be type java.lang.String, java.lang.String[] or java.util.List. Type is " + obj.getClass().getName());
            }
            if (obj instanceof List) {
                temp.put(entry.getKey(), new ArrayList<String>((List<String>)obj));
            }
            else if (obj instanceof String) {
                List<String> list = new ArrayList<String>();
                list.add((String)obj);
                temp.put(entry.getKey(), list);
            }
            else {
                temp.put(entry.getKey(), new ArrayList<String>(Arrays.asList((String[]) obj)));
            }
        }

        this.parameters.putAll( temp );
    }

    /**
     * Indicated the security setting for this URL.
     * <p>
     * Secure set to <code>true</code> indicates that the portlet requests
     * a secure connection between the client and the portlet window for
     * this URL. Secure set to <code>false</code> indicates that the portlet
     * does not need a secure connection for this URL. If the security is not
     * set for a URL, it will stay the same as the current request.
     *
     * @param  secure  true, if portlet requests to have a secure connection
     *                 between its portlet window and the client; false, if
     *                 the portlet does not require a secure connection.
     *
     * @throws PortletSecurityException  if the run-time environment does
     *                                   not support the indicated setting
     */
    public void setSecure( boolean secure ) throws PortletSecurityException {
        // This implementation does assume not having a supporting security environment installed!
        if (secure ) {
            throw new PortletSecurityException( "The current implementation does assume not having a supporting security environment installed!" );
        }

        this.secure = secure;
    }

    /**
     * Returns the portlet URL string representation to be embedded in the
     * markup.<br>
     * Note that the returned String may not be a valid URL, as it may
     * be rewritten by the portal/portlet-container before returning the
     * markup to the client.
     *
     * @return   the encoded URL as a string
     */
    public String toString() {
        StringBuilder url = new StringBuilder( 70 );

        if ( log.isDebugEnabled() ) {
            log.debug( "portlet name for insert into url: " + portletName );
            log.debug( "portletRequest: " + portletRequest );
        }

        String resultPortletName;
        if ( portletName.startsWith( PortletContainer.PORTLET_ID_NAME_SEPARATOR ) ) {
            resultPortletName = portletName.substring(PortletContainer.PORTLET_ID_NAME_SEPARATOR .length());
        }
        else {
            resultPortletName = portletName;
        }

        Long contextId=null;
        if (portalRequestInstance.getRequestContext().getExtendedCatalogItem()!=null) {
             contextId = portalRequestInstance.getRequestContext().getExtendedCatalogItem().getCatalogId();
        }
        if (log.isDebugEnabled()) {
            log.debug("portalRequestInstance.getRequestContext().getExtendedCatalogItem(): " + portalRequestInstance.getRequestContext().getExtendedCatalogItem());
            log.debug("contextId: "+contextId);
        }

        // if url pointed to other portlet, change value of contextId with id of targer's portlet
        contextId = prepareContextId(contextId);
        if (contextId==null && isActionReqeust) {
            log.warn("In some cases action request with contextId==null may produce unexpected error");
        }

        url.append(
            CtxRequestContextPocessor.encodeUrl(
                portletRequest, resultPortletName,
                (String)portletRequest.getAttribute( ContainerConstants.PORTAL_TEMPLATE_NAME_ATTRIBUTE ),
                portletRequest.getLocale(),
                isActionReqeust,
                namespace,
                contextId)
        );

        if ( parameters != null ) {
            Iterator names = parameters.keySet().iterator();

            if (names.hasNext()) {
                url.append( '?' );
            }

            boolean isNotFirst = false;
            while( names.hasNext() ) {
                String key = (String)names.next();
                Object obj = parameters.get( key );
                if (obj instanceof List) {
                    for (Object o : ((List) obj)) {
                        String value = (String) o;
                        if (isNotFirst) {
                            url.append('&');
                        } else {
                            isNotFirst = true;
                        }
                        url.append(key).append('=').append(value);
                    }
                }
                else {
                    url.append( key ).append( '=' ).append( obj.toString() ).append( '&' );
                }
            }
        }

        return url.toString();
    }

    private Long prepareContextId(Long contextId) {

        List<String> porteltNameParam = parameters.get(ContainerConstants.NAME_TYPE_CONTEXT_PARAM);
        if (porteltNameParam!=null && porteltNameParam.size()>1) {
            throw new IllegalStateException("portletName defined more than once in parameters: " + porteltNameParam);
        }
        if (porteltNameParam !=null) {
            portalRequestInstance.getRequestContext().getDefaultPortletName();
            PortletEntry entry;
            try {
                entry = portalRequestInstance.getPortletContainer().getPortletInstance(porteltNameParam.get(0));
            }
            catch (PortletContainerException e) {
                String es = "Error get PortletEntry";
                log.error(es, e);
                throw new IllegalStateException(es, e);
            }
            if (entry!=null) {
                String portletId = PortletService.getStringParam(
                    entry.getPortletDefinition(), ContainerConstants.name_portlet_id
                );
                if (portletId!=null) {
                    List<String> ids = parameters.get(portletId);
                    if (ids!=null){
                        if (ids.size()>1) {
                            throw new IllegalStateException("portlet id defined more than once in parameters: " + ids);
                        }
                        return new Long(ids.get(0));
                    }
                }
            }
        }
        return contextId;
    }
    // --------------------------------------------------------------------------------------------

    // internal methods ---------------------------------------------------------------------------
    private boolean isPortletModeSupported( PortletMode requestedPortletMode ) {
        return true;
/*

        PortletDefinition portletDefinition = referencedPortletWindow.getPortletEntity().getPortletDefinition();
        List<Supports> supportses = portletDefinition.getSupports();
        for (Supports supports : supportses) {

            // todo switch to real mimetype of request
            if (supports.getMimeType().equals("text/html")) {
                for (String portletMode : supports.getPortletMode()) {
                    if (portletMode.equals(requestedPortletMode.toString()) ) {
                        return true;
                    }
                }
            }
        }
        return false;
*/
    }
    // --------------------------------------------------------------------------------------------

    // additional methods -------------------------------------------------------------------------
    public String getParameter( String name ) {
        List<String> list = parameters.get( name );
        if (list==null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public String[] getParameters( String name ) {
        List<String> list = parameters.get( name );
        if (list==null || list.isEmpty()) {
            return new String[]{};
        }
        return list.toArray(new String[0]);
    }

    public PortletMode getPortletMode() {
        return mode;
    }

    public WindowState getWindowState() {
        return state;
    }
    // --------------------------------------------------------------------------------------------
}
