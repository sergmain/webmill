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

 * Date: Dec 2, 2002

 * Time: 10:22:19 PM

 *

 * $Id$

 */

package org.riverock.portlet.servlets.view;



import java.io.IOException;

import java.io.Writer;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.riverock.common.tools.ExceptionTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.portlet.main.Constants;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.a3.AuthTools;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.utils.ServletUtils;



import org.apache.log4j.Logger;



public class UploadPrice extends HttpServlet

{

    private static Logger log = Logger.getLogger(UploadPrice.class);



    public UploadPrice()

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

            if (log.isDebugEnabled())

                log.debug("Process input auth data");



            out = response.getWriter();



            DatabaseAdapter db_ = DatabaseAdapter.getInstance(false);

            InitPage jspPage = new InitPage(db_, request,

                                            "mill.locale.auth"

            );



            AuthSession auth_ = AuthTools.check(request, response, "/");

            if (auth_ == null)

                return;





            if (auth_.isUserInRole("webmill.upload_price_list"))

            {

                String param = jspPage.getAsURL() +

                    Constants.NAME_TEMPLATE_CONTEXT_PARAM + '=' +

                    ServletUtils.getString(request, Constants.NAME_TEMPLATE_CONTEXT_PARAM) + '&' +

                    Constants.NAME_TYPE_CONTEXT_PARAM + '=' +

                    Constants.CTX_TYPE_UPLOAD_PRICE_CONTR;





                out.write(

                        "<form method=\"POST\" action=\"" + response.encodeURL(CtxURL.ctx()) + '?' + param + "\" ENCTYPE=\"multipart/form-data\">" +

                        "Внимание! Файл импорта прайс-листа должен быть в корректном XML формате<br>" +

                        "Если загрузка прайс-листа прошла успешно, то дополнительных сообщений выдано не будет" +

                        "<p>" +

                        "<input type=\"FILE\" name=\"f\" size=\"50\">" +

                        "<br>" +

                        "<input type=\"submit\" value=\"Submit\">" +

                        "</p>" +

                        "</form>"

                );



            } // check auth

        }

        catch (Exception e)

        {

            log.error("Error processing UploadPrice servlet", e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }



    }



}

