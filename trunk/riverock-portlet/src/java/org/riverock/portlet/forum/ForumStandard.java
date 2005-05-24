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

package org.riverock.portlet.forum;

import java.util.Calendar;
import java.util.ResourceBundle;
import java.io.OutputStream;

import javax.portlet.PortletConfig;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.StringTools;
import org.riverock.portlet.main.Constants;

import org.riverock.webmill.portlet.PortletTools;
import org.riverock.webmill.portlet.wrapper.StreamWrapper;

import org.apache.log4j.Logger;

final class ForumStandard {
    private final static Logger log = Logger.getLogger(ForumStandard.class);

    static void process(
        final OutputStream outputStream, final RenderRequest renderRequest, final RenderResponse renderResponse, final PortletConfig portletConfig) throws ForumException {
        try
        {
            ResourceBundle bundle = portletConfig.getResourceBundle( renderRequest.getLocale() );
            StreamWrapper out = new StreamWrapper(outputStream);

            out.write("<!-- begin standart forum -->\n");

            Integer year = PortletTools.getInt(renderRequest, ForumPortlet.NAME_YEAR_PARAM, new Integer(Calendar.getInstance().get(Calendar.YEAR)));
            Integer month = PortletTools.getInt(renderRequest, ForumPortlet.NAME_MONTH_PARAM, new Integer(Calendar.getInstance().get(Calendar.MONTH) + 1));

            ForumInstance forum = null;
            forum = new ForumInstance( renderRequest );

            PortletSession session = renderRequest.getPortletSession();

            if (log.isDebugEnabled()) {
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
                    session.setAttribute(ForumPortlet.FORUM_SUBJECT_SESSION, message.getHeader());

                    out.write("\r\n");
                    out.write("<table border=\"0\">");
                    out.write("<tr>");
                    out.write("<td>\r\n");

                    ForumAddMessage.includeMessageForm( outputStream, renderRequest, renderResponse, portletConfig );

                    out.write("\r\n");
                    out.write("</td>");
                    out.write("</tr>");
                    out.write("</table>\r\n");
                    out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"12\" width=\"100%\">");
                    out.write("<tr>");
                    out.write("<td>\r\n");
                    out.write("<b>");
                    out.write(bundle.getString( "str.date" ));
                    out.write(":");
                    out.write("</b>&nbsp;");
                    out.write(DateTools.getStringDate(message.getDatePost(), "dd-MMMM-yyyy HH:mm:ss", renderRequest.getLocale()));
                    out.write("<BR>\r\n");
                    out.write("<b>");
                    out.write( bundle.getString( "str.from_who" ) );
                    out.write(":");
                    out.write("</b>&nbsp;");
                    out.write("<a href=\"mailto:");
                    out.write(message.getEmail());
                    out.write("\">");
                    out.write( StringTools.toPlainHTML(message.getFio()) );
                    out.write("</a>");
                    out.write("<BR>\r\n");
                    out.write("<b>");
                    out.write( bundle.getString( "str.header" ) );
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
            out.write(forum.getThreads());
            out.write("\r\n");
            out.write("</td>");
            out.write("</tr>");
            out.write("<tr>");
            out.write("<td>\r\n");


            String url = ("<a href=\"" +
                PortletTools.url(
                    ForumPortlet.CTX_TYPE_FORUM, renderRequest, renderResponse ) + '&' +
                ForumPortlet.NAME_ID_FORUM_PARAM + '=' + forum.id_forum +
                "\">");

            out.write(url + bundle.getString( "str.top_forum" ) + "</a>");

            out.write("\r\n");
            out.write("</td>");
            out.write("</tr>");
            out.write("</table>\r\n");
            out.write("<br>\r\n");

            if (forum.id==null) {
                out.write("<table border=\"0\"><tr><td>");
                ForumAddMessage.includeMessageForm( outputStream, renderRequest, renderResponse, portletConfig );
                out.write("</td></tr></table>");
            }

            out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"12\" width=\"100%\"><tr><td><font size=-1>");
            out.write(forum.getListYears() + "<br>");
            out.write(forum.getListMonths() + "<br>");
            out.write("</font></td></tr></table>");

            out.write("\r\n");
            out.write("<!--End of Forum -->\n");

        }
        catch ( Exception e ) {
            String es = "Error process standard forum";
            log.error(es, e);
            throw new ForumException( es, e );
        }
    }
}
