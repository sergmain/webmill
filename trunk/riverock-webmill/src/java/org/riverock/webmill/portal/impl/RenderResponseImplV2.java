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

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.portlet.RenderRequest;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.riverock.webmill.portal.PortalRequestInstance;
import org.riverock.webmill.portal.PortalRequestInstance;
import org.riverock.webmill.portal.impl.PortletURLImpl;
import org.riverock.generic.tools.servlet.ServletResponseWrapperIncludeV3;

import org.apache.log4j.Logger;

/**
 * User: Admin
 * Date: Sep 20, 2003
 * Time: 1:00:54 AM
 *
 * $Id$
 */
public final class RenderResponseImplV2 implements RenderResponse, HttpServletResponse {

    private final static Logger log = Logger.getLogger( RenderResponseImplV2.class );

    private boolean isRedirected = false;
    private String redirectUrl = null;

    private boolean isUsingWriter;
    private boolean isUsingStream;

    private ServletOutputStream wrappedWriter = null;

    // global parameters for page
    private PortalRequestInstance portalRequestInstance = null;
    private RenderRequest renderRequest = null;
    private String namespace = null;
    protected String title = null;

    private HttpServletResponse httpServletResponse = null;
    private ServletResponse servletResponse = null;


    private ServletResponse _getServletResponse() {
        return servletResponse;
    }

    public byte[] getBytes() {
        if (servletResponse!=null && servletResponse instanceof ServletResponseWrapperIncludeV3 )
            return ((ServletResponseWrapperIncludeV3)servletResponse).getBytes();
        else {
            log.warn(
                "ServletResponce not instance of ServletResponseWrapperIncludeV3. " +
                "current class name: " + servletResponse.getClass().getName()
            );
            return new byte[] {};
        }
    }

    public void destroy() {
        servletResponse = null;
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
        namespace = null;
        title = null;
    }

    public ServletResponse getResponse(){
        if ( log.isDebugEnabled() ) {
            log.debug( "getResponse(), return this: " + this );
        }
        return this;
    }

    public void setResponse(ServletResponse response){
        if ( log.isDebugEnabled() ) {
            log.debug( "setResponse(), new response: " + response);
        }
        servletResponse = response;
    }

    public RenderResponseImplV2( PortalRequestInstance portalRequestInstance, RenderRequest renderRequest, HttpServletResponse response, String namespace ) {
        this.httpServletResponse = response;
        this.portalRequestInstance = portalRequestInstance;
        this.renderRequest = renderRequest;
        this.namespace = namespace;
        this.servletResponse = new ServletResponseWrapperIncludeV3( portalRequestInstance.getLocale() );
    }

    public boolean getIsRedirected() {
        return isRedirected;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void addProperty( String s, String s1 ) {
        throw new IllegalStateException("not implemented");
    }

    public void setProperty( String s, String s1 ) {
        throw new IllegalStateException("not implemented");
    }

    public String encodeURL( String url ) {
        return httpServletResponse.encodeURL( url );
    }

    public void addCookie( javax.servlet.http.Cookie cookie ) {
        httpServletResponse.addCookie( cookie );
    }

    public boolean containsHeader( String name ) {
        return httpServletResponse.containsHeader( name );
    }

    public String encodeRedirectUrl( String url ) {
        return httpServletResponse.encodeRedirectURL( url );
    }

    public String encodeRedirectURL( String url ) {
        return httpServletResponse.encodeRedirectURL( url );
    }

    public void sendRedirect( String url ) {
        if ( log.isDebugEnabled() ) {
            log.debug( "sendRedirect to new url: " + url );
        }

        if (url==null)
            return;

        this.redirectUrl = url;
        this.isRedirected = true;
    }

    public void setDateHeader( String name, long date ) {
//        response.setDateHeader( name, date );
    }

    public void sendError( int statusCode, String message ) {
        log.error( "sendError(). statusCode: " + statusCode + ", message: "+message );
//        httpResponseWrapper.sendError( statusCode, message );
    }

    public void sendError( int statusCode ) {
        log.error( "sendError(). statusCode: " + statusCode);
//        httpResponseWrapper.sendError( statusCode );
    }

    public void addHeader( String name, String value ) {
//        httpResponseWrapper.addHeader( name, value );
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
//        httpResponseWrapper.setStatus( sc );
    }

    public void setStatus( int sc, String sm ) {
//        httpResponseWrapper.setStatus( sc, sm );
    }

    public void addIntHeader( String name, int value ) {
//        httpResponseWrapper.addIntHeader( name, value );
    }

    public void setContentLength( int len ) {
//        response.setContentLength( len );
    }

    public String encodeUrl( String url ) {
        return this.encodeURL( url );
    }

    public void setLocale( Locale loc ) {
        _getServletResponse().setLocale( loc );
    }

    public String getContentType() {
        return null;
    }

    public OutputStream getPortletOutputStream() throws IOException {
        if (isUsingWriter) {
            throw new IllegalStateException( "getPortletOutputStream can't be used after getWriter was invoked" );
        }

        if (wrappedWriter == null) {
            wrappedWriter = _getServletResponse().getOutputStream();
        }

        isUsingStream = true;

        return wrappedWriter;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (isUsingWriter) {
            throw new IllegalStateException( "getOutputStream can't be used after getWriter was invoked" );
        }

        if (wrappedWriter == null) {
            wrappedWriter = _getServletResponse().getOutputStream();
        }

        isUsingStream = true;

        return wrappedWriter;
    }

    public PrintWriter getWriter() throws IOException {

        if ( log.isDebugEnabled() ) {
            log.debug( "getWriter(), isUsingStream: "+isUsingStream+", isUsingWriter; "+isUsingWriter);
            log.debug( "response: "+_getServletResponse().getClass().getName() );
        }

        if ( isUsingStream )
            throw new IllegalStateException( "getWriter can't be used after getOutputStream was invoked" );

        isUsingWriter = true;

        return _getServletResponse().getWriter();
    }

    public void setCharacterEncoding( String charset ) {
        _getServletResponse().setCharacterEncoding( charset );
    }

    public Locale getLocale() {
        return renderRequest.getLocale();
    }

    public void setBufferSize( int i ) {
        throw new IllegalStateException( "portlet container does not support buffering" );
    }

    public int getBufferSize() {
        return _getServletResponse().getBufferSize();
    }

    public void flushBuffer() throws IOException {
        _getServletResponse().flushBuffer();
    }

    public void resetBuffer() {
        _getServletResponse().resetBuffer();
    }

    public boolean isCommitted() {
        return _getServletResponse().isCommitted();
    }

    public void reset() {
        _getServletResponse().reset();
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
        _getServletResponse().setContentType( contentType );
    }

    public String getCharacterEncoding() {
        return _getServletResponse().getCharacterEncoding();
    }

}
