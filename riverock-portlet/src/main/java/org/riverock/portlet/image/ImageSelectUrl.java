/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.portlet.image;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.portlet.RenderRequest;




import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.container.tools.PortletService;

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 12:32:13 PM
 *
 * $Id$
 */
public class ImageSelectUrl extends HttpServlet
{
    private static Logger log = Logger.getLogger(ImageSelectUrl.class);

    public ImageSelectUrl()
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
            RenderRequest renderRequest = null;

            ContentTypeTools.setContentType(response, ContentTypeTools.CONTENT_TYPE_UTF8);

            out = response.getWriter();

                AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
                if ( auth_==null )
                {
                    throw new IllegalStateException( "You have not enough right to execute this operation" );
                }

                if (auth_.isUserInRole("webmill.upload_image"))
                {

                    Long id_main = PortletService.getLong(renderRequest, "id_main");
                    if (id_main==null)
                        throw new IllegalArgumentException("id_relate_right not initialized");

                    out.write("\r\n");
                    out.write("<br>\r\n");
                    out.write("<form method=\"post\" action=\"");
                    out.write(response.encodeURL("upload_from_url.jsp"));
                    out.write("\">\r\n            ");
                    out.write("\r\n");
                    out.write("<input type=\"hidden\" name=\"id_main\" value=\"");
                    out.write("" + id_main);
                    out.write("\">\r\n");
                    out.write("<table border=\"0\">\r\n");
                    out.write("<tr>\r\n");
                    out.write("<td width=\"20%\">URL, откуда забрать файл");
                    out.write("</td>\r\n");
                    out.write("<td>");
                    out.write("<INPUT TYPE=\"text\" NAME=\"url_download\" SIZE=\"50\" MAXLENGTH=\"150\" value=\"\">");
                    out.write("</td>\r\n");
                    out.write("</tr>\r\n");
                    out.write("<tr>\r\n");
                    out.write("<td width=\"20%\">Введите краткое описание загружаемого файла (до 300 символов)");
                    out.write("</td>\r\n");
                    out.write("<td>");
                    out.write("<textarea name=\"d\" cols=\"50\" rows=\"10\">");
                    out.write("</textarea>");
                    out.write("</td>\r\n");
                    out.write("</tr>\r\n");
                    out.write("</table>\r\n");
                    out.write("<input type=\"submit\" value=\"Загрузить\">\r\n");
                    out.write("</form>\r\n\r\n            ");
                }
        }
        catch (Exception e)
        {
            log.error(e);
            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));
        }
    }
}
