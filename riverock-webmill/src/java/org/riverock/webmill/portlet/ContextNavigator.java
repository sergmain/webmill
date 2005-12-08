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
package org.riverock.webmill.portlet;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.portal.PortalInstanceImpl;
import org.riverock.generic.site.SiteListSite;
import org.riverock.generic.exception.GenericException;

/**
 * $Id$
 */
public final class ContextNavigator extends HttpServlet {
    private final static Logger log = Logger.getLogger(ContextNavigator.class);

    private Map<Long, PortalInstanceImpl> portalInstanceMap = new HashMap<Long, PortalInstanceImpl>();

    static ServletConfig portalServletConfig = null;
    public void init( ServletConfig servletConfig ) {
        portalServletConfig = servletConfig;
    }

    public void destroy() {
        portalServletConfig = null;

        if (portalInstanceMap!=null) {
            Iterator<Map.Entry<Long,PortalInstanceImpl>> iterator = portalInstanceMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, PortalInstanceImpl> entry = iterator.next();
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

    public void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse )
        throws IOException, ServletException {
        Long siteId = null;
        try {
            siteId = SiteListSite.getIdSite( httpRequest.getServerName() );
        }
        catch (GenericException e) {
            String es = "Erorr get siteId";
            log.error( es, e);
            throw new IllegalStateException( es, e );
        }
        PortalInstanceImpl portalInstance = portalInstanceMap.get( siteId );
        if (portalInstance==null) {
            portalInstance = createNewPortalInsance( siteId );
        }
        portalInstance.process(httpRequest, httpResponse );
    }

    private synchronized PortalInstanceImpl createNewPortalInsance(Long siteId) {
        PortalInstanceImpl portalInstance = portalInstanceMap.get( siteId );
        if (portalInstance!=null) {
            return portalInstance;
        }
        portalInstance = PortalInstanceImpl.getInstance( portalServletConfig );
        portalInstanceMap.put( siteId, portalInstance );
        return portalInstance;
    }

    public static void setContentType(HttpServletResponse response) throws PortalException {
        setContentType(response, WebmillConfig.getHtmlCharset());
    }

    public static void setContentType(HttpServletResponse response, String charset) throws PortalException {

        final String type = "text/html; charset=" + charset;

        if (log.isDebugEnabled()) {
            log.debug("set new charset: " + type);
            log.debug("response: " + response);
        }

        try {
            response.setContentType(type);
        } catch (Exception e) {
            final String es = "Error set new content type to " + charset;
            log.error(es, e);
            throw new PortalException(es, e);
        }
    }
}