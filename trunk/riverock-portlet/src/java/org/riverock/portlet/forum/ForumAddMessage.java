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
package org.riverock.portlet.forum;

import java.util.ResourceBundle;
import java.net.URLDecoder;
import java.io.OutputStream;
import java.io.IOException;

import javax.portlet.PortletConfig;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.Cookie;

import org.riverock.common.tools.ServletTools;
import org.riverock.portlet.main.Constants;
import org.riverock.webmill.portal.PortalConstants;

import org.riverock.webmill.portlet.PortletTools;
import org.riverock.webmill.portlet.wrapper.StreamWrapper;

import org.apache.log4j.Logger;


/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 2:56:39 PM
 *
 * $Id$
 */
final class ForumAddMessage {
    private final static Logger log = Logger.getLogger( ForumAddMessage.class );

    static void includeMessageForm(
        final OutputStream outputStream, final RenderRequest renderRequest,
        final RenderResponse renderResponse, final PortletConfig portletConfig) throws ForumException {
        try
        {
            StreamWrapper out = new StreamWrapper(outputStream);
            ResourceBundle bundle = portletConfig.getResourceBundle( renderRequest.getLocale() );
            out.write("\n<!--$Id$-->\n");

            String forumName = "";
            String forumEmail = "";

            Cookie[] cookies = (Cookie[])renderRequest.getAttribute(PortalConstants.PORTAL_COOKIES_ATTRIBUTE) ;

            if (log.isDebugEnabled())
                log.debug("#13.55, cookie array: " + cookies );

            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++)
                {
                    Cookie c = cookies[i];
                    String name = c.getName();

                    if (log.isDebugEnabled())
                        log.debug("#13.00: " + name + ", value: " + c.getValue() );

                    if (name.equals("_name")) {
                        forumName = URLDecoder.decode( c.getValue(), "utf8" );

//			forumName = StringTools.convertString(c.getValue(),
//				"8859_1",
//				"UTF-8");
//			forumName = StringTools.convertString(c.getValue(),
//				System.getProperties().getProperty("file.encoding"),
//				"UTF-8");

                    }

                    if (name.equals("_email")) {
                        forumEmail = c.getValue();
//			forumEmail = StringTools.convertString(c.getValue(),
//				System.getProperties().getProperty("file.encoding"),
//				"UTF-8");
                    }
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("forumName: " + forumName );
                log.debug("forumEmail: " + forumEmail );
            }

            PortletSession session = renderRequest.getPortletSession();
            String subject = (String) session.getAttribute(ForumPortlet.FORUM_SUBJECT_SESSION);
            if (subject == null)
                subject = "";
            session.removeAttribute(ForumPortlet.FORUM_SUBJECT_SESSION);

            // xxx work around of deutch answer - AW:
            if ((subject.trim().length() > 0) && (!subject.toUpperCase().startsWith("RE: ")))
                subject = "RE: " + subject;

            out.write("\n");
            out.write( "<form action=\"" );
            out.write( renderResponse.encodeURL(PortletTools.ctx( renderRequest )) );
            out.write( "\" method=\"POST\">\n" );

            out.write(
                    ServletTools.getHiddenItem(ForumPortlet.NAME_ID_FORUM_PARAM,
                            "" + PortletTools.getLong(renderRequest, ForumPortlet.NAME_ID_FORUM_PARAM)
                    )+
                    ServletTools.getHiddenItem(Constants.NAME_TYPE_CONTEXT_PARAM,
                            ForumPortlet.CTX_TYPE_FORUM_COMMIT
                    )+
                    ServletTools.getHiddenItem(ForumPortlet.NAME_ID_MAIN_FORUM_PARAM,
                            "" + PortletTools.getLong(renderRequest, ForumPortlet.NAME_ID_MESSAGE_FORUM_PARAM)
                    )+
                    ServletTools.getHiddenItem("action", "add")
            );

            out.write("\r\n");
            out.write("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"100%\">\r\n");
            out.write("<tr>\r\n");
            out.write("<td align=\"right\">");
            out.write( bundle.getString( "str.name" ) );
            out.write(":&nbsp;");
            out.write("</td>\r\n");
            out.write("<td>");
            out.write("<input type=\"text\" name=\"n\" size=\"40\" value=\"");
            out.write(forumName);
            out.write("\">");
            out.write("</td>\r\n");
            out.write("</tr>\r\n");
            out.write("<tr>\r\n");
            out.write("<td align=\"right\">E-mail:&nbsp;");
            out.write("</td>\r\n");
            out.write("<td>");
            out.write("<input type=\"text\" name=\"e\" size=\"40\" value=\"");
            out.write(forumEmail);
            out.write("\">");
            out.write("</td>\r\n");
            out.write("</tr>\r\n");
            out.write("<tr>\r\n");
            out.write("<td align=\"right\">");
            out.write( bundle.getString( "str.header" ) );
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
            out.write( bundle.getString( "str.message" ) );
            out.write(":");
            out.write("<br>\r\n");
            out.write("<textarea name=\"b\" rows=\"8\" cols=\"55\">");
            out.write("</textarea>");
            out.write("</td>\r\n");
            out.write("</tr>\r\n");
            out.write("<tr>\r\n");
            out.write("<td colspan=\"2\">");
            out.write("<input type=\"submit\" value=\"");
            out.write( bundle.getString( "str.add" ) );
            out.write("\">");
            out.write("</td>\r\n");
            out.write("</tr>\r\n");
            out.write("</table>\r\n");
            out.write("</form>");

        }
        catch ( Exception e ) {
            String es = "Error include form for post message in forum";
            log.error(es, e);
            throw new ForumException( es, e );
        }
    }
}
