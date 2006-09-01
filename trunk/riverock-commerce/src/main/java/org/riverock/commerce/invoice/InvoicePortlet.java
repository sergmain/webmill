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
package org.riverock.commerce.invoice;

import java.io.File;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.riverock.common.tools.ExceptionTools;
import org.riverock.common.tools.NumberTools;
import org.riverock.common.tools.ServletTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.tools.XmlTools;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.UserInfo;
import org.riverock.commerce.price.OrderLogic;
import org.riverock.commerce.price.PriceList;
import org.riverock.commerce.price.Shop;
import org.riverock.commerce.price.ShopPortlet;
import org.riverock.portlet.schema.price.OrderItemType;
import org.riverock.portlet.schema.price.ShopOrderType;
import org.riverock.commerce.shop.bean.ShopOrder;
import org.riverock.commerce.tools.ContentTypeTools;
import org.riverock.commerce.tools.SiteUtils;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.tools.PortletService;

/**
 * Author: mill
 * Date: Dec 3, 2002
 * Time: 3:14:51 PM
 *
 * $Id$
 */

@SuppressWarnings({"UnusedAssignment"})
public final class InvoicePortlet implements Portlet {
    private final static Logger log = Logger.getLogger( InvoicePortlet.class );

    public final static String CTX_TYPE_INVOICE = "mill.invoice";

    public InvoicePortlet() {
    }

    private PortletConfig portletConfig = null;

    public void init( PortletConfig portletConfig ) {
        this.portletConfig = portletConfig;
    }

    public void processAction( ActionRequest actionRequest, ActionResponse actionResponse ) {
    }

    public void destroy() {
    }

    private final static Object syncObj = new Object();

