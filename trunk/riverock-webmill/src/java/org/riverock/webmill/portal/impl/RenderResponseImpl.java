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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.portlet.RenderRequest;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.riverock.webmill.portal.PortalRequestInstance;
import org.riverock.generic.tools.servlet.ServletResponseWrapperIncludeV3;

import org.apache.log4j.Logger;

/**
 * User: Admin
 * Date: Sep 20, 2003
 * Time: 1:00:54 AM
 *
 * $Id$
 */
public final class RenderResponseImpl extends HttpServletResponseWrapper implements RenderResponse {
    private final static Logger log = Logger.getLogger( RenderResponseImpl.class );

    private boolean isRedirected = false;
    private String redirectUrl = null;

    private boolean isUsingWriter;
    private boolean isUsingStream;
    private String namespace = null;

    private ServletOutputStream wrappedWriter = null;

    // global parameters for page
    private PortalRequestInstance portalRequestInstance = null;
    private RenderRequest renderRequest = null;

    private Map<String, List<String>> portletProperties = null;

    private ServletResponseWrapper servletResponse = null;

    protected String title = null;

    public byte[] getBytes() {
        if (servletResponse!=null &&
            servletResponse.getResponse()!=null &&
            servletResponse.getResponse() instanceof ServletResponseWrapperIncludeV3 )
            return ((ServletResponseWrapperIncludeV3)servletResponse.getResponse()).getBytes();
        else {
            log.warn(
                "ServletResponce not instance of ServletResponseWrapperIncludeV3. " +
                "current class name: " + servletResponse.getClass().getName()
            );
            return new byte[] {};
        }
    }

    public void destroy() {
        if (wrappedWriter!=null) {
            try {
                wrappedWriter.close();
            }
            catch( IOException e ) {
            }
            wrappedWriter = null;
        }
        portalRequestInstance = null;
        renderRequest = null;
        servletResponse = null;
        namespace = null;
        title = null;
    }

    public RenderResponseImpl( PortalRequestInstance portalRequestInstance, 
        RenderRequest renderRequest, HttpServletResponse response, String namespace,
        Map<String, List<String>> portletProperties) {
        super(response);
        this.portalRequestInstance = portalRequestInstance;
        this.renderRequest = renderRequest;
        this.portletProperties = portletProperties;
        this.namespace = namespace;
        this.servletResponse = new ServletResponseWrapper( new ServletResponseWrapperIncludeV3( portalRequestInstance.getLocale() ) );
    }

    public ServletResponse getResponse() {
        return servletResponse.getResponse();
    }

    public void setResponse(ServletResponse servletResponse) {
        this.servletResponse.setResponse( servletResponse );
    }

    public boolean getIsRedirected() {
        return isRedirected;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void addProperty( String key, String value ) {
        if (key==null) {
            throw new IllegalArgumentException("key can't be null");
        }
        List<String> values = portletProperties.get( key.toLowerCase() );
        if (values==null) {
            values = new ArrayList<String>();
            portletProperties.put( key.toLowerCase(), values );
        }
        values.add( value );
    }

    public void setProperty( String key, String value ) {
        if (key==null) {
            throw new IllegalArgumentException("key can't be null");
        }
        List<String> values = portletProperties.get( key.toLowerCase() );
        if (values==null) {
            values = new ArrayList<String>();
            portletProperties.put( key.toLowerCase(), values );
        }
        else {
            values.clear();
        }
        values.add( value );
    }

    public String encodeURL( String url ) {
        return super.encodeURL( url );
    }

    public void addCookie( javax.servlet.http.Cookie cookie ) {
        super.addCookie( cookie );
    }

    public boolean containsHeader( String name ) {
        return super.containsHeader( name );
    }

    public String encodeRedirectUrl( String url ) {
        return super.encodeRedirectURL( url );
    }

    public String encodeRedirectURL( String url ) {
        return super.encodeRedirectURL( url );
    }

    public void sendRedirect( String url ) {
        if (url==null)
            return;

        if ( log.isDebugEnabled() ) {
            log.debug( "sendRedirect to new url: " + url );
        }
        this.redirectUrl = url;
        this.isRedirected = true;
    }

    public void setDateHeader( String name, long date ) {
//        response.setDateHeader( name, date );
    }

    public void sendError( int statusCode, String message ) throws java.io.IOException {
        log.error( "sendError(). statusCode: " + statusCode + ", message: "+message );
        super.sendError( statusCode, message );
    }

    public void sendError( int statusCode ) throws java.io.IOException {
        log.error( "sendError(). statusCode: " + statusCode);
        super.sendError( statusCode );
    }

    public void addHeader( String name, String value ) {
        super.addHeader( name, value );
    }

    public void setIntHeader( String name, int value ) {
//        response.setIntHeader( name, value );
    }

    public void addDateHeader( String name, long date ) {
//        response.addDateHeader( name, date );
    }

    public void setHeader( String name, String value ) {
//        response.setHeader( name, value );
    }

    public void setStatus( int sc ) {
        super.setStatus( sc );
    }

    public void setStatus( int sc, String sm ) {
        super.setStatus( sc, sm );
    }

    public void addIntHeader( String name, int value ) {
        super.addIntHeader( name, value );
    }

    public void setContentLength( int len ) {
//        response.setContentLength( len );
    }

    public String encodeUrl( String url ) {
        return this.encodeURL( url );
    }

    public void setLocale( Locale loc ) {
        servletResponse.setLocale( loc );
    }

    public String getContentType() {
        return null;
    }

    public OutputStream getPortletOutputStream() throws IOException {
        if (isUsingWriter) {
            throw new IllegalStateException( "getPortletOutputStream can't be used after getWriter was invoked" );
        }

        if (wrappedWriter == null) {
            wrappedWriter = servletResponse.getOutputStream();
        }

        isUsingStream = true;

        return wrappedWriter;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (isUsingWriter) {
            throw new IllegalStateException( "getOutputStream can't be used after getWriter was invoked" );
        }

        if (wrappedWriter == null) {
            wrappedWriter = servletResponse.getOutputStream();
        }

        isUsingStream = true;

        return wrappedWriter;
    }

    public PrintWriter getWriter() throws IOException {

        if ( log.isDebugEnabled() ) {
            log.debug( "getWriter(), isUsingStream: "+isUsingStream+", isUsingWriter; "+isUsingWriter);
            log.debug( "response: "+servletResponse.getClass().getName() );
        }

        if ( isUsingStream )
            throw new IllegalStateException( "getWriter can't be used after getOutputStream was invoked" );

        isUsingWriter = true;

        return servletResponse.getWriter();
    }

    public Locale getLocale() {
        return renderRequest.getLocale();
    }

    public void setBufferSize( int i ) {
        throw new IllegalStateException( "portlet container does not support buffering" );
    }

    public int getBufferSize() {
        return servletResponse.getBufferSize();
    }

    public void flushBuffer() throws IOException {
        servletResponse.flushBuffer();
    }

    public void resetBuffer() {
        servletResponse.resetBuffer();
    }

    public boolean isCommitted() {
        return servletResponse.isCommitted();
    }

    public void reset() {
        servletResponse.reset();
    }

    public PortletURL createRenderURL() {
        return new PortletURLImpl( portalRequestInstance, renderRequest );
    }

    public PortletURL createActionURL() {
        return new PortletURLImpl( portalRequestInstance, renderRequest );
    }

    public String getNamespace() {
        return namespace;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public void setContentType( String contentType ) {
        servletResponse.setContentType( contentType );
    }

    public String getCharacterEncoding() {
        return servletResponse.getCharacterEncoding();
    }

}
