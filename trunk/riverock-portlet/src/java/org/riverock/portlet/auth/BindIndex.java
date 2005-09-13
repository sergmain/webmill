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
 * Time: 11:49:26 AM
 *
 * $Id$
 */

package org.riverock.portlet.auth;

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
import javax.portlet.RenderResponse;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.portlet.portlets.WebmillErrorPage;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.portlet.tools.HtmlTools;
import org.riverock.sso.a3.AuthInfo;
import org.riverock.sso.a3.AuthSession;
import org.riverock.sso.a3.InternalAuthProvider;
import org.riverock.sso.utils.AuthHelper;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;


public final class BindIndex extends HttpServlet
{
    private final static Log log = LogFactory.getLog(BindIndex.class);
    static String AUTH_BIND_ROLE = "webmill.auth_bind";

    public BindIndex(){}

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        if (log.isDebugEnabled())
            log.debug("method is POST");

        doGet(request, response);
    }

    public void doGet(HttpServletRequest request_, HttpServletResponse response)
        throws IOException {

        Writer out = null;
        DatabaseAdapter db_ = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            RenderRequest renderRequest = (RenderRequest)request_;
            RenderResponse renderResponse= (RenderResponse)response;
            ResourceBundle bundle = (ResourceBundle)renderRequest.getAttribute( ContainerConstants.PORTAL_RESOURCE_BUNDLE_ATTRIBUTE );

            ContentTypeTools.setContentType(response, ContentTypeTools.CONTENT_TYPE_UTF8);
            out = response.getWriter();

            AuthSession auth_ = (AuthSession) renderRequest.getUserPrincipal();
            if (auth_ == null || !auth_.isUserInRole(BindIndex.AUTH_BIND_ROLE)) {
                WebmillErrorPage.process(out, null, "You have not enough right to execute this operation", "/", "continue");
                return;
            }

            AuthInfo authInfo = InternalAuthProvider.getAuthInfo(auth_);

            db_ = DatabaseAdapter.getInstance();

            String index_page = PortletService.url("mill.auth.bind", renderRequest, renderResponse );


            String sql_=null;
            switch(db_.getFamaly())
            {
                case DatabaseManager.MYSQL_FAMALY:
                    sql_ = "select a.id_auth_user, a.id_user, a.user_login, " +
                        "                a.is_service, a.is_road, a.is_use_current_firm, a.is_root, a.id_road, " +
                        "                a.id_service, " +
                        "             b.LAST_NAME, b.FIRST_NAME, b.MIDDLE_NAME, " +
                        "             c.id_firm, c.short_name " +
                        " " +
                        "            from    auth_user a, main_user_info b, main_list_firm c " +
                        "            where   a.id_user = b.id_user and " +
                        "                    a.id_firm = c.id_firm and " +
                        "                    b.ID_FIRM in ( " + AuthHelper.getGrantedFirmId(db_, auth_.getUserLogin())+ " ) " +
                        "            order by c.id_firm asc, c.short_name asc, b.LAST_NAME asc, b.FIRST_NAME asc, b.MIDDLE_NAME asc ";

                    ps = db_.prepareStatement(sql_);
                    break;
                default:
                    sql_ = "select a.id_auth_user, a.id_user, a.user_login, " +
                        "                a.is_service, a.is_road, a.is_use_current_firm, a.is_root, a.id_road, " +
                        "                a.id_service, " +
                        "             b.LAST_NAME, b.FIRST_NAME, b.MIDDLE_NAME, " +
                        "             c.id_firm, c.short_name " +
                        " " +
                        "            from    auth_user a, main_user_info b, main_list_firm c " +
                        "            where   a.id_user = b.id_user and " +
                        "                    a.id_firm = c.id_firm and " +
                        "                    b.ID_FIRM in " +
                        "                    ( " +
                        "            select  a01.id_firm " +
                        "            from    auth_user a01 " +
                        "            where   a01.is_use_current_firm = 1 " +
                        "             and a01.user_login = ? " +
                        "            union " +
                        "            select  d02.id_firm " +
                        "            from    auth_user a02, main_relate_service_firm d02 " +
                        "          where   a02.is_service = 1 and a02.id_service = d02.id_service " +
                        "             and a02.user_login = ? " +
                        "            union " +
                        "            select  e03.id_firm " +
                        "            from    auth_user a03, main_relate_road_service d03, main_relate_service_firm e03 " +
                        "            where   a03.is_road = 1 and a03.id_road = d03.id_road and d03.id_service = e03.id_service " +
                        "             and a03.user_login = ? " +
                        "            union " +
                        "            select  b04.id_firm " +
                        "            from    auth_user a04, main_list_firm b04 " +
                        "            where   a04.is_root = 1 and a04.user_login = ? " +
                        "            ) " +
                        "            order by c.id_firm asc, c.short_name asc, b.LAST_NAME asc, b.FIRST_NAME asc, b.MIDDLE_NAME asc ";

                    ps = db_.prepareStatement(sql_);
                    ps.setString(1, auth_.getUserLogin());
                    ps.setString(2, auth_.getUserLogin());
                    ps.setString(3, auth_.getUserLogin());
                    ps.setString(4, auth_.getUserLogin());

                    break;
            }
            rs = ps.executeQuery();

            out.write("\r\n");
            out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"2\">\r\n");
            out.write("<tr>\r\n");
            out.write("<td valign=\"top\">\r\n        ");


            out.write("\r\n");
            out.write("<b>");
            out.write(bundle.getString("index.jsp.title"));
            out.write("</b>\r\n");
            out.write("<p>");
            out.write("<a href=\"");
            out.write( PortletService.url("mill.auth.add_bind", renderRequest, renderResponse ) );
            out.write("\">");
            out.write(bundle.getString("button.add"));
            out.write("</a>");
            out.write("</p>\r\n");
            out.write("<table border=\"0\" class=\"l\">\r\n");
            out.write("<tr>\r\n");
            out.write("<th class=\"memberArea\">");
            out.write(bundle.getString("index.jsp.name_firm"));
            out.write("</th>\r\n");
            out.write("<th class=\"memberArea\">");
            out.write(bundle.getString("index.jsp.fio"));
            out.write("</th>\r\n");