    public void render( RenderRequest renderRequest, RenderResponse renderResponse )
        throws PortletException {
        OrderLogic.process( renderRequest );

        ResourceBundle bundle = portletConfig.getResourceBundle( renderRequest.getLocale() );
        PrintWriter out = null;
        try {
            ContentTypeTools.setContentType( renderResponse, ContentTypeTools.CONTENT_TYPE_UTF8 );
            out = renderResponse.getWriter();

            PortletSession session = renderRequest.getPortletSession();
            ShopOrder order = (ShopOrder)session.getAttribute( ShopPortlet.ORDER_SESSION, PortletSession.APPLICATION_SCOPE );

            if ( order == null )
                return;

            Shop shop = (Shop)session.getAttribute( ShopPortlet.CURRENT_SHOP, PortletSession.APPLICATION_SCOPE );

            AuthSession authSession = (AuthSession)renderRequest.getUserPrincipal();


            boolean isActivateEmailOrder = false;
            String orderEmail = null;

            // Todo uncomment and implement
/*
            boolean isRegisterAllowed = portalInfo.getSites().getIsRegisterAllowed();
            boolean isActivateEmailOrder = portalInfo.getSites().getIsActivateEmailOrder();
            String orderEmail = portalInfo.getSites().getOrderEmail();
*/

            if ( order.getAuthSession() == null && authSession != null ) {
                if ( log.isDebugEnabled() )
                    log.debug( "AuthSession is null. Try get from session" );

                if ( log.isDebugEnabled() )
                    log.debug( "AuthSession in servlet session - " + authSession );

                if ( authSession != null && log.isDebugEnabled() )
                    log.debug( "AuthSession not null. getLoginStatus() - " + authSession.checkAccess( renderRequest.getServerName() ) );

                if ( ( authSession != null ) && ( authSession.checkAccess( renderRequest.getServerName() ) ) ) {
                    DatabaseAdapter dbDyn = null;
                    try {
                        dbDyn = DatabaseAdapter.getInstance();

                        if ( log.isDebugEnabled() )
                            log.debug( "Update order with new authSession" );

                        OrderLogic.updateAuthSession( dbDyn, order, authSession );
                        order.setAuthSession( authSession );
                        dbDyn.commit();
                    }
                    catch( Exception e1 ) {
                        try {
                            dbDyn.rollback();
                        }
                        catch( Exception e02 ) {
                            // catch rollback error
                        }
                        throw e1;
                    }
                    finally {
                        DatabaseAdapter.close( dbDyn );
                        dbDyn = null;
                    }
                }
            }

            Long currencyID = PortletService.getLong( renderRequest, ShopPortlet.NAME_ID_CURRENCY_SHOP );

            String addForm =
                ServletTools.getHiddenItem( ContainerConstants.NAME_TYPE_CONTEXT_PARAM, CTX_TYPE_INVOICE ) +
                ServletTools.getHiddenItem( ShopPortlet.NAME_ID_CURRENCY_SHOP, currencyID ) +
                ServletTools.getHiddenItem( ShopPortlet.NAME_ID_GROUP_SHOP,
                    PortletService.getInt( renderRequest, ShopPortlet.NAME_ID_GROUP_SHOP, 0 ) ) +
                ServletTools.getHiddenItem( ShopPortlet.NAME_ID_SHOP_PARAM, shop.getShopBean().id_shop );

            String addUrl =
                ShopPortlet.NAME_ID_CURRENCY_SHOP + '=' + currencyID + '&' +
                ShopPortlet.NAME_ID_GROUP_SHOP + '=' +
                PortletService.getInt( renderRequest, ShopPortlet.NAME_ID_GROUP_SHOP, 0 ) + '&' +
                ShopPortlet.NAME_ID_SHOP_PARAM + '=' + shop.getShopBean().id_shop;

            String action = PortletService.getString(renderRequest, "action", null);

            if ( log.isDebugEnabled() )
                log.debug( "Action - " + action );

            if ( action != null ) {

                if ( log.isDebugEnabled() )
                    log.debug( "Action is NOT null" );

                if ( action.equals( "set" ) ) {
                    Long id_item = PortletService.getLong( renderRequest, "set_id_item" );
                    int count = PortletService.getInt( renderRequest, ShopPortlet.NAME_INVOICE_NEW_COUNT_PARAM, 0 );

                    if ( log.isDebugEnabled() ) {
                        log.debug( "action - set" );
                        log.debug( "id_item - " + id_item );
                        log.debug( "count - " + count );
                    }

                    // set new quantity of item in backet
                    DatabaseAdapter dbDyn = null;
                    try {
                        dbDyn = DatabaseAdapter.getInstance();
                        Long siteId = new Long( renderRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
                        OrderLogic.setItem( dbDyn, order, id_item, count, siteId );
                        dbDyn.commit();
                    }
                    catch( Exception e1 ) {
                        try {
                            dbDyn.rollback();
                        }
                        catch( Exception e02 ) {
                            // catch roolback error
                        }
                        throw e1;
                    }
                    finally {
                        DatabaseAdapter.close( dbDyn );
                        dbDyn = null;
                    }
                } else if ( action.equals( "del" ) ) {
                    Long id_item = PortletService.getLong( renderRequest, "del_id_item" );
                    if ( log.isDebugEnabled() ) {
                        log.debug( "action - del" );
                        log.debug( "id_item - " + id_item );
                    }

                    // delete item from basket
                    DatabaseAdapter dbDyn = null;
                    try {
                        dbDyn = DatabaseAdapter.getInstance();
                        OrderLogic.delItem( dbDyn, order, id_item );
                    }
                    catch( Exception e1 ) {
                        log.error( "Error delete item from basket", e1 );
                        out.write( ExceptionTools.getStackTrace( e1, 100, "<br>" ) );
                        try {
                            dbDyn.rollback();
                        }
                        catch( Exception e02 ) {
                            // catch rollback error
                        }
                        throw e1;
                    }
                    finally {
                        DatabaseAdapter.close( dbDyn );
                        dbDyn = null;
                    }
                }
            }

            {
                // synchronize session with DB
                DatabaseAdapter dbDyn = null;
                try {
                    dbDyn = DatabaseAdapter.getInstance();
                    PriceList.synchronizeOrderWithDb( dbDyn, order );
                    dbDyn.commit();
                }
                catch( Exception e1 ) {
                    log.error( "Error delete item from basket", e1 );
                    out.write( ExceptionTools.getStackTrace( e1, 100, "<br>" ) );
                    try {
                        dbDyn.rollback();
                    }
                    catch( Exception e02 ) {
                        // catch rollback error
                    }
                    throw e1;
                }
                finally {
                    DatabaseAdapter.close( dbDyn );
                    dbDyn = null;
                }
            }

            if ( authSession != null && "send".equals( action ) ) {
                // send order

                String s = bundle.getString( "reg.send_order.1" );
                String orderCustomString =
                    MessageFormat.format( s, "" + order.getIdOrder());

                UserInfo ui = authSession.getUserInfo();
                String orderAdminString =
                    "Заказчик: " + ui.getLastName() + ' ' + ui.getFirstName() + ' ' + ui.getMiddleName() + "\n\n" +
                    orderCustomString;


                for( int k = 0; k<order.getShopOrdertListCount(); k++ ) {
                    ShopOrderType shopOrder = order.getShopOrdertList( k );

                    if ( shopOrder.getOrderItemListCount()>0 ) {
                        OrderItemType itemTemp = shopOrder.getOrderItemList( 0 );
                        Shop shopTemp = Shop.getInstance( shopOrder.getIdShop() );

                        s = bundle.getString( "reg.send_order.shop-header" );
                        orderCustomString +=
                            MessageFormat.format( s,
                                "" + shopOrder.getIdShop(),
                                shopTemp.getShopBean().name_shop,
                                shopTemp.getShopBean().code_shop,
                                itemTemp.getResultCurrency().getCurrencyName());

                        for( int i = 0; i<shopOrder.getOrderItemListCount(); i++ ) {
                            OrderItemType item = shopOrder.getOrderItemList( i );

                            double itemFullPrice =
                                NumberTools.multiply(
                                    item.getWmPriceItemResult(), item.getCountItem(), item.getPrecisionResult()
                                );

                            s = bundle.getString( "reg.send_order.2" );
                            orderCustomString +=
                                MessageFormat.format( s,
                                    "" + ( i + 1 ),
                                    item.getItem(),
                                    NumberTools.toString( item.getWmPriceItemResult(), item.getPrecisionResult() ),
                                    item.getResultCurrency().getCurrencyName(),
                                    "" + item.getCountItem(),
                                    NumberTools.toString( itemFullPrice, item.getPrecisionResult() ),
                                    "",
                                    "" + shopOrder.getIdShop(),
                                    "" + item.getIdOrigin());
                        }
                    }
                }

                if ( log.isDebugEnabled() )
                    log.debug( "Your order N" + order.getIdOrder() + "\n" + orderCustomString );

                if ( Boolean.TRUE.equals( isActivateEmailOrder ) ) {
                    if ( log.isDebugEnabled() ) {
                        synchronized (syncObj) {
                            XmlTools.writeToFile( order, SiteUtils.getTempDir() + File.separatorChar + "order.xml" );
                        }
                    }
                    String currentCurrency = "";
                    int currentPrecision = 0;
                    for( int k = 0; k<order.getShopOrdertListCount(); k++ ) {
                        ShopOrderType shopOrder = order.getShopOrdertList( k );
                        OrderItemType itemTemp = shopOrder.getOrderItemList( 0 );
                        Shop shopTemp = Shop.getInstance( shopOrder.getIdShop() );

                        s = bundle.getString( "reg.send_order.shop-header" );
                        orderAdminString +=
                            MessageFormat.format( s,
                                "" + shopOrder.getIdShop(),
                                shopTemp.getShopBean().name_shop,
                                shopTemp.getShopBean().code_shop,
                                itemTemp.getResultCurrency().getCurrencyName());

                        double orderSumm = 0;
                        for( int i = 0; i<shopOrder.getOrderItemListCount(); i++ ) {
                            OrderItemType item = shopOrder.getOrderItemList( i );

                            if ( i == 0 ) {
                                currentCurrency = item.getResultCurrency().getCurrencyName();
                                if ( item.getPrecisionResult() != null )
                                    currentPrecision = item.getPrecisionResult();
                            }

                            double itemFullPrice =
                                NumberTools.multiply(
                                    item.getWmPriceItemResult(), item.getCountItem(), item.getPrecisionResult()
                                );

                            orderSumm += itemFullPrice;

                            s = bundle.getString( "reg.send_order.2" );
                            orderAdminString +=
                                MessageFormat.format( s,
                                    "" + ( i + 1 ),
                                    item.getItem(),
                                    NumberTools.toString( item.getWmPriceItemResult(), currentPrecision ),
                                    item.getResultCurrency().getCurrencyName(),
                                    "" + item.getCountItem(),
                                    NumberTools.toString( itemFullPrice, currentPrecision ),
                                    "",
                                    "" + shopOrder.getIdShop(),
                                    "" + item.getIdOrigin());
                        }
                        orderAdminString +=
                            "Итого на сумму: " +
                            NumberTools.toString( NumberTools.truncate( orderSumm, currentPrecision ), currentPrecision ) +
                            " " + currentCurrency + "\n\n";
                    }
                    if ( log.isDebugEnabled() )
                        log.debug( "send admin order: your order N" + order.getIdOrder() + "\n" + orderAdminString );
                }

                if (true) {
                    throw new IllegalStateException("Need switch to user Poral mail service");
                }
/*
                MailMessage.sendMessage( orderCustomString,
                    order.getAuthSession().getUserInfo().getEmail(),
                    orderEmail,
                    "Your order N" + order.getIdOrder(),
                    GenericConfig.getMailSMTPHost() );


                if ( Boolean.TRUE.equals( isActivateEmailOrder ) ) {
                    MailMessage.sendMessage( orderAdminString,
                        orderEmail,
                        orderEmail,
                        "Order N" + order.getIdOrder(),
                        GenericConfig.getMailSMTPHost() );
                }
*/
                String shopUrl = "<a href=\"" +
                    PortletService.url( ShopPortlet.CTX_TYPE_SHOP, renderRequest, renderResponse ) + '&' +
                    addUrl + "\">";

                String str = null;
                Object args1[] = {PortletService.url( "mill.index", renderRequest, renderResponse )};

                final String sendOrderName = "invoice.order-send-complete";
                s = bundle.getString( sendOrderName );
                if ( s != null ) {
                    str = MessageFormat.format( s, args1 );
                } else {
                    str = "Ваш заказ N" + order.getIdOrder() + " успешно отослан. " + shopUrl + "Продолжить</a>";
                }

                args1 = null;
                out.write( str );
                session.removeAttribute( ShopPortlet.ORDER_SESSION, PortletSession.APPLICATION_SCOPE );
                return;
            }
/*
            if ( order.getAuthSession() == null ) {

                String backURL = StringTools.rewriteURL( PortletService.url( "mill.invoice", renderRequest, renderResponse ) );

                out.write( "<table cellspacing=\"2\" cellpadding=\"2\">\n" );
                out.write( "<tr>\n" );
                out.write( "<td class=\"pricedata\" rowspan=\"2\">\n" );
                out.write( bundle.getString( "invoice.attention" ) );
                out.write( "<br>\n" );

                if ( Boolean.TRUE.equals( isRegisterAllowed ) ) {

                    out.write( bundle.getString( "invoice.register" ) );
                    out.write( "\n" );
                    out.write( "<a href=\"" );
                    out.write( PortletService.url( "mill.register", renderRequest, renderResponse ) + '&' +
                        LoginUtils.NAME_TOURL_PARAM + '=' + backURL );
                    out.write( "\">" );
                    out.write( bundle.getString( "button.next" ) );
                    out.write( " " );
                    out.write( "</a>\n" );

                }
                out.write( "</td>\n" +
                    "<form method=\"POST\" action=\"" + PortletService.ctx( renderRequest ) + "\">\n" +
                    ServletTools.getHiddenItem( ContainerConstants.NAME_TYPE_CONTEXT_PARAM, LoginUtils.LOGIN_CHECK_PORTLET ) +
                    addForm +
                    ServletTools.getHiddenItem( LoginUtils.NAME_TOURL_PARAM, backURL ) +
                    "<td class=\"pricedata\">" );

                out.write( bundle.getString( "invoice.login" ) );
                out.write( "</td>\n" );
                out.write( "<td class=\"pricedata\">" );
                out.write( "<input type=\"text\" name=\"username\" size=\"12\" tabindex=\"1\">" );
                out.write( "</td>\n" );
                out.write( "<td rowspan=\"2\" valign=\"middle\" class=\"pricedata\">" );
                out.write( "<input type=\"submit\" value=\"Login\" tabindex=\"3\">\n" );
                out.write( "<tr>\n" );
                out.write( "<td class=\"pricedata\">" );
                out.write( bundle.getString( "invoice.password" ) );
                out.write( "</td>\r\n" );
                out.write( "<td class=\"pricedata\">" );
                out.write( "<input type=\"password\" name=\"password1\" size=\"12\" tabindex=\"2\">" );
                out.write( "</td>\n" );
                out.write( "</tr>\n" );
                out.write( "</form>\n" );
                out.write( "</table>\n" );
                out.write( "<br>" );
            }
*/
            out.write( "<table border=\"0\">\n<tr>\n<td align=\"left\">\n" );
            out.write( "<a href=\"" +
                PortletService.url( ShopPortlet.CTX_TYPE_SHOP, renderRequest, renderResponse ) + '&' +
                addUrl + "\">" );

            out.write( bundle.getString( "invoice.continue_select" ) );
            out.write( "</a>" );
            out.write( "</td>\n</tr>\n</table>\n<br>" );
            out.write( bundle.getString( "invoice.your_select" ) );

            out.write( "<table border=\"0\" cellpadding=\"2px\" cellspacing=\"2px\">\n" );
            for( int k = 0; k<order.getShopOrdertListCount(); k++ ) {
                ShopOrderType shopOrder = order.getShopOrdertList( k );
                if ( shopOrder.getOrderItemListCount()>0 ) {
                    Shop tempShop = Shop.getInstance( shopOrder.getIdShop() );
                    out.write( "<tr>\n<td colspan=\"6\" align=\"left\" border=\"0\">\n" );
                    out.write( tempShop.getShopBean().name_shop_for_price_list );
                    out.write( "</td>\n<tr>\n" );

                    out.write( "<tr>\n" );
                    out.write( "<th class=\"priceData\">" );
                    out.write( bundle.getString( "invoice.name_item" ) );

                    out.write( "</th>\n" );
                    out.write( "<th class=\"priceData\">" );
                    out.write( bundle.getString( "invoice.ppq" ) );

                    out.write( "</th>\n" );
                    out.write( "<th class=\"priceData\">" );
                    out.write( bundle.getString( "invoice.quantity" ) );

                    out.write( "</th>\n" );
                    out.write( "<th class=\"priceData\">" );
                    out.write( bundle.getString( "invoice.total_price" ) );

                    out.write( "</th>\n" );
                    out.write( "<th class=\"priceData\">" );
                    out.write( bundle.getString( "invoice.currency" ) );

                    out.write( "</th>\n" );
                    out.write( "<th class=\"priceData\">" );
                    out.write( bundle.getString( "invoice.delete_item" ) );

                    out.write( "</th>\n" );
                    out.write( "</tr>" );

                    String currentCurrency = "";
                    double orderSumm = 0;
                    int currentPrecision = 0;
                    for( int i = 0; i<shopOrder.getOrderItemListCount(); i++ ) {
                        OrderItemType item = shopOrder.getOrderItemList( i );

                        if ( i == 0 ) {
                            currentCurrency = item.getResultCurrency().getCurrencyName();
                            currentPrecision = item.getPrecisionResult();
                        }

                        double itemFullPrice =
                            NumberTools.multiply(
                                item.getWmPriceItemResult(), item.getCountItem(), item.getPrecisionResult()
                            );

                        orderSumm += itemFullPrice;

                        out.write( "<tr>\n" );
                        out.write( "<td class=\"priceData\">" );
                        out.write( item.getItem() );
                        out.write( "</td>\n" );
                        out.write( "<form method=\"GET\" action=\"" + PortletService.ctx( renderRequest ) + "\">\n" );
                        out.write( addForm );
                        out.write( ServletTools.getHiddenItem( "set_id_item", item.getIdItem() ) );
                        out.write( ServletTools.getHiddenItem( "action", "set" ) );
                        out.write( "<td class=\"priceData\" align=\"right\">" );
                        out.write( NumberTools.toString( item.getWmPriceItemResult(), currentPrecision ) );
                        out.write( "</td>\n" );
                        out.write( "<td class=\"priceData\" align=\"center\">\n" );
                        out.write( "<input type=\"text\" size=\"3\" name=\"" +
                            ShopPortlet.NAME_INVOICE_NEW_COUNT_PARAM +
                            "\" value=\"" + item.getCountItem() + "\">&nbsp;\n" );
                        out.write( "<input type=\"submit\" value=\"" );
                        out.write( bundle.getString( "invoice.change_qty" ) );

                        out.write( "\">" );
                        out.write( "</td>\n" );
                        out.write( "<td class=\"priceData\" align=\"right\">" );
                        out.write( NumberTools.toString( itemFullPrice, currentPrecision ) );
                        out.write( "</td>\n" );
                        out.write( "<td class=\"priceData\" align=\"center\">" );
                        out.write( item.getResultCurrency().getCurrencyName() );
                        out.write( "</td>\n" );
                        out.write( "</form>\n" );
                        out.write( "<form method=\"GET\" action=\"" + PortletService.ctx( renderRequest ) + "\">\n" );
                        out.write( addForm );
                        out.write( ServletTools.getHiddenItem( "del_id_item", item.getIdItem() ) );
                        out.write( ServletTools.getHiddenItem( "action", "del" ) );
                        out.write( "<td class=\"priceData\" align=\"center\">" );
                        out.write( "<input type=\"submit\" value=\"" );
                        out.write( bundle.getString( "invoice.delete_button" ) );

                        out.write( "\">" );
                        out.write( "</td>\n" );
                        out.write( "</form>\n" );
                        out.write( "</tr>\n" );

                    }

                    out.write( "<tr>\n<td colspan=\"6\" align=\"left\" border=\"0\">\n" );
                    out.write( bundle.getString( "invoice.total_summ" ) );

                    out.write( " " +
                        NumberTools.toString( NumberTools.truncate( orderSumm, currentPrecision ), currentPrecision ) +
                        " " + currentCurrency );
                    out.write( "</td>\n<tr>\n" );

                    if ( log.isDebugEnabled() ) {
                        out.write( "<tr>\n<td colspan=\"6\" align=\"left\" border=\"0\">\n" );
                        out.write( bundle.getString( "invoice.total_summ" ) );

                        out.write( " " + orderSumm + " " + currentCurrency );
                        out.write( "</td>\n<tr>\n" );
                    }

                    out.write( "<tr>\n<td colspan=\"6\" align=\"left\" border=\"0\">\n" );
                    out.write( "&nbsp;" );
                    out.write( "</td>\n<tr>\n" );
                }
            }
            out.write( "</table>\n" );


            if ( order.getShopOrdertListCount()!= 0 && order.getAuthSession()!=null &&
                 !StringUtils.isBlank( orderEmail ) && isActivateEmailOrder ) {

                out.write( "<br>\n" );
                out.write( "<form method=\"GET\" action=\"" + PortletService.ctx( renderRequest ) + "\">\n" );
                out.write( addForm );
                out.write( ServletTools.getHiddenItem( "action", "send" ) );
                out.write( "<input type=\"submit\" value=\"" );
                out.write( bundle.getString( "invoice.send_button" ) );

                out.write( "\">\n" );
                out.write( "</form>" );

            }
        }
        catch( Exception e ) {
            final String es = "Error procesing invoice";
            log.error( es, e );
            throw new PortletException( es, e );
        }
        if (out!=null) {
            out.flush();
            out.close();
            out = null;
        }
    }
}
