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

import javax.portlet.PortletSession;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.StringTools;
import org.riverock.portlet.forum.ForumMessage;
import org.riverock.portlet.forum.SimpleForum;
import org.riverock.portlet.main.Constants;
import org.riverock.webmill.portlet.ContextNavigator;
import org.riverock.webmill.portlet.CtxInstance;
import org.riverock.webmill.portlet.PortletTools;
import org.riverock.webmill.utils.ServletUtils;

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

    public void doGet(HttpServletRequest request_, HttpServletResponse response)
            throws IOException, ServletException
    {
        Writer out = null;
        try
        {
            CtxInstance ctxInstance =
                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );

            out = response.getWriter();

            ContextNavigator.setContentType(response, "utf-8");

            out.write("<!-- begin standart forum -->\n");

            Integer year = PortletTools.getInt(ctxInstance.getPortletRequest(), Constants.NAME_YEAR_PARAM, new Integer(Calendar.getInstance().get(Calendar.YEAR)));
            Integer month = PortletTools.getInt(ctxInstance.getPortletRequest(), Constants.NAME_MONTH_PARAM, new Integer(Calendar.getInstance().get(Calendar.MONTH) + 1));

            SimpleForum forum = new SimpleForum(ctxInstance.getPortletRequest(), response );

            PortletSession session = ctxInstance.getPortletRequest().getPortletSession();

//            String nameTemplate = (String) session.getAttribute(Constants.TEMPLATE_NAME_SESSION);
            String nameTemplate = ctxInstance.getNameTemplate();

            if (log.isDebugEnabled())
            {
                log.debug("ID message: " + forum.id);
                log.debug("ID forum: " + forum.id_forum);
                log.debug("#FORUM.01.01: " + month);
            }

            int v_curr_year = year.intValue();

            int v_curr_month;
            v_curr_month = month.intValue();

            if (log.isDebugEnabled())
                log.debug("Requested year is equals current year - "+(v_curr_year == Calendar.getInstance().get(Calendar.YEAR)));

            if (v_curr_year == Calendar.getInstance().get(Calendar.YEAR))
                v_curr_month = month.intValue();
            else

            if (log.isDebugEnabled())
            {
                log.debug("#FORUM month: " + v_curr_month);
                log.debug("#FORUM year: " + v_curr_year);
            }

            forum.year = v_curr_year;
            forum.month = v_curr_month;

            if (log.isDebugEnabled())
                log.debug("id - " + forum.id);

// Show messaage with id="id"
            if (forum.id!=null) {
                ForumMessage message = forum.getCurrentMessage();
                if (message!=null) {
                    session.setAttribute(Constants.FORUM_SUBJECT_SESSION, message.getHeader());

                    out.write("\r\n");
                    out.write("<table border=\"0\">");
                    out.write("<tr>");
                    out.write("<td>\r\n");


                    ServletUtils.include(request_, response, null, "/mill.forum_add_message", out );

                    out.write("\r\n");
                    out.write("</td>");
                    out.write("</tr>");
                    out.write("</table>\r\n");
                    out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"12\" width=\"100%\">");
                    out.write("<tr>");
                    out.write("<td>\r\n");
                    out.write("<b>");
                    out.write(ctxInstance.sCustom.getStr("str.date"));
                    out.write(":");
                    out.write("</b>&nbsp;");
                    out.write(DateTools.getStringDate(message.getDatePost(), "dd-MMMM-yyyy HH:mm:ss", ctxInstance.getPortletRequest().getLocale()));
                    out.write("<BR>\r\n");
                    out.write("<b>");
                    out.write(ctxInstance.sCustom.getStr("str.from_who"));
                    out.write(":");
                    out.write("</b>&nbsp;");
                    out.write("<a href=\"mailto:");
                    out.write(message.getEmail());
                    out.write("\">");
                    out.write(
                            StringTools.toPlainHTML(message.getFio())
                    );
                    out.write("</a>");
                    out.write("<BR>\r\n");
                    out.write("<b>");
                    out.write(ctxInstance.sCustom.getStr("str.header"));
                    out.write(":");
                    out.write("</b>&nbsp;");
                    out.write(StringTools.toPlainHTML(message.getHeader()));
                    out.write("<BR>\r\n");
                    out.write("<p>");
                    out.write(StringTools.toPlainHTML(message.getText()));
                    out.write("</p>\r\n");
                    out.write("</td>");
                    out.write("</tr>");
                    out.write("</table>\r\n                ");

                }
                else
                    out.write("Error get message id=" + forum.id + "<br>");
            } // if (forum.id!=null)

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
                    ctxInstance.url(Constants.CTX_TYPE_FORUM) + '&' +
                    Constants.NAME_ID_FORUM_PARAM + '=' + forum.id_forum +
                    "\">");

            out.write(url + ctxInstance.sCustom.getStr("str.top_forum") + "</a>");

            out.write("\r\n");
            out.write("</td>");
            out.write("</tr>");
            out.write("</table>\r\n");
            out.write("<br>\r\n");

            if (forum.id==null) {
                out.write("<table border=\"0\"><tr><td>");
                ServletUtils.include(request_, response, null, "/mill.forum_add_message", out);
                out.write("</td></tr></table>");
            }

            out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"12\" width=\"100%\"><tr><td><font size=-1>");
            out.write(forum.getListYears() + "<br>");
            out.write(forum.getListMonths() + "<br>");
            out.write("</font></td></tr></table>");

            out.write("\r\n");
            out.write("<!--End of Forum -->\n");

        }
        catch (Exception e)
        {
            log.error(e);
            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));
        }
    }
}
