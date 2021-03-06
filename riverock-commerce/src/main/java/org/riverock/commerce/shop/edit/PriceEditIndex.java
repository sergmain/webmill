/*
 * org.riverock.commerce - Commerce application
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
package org.riverock.commerce.shop.edit;

import java.io.IOException;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.riverock.commerce.price.ShopPortlet;
import org.riverock.commerce.tools.ContentTypeTools;
import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.ContainerConstants;
import org.riverock.common.utils.PortletUtils;

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 2:41:47 PM
 *
 * $Id: PriceEditIndex.java 1229 2007-06-28 11:25:40Z serg_main $
 */
public final class PriceEditIndex extends HttpServlet {
    private final static Logger log = Logger.getLogger(PriceEditIndex.class);

    public PriceEditIndex() {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException  {
        if (log.isDebugEnabled())
            log.debug("method is POST");

        doGet(request, response);
    }

    public void doGet(HttpServletRequest request_, HttpServletResponse response)
            throws IOException {
        Writer out = null;
        try
        {
            RenderRequest renderRequest = (RenderRequest)request_;
            RenderResponse renderResponse= (RenderResponse)response;
            ContentTypeTools.setContentType(response, ContentTypeTools.CONTENT_TYPE_UTF8);
            ResourceBundle bundle=null;
//            bundle = (ResourceBundle)renderRequest.getAttribute( ContainerConstants.PORTAL_RESOURCE_BUNDLE_ATTRIBUTE );

            out = response.getWriter();

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
            if ( auth_==null )
            {
                throw new IllegalStateException( "You have not enough right to execute this operation" );
            }


                String sql_ =
                        "select a.* from WM_PRICE_SHOP_LIST a, WM_PORTAL_VIRTUAL_HOST b "+
                        "where a.ID_SITE = b.ID_SITE and b.NAME_VIRTUAL_HOST=? ";

                PreparedStatement ps = null;
                ResultSet rs = null;

                if( auth_.isUserInRole("webmill.edit_price_list") )
                {

                    try
                    {
//                        ps = db_.prepareStatement( sql_ );
                        ps.setString(1, renderRequest.getServerName() );

                        rs = ps.executeQuery();

                        out.write(
                                "<b>" + "Configure shop" + "</b>"+
                                "<table width=\"100%\" border=\"1\" class=\"l\">"+
                                "<tr>"+
                                "<th class=\"memberArea\">Name shop</th>"+
                                "<th class=\"memberArea\">" + bundle.getString("index.jsp.action") + "</th>" +
                                "</tr>"
                        );

                        while (rs.next() )
                        {

                            out.write(
                                    "<tr>"+
                                    "<td class=\"memberArea\">"+
                                    RsetTools.getString(rs, "NAME_SHOP", "&nbsp;") +
                                    "</td>"
                            );

                            out.write("<td class=\"memberAreaAction\">");

                            Long id_arm = RsetTools.getLong(rs, "ID_SHOP");

              out.write("\r\n");
              out.write("<input type=\"button\" value=\"");
              out.write(bundle.getString("button.next"));
              out.write("\" onclick=\"location.href='");
              out.write(
                  PortletUtils.url("mill.price.shop", renderRequest, renderResponse )+'&'+
                  ShopPortlet.NAME_ID_SHOP_PARAM + '=' +id_arm
              );
              out.write("';\">\r\n");
              out.write("</td>\r\n");
              out.write("</tr>\r\n                    ");

                        }
              out.write("\r\n");
              out.write("</table>\r\n            ");


                    }
                    catch(Exception e)
                    {
                        out.write( e.toString() );
                        log.error("Error create list of shops", e);
                    }
                }
                else
                {
                    out.write( bundle.getString("access_denied"));
                }
        }
        catch (Exception e)
        {
            log.error(e);
            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));
        }
    }
}
