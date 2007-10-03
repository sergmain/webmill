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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortalContext;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.log4j.Logger;

import org.riverock.common.tools.servlet.ServletResponseWrapperInclude;
import org.riverock.interfaces.generic.InternalResponse;
import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.portal.PortalRequest;
import org.riverock.webmill.portal.namespace.Namespace;
import org.riverock.webmill.portal.url.interpreter.RequestState;

/**
 * User: Admin
 * Date: Sep 20, 2003
 * Time: 1:00:54 AM
 *
 * $Id$
 */
public final class RenderResponseImpl extends HttpServletResponseWrapper implements RenderResponse, InternalResponse {
    private final static Logger log = Logger.getLogger( RenderResponseImpl.class );

    private boolean isRedirected = false;
    private String redirectUrl = null;

    private boolean isUsingWriter;
    private boolean isUsingStream;
    private Namespace namespace = null;
    private String portletName = null;

    private ServletOutputStream wrappedWriter = null;

    // global parameters for page
    private PortalRequest portalRequest = null;
    private RenderRequest renderRequest = null;

    private Map<String, List<String>> portletProperties = null;

    private ServletResponseWrapper servletResponse = null;
    private PortalContext portalContext = null;
    private PortletContainer portletContainer = null;

    /**
     * Portlet title
     */
    private String title = null;

    // current request state
    private RequestState requestState = null;

    private boolean included = false;

    private boolean isSupportHttpSendRedirect = false;

    public byte[] getBytes() {
        if (servletResponse!=null &&
            servletResponse.getResponse()!=null &&
            servletResponse.getResponse() instanceof ServletResponseWrapperInclude )
            return ((ServletResponseWrapperInclude)servletResponse.getResponse()).getBytes();
        else {
            if (servletResponse!=null)
                log.warn(
                    "ServletResponce not instance of ServletResponseWrapperInclude. " +
                        "current class name: " + servletResponse.getClass().getName()
                );
            else {
                log.warn("servletResponse is null");
            }
            return new byte[] {};
        }
    }

    public void destroy() {
        if (wrappedWriter!=null) {
            try {
                wrappedWriter.close();
            }
            catch( IOException e ) {
                // silence close write
            }
            wrappedWriter = null;
        }
        portalRequest = null;
        renderRequest = null;
        servletResponse = null;
        namespace = null;
        title = null;
    }

    public RenderResponseImpl( PortalRequest portalRequest,
        RenderRequest renderRequest, HttpServletResponse response, Namespace namespace,
        Map<String, List<String>> portletProperties, RequestState requestState, String portletName,
        PortalContext portalContext,
        PortletContainer portletContainer,
        boolean isSupportHttpSendRedirect
    ) {
        super(response);
        this.requestState = requestState;
        this.portalRequest = portalRequest;
        this.renderRequest = renderRequest;
        this.portletProperties = portletProperties;
        this.namespace = namespace;
        this.portletName = portletName;
        this.servletResponse = new ServletResponseWrapper( new ServletResponseWrapperInclude( portalRequest.getLocale() ) );
        this.portalContext = portalContext;
        this.portletContainer = portletContainer;
        this.isSupportHttpSendRedirect = isSupportHttpSendRedirect;
    }

    /**
    * Return the wrapped ServletResponse object.
    */
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

    /**
     * Adds a String property to an existing key to be returned to the portal.
     * <p>
     * This method allows response properties to have multiple values.
     * <p>
     * Properties can be used by portlets to provide vendor specific
     * information to the portal.
     *
     * @param  key    the key of the property to be returned to the portal
     * @param  value  the value of the property to be returned to the portal
     *
     * @exception  java.lang.IllegalArgumentException
     *                            if key is <code>null</code>.
     */
    public void addProperty( String key, String value ) {
        if (key==null) {
            throw new IllegalArgumentException("Property key can't be null");
        }
        List<String> values = portletProperties.get( key.toLowerCase() );
        if (values==null) {
            values = new ArrayList<String>();
            portletProperties.put( key.toLowerCase(), values );
        }
        values.add( value );
    }

