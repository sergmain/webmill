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

 * Time: 2:57:40 PM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.view.forum;



import java.io.IOException;

import java.io.Writer;

import java.util.Calendar;

import java.util.List;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



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





public class ForumPlain extends HttpServlet

{

    private static Logger cat = Logger.getLogger(ForumPlain.class);



    public ForumPlain()

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



            out.write("<!-- $Id$ -->");



            InitPage jspPage = new InitPage(DatabaseAdapter.getInstance(false),

                    request, response,

                    "mill.locale.forum",

                    Constants.NAME_LANG_PARAM +

                    Constants.NAME_YEAR_PARAM +

                    Constants.NAME_MONTH_PARAM,

                    null, null);



            SimpleForum forum = new SimpleForum(request, response, jspPage);

//            HttpSession session = request.getSession();



//            String nameTemplate = (String) session.getAttribute(Constants.TEMPLATE_NAME_SESSION);

            String nameTemplate = request.getParameter(

                Constants.NAME_TEMPLATE_CONTEXT_PARAM

            );



            if (cat.isDebugEnabled())

                cat.debug("id - " + forum.id);

// ====

            int v_curr_year = jspPage.cross.getYear().intValue();



            int v_curr_month;

            v_curr_month = jspPage.cross.getMonth().intValue();



            if (v_curr_year == Calendar.getInstance().get(Calendar.YEAR))

            {

                if (cat.isDebugEnabled())

                    cat.debug("#FORUM.01.01.1");



                v_curr_month = jspPage.cross.getMonth().intValue();

            }

            else

            {

                if (cat.isDebugEnabled())

                    cat.debug("#FORUM.01.01.2");



//	jspPage.cross.month = new Integer(v_curr_month);



//	Integer firstMonth = forum.getFirstMonthInYear();

//	if (firstMonth==null)

//		v_curr_month =



//                jspPage.reinit(request, response);

                forum.jspPage = jspPage;

            }

            if (cat.isDebugEnabled())

            {

                cat.debug("#FORUM.01.03: " + v_curr_month);

                cat.debug("#FORUM.01.05: " + forum.year);

            }



            forum.year = v_curr_year;

            forum.month = v_curr_month;



// ====

            if (forum.id!=null)

            {

                List v = forum.getMessagesInThread();

                if (v != null)

                {



                    out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"12\" width=\"100%\">");

                    int sizeList = v.size();

                    for (int i = 0; i < sizeList; i++)

                    {

                        ForumMessage message = (ForumMessage) v.get(i);



                        out.write("<tr><td>");

                        out.write("<b>" + jspPage.sCustom.getStr("str.date") + ':' + "</b>&nbsp;" +

                                DateTools.getStringDate(message.datePost, "dd-MMMM-yyyy HH:mm:ss", jspPage.currentLocale) + "<BR>"

                        );

                        out.write("<b>" + jspPage.sCustom.getStr("str.from_who") + ':' +

                                "</b>&nbsp;<a href=\"mailto:" + message.email + "\">" +

                                StringTools.toPlainHTML(message.fio) + "</a><BR>"

                        );

                        out.write("<b>" + jspPage.sCustom.getStr("str.header") + ':' +

                                "</b>&nbsp;" + StringTools.toPlainHTML(message.header) + "<BR>"

                        );

                        out.write("<p>" + StringTools.toPlainHTML(message.text) + "</p>");

                        out.write("</td></tr>");



                    }

                    out.write("</table>");

                }

                else

                    out.write("Error get thread messages id=" + forum.id + "<br>");



/*

            out.writeln("<a href=\""+response.encodeURL( forum.forumURI )+'?'+

                    jspPage.addURL +

                    Constants.NAME_ID_FORUM_PARAM +'='+ forum.id_forum +"\">"+

                    jspPage.sCustom.getStr("str.top_forum")+"</a><br>"

            );

*/



                out.write("<a href=\"" +

                        CtxURL.url(request, response, jspPage.cross, Constants.CTX_TYPE_FORUM) + '&' +

                        Constants.NAME_ID_FORUM_PARAM + '=' + forum.id_forum +

                        "\">" +

                        jspPage.sCustom.getStr("str.top_forum") + "</a>"

                );





                ServletUtils.include(request, response, "/mill.forum_add_message", out);



            } // if (!isJustEntered)

            else

            {	// if isJustEntered

                out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"12\" width=\"100%\"><tr><td>");

                out.write("<!-- is here -->\n");

                out.write(forum.getStartMessages(nameTemplate));

                out.write("</td></tr></table>");



                ServletUtils.include(request, response, "/mill.forum_add_message", out);



            } // if isJustEntered





            out.write("\r\n");

            out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"12\" width=\"100%\">");

            out.write("<tr>");

            out.write("<td>");

            out.write("<font size=-1>\r\n");

            out.write(forum.getListYears(nameTemplate));

            out.write("<br>\r\n");

            out.write(forum.getListMonths(nameTemplate));

            out.write("<br>\r\n");

            out.write("</font>");

            out.write("</td>");

            out.write("</tr>");

            out.write("</table>");



        }

        catch (Exception e)

        {

            cat.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }



    }

}

