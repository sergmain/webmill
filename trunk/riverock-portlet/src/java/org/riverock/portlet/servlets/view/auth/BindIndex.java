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



import org.riverock.sso.a3.AuthInfo;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.sso.a3.InternalAuthProvider;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.portlet.main.Constants;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.portlet.tools.HtmlTools;



public class BindIndex extends HttpServlet

{

    private static Logger cat = Logger.getLogger(BindIndex.class);



    public BindIndex()

    {

    }



    public void doPost(HttpServletRequest request, HttpServletResponse response)

        throws IOException, ServletException

    {

        if (cat.isDebugEnabled())

            cat.debug("method is POST");



        doGet(request, response);

    }



    public void doGet(HttpServletRequest request, HttpServletResponse response)

        throws IOException, ServletException

    {

        Writer out = null;

        try

        {



            InitPage.setContentType(response);



            out = response.getWriter();



            AuthSession auth_ = AuthTools.check(request, response, "/");

            if (auth_ == null)

                return;



            AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );



            DatabaseAdapter db_ = DatabaseAdapter.getInstance(false);



            InitPage jspPage = new InitPage(db_, request, response,

                "mill.locale.AUTH_USER",

                Constants.NAME_LANG_PARAM, null, null);



            String index_page = CtxURL.url(request, response, jspPage.cross, "mill.auth.bind");



//            String sql_ =

//                "select " +

//                "	a.id_auth_user\n" +

//                "	,name_firm\n" +

//                "	,fio\n" +

//                "	,code_proff\n" +

//                "	,a.id_user\n" +

//                "	,a.user_login\n" +

//                "	,a.is_service\n" +

//                "	,a.is_road\n" +

//                "	,a.is_use_current_firm\n" +

//                "	,a.is_root\n" +

//                "	,a.id_road\n" +

//                "	,a.id_service\n" +

//

//                "from    V_AUTH_USER_FULL a, main_user_info b " +

//                "where   a.id_user = b.id_user and b.ID_FIRM in " +

//                "        (select z1.ID_FIRM from v$_read_list_firm z1 " +

//                "        where z1.user_login = ?) " +

//                "order by name_firm asc, a.fio asc";



            String sql_ =

            "select a.id_auth_user, a.id_user, a.user_login, " +

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





//out.writeln(sql_);

//if (true) return;



            PreparedStatement ps = null;

            ResultSet rs = null;



            try

            {

                if (auth_.isUserInRole("webmill.auth_bind"))

                {



                    ps = db_.prepareStatement(sql_);

                    ps.setString(1, auth_.getUserLogin());

                    ps.setString(2, auth_.getUserLogin());

                    ps.setString(3, auth_.getUserLogin());

                    ps.setString(4, auth_.getUserLogin());

                    rs = ps.executeQuery();



                    out.write("\r\n");

                    out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"2\">\r\n");

                    out.write("<tr>\r\n");

                    out.write("<td valign=\"top\">\r\n        ");





                    out.write("\r\n");

                    out.write("<b>");

                    out.write(jspPage.sCustom.getStr("index.jsp.title"));

                    out.write("</b>\r\n");

                    out.write("<p>");

                    out.write("<a href=\"");

                    out.write(



                        CtxURL.url(request, response, jspPage.cross, "mill.auth.add_bind")



                    );

                    out.write("\">");

                    out.write(jspPage.sMain.getStr("button.add"));

                    out.write("</a>");

                    out.write("</p>\r\n");

                    out.write("<table border=\"0\" class=\"l\">\r\n");

                    out.write("<tr>\r\n");

                    out.write("<th class=\"memberArea\">");

                    out.write(jspPage.sCustom.getStr("index.jsp.name_firm"));

                    out.write("</th>\r\n");

                    out.write("<th class=\"memberArea\">");

                    out.write(jspPage.sCustom.getStr("index.jsp.fio"));

                    out.write("</th>\r\n");



//                    out.write("<th class=\"memberArea\">");

//                    out.write(jspPage.sCustom.getStr("index.jsp.code_proff"));

//                    out.write("</th>\r\n");



                    out.write("<th class=\"memberArea\">");

                    out.write(jspPage.sCustom.getStr("index.jsp.user_login"));

                    out.write("</th>\r\n            ");



                    if (authInfo.isUseCurrentFirm == 1)

                    {

                        out.write("\r\n");

                        out.write("<th class=\"memberArea\" width=\"5%\">");

                        out.write(jspPage.sCustom.getStr("index.jsp.is_use_current_firm"));

                        out.write("</th>\r\n                ");



                    }

                    out.write("\r\n            ");



                    if (authInfo.isService == 1)

                    {

                        out.write("\r\n");

                        out.write("<th class=\"memberArea\" width=\"5%\">");

                        out.write(jspPage.sCustom.getStr("index.jsp.is_service"));

                        out.write("</th>\r\n                ");



                    }

                    out.write("\r\n            ");



                    if (authInfo.isRoad == 1)

                    {

                        out.write("\r\n");

                        out.write("<th class=\"memberArea\" width=\"5%\">");

                        out.write(jspPage.sCustom.getStr("index.jsp.is_road"));

                        out.write("</th>\r\n                ");



                    }

                    out.write("\r\n");

                    out.write("<th class=\"memberArea\">");

                    out.write(jspPage.sCustom.getStr("index.jsp.action"));

                    out.write("</th>\r\n");

                    out.write("</tr>");





                    while (rs.next())

                    {



                        out.write(

                            "<tr>\r\n"+

                            "<td class=\"memberArea\">"+

                            RsetTools.getLong(rs, "ID_FIRM")+", "+

                            RsetTools.getString(rs, "short_name", "&nbsp;")+

                            "</td>\r\n"+



                            "<td class=\"memberArea\">"+

                            RsetTools.getString(rs, "LAST_NAME", "&nbsp;")+" "+

                            RsetTools.getString(rs, "FIRST_NAME", "&nbsp;")+" "+

                            RsetTools.getString(rs, "MIDDLE_NAME", "&nbsp;")+

                            "</td>\r\n"

                        );



//                        out.write("<td class=\"memberArea\">");

//                        out.write(RsetTools.getString(rs, "code_proff", "&nbsp;"));

//                        out.write("</td>\r\n");



                        out.write("<td class=\"memberArea\">");

                        out.write(RsetTools.getString(rs, "user_login", "&nbsp;"));

                        out.write("</td>\r\n                ");



                        if (authInfo.isUseCurrentFirm == 1)

                        {

                            out.write("\r\n");

                            out.write("<td class=\"memberArea\">");

                            out.write(HtmlTools.printYesNo(rs, "is_use_current_firm", false, jspPage.currentLocale));

                            out.write("</td>\r\n                    ");



                        }



                        if (authInfo.isService == 1)

                        {

                            out.write("\r\n");

                            out.write("<td class=\"memberArea\">");

                            out.write(HtmlTools.printYesNo(rs, "is_service", false, jspPage.currentLocale));

                            out.write("</td>\r\n                    ");



                        }



                        if (authInfo.isRoad == 1)

                        {

                            out.write("\r\n");

                            out.write("<td class=\"memberArea\">");

                            out.write(HtmlTools.printYesNo(rs, "is_road", false, jspPage.currentLocale));

                            out.write("</td>\r\n                    ");



                        }



                        out.write("<td class=\"memberAreaAction\">");





                        Long id_auth_user = RsetTools.getLong(rs, "id_auth_user");



                        out.write("<input type=\"button\" value=\"");

                        out.write(jspPage.sMain.getStr("button.change"));

                        out.write("\" onclick=\"location.href='");

                        out.write(



                            CtxURL.url(request, response, jspPage.cross, "mill.auth.ch_bind") + '&'



                        );

                        out.write("id_auth_user=");

                        out.write("" + id_auth_user);

                        out.write("';\">\r\n");

                        out.write("<input type=\"button\" value=\"");

                        out.write(jspPage.sMain.getStr("button.delete"));

                        out.write("\" onclick=\"location.href='");

                        out.write(



                            CtxURL.url(request, response, jspPage.cross, "mill.auth.del_bind") + '&'



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

                    out.write(response.encodeURL("add_bind.jsp"));

                    out.write("?");

                    out.write(jspPage.cross.getAsURL());

                    out.write("\">");

                    out.write(jspPage.sMain.getStr("button.add"));

                    out.write("</a>");

                    out.write("</p>");





                }

                else

                {

                    out.write(jspPage.sMain.getStr("access_denied"));

                }



            }

            catch (Exception e)

            {

                out.write(e.toString());

            }

            finally

            {

                if (rs != null)

                {

                    try

                    {

                        rs.close();

                        rs = null;

                    }

                    catch (Exception e01)

                    {

                    }

                }

                if (ps != null)

                {

                    try

                    {

                        ps.close();

                        ps = null;

                    }

                    catch (Exception e02)

                    {

                    }

                }

            }



            out.write("<p>");

            out.write("<a href=\"");

            out.write(index_page);

            out.write("\">");

            out.write(jspPage.sMain.getStr("page.main.3"));

            out.write("</a>");

            out.write("</p>\r\n");

            out.write("<p>");

            out.write("<a href=\"");

            out.write(response.encodeURL(CtxURL.ctx()));

            out.write("?");

            out.write(jspPage.cross.getAsURL());

            out.write("\">");

            out.write(jspPage.sMain.getStr("page.main.4"));

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



    }

}

