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

 * User: Admin

 * Date: Dec 13, 2002

 * Time: 3:04:50 PM

 *

 * $Id$

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

import java.io.Writer;



import javax.portlet.PortletSession;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



import org.apache.log4j.Logger;

import org.riverock.common.mail.MailMessage;

import org.riverock.common.tools.ExceptionTools;

import org.riverock.common.tools.NumberTools;

import org.riverock.common.tools.ServletTools;

import org.riverock.common.tools.StringTools;

import org.riverock.generic.config.GenericConfig;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.tools.XmlTools;

import org.riverock.portlet.main.Constants;

import org.riverock.portlet.portlets.model.OrderLogic;

import org.riverock.portlet.price.PriceList;

import org.riverock.portlet.price.Shop;

import org.riverock.portlet.schema.price.OrderItemType;

import org.riverock.portlet.schema.price.OrderType;

import org.riverock.portlet.schema.price.ShopOrderType;

import org.riverock.sso.a3.AuthSession;

import org.riverock.sso.schema.AuthSessionType;

import org.riverock.sso.schema.MainUserInfoType;

import org.riverock.webmill.config.WebmillConfig;

import org.riverock.webmill.portlet.ContextNavigator;

import org.riverock.webmill.portlet.CtxInstance;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.portlet.PortletTools;





public class ShopInvoiceV2 extends HttpServlet

{

    private static Logger log = Logger.getLogger(ShopInvoiceV2.class);



    public ShopInvoiceV2()

    {

    }



    public void doPost(HttpServletRequest request, HttpServletResponse response)

        throws IOException, ServletException

    {

        if (log.isDebugEnabled())

            log.debug("method is POST");



        doGet(request, response);

    }



    private static Object syncObj = new Object();



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

            db_ = DatabaseAdapter.getInstance(false);



            out = response.getWriter();



//            InitPage jspPage = new InitPage(db_, request,

//                                            "mill.locale._price_list"

//            );



            String index_page = ctxInstance.url("mill.index");

            String invoice_page = ctxInstance.url("mill.invoice");

            String indexPageForm = ctxInstance.urlAsForm(

                ctxInstance.getNameTemplate(), "mill.invoice"

            );



            PortletSession session = ctxInstance.getPortletRequest().getPortletSession();

            OrderType order = (OrderType) session.getAttribute(Constants.ORDER_SESSION);



            if (order == null)

                return;



            Shop shop = (Shop) session.getAttribute(Constants.CURRENT_SHOP);



//	boolean change = false;



            AuthSession authSession = (AuthSession)ctxInstance.getPortletRequest().getUserPrincipal();



            if (order.getAuthSession() == null && authSession != null)

            {

                if (log.isDebugEnabled())

                    log.debug("AuthSession is null. Try get from session");



                if (log.isDebugEnabled())

                    log.debug("AuthSession in servlet session - " + authSession);



                if (authSession != null && log.isDebugEnabled())

                    log.debug("AuthSession not null. getLoginStatus() - " + authSession.checkAccess( ctxInstance.getPortletRequest().getServerName()));



                if ((authSession != null) && (authSession.checkAccess( ctxInstance.getPortletRequest().getServerName())))

                {

                    DatabaseAdapter dbDyn = null;

                    try

                    {

                        dbDyn = DatabaseAdapter.getInstance(true);



                        if (log.isDebugEnabled())

                            log.debug("Update order with new authSession");



                        OrderLogic.updateAuthSession(dbDyn, order, authSession);

                        AuthSessionType orderAuthSession =

                            new AuthSessionType();



                        orderAuthSession.setUserInfo(authSession.getUserInfo());

                        order.setAuthSession(authSession);

                    }

                    catch (Exception e1)

                    {

                        try

                        {

                            dbDyn.rollback();

                        }

                        catch (Exception e02)

                        {

                        }

                        throw e1;

                    }

                    finally

                    {

                        try

                        {

                            dbDyn.commit();

                        }

                        catch (Exception e01)

                        {

                        }



                        DatabaseAdapter.close(dbDyn);

                        dbDyn = null;

                    }

                }

            }



