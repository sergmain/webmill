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

package org.riverock.portlet.forum;

import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.io.OutputStream;

import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.StringTools;
import org.riverock.portlet.main.Constants;

import org.riverock.webmill.portlet.PortletTools;
import org.riverock.webmill.portlet.wrapper.StreamWrapper;

import org.apache.log4j.Logger;

final class ForumPlain {
    private final static Logger log = Logger.getLogger( ForumPlain.class );


    static void process(
        final OutputStream outputStream, final RenderRequest renderRequest, final RenderResponse renderResponse, final PortletConfig portletConfig) throws ForumException {
        try {
            ResourceBundle bundle = portletConfig.getResourceBundle( renderRequest.getLocale() );

            StreamWrapper out = new StreamWrapper(outputStream);
            out.write("\n<!-- $Id$ -->\n");


            Integer year = PortletTools.getInt(renderRequest, Constants.NAME_YEAR_PARAM, new Integer(Calendar.getInstance().get(Calendar.YEAR)));
            Integer month = PortletTools.getInt(renderRequest, Constants.NAME_MONTH_PARAM, new Integer(Calendar.getInstance().get(Calendar.MONTH) + 1));

            ForumInstance forum = null;
            forum = new ForumInstance( renderRequest );

            if (log.isDebugEnabled())
                log.debug("id - " + forum.id);

            int v_curr_year = year.intValue();

            int v_curr_month;
            v_curr_month = month.intValue();

            if (v_curr_year == Calendar.getInstance().get(Calendar.YEAR)) {
                if (log.isDebugEnabled())
                    log.debug("#FORUM.01.01.1");

                v_curr_month = month.intValue();
            }
            else {
                if (log.isDebugEnabled())
                    log.debug("#FORUM.01.01.2");

            }

            if (log.isDebugEnabled()) {
                log.debug("#FORUM.01.03: " + v_curr_month);
                log.debug("#FORUM.01.05: " + forum.year);
            }

            forum.year = v_curr_year;
            forum.month = v_curr_month;

// ====
            if (forum.id!=null) {
                List v = forum.getMessagesInThread();
                if (v != null) {

                    out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"12\" width=\"100%\">");

                    ListIterator it = v.listIterator();
                    while (it.hasNext()) {
                        ForumMessage message = (ForumMessage)it.next();

                        out.write("<tr><td>");
                        out.write("<b>" + bundle.getString( "str.date" ) + ':' + "</b>&nbsp;" +
                                DateTools.getStringDate(message.getDatePost(), "dd-MMMM-yyyy HH:mm:ss", renderRequest.getLocale()) + "<BR>"
                        );
                        out.write("<b>" + bundle.getString( "str.from_who" ) + ':' +
                                "</b>&nbsp;<a href=\"mailto:" + message.getEmail() + "\">" +
                                StringTools.toPlainHTML(message.getFio()) + "</a><BR>"
                        );
                        out.write("<b>" + bundle.getString( "str.header" ) + ':' +
                                "</b>&nbsp;" + StringTools.toPlainHTML(message.getHeader()) + "<BR>"
                        );
                        out.write("<p>" + StringTools.toPlainHTML(message.getText()) + "</p>");
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
                    PortletTools.getStr("str.top_forum")+"</a><br>"
            );
*/

                out.write("<a href=\"" +
                        PortletTools.url(Constants.CTX_TYPE_FORUM, renderRequest, renderResponse ) + '&' +
                        Constants.NAME_ID_FORUM_PARAM + '=' + forum.id_forum +
                        "\">" +
                    bundle.getString( "str.top_forum" ) + "</a>"
                );

                ForumAddMessage.includeMessageForm( outputStream, renderRequest, renderResponse, portletConfig );

            } // if (!isJustEntered)
            else {	// if isJustEntered
                out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"12\" width=\"100%\"><tr><td>");
                out.write("<!-- is here -->\n");
                out.write(forum.getStartMessages());
                out.write("</td></tr></table>");

                ForumAddMessage.includeMessageForm( outputStream, renderRequest, renderResponse, portletConfig );

            } // if isJustEntered


            out.write("\r\n");
            out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"12\" width=\"100%\">");
            out.write("<tr>");
            out.write("<td>");
            out.write("<font size=-1>\r\n");
            out.write(forum.getListYears());
            out.write("<br>\r\n");
            out.write(forum.getListMonths());
            out.write("<br>\r\n");
            out.write("</font>");
            out.write("</td>");
            out.write("</tr>");
            out.write("</table>");

        }
        catch ( Exception e ) {
            String es = "Error process plain forum";
            log.error(es, e);
            throw new ForumException( es, e );
        }
    }
}
