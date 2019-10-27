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
package org.riverock.webmill.a3.audit;

import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.riverock.common.html.Header;
import org.riverock.common.tools.StringTools;

/**
 * User: Admin
 * Date: Mar 19, 2003
 * Time: 10:42:41 PM
 * 
 * $Id: RequestStatisticFilter.java 1052 2006-11-14 16:46:45Z serg_main $
 */
public final class RequestStatisticFilter implements Filter {
    private final static Logger log = Logger.getLogger(RequestStatisticFilter.class);

    private FilterConfig filterConfig = null;
    public void init(FilterConfig filterConfig) {
        System.out.println("start init statistic filter");
        this.filterConfig = filterConfig;
        System.out.println("end init statistic filter");
    }

    /**
     * Take this filter out of service.
     */
    public void destroy() {
        this.filterConfig = null;
        RequestStatisticService.destroyService();
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws ServletException {

        String startInfo = null;
        if (log.isDebugEnabled()) {
            log.debug("enter into filter");
            startInfo = getDebugInfo(request, response);
        }

        try {
            RequestStatisticService statisticService = RequestStatisticService.getInstance();
            statisticService.process(
                Header.getUserAgent(request),
                ((HttpServletRequest)request).getRequestURI(),
                Header.getReferer(request),
                ((HttpServletRequest) request).getQueryString(),
                request.getServerName()
            );
        }
        catch (Throwable th) {
            try {
                log.error("startInfo:\n" + startInfo);
                log.error("endInfo:\n" + getDebugInfo(request, response));
            }
            catch (Throwable e) {
                log.error("Nested Throwable in getDebugInfo()", e);
                e.printStackTrace( System.err );
            }
            final String es = "Exception call chain of filter";
            log.fatal(es, th);
            th.printStackTrace( System.err );
        }

        // Pass control on to the next filter
        try {
            // !!!!!!!!!!!!!!!!! DO NOT COMMENT THIS LINE !!!!!!!!!!!!!!!!!!!!!!!!
            chain.doFilter(request, response);
        }
        catch (Throwable exc) {
            final String es = "Exception call chain of filter";
            log.fatal(es, exc);
            exc.printStackTrace( System.err );
            throw new ServletException(es, exc);
        }
    }

    private static String getDebugInfo(ServletRequest request, ServletResponse response) {
        String s_ = "\n";
        s_ += "Request Received at " + (new Timestamp(System.currentTimeMillis())) + "\n";
        s_ += " characterEncoding=" + request.getCharacterEncoding() + "\n";
        s_ += "     contentLength=" + request.getContentLength() + "\n";
        s_ += "       contentType=" + request.getContentType() + "\n";
        s_ += "            locale=" + request.getLocale() + "\n";

        s_ = ("           locales=");
        Enumeration locales = request.getLocales();
        boolean first = true;
        while (locales.hasMoreElements()) {
            Locale locale = (Locale) locales.nextElement();
            if (first)
                first = false;
            else
                s_ += (", ");
            s_ += (locale.toString());
        }
        s_ += s_ + "\n";
        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            s_ += ("         parameter=" + name + "=" + StringTools.arrayToString( request.getParameterValues(name) ) + '\n');
        }
        s_ += "          protocol=" + request.getProtocol() + "\n";
        s_ += "        remoteAddr=" + request.getRemoteAddr() + "\n";
        s_ += "        remoteHost=" + request.getRemoteHost() + "\n";
        s_ += "            scheme=" + request.getScheme() + "\n";
        s_ += "        serverName=" + request.getServerName() + "\n";
        s_ += "        serverPort=" + request.getServerPort() + "\n";
        s_ += "          isSecure=" + request.isSecure() + "\n";
        s_ += "\n";
        s_ += "     request class=" + request.getClass().getName() + "\n";
        s_ += "    response class=" + response.getClass().getName() + "\n";
        s_ += "\n";

        // Render the HTTP servlet request properties
        if (request instanceof HttpServletRequest) {
            s_ += "---------------------------------------------" + "\n";
            HttpServletRequest hrequest = (HttpServletRequest) request;
            s_ += "       contextPath=" + hrequest.getContextPath() + "\n";
            Cookie cookies[] = hrequest.getCookies();
            if (cookies == null)
                cookies = new Cookie[0];
            for (final Cookie newVar : cookies) {
                s_ += "            cookie=" + newVar.getName() + "=" + newVar.getValue() + "\n";
            }
            names = hrequest.getHeaderNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                String value = hrequest.getHeader(name);
                s_ += "            header=" + name + "=" + value + "\n";
            }
            names = hrequest.getAttributeNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                Object obj = hrequest.getAttribute(name);
                String value = obj.toString();
                s_ += "         attribute=" + name + "=" + value + ", class: " + obj.getClass().getName() + "\n";
            }
            s_ += "            method=" + hrequest.getMethod() + "\n";
            s_ += "          pathInfo=" + hrequest.getPathInfo() + "\n";
            s_ += "       queryString=" + hrequest.getQueryString() + "\n";
            s_ += "        remoteUser=" + hrequest.getRemoteUser() + "\n";
            s_ += "requestedSessionId=" + hrequest.getRequestedSessionId() + "\n";
            s_ += "        requestURI=" + hrequest.getRequestURI() + "\n";
            s_ += "       servletPath=" + hrequest.getServletPath() + "\n";
        }
        s_ += "=============================================" + "\n";
        return s_;
    }

    /**
     * Return a String representation of this object.
     */
    public String toString() {
        if (filterConfig == null)
            return ("RequestDumperFilter()");
        StringBuilder sb = new StringBuilder("RequestDumperFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }
}
