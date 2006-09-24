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
package org.riverock.webmill.portal;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.riverock.webmill.portal.utils.SiteList;

/**
 * $Id$
 */
public final class ContextNavigator extends HttpServlet {
    private final static Logger log = Logger.getLogger(ContextNavigator.class);

    private ConcurrentMap<Long, PortalInstanceImpl> portalInstanceMap = new ConcurrentHashMap<Long, PortalInstanceImpl>();

    private ServletConfig portalServletConfig = null;

    public void init(ServletConfig servletConfig) {
        portalServletConfig = servletConfig;
    }

    public void destroy() {
        portalServletConfig = null;

        if (portalInstanceMap != null) {
            for (Map.Entry<Long, PortalInstanceImpl> entry : portalInstanceMap.entrySet()) {
                entry.getValue().destroy();
            }
            portalInstanceMap.clear();
            portalInstanceMap = null;
        }
    }

    public ContextNavigator() {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        if (log.isDebugEnabled())
            log.debug("method is POST");

        doGet(request, response);
    }

    public void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.debug("httpRequest: "+httpRequest+", httpResponse: " +httpResponse);
        }

        Long siteId = SiteList.getSiteId(httpRequest.getServerName());
        if (siteId==null) {
            throw new ServletException("Site for host "+ httpRequest.getServerName()+" not configured. For configuration use admin section.");
        }

        PortalInstanceImpl portalInstance = portalInstanceMap.get(siteId);
        if (portalInstance == null) {
            portalInstance = createNewPortalInsance(siteId);
        }
        portalInstance.process(httpRequest, httpResponse);
    }

    private PortalInstanceImpl createNewPortalInsance(Long siteId) {
        PortalInstanceImpl portalInstance = portalInstanceMap.get(siteId);
        if (portalInstance != null) {
            return portalInstance;
        }
        portalInstance = PortalInstanceImpl.getInstance(portalServletConfig);
        portalInstanceMap.putIfAbsent(siteId, portalInstance);
        return portalInstance;
    }
}