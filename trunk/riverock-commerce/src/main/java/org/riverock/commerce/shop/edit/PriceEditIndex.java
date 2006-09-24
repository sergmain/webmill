/*
 * org.riverock.commerce - Commerce application
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.commerce.shop.edit;

import java.io.IOException;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;




import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;

import org.riverock.commerce.price.ShopPortlet;
import org.riverock.commerce.tools.ContentTypeTools;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;



/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 2:41:47 PM
 *
 * $Id$
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
        DatabaseAdapter db_ = null;
        try
        {
            RenderRequest renderRequest = (RenderRequest)request_;
            RenderResponse renderResponse= (RenderResponse)response;
            ContentTypeTools.setContentType(response, ContentTypeTools.CONTENT_TYPE_UTF8);
            ResourceBundle bundle = (ResourceBundle)renderRequest.getAttribute( ContainerConstants.PORTAL_RESOURCE_BUNDLE_ATTRIBUTE );

            out = response.getWriter();

            AuthSession auth_ = (AuthSession)renderRequest.getUserPrincipal();
            if ( auth_==null )
            {
                throw new IllegalStateException( "You have not enough right to execute this operation" );
            }

                db_ = DatabaseAdapter.getInstance();

                String sql_ =
                        "select a.* from WM_PRICE_SHOP_LIST a, WM_PORTAL_VIRTUAL_HOST b "+
                        "where a.ID_SITE = b.ID_SITE and b.NAME_VIRTUAL_HOST=? ";

                PreparedStatement ps = null;
                ResultSet rs = null;

                if( auth_.isUserInRole("webmill.edit_price_list") )
                {

                    try
                    {
                        ps = db_.prepareStatement( sql_ );
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
                  PortletService.url("mill.price.shop", renderRequest, renderResponse )+'&'+
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
                    finally
                    {
                        DatabaseManager.close(rs, ps);
                        rs = null;
                        ps = null;

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
        finally
        {
            DatabaseAdapter.close(db_);
            db_ = null;
        }

    }
}
