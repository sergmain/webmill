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

import org.riverock.generic.tools.StringManager;

import org.riverock.portlet.tools.HtmlTools;

import org.riverock.portlet.portlets.WebmillErrorPage;

import org.riverock.sso.a3.AuthInfo;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.sso.a3.InternalAuthProvider;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;



import org.riverock.webmill.portlet.PortletTools;



public class RightDelete extends HttpServlet

{

    private static Logger cat = Logger.getLogger(RightDelete.class);



    public RightDelete()

    {

    }



    public void doPost(HttpServletRequest request, HttpServletResponse response)

        throws IOException, ServletException

    {

        if (cat.isDebugEnabled())

            cat.debug("method is POST");



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



            // Todo. After implement this portlet as 'real' portlet, not servlet,

            // remove following code, and switch to 'ctxInstance.sCustom' field

            // start from

            StringManager sCustom = null;

            String nameLocaleBundle = null;

            nameLocaleBundle = "mill.locale.AUTH_RELATE_RIGHT_ARM";

            if ((nameLocaleBundle != null) && (nameLocaleBundle.trim().length() != 0))

                sCustom = StringManager.getManager(nameLocaleBundle, ctxInstance.getPortletRequest().getLocale());

            // end





            String index_page = ctxInstance.url("mill.auth.right");



            AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );

            if (authInfo.isRoot != 1)

            {

                response.sendRedirect(index_page);

                return;

            }



            Long id_relate_right = PortletTools.getLong(ctxInstance.getPortletRequest(), "id_relate_right");

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

                        out.write(sCustom.getStr("del_right.jsp.confirm"));

                        out.write("\r\n");

                        out.write("<BR>\r\n");

                        out.write("<FORM ACTION=\"");

                        out.write(

                            ctxInstance.url("mill.auth.commit_del_right")



                        );

                        out.write("\" METHOD=\"POST\">\r\n");

                        out.write("<INPUT TYPE=\"hidden\" NAME=\"id_relate_right\" VALUE=\"");

                        out.write("" + RsetTools.getLong(rs, "id_relate_right"));

                        out.write("\">\r\n");

                        out.write("<INPUT TYPE=\"submit\" VALUE=\"");

                        out.write(ctxInstance.getStringManager().getStr("button.delete"));

                        out.write("\">\r\n");

                        out.write(ctxInstance.getAsForm());

                        out.write("\r\n");

                        out.write("</FORM>                                                               \t\r\n");

                        out.write("<TABLE  border=\"1\" width=\"100%\" class=\"l\">                       \r\n\t");

                        out.write("<tr>\r\n\t\t");

                        out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                        out.write(sCustom.getStr("del_right.jsp.id_access_group"));

                        out.write("</td>\r\n\t\t");

                        out.write("<td align=\"left\">\r\n\t\t");

                        out.write(RsetTools.getString(rs, "name_access_group", "&nbsp;"));

                        out.write("\r\n\t\t");

                        out.write("</td>\r\n\t");

                        out.write("</tr>\r\n\t");

                        out.write("<tr>\r\n\t\t");

                        out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                        out.write(sCustom.getStr("del_right.jsp.id_object_arm"));

                        out.write("</td>\r\n\t\t");

                        out.write("<td align=\"left\">\r\n\t\t");



                        String v_str =

                            "арм: " +

                            RsetTools.getString(rs, "name_arm") +

                            ", модуль: " +

                            RsetTools.getString(rs, "name_object_arm") +

                            "; арм: " +

                            RsetTools.getString(rs, "code_arm") +

                            ", модуль: " +

                            RsetTools.getString(rs, "code_object_arm");



                        out.write( v_str );



                        out.write("\n");

                        out.write("</td>\r\n\t");

                        out.write("</tr>\r\n\t");

                        out.write("<tr>\r\n\t\t");

                        out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                        out.write(sCustom.getStr("del_right.jsp.code_right"));

                        out.write("</td>\r\n\t\t");

                        out.write("<td align=\"left\">\r\n\t\t");

                        out.write(RsetTools.getString(rs, "code_right", "&nbsp;"));

                        out.write("\r\n\t\t");

                        out.write("</td>\r\n\t");

                        out.write("</tr>\r\n\t");

                        out.write("<tr>\r\n\t\t");

                        out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                        out.write(sCustom.getStr("del_right.jsp.is_road"));

                        out.write("</td>\r\n\t\t");

                        out.write("<td align=\"left\">\r\n\t\t");

                        out.write(HtmlTools.printYesNo(rs, "is_road", false, ctxInstance.getPortletRequest().getLocale()));

                        out.write("\r\n\t\t");

                        out.write("</td>\r\n\t");

                        out.write("</tr>\r\n\t");

                        out.write("<tr>\r\n\t\t");

                        out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                        out.write(sCustom.getStr("del_right.jsp.is_service"));

                        out.write("</td>\r\n\t\t");

                        out.write("<td align=\"left\">\r\n\t\t");

                        out.write(HtmlTools.printYesNo(rs, "is_service", false, ctxInstance.getPortletRequest().getLocale()));

                        out.write("\r\n\t\t");

                        out.write("</td>\r\n\t");

                        out.write("</tr>\r\n\t");

                        out.write("<tr>\r\n\t\t");

                        out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                        out.write(sCustom.getStr("del_right.jsp.is_firm"));

                        out.write("</td>\r\n\t\t");

                        out.write("<td align=\"left\">\r\n\t\t");

                        out.write(HtmlTools.printYesNo(rs, "is_firm", false, ctxInstance.getPortletRequest().getLocale()));

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

            out.write(ctxInstance.getStringManager().getStr("page.main.3"));

            out.write("</a>");

            out.write("</p>\r\n");

            out.write("</td>\r\n");

            out.write("</tr>\r\n");

            out.write("</table>\r\n");



        }

        catch (Exception e)

        {

            cat.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }

        finally

        {

            DatabaseManager.close(db_);

            db_ = null;

        }

    }

}

