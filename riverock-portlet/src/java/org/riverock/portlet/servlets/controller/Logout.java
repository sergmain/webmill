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

 * Time: 9:50:27 PM

 *

 * $Id$

 */

package org.riverock.portlet.servlets.controller;



import org.apache.log4j.Logger;



import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import javax.servlet.ServletException;

import java.io.IOException;

import java.io.Writer;

import java.util.Enumeration;



import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.utils.ServletUtils;



public class Logout extends HttpServlet

{

    private static Logger log = Logger.getLogger(Logout.class);



    public Logout()

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

            InitPage.setContentType(response, "utf-8");



            HttpSession session = request.getSession();



            if (log.isDebugEnabled())

            {

                for (Enumeration e = request.getParameterNames();

                     e.hasMoreElements(); )

                {



                    String param = (String)e.nextElement();

                    log.debug("parameter "+ param+" value "+ServletUtils.getString(request, param) );

                }



            }



/*

            InitPage webPage =  new InitPage( DatabaseAdapter.getInstance(false),

                    request, response,

                    CtxURL.ctx(), null,

                    Constants.NAME_LANG_PARAM, null, null);

*/

            int countLoop = 3;



            for (int i=0; i<countLoop; i++)

            {

                try

                {

                    for (Enumeration e = session.getAttributeNames();

                         e.hasMoreElements();

                         e = session.getAttributeNames()

                            )

                    {

                        String name = (java.lang.String) e.nextElement() ;



                        if (log.isDebugEnabled())

                            log.debug("Attribute: "+name);



                        ServletTools.immediateRemoveAttribute( session, name );

//		session.removeAttribute( name );

                    }

                }

                catch( java.util.ConcurrentModificationException e)

                {

                    if (i==countLoop-1)

                        throw e;

                }

            }



            session.invalidate();



            if (log.isDebugEnabled())

                log.debug("url to redir:  "+response.encodeURL( CtxURL.ctx() ) );



            response.sendRedirect( response.encodeURL( CtxURL.ctx() ) );

            return;



        }

         catch (Exception e)

         {

             log.error(e);

             out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

         }



     }



}

