/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.image;

import java.io.IOException;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.PortletConfig;
import javax.portlet.RenderResponse;


import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.container.tools.PortletService;

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 12:31:59 PM
 *
 * $Id$
 */
public final class ImageList extends HttpServlet {

    private final static Logger cat = Logger.getLogger( ImageList.class );

    public ImageList() {
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response )
        throws IOException, ServletException {
        if ( cat.isDebugEnabled() )
            cat.debug( "method is POST" );

        doGet( request, response );
    }

    public void doGet( HttpServletRequest request_, HttpServletResponse response )
        throws IOException {
        Writer out = null;
        DatabaseAdapter db_ = null;
        try {
            RenderRequest renderRequest = null;
            RenderResponse renderResponse= null;

            PortletConfig portletConfig = null;
            ResourceBundle bundle = portletConfig.getResourceBundle( renderRequest.getLocale() );

            ContentTypeTools.setContentType(response, ContentTypeTools.CONTENT_TYPE_UTF8);

            out = response.getWriter();

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
            if ( auth_ == null ) {
                throw new IllegalStateException( "You have not enough right to execute this operation" );
            }

            db_ = DatabaseAdapter.getInstance();

            String index_page = PortletService.url( "mill.image.index", renderRequest, renderResponse );

            if ( auth_.isUserInRole( "webmill.upload_image" ) ) {

                Long id_main_ = PortletService.getLong( renderRequest, "id_main" );
                long pageNum = PortletService.getInt( renderRequest, "pageNum", 0);
                long countImage = PortletService.getInt( renderRequest, "countImage", 10 );

                String sql_ =
                    "select  a.id_main, a.name_file " +
                    "from    WM_IMAGE_DIR a, WM_AUTH_USER b " +
                    "where   a.id = ? and a.is_group = 1 and " +
                    "a.ID_FIRM = b.ID_FIRM and b.user_login = ?";

                PreparedStatement ps = db_.prepareStatement( sql_ );
                RsetTools.setLong( ps, 1, id_main_ );
                ps.setString( 2, auth_.getUserLogin() );
                ResultSet rs = ps.executeQuery();

                if ( rs.next() ) {
                    out.write( "\r\n" );
                    out.write( "<table border=\"0\" width=\"100%\">\r\n" );
                    out.write( "<tr>\r\n" );
                    out.write( "<td>\r\n" );
                    out.write( "<a href=\"" );
                    out.write( index_page );
                    out.write( "id_main=" );
                    out.write( "" + id_main_ );
                    out.write( "\">\r\n   " );
                    out.write( bundle.getString( "price.top_level" ) );
                    out.write( "</a>\r\n" );
                    out.write( "</td>\r\n" );
                    out.write( "</tr>\r\n" );
                    out.write( "</table>\r\n                " );

                }
                DatabaseManager.close( rs, ps );
                rs = null;
                ps = null;

                out.write( "\r\n" );
                out.write( "<br>\r\n" );
                out.write( "<table border=\"0\" width=\"100%\" cellpadding=\"2px\" cellspacing=\"2px\">\r\n            " );

                sql_ =
                    "select id, id_main, name_file, description " +
                    "from   WM_IMAGE_DIR a, WM_AUTH_USER b \n" +
                    "where  a.id_main = ? and a.ID_FIRM = b.ID_FIRM and a.is_group = 0 " +
                    "	    and b.user_login=? \n" +
                    "order by id asc ";

                ps = db_.prepareStatement( sql_ );
                RsetTools.setLong( ps, 1, id_main_ );
                ps.setString( 2, auth_.getUserLogin() );

                rs = ps.executeQuery();

                int counter = 0;
                while( rs.next() ) {

                    if ( counter>= ( pageNum + 1 )*countImage )
                        break;

                    if ( ( counter>= pageNum*countImage ) && ( counter<( pageNum + 1 )*countImage ) ) {
                        Long id_ = RsetTools.getLong( rs, "id" );
                        out.write( "\r\n" );
                        out.write( "<tr>\r\n" );
                        out.write( "<td class=\"imageDirData\" width=\"100%\">\r\n" );
                        out.write( "<a href=\"" );
                        out.write( PortletService.url( "mill.image.change_desc", renderRequest, renderResponse ) + '&' +
                            "id=" + id_ );
                        out.write( "\">\r\n                    " );
                        out.write( RsetTools.getString( rs, "description" ) );
                        out.write( "\r\n" );
                        out.write( "</a>" );
                        out.write( "<br>\r\n" );
                        out.write( "<img src=\"" );
                        out.write( RsetTools.getString( rs, "name_file" ) );
                        out.write( "\">\r\n" );
                        out.write( "</td>\r\n" );
                        out.write( "</tr>\r\n                    " );

                    }
                    counter++;
                } // end while(rs.next())
                out.write( "\r\n" );
                out.write( "<table>" );
                out.write( "<tr>\r\n" );
                out.write( "<td width=50% align=\"center\">\r\n            " );

                if ( pageNum>0 ) {
                    out.write( "\r\n" );
                    out.write( "<a href=\"" );
                    out.write( PortletService.url( "mill.image.list", renderRequest, renderResponse ) + '&' +
                        "id_main=" + id_main_ + "&pageNum=" + ( pageNum - 1 ) );
                    out.write( "\">prev" );
                    out.write( "</a>\r\n                " );
                }
                out.write( "\r\n" );
                out.write( "</td>\r\n" );
                out.write( "<td width=50% align=\"center\">\r\n            " );

                if ( rs.next() ) {

                    out.write( "\r\n" );
                    out.write( "<a href=\"" );
                    out.write( PortletService.url( "mill.image.list", renderRequest, renderResponse ) + '&' +
                        "id_main=" + id_main_ + "&pageNum=" + ( pageNum + 1 ) );
                    out.write( "\">next" );
                    out.write( "</a>" );
                    out.write( "<br>\r\n                " );

                }
                out.write( "\r\n" );
                out.write( "</td>\r\n" );
                out.write( "</tr>" );
                out.write( "</table>\r\n            " );


                DatabaseManager.close( rs, ps );
                rs = null;
                ps = null;

                out.write( "\r\n" );
                out.write( "</table>\r\n\r\n" );
                out.write( "<a href=\"" );
                out.write( PortletService.url( "mill.image.desc", renderRequest, renderResponse ) + '&' +
                    "id_main=" + id_main_ );
                out.write( "\">upload file" );
                out.write( "</a>" );
                out.write( "<br>\r\n" );
                out.write( "<a href=\"" );
                out.write( response.encodeURL( "a.jsp" ) );
                out.write( "?" );
                out.write( "id_main=" );
                out.write( "" + id_main_ );
                out.write( "\">Browse folder with image" );
                out.write( "</a>" );
                out.write( "<br>\r\n\r\n            " );

            }
        }
        catch( Exception e ) {
            cat.error( e );
            out.write( ExceptionTools.getStackTrace( e, 20, "<br>" ) );
        }
        finally {
            DatabaseAdapter.close( db_ );
            db_ = null;
        }

    }
}