            Long currencyID = PortletTools.getLong(ctxInstance.getPortletRequest(), Constants.NAME_ID_CURRENCY_SHOP);



            String addForm =

                ServletTools.getHiddenItem(

                    Constants.NAME_ID_CURRENCY_SHOP,

                    currencyID

                ) +

                ServletTools.getHiddenItem(

                    Constants.NAME_ID_GROUP_SHOP,

                    PortletTools.getInt(ctxInstance.getPortletRequest(), Constants.NAME_ID_GROUP_SHOP)

                ) +

                ServletTools.getHiddenItem(

                    Constants.NAME_ID_SHOP_PARAM,

                    shop.id_shop

                );



            String addUrl =

                Constants.NAME_ID_CURRENCY_SHOP + '=' +

                currencyID + '&' +

                Constants.NAME_ID_GROUP_SHOP + '=' +

                PortletTools.getInt(ctxInstance.getPortletRequest(), Constants.NAME_ID_GROUP_SHOP) + '&' +

                Constants.NAME_ID_SHOP_PARAM + '=' + shop.id_shop;



            String action = PortletTools.getString(ctxInstance.getPortletRequest(), "action");



            if (log.isDebugEnabled())

                log.debug("Action - " + action);



            if (action != null)

            {



                if (log.isDebugEnabled())

                    log.debug("Action is NOT null");



                if (action.equals("set"))

                {

                    Long id_item = PortletTools.getLong(ctxInstance.getPortletRequest(), "set_id_item");

                    int count = PortletTools.getInt(ctxInstance.getPortletRequest(), Constants.NAME_INVOICE_NEW_COUNT_PARAM, new Integer(0)).intValue();



                    if (log.isDebugEnabled())

                    {

                        log.debug("action - set");

                        log.debug("id_item - " + id_item);

                        log.debug("count - " + count);

                    }



                    // set new quantyt of item in backet

                    DatabaseAdapter dbDyn = null;

                    try

                    {

                        dbDyn = DatabaseAdapter.getInstance(true);

                        OrderLogic.setItem(dbDyn, order, id_item, count);

                    }

                    catch (Exception e1)

                    {

                        try

                        {

                            dbDyn.rollback();

                        }

                        catch (Exception e02)

                        {

                        }

                        throw e1;

                    }

                    finally

                    {

                        try

                        {

                            dbDyn.commit();

                        }

                        catch (Exception e01)

                        {

                        }



                        DatabaseAdapter.close(dbDyn);

                        dbDyn = null;

                    }

                }

                else if (action.equals("del"))

                {

                    Long id_item = PortletTools.getLong(ctxInstance.getPortletRequest(), "del_id_item");

                    if (log.isDebugEnabled())

                    {

                        log.debug("action - del");

                        log.debug("id_item - " + id_item);

                    }



                    // delete item from basket

                    DatabaseAdapter dbDyn = null;

                    try

                    {

                        dbDyn = DatabaseAdapter.getInstance(true);

                        OrderLogic.delItem(dbDyn, order, id_item);

                    }

                    catch (Exception e1)

                    {

                        log.error("Error delete item from basket", e1);

                        out.write(ExceptionTools.getStackTrace(e1, 100, "<br>"));

                        try

                        {

                            dbDyn.rollback();

                        }

                        catch (Exception e02)

                        {

                        }

                        throw e1;

                    }

                    finally

                    {

                        try

                        {

                            dbDyn.commit();

                        }

                        catch (Exception e01)

                        {

                        }



                        DatabaseAdapter.close(dbDyn);

                        dbDyn = null;

                    }

                }

            }



            {

// синхронизируем то что хранится в сессии с базой данных

                DatabaseAdapter dbDyn = null;

                try

                {

                    dbDyn = DatabaseAdapter.getInstance(true);

                    PriceList.synchronizeOrderWithDb(dbDyn, order);

                }

                catch (Exception e1)

                {

                    log.error("Error delete item from basket", e1);

                    out.write(ExceptionTools.getStackTrace(e1, 100, "<br>"));

                    try

                    {

                        dbDyn.rollback();

                    }

                    catch (Exception e02)

                    {

                    }

                    throw e1;

                }

                finally

                {

                    try

                    {

                        dbDyn.commit();

                    }

                    catch (Exception e01)

                    {

                    }



                    DatabaseAdapter.close(dbDyn);

                    dbDyn = null;

                }

            }



