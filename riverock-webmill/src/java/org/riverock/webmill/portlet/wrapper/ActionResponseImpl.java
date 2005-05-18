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

/**
 * User: Admin
 * Date: Sep 20, 2003
 * Time: 1:02:30 AM
 *
 * $Id$
 */
package org.riverock.webmill.portlet.wrapper;

import java.io.IOException;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletRequest;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.servlet.http.HttpServletResponse;

import org.riverock.webmill.portlet.PortalRequestInstance;

import org.apache.log4j.Logger;

public final class ActionResponseImpl implements ActionResponse {
    private final static Logger log = Logger.getLogger( ActionResponseImpl.class );

//    private PortalRequestInstance portalRequestInstance = null;
//    private PortletRequest portletRequest = null;
    private HttpServletResponse httpResponse = null;
    private String namespace = null;
    private PortletMode portletMode = PortletMode.VIEW;
    private WindowState windowState = WindowState.NORMAL;
    private boolean isRedirected = false;
    private String redirectUrl = null;
    private Map renderParameters = null;

    public void destroy() {
        httpResponse = null;
        namespace = null;
        portletMode = null;
        windowState = null;
        redirectUrl = null;
        if (renderParameters!=null) {
            renderParameters.clear();
            renderParameters = null;
        }
    }

    public ActionResponseImpl( final PortalRequestInstance portalRequestInstance, final ActionRequest renderRequest, final HttpServletResponse response, final String namespace, final Map renderParameters ) {
//        this.portalRequestInstance = portalRequestInstance;
//        this.portletRequest = renderRequest;
        this.namespace = namespace;
        this.httpResponse = response;
        this.renderParameters = renderParameters;
    }

    public boolean getIsRedirected() {
        return isRedirected;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void addProperty( final String key, final String value ) {
        throw new IllegalStateException("not implemented");
    }

    public void setProperty( final String key, final String value ) {
        throw new IllegalStateException("not implemented");
    }

    public String encodeURL( final String url ) {
        return httpResponse.encodeURL( url );
    }

    public void setWindowState( final WindowState windowState ) {
        this.windowState = windowState;
    }

    public void setPortletMode( final PortletMode portletMode ) {
        this.portletMode = portletMode;
    }

    public void sendRedirect( final String url ) {
        if (url==null)
            return;

        if ( log.isDebugEnabled() ) {
            log.debug( "sendRedirect to new url: " + url );
        }
        this.redirectUrl = url;
        this.isRedirected = true;
    }

    public void setRenderParameters( final Map map ) {
        renderParameters.putAll( map );
    }

    public void setRenderParameter( final String key, final String value ) {
        renderParameters.put( key, value );
    }

    public void setRenderParameter( final String key, final String[] value ) {
        renderParameters.put( key, value );
    }
}
