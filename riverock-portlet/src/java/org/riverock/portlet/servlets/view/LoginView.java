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



package org.riverock.portlet.servlets.view;



import java.io.IOException;

import java.io.Writer;



import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import javax.servlet.ServletException;



import org.apache.log4j.Logger;



import org.riverock.webmill.port.InitPage;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.portlet.main.Constants;

import org.riverock.sso.a3.AuthSession;

import org.riverock.common.tools.ServletTools;

import org.riverock.common.tools.StringTools;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.utils.ServletUtils;



/**

 * Author: mill

 * Date: Dec 2, 2002

 * Time: 4:10:28 PM

 *

 * $Id$

 */

public class LoginView extends HttpServlet

{

    private static Logger cat = Logger.getLogger("org.riverock.servlets.view.LoginView");



    public LoginView()

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



            ContextNavigator.setContentType(response);



            out = response.getWriter();



            if (cat.isDebugEnabled())

                cat.debug("Process input auth data");



            DatabaseAdapter db_ = DatabaseAdapter.getInstance(false);

            InitPage jspPage = new InitPage(db_, request,

                                            "mill.locale.auth"

            );



            HttpSession session = request.getSession();

            AuthSession auth_ = (AuthSession) session.getAttribute(Constants.AUTH_SESSION);



            if (auth_ == null)

            {

                auth_ = new AuthSession(

                        request.getParameter(Constants.NAME_USERNAME_PARAM),

                        request.getParameter(Constants.NAME_PASSWORD_PARAM)

                );

            }



            if (auth_.checkAccess( request.getServerName()))

            {

                if (cat.isDebugEnabled())

                    cat.debug("user " + auth_.getUserLogin() + "is  valid for " + request.getServerName() + " site");



                return;

            }



            out.write( "<form method = \"POST\" action = \"" + CtxURL.ctx() + "\" >");



            out.write(jspPage.getAsForm());

            out.write(ServletTools.getHiddenItem(

                    Constants.NAME_TEMPLATE_CONTEXT_PARAM,

                    ServletUtils.getString(request, Constants.NAME_TEMPLATE_CONTEXT_PARAM)

            )

            );

            out.write(ServletTools.getHiddenItem(

                    Constants.NAME_TYPE_CONTEXT_PARAM,

                    Constants.CTX_TYPE_LOGIN_CHECK

            )

            );



            String srcURL = null;

            if (request.getParameter(Constants.NAME_TOURL_PARAM) != null)

                srcURL = ServletUtils.getString(request, Constants.NAME_TOURL_PARAM);

            else

            {

                srcURL = CtxURL.url(request, response, jspPage, Constants.CTX_TYPE_LOGIN);

            }



            if (cat.isDebugEnabled())

                cat.debug("toUTL - " + srcURL);



            srcURL = StringTools.replaceString(srcURL, "%3D", "=");

            srcURL = StringTools.replaceString(srcURL, "%26", "&");



            if (cat.isDebugEnabled())

                cat.debug("encoded toUTL - " + srcURL);



            out.write(ServletTools.getHiddenItem(Constants.NAME_TOURL_PARAM, srcURL));



            if (cat.isDebugEnabled())

                cat.debug("Header string - " + jspPage.sCustom.getStr("auth.check.header"));





            out.write("<table border = \"1\" cellspacing = \"0\" cellpadding = \"2\" align = \"center\" width = \"100%\">"+

                    "<tr><th class=\"formworks\">"+ jspPage.sCustom.getStr("auth.check.header") + "</th></tr>" +

                    "<tr><td class=\"formworks\"><input type = \"text\" name = \""+ Constants.NAME_USERNAME_PARAM + "\">&nbsp;"+ jspPage.sCustom.getStr("auth.check.login") + "&nbsp;</td></tr>"+

                    "<tr><td class=\"formworks\"><input type = \"password\" name=\""+ Constants.NAME_PASSWORD_PARAM +"\" value = \"\" >&nbsp;"+ jspPage.sCustom.getStr("auth.check.password") +"</td></tr>"+

                    "<tr><td class=\"formworks\" align=\"center\"><input type=\"submit\" name=\"button\" value=\""+

                    jspPage.sCustom.getStr("auth.check.register") +"\"></td></tr>"+

            "</table>"+

            "</form>");

        }

        catch (Exception e)

        {

            cat.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }



    }

}

