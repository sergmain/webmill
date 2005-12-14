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
 * Time: 12:12:11 PM
 *
 * $Id$
 */

package org.riverock.portlet.auth;

import java.io.IOException;
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
import org.riverock.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.sso.a3.InternalAuthProvider;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;

public final class RightDelete extends HttpServlet
{
    private final static Log log = LogFactory.getLog(RightDelete.class);

    public RightDelete()
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

//                String sql_ =
//                    "select b.FULL_NAME_MODULE NAME_ARM_MODULE, c.NAME_ACCESS_GROUP, " +
//                    "a.CODE_RIGHT, a.ID_RELATE_RIGHT, a.ID_ACCESS_GROUP, " +
//                    "a.IS_ROAD, a.IS_SERVICE, a.IS_FIRM, a.ID_OBJECT_ARM "+
//                    "from AUTH_RELATE_RIGHT_ARM a, V_AUTH_MODULE_FULL b, AUTH_ACCESS_GROUP c "+
//                    "where a.ID_ACCESS_GROUP=c.ID_ACCESS_GROUP and " +
//                    "a.ID_OBJECT_ARM=b.ID_OBJECT_ARM and "+
//                    "a.ID_RELATE_RIGHT=?";

                String sql_ =
                    "select b1.name_arm, a1.name_object_arm, b1.code_arm, a1.code_object_arm, c.NAME_ACCESS_GROUP, "+
                    "a.CODE_RIGHT, a.ID_RELATE_RIGHT, a.ID_ACCESS_GROUP, "+
                    "a.IS_ROAD, a.IS_SERVICE, a.IS_FIRM "+
                    "from AUTH_RELATE_RIGHT_ARM a, auth_object_arm a1, auth_arm b1, AUTH_ACCESS_GROUP c "+
                    "where a.ID_ACCESS_GROUP=c.ID_ACCESS_GROUP and "+
                    "a.ID_OBJECT_ARM=a1.ID_OBJECT_ARM and "+
                    "a1.id_arm = b1.id_arm and "+
                    "a.ID_RELATE_RIGHT=?";

                PreparedStatement ps = null;
                ResultSet rs = null;
                try
                {
                    ps = db_.prepareStatement(sql_);
                    RsetTools.setLong(ps, 1, id_relate_right);

                    rs = ps.executeQuery();
                    if (rs.next())
                    {

                        out.write("\r\n");
                        out.write(bundle.getString("del_right.jsp.confirm"));
                        out.write("\r\n");
                        out.write("<BR>\r\n");
                        out.write("<FORM ACTION=\"");
                        out.write(
                            PortletService.url("mill.auth.commit_del_right", renderRequest, renderResponse )

                        );
                        out.write("\" METHOD=\"POST\">\r\n");
                        out.write("<INPUT TYPE=\"hidden\" NAME=\"id_relate_right\" VALUE=\"");
                        out.write("" + RsetTools.getLong(rs, "id_relate_right"));
                        out.write("\">\r\n");
                        out.write("<INPUT TYPE=\"submit\" VALUE=\"");
                        out.write(bundle.getString("button.delete"));
                        out.write("\">\r\n");
                        out.write("\r\n");
                        out.write("</FORM>                                                               \t\r\n");
                        out.write("<TABLE  border=\"1\" width=\"100%\" class=\"l\">                       \r\n\t");
                        out.write("<tr>\r\n\t\t");
                        out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                        out.write(bundle.getString("del_right.jsp.id_access_group"));
                        out.write("</td>\r\n\t\t");
                        out.write("<td align=\"left\">\r\n\t\t");
                        out.write(RsetTools.getString(rs, "name_access_group", "&nbsp;"));
                        out.write("\r\n\t\t");
                        out.write("</td>\r\n\t");
                        out.write("</tr>\r\n\t");
                        out.write("<tr>\r\n\t\t");
                        out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                        out.write(bundle.getString("del_right.jsp.id_object_arm"));
                        out.write("</td>\r\n\t\t");
                        out.write("<td align=\"left\">\r\n\t\t");

                        String v_str =
                            "���: " +
                            RsetTools.getString(rs, "name_arm") +
                            ", ������: " +
                            RsetTools.getString(rs, "name_object_arm") +
                            "; ���: " +
                            RsetTools.getString(rs, "code_arm") +
                            ", ������: " +
                            RsetTools.getString(rs, "code_object_arm");

                        out.write( v_str );

                        out.write("\n");
                        out.write("</td>\r\n\t");
                        out.write("</tr>\r\n\t");
                        out.write("<tr>\r\n\t\t");
                        out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                        out.write(bundle.getString("del_right.jsp.code_right"));
                        out.write("</td>\r\n\t\t");
                        out.write("<td align=\"left\">\r\n\t\t");
                        out.write(RsetTools.getString(rs, "code_right", "&nbsp;"));
                        out.write("\r\n\t\t");
                        out.write("</td>\r\n\t");
                        out.write("</tr>\r\n\t");
                        out.write("<tr>\r\n\t\t");
                        out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                        out.write(bundle.getString("del_right.jsp.is_road"));
                        out.write("</td>\r\n\t\t");
                        out.write("<td align=\"left\">\r\n\t\t");
                        out.write(HtmlTools.printYesNo(rs, "is_road", false, bundle ));
                        out.write("\r\n\t\t");
                        out.write("</td>\r\n\t");
                        out.write("</tr>\r\n\t");
                        out.write("<tr>\r\n\t\t");
                        out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                        out.write(bundle.getString("del_right.jsp.is_service"));
                        out.write("</td>\r\n\t\t");
                        out.write("<td align=\"left\">\r\n\t\t");
                        out.write(HtmlTools.printYesNo(rs, "is_service", false, bundle ));
                        out.write("\r\n\t\t");
                        out.write("</td>\r\n\t");
                        out.write("</tr>\r\n\t");
                        out.write("<tr>\r\n\t\t");
                        out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                        out.write(bundle.getString("del_right.jsp.is_firm"));
                        out.write("</td>\r\n\t\t");
                        out.write("<td align=\"left\">\r\n\t\t");
                        out.write(HtmlTools.printYesNo(rs, "is_firm", false, bundle ));
                        out.write("\r\n\t\t");
                        out.write("</td>\r\n\t");
                        out.write("</tr>\r\n");
                        out.write("</TABLE>\r\n");

                    }
                }
                finally
                {
                    DatabaseManager.close(rs, ps);
                    rs = null;
                    ps = null;
                }
            }

            out.write("\r\n");
            out.write("<br>\r\n");
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
            String es = "Error RightDelete";
            log.error( es, e );
            throw new ServletException( es, e );
        }
        finally {
            DatabaseManager.close(db_);
            db_ = null;
        }
    }
}