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
package org.riverock.webmill.main;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.riverock.common.html.Header;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.port.PortalInfoImpl;

/**
 * @author Serge Maslyukov
 * Date: Nov 22, 2002
 * Time: 3:13:52 PM
 *
 * $Id$
 */
public final class ServletCSS extends HttpServlet {
    private final static Logger log = Logger.getLogger( ServletCSS.class );

    public ServletCSS() {
    }

    public void doPost( final HttpServletRequest request, final HttpServletResponse response )
        throws ServletException, IOException {
        if ( log.isDebugEnabled() ) {
            log.debug( "method is POST" );
        }

        doGet( request, response );
    }

    public void doGet( final HttpServletRequest request, final HttpServletResponse response )
        throws ServletException, IOException {

        DatabaseAdapter db_ = null;
        Writer out = null;
        try {
            response.setContentType( "text/css" );
            out = response.getWriter();

            db_ = DatabaseAdapter.getInstance();
            PortalInfoImpl p = PortalInfoImpl.getInstance( db_, request.getServerName() );

            if ( log.isDebugEnabled() )
                log.debug( "Dynamic status " + p.getSites().getIsCssDynamic() );

            if ( Boolean.TRUE.equals( p.getSites().getIsCssDynamic() ) ) {
                if ( log.isDebugEnabled() ) {
                    log.debug( "ID_SITE " + p.getSites().getIdSite() );
                    log.debug( "p.getDefaultLocale().toString() " + p.getDefaultLocale().toString() );
                    log.debug( "request parameter " + ContainerConstants.NAME_LANG_PARAM + ": " + request.getParameter( ContainerConstants.NAME_LANG_PARAM ) );
                    log.debug( "Referer: " + Header.getReferer( request ) );
                }

                Long siteId = p.getSiteId();
                if ( log.isDebugEnabled() ) {
                    log.debug( "siteId: " + siteId );
                }

                ContentCSS css = ContentCSS.getInstance( db_, siteId );
                if ( css == null || css.getIsEmpty() ) {
                    out.write( "<style type=\"text/css\"><!-- --></style>" );
                }
                else {
                    out.write( css.getCss() );
                }
                return;
            }

            String cssFile = ( p.getSites().getCssFile() != null ?p.getSites().getCssFile() :"//styles.css" );

            RequestDispatcher dispatcher = request.getRequestDispatcher( cssFile );
            if ( log.isDebugEnabled() ) {
                log.debug( "forvard to static CSS: " + cssFile );
                log.debug( "RequestDispatcher - " + dispatcher );
            }

            if ( dispatcher == null ) {
                if ( log.isDebugEnabled() ) {
                    log.debug( "RequestDispatcher is null" );
                }

                out.write( "Error get dispatcher for path " + cssFile );
            }
            else {
                dispatcher.forward( request, response );
            }
            return;
        }
        catch( Exception e ) {
            final String es = "Error processing ServletCSS";
            log.error( es, e );
            throw new ServletException( es, e );
        }
        finally {
            DatabaseManager.close( db_ );
            db_ = null;
            if (out!=null) {
                out.flush();
                out.close();
                out = null;
            }
        }
    }
}
