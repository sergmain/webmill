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
package org.riverock.webmill.portal;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.riverock.webmill.portal.utils.SiteList;
import org.riverock.webmill.utils.PortletUtils;
import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.common.html.Header;

/**
 * $Id$
 */
public final class ContextNavigator extends HttpServlet {
    private final static Logger log = Logger.getLogger(ContextNavigator.class);

    private final ConcurrentMap<Long, PortalInstanceImpl> portalInstanceMap = new ConcurrentHashMap<Long, PortalInstanceImpl>();

    private ServletConfig portalServletConfig = null;

    public void init(ServletConfig servletConfig) {
        portalServletConfig = servletConfig;
    }

    public void destroy() {
        portalServletConfig = null;

        for (PortalInstanceImpl portalInstance : portalInstanceMap.values()) {
            portalInstance.destroy();
        }
        portalInstanceMap.clear();
    }

    public ContextNavigator() {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.debug("method is POST");
        }

        doGet(request, response);
    }

    public void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.debug("httpRequest: "+httpRequest+", httpResponse: " +httpResponse);
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

        PortalInstanceImpl portalInstance = portalInstanceMap.get(host.getSiteId());
        if (portalInstance == null) {
            portalInstance = createNewPortalInsance(host.getSiteId());
        }
        portalInstance.process(httpRequest, httpResponse);
    }

    private static void printErrorString(HttpServletResponse httpResponse, String errorString) throws IOException {
        PortletUtils.setContentType(httpResponse);
        PrintWriter writer = httpResponse.getWriter();
        writer.write(errorString);
        writer.flush();
        writer.close();
    }

    private synchronized PortalInstanceImpl createNewPortalInsance(Long siteId) {
        PortalInstanceImpl portalInstance = portalInstanceMap.get(siteId);
        if (portalInstance != null) {
            return portalInstance;
        }
        portalInstance = PortalInstanceImpl.getInstance(siteId, portalServletConfig);
        portalInstanceMap.putIfAbsent(siteId, portalInstance);
        return portalInstance;
    }
}


