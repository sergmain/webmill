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



import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.portlet.tools.HtmlTools;

import org.riverock.sso.a3.AuthInfo;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.sso.a3.InternalAuthProvider;

import org.riverock.sso.a3.InternalAuthProviderTools;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.portlet.PortletTools;





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



    public void doGet(HttpServletRequest request_, HttpServletResponse response)

        throws IOException, ServletException

    {

        Writer out = null;

        try

        {



            CtxInstance ctxInstance =

                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );



            out = response.getWriter();



            ContextNavigator.setContentType(response);



            AuthSession auth_ = AuthTools.check(ctxInstance.getPortletRequest(), response, "/");

            if (auth_ == null)

                return;



            AuthInfo authInfo = InternalAuthProvider.getAuthInfo( auth_ );



            DatabaseAdapter db_ = DatabaseAdapter.getInstance(false);

//            InitPage jspPage = new InitPage(db_, request,

//                                            "mill.locale.AUTH_USER"

//            );



            String index_page = CtxURL.url(ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.auth.bind");



//            if (ServletTools.isNotInit(request, response, "id_auth_user", index_page))

//                return;

            Long id_auth_user = PortletTools.getLong(ctxInstance.getPortletRequest(), "id_auth_user");

            if (id_auth_user==null)

                throw new IllegalArgumentException("id_auth_user not initialized");



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

                out.write(ctxInstance.sCustom.getStr("del_bind.jsp.confirm"));

                out.write("\r\n");

                out.write("<BR>\r\n");

                out.write("<FORM ACTION=\"");

                out.write(



                    CtxURL.url(ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.auth.commit_del_bind")



                );

                out.write("\" METHOD=\"POST\">\r\n");

                out.write("<INPUT TYPE=\"hidden\" NAME=\"id_auth_user\" VALUE=\"");

                out.write("" + id_auth_user);

                out.write("\">\r\n");

                out.write("<INPUT TYPE=\"submit\" VALUE=\"");

                out.write(ctxInstance.page.sMain.getStr("button.delete"));

                out.write("\">\r\n");

                out.write(ctxInstance.page.getAsForm());

                out.write("\r\n");

                out.write("</FORM>                                                               \t\r\n");

                out.write("<TABLE  border=\"1\" width=\"100%\" class=\"l\">                       \r\n");

                out.write("<!--tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write("<%=ctxInstance.sCustom.getStr(\"del_bind.jsp.name_firm\")%\\>");

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write("<%= RsetTools.getString(rs, \"name_firm\", \"&nbsp;\")%\\>\r\n");

                out.write("</td>\r\n");

                out.write("</tr-->\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(ctxInstance.sCustom.getStr("del_bind.jsp.user_login"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write(authInfoUser.userLogin);

                out.write("\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(ctxInstance.sCustom.getStr("del_bind.jsp.is_service"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write(HtmlTools.printYesNo(authInfoUser.isService, false, ctxInstance.page.currentLocale));

                out.write("\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(ctxInstance.sCustom.getStr("del_bind.jsp.is_road"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write(HtmlTools.printYesNo(authInfoUser.isRoad, false, ctxInstance.page.currentLocale));

                out.write("\r\n");

                out.write("</td>\r\n");

                out.write("</tr>\r\n");

                out.write("<tr>\r\n");

                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");

                out.write(ctxInstance.sCustom.getStr("del_bind.jsp.is_use_current_firm"));

                out.write("</td>\r\n");

                out.write("<td align=\"left\">\r\n");

                out.write(HtmlTools.printYesNo(authInfoUser.isUseCurrentFirm, false, ctxInstance.page.currentLocale));

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

            out.write(ctxInstance.page.sMain.getStr("page.main.3"));

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