            if (authSession != null && "send".equals(action) )

            {

                // send order



                String orderCustomString =

                    ctxInstance.sCustom.getStr("reg.send_order.1",

                        new Object[]

                        {

                            ""+order.getIdOrder()

                        }

                    );

                MainUserInfoType ui = authSession.getUserInfo();

                String orderAdminString =

                    "Заказчик: "+ ui.getLastName()+' '+ui.getFirstName()+' '+ui.getMiddleName()+"\n\n"+

                    orderCustomString;





                for (int k = 0; k < order.getShopOrdertListCount(); k++)

                {

                    ShopOrderType shopOrder = order.getShopOrdertList(k);



                    if (shopOrder.getOrderItemListCount()>0)

                    {

                        OrderItemType itemTemp = shopOrder.getOrderItemList(0);

                        Shop shopTemp = Shop.getInstance(db_, shopOrder.getIdShop());

                        orderCustomString += ctxInstance.sCustom.getStr("reg.send_order.shop-header",

                            new Object[]

                            {

                                ""+shopOrder.getIdShop(),

                                shopTemp.name_shop,

                                shopTemp.code_shop,

                                itemTemp.getResultCurrency().getCurrencyName()

                            }

                        );

                        for (int i = 0; i < shopOrder.getOrderItemListCount(); i++)

                        {

                            OrderItemType item = shopOrder.getOrderItemList(i);



                            double itemFullPrice =

                                NumberTools.multiply(

                                    item.getPriceItemResult().doubleValue(),

                                    item.getCountItem().intValue(),

                                    item.getPrecisionResult().intValue()

                                );



                            orderCustomString += ctxInstance.sCustom.getStr("reg.send_order.2",

                                new String[]

                                {

                                    "" + (i + 1),

                                    item.getItem(),

                                    NumberTools.toString(item.getPriceItemResult().doubleValue(), item.getPrecisionResult().intValue()),

                                    item.getResultCurrency().getCurrencyName(),

                                    "" + item.getCountItem(),

                                    NumberTools.toString(itemFullPrice, item.getPrecisionResult().intValue()),

                                    "",

                                    "" + shopOrder.getIdShop(),

                                    "" + item.getIdOrigin()

                                }

                            );

                        }

                    }

                }



                if (log.isDebugEnabled())

                    log.debug("Your order N" + order.getIdOrder()+"\n"+orderCustomString);



                if (Boolean.TRUE.equals(ctxInstance.page.p.sites.getIsActivateEmailOrder()) )

                {

                    if (log.isDebugEnabled())

                    {

                        synchronized(syncObj)

                        {

                            XmlTools.writeToFile(order, WebmillConfig.getWebmillDebugDir()+"order.xml");

                        }

                    }

                    String currentCurrency = "";

                    int currentPrecision = 0;

                    for (int k = 0; k < order.getShopOrdertListCount(); k++)

                    {

                        ShopOrderType shopOrder = order.getShopOrdertList(k);

                        OrderItemType itemTemp = shopOrder.getOrderItemList(0);

                        Shop shopTemp = Shop.getInstance(db_, shopOrder.getIdShop());

                        orderAdminString += ctxInstance.sCustom.getStr("reg.send_order.shop-header",

                            new Object[]

                            {

                                ""+shopOrder.getIdShop(),

                                shopTemp.name_shop,

                                shopTemp.code_shop,

                                itemTemp.getResultCurrency().getCurrencyName()

                            }

                        );



                        double orderSumm = 0;

                        for (int i = 0; i < shopOrder.getOrderItemListCount(); i++)

                        {

                            OrderItemType item = shopOrder.getOrderItemList(i);



                            if (i == 0)

                            {

                                currentCurrency = item.getResultCurrency().getCurrencyName();

                                if (item.getPrecisionResult()!=null)

                                    currentPrecision = item.getPrecisionResult().intValue();

                            }



                            double itemFullPrice =

                                NumberTools.multiply(

                                    item.getPriceItemResult().doubleValue(),

                                    item.getCountItem().intValue(),

                                    item.getPrecisionResult().intValue()

                                );



                            orderSumm += itemFullPrice;



                            orderAdminString += ctxInstance.sCustom.getStr("reg.send_order.2",

                                new String[]

                                {

                                    "" + (i + 1),

                                    item.getItem(),

                                    NumberTools.toString(item.getPriceItemResult().doubleValue(), currentPrecision),

                                    item.getResultCurrency().getCurrencyName(),

                                    "" + item.getCountItem(),

                                    NumberTools.toString(itemFullPrice, currentPrecision),

                                    "",

                                    "" + shopOrder.getIdShop(),

                                    "" + item.getIdOrigin()

                                }

                            );

                        }

                        orderAdminString +=

                            "Итого на сумму: "+

                            NumberTools.toString(NumberTools.truncate(orderSumm, currentPrecision), currentPrecision) +

                            " " + currentCurrency+"\n\n";

                    }

                    if (log.isDebugEnabled())

                        log.debug("send admin order: your order N" + order.getIdOrder()+"\n"+orderAdminString);

                }



                MailMessage.sendMessage(

                    orderCustomString,

                    order.getAuthSession().getUserInfo().getEmail(),

                    ctxInstance.page.p.sites.getOrderEmail(),

                    "Your order N" + order.getIdOrder(),

                            GenericConfig.getMailSMTPHost()

                );





                if (Boolean.TRUE.equals(ctxInstance.page.p.sites.getIsActivateEmailOrder()) )

                {

                    MailMessage.sendMessage(

                        orderAdminString,

                        ctxInstance.page.p.sites.getOrderEmail(),

                        ctxInstance.page.p.sites.getOrderEmail(),

                        "Order N" + order.getIdOrder(),

                        GenericConfig.getMailSMTPHost()

                    );

                }



                String shopUrl = "<a href=\"" +

                    ctxInstance.url(Constants.CTX_TYPE_SHOP) + '&' +

                    addUrl + "\">";

                String str = null;

                String args1[] = {index_page};

                if (ctxInstance.page.sMain.checkKey("invoice.order-send-complete"))

                    str = ctxInstance.page.sMain.getStr("invoice.order-send-complete", args1);

                else

                    str = "Ваш заказ N"+order.getIdOrder()+" успешно отослан. "+shopUrl+"Продолжить</a>";



                args1 = null;

                out.write(str);

                session.removeAttribute( Constants.ORDER_SESSION );

                return;

            }



