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

 * Time: 12:32:51 PM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.view.image;



import java.io.IOException;

import java.io.Writer;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import javax.servlet.http.HttpServlet;



import org.apache.log4j.Logger;



import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.portlet.main.Constants;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.utils.ServletUtils;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.ServletTools;



public class ImageSelectFile extends HttpServlet

{

    private static Logger log = Logger.getLogger(ImageSelectFile.class);



    public ImageSelectFile()

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

        try

        {

            InitPage.setContentType(response);



            out = response.getWriter();



            try

            {



                AuthSession auth_ = AuthTools.check(request, response, "/");

                if (auth_ == null)

                    return;



                DatabaseAdapter db_ = DatabaseAdapter.getInstance(false);

                InitPage jspPage = new InitPage(db_, request, response,

                        "mill.locale._price_list",

                        Constants.NAME_LANG_PARAM, null, null);



                String index_page = CtxURL.url(request, response, jspPage.cross, "mill.image.index");



                if (auth_.isUserInRole("webmill.upload_image"))

                {



                    if (ServletTools.isNotInit(request, response, "d"))

                        return;



                    HttpSession sess = request.getSession(true);



                    if (sess.getAttribute("MILL.IMAGE.ID_MAIN") == null)

                    {

                        response.sendRedirect(index_page);

                        return;

                    }



                    sess.setAttribute("MILL.IMAGE.DESC_IMAGE", ServletUtils.getString(request, "d", "none"));



                    out.write("\r\n            ");

                    out.write("" + ((Long) sess.getAttribute("MILL.IMAGE.ID_MAIN")).longValue());

                    out.write("<br>\r\n            ");

                    out.write(((String) sess.getAttribute("MILL.IMAGE.DESC_IMAGE")));

                    out.write("<br>Выберите файл для загрузки");

                    out.write("<form method=\"post\" action=\"");

                    out.write(



                            CtxURL.url(request, response, jspPage.cross, "mill.image.upload_image")



                    );

                    out.write("\" ENCTYPE=\"multipart/form-data\">\r\n");

                    out.write("<p>\r\n");

                    out.write("<input type=\"submit\" value=\"Submit\">\r\n");

                    out.write("<br>\r\n");

                    out.write("<input type=\"FILE\" name=\"f\" size=\"50\">\r\n");

                    out.write("<br>\r\n");

                    out.write("</p>\r\n");

                    out.write("</form>\r\n            ");



                }

            }

            catch (Exception e)

            {

                log.error(e);

                out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

            }





            out.write("\r\n");



        }

        catch (Exception e)

        {

            log.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }



    }

}

