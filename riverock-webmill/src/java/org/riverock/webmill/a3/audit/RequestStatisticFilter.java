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
package org.riverock.webmill.a3.audit;

import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Locale;
import java.util.concurrent.ConcurrentMap;

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
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * User: Admin
 * Date: Mar 19, 2003
 * Time: 10:42:41 PM
 * <p/>
 * $Id$
 */
public final class RequestStatisticFilter implements Filter {

    private final static Logger log = Logger.getLogger(RequestStatisticFilter.class);

    private static final int SIZE_REFER = 200;
    private static final int SIZE_PARAMETERS = 200;
    private FilterConfig filterConfig = null;

    private ConcurrentMap<String, Long> userAgent = null;
    private ConcurrentMap<String, Long> url = null;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.userAgent = InternalDaoFactory.getInternalDao().getUserAgentList();
        this.url = InternalDaoFactory.getInternalDao().getUrlList();

    }

    /**
     * Take this filter out of service.
     */
    public void destroy() {
        this.filterConfig = null;
        if (userAgent!=null) {
            userAgent.clear();
            userAgent = null;
        }
        if (url!=null) {
            url.clear();
            url = null;
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws ServletException {

        String startInfo = null;
        try {
            RequestStatisticBean bean = new RequestStatisticBean();
            bean.setAccessDate( new Timestamp(System.currentTimeMillis()) );
            startInfo = null;

            //Todo uncomment for production
            if (log.isDebugEnabled()) {
                log.debug("enter into filter");
                startInfo = getDebugInfo(request, response);
            }
            log.debug("Start save userAgent info");


            String userAgentString = Header.getUserAgent(request);
            if (userAgentString == null)
                userAgentString = "UserAgent unknown";
            else if (userAgentString.length() < 5)
                userAgentString = "UserAgent too small";
            else
                userAgentString = StringTools.truncateString(userAgentString, 150);

            bean.setUserAgent( userAgentString );
            bean.setUrl( ((HttpServletRequest)request).getRequestURI() );

            String referer = Header.getReferer(request);
            if (referer == null)
                referer = "";
            int lenRefer = StringTools.lengthUTF(referer);
            if (lenRefer > SIZE_REFER) {
                lenRefer = SIZE_REFER;
                bean.setReferTooBig(true);
            }
            else
                bean.setReferTooBig(false);

            bean.setRefer( new String(StringTools.getBytesUTF(referer), 0, lenRefer) );

            String param = ((HttpServletRequest) request).getQueryString();
            int lenParams = StringTools.lengthUTF(param);
            if (lenParams > SIZE_PARAMETERS) {
                lenParams = SIZE_PARAMETERS;
                bean.setParamTooBig(true);
            }
            else
                bean.setParamTooBig(false);

            bean.setParameters( new String(StringTools.getBytesUTF(param), 0, lenParams) );

            InternalDaoFactory.getInternalDao().saveRequestStatistic( userAgent, url, bean );
        }
        catch (Throwable th) {
            try {
                log.error("startInfo:\n" + startInfo);
                log.error("endInfo:\n" + getDebugInfo(request, response));
            }
            catch (Exception e) {
                log.error("Nested Exception in getDebugInfo()", e);
            }
            final String es = "Exception call chain of filter";
            log.fatal(es, th);
            throw new ServletException(es, th);
        }


        // Pass control on to the next filter
        try {
            // !!!!!!!!!!!!!!!!! DO NOT COMMENT THIS LINE !!!!!!!!!!!!!!!!!!!!!!!!
            chain.doFilter(request, response);
            return;
        }
        catch (Throwable exc) {
            final String es = "Exception call chain of filter";
            log.fatal(es, exc);
            throw new ServletException(es, exc);
        }
    }

    private static String getDebugInfo(ServletRequest request, ServletResponse response) {
        String s_ = "";
        s_ += "\nRequest Received at " +
            (new Timestamp(System.currentTimeMillis())) + "\n";
        s_ += " characterEncoding=" + request.getCharacterEncoding() + "\n";
        s_ += "     contentLength=" + request.getContentLength() + "\n";
        s_ += "       contentType=" + request.getContentType() + "\n";
        s_ += "            locale=" + request.getLocale() + "\n";

        String s = ("           locales=");
        Enumeration locales = request.getLocales();
        boolean first = true;
        while (locales.hasMoreElements()) {
            Locale locale = (Locale) locales.nextElement();
            if (first)
                first = false;
            else
                s += (", ");
            s += (locale.toString());
        }
        s_ += s + "\n";
        Enumeration names = request.getParameterNames();
        s = "";
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            s = ("         parameter=" + name + "=");
            String values[] = request.getParameterValues(name);
            for (int i = 0; i < values.length; i++) {
                if (i > 0)
                    s += (", ");
                s += (values[i]);
            }
            s_ += s + "\n";
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
