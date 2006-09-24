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
package org.riverock.webmill.google.sitemap;

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

import org.riverock.webmill.port.PortalInfoImpl;

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

    private static final String SITEMAP_XML = "sitemap.xml.gz";
    private static final String SITEMAP_DIR = "WEB-INF" + File.separatorChar+ "sitemap" + File.separatorChar;
    private static final int BUFFER_SIZE = 1024;
    private static final String APPLICATION_X_GZIP_CONTENT_TYPE = "application/x-gzip";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        PortalInfoImpl p = PortalInfoImpl.getInstance( request.getServerName() );

        if ( log.isDebugEnabled() ) {
            log.debug( "Dynamic status " + p.getSite().getCssDynamic() );
        }

        String sitemapPath = getServletContext().getRealPath("/") + SITEMAP_DIR + p.getSiteId();

        File path = new File(sitemapPath);
        if (log.isDebugEnabled()) {
            log.debug("sitemap dir: " + path);
            log.debug("sitemap dir exists: " + path.exists());
        }
        if (!path.exists()) {
            path.mkdirs();
        }
        File sitemap = new File(path, SITEMAP_XML);
        if (!sitemap.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        InputStream is = new FileInputStream(sitemap);
        response.setContentType(APPLICATION_X_GZIP_CONTENT_TYPE);
        OutputStream os = response.getOutputStream();
        byte[] bytes = new byte[BUFFER_SIZE];
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
