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

 * User: Admin

 *

 * $Id$

 */

package org.riverock.portlet.servlets.controller;



import java.io.IOException;

import java.io.Writer;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;



import org.riverock.sso.a3.AuthSession;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.portlet.main.Constants;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.utils.ServletUtils;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.common.tools.StringTools;



import org.apache.log4j.Logger;



/**

 * Author: mill

 * Date: Dec 2, 2002

 * Time: 4:42:13 PM

 *

 * $Id$

 */

public class LoginCheck extends HttpServlet

{

    private static Logger log = Logger.getLogger(LoginCheck.class);



    public LoginCheck()

    {

    }



    public void doPost(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        if (log.isDebugEnabled())

            log.debug("method is POST");



        doGet(request, response);

    }



    public void doGet(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        Writer out = null;

        DatabaseAdapter db_ = null;

        try

        {



            InitPage.setContentType(response, "utf-8");



            out = response.getWriter();



            db_ = DatabaseAdapter.getInstance( false );

            InitPage jspPage =  new InitPage(db_, request, response,

                    "mill.locale.auth",

                    Constants.NAME_LANG_PARAM, null, null);



// AuthSession просто создает объект без проверки прав доступа.

            HttpSession session = request.getSession();

            AuthSession auth_ = (AuthSession)session.getAttribute( Constants.AUTH_SESSION );



            if (auth_==null)

            {

                auth_ = new AuthSession(

                        request.getParameter(Constants.NAME_USERNAME_PARAM ),

                        request.getParameter(Constants.NAME_PASSWORD_PARAM )

                );

            }



            if(log.isDebugEnabled())

                log.debug("URL #1: "+ServletUtils.getString(request, Constants.NAME_TOURL_PARAM  , "ctx?"+jspPage.addURL));



            if (auth_.checkAccess( request.getServerName() ))

            {

                session.setAttribute( Constants.AUTH_SESSION, auth_);

                String url = ServletUtils.getString(request, Constants.NAME_TOURL_PARAM  , "ctx?"+jspPage.addURL);



                if(log.isDebugEnabled())

                    log.debug("URL #2: "+url);



                url = response.encodeURL( url );



                if(log.isDebugEnabled())

                    log.debug("URL #3: "+url);



                response.sendRedirect( url );

                return;

            }



// ёзер не правильно ввел авторизационные данные

// удал€ем из сесси все объекты

            ServletTools.cleanSession(session);



            long currentTimeMills = System.currentTimeMillis();



            if(log.isDebugEnabled())

                log.debug("mills "+currentTimeMills);



            while ( (System.currentTimeMillis() - currentTimeMills)<3000)

                for (int i=0; i<50; i++);



            if(log.isDebugEnabled())

                log.debug("mills "+System.currentTimeMillis());



            session.invalidate();



            String redirUrl= CtxURL.url( request, response, jspPage.cross, Constants.CTX_TYPE_LOGIN );



            String srcUrl = null;

            if (request.getParameter(Constants.NAME_TOURL_PARAM  )!= null)

            {

                srcUrl = ServletUtils.getString(request, Constants.NAME_TOURL_PARAM  );



                srcUrl = StringTools.rewriteURL(srcUrl );



                srcUrl = "&"+srcUrl;

            }

            else

                srcUrl = "";



            if (log.isDebugEnabled())

            {

                log.debug("encoded toUTL - "+ srcUrl);

                log.debug("redirURL - "+ redirUrl);

            }



            response.sendRedirect( redirUrl+srcUrl);

            if (true) return;



        }

        catch (Exception e)

        {

            log.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }

        finally

        {

            DatabaseManager.close( db_ );

            db_ = null;

        }

    }



}

