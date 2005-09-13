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
import org.riverock.sso.a3.AuthSession;
import org.riverock.sso.a3.InternalAuthProvider;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 12:10:59 PM
 *
 * $Id$
 */
public final class RightIndex extends HttpServlet
{
    private final static Log log = LogFactory.getLog(RightIndex.class);

    static String AUTH_RIGHT_ROLE = "webmill.auth_right";

    public RightIndex()
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
            ResourceBundle bundle = (ResourceBundle)renderRequest.getAttribute( ContainerConstants.PORTAL_RESOURCE_BUNDLE_ATTRIBUTE );
            ContentTypeTools.setContentType(response, ContentTypeTools.CONTENT_TYPE_UTF8);
            out = response.getWriter();

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
            if ( auth_ == null || !auth_.isUserInRole( RightIndex.AUTH_RIGHT_ROLE ) ) {
                WebmillErrorPage.process(out, null, "You have not enough right to execute this operation", "/", "continue");
                return;
            }

            db_ = DatabaseAdapter.getInstance();

            AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );
            boolean isRootLevel = (authInfo.getRoot() == 1);

            String v_str =
                "select b1.name_arm, a1.name_object_arm, b1.code_arm, a1.code_object_arm, c.NAME_ACCESS_GROUP, "+
                "a.CODE_RIGHT, a.ID_RELATE_RIGHT, a.ID_ACCESS_GROUP, "+
                "a.IS_ROAD, a.IS_SERVICE, a.IS_FIRM "+
                "from AUTH_RELATE_RIGHT_ARM a, auth_object_arm a1, auth_arm b1, AUTH_ACCESS_GROUP c "+
                "where a.ID_ACCESS_GROUP=c.ID_ACCESS_GROUP and "+
                "a.ID_OBJECT_ARM=a1.ID_OBJECT_ARM and "+
                "a1.id_arm = b1.id_arm ";

            if (!isRootLevel)
            {
                v_str +=
                    " and  a.ID_ACCESS_GROUP in " +
                    "	( " +
                    "	select  b1.ID_ACCESS_GROUP " +
                    "	from    AUTH_USER a1, AUTH_RELATE_ACCGROUP b1 " +
                    "	where   a1.USER_LOGIN=? and a1.ID_AUTH_USER=b1.ID_AUTH_USER " +
                    "	) ";
            }
            v_str += "order by c.NAME_ACCESS_GROUP ASC";


            ps = db_.prepareStatement(v_str);
            if (!isRootLevel)
                ps.setString(1, auth_.getUserLogin());

            rs = ps.executeQuery();

            out.write("\r\n");
            out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"2\">\r\n");
            out.write("<tr>\r\n");
            out.write("<td valign=\"top\">\r\n");

            if (auth_.isUserInRole("webmill.auth_right"))
            {
                out.write("\r\n");
                out.write("<b>");
                out.write(bundle.getString("index.jsp.title"));
                out.write("</b>\r\n");
                out.write("<p>");
                out.write("<a href=\"");
                out.write(

                    PortletService.url("mill.auth.add_right", renderRequest, renderResponse )

                );
                out.write("\">");
                out.write(bundle.getString("button.add"));
                out.write("</a>");
                out.write("</p>\r\n");
                out.write("<table border=\"0\">\r\n");
                out.write("<tr>\r\n");
                out.write("<th class=\"memberArea\">");
                out.write(bundle.getString("index.jsp.id_access_group"));
                out.write("</th>\r\n");
                out.write("<th class=\"memberArea\">");
                out.write(bundle.getString("index.jsp.id_object_arm"));
                out.write("</th>\r\n");
                out.write("<th class=\"memberArea\">");
                out.write(bundle.getString("index.jsp.code_right_s"));
                out.write("</th>\r\n");
                out.write("<th class=\"memberArea\">");
                out.write(bundle.getString("index.jsp.code_right_u"));
                out.write("</th>\r\n");
                out.write("<th class=\"memberArea\">");
                out.write(bundle.getString("index.jsp.code_right_i"));
                out.write("</th>\r\n");
                out.write("<th class=\"memberArea\">");
                out.write(bundle.getString("index.jsp.code_right_d"));
                out.write("</th>\r\n");
                out.write("<th class=\"memberArea\">");
                out.write(bundle.getString("index.jsp.code_right_a"));
                out.write("</th>\r\n");
                out.write("<th class=\"memberArea\" width=\"5%\">");
                out.write(bundle.getString("index.jsp.is_road"));
                out.write("</th>\r\n");
                out.write("<th class=\"memberArea\" width=\"5%\">");
                out.write(bundle.getString("index.jsp.is_service"));
                out.write("</th>\r\n");
                out.write("<th class=\"memberArea\" width=\"5%\">");
                out.write(bundle.getString("index.jsp.is_firm"));
                out.write("</th>\r\n        ");

                if (isRootLevel)
                {
                    out.write("\r\n");
                    out.write("<th class=\"memberArea\">");
                    out.write(bundle.getString("index.jsp.action"));
                    out.write("</th>\r\n            ");

                }
                out.write("\r\n");
                out.write("</tr>\r\n        ");

                while (rs.next())
                {
                    out.write("\r\n");
                    out.write("<tr>\r\n");
                    out.write("<td class=\"memberArea\">");

                    v_str =
                        "арм: " +
                        RsetTools.getString(rs, "name_arm") +
                        ", модуль: " +
                        RsetTools.getString(rs, "name_object_arm") +
                        "; арм: " +
                        RsetTools.getString(rs, "code_arm") +
                        ", модуль: " +
                        RsetTools.getString(rs, "code_object_arm");

                    out.write( v_str );

                    out.write("</td>\r\n");
                    out.write("<td class=\"memberArea\">");
                    out.write(RsetTools.getString(rs, "name_access_group", "&nbsp;"));
                    out.write("</td>\r\n");

                    String right = RsetTools.getString(rs, "CODE_RIGHT");
                    int S1 = (right.indexOf('S')==-1?0:1);
                    int U1 = (right.indexOf('U')==-1?0:1);
                    int I1 = (right.indexOf('I')==-1?0:1);
                    int D1 = (right.indexOf('D')==-1?0:1);
                    int A1 = (right.indexOf('A')==-1?0:1);

                    out.write("<td class=\"memberArea\">");
                    out.write(HtmlTools.printYesNo(S1, false, bundle ));
                    out.write("</td>\r\n");
                    out.write("<td class=\"memberArea\">");
                    out.write(HtmlTools.printYesNo(U1, false, bundle ));
                    out.write("</td>\r\n");
                    out.write("<td class=\"memberArea\">");
                    out.write(HtmlTools.printYesNo(I1, false, bundle ));
                    out.write("</td>\r\n");
                    out.write("<td class=\"memberArea\">");
                    out.write(HtmlTools.printYesNo(D1, false, bundle ));
                    out.write("</td>\r\n");
                    out.write("<td class=\"memberArea\">");
                    out.write(HtmlTools.printYesNo(A1, false, bundle ));
                    out.write("</td>\r\n");


                    out.write("<td class=\"memberArea\">");
                    out.write(HtmlTools.printYesNo(rs, "is_road", false, bundle ));
                    out.write("</td>\r\n");
                    out.write("<td class=\"memberArea\">");
                    out.write(HtmlTools.printYesNo(rs, "is_service", false, bundle ));
                    out.write("</td>\r\n");
                    out.write("<td class=\"memberArea\">");
                    out.write(HtmlTools.printYesNo(rs, "is_firm", false, bundle ));
                    out.write("</td>\r\n            ");

                    Long id_relate_right = RsetTools.getLong(rs, "id_relate_right");
                    if (isRootLevel)
                    {
                        out.write("\r\n");
                        out.write("<td class=\"memberAreaAction\">\r\n");
                        out.write("<input type=\"button\" value=\"");
                        out.write(bundle.getString("button.change"));
                        out.write("\" onclick=\"location.href='");
                        out.write(

                            PortletService.url("mill.auth.ch_right", renderRequest, renderResponse ) + '&'

                        );
                        out.write("id_relate_right=");
                        out.write("" + id_relate_right);
                        out.write("';\">\r\n");
                        out.write("<input type=\"button\" value=\"");
                        out.write(bundle.getString("button.delete"));
                        out.write("\" onclick=\"location.href='");
                        out.write(

                            PortletService.url("mill.auth.del_right", renderRequest, renderResponse ) + '&'

                        );
                        out.write("id_relate_right=");
                        out.write("" + id_relate_right);
                        out.write("';\">\r\n");
                        out.write("</td>\r\n                ");
                    }
                    out.write("\r\n");
                    out.write("</tr>\r\n            ");


                }

                out.write("\r\n");
                out.write("</table>\r\n");
                out.write("<p>");
                out.write("<a href=\"");
                out.write(

                    PortletService.url("mill.auth.add_right", renderRequest, renderResponse )

                );
                out.write("\">");
                out.write(bundle.getString("button.add"));
                out.write("</a>");
                out.write("</p>\r\n");

            }
            else
            {
                out.write(bundle.getString("access_denied"));
            }

            out.write("\r\n");
            out.write("</td>\r\n");
            out.write("</tr>\r\n");
            out.write("</table>\r\n\r\n");

        }
        catch (Exception e) {
            String es = "Error RightIndex";
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