    /**
     * Sets a String property to be returned to the portal.
     * <p>
     * Properties can be used by portlets to provide vendor specific
     * information to the portal.
     * <p>
     * This method resets all properties previously added with the same key.
     *
     * @param  key    the key of the property to be returned to the portal
     * @param  value  the value of the property to be returned to the portal
     *
     * @exception  java.lang.IllegalArgumentException
     *                            if key is <code>null</code>.
     */
    public void setProperty( String key, String value ) {
        if (key==null) {
            throw new IllegalArgumentException("Property key can't be null");
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

    /**
     * @deprecated As of version 2.1, use encodeURL(String url) instead
     * @param url
     * @return String
     */
    public String encodeUrl( String url ) {
        return this.encodeURL( url );
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
        if (!isSupportHttpSendRedirect) {
            log.info("RenderResponse.sendRedirect() not enabled in portlet.xml");
            return;
        }
        if (url==null) {
            return;
        }

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

    public void setLocale( Locale loc ) {
        servletResponse.setLocale( loc );
    }

    public String getContentType() {
        return null;
    }

    /**
   * The default behavior of this method is to return getOutputStream()
   * on the wrapped response object.
   */
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

    // RenderResponse methods

    /**
     * Returns a <code>OutputStream</code> suitable for writing binary
     * data in the response. The portlet container does not encode the
     * binary data.
     * <p>
     * Before calling this method the content type of the
     * render response must be set using the {@link #setContentType}
     * method.
     * <p>
     * Calling <code>flush()</code> on the OutputStream commits the response.
     * <p>
     * Either this method or {@link #getWriter} may be called to write the body, not both.
     *
     * @return	a <code>OutputStream</code> for writing binary data
     *
     * @exception java.lang.IllegalStateException   if the <code>getWriter</code> method
     * 					has been called on this response, or
     *                                    if no content type was set using the
     *                                    <code>setContentType</code> method.
     *
     * @exception java.io.IOException 	if an input or output exception occurred
     *
     * @see #setContentType
     * @see #getWriter
     */
    public OutputStream getPortletOutputStream() throws IOException {
        return getOutputStream();
    }

    /**
     * Returns a PrintWriter object that can send character
     * text to the portal.
     * <p>
     * Before calling this method the content type of the
     * render response must be set using the {@link #setContentType}
     * method.
     * <p>
     * Either this method or {@link #getPortletOutputStream} may be
     * called to write the body, not both.
     *
     * @return    a <code>PrintWriter</code> object that
     *		can return character data to the portal
     *
     * @exception  java.io.IOException
     *                 if an input or output exception occurred
     * @exception  java.lang.IllegalStateException
     *                 if the <code>getPortletOutputStream</code> method
     * 		     has been called on this response,
     *                 or if no content type was set using the
     *                 <code>setContentType</code> method.
     *
     * @see #setContentType
     * @see #getPortletOutputStream
     */
    public PrintWriter getWriter() throws IOException {

        if ( log.isDebugEnabled() ) {
            log.debug( "getWriter(), isUsingStream: "+isUsingStream+", isUsingWriter; "+isUsingWriter);
            log.debug( "response: "+servletResponse.getClass().getName() );
        }

        if ( isUsingStream ) {
            throw new IllegalStateException( "getWriter can't be used after getOutputStream was invoked" );
        }
        isUsingWriter = true;
        //noinspection UnnecessaryLocalVariable
        PrintWriter writer = servletResponse.getWriter();
        return writer;
    }

    /**
     * Returns the locale assigned to the response.
     *
     * @return  Locale of this response
     */
    public Locale getLocale() {
        return renderRequest.getLocale();
    }

    /**
     * Current version of Webmill portal does not support buffering
     *
     * Sets the preferred buffer size for the body of the response.
     * The portlet container will use a buffer at least as large as
     * the size requested.
     * <p>
     * This method must be called before any response body content is
     * written; if content has been written, or the portlet container
     * does not support buffering, this method may throw an
     * <code>IllegalStateException</code>.
     *
     * @param size 	the preferred buffer size
     *
     * @exception  java.lang.IllegalStateException
     *                    if this method is called after
     *			content has been written, or the
     *                    portlet container does not support buffering
     *
     * @see 		#getBufferSize
     * @see 		#flushBuffer
     * @see 		#isCommitted
     * @see 		#reset
     */
    public void setBufferSize( int size ) {
        servletResponse.setBufferSize(size);
    }

    /**
     * Returns the actual buffer size used for the response.  If no buffering
     * is used, this method returns 0.
     *
     * @return	 	the actual buffer size used
     *
     * @see 		#setBufferSize
     * @see 		#flushBuffer
     * @see 		#isCommitted
     * @see 		#reset
     */
    public int getBufferSize() {
        return servletResponse.getBufferSize();
    }

    /**
     * Forces any content in the buffer to be written to the client.  A call
     * to this method automatically commits the response.
     *
     * @exception  java.io.IOException  if an error occured when writing the output
     *
     * @see 		#setBufferSize
     * @see 		#getBufferSize
     * @see 		#isCommitted
     * @see 		#reset
     */
    public void flushBuffer() throws IOException {
        servletResponse.flushBuffer();
    }

    /**
     * Clears the content of the underlying buffer in the response without
     * clearing properties set. If the response has been committed,
     * this method throws an <code>IllegalStateException</code>.
     *
     * @exception  IllegalStateException 	if this method is called after
     *					response is comitted
     *
     * @see 		#setBufferSize
     * @see 		#getBufferSize
     * @see 		#isCommitted
     * @see 		#reset
     */
    public void resetBuffer() {
        servletResponse.resetBuffer();
    }

    /**
     * Clears any data that exists in the buffer as well as the properties set.
     * If the response has been committed, this method throws an
     * <code>IllegalStateException</code>.
     *
     * @exception java.lang.IllegalStateException  if the response has already been
     *                                   committed
     *
     * @see 		#setBufferSize
     * @see 		#getBufferSize
     * @see 		#flushBuffer
     * @see 		#isCommitted
     */
    public void reset() {
        servletResponse.reset();
        portletProperties.clear();
    }

    /**
     * Returns a boolean indicating if the response has been
     * committed.
     *
     * @return		a boolean indicating if the response has been
     *  		committed
     *
     * @see 		#setBufferSize
     * @see 		#getBufferSize
     * @see 		#flushBuffer
     * @see 		#reset
     */
    public boolean isCommitted() {
        return servletResponse.isCommitted();
    }

    /**
     * Creates a portlet URL targeting the portlet. If no portlet mode,
     * window state or security modifier is set in the PortletURL the
     * current values are preserved. If a request is triggered by the
     * PortletURL, it results in a render request.
     * <p>
     * The returned URL can be further extended by adding
     * portlet-specific parameters and portlet modes and window states.
     * <p>
     * The created URL will per default not contain any parameters
     * of the current render request.
     *
     * @return a portlet render URL
     */
    public PortletURL createRenderURL() {
        return new PortletURLImpl(portalRequest, renderRequest, false, namespace, requestState, portletName, portalContext, portletContainer);
    }

    /**
     * Creates a portlet URL targeting the portlet. If no portlet mode,
     * window state or security modifier is set in the PortletURL the
     * current values are preserved. If a request is triggered by the
     * PortletURL, it results in an action request.
     * <p>
     * The returned URL can be further extended by adding
     * portlet-specific parameters and portlet modes and window states.
     * <p>
     * The created URL will per default not contain any parameters
     * of the current render request.
     *
     * @return a portlet action URL
     */
    public PortletURL createActionURL() {
        return new PortletURLImpl(portalRequest, renderRequest, true, namespace, requestState, portletName, portalContext, portletContainer);
    }

    /**
     * The value returned by this method should be prefixed or appended to
     * elements, such as JavaScript variables or function names, to ensure
     * they are unique in the context of the portal page.
     *
     * @return   the namespace
     */
    public String getNamespace() {
        return namespace.getNamespace();
    }

    /**
     * This method sets the title of the portlet.
     * <p>
     * The value can be a text String
     *
     * @param  title    portlet title as text String or resource URI
     */
    public void setTitle( String title ) {
        this.title = title;
    }

    /**
     * Sets the MIME type for the render response. The portlet must
     * set the content type before calling {@link #getWriter} or
     * {@link #getPortletOutputStream}.
     * <p>
     * Calling <code>setContentType</code> after <code>getWriter</code>
     * or <code>getOutputStream</code> does not change the content type.
     *
     * @param   type  the content MIME type
     *
     * @throws  java.lang.IllegalArgumentException
     *              if the given type is not in the list returned
     *              by <code>PortletRequest.getResponseContentTypes</code>
     *
     * @see  RenderRequest#getResponseContentTypes
     * @see  #getContentType
     */
    public void setContentType( String type ) {
        servletResponse.setContentType( type );
    }

    /**
     * Returns the name of the charset used for
     * the MIME body sent in this response.
     *
     * <p>See <a href="http://ds.internic.net/rfc/rfc2045.txt">RFC 2047</a>
     * for more information about character encoding and MIME.
     *
     * @return		a <code>String</code> specifying the
     *			name of the charset, for
     *			example, <code>ISO-8859-1</code>
     *
     */
    public String getCharacterEncoding() {
        return servletResponse.getCharacterEncoding();
    }

    public void setIncluded(boolean included) {
        this.included = included;
    }

    public boolean isIncluded() {
        return included;
    }
}
