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

 * Time: 12:11:13 PM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.view.auth;



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

import org.riverock.portlet.portlets.WebmillErrorPage;

import org.riverock.sso.a3.AuthInfo;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.InternalAuthProvider;

import org.riverock.tools.Client;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;





public class RightAdd extends HttpServlet

{

    private static Logger log = Logger.getLogger(RightAdd.class);



    public RightAdd()

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

        DatabaseAdapter db_ = null;

        try

        {

            CtxInstance ctxInstance =

                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );



            out = response.getWriter();



            ContextNavigator.setContentType(response);



            AuthSession auth_ = (AuthSession)ctxInstance.getPortletRequest().getUserPrincipal();

            if ( auth_==null )

            {

                WebmillErrorPage.process(out, null, "You have not enough right to execute this operation", "/", "continue");

                return;

            }



            db_ = DatabaseAdapter.getInstance(false);



            String index_page = ctxInstance.url("mill.auth.right");



            AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );

            if (authInfo.isRoot != 1)

            {

                response.sendRedirect(index_page);

                return;

            }



            out.write("\r\n");

            out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"2\" width=\"100%\">\r\n");

            out.write("<tr>\r\n");

            out.write("<td valign=\"top\">\r\n");



            if (auth_.isUserInRole("webmill.auth_right"))

            {

                out.write("\r\n");

                out.write("<form method=\"POST\" action=\"");

                out.write(



                    ctxInstance.url("mill.auth.commit_add_right")



                );

                out.write("\">\r\n");

                out.write("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"1\">\r\n");

                out.write("<tr>\r\n");

                out.write("<th colspan=\"2\">");

                out.write(ctxInstance.sCustom.getStr("add_right.jsp.new_rec"));

                out.write("</th>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n\t");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(ctxInstance.sCustom.getStr("add_right.jsp.id_access_group"));

                out.write("</td>\r\n\t");

                out.write("<td align=\"left\">\r\n");

                out.write("<select name=\"id_access_group\">\r\n");

                out.write(Client.make_list_prn(null, null, db_, "auth_access_group", "id_access_group", "name_access_group", null, "name_access_group"));

                out.write("\r\n");

                out.write("</select>\r\n\t");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(ctxInstance.sCustom.getStr("add_right.jsp.id_object_arm"));

                out.write("</td>\n");

                out.write("<td align=\"left\">\n"+

                    "<select name=\"id_object_arm\">\n"

                );



                String sql =

                    "select distinct a.id_object_arm, a.name_object_arm, a.code_object_arm, " +

                    "name_arm, a.name_object_arm, b.code_arm, a.code_object_arm, " +

                    "a.url, a.order_field, a.string_key, a.is_new " +

                    "from auth_object_arm a, auth_arm b " +

                    "where a.id_arm = b.id_arm ";



                PreparedStatement ps = null;

                ResultSet rs = null;

                long v_id = -1;

                try

                {

                    ps = db_.prepareStatement(sql);



                    rs = ps.executeQuery();



                    long v_num;

                    String v_str;

                    String v_select;



                    while (rs.next())

                    {



                        v_num = rs.getLong(1);



                        if (v_num == v_id)

                            v_select = " SELECTED";

                        else

                            v_select = "";



                        v_str =

                            "арм: " +

                            RsetTools.getString(rs, "name_arm") +

                            ", модуль: " +

                            RsetTools.getString(rs, "name_object_arm") +

                            "; арм: " +

                            RsetTools.getString(rs, "code_arm") +

                            ", модуль: " +

                            RsetTools.getString(rs, "code_object_arm");



                        out.write(

                            "<option" + v_select + " value=\"" + v_num + "\">" +

                            v_str.replace('\"', '\'') +

                            "</option>\n"

                        );

                    }

                }

                finally

                {

                    DatabaseManager.close(rs, ps);

                    rs = null;

                    ps = null;

                }



                out.write(

                    "</select>\n"+

                    "</td>\r"+

                    "</tr>\r"

                );

                out.write("<tr>\r\n\t");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(ctxInstance.sCustom.getStr("add_right.jsp.is_road"));

                out.write("</td>\r\n\t");

                out.write("<td align=\"left\">\r\n\t");

                out.write("<select name=\"is_road\">\r\n\t");

                out.write("<option value=\"0\" selected>");

                out.write(ctxInstance.getStringManager().getStr("yesno.no"));

                out.write("</option>\r\n\t");

                out.write("<option value=\"1\">");

                out.write(ctxInstance.getStringManager().getStr("yesno.yes"));

                out.write("</option>\r\n\t");

                out.write("</select>\r\n\t");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n\t");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(ctxInstance.sCustom.getStr("add_right.jsp.is_service"));

                out.write("</td>\r\n\t");

                out.write("<td align=\"left\">\r\n\t");

                out.write("<select name=\"is_service\">\r\n\t");

                out.write("<option value=\"0\" selected>");

                out.write(ctxInstance.getStringManager().getStr("yesno.no"));

                out.write("</option>\r\n\t");

                out.write("<option value=\"1\">");

                out.write(ctxInstance.getStringManager().getStr("yesno.yes"));

                out.write("</option>\r\n\t");

                out.write("</select>\r\n\t");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n\t");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(ctxInstance.sCustom.getStr("add_right.jsp.is_firm"));

                out.write("</td>\r\n\t");

                out.write("<td align=\"left\">\r\n\t");

                out.write("<select name=\"is_firm\">\r\n\t");

                out.write("<option value=\"0\">");

                out.write(ctxInstance.getStringManager().getStr("yesno.no"));

                out.write("</option>\r\n\t");

                out.write("<option value=\"1\" selected>");

                out.write(ctxInstance.getStringManager().getStr("yesno.yes"));

                out.write("</option>\r\n\t");

                out.write("</select>\r\n\t");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n\t");

                out.write("<td align=\"right\" width=\"33%\" class=\"par\">");

                out.write(ctxInstance.sCustom.getStr("add_right.jsp.code_right_s"));

                out.write("</td>\r\n\t");

                out.write("<td align=\"left\">\r\n\t");

                out.write("<select name=\"rs\">\r\n\t");

                out.write("<option value=\"0\">");

                out.write(ctxInstance.getStringManager().getStr("yesno.no"));

                out.write("</option>\r\n\t");

                out.write("<option value=\"1\">");

                out.write(ctxInstance.getStringManager().getStr("yesno.yes"));

                out.write("</option>\r\n\t");

                out.write("</select>\r\n\t");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n\t");

                out.write("<td align=\"right\" width=\"33%\" class=\"par\">");

                out.write(ctxInstance.sCustom.getStr("add_right.jsp.code_right_i"));

                out.write("</td>\r\n\t");

                out.write("<td align=\"left\">\r\n\t");

                out.write("<select name=\"ri\">\r\n\t");

                out.write("<option value=\"0\">");

                out.write(ctxInstance.getStringManager().getStr("yesno.no"));

                out.write("</option>\r\n\t");

                out.write("<option value=\"1\">");

                out.write(ctxInstance.getStringManager().getStr("yesno.yes"));

                out.write("</option>\r\n\t");

                out.write("</select>\r\n\t");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n\t");

                out.write("<td align=\"right\" width=\"33%\" class=\"par\">");

                out.write(ctxInstance.sCustom.getStr("add_right.jsp.code_right_u"));

                out.write("</td>\r\n\t");

                out.write("<td align=\"left\">\r\n\t");

                out.write("<select name=\"ru\">\r\n\t");

                out.write("<option value=\"0\">");

                out.write(ctxInstance.getStringManager().getStr("yesno.no"));

                out.write("</option>\r\n\t");

                out.write("<option value=\"1\">");

                out.write(ctxInstance.getStringManager().getStr("yesno.yes"));

                out.write("</option>\r\n\t");

                out.write("</select>\r\n\t");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n\t");

                out.write("<td align=\"right\" width=\"33%\" class=\"par\">");

                out.write(ctxInstance.sCustom.getStr("add_right.jsp.code_right_d"));

                out.write("</td>\r\n\t");

                out.write("<td align=\"left\">\r\n\t");

                out.write("<select name=\"rd\">\r\n\t");

                out.write("<option value=\"0\">");

                out.write(ctxInstance.getStringManager().getStr("yesno.no"));

                out.write("</option>\r\n\t");

                out.write("<option value=\"1\">");

                out.write(ctxInstance.getStringManager().getStr("yesno.yes"));

                out.write("</option>\r\n\t");

                out.write("</select>\r\n\t");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n\t");

                out.write("<td align=\"right\" width=\"33%\" class=\"par\">");

                out.write(ctxInstance.sCustom.getStr("add_right.jsp.code_right_a"));

                out.write("</td>\r\n\t");

                out.write("<td align=\"left\">\r\n\t");

                out.write("<select name=\"ra\">\r\n\t");

                out.write("<option value=\"0\">");

                out.write(ctxInstance.getStringManager().getStr("yesno.no"));

                out.write("</option>\r\n\t");

                out.write("<option value=\"1\">");

                out.write(ctxInstance.getStringManager().getStr("yesno.yes"));

                out.write("</option>\r\n\t");

                out.write("</select>\r\n\t");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("</table>\r\n");

                out.write("<br>\r\n");

                out.write("<table width=\"100%\" border=\"0\">\r\n");

                out.write("<tr align=\"center\">\r\n");

                out.write("<td>\r\n");

                out.write("<input type=\"submit\" class=\"par\" value=\"");

                out.write(ctxInstance.getStringManager().getStr("button.add"));

                out.write("\">\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("</table>\r\n");

                out.write("</form>\r\n");



            }



// <p><a href="%= index_page %">%=ctxInstance.getStringManager().getStr("page.main.3")></a></p>



            out.write("\r\n");

            out.write("<br>\r\n");

            out.write("</td>\r\n");

            out.write("</tr>\r\n");

            out.write("</table>\r\n");

            out.write("<p>");

            out.write("<a href=\"");

            out.write(index_page);

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

            DatabaseManager.close(db_);

            db_ = null;

        }



    }

}

