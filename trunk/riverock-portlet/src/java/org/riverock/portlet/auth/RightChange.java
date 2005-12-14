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

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;

import org.riverock.portlet.portlets.WebmillErrorPage;
import org.riverock.portlet.tools.HtmlTools;
import org.riverock.portlet.tools.Client;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.sso.a3.InternalAuthProvider;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.ContainerConstants;

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 12:11:58 PM
 *
 * $Id$
 */
public final class RightChange extends HttpServlet
{
    private final static Log log = LogFactory.getLog(RightChange.class);

    public RightChange()
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        if (log.isDebugEnabled())
            log.debug("method is POST");

        doGet(request, response);
    }

    public void doGet(HttpServletRequest request_, HttpServletResponse response)
        throws ServletException
    {
        Writer out = null;
        DatabaseAdapter db_ = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            RenderRequest renderRequest = (RenderRequest)request_;
            RenderResponse renderResponse= (RenderResponse)response;
//            ResourceBundle bundle = (ResourceBundle)renderRequest.getAttribute( ContainerConstants.PORTAL_RESOURCE_BUNDLE_ATTRIBUTE );
            ResourceBundle bundle = ResourceBundle.getBundle("org.riverock.portlet.resource.AuthRelateRightArm", renderRequest.getLocale() );

            ContentTypeTools.setContentType(response, ContentTypeTools.CONTENT_TYPE_UTF8);
            out = response.getWriter();

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
            if ( auth_ == null || !auth_.isUserInRole( RightIndex.AUTH_RIGHT_ROLE ) ) {
                WebmillErrorPage.process(out, null, "You have not enough right to execute this operation", "/", "continue");
                return;
            }

            db_ = DatabaseAdapter.getInstance();

            String index_page = PortletService.url("mill.auth.right", renderRequest, renderResponse );

            AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );
            if (authInfo.getRoot() != 1)
            {
                response.sendRedirect(index_page);
                return;
            }

            Long id_relate_right = PortletService.getLong(renderRequest, "id_relate_right");
            if (id_relate_right==null)
                throw new IllegalArgumentException("id_relate_right not initialized");

            out.write("\r\n");
            out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"2\" width=\"100%\">\r\n");
            out.write("<tr>\r\n");
            out.write("<td valign=\"top\">\r\n");

            if (auth_.isUserInRole("webmill.auth_right"))
            {

                String v_str =
                    "select c.NAME_ACCESS_GROUP, a.CODE_RIGHT, a.ID_RELATE_RIGHT, a.ID_ACCESS_GROUP, " +
                    "a.IS_ROAD, a.IS_SERVICE, a.IS_FIRM, a.ID_OBJECT_ARM "+
                    "from AUTH_RELATE_RIGHT_ARM a, AUTH_ACCESS_GROUP c "+
                    "where a.ID_ACCESS_GROUP=c.ID_ACCESS_GROUP and " +
                    "a.ID_RELATE_RIGHT=?";

                ps = db_.prepareStatement(v_str);
                RsetTools.setLong(ps, 1, id_relate_right);

                rs = ps.executeQuery();

                if (rs.next())
                {

                    out.write("\r\n");
                    out.write("<FORM ACTION=\"");
                    out.write(
                        PortletService.url("mill.auth.commit_ch_right", renderRequest, renderResponse )

                    );
                    out.write("\" METHOD=\"POST\">\r\n");
                    out.write("<input type=\"submit\" value=\"");
                    out.write(bundle.getString("button.change"));
                    out.write("\">\r\n");
                    out.write("<INPUT TYPE=\"hidden\" NAME=\"id_relate_right\" VALUE=\"");
                    out.write("" + id_relate_right);
                    out.write("\">      \r\n");
                    out.write("<TABLE  border=\"0\" width=\"100%\" class=\"l\">                                  \r\n\t");
                    out.write("<tr>\r\n\t\t");
                    out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                    out.write(bundle.getString("ch_right.jsp.id_access_group"));
                    out.write("</td>\r\n\t\t");
                    out.write("<td align=\"left\">\r\n");
                    out.write("<select name=\"id_access_group\">\r\n");
                    out.write(Client.make_list_prn(rs, "ID_ACCESS_GROUP", db_, "AUTH_ACCESS_GROUP", "ID_ACCESS_GROUP", "NAME_ACCESS_GROUP"));
                    out.write("\r\n");
                    out.write("</select>\r\n\t\t");
                    out.write("</td>\r\n\t");
                    out.write("</tr>\r\n\t");
                    out.write("<tr>\r\n\t\t");
                    out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                    out.write(bundle.getString("ch_right.jsp.id_object_arm"));
                    out.write("</td>\r\n\t\t");
                    out.write("<td align=\"left\">\r\n");
                    out.write("<select name=\"id_object_arm\">\r\n");


                    String sql =
                        "select distinct a.id_object_arm, a.name_object_arm,  " +
                        "name_arm, a.name_object_arm, b.code_arm, a.code_object_arm, " +
                        "a.url, a.order_field, a.string_key, a.is_new " +
                        "from auth_object_arm a, auth_arm b " +
                        "where a.id_arm = b.id_arm ";

                    PreparedStatement ps1 = null;
                    ResultSet rs1 = null;
                    Long v_id = RsetTools.getLong(rs, "ID_OBJECT_ARM");
                    try
                    {
                        ps1 = db_.prepareStatement(sql);

                        rs1 = ps1.executeQuery();

                        Long v_num;
                        String v_select;

                        while (rs1.next())
                        {

                            v_num = RsetTools.getLong(rs1, "id_object_arm");

                            if (v_id.equals(v_num))
                                v_select = " SELECTED";
                            else
                                v_select = "";

                            v_str =
                                "���: " +
                                RsetTools.getString(rs1, "name_arm") +
                                ", ������: " +
                                RsetTools.getString(rs1, "name_object_arm") +
                                "; ���: " +
                                RsetTools.getString(rs1, "code_arm") +
                                ", ������: " +
                                RsetTools.getString(rs1, "code_object_arm");

                            out.write(
                                "<option" + v_select + " value=\"" + v_num + "\">" +
                                v_str.replace('\"', '\'') +
                                "</option>\n"
                            );
                        }
                    }
                    finally
                    {
                        DatabaseManager.close(rs1, ps1);
                        rs1 = null;
                        ps1 = null;
                    }

                    out.write(" \r\n");
                    out.write("</select>\r\n\t\t");
                    out.write("</td>\r\n\t");
                    out.write("</tr>\r\n\t");

                    String right = RsetTools.getString(rs, "CODE_RIGHT");
                    int S1 = (right.indexOf('S')==-1?0:1);
                    int U1 = (right.indexOf('U')==-1?0:1);
                    int I1 = (right.indexOf('I')==-1?0:1);
                    int D1 = (right.indexOf('D')==-1?0:1);
                    int A1 = (right.indexOf('A')==-1?0:1);

                    out.write("<tr>\n");
                    out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                    out.write(bundle.getString("ch_right.jsp.code_right_s"));
                    out.write("</td>\r\n            ");
                    out.write("<td align=\"left\">\r\n\t\t");
                    out.write("<SELECT NAME=\"rs\" SIZE=\"1\">\r\n\t\t");
                    out.write(HtmlTools.printYesNo(S1, true, bundle ));
                    out.write("\r\n\t\t");
                    out.write("</SELECT>\r\n");
                    out.write("</td>\r\n\t");
                    out.write("</tr>\r\n\r\n\r\n\t");
                    out.write("<tr>\r\n            ");
                    out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                    out.write(bundle.getString("ch_right.jsp.code_right_u"));
                    out.write("</td>\n");
                    out.write("<td align=\"left\">\n");
                    out.write("<SELECT NAME=\"ru\" SIZE=\"1\">\r\n\t\t");
                    out.write(HtmlTools.printYesNo(U1, true, bundle ));
                    out.write("\r\n\t\t");
                    out.write("</SELECT>\r\n\t    ");
                    out.write("</td>\r\n\t");
                    out.write("</tr>\r\n\r\n\r\n\t");
                    out.write("<tr>\r\n            ");
                    out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                    out.write(bundle.getString("ch_right.jsp.code_right_i"));
                    out.write("</td>\r\n            ");
                    out.write("<td align=\"left\">\r\n\t\t");
                    out.write("<SELECT NAME=\"ri\" SIZE=\"1\">\r\n\t\t");
                    out.write(HtmlTools.printYesNo(I1, true, bundle ));
                    out.write("\r\n\t\t");
                    out.write("</SELECT>\r\n            ");
                    out.write("</td>\r\n\t");
                    out.write("</tr>\r\n\r\n\r\n\t");
                    out.write("<tr>\r\n            ");
                    out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                    out.write(bundle.getString("ch_right.jsp.code_right_d"));
                    out.write("</td>\r\n            ");
                    out.write("<td align=\"left\">\r\n\t\t");
                    out.write("<SELECT NAME=\"rd\" SIZE=\"1\">\r\n\t\t");
                    out.write(HtmlTools.printYesNo(D1, true, bundle ));
                    out.write("\r\n\t\t");
                    out.write("</SELECT>\r\n            ");
                    out.write("</td>\r\n\t");
                    out.write("</tr>\r\n\r\n\r\n\t");
                    out.write("<tr>\r\n            ");
                    out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                    out.write(bundle.getString("ch_right.jsp.code_right_a"));
                    out.write("</td>\r\n            ");
                    out.write("<td align=\"left\">\r\n\t\t");
                    out.write("<SELECT NAME=\"ra\" SIZE=\"1\">\r\n\t\t");
                    out.write(HtmlTools.printYesNo(A1, true, bundle ));
                    out.write("\r\n\t\t");
                    out.write("</SELECT>\r\n            ");
                    out.write("</td>\r\n\t");
                    out.write("</tr>\r\n\r\n\t");
                    out.write("<tr>\r\n\t\t");
                    out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                    out.write(bundle.getString("ch_right.jsp.is_road"));
                    out.write("</td>\r\n\t\t");
                    out.write("<td align=\"left\">\r\n\t\t");
                    out.write("<select name=\"is_road\" size=\"1\">\r\n\t\t");
                    out.write(HtmlTools.printYesNo(rs, "IS_ROAD", true, bundle ));
                    out.write("\r\n\t\t");
                    out.write("</select>\r\n\t\t");
                    out.write("</td>\r\n\t");
                    out.write("</tr>\r\n\t");
                    out.write("<tr>\r\n\t\t");
                    out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                    out.write(bundle.getString("ch_right.jsp.is_service"));
                    out.write("</td>\r\n\t\t");
                    out.write("<td align=\"left\">\r\n\t\t");
                    out.write("<select name=\"is_service\" size=\"1\">\r\n\t\t");
                    out.write(HtmlTools.printYesNo(rs, "IS_SERVICE", true, bundle ));
                    out.write("\r\n\t\t");
                    out.write("</select>\r\n\t\t");
                    out.write("</td>\r\n\t");
                    out.write("</tr>\r\n\t");
                    out.write("<tr>\r\n\t\t");
                    out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                    out.write(bundle.getString("ch_right.jsp.is_firm"));
                    out.write("</td>\r\n\t\t");
                    out.write("<td align=\"left\">\r\n\t\t");
                    out.write("<select name=\"is_firm\" size=\"1\">\r\n\t\t");
                    out.write(HtmlTools.printYesNo(rs, "IS_FIRM", true, bundle ));
                    out.write("\r\n\t\t");
                    out.write("</select>\r\n\t\t");
                    out.write("</td>\r\n\t");
                    out.write("</tr>\r\n");
                    out.write("</TABLE>                                                              \r\n");
                    out.write("<BR>                                                                  \r\n");
                    out.write("<INPUT TYPE=\"submit\" VALUE=\"");
                    out.write(bundle.getString("button.change"));
                    out.write("\">                            \r\n");
                    out.write("</FORM>                                                               \r\n");
                    out.write("<BR>                                                                  \r\n                                                                       \r\n");
                }
                DatabaseManager.close(rs, ps);
                rs = null;
                ps = null;
            }
            out.write("                                                                    \r\n                                                                       \r\n");
            out.write("<p>");
            out.write("<a href=\"");
            out.write(index_page);
            out.write("\">");
            out.write(bundle.getString("page.main.3"));
            out.write("</a>");
            out.write("</p>\r\n");
            out.write("</td>\r\n");
            out.write("</tr>\r\n");
            out.write("</table>\r\n");

        }
        catch (Exception e) {
            String es = "Error RightChange";
            log.error( es, e );
            throw new ServletException( es, e );
        }
        finally {
            DatabaseManager.close(db_, rs, ps);
            rs = null;
            ps = null;
            db_ = null;
        }
    }
}