//                    out.write("<th class=\"memberArea\">");
//                    out.write(bundle.getString("index.jsp.code_proff"));
//                    out.write("</th>\r\n");

            out.write("<th class=\"memberArea\">");
            out.write(bundle.getString("index.jsp.user_login"));
            out.write("</th>\r\n            ");

            if (authInfo.getUseCurrentFirm() == 1)
            {
                out.write("\r\n");
                out.write("<th class=\"memberArea\" width=\"5%\">");
                out.write(bundle.getString("index.jsp.is_use_current_firm"));
                out.write("</th>\r\n                ");

            }
            out.write("\r\n            ");

            if (authInfo.getService() == 1)
            {
                out.write("\r\n");
                out.write("<th class=\"memberArea\" width=\"5%\">");
                out.write(bundle.getString("index.jsp.is_service"));
                out.write("</th>\r\n                ");

            }
            out.write("\r\n            ");

            if (authInfo.getRoad() == 1)
            {
                out.write("\r\n");
                out.write("<th class=\"memberArea\" width=\"5%\">");
                out.write(bundle.getString("index.jsp.is_road"));
                out.write("</th>\r\n                ");

            }
            out.write("\r\n");
            out.write("<th class=\"memberArea\">");
            out.write(bundle.getString("index.jsp.action"));
            out.write("</th>\r\n");
            out.write("</tr>");


            while (rs.next())
            {

                out.write(
                    "<tr>\r\n" +
                    "<td class=\"memberArea\">" +
                    RsetTools.getLong(rs, "ID_FIRM") + ", " +
                    RsetTools.getString(rs, "short_name", "&nbsp;") +
                    "</td>\r\n" +

                    "<td class=\"memberArea\">" +
                    RsetTools.getString(rs, "LAST_NAME", "&nbsp;") + " " +
                    RsetTools.getString(rs, "FIRST_NAME", "&nbsp;") + " " +
                    RsetTools.getString(rs, "MIDDLE_NAME", "&nbsp;") +
                    "</td>\r\n"
                );

//                        out.write("<td class=\"memberArea\">");
//                        out.write(RsetTools.getString(rs, "code_proff", "&nbsp;"));
//                        out.write("</td>\r\n");

                out.write("<td class=\"memberArea\">");
                out.write(RsetTools.getString(rs, "user_login", "&nbsp;"));
                out.write("</td>\r\n                ");

                if (authInfo.getUseCurrentFirm() == 1)
                {
                    out.write("\r\n");
                    out.write("<td class=\"memberArea\">");
                    out.write(HtmlTools.printYesNo(rs, "is_use_current_firm", false, bundle ));
                    out.write("</td>\r\n                    ");

                }

                if (authInfo.getService() == 1)
                {
                    out.write("\r\n");
                    out.write("<td class=\"memberArea\">");
                    out.write(HtmlTools.printYesNo(rs, "is_service", false, bundle ));
                    out.write("</td>\r\n                    ");

                }

                if (authInfo.getRoad() == 1)
                {
                    out.write("\r\n");
                    out.write("<td class=\"memberArea\">");
                    out.write(HtmlTools.printYesNo(rs, "is_road", false, bundle ));
                    out.write("</td>\r\n                    ");

                }

                out.write("<td class=\"memberAreaAction\">");


                Long id_auth_user = RsetTools.getLong(rs, "id_auth_user");

                out.write("<input type=\"button\" value=\"");
                out.write(bundle.getString("button.change"));
                out.write("\" onclick=\"location.href='");
                out.write(

                    PortletService.url("mill.auth.ch_bind", renderRequest, renderResponse ) + '&'

                );
                out.write("id_auth_user=");
                out.write("" + id_auth_user);
                out.write("';\">\r\n");
                out.write("<input type=\"button\" value=\"");
                out.write(bundle.getString("button.delete"));
                out.write("\" onclick=\"location.href='");
                out.write(

                    PortletService.url("mill.auth.del_bind", renderRequest, renderResponse ) + '&'

                );
                out.write("id_auth_user=");
                out.write("" + id_auth_user);
                out.write("';\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>");


            }

            out.write("</table>\r\n");
            out.write("<p>");
            out.write("<a href=\"");
            out.write( PortletService.url("mill.auth.add_bind", renderRequest, renderResponse ) );
            out.write("?");
            out.write("\">");
            out.write(bundle.getString("button.add"));
            out.write("</a>");
            out.write("</p>");

            out.write("<p>");
            out.write("<a href=\"");
            out.write(index_page);
            out.write("\">");
            out.write(bundle.getString("page.main.3"));
            out.write("</a>");
            out.write("</p>\r\n");
            out.write("<p>");
            out.write("<a href=\"");
            out.write(response.encodeURL(PortletService.ctx( renderRequest )));
            out.write("\">");
            out.write(bundle.getString("page.main.4"));
            out.write("</a>");
            out.write("</p>\r\n");
            out.write("</td>\r\n");
            out.write("</tr>\r\n");
            out.write("</table>\r\n");

        }
        catch (Exception e)
        {
            log.error("Exception in BindIndex", e);
            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));
        }
        finally
        {
            DatabaseManager.close(db_, rs, ps);
            rs = null;
            ps = null;
            db_ = null;
        }

    }
}
