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



import javax.portlet.PortletSession;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.StringTools;

import org.riverock.portlet.main.Constants;

import org.riverock.sso.a3.AuthSession;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.portlet.PortletTools;



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



    public void doGet(HttpServletRequest request_, HttpServletResponse response)

            throws IOException, ServletException

    {

        Writer out = null;

        try

        {

            CtxInstance ctxInstance =

                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );





            ContextNavigator.setContentType(response, "utf-8");



            out = response.getWriter();



//            InitPage jspPage =  new InitPage(db_, request,

//                                             "mill.locale.auth"

//            );



            log.warn("ctxInstance: "+ctxInstance);

            if (ctxInstance!=null)

                log.warn("ctxInstance.getPortletRequest(): "+ctxInstance.getPortletRequest());



            PortletSession session = ctxInstance.getPortletRequest().getPortletSession();

            AuthSession auth_ = (AuthSession)session.getAttribute( Constants.AUTH_SESSION );



            if (auth_==null)

            {

                auth_ = new AuthSession(

                        ctxInstance.getPortletRequest().getParameter(Constants.NAME_USERNAME_PARAM ),

                        ctxInstance.getPortletRequest().getParameter(Constants.NAME_PASSWORD_PARAM )

                );

            }



            if(log.isDebugEnabled())

                log.debug("URL #1: "+PortletTools.getString(ctxInstance.getPortletRequest(), Constants.NAME_TOURL_PARAM  , "ctx?"+ctxInstance.page.getAsURL()));



            if (auth_.checkAccess( ctxInstance.page.p.getServerName() ))

            {

                session.setAttribute( Constants.AUTH_SESSION, auth_);

                String url = PortletTools.getString(ctxInstance.getPortletRequest(), Constants.NAME_TOURL_PARAM  , "ctx?"+ctxInstance.page.getAsURL());



                if(log.isDebugEnabled())

                    log.debug("URL #2: "+url);



                url = response.encodeURL( url );



                if(log.isDebugEnabled())

                    log.debug("URL #3: "+url);



                response.sendRedirect( url );

                return;

            }



            // User not corrected input authorization data

            // remove all objects from session

            PortletTools.cleanSession(session);



            long currentTimeMills = System.currentTimeMillis();



            if(log.isDebugEnabled())

                log.debug("mills "+currentTimeMills);



            while ( (System.currentTimeMillis() - currentTimeMills)<3000)

                for (int i=0; i<50; i++);



            if(log.isDebugEnabled())

                log.debug("mills "+System.currentTimeMillis());



            session.invalidate();



            String redirUrl= CtxURL.url( ctxInstance.getPortletRequest(), response, ctxInstance.page, Constants.CTX_TYPE_LOGIN );



            String srcUrl = null;

            if (ctxInstance.getPortletRequest().getParameter(Constants.NAME_TOURL_PARAM  )!= null)

            {

                srcUrl = PortletTools.getString(ctxInstance.getPortletRequest(), Constants.NAME_TOURL_PARAM  );



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

            log.error("LOginCheck exception: ", e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }

    }



}

