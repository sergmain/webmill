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

 * Time: 12:31:38 PM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.view.image;



import java.io.IOException;

import java.io.Writer;



import javax.portlet.PortletSession;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.portlet.PortletTools;



public class ImageDescription extends HttpServlet

{

    private static Logger cat = Logger.getLogger(ImageDescription.class);



    public ImageDescription()

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



            try

            {



                AuthSession auth_ = AuthTools.check(ctxInstance.getPortletRequest(), response, "/");

                if (auth_ == null)

                    return;



                if (auth_.isUserInRole("webmill.upload_image"))

                {



                    PortletSession sess = ctxInstance.getPortletRequest().getPortletSession(true);

                    Long l = PortletTools.getLong(ctxInstance.getPortletRequest(), "id_main");

                    if (l==null)

                        throw new IllegalArgumentException("id_main not initialized");



                    out.write("" + l.longValue());



                    out.write("<br>");

                    sess.setAttribute("MILL.IMAGE.ID_MAIN", l);



                    l = (Long) sess.getAttribute("MILL.IMAGE.ID_MAIN");

                    if (l != null)

                        out.write("" + l.longValue());

                    else

                        out.write("is null");



                    out.write("Введите краткое описание загружаемого файла (до 300 символов)<br>");

                    out.write("<br>\r\n");

                    out.write("<form method=\"POST\" action=\"");

                    out.write(



                            CtxURL.url(ctxInstance.getPortletRequest(), response, ctxInstance.page, "mill.image.step_file")



                    );

                    out.write("\">\r\n");

                    out.write("<p>\r\n");

                    out.write("<input type=\"submit\" value=\"Далее\">\r\n");

                    out.write("<br>\r\n");

                    out.write("<textarea name=\"d\" cols=\"50\" rows=\"10\">");

                    out.write("</textarea>\r\n");

                    out.write("</p>\r\n");

                    out.write("</form>\r\n            ");

                }

            }

            catch (Exception e)

            {

                cat.error(e);

                out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

            }

        }



        catch (Exception e)

        {

            cat.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }



    }

}

