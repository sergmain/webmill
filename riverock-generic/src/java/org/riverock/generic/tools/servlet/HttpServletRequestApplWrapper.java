/*
 * org.riverock.generic -- Database connectivity classes
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

/**
 * Author: mill
 * Date: Feb 19, 2003
 * Time: 9:04:25 AM
 *
 * $Id$
 */

package org.riverock.generic.tools.servlet;

import java.util.Enumeration;
import java.util.Map;
import java.util.Locale;
import java.util.Hashtable;
import java.security.Principal;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletInputStream;
import javax.servlet.RequestDispatcher;

import org.apache.log4j.Logger;

public class HttpServletRequestApplWrapper implements HttpServletRequest
{
    private static Logger cat = Logger.getLogger("org.riverock.generic.tools.servlet.HttpServletRequestApplWrapper");

    public HttpServletRequestApplWrapper()
    {
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
    private Hashtable parameterNames = new Hashtable();

    public String getParameter(String s)
    {
        return (String)parameterNames.get( s );
    }

    public void setParameter(String key, String obj)
    {
        parameterNames.put(key, obj );
    }
    public Enumeration getParameterNames()
    {
        return parameterNames.keys();
    }

    public String[] getParameterValues(String s)
    {
        return new String[0];
    }


    public String getPathInfo()
    {
        return null;
    }

    public String getPathTranslated()
    {
        return null;
    }

    public String getContextPath()
    {
        return null;
    }

    public String getQueryString()
    {
        return null;
    }

    public String getRemoteUser()
    {
        return null;
    }

    public boolean isUserInRole(String s)
    {
        return false;
    }

    public Principal getUserPrincipal()
    {
        return null;
    }

    public String getRequestedSessionId()
    {
        return null;
    }

    public String getRequestURI()
    {
        return null;
    }

    public StringBuffer getRequestURL()
    {
        return null;
    }

    public String getServletPath()
    {
        return null;
    }

    public HttpSession getSession(boolean b)
    {
        return session;
    }

    public HttpSession getSession()
    {
        return session;
    }

    public boolean isRequestedSessionIdValid()
    {
        return false;
    }

    public boolean isRequestedSessionIdFromCookie()
    {
        return false;
    }

    public boolean isRequestedSessionIdFromURL()
    {
        return false;
    }

    /**
     * @deprecated
     */
    public boolean isRequestedSessionIdFromUrl()
    {
        return false;
    }

    public Object getAttribute(String s)
    {
        return null;
    }

    public Enumeration getAttributeNames()
    {
        return null;
    }

    public String getCharacterEncoding()
    {
        return null;
    }

    public void setCharacterEncoding(String s) throws UnsupportedEncodingException
    {
    }

    public int getContentLength()
    {
        return 0;
    }

    public String getContentType()
    {
        return null;
    }

    public ServletInputStream getInputStream() throws IOException
    {
        return null;
    }

    public Map getParameterMap()
    {
        return null;
    }

    public String getProtocol()
    {
        return null;
    }

    public String getScheme()
    {
        return null;
    }

    public String getServerName()
    {
        return serverName;
    }

    public int getServerPort()
    {
        return 0;
    }

    public BufferedReader getReader() throws IOException
    {
        return null;
    }

    public String getRemoteAddr()
    {
        return null;
    }

    public String getRemoteHost()
    {
        return null;
    }

    public void setAttribute(String s, Object o)
    {
    }

    public void removeAttribute(String s)
    {
    }

    public Locale getLocale()
    {
        return null;
    }

    public Enumeration getLocales()
    {
        return null;
    }

    public boolean isSecure()
    {
        return false;
    }

    public RequestDispatcher getRequestDispatcher(String s)
    {
        return null;
    }

    /**
     * @deprecated
     */
    public String getRealPath(String s)
    {
        return null;
    }

    public String getAuthType()
    {
        return authType;
    }

    public void setAuthType(String authType)
    {
        this.authType = authType;
    }

    public Cookie[] getCookies()
    {
        return cookies;
    }

    public long getDateHeader(String s)
    {
        return 0;
    }

    public String getHeader(String s)
    {
        return null;
    }

    public Enumeration getHeaders(String s)
    {
        return null;
    }

    public void setCookies(Cookie[] cookies)
    {
        this.cookies = cookies;
    }

    public long getDateHeader()
    {
        return dateHeader;
    }

    public void setDateHeader(long dateHeader)
    {
        this.dateHeader = dateHeader;
    }

    public String getHeader()
    {
        return header;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public Enumeration getHeaders()
    {
        return headers;
    }

    public void setHeaders(Enumeration headers)
    {
        this.headers = headers;
    }

    public Enumeration getHeaderNames()
    {
        return headerNames;
    }

    public int getIntHeader(String s)
    {
        return 0;
    }

    public void setHeaderNames(Enumeration headerNames)
    {
        this.headerNames = headerNames;
    }

    public int getIntHeader()
    {
        return intHeader;
    }

    public void setIntHeader(int intHeader)
    {
        this.intHeader = intHeader;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

//    public void setParameterNames(Enumeration parameterNames)
//    {
//        this.parameterNames = parameterNames;
//    }

    HttpSession  session = null;

    public void setSession( HttpSession session_)
    {
        this.session = session_;
    }

    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

}
