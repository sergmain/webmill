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
package org.riverock.portlet.firm;

import java.io.Writer;
import java.util.ResourceBundle;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.portlet.portlets.WebmillErrorPage;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 9:32:08 AM
 *
 * $Id$
 */
public final class FirmAdd extends HttpServlet
{
    private final static Log log = LogFactory.getLog( FirmAdd.class );

    public FirmAdd()
    {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if (log.isDebugEnabled())
            log.debug("method is POST");

        doGet(request, response);
    }

    public void doGet(HttpServletRequest request_, HttpServletResponse response)
            throws ServletException
    {
        Writer out = null;
        try {
            RenderRequest renderRequest = (RenderRequest)request_;
            RenderResponse renderResponse= (RenderResponse)response;
            ResourceBundle bundle = (ResourceBundle)renderRequest.getAttribute( ContainerConstants.PORTAL_RESOURCE_BUNDLE_ATTRIBUTE );
            ContentTypeTools.setContentType(response, ContentTypeTools.CONTENT_TYPE_UTF8);
            out = response.getWriter();

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
            if ( auth_==null )
            {
                WebmillErrorPage.process(out, null, "You have not enough right to execute this operation", "/", "continue");
                return;
            }

            String index_page = PortletService.url("mill.firm.index", renderRequest, renderResponse );

            if (auth_.isUserInRole("webmill.firm_insert"))
            {

                out.write("\r\n");
                out.write("<form method=\"POST\" action=\"");
                out.write(
                        PortletService.url("mill.firm.commit_add_firm", renderRequest, renderResponse )
                );
                out.write("\">\r\n");
                out.write("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"1\" bordercolor=\"#000000\">\r\n");
                out.write("<tr>\r\n");
                out.write("<td class=\"head\">");
                out.write(bundle.getString("add_firm.jsp.new_rec"));
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("</table>\r\n");
                out.write("<table width=\"100%\" border=\"1\" cellspacing=\"0\" bordercolor=\"#000000\" cellpadding=\"3\" class=\"l\">\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("add_firm.jsp.full_name"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<input type=\"text\" name=\"full_name\" size=\"50\" maxlength=\"800\" value=\"\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("add_firm.jsp.short_name"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<input type=\"text\" name=\"short_name\" size=\"50\" maxlength=\"250\" value=\"\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("add_firm.jsp.address"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<input type=\"text\" name=\"address\" size=\"50\" maxlength=\"200\" value=\"\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("add_firm.jsp.telefon_buh"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<input type=\"text\" name=\"telefon_buh\" size=\"30\" maxlength=\"30\" value=\"\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("add_firm.jsp.telefon_chief"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<input type=\"text\" name=\"telefon_chief\" size=\"30\" maxlength=\"30\" value=\"\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("add_firm.jsp.chief"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<input type=\"text\" name=\"chief\" size=\"50\" maxlength=\"200\" value=\"\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("add_firm.jsp.buh"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<input type=\"text\" name=\"buh\" size=\"50\" maxlength=\"200\" value=\"\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("add_firm.jsp.fax"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<input type=\"text\" name=\"fax\" size=\"30\" maxlength=\"30\" value=\"\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("add_firm.jsp.email"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<input type=\"text\" name=\"email\" size=\"50\" maxlength=\"80\" value=\"\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("add_firm.jsp.icq"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<input type=\"text\" name=\"icq\" size=\"20\" maxlength=\"20\" value=\"\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("add_firm.jsp.short_client_info"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<input type=\"text\" name=\"short_client_info\" size=\"50\" maxlength=\"1500\" value=\"\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("add_firm.jsp.url"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<input type=\"text\" name=\"url\" size=\"50\" maxlength=\"160\" value=\"\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("add_firm.jsp.short_info"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<input type=\"text\" name=\"short_info\" size=\"50\" maxlength=\"2000\" value=\"\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write("<span style=\"color:red\">*");
                out.write("</span>");
                out.write(bundle.getString("add_firm.jsp.is_work"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<select name=\"is_work\">\r\n");
                out.write("<option value=\"0\">");
                out.write(bundle.getString("yesno.no"));
                out.write("</option>\r\n");
                out.write("<option value=\"1\" selected>");
                out.write(bundle.getString("yesno.yes"));
                out.write("</option>\r\n");
                out.write("</select>\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("add_firm.jsp.is_need_recvizit"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<select name=\"is_need_recvizit\">\r\n");
                out.write("<option value=\"0\">");
                out.write(bundle.getString("yesno.no"));
                out.write("</option>\r\n");
                out.write("<option value=\"1\">");
                out.write(bundle.getString("yesno.yes"));
                out.write("</option>\r\n");
                out.write("</select>\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("add_firm.jsp.is_need_person"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<select name=\"is_need_person\">\r\n");
                out.write("<option value=\"0\">");
                out.write(bundle.getString("yesno.no"));
                out.write("</option>\r\n");
                out.write("<option value=\"1\">");
                out.write(bundle.getString("yesno.yes"));
                out.write("</option>\r\n");
                out.write("</select>\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("add_firm.jsp.fio"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<input type=\"text\" name=\"fio\" size=\"50\" maxlength=\"200\" value=\"\">\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("<tr>\r\n");
                out.write("<td align=\"right\" width=\"25%\" class=\"par\">");
                out.write(bundle.getString("add_firm.jsp.is_search"));
                out.write("</td>\r\n");
                out.write("<td align=\"left\">\r\n");
                out.write("<select name=\"is_search\">\r\n");
                out.write("<option value=\"0\">");
                out.write(bundle.getString("yesno.no"));
                out.write("</option>\r\n");
                out.write("<option value=\"1\">");
                out.write(bundle.getString("yesno.yes"));
                out.write("</option>\r\n");
                out.write("</select>\r\n");
                out.write("</td>\r\n");
                out.write("</tr>\r\n");
                out.write("</table>\r\n");
                out.write("<br>\r\n");
                out.write("<input type=\"submit\" class=\"par\" value=\"");
                out.write(bundle.getString("button.add"));
                out.write("\">\r\n");
                out.write("</form>\r\n");

            }
            out.write("\r\n");
            out.write("<br>\r\n");
            out.write("<p>");
            out.write("<a href=\"");
            out.write(index_page);
            out.write("\">");
            out.write(bundle.getString("page.main.3"));
            out.write("</a>");
            out.write("</p>\r\n");

        }
        catch (Exception e) {
            String es = "Error FirmAdd";
            log.error( es, e );
            throw new ServletException( es, e );
        }

    }
}
