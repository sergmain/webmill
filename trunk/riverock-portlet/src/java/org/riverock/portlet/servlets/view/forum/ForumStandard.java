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

 * Time: 2:57:59 PM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.view.forum;



import java.io.IOException;

import java.io.Writer;

import java.util.Calendar;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;



import org.riverock.common.tools.DateTools;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.StringTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.portlet.forum.ForumMessage;

import org.riverock.portlet.forum.SimpleForum;

import org.riverock.portlet.main.Constants;

import org.riverock.webmill.port.InitPage;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.utils.ServletUtils;



import org.apache.log4j.Logger;



public class ForumStandard extends HttpServlet

{

    private static Logger log = Logger.getLogger(ForumStandard.class);



    public ForumStandard()

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



            InitPage.setContentType(response, "utf-8");



            out.write("<!-- begin standart forum -->");



            InitPage jspPage = new InitPage(DatabaseAdapter.getInstance(false),

                    request, response,

                    "mill.locale.forum",

                    Constants.NAME_LANG_PARAM +

                    Constants.NAME_YEAR_PARAM +

                    Constants.NAME_MONTH_PARAM,

                    null, null);



            SimpleForum forum = new SimpleForum(request, response, jspPage);



            HttpSession session = request.getSession();



//            String nameTemplate = (String) session.getAttribute(Constants.TEMPLATE_NAME_SESSION);

            String nameTemplate = request.getParameter(

                Constants.NAME_TEMPLATE_CONTEXT_PARAM

            );



            if (log.isDebugEnabled())

            {

                log.debug("ID message: " + forum.id);

                log.debug("ID forum: " + forum.id_forum);

                log.debug("#FORUM.01.01: " + jspPage.cross.getMonth());

            }



            int v_curr_year = jspPage.cross.getYear().intValue();



            int v_curr_month;

            v_curr_month = jspPage.cross.getMonth().intValue();



            if (v_curr_year == Calendar.getInstance().get(Calendar.YEAR))

            {

                if (log.isDebugEnabled())

                    log.debug("#FORUM.01.01.1");



                v_curr_month = jspPage.cross.getMonth().intValue();

            }

            else

            {

                if (log.isDebugEnabled())

                    log.debug("#FORUM.01.01.2");



//	jspPage.cross.month = new Integer(v_curr_month);



//	Integer firstMonth = forum.getFirstMonthInYear();

//	if (firstMonth==null)

//		v_curr_month =



//                jspPage.reinit(request, response);

                forum.jspPage = jspPage;

            }

            if (log.isDebugEnabled())

            {

                log.debug("#FORUM.01.03: " + v_curr_month);

                log.debug("#FORUM.01.05: " + forum.year);

            }



            forum.year = v_curr_year;

            forum.month = v_curr_month;



            if (log.isDebugEnabled())

                log.debug("isJustEnabled - " + forum.isJustEntered);



// Show messaage with id="id"

            if (!forum.isJustEntered)

            {

                ForumMessage message = forum.getCurrentMessage();

                if ((message != null) && message.isOk)

                {

//System.out.writeln("ID message #1.1: "+forum.id);

//System.out.writeln("ID message #2: "+message.id);



                    session.setAttribute(Constants.FORUM_SUBJECT_SESSION, message.header);



                    out.write("\r\n");

                    out.write("<table border=\"0\">");

                    out.write("<tr>");

                    out.write("<td>\r\n");





                    ServletUtils.include(request, response, "/mill.forum_add_message", out );



                    out.write("\r\n");

                    out.write("</td>");

                    out.write("</tr>");

                    out.write("</table>\r\n");

                    out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"12\" width=\"100%\">");

                    out.write("<tr>");

                    out.write("<td>\r\n");

                    out.write("<b>");

                    out.write(jspPage.sCustom.getStr("str.date"));

                    out.write(":");

                    out.write("</b>&nbsp;");

                    out.write(DateTools.getStringDate(message.datePost, "dd-MMMM-yyyy HH:mm:ss", jspPage.currentLocale));

                    out.write("<BR>\r\n");

                    out.write("<b>");

                    out.write(jspPage.sCustom.getStr("str.from_who"));

                    out.write(":");

                    out.write("</b>&nbsp;");

                    out.write("<a href=\"mailto:");

                    out.write(message.email);

                    out.write("\">");

                    out.write(

                            StringTools.toPlainHTML(message.fio)

                    );

                    out.write("</a>");

                    out.write("<BR>\r\n");

                    out.write("<b>");

                    out.write(jspPage.sCustom.getStr("str.header"));

                    out.write(":");

                    out.write("</b>&nbsp;");

                    out.write(StringTools.toPlainHTML(message.header));

                    out.write("<BR>\r\n");

                    out.write("<p>");

                    out.write(StringTools.toPlainHTML(message.text));

                    out.write("</p>\r\n");

                    out.write("</td>");

                    out.write("</tr>");

                    out.write("</table>\r\n                ");



                }

                else

                    out.write("Error get message id=" + forum.id + "<br>");

            } // if (!isJustEntered)



            out.write("\r\n");

            out.write("<!-- here -->\r\n");

            out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"12\" width=\"100%\">");

            out.write("<tr>");

            out.write("<td>\r\n");

            out.write(forum.getThreads(nameTemplate));

            out.write("\r\n");

            out.write("</td>");

            out.write("</tr>");

            out.write("<tr>");

            out.write("<td>\r\n");





            String url = ("<a href=\"" +

                    CtxURL.url(request, response, jspPage.cross, Constants.CTX_TYPE_FORUM) + '&' +

                    Constants.NAME_ID_FORUM_PARAM + '=' + forum.id_forum +

                    "\">");



            out.write(url + jspPage.sCustom.getStr("str.top_forum") + "</a>");



            out.write("\r\n");

            out.write("</td>");

            out.write("</tr>");

            out.write("</table>\r\n");

            out.write("<br>\r\n");



            if (forum.isJustEntered)

            {

                out.write("<table border=\"0\"><tr><td>");

                ServletUtils.include(request, response, "/mill.forum_add_message", out);

                out.write("</td></tr></table>");

            }



            out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"12\" width=\"100%\"><tr><td><font size=-1>");

            out.write(forum.getListYears(nameTemplate) + "<br>");

            out.write(forum.getListMonths(nameTemplate) + "<br>");

            out.write("</font></td></tr></table>");



            out.write("\r\n");

            out.write("<!--End of Forum -->");



        }

        catch (Exception e)

        {

            log.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }



    }

}

