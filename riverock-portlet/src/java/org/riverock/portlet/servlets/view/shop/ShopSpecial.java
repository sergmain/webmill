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

 * Time: 3:15:51 PM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.view.shop;



import java.io.IOException;

import java.io.Writer;



import javax.portlet.PortletSession;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.portlet.main.Constants;

import org.riverock.portlet.price.BasketShopSession;

import org.riverock.portlet.price.PriceSpecialItems;

import org.riverock.portlet.price.Shop;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;





public class ShopSpecial extends HttpServlet

{

    private static Logger cat = Logger.getLogger(ShopSpecial.class);



    public ShopSpecial()

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

        DatabaseAdapter db_ = null;

        try

        {

            CtxInstance ctxInstance =

                (CtxInstance)request_.getSession().getAttribute( org.riverock.webmill.main.Constants.PORTLET_REQUEST_SESSION );



            ContextNavigator.setContentType(response);



            out = response.getWriter();



            out.write("\r\n");

            out.write("<!-- $Id$ -->\r\n");

            out.write("\r\n");



            db_ = DatabaseAdapter.getInstance(false);



            PortletSession session = ctxInstance.getPortletRequest().getPortletSession();



            BasketShopSession b = (BasketShopSession) session.getAttribute(Constants.BASKET_SHOP_SESSION);

            if (cat.isDebugEnabled())

                cat.debug("BasketShopSession - " + b);



            if (b != null)

            {



                Long currencyID = null;

                Shop shop = Shop.getInstance(db_, b.id_shop);



                String currencyForm = "<input type=\"hidden\" name=\"c\" value=\"" + currencyID + "\">";

                String currencyURL = "&c=" + currencyID;



                if (shop.is_default_currency == 1)

                {

                    currencyForm = "";

                    currencyURL = "";

                    currencyID = shop.currencyID;

                }



// !!!!!!!

// Этот кусок надо. Закоментарен для текущей компиляции

                PriceSpecialItems specItems = null;

//                      PriceList.getSpecialItems(db_, shop, currencyID,

//                              ctxInstance.getPortletRequest().getServerName() );







                out.write("<table width=\"100%\" border=\"0\" cellspacing=\"3\" cellpadding=\"0\">");



                if (true)

                    throw new Exception("Need refactoring");

/*

                for (int i = 0; i < specItems.items.size(); i++)

                {

                    PriceListItemExtend item =

                        (PriceListItemExtend) specItems.items.elementAt(i);



                    double itemPrice = NumberTools.truncate(item.priceItem, 2); // xxxx shop.commasCount);



                    out.write("<tr>\r\n");

                    out.write("<td rowspan=\"2\" valign=\"top\" class=\"ImageLeft\">");





                    if (item.imageNameFile != null && !"".equals(item.imageNameFile.trim()))

                    {



                        out.write("<img src=\"");

                        out.write(item.imageNameFile);

                        out.write("\" border=\"0\">");





                    }



                    out.write("</td>\r\n");

                    out.write("<td width=\"80%\" valign=\"top\">\r\n            ");

                    out.write(item.nameItem);

                    out.write("<br>\r\n            ");

                    out.write(item.desc);

                    out.write("<br>\r\nР?РчР_Р°: ");

                    out.write("" + itemPrice);

                    out.write(",&nbsp;");

                    out.write(item.nameCurrency);

                    out.write("\r\n");

                    out.write("</td>\r\n");

                    out.write("</tr>\r\n");

                    out.write("<tr>\r\n");

                    out.write("<form action=\"");

                    out.write(response.encodeURL(CtxURL.ctx()));

                    out.write("\">\r\n");

                    out.write("<td valign=\"bottom\">\r\n            ");

                    out.write(webPage.cross.getAsForm());

                    out.write("\r\n");

                    out.write("<input type=\"hidden\" name=\"c\" value=\"");

                    out.write("" + currencyID);

                    out.write("\">\r\n");

                    out.write("<input type=\"hidden\" name=\"add_id\" value=\"");

                    out.write("" + item.idPK);

                    out.write("\">\r\nР' Р·Р°РєР°Р· ");

                    out.write("<input type=\"text\" size=\"3\" value=\"1\" name=\"v\">&nbsp;");

                    out.write("<input type=\"submit\" value=\"+\">");

                    out.write("<br>");

                    out.write("<br>\r\n");

                    out.write("</td>\r\n");

                    out.write("</form>\r\n");

                    out.write("</tr>");





                }

*/



                out.write("\r\n");

                out.write("</table>\r\n        ");





            }	//  ff  ( b != null)



            out.write("\r\n");



        }

        catch (Exception e)

        {

            cat.error(e);

            out.write(ExceptionTools.getStackTrace(e, 20, "<br>"));

        }

        finally

        {

            DatabaseAdapter.close(db_);

            db_ = null;

        }

    }

}

