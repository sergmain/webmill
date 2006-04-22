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
 * Time: 12:31:38 PM
 *
 * $Id$
 */

package org.riverock.portlet.image;

import java.io.IOException;
import java.io.Writer;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.container.tools.PortletService;





public class ImageDescription extends HttpServlet
{
    private static Logger cat = Logger.getLogger(ImageDescription.class);

    public ImageDescription()
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
        try {
            RenderRequest renderRequest = null;
            RenderResponse renderResponse= null;

            ContentTypeTools.setContentType(response, ContentTypeTools.CONTENT_TYPE_UTF8);

            out = response.getWriter();

            try
            {

                AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
                if ( auth_==null ) {
                    throw new IllegalStateException("You have not enough right to execute this operation" );
                }

                if (auth_.isUserInRole("webmill.upload_image"))
                {

                    PortletSession sess = renderRequest.getPortletSession(true);
                    Long l = PortletService.getLong(renderRequest, "id_main");
                    if (l==null)
                        throw new IllegalArgumentException("id_main not initialized");

                    out.write("" + l);

                    out.write("<br>");
                    sess.setAttribute("MILL.IMAGE.ID_MAIN", l);

                    l = (Long) sess.getAttribute("MILL.IMAGE.ID_MAIN");
                    if (l != null)
                        out.write("" + l);
                    else
                        out.write("is null");

                    out.write("Введите краткое описание загружаемого файла (до 300 символов)<br>");
                    out.write("<br>\r\n");
                    out.write("<form method=\"POST\" action=\"");
                    out.write(

                            PortletService.url("mill.image.step_file", renderRequest, renderResponse )

                    );
                    out.write("\">\r\n");
                    out.write("<p>\r\n");
                    out.write("<input type=\"submit\" value=\"Далее\">\r\n");
                    out.write("<br>\r\n");
                    out.write("<textarea name=\"d\" cols=\"50\" rows=\"10\">");
                    out.write("</textarea>\r\n");
                    out.write("</p>\r\n");
                    out.write("</form>\r\n            ");
                }
            }
            catch (Exception e)
            {
                cat.error(e);
                out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));
            }
        }

        catch (Exception e)
        {
            cat.error(e);
            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));
        }

    }
}
