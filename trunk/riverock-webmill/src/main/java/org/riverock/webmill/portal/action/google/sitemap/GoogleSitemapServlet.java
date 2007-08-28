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

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.riverock.webmill.portal.info.PortalInfoImpl;
import org.riverock.webmill.portal.utils.PortalUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 19.09.2006
 *         Time: 15:31:15
 *         <p/>
 *         $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public class GoogleSitemapServlet extends HttpServlet {
    private final static Logger log = Logger.getLogger( GoogleSitemapServlet.class );

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        PortalInfoImpl p = PortalInfoImpl.getInstance( request.getServerName() );
        String sitemapPath = getServletContext().getRealPath("/") + GoogleSitemapConstants.SITEMAP_DIR + p.getSiteId();

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
                p.getSiteId(), PortalUtils.buildVirtualHostUrl(request), request.getContextPath(), getServletContext().getRealPath("/")
            );
            if (!sitemap.exists()) {
                log.warn("Google sitemap not created for unknown reason");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }

        InputStream is = new FileInputStream(sitemap);
        response.setContentType(GoogleSitemapConstants.APPLICATION_X_GZIP_CONTENT_TYPE);
        OutputStream os = response.getOutputStream();
        byte[] bytes = new byte[GoogleSitemapConstants.BUFFER_SIZE];
        int count;
        while ((count=is.read(bytes))!=-1) {
            os.write(bytes, 0, count);
        }
        os.flush();
        os.close();
        os=null;
        is.close();
        is=null;
        sitemap=null;
        path=null;
    }
}
