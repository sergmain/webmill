/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.generic.tools.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Author: mill
 * Date: Feb 19, 2003
 * Time: 9:04:25 AM
 * <p/>
 * $Id$
 */

public class HttpServletRequestApplWrapper implements HttpServletRequest {

    public HttpServletRequestApplWrapper() {
    }

    private String serverName = null;
    private String authType = null;
    private Cookie[] cookies = null;
    private long dateHeader;
    private String header = null;
    private Enumeration headers = null;
    private Enumeration headerNames = null;
    private int intHeader;
    private String method = null;
    private Map<String, String> parameterNames = new HashMap<String, String>();

    public String getParameter( String s ) {
        return parameterNames.get( s );
    }

    public void setParameter( String key, String obj ) {
        parameterNames.put( key, obj );
    }

    public Enumeration getParameterNames() {
        return Collections.enumeration(parameterNames.keySet());
    }

    public String[] getParameterValues( String s ) {
        return new String[0];
    }


    public String getPathInfo() {
        return null;
    }

    public String getPathTranslated() {
        return null;
    }

    public String getContextPath() {
        return null;
    }

    public String getQueryString() {
        return null;
    }

    public String getRemoteUser() {
        return null;
    }

    public boolean isUserInRole( String s ) {
        return false;
    }

    public Principal getUserPrincipal() {
        return null;
    }

    public String getRequestedSessionId() {
        return null;
    }

    public String getRequestURI() {
        return null;
    }

    public StringBuffer getRequestURL() {
        return null;
    }

    public String getServletPath() {
        return null;
    }

    public HttpSession getSession( boolean b ) {
        return session;
    }

    public HttpSession getSession() {
        return session;
    }

    public boolean isRequestedSessionIdValid() {
        return false;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    /**
     * @deprecated
     */
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    public Object getAttribute( String s ) {
        return null;
    }

    public Enumeration getAttributeNames() {
        return null;
    }

    public String getCharacterEncoding() {
        return null;
    }

    public void setCharacterEncoding( String s ) throws UnsupportedEncodingException {
    }

    public int getContentLength() {
        return 0;
    }

    public String getContentType() {
        return null;
    }

    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    public Map getParameterMap() {
        return null;
    }

    public String getProtocol() {
        return null;
    }

    public String getScheme() {
        return null;
    }

    public String getServerName() {
        return serverName;
    }

    public int getServerPort() {
        return 0;
    }

    public BufferedReader getReader() throws IOException {
        return null;
    }

    public String getRemoteAddr() {
        return null;
    }

    public String getRemoteHost() {
        return null;
    }

    public void setAttribute( String s, Object o ) {
    }

    public void removeAttribute( String s ) {
    }

    public Locale getLocale() {
        return null;
    }

    public Enumeration getLocales() {
        return null;
    }

    public boolean isSecure() {
        return false;
    }

    public RequestDispatcher getRequestDispatcher( String s ) {
        return null;
    }

    /**
     * @deprecated
     */
    public String getRealPath( String s ) {
        return null;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType( String authType ) {
        this.authType = authType;
    }

    public Cookie[] getCookies() {
        Cookie[] c = new Cookie[cookies.length];
        System.arraycopy(cookies, 0, c, 0, cookies.length);
        return c;
    }

    public long getDateHeader( String s ) {
        return 0;
    }

    public String getHeader( String s ) {
        return null;
    }

    public Enumeration getHeaders( String s ) {
        return null;
    }

    public void setCookies( Cookie[] cookies ) {
        Cookie[] c = new Cookie[cookies.length];
        System.arraycopy(cookies, 0, c, 0, cookies.length);
        this.cookies = c;
    }

    public long getDateHeader() {
        return dateHeader;
    }

    public void setDateHeader( long dateHeader ) {
        this.dateHeader = dateHeader;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader( String header ) {
        this.header = header;
    }

    public Enumeration getHeaders() {
        return headers;
    }

    public void setHeaders( Enumeration headers ) {
        this.headers = headers;
    }

    public Enumeration getHeaderNames() {
        return headerNames;
    }

    public int getIntHeader( String s ) {
        return 0;
    }

    public void setHeaderNames( Enumeration headerNames ) {
        this.headerNames = headerNames;
    }

    public int getIntHeader() {
        return intHeader;
    }

    public void setIntHeader( int intHeader ) {
        this.intHeader = intHeader;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod( String method ) {
        this.method = method;
    }

//    public void setParameterNames(Enumeration parameterNames)
//    {
//        this.parameterNames = parameterNames;
//    }

    HttpSession session = null;

    public void setSession( HttpSession session_ ) {
        this.session = session_;
    }

    public void setServerName( String serverName ) {
        this.serverName = serverName;
    }

    int remotePort;

    public void setRemotePort( int port ) {
        this.remotePort = port;
    }

    public int getRemotePort() {
        return remotePort;
    }

    private String localName = null;

    public void setLocalName( String localName ) {
        this.localName = localName;
    }

    public String getLocalName() {
        return localName;
    }

    private String localAddr = null;

    public void setLocalAddr( String localAddr ) {
        this.localAddr = localAddr;
    }

    public String getLocalAddr() {
        return localAddr;
    }

    int localPort;

    public void setLocalPort( int port ) {
        this.localPort = port;
    }

    public int getLocalPort() {
        return localPort;
    }
}
