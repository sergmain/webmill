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

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 9:52:42 AM
 *
 * $Id$
 */

package org.riverock.portlet.servlets.view.firm;

import java.io.IOException;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.tools.StringManager;
import org.riverock.portlet.portlets.WebmillErrorPage;
import org.riverock.portlet.tools.HtmlTools;
import org.riverock.sso.a3.AuthSession;
import org.riverock.sso.utils.AuthHelper;
import org.riverock.webmill.portlet.ContextNavigator;
import org.riverock.webmill.portlet.CtxInstance;
import org.riverock.webmill.portlet.PortletTools;

public class FirmDelete extends HttpServlet
{
    private static Logger log = Logger.getLogger(FirmDelete.class);

    public FirmDelete()
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        if (log.isDebugEnabled())
            log.debug("method is POST");

        doGet(request, response);
    }

    public void doGet(HttpServletRequest request_, HttpServletResponse response)
            throws IOException, ServletException
    {
        Writer out = null;
        DatabaseAdapter db_ = null;;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try
        {
            CtxInstance ctxInstance =
                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );

            ContextNavigator.setContentType(response);

            out = response.getWriter();

            AuthSession auth_ = (AuthSession)ctxInstance.getPortletRequest().getUserPrincipal();
            if ( auth_==null )
            {
                WebmillErrorPage.process(out, null, "You have not enough right to execute this operation", "/", "continue");
                return;
            }

            db_ = DatabaseAdapter.getInstance( false );

            // Todo. After implement this portlet as 'real' portlet, not servlet,
            // remove following code, and switch to 'ctxInstance.sCustom' field
            // in just time parameter 'locale-name-package' not used
            // start from
            StringManager sCustom = null;
            String nameLocaleBundle = null;
            nameLocaleBundle = "mill.firm.index";
            if ((nameLocaleBundle != null) && (nameLocaleBundle.trim().length() != 0))
                sCustom = StringManager.getManager(nameLocaleBundle, ctxInstance.getPortletRequest().getLocale());
            // end where

            String index_page = ctxInstance.url("mill.firm.index");

//            if (ServletTools.isNotInit(request, response, "id_firm", index_page))
//                return;
            Long id_firm = PortletTools.getLong(ctxInstance.getPortletRequest(), "id_firm");
            if (id_firm==null)
                throw new IllegalArgumentException("id_relate_right not initialized");

            if( auth_.isUserInRole("webmill.firm_delete") )
            {

                String v_str =
                    "select a.* "+
                    "from 	MAIN_LIST_FIRM a "+
                    "where  a.ID_FIRM = ? and a.is_deleted=0 and a.ID_FIRM in ";

                switch (db_.getFamaly())
                {
                    case DatabaseManager.MYSQL_FAMALY:
                        String idList = AuthHelper.getGrantedFirmId(db_, ctxInstance.getPortletRequest().getRemoteUser());

                        v_str += " ("+idList+") ";

                        break;
                    default:
                        v_str +=
                            "(select z1.ID_FIRM from v$_read_list_firm z1 where z1.user_login = ?)";
                        break;
                }


                ps = db_.prepareStatement( v_str );
                RsetTools.setLong(ps, 1, id_firm);
                switch (db_.getFamaly())
                {
                    case DatabaseManager.MYSQL_FAMALY:
                        break;
                    default:
                        ps.setString(2, auth_.getUserLogin() );
                        break;
                }
                    rs = ps.executeQuery();

                    if (rs.next())
                    {

              out.write("\r\n");
              out.write(sCustom.getStr("del_firm.jsp.confirm"));
              out.write("\r\n");
              out.write("<BR>\r\n");
              out.write("<FORM ACTION=\""+ctxInstance.url("mill.firm.commit_del_firm") );
              out.write("\" METHOD=\"POST\">\r\n");
              out.write("<INPUT TYPE=\"hidden\" NAME=\"id_firm\" VALUE=\"");
              out.write(""+ RsetTools.getLong(rs, "ID_FIRM"));
              out.write("\">\r\n");
              out.write("<INPUT TYPE=\"submit\" VALUE=\"");
              out.write(ctxInstance.getStringManager().getStr("button.delete"));
              out.write("\">\r\n");
              out.write( ctxInstance.getAsForm() );
              out.write("\r\n");
              out.write("</FORM>\r\n");
              out.write("<TABLE  border=\"1\" width=\"100%\" class=\"l\">\r\n");
              out.write("<tr>\r\n");
              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
              out.write(sCustom.getStr("del_firm.jsp.full_name"));
              out.write("</td>\r\n");
              out.write("<td align=\"left\">");
              out.write( RsetTools.getString(rs, "full_name", "&nbsp;"));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr>\r\n");
              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
              out.write(sCustom.getStr("del_firm.jsp.short_name"));
              out.write("</td>\r\n");
              out.write("<td align=\"left\">");
              out.write( RsetTools.getString(rs, "short_name", "&nbsp;"));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr>\r\n");
              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
              out.write(sCustom.getStr("del_firm.jsp.address"));
              out.write("</td>\r\n");
              out.write("<td align=\"left\">");
              out.write( RsetTools.getString(rs, "address", "&nbsp;"));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr>\r\n");
              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
              out.write(sCustom.getStr("del_firm.jsp.telefon_buh"));
              out.write("</td>\r\n");
              out.write("<td align=\"left\">");
              out.write( RsetTools.getString(rs, "telefon_buh", "&nbsp;"));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr>\r\n");
              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
              out.write(sCustom.getStr("del_firm.jsp.telefon_chief"));
              out.write("</td>\r\n");
              out.write("<td align=\"left\">");
              out.write( RsetTools.getString(rs, "telefon_chief", "&nbsp;"));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr>\r\n");
              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
              out.write(sCustom.getStr("del_firm.jsp.chief"));
              out.write("</td>\r\n");
              out.write("<td align=\"left\">");
              out.write( RsetTools.getString(rs, "chief", "&nbsp;"));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr>\r\n");
              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
              out.write(sCustom.getStr("del_firm.jsp.buh"));
              out.write("</td>\r\n");
              out.write("<td align=\"left\">");
              out.write( RsetTools.getString(rs, "buh", "&nbsp;"));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr>\r\n");
              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
              out.write(sCustom.getStr("del_firm.jsp.fax"));
              out.write("</td>\r\n");
              out.write("<td align=\"left\">");
              out.write( RsetTools.getString(rs, "fax", "&nbsp;"));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr>\r\n");
              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
              out.write(sCustom.getStr("del_firm.jsp.email"));
              out.write("</td>\r\n");
              out.write("<td align=\"left\">");
              out.write( RsetTools.getString(rs, "email", "&nbsp;"));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr>\r\n");
              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
              out.write(sCustom.getStr("del_firm.jsp.icq"));
              out.write("</td>\r\n");
              out.write("<td align=\"left\">");
              out.write(""+ RsetTools.getLong(rs, "icq"));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr>\r\n");
              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
              out.write(sCustom.getStr("del_firm.jsp.short_client_info"));
              out.write("</td>\r\n");
              out.write("<td align=\"left\">");
              out.write( RsetTools.getString(rs, "short_client_info", "&nbsp;"));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr>\r\n");
              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
              out.write(sCustom.getStr("del_firm.jsp.url"));
              out.write("</td>\r\n");
              out.write("<td align=\"left\">");
              out.write( RsetTools.getString(rs, "url", "&nbsp;"));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr>\r\n");
              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
              out.write(sCustom.getStr("del_firm.jsp.short_info"));
              out.write("</td>\r\n");
              out.write("<td align=\"left\">");
              out.write( RsetTools.getString(rs, "short_info", "&nbsp;"));
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr>\r\n");
              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
              out.write(sCustom.getStr("del_firm.jsp.is_work"));
              out.write("</td>\r\n");
              out.write("<td align=\"left\">");
              out.write( HtmlTools.printYesNo(rs, "is_work", false, ctxInstance.getPortletRequest().getLocale() ) );
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr>\r\n");
              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
              out.write(sCustom.getStr("del_firm.jsp.is_need_recvizit"));
              out.write("</td>\r\n");
              out.write("<td align=\"left\">");
              out.write( HtmlTools.printYesNo(rs, "is_need_recvizit", false, ctxInstance.getPortletRequest().getLocale() ) );
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr>\r\n");
              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
              out.write(sCustom.getStr("del_firm.jsp.is_need_person"));
              out.write("</td>\r\n");
              out.write("<td align=\"left\">");
              out.write( HtmlTools.printYesNo(rs, "is_need_person", false, ctxInstance.getPortletRequest().getLocale() ) );
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("<tr>\r\n");
              out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
              out.write(sCustom.getStr("del_firm.jsp.is_search"));
              out.write("</td>\r\n");
              out.write("<td align=\"left\">");
              out.write( HtmlTools.printYesNo(rs, "is_search", false, ctxInstance.getPortletRequest().getLocale() ) );
              out.write("</td>\r\n");
              out.write("</tr>\r\n");
              out.write("</TABLE>\r\n");

                    }
            }
              out.write("\r\n");
              out.write("<br>\r\n");
              out.write("<p>");
              out.write("<a href=\"");
              out.write( index_page );
              out.write("\">");
              out.write(ctxInstance.getStringManager().getStr("page.main.3"));
              out.write("</a>");
              out.write("</p>\r\n");

        }
        catch (Exception e)
        {
            log.error(e);
            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));
        }
        finally
        {
            DatabaseManager.close( db_, rs, ps );
            rs = null;
            ps = null;
            db_ = null;
        }
    }
}
