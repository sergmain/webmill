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

 * Time: 2:56:39 PM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.view.forum;



import java.io.IOException;

import java.io.Writer;



import javax.portlet.PortletSession;

import javax.servlet.ServletException;

import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.portlet.main.Constants;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.portlet.PortletTools;





public class ForumAddMessage extends HttpServlet

{

    private static Logger cat = Logger.getLogger("org.riverock.servlets.view.forum.ForumAddMessage");



    public ForumAddMessage()

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



            out.write("<!--$Id$-->\r\n");





            ContextNavigator.setContentType(response, "utf-8");



            String forum_name = "";

            String forum_email = "";



            if (cat.isDebugEnabled())

                cat.debug("#13.55");



            Cookie[] cookies = ctxInstance.getCookies();

            if (cookies != null)

            {

                for (int i = 0; i < cookies.length; i++)

                {

                    Cookie c = cookies[i];

                    String name = c.getName();



                    if (cat.isDebugEnabled())

                        cat.debug("#13.00: " + name);



                    if (name.equals("_name"))

                    {

                        forum_name = c.getValue();

//			forum_name = StringTools.convertString(c.getValue(),

//				"8859_1",

//				"UTF-8");

//			forum_name = StringTools.convertString(c.getValue(),

//				System.getProperties().getProperty("file.encoding"),

//				"UTF-8");



                        if (cat.isDebugEnabled())

                        {

                            cat.debug("#13.01.01: " + forum_name);

                            cat.debug("#13.01.02: " + c.getValue());

                        }

                    }



                    if (name.equals("_email"))

                    {

                        forum_email = c.getValue();

//			forum_email = StringTools.convertString(c.getValue(),

//				System.getProperties().getProperty("file.encoding"),

//				"UTF-8");

                    }

                }

            }



            PortletSession session = ctxInstance.getPortletRequest().getPortletSession();

            String subject = (String) session.getAttribute(Constants.FORUM_SUBJECT_SESSION);

            if (subject == null)

                subject = "";

            session.removeAttribute(Constants.FORUM_SUBJECT_SESSION);



// xxx work around of deutch answer - AW:

            if ((subject.trim().length() > 0) && (!subject.toUpperCase().startsWith("RE: ")))

                subject = "RE: " + subject;



            out.write("\r\n");

            out.write("<form action=\"");

            out.write(response.encodeURL(CtxURL.ctx()));

            out.write("\" method=\"POST\">\r\n");



            out.write(

                    ctxInstance.getAsForm()+

                    ServletTools.getHiddenItem(Constants.NAME_ID_FORUM_PARAM,

                            "" + PortletTools.getLong(ctxInstance.getPortletRequest(), Constants.NAME_ID_FORUM_PARAM)

                    )+

                    ServletTools.getHiddenItem(Constants.NAME_TYPE_CONTEXT_PARAM,

                            Constants.CTX_TYPE_FORUM_COMMIT

                    )+

                    ServletTools.getHiddenItem(Constants.NAME_TEMPLATE_CONTEXT_PARAM,

                            ctxInstance.getNameTemplate()

                    )+

                    ServletTools.getHiddenItem(Constants.NAME_ID_MAIN_FORUM_PARAM,

                            "" + PortletTools.getLong(ctxInstance.getPortletRequest(), Constants.NAME_ID_MESSAGE_FORUM_PARAM)

                    )+

                    ServletTools.getHiddenItem("action", "add")

            );



            out.write("\r\n");

            out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\">\r\n");

            out.write("<tr>\r\n");

            out.write("<td align=\"right\">");

            out.write(ctxInstance.sCustom.getStr("str.name"));

            out.write(":&nbsp;");

            out.write("</td>\r\n");

            out.write("<td>");

            out.write("<input type=\"text\" name=\"n\" size=\"40\" value=\"");

            out.write(forum_name);

            out.write("\">");

            out.write("</td>\r\n");

            out.write("</tr>\r\n");

            out.write("<tr>\r\n");

            out.write("<td align=\"right\">E-mail:&nbsp;");

            out.write("</td>\r\n");

            out.write("<td>");

            out.write("<input type=\"text\" name=\"e\" size=\"40\" value=\"");

            out.write(forum_email);

            out.write("\">");

            out.write("</td>\r\n");

            out.write("</tr>\r\n");

            out.write("<tr>\r\n");

            out.write("<td align=\"right\">");

            out.write(ctxInstance.sCustom.getStr("str.header"));

            out.write(":&nbsp;");

            out.write("</td>\r\n");

            out.write("<td>");

            out.write("<input type=\"text\" name=\"h\" size=\"40\" value=\"");

            out.write(subject);

            out.write("\">");

            out.write("</td>\r\n");

            out.write("</tr>\r\n");

            out.write("<tr>\r\n");

            out.write("<td colspan=\"2\">");

            out.write(ctxInstance.sCustom.getStr("str.message"));

            out.write(":");

            out.write("<br>\r\n");

            out.write("<textarea name=\"b\" rows=\"8\" cols=\"55\">");

            out.write("</textarea>");

            out.write("</td>\r\n");

            out.write("</tr>\r\n");

            out.write("<tr>\r\n");

            out.write("<td colspan=\"2\">");

            out.write("<input type=\"submit\" value=\"");

            out.write(ctxInstance.sCustom.getStr("str.add"));

            out.write("\">");

            out.write("</td>\r\n");

            out.write("</tr>\r\n");

            out.write("</table>\r\n");

            out.write("</form>");



        }

        catch (Exception e)

        {

            cat.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }



    }

}

