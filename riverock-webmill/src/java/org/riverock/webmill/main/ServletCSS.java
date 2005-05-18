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

import java.io.Writer;
import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;

import org.apache.log4j.Logger;

import org.riverock.webmill.port.PortalInfo;
import org.riverock.webmill.portlet.ContextFactory;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.tools.servlet.HttpServletRequestWrapperFromString;
import org.riverock.common.tools.StringTools;
import org.riverock.common.html.Header;

/**
 * Author: mill
 * Date: Nov 22, 2002
 * Time: 3:13:52 PM
 * <p/>
 * $Id$
 */
public final class ServletCSS extends HttpServlet {

    private final static Logger log = Logger.getLogger( ServletCSS.class );

    public ServletCSS() {
    }

    protected void finalize() throws Throwable {
        super.finalize();
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
            PortalInfo p = PortalInfo.getInstance( db_, request.getServerName() );

            if ( log.isDebugEnabled() )
                log.debug( "Dynamic status " + p.getSites().getIsCssDynamic() );

            if ( Boolean.TRUE.equals( p.getSites().getIsCssDynamic() ) ) {
                if ( log.isDebugEnabled() ) {
                    log.debug( "ID_SITE " + p.getSites().getIdSite() );
                    log.debug( "p.getDefaultLocale().toString() " + p.getDefaultLocale().toString() );
                    log.debug( "request parameter " + Constants.NAME_LANG_PARAM + ": " + request.getParameter( Constants.NAME_LANG_PARAM ) );
                    log.debug( "Referer: " + Header.getReferer( request ) );
                }

                Locale locale = null;
                Object obj = request.getParameter( Constants.NAME_LANG_PARAM );
                if (obj!=null) {
                    locale = StringTools.getLocale( obj.toString() );
                    if ( log.isDebugEnabled() ) {
                        log.debug( "locale from param: '" + locale.toString() + "'" );
                    }
                }

                if (locale==null) {
                    HttpServletRequestWrapperFromString wrapper =
                        new HttpServletRequestWrapperFromString(request, Header.getReferer( request ) );

                    final boolean flag = request.getServerName().equals( wrapper.getServerName()) &&
                                            (request.getServerPort()!=-1?request.getServerPort():80 )==wrapper.getServerPort() &&
                                            request.getContextPath().equals( wrapper.getContextPath());

                    if (log.isDebugEnabled()) {
                        log.debug( "locale not found in parameter and header. Start parse referer");
                        log.debug( "Referer: " + Header.getReferer( request ) );
                        log.debug( "Referer serverName: " +wrapper.getServerName()+", current serverName: " +request.getServerName());
                        log.debug( "Referer contextPath: " +wrapper.getContextPath()+", current contextPath: " +request.getContextPath());
                        log.debug( "Referer serverPort: " +wrapper.getServerPort()+", current serverPort: " +request.getServerPort());
                        log.debug( "Referer is current context: " + flag);
                    }

                    if (flag) {
                        ContextFactory contextFactory = ContextFactory.initTypeContext( db_, wrapper, p, null );
                        locale = contextFactory.getRealLocale();
                        if (log.isDebugEnabled()) {
                            log.debug( "locale extracted from referer: " + locale.toString() );
                        }
                    }
                }
                if (locale==null) {
                    if ( log.isDebugEnabled() ) {
                        log.debug(
                            "locale from ContextFactory is null. " +
                            "Use default for this server locale: " + p.getDefaultLocale() );
                    }
                    locale = p.getDefaultLocale();
                }

                Long siteSupportLanguageId = p.getIdSupportLanguage( locale );
                if ( log.isDebugEnabled() ) {
                    log.debug( "siteSupportLanguageId: " + siteSupportLanguageId );
                }

                ContentCSS css = ContentCSS.getInstance( db_, siteSupportLanguageId );
                if ( css == null || css.getIsEmpty() ) {
                    out.write( "<style type=\"text/css\"><!-- -->" );
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
            if (out!=null) {
                out.flush();
                out.close();
                out = null;
            }
        }
    }
}
