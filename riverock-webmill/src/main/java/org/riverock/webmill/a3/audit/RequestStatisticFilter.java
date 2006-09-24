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
 * $Id$
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
