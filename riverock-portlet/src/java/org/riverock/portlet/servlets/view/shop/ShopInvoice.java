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

 * Time: 3:14:51 PM

 *

 * $Id$

 */



package org.riverock.portlet.servlets.view.shop;



import java.io.IOException;



import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;





public class ShopInvoice extends HttpServlet

{

    private static Logger cat = Logger.getLogger("org.riverock.servlets.view.shop.ShopInvoice");



    public ShopInvoice()

    {

    }



    public void doPost(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        if (cat.isDebugEnabled())

            cat.debug("method is POST");



        doGet(request, response);

    }



    public void doGet(HttpServletRequest request, HttpServletResponse response)

            throws IOException, ServletException

    {

        if (true)

            throw new ServletException("This servlet no longer supported");

/*

        Writer out = null;

        try

        {

            InitPage.setContentType(response);



            out = response.getWriter();



            DatabaseAdapter db_ = DatabaseAdapter.getInstance(false);

            InitPage jspPage = new InitPage(db_, request, response,

                    null, "mill.locale._price_list",

                    Constants.NAME_LANG_PARAM, null, null);



            String index_page = CtxURL.url(request, response, jspPage.cross, "mill.invoice");



            HttpSession session = request.getSession();

            BasketShopSession b = (BasketShopSession) session.getAttribute(Constants.BASKET_SHOP_SESSION);

            Shop shop = (Shop) session.getAttribute(Constants.CURRENT_SHOP);



//	boolean change = false;



            if (b.authSession == null)

            {

                AuthSession authSession = (AuthSession) session.getAttribute(Constants.AUTH_SESSION);

                if ((authSession != null) && (authSession.checkAccess(db_, request.getServerName())))

                {

                    b.updateAuthSession(authSession);

//		change = true;

                }

            }



            long id_shop = shop.id_shop;



            int currencyID = ServletTools.getInt(request, Constants.NAME_ID_CURRENCY_SHOP, 0);



            String currencyForm = "<input type=\"hidden\" name=\"" +

                    Constants.NAME_ID_CURRENCY_SHOP + "\" value=\"" + currencyID + "\">";



//            String currencyURL = "&" + Constants.NAME_ID_CURRENCY_SHOP + "=" + currencyID;



            if (currencyID == 0)

            {

//                if (shop.is_default_currency == 1)

                if (shop.currencyID!=0)

                {

//                currencyURL = "";

                    currencyForm = "";

                    currencyID = (int) shop.currencyID;

                }

                else

                    throw new ServletException("Default currency for this shop not defined");

            }



            Vector orderItems = null;

//            orderItems = PriceList.getOrderItems(db_, id_shop, currencyID,

//                        request.getServerName(), b.id_order);



            String name = ServletUtils.getString(request, "name", null);



            if (name != null)

            {

                int id_item = ServletTools.getInt(request, "id_item");



                if (name.equals("set"))

                {

                    // set new quantyt of item in backet

                    int count = ServletTools.getInt(request, "count");

                    b.setItem(id_item, count, id_shop);

//			change = true;



                }

                else if (name.equals("del"))

                {

                    // delete item from basket

                    try

                    {

                        b.delItem(id_item, id_shop);

                    }

                    catch (Exception e11)

                    {

                        cat.error("Error delete item from basket", e11);

                        out.write(ExceptionTools.getStackTrace(e11, 10, "<br>"));

                    }

//		    change = true;

                }

                else if (name.equals("send"))

                {

                    // send order



                    String order =

                            jspPage.sCustom.getStr("reg.send_order.1");



//                    String charset = LocaleCharset.getCharset(jspPage.currentLocale);

                    for (int i = 0; i < orderItems.size(); i++)

                    {

                        PriceListItemOrder item = (PriceListItemOrder) orderItems.elementAt(i);

// System.out.writeln( jspPage.sCustom.getStr("reg.send_order.2") );



                        order += jspPage.sCustom.getStr("reg.send_order.2",

                                new String[]

                                {

                                    "" + (i + 1),

                                    item.nameItem,

                                    "" + item.priceItem,

                                    item.nameCurrency,

                                    "" + item.qty,

                                    "" + item.qty * item.priceItem

                                }



//{

//""+(i+1),

//StringTools.convertString( item.nameItem, charset, "8859_1"),

//""+item.priceItem,

//StringTools.convertString( item.nameCurrency, charset, "8859_1"),

//""+item.qty,

//""+item.qty*item.priceItem

//}



                        );

                    }



                    MailMessage.sendMessage(

                            order,

                            b.authSession.initUserInfo().getEmail(),

                            jspPage.p.orderEmail,

                            "Your order N" + b.id_order,

                            jspPage.currentLocale);



                    if (jspPage.p.getIsActivateEmailOrder)

                    {



                        MailMessage.sendMessage(

                                "New order N" + b.id_order + " is complete",

                                jspPage.p.orderEmail,

                                jspPage.p.orderEmail,

                                "Order N" + b.id_order,

                                jspPage.currentLocale);



                    }



//		    change = false;

                }



            }



//if (change)

//{

//ServletTools.immediateRemoveAttribute(

//session, Constants.BASKET_SHOP_SESSION );

//

//session.setAttribute( Constants.BASKET_SHOP_SESSION , b);

//}



            if (b.authSession == null)

            {



                String backURL = StringTools.rewriteURL( index_page );

//System.out.writeln( backURL );



                out.write("<table cellspacing=\"2\" cellpadding=\"2\">\n");

                out.write("<tr>\n");

                out.write("<td class=\"pricedata\" rowspan=\"2\">\n");

                out.write(jspPage.sCustom.getStr("invoice.attention"));

                out.write("<br>\n");



                if (jspPage.p.getIsRegisterAllowed)

                {



                    out.write(jspPage.sCustom.getStr("invoice.register"));

                    out.write("\n");

                    out.write("<a href=\"");

                    out.write(



                            CtxURL.url(request, response, jspPage.cross, "mill.register") + '&' +

                            Constants.NAME_TOURL_PARAM + '=' + backURL



                    );

                    out.write("\">");

                    out.write(jspPage.sMain.getStr("button.next"));

                    out.write(" ");

                    out.write("</a>\n");



                }

                out.write("\n");

                out.write("</td>\n");

                out.write("<form method=\"post\" action=\"");

                out.write(



                        CtxURL.url(request, response, jspPage.cross, "mill.register") + '&' +

                        Constants.NAME_TOURL_PARAM + '=' + backURL



                );

                out.write("\">\n");

                out.write("<input type=\"hidden\" name=\"action\" value=\"reg_exists\">\n");

                out.write("<td class=\"pricedata\">");

                out.write(jspPage.sCustom.getStr("invoice.login"));

                out.write("</td>\n");

                out.write("<td class=\"pricedata\">");

                out.write("<input type=\"text\" name=\"username\" size=\"12\">");

                out.write("</td>\n");

                out.write("<td rowspan=\"2\" valign=\"middle\" class=\"pricedata\">");

                out.write("<input type=\"submit\" value=\"Login\">\n");

                out.write("<tr>\n");

                out.write("<td class=\"pricedata\">");

                out.write(jspPage.sCustom.getStr("invoice.password"));

                out.write("</td>\r\n");

                out.write("<td class=\"pricedata\">");

                out.write("<input type=\"password\" name=\"password1\" size=\"12\">");

                out.write("</td>\n");

                out.write("</tr>\n");

                out.write("</form>\n");

                out.write("</table>\n");

                out.write("<br>");





            }	// b.authSession == null

//	else

//{

//

//out.write( "Email: "+b.authSession.userInfo.email );

//}





            out.write("<table border=\"0\">\n");

            out.write("<tr>\n");

            out.write("<td width=\"50%\">\n");

            out.write("<a href=\"");

            out.write(response.encodeURL(Constants.URI_CTX_MANAGER));

            out.write("?");

            out.write(jspPage.addURL);

            out.write("\">");

            out.write(jspPage.sCustom.getStr("invoice.continue_select"));

            out.write("</a>");

            out.write("<br>\n");

            out.write("</td>\n");

            out.write("<td width=\"50%\">\n");





            out.write("<!-- index_page - "+index_page+" -->\n");



            if (shop.isNeedRecalc)

            {



                out.write(

                    "<FORM ACTION=\"" + response.encodeURL( CtxURL.ctx()  ) + "\" METHOD=\"GET\">\n"+

                    jspPage.cross.getAsForm()+

                    ServletTools.getHiddenItem(Constants.NAME_TYPE_CONTEXT_PARAM, Constants.CTX_TYPE_INVOICE )+

                    ServletTools.getHiddenItem(Constants.NAME_TEMPLATE_CONTEXT_PARAM, ServletUtils.getString(request, Constants.NAME_TEMPLATE_CONTEXT_PARAM))+

                    "<SELECT NAME=\"" + Constants.NAME_ID_CURRENCY_SHOP + "\" SIZE=\"1\">\n"

                );

                out.write(

                        Client.make_list_prn(currencyID, db_,

                                "v_cash_client_curr_null a, price_shop_table c ",

                                "a.id_currency",

                                "a.name_currency",

                                " where a.is_used = 1 and a.ID_SITE=c.ID_SITE and c.id_shop = " + id_shop,

                                null, "0")

                );

                out.write("</SELECT>\n");

                out.write("<INPUT TYPE=\"submit\" VALUE=\"");

                out.write(jspPage.sCustom.getStr("invoice.recalculate"));

                out.write("\">\n");

                out.write("</FORM>");





            } // if (shop.isNeedRecalc == 1)





            out.write("</td>\n</tr>\n</table>\n<br>");

            out.write(

                    jspPage.sCustom.getStr("invoice.your_select")

            );

            out.write("<table border=\"1\" cellpadding=\"2px\" cellspacing=\"2px\">\n");

            out.write("<tr>\n");

            out.write("<th class=\"priceData\">");

            out.write(jspPage.sCustom.getStr("invoice.name_item"));

            out.write("</th>\n");

            out.write("<th class=\"priceData\">");

            out.write(jspPage.sCustom.getStr("invoice.ppq"));

            out.write("</th>\n");

            out.write("<th class=\"priceData\">");

            out.write(jspPage.sCustom.getStr("invoice.quantity"));

            out.write("</th>\n");

            out.write("<th class=\"priceData\">");

            out.write(jspPage.sCustom.getStr("invoice.total_price"));

            out.write("</th>\n");

            out.write("<th class=\"priceData\">");

            out.write(jspPage.sCustom.getStr("invoice.currency"));

            out.write("</th>\n");

            out.write("<th class=\"priceData\">");

            out.write(jspPage.sCustom.getStr("invoice.delete_item"));

            out.write("</th>\n");

            out.write("</tr>");



            String currentCurrency = "";

            double priceOrder = 0;



            for (int i = 0; i < orderItems.size(); i++)

            {

                PriceListItemOrder item = (PriceListItemOrder) orderItems.elementAt(i);



                if (i == 0)

                    currentCurrency = item.nameCurrency;



                Object obj[] = {shop, b.authSession};

                double itemPrice = PriceList.calcPrice(item.priceItem, obj);



//		float itemPrice = item.priceItem;



                double itemFullPrice = item.qty * itemPrice;



                priceOrder += itemFullPrice;



                out.write("<tr>\n");

                out.write("<td class=\"priceData\">");

                out.write(item.nameItem);

                out.write("</td>\n");

                out.write("<form method=\"POST\" action=\"");

                out.write(index_page);

                out.write("\">\n");

                out.write(jspPage.cross.getAsForm());

                out.write("\n");

                out.write("<td class=\"priceData\" align=\"right\">");

                out.write("" + itemPrice);

                out.write("</td>\n");

                out.write(currencyForm);

                out.write("\n");

                out.write("<input type=\"hidden\" name=\"id_item\" value=\"");

                out.write("" + item.idPK);

                out.write("\">\n");

                out.write("<input type=\"hidden\" name=\"name\" value=\"set\">\n");

                out.write("<td class=\"priceData\" align=\"center\">");

                out.write("<input type=\"text\" size=\"3\" name=\"count\" value=\"");

                out.write(""+item.qty);

                out.write("\">&nbsp;\n");

                out.write("<input type=\"submit\" value=\"");

                out.write(jspPage.sCustom.getStr("invoice.change_qty"));

                out.write("\">");

                out.write("</td>\n");

                out.write("<td class=\"priceData\" align=\"right\">");

                out.write("" + itemFullPrice);

                out.write("</td>\n");

                out.write("<td class=\"priceData\" align=\"center\">");

                out.write(item.nameCurrency);

                out.write("</td>\n");

                out.write("</form>\n");

                out.write("<form method=\"POST\" action=\"");

                out.write(index_page);

                out.write("\">\n        ");

                out.write(jspPage.cross.getAsForm());

                out.write("\n");

                out.write("<input type=\"hidden\" name=\"name\" value=\"del\">\n");

                out.write(currencyForm);

                out.write("\n");

                out.write("<input type=\"hidden\" name=\"id_item\" value=\"");

                out.write("" + item.idPK);

                out.write("\">\n");

                out.write("<td class=\"priceData\" align=\"center\">");

                out.write("<input type=\"submit\" value=\"");

                out.write(jspPage.sCustom.getStr("invoice.delete_button"));

                out.write("\">");

                out.write("</td>\n");

                out.write("</form>\n");

                out.write("</tr>\n");





            } // for (int i=0; i<items.size(); i++)





            out.write("</table>\n");



            if (currencyID != 0)

            {

                priceOrder = PriceList.calcPrice(priceOrder,

                        new Object[]{shop});



                out.write("<br>\n        ");

                out.write(jspPage.sCustom.getStr("invoice.total_summ"));

                out.write(" ");

                out.write("" + priceOrder);

                out.write(" ");

                out.write(currentCurrency);



            } //if (currencyID != 0)



            if ((orderItems.size() != 0) && (b.authSession != null) &&

                    (jspPage.p.orderEmail.trim().length() != 0) &&

                    (jspPage.p.getIsActivateEmailOrder)

            )

            {



                out.write("<br>\n");

                out.write("<form method=\"POST\" action=\"");

                out.write(index_page);

                out.write("\">\n");

                out.write(jspPage.cross.getAsForm());

                out.write("<input type=\"hidden\" name=\"name\" value=\"send\">\r\n");

                out.write("<input type=\"hidden\" name=\"");

                out.write(Constants.NAME_ID_CURRENCY_SHOP);

                out.write("\" value=\"");

                out.write(""+currencyID);

                out.write("\">\n");

                out.write("<input type=\"submit\" value=\"");

                out.write(jspPage.sCustom.getStr("invoice.send_button"));

                out.write("\">\n");

                out.write("</form>");





            }





        }

        catch (Exception e)

        {

            cat.error(e);

            out.write(ExceptionTools.getStackTrace(e, 100, "<br>"));

        }

*/

    }

}

