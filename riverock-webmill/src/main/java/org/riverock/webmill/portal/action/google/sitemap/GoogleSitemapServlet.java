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
package org.riverock.webmill.portal.action.google.sitemap;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.riverock.webmill.portal.utils.PortalUtils;
import org.riverock.webmill.utils.ServletUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 19.09.2006
 *         Time: 15:31:15
 *         <p/>
 *         $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public class GoogleSitemapServlet {
    private final static Logger log = Logger.getLogger( GoogleSitemapServlet.class );

    public static void doService(HttpServletRequest request, HttpServletResponse response, String realPath, Long siteId) throws ServletException, IOException {
        
        String sitemapPath = realPath + GoogleSitemapConstants.SITEMAP_DIR + siteId;

        File path = new File(sitemapPath);
        if (log.isDebugEnabled()) {
            log.debug("sitemap dir: " + path);
            log.debug("sitemap dir exists: " + path.exists());
        }
        if (!path.exists()) {
            path.mkdirs();
        }
        File sitemap = new File(path, GoogleSitemapConstants.SITEMAP_XML);
        if (!sitemap.exists()) {
            GoogleSitemapService.createSitemap(
                siteId, PortalUtils.buildVirtualHostUrl(request), request.getContextPath(), realPath
            );
            if (!sitemap.exists()) {
                log.warn("Google sitemap not created for unknown reason");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }

        ServletUtils.outputFileToResponse(response, sitemap, GoogleSitemapConstants.APPLICATION_X_GZIP_CONTENT_TYPE);
        sitemap=null;
        path=null;
    }

}
