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
package org.riverock.webmill.portal.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.riverock.webmill.portal.namespace.Namespace;
import org.riverock.webmill.portal.PortalRequestInstance;
import org.riverock.webmill.portal.context.CtxContextFactory;

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

    private Map<String, Object> parameters = new HashMap<String, Object>();

//    protected PortletWindow portletWindow;

    private boolean secure;
    private WindowState state = null;
    private PortalRequestInstance portalRequestInstance = null;
    private RenderRequest portletRequest = null;
    private boolean isActionReqeust = false;
    private Namespace namespace = null;

    public PortletURLImpl( PortalRequestInstance portalRequestInstance, RenderRequest renderRequest, boolean isActionReqeust, Namespace namespace ) {
        this.portalRequestInstance = portalRequestInstance;
        this.portletRequest = renderRequest;
        this.secure = portalRequestInstance.getHttpRequest().isSecure();
        this.isActionReqeust = isActionReqeust;
        this.namespace = namespace;
    }

    public void setWindowState( WindowState windowState ) throws WindowStateException {
//        PortalContext portalContext = .getPortalContext();
//        Enumeration supportedWindowStates = portalContext.getSupportedWindowStates();
//        if (windowState != null)
//        {
//            while (supportedWindowStates.hasMoreElements())
//            {
//                WindowState supportedWindowState = (WindowState) supportedWindowStates.nextElement();
//                if (windowState.equals(supportedWindowState))
//                {
//                    state = windowState;
//                    return;
//                }
//            }
//        }
        throw new WindowStateException( "unsupported Window State used: " + windowState, windowState );
    }

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

    public void setParameter( String name, String value ) {
        if ( name == null || value == null ) {
            throw new IllegalArgumentException( "name and value must not be null" );
        }
        MapWithParameters.put( parameters, name, value );
    }

    public void setParameter( String name, String[] values ) {
        if ( name == null || values == null || values.length == 0 ) {
            throw new IllegalArgumentException( "name and values must not be null or values be an empty array" );
        }
        List list = Arrays.asList( values );
        parameters.put( name, list );
    }

    /* (non-Javadoc)
     * @see javax.portlet.PortletURL#setParameters(Map)
     */
    public void setParameters( Map map ) {
        if ( map == null ) {
            throw new IllegalArgumentException( "Parameters must not be null." );
        }

        Map<String, Object> temp = new HashMap<String, Object>( 2*map.size() );
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>)iterator.next();

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
            if (obj instanceof List)
                temp.put(entry.getKey(), obj);
            else if (obj instanceof String)
                temp.put(entry.getKey(), obj);
            else
                temp.put(entry.getKey(), Arrays.asList((String[]) obj));
        }

        this.parameters.putAll( temp );
    }

    public void setSecure( boolean secure ) throws PortletSecurityException {
        // This implementation does assume not having a supporting security environment installed!
        if ( secure == true ) {
            throw new PortletSecurityException( "The current implementation does assume not having a supporting security environment installed!" );
        }

        this.secure = secure;
    }

    public String toString() {
        StringBuilder url = new StringBuilder( 50 );

        String portletName = getParameter( ContainerConstants.NAME_TYPE_CONTEXT_PARAM );
        if ( log.isDebugEnabled() ) {
            log.debug( "portlet name for insert into url: " + portletName );
            log.debug( "portletRequest: " + portletRequest );
        }
        Object tempObj = portletRequest.getAttribute( ContainerConstants.PORTAL_CURRENT_PORTLET_NAME_ATTRIBUTE );
        if ( log.isDebugEnabled() ) {
            log.debug( "portlet name from attributes: " + tempObj );
        }

        if (portletName==null && tempObj!=null)
            portletName = (String)tempObj;

        if ( log.isDebugEnabled() ) {
            log.debug( "Result portlet name for insert into url: " + portletName );
        }

        url.append(
            CtxContextFactory.encodeUrl(
                portletRequest, portletName,
                (String)portletRequest.getAttribute( ContainerConstants.PORTAL_TEMPLATE_NAME_ATTRIBUTE ),
                portletRequest.getLocale(),
                isActionReqeust,
		namespace
            )
        );

        if ( parameters != null ) {
            Iterator names = parameters.keySet().iterator();

            if (names.hasNext())
                url.append( '?' );

            boolean isNotFirst = false;
            while( names.hasNext() ) {
                String key = (String)names.next();
                Object obj = parameters.get( key );
                if (obj instanceof List) {
                    Iterator it = ((List)obj).iterator();
                    while (it.hasNext()) {
                        String value = (String)it.next();
                        if (isNotFirst) {
                            url.append( '&' );
                        }
                        else {
                            isNotFirst = true;
                        }
                        url.append( key ).append( '=' ).append( value );
                    }
                }
                else {
                    url.append( key ).append( '=' ).append( obj.toString() ).append( '&' );
                }
            }
        }

        return url.toString();
    }
    // --------------------------------------------------------------------------------------------

    // internal methods ---------------------------------------------------------------------------
    private boolean isPortletModeSupported( PortletMode requestedPortletMode ) {
// Todo need implement
//
//        PortletDefinition portletDefinition = referencedPortletWindow.getPortletEntity().getPortletDefinition();
//        ContentTypeSet contentTypes = portletDefinition.getContentTypeSet();
//        ContentType contentType = contentTypes.get("text/html");
//        Iterator portletModes = contentType.getPortletModes();
//        if (requestedPortletMode != null)
//        {
//            while (portletModes.hasNext())
//            {
//                PortletMode supportedPortletMode = (PortletMode) portletModes.next();
//                if (requestedPortletMode.equals(supportedPortletMode))
//                {
//                    return true;
//                }
//            }
//        }
        return true;
    }
    // --------------------------------------------------------------------------------------------

    // additional methods -------------------------------------------------------------------------
    public String getParameter( String name ) {
        return (String)parameters.get( name );
    }

    public String[] getParameters( String name ) {
        return (String[])parameters.get( name );
    }

    public PortletMode getPortletMode() {
        return mode;
    }

    public WindowState getWindowState() {
        return state;
    }
    // --------------------------------------------------------------------------------------------

    public void setSecure() {
        secure = true;
    }

}
