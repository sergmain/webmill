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
package org.riverock.webmill.portal.internal_servlet.css;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.riverock.common.html.Header;
import org.riverock.interfaces.ContainerConstants;
import org.riverock.interfaces.portal.PortalInfo;
import org.riverock.interfaces.portal.bean.Css;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * @author Serge Maslyukov
 * Date: Nov 22, 2002
 * Time: 3:13:52 PM
 *
 * $Id: ServletCSS.java 1412 2007-09-07 17:43:10Z serg_main $
 */
public final class ServletCSS {
    private final static Logger log = Logger.getLogger( ServletCSS.class );
    private static final String TEXT_CSS = "text/css";

    public static void doService( HttpServletRequest request, HttpServletResponse response, String realPath, PortalInfo portalInfo )
        throws ServletException, IOException {

        OutputStream out = null;
        try {
            if ( log.isDebugEnabled() )  {
                log.debug( "Dynamic status " + portalInfo.getSite().getCssDynamic() );
            }

            if ( portalInfo.getSite().getCssDynamic() ) {
                if ( log.isDebugEnabled() ) {
                    log.debug( "ID_SITE " + portalInfo.getSite().getSiteId() );
                    log.debug( "p.getDefaultLocale().toString() " + portalInfo.getDefaultLocale().toString() );
                    log.debug( "request parameter " + ContainerConstants.NAME_LANG_PARAM + ": " + request.getParameter( ContainerConstants.NAME_LANG_PARAM ) );
                    log.debug( "Referer: " + Header.getReferer( request ) );
                }

                if ( log.isDebugEnabled() ) {
                    log.debug( "siteId: " + portalInfo.getSiteId() );
                }

                Css css = InternalDaoFactory.getInternalCssDao().getCssCurrent( portalInfo.getSiteId() );
                response.setContentType(TEXT_CSS);
                out = response.getOutputStream();
                if ( css == null ) {
                    out.write( "<style type=\"text/css\"><!-- --></style>".getBytes() );
                }
                else {
                    out.write( css.getCss().getBytes() );
                }
            }
            else {
                String cssFile = ( portalInfo.getSite().getCssFile() != null ?portalInfo.getSite().getCssFile() :"/styles.css" );

                File realPathFile = new File(realPath);
                if (!realPathFile.exists()) {
                    throw new PortalException("Real path not exists, " + realPathFile.getAbsolutePath());
                }

                File file;
                if (cssFile.charAt(0)=='/') {
                    file = new File(realPathFile, cssFile.substring(1));
                }
                else {
                    file = new File(realPathFile, cssFile);
                }
                if (log.isDebugEnabled()) {
                    log.debug("CSS file: " + file.getAbsolutePath());
                }

                if (!file.exists()) {
                    out = response.getOutputStream();
                    out.write( ("<!-- CSS file not exists " + file.getAbsolutePath() + " -->").getBytes() );
                    return;
                }
                InputStream is = new FileInputStream(file);
                response.setContentType(TEXT_CSS);
                out = response.getOutputStream();
                byte[] bytes = new byte[0x200];
                int count;
                while ((count=is.read(bytes))!=-1) {
                    out.write(bytes, 0, count);
                }
                is.close();
                //noinspection UnusedAssignment
                is=null;
            }
        }
        catch( Exception e ) {
            final String es = "Error processing ServletCSS";
            log.error( es, e );
            throw new ServletException( es, e );
        }
        finally {
            if (out!=null) {
                try {
                    out.flush();
                }
                catch (IOException e) {
                    log.error("Error flush CSS output stream");
                }
                try {
                    out.close();
                }
                catch (IOException e) {
                    log.error("Error close CSS output stream");
                }
                //noinspection UnusedAssignment
                out = null;
            }
        }
    }
}
