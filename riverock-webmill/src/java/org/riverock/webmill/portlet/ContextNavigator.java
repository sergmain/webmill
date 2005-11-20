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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.portal.PortalInstanceImpl;

/**
 * $Id$
 */
public final class ContextNavigator extends HttpServlet {
    private final static Logger log = Logger.getLogger(ContextNavigator.class);

    private PortalInstanceImpl portalInstance = null;

    static ServletConfig portalServletConfig = null;
    public void init( ServletConfig servletConfig ) {
        portalServletConfig = servletConfig;
        portalInstance = PortalInstanceImpl.getInstance( portalServletConfig );
    }

    public void destroy() {
        portalServletConfig = null;
        if (portalInstance!=null) {
            portalInstance.destroy();
            portalInstance = null;
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
        portalInstance.process(httpRequest, httpResponse );
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