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
package org.riverock.webmill.portal.impl;

import java.util.List;
import java.util.Map;

import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.riverock.common.collections.MapWithParameters;

/**
 * User: Admin
 * Date: Sep 20, 2003
 * Time: 1:02:30 AM
 *
 * $Id$
 */
public final class ActionResponseImpl implements ActionResponse {
    private final static Logger log = Logger.getLogger( ActionResponseImpl.class );

    private HttpServletResponse httpResponse = null;
    private PortletMode portletMode = PortletMode.VIEW;
    private WindowState windowState = WindowState.NORMAL;
    private boolean isRedirected = false;
    private String redirectUrl = null;
    private Map<String, List<String>> renderParameters = null;
    private Map<String, List<String>> portletProperties = null;

    public void destroy() {
        httpResponse = null;
        portletMode = null;
        windowState = null;
        redirectUrl = null;
        if (renderParameters!=null) {
            renderParameters.clear();
            renderParameters = null;
        }
    }

    public ActionResponseImpl( final HttpServletResponse response, final Map<String, List<String>> renderParameters,
                               Map<String, List<String>> portletProperties) {
        this.httpResponse = response;
        this.renderParameters = renderParameters;
        this.portletProperties = portletProperties;
        if (log.isDebugEnabled()) {
            log.debug("renderParameters: " + ((Object)renderParameters).toString());
        }
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
        if (isRedirected) {
            throw new IllegalStateException( "Method is invoked after sendRedirect has been called" );
        }
        if (map==null) {
            throw new IllegalArgumentException("Map is null");
        }
        for (Object o : map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;

            if (!(entry.getKey() instanceof String)) {
                throw new IllegalArgumentException("Key must not be null and of type java.lang.String.");
            }
            if (!(entry.getValue() instanceof String[])) {
                throw new IllegalArgumentException("Value must not be null and of type java.lang.String[].");
            }
            if (log.isDebugEnabled()) {
                log.debug("set renderParameters, key: " + (String)entry.getKey()+", values: " + entry.getValue());
            }

            MapWithParameters.putInStringList(renderParameters, (String)entry.getKey(), (String[])entry.getValue());
        }
    }

    public void setRenderParameter( final String key, final String value ) {
        if (isRedirected) {
            throw new IllegalStateException( "Method is invoked after sendRedirect has been called" );
        }
        if (key==null) {
            throw new IllegalArgumentException("Key is null");
        }
        if (value==null) {
            throw new IllegalArgumentException("Value is null");
        }
        if (log.isDebugEnabled()) {
            log.debug("set renderParameters, key: " + key+", values: " + value);
        }
        MapWithParameters.putInStringList(renderParameters, key, value);
    }

    public void setRenderParameter( final String key, final String[] value ) {
        if (isRedirected) {
            throw new IllegalStateException( "Method is invoked after sendRedirect has been called" );
        }
        if (key==null) {
            throw new IllegalArgumentException("Key is null");
        }
        if (value==null) {
            throw new IllegalArgumentException("Value is null");
        }
        if (log.isDebugEnabled()) {
            log.debug("set renderParameters, key: " + key+", values: " + value);
        }


        MapWithParameters.putInStringList(renderParameters, key, value);
    }
}
