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

 * Time: 12:10:59 PM

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

import org.riverock.portlet.tools.HtmlTools;

import org.riverock.sso.a3.AuthInfo;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.InternalAuthProvider;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;



public class RightIndex extends HttpServlet

{

    private static Logger log = Logger.getLogger(RightIndex.class);



    public RightIndex()

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

        PreparedStatement ps = null;

        ResultSet rs = null;

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



            db_ = DatabaseAdapter.getInstance(false);



//            InitPage jspPage = new InitPage(db_, request,

//                                            "mill.locale.AUTH_RELATE_RIGHT_ARM"

//            );



            AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );

            boolean isRootLevel = (authInfo.isRoot == 1);



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

                out.write(ctxInstance.sCustom.getStr("index.jsp.title"));

                out.write("</b>\r\n");

                out.write("<p>");

                out.write("<a href=\"");

                out.write(



                    ctxInstance.url("mill.auth.add_right")



                );

                out.write("\">");

                out.write(ctxInstance.page.sMain.getStr("button.add"));

                out.write("</a>");

                out.write("</p>\r\n");

                out.write("<table border=\"0\">\r\n");

                out.write("<tr>\r\n");

                out.write("<th class=\"memberArea\">");

                out.write(ctxInstance.sCustom.getStr("index.jsp.id_access_group"));

                out.write("</th>\r\n");

                out.write("<th class=\"memberArea\">");

                out.write(ctxInstance.sCustom.getStr("index.jsp.id_object_arm"));

                out.write("</th>\r\n");

                out.write("<th class=\"memberArea\">");

                out.write(ctxInstance.sCustom.getStr("index.jsp.code_right_s"));

                out.write("</th>\r\n");

                out.write("<th class=\"memberArea\">");

                out.write(ctxInstance.sCustom.getStr("index.jsp.code_right_u"));

                out.write("</th>\r\n");

                out.write("<th class=\"memberArea\">");

                out.write(ctxInstance.sCustom.getStr("index.jsp.code_right_i"));

                out.write("</th>\r\n");

                out.write("<th class=\"memberArea\">");

                out.write(ctxInstance.sCustom.getStr("index.jsp.code_right_d"));

                out.write("</th>\r\n");

                out.write("<th class=\"memberArea\">");

                out.write(ctxInstance.sCustom.getStr("index.jsp.code_right_a"));

                out.write("</th>\r\n");

                out.write("<th class=\"memberArea\" width=\"5%\">");

                out.write(ctxInstance.sCustom.getStr("index.jsp.is_road"));

                out.write("</th>\r\n");

                out.write("<th class=\"memberArea\" width=\"5%\">");

                out.write(ctxInstance.sCustom.getStr("index.jsp.is_service"));

                out.write("</th>\r\n");

                out.write("<th class=\"memberArea\" width=\"5%\">");

                out.write(ctxInstance.sCustom.getStr("index.jsp.is_firm"));

                out.write("</th>\r\n        ");



                if (isRootLevel)

                {

                    out.write("\r\n");

                    out.write("<th class=\"memberArea\">");

                    out.write(ctxInstance.sCustom.getStr("index.jsp.action"));

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

                    out.write(HtmlTools.printYesNo(S1, false, ctxInstance.getPortletRequest().getLocale()));

                    out.write("</td>\r\n");

                    out.write("<td class=\"memberArea\">");

                    out.write(HtmlTools.printYesNo(U1, false, ctxInstance.getPortletRequest().getLocale()));

                    out.write("</td>\r\n");

                    out.write("<td class=\"memberArea\">");

                    out.write(HtmlTools.printYesNo(I1, false, ctxInstance.getPortletRequest().getLocale()));

                    out.write("</td>\r\n");

                    out.write("<td class=\"memberArea\">");

                    out.write(HtmlTools.printYesNo(D1, false, ctxInstance.getPortletRequest().getLocale()));

                    out.write("</td>\r\n");

                    out.write("<td class=\"memberArea\">");

                    out.write(HtmlTools.printYesNo(A1, false, ctxInstance.getPortletRequest().getLocale()));

                    out.write("</td>\r\n");





                    out.write("<td class=\"memberArea\">");

                    out.write(HtmlTools.printYesNo(rs, "is_road", false, ctxInstance.getPortletRequest().getLocale()));

                    out.write("</td>\r\n");

                    out.write("<td class=\"memberArea\">");

                    out.write(HtmlTools.printYesNo(rs, "is_service", false, ctxInstance.getPortletRequest().getLocale()));

                    out.write("</td>\r\n");

                    out.write("<td class=\"memberArea\">");

                    out.write(HtmlTools.printYesNo(rs, "is_firm", false, ctxInstance.getPortletRequest().getLocale()));

                    out.write("</td>\r\n            ");



                    Long id_relate_right = RsetTools.getLong(rs, "id_relate_right");

                    if (isRootLevel)

                    {

                        out.write("\r\n");

                        out.write("<td class=\"memberAreaAction\">\r\n");

                        out.write("<input type=\"button\" value=\"");

                        out.write(ctxInstance.page.sMain.getStr("button.change"));

                        out.write("\" onclick=\"location.href='");

                        out.write(



                            ctxInstance.url("mill.auth.ch_right") + '&'



                        );

                        out.write("id_relate_right=");

                        out.write("" + id_relate_right);

                        out.write("';\">\r\n");

                        out.write("<input type=\"button\" value=\"");

                        out.write(ctxInstance.page.sMain.getStr("button.delete"));

                        out.write("\" onclick=\"location.href='");

                        out.write(



                            ctxInstance.url("mill.auth.del_right") + '&'



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



                    ctxInstance.url("mill.auth.add_right")



                );

                out.write("\">");

                out.write(ctxInstance.page.sMain.getStr("button.add"));

                out.write("</a>");

                out.write("</p>\r\n");



            }

            else

            {

                out.write(ctxInstance.page.sMain.getStr("access_denied"));

            }



            out.write("\r\n");

            out.write("</td>\r\n");

            out.write("</tr>\r\n");

            out.write("</table>\r\n\r\n");



        }

        catch (Exception e)

        {

            log.error(e);

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

