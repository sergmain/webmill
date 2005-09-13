/*
 * org.riverock.webmill.container -- Webmill portlet container implementation
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
package org.riverock.webmill.container.impl;

import java.net.MalformedURLException;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

public final class PortletContextImpl implements PortletContext {
//    private final static Logger log = Logger.getLogger( PortletContextImpl.class );

    private ServletContext servletContext;
    private String portalName;
    private int portalMajorVersion;
    private int portalMinorVersion;

    public PortletContextImpl( ServletContext servletContext, String portalName, int portalMajorVersion, int portalMinorVersion ) {
        this.servletContext = servletContext;
        this.portalName = portalName;
        this.portalMajorVersion = portalMajorVersion;
        this.portalMinorVersion = portalMinorVersion;
    }

    public String getServerInfo() {
        return portalName + '/' + portalMajorVersion + '.' + portalMinorVersion;
    }

    public PortletRequestDispatcher getRequestDispatcher( final String path ) {
        RequestDispatcher rd = servletContext.getRequestDispatcher( path );
/*
        if ( log.isDebugEnabled() ) {
            log.debug( "ServletContext: " + servletContext );
            log.debug( "RequestDispatcher: " + rd ); 
        }
*/

        return new PortletRequestDispatcherImpl( rd );
    }

    public PortletRequestDispatcher getNamedDispatcher( final String name ) {
        RequestDispatcher rd = servletContext.getNamedDispatcher( name );
        return rd!=null ?new PortletRequestDispatcherImpl( rd ) :null;
    }

    public java.io.InputStream getResourceAsStream( final String path ) {
        return servletContext.getResourceAsStream( path );
    }

    public int getMajorVersion() {
        return portalMajorVersion;
    }

    public int getMinorVersion() {
        return portalMinorVersion;
    }

    public String getMimeType( final String file ) {
        return servletContext.getMimeType( file );
    }

    public String getRealPath( final String path ) {
        return servletContext.getRealPath( path );
    }

    public java.util.Set getResourcePaths( final String path ) {
        return servletContext.getResourcePaths( path );
    }

    public java.net.URL getResource( final String path ) throws MalformedURLException {
        if (path == null || !path.startsWith( "/" )) {
            throw new MalformedURLException( "path must start with a '/'" );
        }
        return servletContext.getResource( path );
    }

    public Object getAttribute( final String name ) {
        if (name == null) {
            throw new IllegalArgumentException( "Attribute name == null" );
        }

        return servletContext.getAttribute( name );
    }

    public java.util.Enumeration getAttributeNames() {
        return servletContext.getAttributeNames();
    }

    public String getInitParameter( final String name ) {
        if (name == null) {
            throw new IllegalArgumentException( "Parameter name == null" );
        }

        return servletContext.getInitParameter( name );
    }

    public java.util.Enumeration getInitParameterNames() {
        return servletContext.getInitParameterNames();
    }

    public void log( final String msg ) {
        servletContext.log( msg );
    }

    public void log( final String message, final Throwable throwable ) {
        servletContext.log( message, throwable );
    }

    public void removeAttribute( final String name ) {
        if (name == null) {
            throw new IllegalArgumentException( "Attribute name == null" );
        }

        servletContext.removeAttribute( name );
    }

    public void setAttribute( final String name, final Object object ) {
        if (name == null) {
            throw new IllegalArgumentException( "Attribute name == null" );
        }

        servletContext.setAttribute( name, object );
    }

    public String getPortletContextName() {
        return servletContext.getServletContextName();
    }

    public javax.servlet.ServletContext getServletContext() {
        return servletContext;
    }
}

