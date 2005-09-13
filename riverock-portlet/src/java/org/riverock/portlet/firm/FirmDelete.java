/*
 * org.riverock.portlet -- Portlet Library
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
package org.riverock.portlet.firm;

import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.portlet.portlets.WebmillErrorPage;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.portlet.tools.HtmlTools;
import org.riverock.sso.a3.AuthSession;
import org.riverock.sso.utils.AuthHelper;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;


/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 9:52:42 AM
 * <p/>
 * $Id$
 */
public final class FirmDelete extends HttpServlet {
    private final static Log log = LogFactory.getLog( FirmDelete.class );

    public FirmDelete() {
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response )
        throws ServletException {
        if( log.isDebugEnabled() )
            log.debug( "method is POST" );

        doGet( request, response );
    }

    public void doGet( HttpServletRequest request_, HttpServletResponse response )
        throws ServletException {
        Writer out = null;
        DatabaseAdapter db_ = null;
        ;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            RenderRequest renderRequest = ( RenderRequest ) request_;
            RenderResponse renderResponse = ( RenderResponse ) response;
            ResourceBundle bundle = ( ResourceBundle ) renderRequest.getAttribute( ContainerConstants.PORTAL_RESOURCE_BUNDLE_ATTRIBUTE );
            ContentTypeTools.setContentType( response, ContentTypeTools.CONTENT_TYPE_UTF8 );
            out = response.getWriter();

            AuthSession auth_ = ( AuthSession ) renderRequest.getUserPrincipal();
            if( auth_ == null ) {
                WebmillErrorPage.process( out, null, "You have not enough right to execute this operation", "/", "continue" );
                return;
            }

            db_ = DatabaseAdapter.getInstance();

            String index_page = PortletService.url( "mill.firm.index", renderRequest, renderResponse );

            Long id_firm = PortletService.getLong( renderRequest, "id_firm" );
            if( id_firm == null )
                throw new IllegalArgumentException( "id_relate_right not initialized" );

            if( auth_.isUserInRole( "webmill.firm_delete" ) ) {

                String v_str =
                    "select a.* " +
                    "from 	MAIN_LIST_FIRM a " +
                    "where  a.ID_FIRM = ? and a.is_deleted=0 and a.ID_FIRM in ";

                switch( db_.getFamaly() ) {
                    case DatabaseManager.MYSQL_FAMALY:
                        String idList = AuthHelper.getGrantedFirmId( db_, renderRequest.getRemoteUser() );

                        v_str += " (" + idList + ") ";

                        break;
                    default:
                        v_str +=
                            "(select z1.ID_FIRM from v$_read_list_firm z1 where z1.user_login = ?)";
                        break;
                }


                ps = db_.prepareStatement( v_str );
                RsetTools.setLong( ps, 1, id_firm );
                switch( db_.getFamaly() ) {
                    case DatabaseManager.MYSQL_FAMALY:
                        break;
                    default:
                        ps.setString( 2, auth_.getUserLogin() );
                        break;
                }
                rs = ps.executeQuery();

                if( rs.next() ) {

                    out.write( "\r\n" );
                    out.write( bundle.getString( "del_firm.jsp.confirm" ) );
                    out.write( "\r\n" );
                    out.write( "<BR>\r\n" );
                    out.write( "<FORM ACTION=\"" + PortletService.url( "mill.firm.commit_del_firm", renderRequest, renderResponse ) );
                    out.write( "\" METHOD=\"POST\">\r\n" );
                    out.write( "<INPUT TYPE=\"hidden\" NAME=\"id_firm\" VALUE=\"" );
                    out.write( "" + RsetTools.getLong( rs, "ID_FIRM" ) );
                    out.write( "\">\r\n" );
                    out.write( "<INPUT TYPE=\"submit\" VALUE=\"" );
                    out.write( bundle.getString( "button.delete" ) );
                    out.write( "\">\r\n" );
                    out.write( "\r\n" );
                    out.write( "</FORM>\r\n" );
                    out.write( "<TABLE  border=\"1\" width=\"100%\" class=\"l\">\r\n" );
                    out.write( "<tr>\r\n" );
                    out.write( "<td align=\"right\" width=\"25%\" class=\"par\">" );
                    out.write( bundle.getString( "del_firm.jsp.full_name" ) );
                    out.write( "</td>\r\n" );
                    out.write( "<td align=\"left\">" );
                    out.write( RsetTools.getString( rs, "full_name", "&nbsp;" ) );
                    out.write( "</td>\r\n" );
                    out.write( "</tr>\r\n" );
                    out.write( "<tr>\r\n" );
                    out.write( "<td align=\"right\" width=\"25%\" class=\"par\">" );
                    out.write( bundle.getString( "del_firm.jsp.short_name" ) );
                    out.write( "</td>\r\n" );
                    out.write( "<td align=\"left\">" );
                    out.write( RsetTools.getString( rs, "short_name", "&nbsp;" ) );
                    out.write( "</td>\r\n" );
                    out.write( "</tr>\r\n" );
                    out.write( "<tr>\r\n" );
                    out.write( "<td align=\"right\" width=\"25%\" class=\"par\">" );
                    out.write( bundle.getString( "del_firm.jsp.address" ) );
                    out.write( "</td>\r\n" );
                    out.write( "<td align=\"left\">" );
                    out.write( RsetTools.getString( rs, "address", "&nbsp;" ) );
                    out.write( "</td>\r\n" );
                    out.write( "</tr>\r\n" );
                    out.write( "<tr>\r\n" );
                    out.write( "<td align=\"right\" width=\"25%\" class=\"par\">" );
                    out.write( bundle.getString( "del_firm.jsp.telefon_buh" ) );
                    out.write( "</td>\r\n" );
                    out.write( "<td align=\"left\">" );
                    out.write( RsetTools.getString( rs, "telefon_buh", "&nbsp;" ) );
                    out.write( "</td>\r\n" );
                    out.write( "</tr>\r\n" );
                    out.write( "<tr>\r\n" );
                    out.write( "<td align=\"right\" width=\"25%\" class=\"par\">" );
                    out.write( bundle.getString( "del_firm.jsp.telefon_chief" ) );
                    out.write( "</td>\r\n" );
                    out.write( "<td align=\"left\">" );
                    out.write( RsetTools.getString( rs, "telefon_chief", "&nbsp;" ) );
                    out.write( "</td>\r\n" );
                    out.write( "</tr>\r\n" );
                    out.write( "<tr>\r\n" );
                    out.write( "<td align=\"right\" width=\"25%\" class=\"par\">" );
                    out.write( bundle.getString( "del_firm.jsp.chief" ) );
                    out.write( "</td>\r\n" );
                    out.write( "<td align=\"left\">" );
                    out.write( RsetTools.getString( rs, "chief", "&nbsp;" ) );
                    out.write( "</td>\r\n" );
                    out.write( "</tr>\r\n" );
                    out.write( "<tr>\r\n" );
                    out.write( "<td align=\"right\" width=\"25%\" class=\"par\">" );
                    out.write( bundle.getString( "del_firm.jsp.buh" ) );
                    out.write( "</td>\r\n" );
                    out.write( "<td align=\"left\">" );
                    out.write( RsetTools.getString( rs, "buh", "&nbsp;" ) );
                    out.write( "</td>\r\n" );
                    out.write( "</tr>\r\n" );
                    out.write( "<tr>\r\n" );
                    out.write( "<td align=\"right\" width=\"25%\" class=\"par\">" );
                    out.write( bundle.getString( "del_firm.jsp.fax" ) );
                    out.write( "</td>\r\n" );
                    out.write( "<td align=\"left\">" );
                    out.write( RsetTools.getString( rs, "fax", "&nbsp;" ) );
                    out.write( "</td>\r\n" );
                    out.write( "</tr>\r\n" );
                    out.write( "<tr>\r\n" );
                    out.write( "<td align=\"right\" width=\"25%\" class=\"par\">" );
                    out.write( bundle.getString( "del_firm.jsp.email" ) );
                    out.write( "</td>\r\n" );
                    out.write( "<td align=\"left\">" );
                    out.write( RsetTools.getString( rs, "email", "&nbsp;" ) );
                    out.write( "</td>\r\n" );
                    out.write( "</tr>\r\n" );
                    out.write( "<tr>\r\n" );
                    out.write( "<td align=\"right\" width=\"25%\" class=\"par\">" );
                    out.write( bundle.getString( "del_firm.jsp.icq" ) );
                    out.write( "</td>\r\n" );
                    out.write( "<td align=\"left\">" );
                    out.write( "" + RsetTools.getLong( rs, "icq" ) );
                    out.write( "</td>\r\n" );
                    out.write( "</tr>\r\n" );
                    out.write( "<tr>\r\n" );
                    out.write( "<td align=\"right\" width=\"25%\" class=\"par\">" );
                    out.write( bundle.getString( "del_firm.jsp.short_client_info" ) );
                    out.write( "</td>\r\n" );
                    out.write( "<td align=\"left\">" );
                    out.write( RsetTools.getString( rs, "short_client_info", "&nbsp;" ) );
                    out.write( "</td>\r\n" );
                    out.write( "</tr>\r\n" );
                    out.write( "<tr>\r\n" );
                    out.write( "<td align=\"right\" width=\"25%\" class=\"par\">" );
                    out.write( bundle.getString( "del_firm.jsp.url" ) );
                    out.write( "</td>\r\n" );
                    out.write( "<td align=\"left\">" );
                    out.write( RsetTools.getString( rs, "url", "&nbsp;" ) );
                    out.write( "</td>\r\n" );
                    out.write( "</tr>\r\n" );
                    out.write( "<tr>\r\n" );
                    out.write( "<td align=\"right\" width=\"25%\" class=\"par\">" );
                    out.write( bundle.getString( "del_firm.jsp.short_info" ) );
                    out.write( "</td>\r\n" );
                    out.write( "<td align=\"left\">" );
                    out.write( RsetTools.getString( rs, "short_info", "&nbsp;" ) );
                    out.write( "</td>\r\n" );
                    out.write( "</tr>\r\n" );
                    out.write( "<tr>\r\n" );
                    out.write( "<td align=\"right\" width=\"25%\" class=\"par\">" );
                    out.write( bundle.getString( "del_firm.jsp.is_work" ) );
                    out.write( "</td>\r\n" );
                    out.write( "<td align=\"left\">" );
                    out.write( HtmlTools.printYesNo( rs, "is_work", false, bundle ) );
                    out.write( "</td>\r\n" );
                    out.write( "</tr>\r\n" );
                    out.write( "<tr>\r\n" );
                    out.write( "<td align=\"right\" width=\"25%\" class=\"par\">" );
                    out.write( bundle.getString( "del_firm.jsp.is_need_recvizit" ) );
                    out.write( "</td>\r\n" );
                    out.write( "<td align=\"left\">" );
                    out.write( HtmlTools.printYesNo( rs, "is_need_recvizit", false, bundle ) );
                    out.write( "</td>\r\n" );
                    out.write( "</tr>\r\n" );
                    out.write( "<tr>\r\n" );
                    out.write( "<td align=\"right\" width=\"25%\" class=\"par\">" );
                    out.write( bundle.getString( "del_firm.jsp.is_need_person" ) );
                    out.write( "</td>\r\n" );
                    out.write( "<td align=\"left\">" );
                    out.write( HtmlTools.printYesNo( rs, "is_need_person", false, bundle ) );
                    out.write( "</td>\r\n" );
                    out.write( "</tr>\r\n" );
                    out.write( "<tr>\r\n" );
                    out.write( "<td align=\"right\" width=\"25%\" class=\"par\">" );
                    out.write( bundle.getString( "del_firm.jsp.is_search" ) );
                    out.write( "</td>\r\n" );
                    out.write( "<td align=\"left\">" );
                    out.write( HtmlTools.printYesNo( rs, "is_search", false, bundle ) );
                    out.write( "</td>\r\n" );
                    out.write( "</tr>\r\n" );
                    out.write( "</TABLE>\r\n" );

                }
            }
            out.write( "\r\n" );
            out.write( "<br>\r\n" );
            out.write( "<p>" );
            out.write( "<a href=\"" );
            out.write( index_page );
            out.write( "\">" );
            out.write( bundle.getString( "page.main.3" ) );
            out.write( "</a>" );
            out.write( "</p>\r\n" );

        }
        catch( Exception e ) {
            String es = "Error FirmDelete";
            log.error( es, e );
            throw new ServletException( es, e );
        }
        finally {
            DatabaseManager.close( db_, rs, ps );
            rs = null;
            ps = null;
            db_ = null;
        }
    }
}
