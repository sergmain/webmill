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
package org.riverock.webmill.portal.instance;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.CacheManager;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;

import org.riverock.common.html.Header;
import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.webmill.css.ServletCSS;
import org.riverock.webmill.portal.action.google.sitemap.GoogleSitemapServlet;
import org.riverock.webmill.portal.dao.HibernateUtils;
import org.riverock.webmill.portal.info.PortalInfoImpl;
import org.riverock.webmill.portal.menu.SiteMenu;
import org.riverock.webmill.portal.site.SiteList;
import org.riverock.webmill.portal.static_content.StaticContent;
import org.riverock.webmill.utils.PortletUtils;

/**
 * $Id$
 */
public final class PortalFrontController extends HttpServlet {
    private final static Logger log = Logger.getLogger(PortalFrontController.class);

    private final ConcurrentMap<Long, PortalInstanceImpl> portalInstanceMap = new ConcurrentHashMap<Long, PortalInstanceImpl>();

    private ServletConfig portalServletConfig = null;
    private ClassLoader classLoader=null;

    public void init(ServletConfig servletConfig) {
        portalServletConfig = servletConfig;
        classLoader = PortalFrontController.class.getClassLoader();
    }

    public void destroy() {
        portalServletConfig = null;
        classLoader=null;

        for (PortalInstanceImpl portalInstance : portalInstanceMap.values()) {
            portalInstance.destroy();
        }
        portalInstanceMap.clear();
        try {
            HibernateUtils.destroy();
        }
        catch (Throwable th) {
            log.warn("Error unload hibernate", th);
        }
        PortalInfoImpl.destroyAll();
        SiteMenu.destroyAll();
        CacheManager.getInstance().shutdown();
        LogFactory.releaseAll();
        LogManager.shutdown();
    }

    public PortalFrontController() {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.debug("method is POST");
        }

        doGet(request, response);
    }

    private final static Object syncCounter = new Object();
    private static int counterNDC = 0;

    public void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {

        // Prepare Nested Diagnostic Contexts
        int counter;
        synchronized (syncCounter) {
            counter = counterNDC;
            ++counterNDC;
        }
        String counterNDC = Integer.toString(counter);
        NDC.push(counterNDC);
        try {
            if (log.isDebugEnabled()) {
                log.debug("httpRequest: "+httpRequest+", httpResponse: " +httpResponse);
                putMainRequestDebug(counter, httpRequest, httpResponse);
            }

            VirtualHost host = SiteList.getVirtualHost(httpRequest.getServerName());
            if (host==null) {
                String errorString = "Site for host " + httpRequest.getServerName() + " not configured. For configuration use admin section.";
                printErrorString(httpResponse, errorString);
                log.warn(errorString);
                log.warn("Referer: " + Header.getReferer(httpRequest));
                return;
            }
            if (!host.isDefaultHost()) {
                host = SiteList.getDefaultVirtualHost(host.getSiteId());
                if (host==null) {
                    String errorString = "Default virtual host for host " + httpRequest.getServerName() + " not configured. For configuration use admin section.";
                    printErrorString(httpResponse, errorString);
                    return;
                }
                StringBuilder sb = new StringBuilder(httpRequest.getScheme())
                    .append("://")
                    .append(host.getHost())
                    .append(':')
                    .append(httpRequest.getServerPort());
                String uri = httpRequest.getRequestURI();
                if (uri!=null) {
                    sb.append(uri);
                }
                String query = httpRequest.getQueryString();
                if (query!=null) {
                    sb.append('?').append(query);
                }
                if (log.isDebugEnabled()) {
                    log.debug("send redirect to "+sb.toString());
                }
                httpResponse.sendRedirect(sb.toString());
                return;
            }

            // request internal servlet?
            String pathInfo = httpRequest.getPathInfo();
            String realPath = portalServletConfig.getServletContext().getRealPath("/");
            if (pathInfo.startsWith("/css")) {
                ServletCSS.doService(httpRequest, httpResponse, realPath, host.getSiteId());
                return;
            }
            else if (pathInfo.equals("/sitemap.xml.gz")) {
                GoogleSitemapServlet.doService(httpRequest, httpResponse, realPath, host.getSiteId());
                return;
            }

            if (StaticContent.isStaticContent(httpRequest, httpResponse, realPath)) {
                return;
            }

            PortalInstanceImpl portalInstance = portalInstanceMap.get(host.getSiteId());
            if (portalInstance == null) {
                portalInstance = createNewPortalInstance(host.getSiteId());
            }
            portalInstance.process(httpRequest, httpResponse, counterNDC);
        }
        finally {
            NDC.pop();
        }
    }

    private static void printErrorString(HttpServletResponse httpResponse, String errorString) throws IOException {
        PortletUtils.setContentType(httpResponse);
        PrintWriter writer = httpResponse.getWriter();
        writer.write(errorString);
        writer.flush();
        writer.close();
    }

    private synchronized PortalInstanceImpl createNewPortalInstance(Long siteId) {
        PortalInstanceImpl portalInstance = portalInstanceMap.get(siteId);
        if (portalInstance != null) {
            return portalInstance;
        }
        portalInstance = PortalInstanceImpl.getInstance(siteId, portalServletConfig, classLoader);
        portalInstanceMap.putIfAbsent(siteId, portalInstance);
        return portalInstance;
    }

    private static void putMainRequestDebug(int counter, HttpServletRequest request_, HttpServletResponse response_) {
        log.debug("counter #6 " + counter);
        log.debug("request_ " + request_);
        log.debug("response_ " + response_);
        log.debug("Request methos type - " + request_.getMethod() );
        log.debug("Request URL - " + request_.getRequestURL());
        log.debug("Request query string - " + request_.getQueryString());

        for (Enumeration e = request_.getParameterNames(); e.hasMoreElements();) {
            String s = (String) e.nextElement();
            log.debug("Request parameter - " + s + ", value - " + request_.getParameter(s) );
        }
        log.debug( "This request made with cookie" );
        Cookie[] cookies = request_.getCookies();
        if (cookies!=null) {
            for (final Cookie newVar : cookies) {
                log.debug(cookieToString(newVar));
            }
        }
    }

    private static String cookieToString( Cookie cookie) {
        return
            "Cookie: " +
            "domain: " + cookie.getDomain()+", " +
            "path: "+cookie.getPath()+", " +
            "name: "+cookie.getName()+", " +
            "value: "+cookie.getValue();
    }

}