            if (order.getAuthSession() == null)

            {



                String backURL = StringTools.rewriteURL(invoice_page);



                out.write("<table cellspacing=\"2\" cellpadding=\"2\">\n");

                out.write("<tr>\n");

                out.write("<td class=\"pricedata\" rowspan=\"2\">\n");

                out.write(ctxInstance.sCustom.getStr("invoice.attention"));

                out.write("<br>\n");



                if (Boolean.TRUE.equals(ctxInstance.page.p.sites.getIsRegisterAllowed()) )

                {



                    out.write(ctxInstance.sCustom.getStr("invoice.register"));

                    out.write("\n");

                    out.write("<a href=\"");

                    out.write(



                        ctxInstance.url("mill.register") + '&' +

                        Constants.NAME_TOURL_PARAM + '=' + backURL



                    );

                    out.write("\">");

                    out.write(ctxInstance.page.sMain.getStr("button.next"));

                    out.write(" ");

                    out.write("</a>\n");



                }

                out.write(

                    "</td>\n"+

                    "<form method=\"POST\" action=\"" + CtxURL.ctx() + "\">\n"+

                    ctxInstance.urlAsForm(ctxInstance.getNameTemplate(), "mill.register")+

                    addForm+

                    ServletTools.getHiddenItem(Constants.NAME_TOURL_PARAM, backURL)+

                    ServletTools.getHiddenItem(Constants.NAME_REGISTER_ACTION_PARAM, "reg_exists")+

                    "<td class=\"pricedata\">"

                );



                out.write(ctxInstance.sCustom.getStr("invoice.login"));

                out.write("</td>\n");

                out.write("<td class=\"pricedata\">");

                out.write("<input type=\"text\" name=\"username\" size=\"12\" tabindex=\"1\">");

                out.write("</td>\n");

                out.write("<td rowspan=\"2\" valign=\"middle\" class=\"pricedata\">");

                out.write("<input type=\"submit\" value=\"Login\" tabindex=\"3\">\n");

                out.write("<tr>\n");

                out.write("<td class=\"pricedata\">");

                out.write(ctxInstance.sCustom.getStr("invoice.password"));

                out.write("</td>\r\n");

                out.write("<td class=\"pricedata\">");

                out.write("<input type=\"password\" name=\"password1\" size=\"12\" tabindex=\"2\">");

                out.write("</td>\n");

                out.write("</tr>\n");

                out.write("</form>\n");

                out.write("</table>\n");

                out.write("<br>");

            }	// order.authSession == null



