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
 * Time: 1:00:54 AM
 *
 * $Id$
 */
package org.riverock.webmill.portlet.wrapper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.riverock.webmill.portlet.PortalRequestInstance;

public final class RenderResponseImpl implements RenderResponse {

    private boolean isUsingWriter;
    private boolean isUsingStream;

    private ServletOutputStream wrappedWriter = null;

    // global parameters for page
    private PortalRequestInstance portalRequestInstance = null;
    private HttpServletResponse response = null;
    private String namespace = null;

    public RenderResponseImpl() {
    }

    public RenderResponseImpl( PortalRequestInstance portalRequestInstance, HttpServletResponse response, Writer writer, String namespace ) {
        this.portalRequestInstance = portalRequestInstance;
        this.namespace = namespace;
        this.response = new ServletResponseWrapperInclude( response, writer );
    }

    public RenderResponseImpl( PortalRequestInstance portalRequestInstance, HttpServletResponse response, OutputStream stream, String namespace ) {
        this.portalRequestInstance = portalRequestInstance;
        this.namespace = namespace;
        this.response = new ServletResponseWrapperInclude( response, stream );
    }

    public void addProperty( String s, String s1 ) {
        throw new IllegalStateException("not implemented");
    }

    public void setProperty( String s, String s1 ) {
        throw new IllegalStateException("not implemented");
    }

    public String encodeURL( String url ) {
        return response.encodeURL( url );
    }

    public void addCookie( javax.servlet.http.Cookie cookie ) {
        response.addCookie( cookie );
    }

    public boolean containsHeader( String name ) {
        return response.containsHeader( name );
    }

    public String encodeRedirectUrl( String url ) {
        return response.encodeRedirectUrl( url );
    }

    public String encodeRedirectURL( String url ) {
        return response.encodeRedirectURL( url );
    }

    public void sendRedirect( String location ) throws java.io.IOException {
        response.sendRedirect( location );
    }

    public void setDateHeader( String name, long date ) {
        response.setDateHeader( name, date );
    }

    public void sendError( int sc, String msg ) throws java.io.IOException {
        response.sendError( sc, msg );
    }

    public void sendError( int sc ) throws java.io.IOException {
        response.sendError( sc );
    }

    public void addHeader( String name, String value ) {
        response.addHeader( name, value );
    }

    public void setIntHeader( String name, int value ) {
        response.setIntHeader( name, value );
    }

    public void addDateHeader( String name, long date ) {
        response.addDateHeader( name, date );
    }

    public void setHeader( String name, String value ) {
        response.setHeader( name, value );
    }

    public void setStatus( int sc ) {
        response.setStatus( sc );
    }

    public void setStatus( int sc, String sm ) {
        response.setStatus( sc, sm );
    }

    public void addIntHeader( String name, int value ) {
        response.addIntHeader( name, value );
    }

    public void setContentLength( int len ) {
        response.setContentLength( len );
    }

    public String encodeUrl( String url ) {
        return this.encodeURL( url );
    }

    public void setLocale( java.util.Locale loc ) {
        response.setLocale( loc );
    }

    public String getContentType() {
        return null;
    }

    public OutputStream getPortletOutputStream() throws IOException {
        if (isUsingWriter) {
            throw new IllegalStateException( "getPortletOutputStream can't be used after getWriter was invoked" );
        }

        if (wrappedWriter == null) {
            wrappedWriter = response.getOutputStream();
//            wrappedWriter = new ServletOutputStreamWithWriter( response.getWriter(), response.getCharacterEncoding() );
        }

        isUsingStream = true;

        return wrappedWriter;
    }

    public PrintWriter getWriter() throws IOException {

        if (isUsingStream)
            throw new IllegalStateException( "getWriter can't be used after getOutputStream was invoked" );

        isUsingWriter = true;

        return response.getWriter();
    }

    public Locale getLocale() {
        throw new IllegalStateException("not implemented");
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setBufferSize( int i ) {
        throw new IllegalStateException( "portlet container does not support buffering" );
    }

    public int getBufferSize() {
        return response.getBufferSize();
    }

    public void flushBuffer() throws IOException {
        response.flushBuffer();
    }

    public void resetBuffer() {
        response.resetBuffer();
    }

    public boolean isCommitted() {
        return response.isCommitted();
    }

    public void reset() {
        response.reset();
    }


    public PortletURL createRenderURL() {
        return new PortletURLImpl( portalRequestInstance );
    }

    public PortletURL createActionURL() {
        throw new IllegalStateException("not implemented");
    }

    public String getNamespace() {
        // Todo: check structure namespace
        // The getNamespace method must return a valid identifier as defined in the 3.8 Identifier
        // Section of the Java Language Specification Second Edition.
        return namespace;
    }

    public void setTitle( String s ) {
        throw new IllegalStateException("not implemented");
    }

    public void setContentType( String contentType ) {
        response.setContentType( contentType );
    }

    public String getCharacterEncoding() {
        return response.getCharacterEncoding();
    }

}
