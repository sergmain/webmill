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

 * Time: 11:55:24 AM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.view.auth;



import java.io.IOException;

import java.io.Writer;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.portlet.main.Constants;

import org.riverock.portlet.tools.HtmlTools;

import org.riverock.sso.a3.AuthInfo;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.sso.a3.InternalAuthProvider;

import org.riverock.sso.a3.InternalAuthProviderTools;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portlet.CtxURL;



import org.apache.log4j.Logger;





public class BindDelete extends HttpServlet

{

    private static Logger cat = Logger.getLogger(BindDelete.class);



    public BindDelete()

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



            InitPage.setContentType(response);



            AuthSession auth_ = AuthTools.check(request, response, "/");

            if (auth_ == null)

                return;



            AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );



            DatabaseAdapter db_ = DatabaseAdapter.getInstance(false);

            InitPage jspPage = new InitPage(db_, request, response,

                "mill.locale.AUTH_USER",

                Constants.NAME_LANG_PARAM, null, null);



//            PortalInfo p = PortalInfo.getInstance(db_, request.getServerName());

//            CrossPageParam cross = new CrossPageParam(request, Constants.NAME_LANG_PARAM, p.defaultLocale, null, null);

//            Locale loc = cross.getLocale();



//            StringManager sm = StringManager.getManager("mill.locale.AUTH_USER", loc);

//            StringManager sm1 = StringManager.getManager("mill.locale.main", loc);



            String index_page = CtxURL.url(request, response, jspPage.cross, "mill.auth.bind");



            if (ServletTools.isNotInit(request, response, "id_auth_user", index_page))

                return;

            Long id_auth_user = ServletTools.getLong(request, "id_auth_user");



            AuthInfo authInfoUser = AuthInfo.getInstance(db_, id_auth_user);

            boolean isRigthRelate = false;



//            MainUserInfo userInfo = null;



            if (authInfoUser != null && authInfo != null)

            {

                isRigthRelate =

                    InternalAuthProviderTools.checkRigthOnUser(db_,

                        authInfoUser.authUserID, authInfo.authUserID);

//                userInfo = new MainUserInfo( db_, authInfoUser.userLogin );

            }



            if (auth_.isUserInRole("webmill.auth_bind") && isRigthRelate)

            {



                out.write("\r\n");

                out.write(jspPage.sCustom.getStr("del_bind.jsp.confirm"));

                out.write("\r\n");

                out.write("<BR>\r\n");

                out.write("<FORM ACTION=\"");

                out.write(



                    CtxURL.url(request, response, jspPage.cross, "mill.auth.commit_del_bind")



                );

                out.write("\" METHOD=\"POST\">\r\n");

                out.write("<INPUT TYPE=\"hidden\" NAME=\"id_auth_user\" VALUE=\"");

                out.write("" + id_auth_user);

                out.write("\">\r\n");

                out.write("<INPUT TYPE=\"submit\" VALUE=\"");

                out.write(jspPage.sMain.getStr("button.delete"));

                out.write("\">\r\n");

                out.write(jspPage.cross.getAsForm());

                out.write("\r\n");

                out.write("</FORM>                                                               \t\r\n");

                out.write("<TABLE  border=\"1\" width=\"100%\" class=\"l\">                       \r\n");

                out.write("<!--tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write("<%=jspPage.sCustom.getStr(\"del_bind.jsp.name_firm\")%\\>");

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<%= RsetTools.getString(rs, \"name_firm\", \"&nbsp;\")%\\>\r\n");

                out.write("</td>\r\n");

                out.write("</tr-->\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("del_bind.jsp.user_login"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write(authInfoUser.userLogin);

                out.write("\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("del_bind.jsp.is_service"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write(HtmlTools.printYesNo(authInfoUser.isService, false, jspPage.currentLocale));

                out.write("\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("del_bind.jsp.is_road"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write(HtmlTools.printYesNo(authInfoUser.isRoad, false, jspPage.currentLocale));

                out.write("\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(jspPage.sCustom.getStr("del_bind.jsp.is_use_current_firm"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write(HtmlTools.printYesNo(authInfoUser.isUseCurrentFirm, false, jspPage.currentLocale));

                out.write("\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("</TABLE>\r\n");





            } // if ( getRight())



            out.write("\r\n");

            out.write("<br>\r\n");

            out.write("<p>");

            out.write("<a href=\"");

            out.write(index_page);

            out.write("\">");

            out.write(jspPage.sMain.getStr("page.main.3"));

            out.write("</a>");

            out.write("</p>\r\n");



        }

        catch (Exception e)

        {

            cat.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }



    }





}