            out.write("<table border=\"0\">\n<tr>\n<td align=\"left\">\n");

            out.write("<a href=\"" +

                ctxInstance.url(Constants.CTX_TYPE_SHOP) + '&' +

                addUrl + "\">");



            out.write(ctxInstance.sCustom.getStr("invoice.continue_select"));

            out.write("</a>");

            out.write("</td>\n</tr>\n</table>\n<br>");

            out.write(

                ctxInstance.sCustom.getStr("invoice.your_select")

            );



//            double totalOrderSumm = 0;

            out.write("<table border=\"0\" cellpadding=\"2px\" cellspacing=\"2px\">\n");

            for (int k = 0; k < order.getShopOrdertListCount(); k++)

            {

                ShopOrderType shopOrder = order.getShopOrdertList(k);

                if (shopOrder.getOrderItemListCount() > 0)

                {

                    Shop tempShop = Shop.getInstance(db_, shopOrder.getIdShop());

                    out.write("<tr>\n<td colspan=\"6\" align=\"left\" border=\"0\">\n");

                    out.write(tempShop.name_shop_for_price_list);

                    out.write("</td>\n<tr>\n");



                    out.write("<tr>\n");

                    out.write("<th class=\"priceData\">");

                    out.write(ctxInstance.sCustom.getStr("invoice.name_item"));

                    out.write("</th>\n");

                    out.write("<th class=\"priceData\">");

                    out.write(ctxInstance.sCustom.getStr("invoice.ppq"));

                    out.write("</th>\n");

                    out.write("<th class=\"priceData\">");

                    out.write(ctxInstance.sCustom.getStr("invoice.quantity"));

                    out.write("</th>\n");

                    out.write("<th class=\"priceData\">");

                    out.write(ctxInstance.sCustom.getStr("invoice.total_price"));

                    out.write("</th>\n");

                    out.write("<th class=\"priceData\">");

                    out.write(ctxInstance.sCustom.getStr("invoice.currency"));

                    out.write("</th>\n");

                    out.write("<th class=\"priceData\">");

                    out.write(ctxInstance.sCustom.getStr("invoice.delete_item"));

                    out.write("</th>\n");

                    out.write("</tr>");



                    String currentCurrency = "";

                    double orderSumm = 0;

                    int currentPrecision = 0;

                    for (int i = 0; i < shopOrder.getOrderItemListCount(); i++)

                    {

                        OrderItemType item = shopOrder.getOrderItemList(i);



                        if (i == 0)

                        {

                            currentCurrency = item.getResultCurrency().getCurrencyName();

                            currentPrecision = item.getPrecisionResult().intValue();

                        }



                        double itemFullPrice =

                            NumberTools.multiply(

                                item.getPriceItemResult().doubleValue(),

                                item.getCountItem().intValue(),

                                item.getPrecisionResult().intValue()

                            );



                        orderSumm += itemFullPrice;



                        out.write("<tr>\n");

                        out.write("<td class=\"priceData\">");

                        out.write(item.getItem());

                        out.write("</td>\n");

                        out.write("<form method=\"GET\" action=\"" + CtxURL.ctx() + "\">\n");

                        out.write(indexPageForm);

                        out.write(addForm);

                        out.write(ServletTools.getHiddenItem("set_id_item", item.getIdItem()));

                        out.write(ServletTools.getHiddenItem("action", "set"));

                        out.write("<td class=\"priceData\" align=\"right\">");

                        out.write(NumberTools.toString(item.getPriceItemResult().doubleValue(), currentPrecision));

                        out.write("</td>\n");

                        out.write("<td class=\"priceData\" align=\"center\">\n");

                        out.write("<input type=\"text\" size=\"3\" name=\"" +

                            Constants.NAME_INVOICE_NEW_COUNT_PARAM +

                            "\" value=\"" + item.getCountItem() + "\">&nbsp;\n"

                        );

                        out.write("<input type=\"submit\" value=\"");

                        out.write(ctxInstance.sCustom.getStr("invoice.change_qty"));

                        out.write("\">");

                        out.write("</td>\n");

                        out.write("<td class=\"priceData\" align=\"right\">");

                        out.write(NumberTools.toString(itemFullPrice, currentPrecision));

                        out.write("</td>\n");

                        out.write("<td class=\"priceData\" align=\"center\">");

                        out.write(item.getResultCurrency().getCurrencyName());

                        out.write("</td>\n");

                        out.write("</form>\n");

                        out.write("<form method=\"GET\" action=\"" + CtxURL.ctx() + "\">\n");

                        out.write(indexPageForm);

                        out.write(addForm);

//                    out.write( ServletTools.getHiddenItem(Constants.NAME_ID_SHOP_PARAM, shop.id_shop) );

                        out.write(ServletTools.getHiddenItem("del_id_item", item.getIdItem()));

                        out.write(ServletTools.getHiddenItem("action", "del"));

                        out.write("<td class=\"priceData\" align=\"center\">");

                        out.write("<input type=\"submit\" value=\"");

                        out.write(ctxInstance.sCustom.getStr("invoice.delete_button"));

                        out.write("\">");

                        out.write("</td>\n");

                        out.write("</form>\n");

                        out.write("</tr>\n");



                    } // for (int i=0; i<items.size(); i++)



                    out.write("<tr>\n<td colspan=\"6\" align=\"left\" border=\"0\">\n");

                    out.write(ctxInstance.sCustom.getStr("invoice.total_summ"));

                    out.write(" " +

                        NumberTools.toString(NumberTools.truncate(orderSumm, currentPrecision), currentPrecision) +

                        " " + currentCurrency

                    );

                    out.write("</td>\n<tr>\n");



                    if (log.isDebugEnabled())

                    {

                        out.write("<tr>\n<td colspan=\"6\" align=\"left\" border=\"0\">\n");

                        out.write(ctxInstance.sCustom.getStr("invoice.total_summ"));

                        out.write(" " + orderSumm + " " + currentCurrency);

                        out.write("</td>\n<tr>\n");

                    }



                    out.write("<tr>\n<td colspan=\"6\" align=\"left\" border=\"0\">\n");

                    out.write("&nbsp;");

                    out.write("</td>\n<tr>\n");

                }

//                totalOrderSumm += orderSumm;

            }

            out.write("</table>\n");





            if ((order.getShopOrdertListCount() != 0) && (order.getAuthSession() != null) &&

                (ctxInstance.page.p.sites.getOrderEmail().trim().length() != 0) &&

                (Boolean.TRUE.equals(ctxInstance.page.p.sites.getIsActivateEmailOrder()) )

            )

            {



                out.write("<br>\n");

                out.write("<form method=\"GET\" action=\"" + CtxURL.ctx() + "\">\n");

                out.write(indexPageForm);

                out.write(addForm);

                out.write(ServletTools.getHiddenItem("action", "send"));

                out.write("<input type=\"submit\" value=\"");

                out.write(ctxInstance.sCustom.getStr("invoice.send_button"));

                out.write("\">\n");

                out.write("</form>");



            }

        }

        catch (Exception e)

        {

            log.error("Error procesing invoice", e);

            out.write(ExceptionTools.getStackTrace(e, 100, "<br>"));

        }

        finally

        {

            DatabaseAdapter.close(db_);

            db_ = null;

        }

    }

}

