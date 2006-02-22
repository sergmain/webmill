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

package org.riverock.portlet.shop.upload;

import java.io.Writer;

import javax.portlet.RenderRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.portlet.tools.ContentTypeTools;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;

/**
 * User: Admin
 * Date: Dec 2, 2002
 * Time: 10:22:19 PM
 *
 * $Id$
 */
public final class UploadPrice extends HttpServlet {

    private final static Log log = LogFactory.getLog( UploadPrice.class );

    public final static String UPLOAD_FILE_PARM_NAME = "f";
    public final static String CTX_TYPE_UPLOAD_PRICE_CONTR    = "mill.upload_price_controller";
    public final static String CTX_TYPE_UPLOAD_PRICE   = "mill.upload_price";

    public UploadPrice() {
    }

    public void doPost( HttpServletRequest request, HttpServletResponse response )
        throws ServletException {
        if ( log.isDebugEnabled() )
            log.debug( "method is POST" );

        doGet( request, response );
    }

    public void doGet( HttpServletRequest request_, HttpServletResponse response )
        throws ServletException {
        Writer out = null;
        try {

            RenderRequest renderRequest = (RenderRequest)request_;

            ContentTypeTools.setContentType(response, ContentTypeTools.CONTENT_TYPE_UTF8);
            out = response.getWriter();

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
            if ( auth_ == null || !auth_.isUserInRole( "webmill.upload_price_list" ) ) {
//    static void      processPortletError( Writer out, Throwable th, String errorMessage, String url, String urlMessage ) throws IOException {
//                WebmillErrorPage.process( out, null, "You have not right to upload price", "/", "continue" );

        	out.write( "You have not right to upload price. <p><a href=\"/\">continue</a></p>\n" );
                return;
            }

            String param =
                ContainerConstants.NAME_TYPE_CONTEXT_PARAM + '=' + CTX_TYPE_UPLOAD_PRICE_CONTR;

            out.write(
                "<form method=\"POST\" action=\"" + response.encodeURL( PortletService.ctx( renderRequest ) ) + '?' + param + "\" ENCTYPE=\"multipart/form-data\">" +
                "Внимание! Файл импорта прайс-листа должен быть в корректном XML формате<br>" +
                "Если загрузка прайс-листа прошла успешно, то дополнительных сообщений выдано не будет" +
                "<p>" +
                "<input type=\"FILE\" name=\"" + UPLOAD_FILE_PARM_NAME + "\" size=\"50\">" +
                "<br>" +
                "<input type=\"submit\" value=\"Submit\">" +
                "</p>" +
                "</form>" );
        }
        catch( Exception e ) {
            final String es = "Error processing UploadPrice servlet";
            log.error( es, e );
            throw new ServletException( es, e );
        }
    }
}
