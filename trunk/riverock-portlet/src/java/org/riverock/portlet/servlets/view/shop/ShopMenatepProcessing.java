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

 * Time: 3:15:04 PM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.view.shop;



import java.io.IOException;

import java.io.Writer;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpServlet;



import org.apache.log4j.Logger;



import org.riverock.webmill.port.InitPage;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.StringTools;





public class ShopMenatepProcessing extends HttpServlet

{

    private static Logger cat = Logger.getLogger("org.riverock.servlets.view.shop.ShopMenatepProcessing");



    public ShopMenatepProcessing()

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



            String mainURL =

                    "https://www.menatepspb.com/ib/eps2/enter/?basket_url=" +



                    StringTools.replaceStringArray(

                            "http://" + request.getServerName() + response.encodeURL("/basket.jsp"),

                            new String[][]{{"=", "%3D"}, {"&", "%26"}}

                    );



            out.write("Для проведения платежа перейдите <a href =\"" + mainURL + "\">здесь</a>");

        }

        catch (Exception e)

        {

            cat.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }



    }

}

