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



import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.portlet.main.Constants;

import org.riverock.portlet.portlets.WebmillErrorPage;

import org.riverock.sso.a3.AuthSession;

import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.CtxURL;



public class UploadPrice extends HttpServlet

{

    private static Logger log = Logger.getLogger(UploadPrice.class);



    public static String UPLOAD_FILE_PARM_NAME = "f";

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



    public void doGet(HttpServletRequest request_, HttpServletResponse response)

        throws IOException, ServletException

    {

        Writer out = null;

        try

        {

            CtxInstance ctxInstance =

                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );



            if (log.isDebugEnabled())

                log.debug("Process input auth data");



            out = response.getWriter();



            AuthSession auth_ = (AuthSession)ctxInstance.getPortletRequest().getUserPrincipal();

            if ( auth_==null || !auth_.isUserInRole( "webmill.upload_price_list" ) )

            {

                WebmillErrorPage.process(out, null, "You have not right to bind right", "/", "continue");

                return;

            }





            {

                String param = ctxInstance.page.getAsURL() +

                    Constants.NAME_TEMPLATE_CONTEXT_PARAM + '=' +

                    ctxInstance.getNameTemplate() + '&' +

                    Constants.NAME_TYPE_CONTEXT_PARAM + '=' +

                    Constants.CTX_TYPE_UPLOAD_PRICE_CONTR;



                out.write(

                        "<form method=\"POST\" action=\"" + response.encodeURL(CtxURL.ctx()) + '?' + param + "\" ENCTYPE=\"multipart/form-data\">" +

                        "Внимание! Файл импорта прайс-листа должен быть в корректном XML формате<br>" +

                        "Если загрузка прайс-листа прошла успешно, то дополнительных сообщений выдано не будет" +

                        "<p>" +

                        "<input type=\"FILE\" name=\""+UPLOAD_FILE_PARM_NAME+"\" size=\"50\">" +

                        "<br>" +

                        "<input type=\"submit\" value=\"Submit\">" +

                        "</p>" +

                        "</form>"

                );



            }

        }

        catch (Exception e)

        {

            log.error("Error processing UploadPrice servlet", e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }

    }



}

