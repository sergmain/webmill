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



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.portlet.main.Constants;

import org.riverock.sso.a3.AuthSession;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;



import org.riverock.webmill.portlet.PortletTools;



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



    public void doGet(HttpServletRequest request_, HttpServletResponse response)

            throws IOException, ServletException

    {

        Writer out = null;

        try

        {

            CtxInstance ctxInstance =

                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );





            ContextNavigator.setContentType(response);



            out = response.getWriter();



            if (cat.isDebugEnabled())

                cat.debug("Process input auth data");



            AuthSession auth_ = (AuthSession)ctxInstance.getPortletRequest().getUserPrincipal();



            if (auth_ == null)

            {

                auth_ = new AuthSession(

                        ctxInstance.getPortletRequest().getParameter(Constants.NAME_USERNAME_PARAM),

                        ctxInstance.getPortletRequest().getParameter(Constants.NAME_PASSWORD_PARAM)

                );

            }



            if (auth_.checkAccess( ctxInstance.getPortletRequest().getServerName()))

            {

                if (cat.isDebugEnabled())

                    cat.debug("user " + auth_.getUserLogin() + "is  valid for " + ctxInstance.getPortletRequest().getServerName() + " site");



                return;

            }



            out.write( "<form method = \"POST\" action = \"" + ctxInstance.ctx() + "\" >");



            out.write(ctxInstance.getAsForm());

            out.write(ServletTools.getHiddenItem(

                    Constants.NAME_TEMPLATE_CONTEXT_PARAM,

                    ctxInstance.getNameTemplate()

            )

            );

            out.write(ServletTools.getHiddenItem(

                    Constants.NAME_TYPE_CONTEXT_PARAM,

                    Constants.CTX_TYPE_LOGIN_CHECK

            )

            );



            String srcURL = null;

            if (ctxInstance.getPortletRequest().getParameter(Constants.NAME_TOURL_PARAM) != null)

            {

                srcURL = PortletTools.getString(ctxInstance.getPortletRequest(), Constants.NAME_TOURL_PARAM);

                out.write( ServletTools.getHiddenItem(Constants.NAME_TOURL_PARAM, srcURL) );

            }



            if (cat.isDebugEnabled())

                cat.debug("Header string - " + ctxInstance.sCustom.getStr("auth.check.header"));





            out.write("<table border = \"1\" cellspacing = \"0\" cellpadding = \"2\" align = \"center\" width = \"100%\">"+

                    "<tr><th class=\"formworks\">"+ ctxInstance.sCustom.getStr("auth.check.header") + "</th></tr>" +

                    "<tr><td class=\"formworks\"><input type = \"text\" name = \""+ Constants.NAME_USERNAME_PARAM + "\">&nbsp;"+ ctxInstance.sCustom.getStr("auth.check.login") + "&nbsp;</td></tr>"+

                    "<tr><td class=\"formworks\"><input type = \"password\" name=\""+ Constants.NAME_PASSWORD_PARAM +"\" value = \"\" >&nbsp;"+ ctxInstance.sCustom.getStr("auth.check.password") +"</td></tr>"+

                    "<tr><td class=\"formworks\" align=\"center\"><input type=\"submit\" name=\"button\" value=\""+

                    ctxInstance.sCustom.getStr("auth.check.register") +"\"></td></tr>"+

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

