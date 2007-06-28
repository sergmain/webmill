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
package org.riverock.commerce.invoice;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.*;

import org.apache.log4j.Logger;

import org.riverock.commerce.bean.Shop;
import org.riverock.commerce.bean.ShopOrder;
import org.riverock.commerce.bean.ShopOrderItem;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.price.OrderLogic;
import org.riverock.commerce.price.ShopPortlet;
import org.riverock.commerce.tools.ContentTypeTools;
import org.riverock.common.tools.NumberTools;
import org.riverock.common.tools.ServletTools;
import org.riverock.common.tools.StringTools;
import org.riverock.interfaces.portal.bean.User;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.ContainerConstants;
import org.riverock.common.utils.PortletUtils;

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
    private static final String COMPLETE_SEND_ORDER = "complete-send-order";

    public InvoicePortlet() {
    }

    private PortletConfig portletConfig = null;

    public void init( PortletConfig portletConfig ) {
        this.portletConfig = portletConfig;
    }

    public void destroy() {
    }

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) {
        String action = PortletUtils.getString(actionRequest, "action", null);

        if ( log.isDebugEnabled() ) {
            log.debug( "Action: " + action );
        }

        if ( action== null ) {
            return;
        }

        PortletSession session = actionRequest.getPortletSession();
        Long userOrderId = (Long)session.getAttribute( ShopPortlet.USER_ORDER_ID, PortletSession.APPLICATION_SCOPE );

        if ( userOrderId == null ) {
            return;
        }

        Long siteId = new Long( actionRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );

        if ( action.equals( "set" ) ) {
            Long shopItemId = PortletUtils.getLong( actionRequest, "set_id_item" );
            int count = PortletUtils.getInt( actionRequest, ShopPortlet.NAME_INVOICE_NEW_COUNT_PARAM, 0 );

            if ( log.isDebugEnabled() ) {
                log.debug( "shopItemId - " + shopItemId );
                log.debug( "count - " + count );
            }

            // set new quantity of item in backet
            CommerceDaoFactory.getOrderDao().setNewQuantity(siteId, userOrderId, shopItemId, count );

        } else if ( action.equals( "del" ) ) {
            Long shopItemId = PortletUtils.getLong( actionRequest, "del_id_item" );
            if ( log.isDebugEnabled() ) {
                log.debug( "id_item - " + shopItemId );
            }
            // delete item from basket
            CommerceDaoFactory.getOrderDao().deleteShopItem(siteId, userOrderId, shopItemId );
        }
        else if ( "send".equals( action )) {
            AuthSession authSession = (AuthSession)actionRequest.getUserPrincipal();
            if (authSession!=null) {
                ResourceBundle bundle = portletConfig.getResourceBundle( actionRequest.getLocale() );
                // send userOrderId
                sendInvoice(bundle, authSession, userOrderId, actionResponse);
            }
        }
    }

    public void render( RenderRequest renderRequest, RenderResponse renderResponse ) throws PortletException {

        PrintWriter out = null;
        try {
            Shop shop = OrderLogic.prepareCurrenctRequest( renderRequest );
            ResourceBundle bundle = portletConfig.getResourceBundle( renderRequest.getLocale() );

            ContentTypeTools.setContentType( renderResponse, ContentTypeTools.CONTENT_TYPE_UTF8 );
            out = renderResponse.getWriter();

            PortletSession session = renderRequest.getPortletSession();
            Long userOrderId = (Long)session.getAttribute( ShopPortlet.USER_ORDER_ID, PortletSession.APPLICATION_SCOPE );

            if ( userOrderId == null ) {
                return;
            }

            AuthSession authSession = (AuthSession)renderRequest.getUserPrincipal();
            int shopGroupId = PortletUtils.getInt( renderRequest, ShopPortlet.NAME_ID_GROUP_SHOP, 0 );

            if ( authSession != null ) {
                if ( log.isDebugEnabled() ) {
                    log.debug( "AuthSession is null. Try get from session" );
                    log.debug( "AuthSession in servlet session - " + authSession );
                }

                if ( authSession != null && log.isDebugEnabled() )
                    log.debug( "AuthSession not null. getLoginStatus() - " + authSession.checkAccess( renderRequest.getServerName() ) );

                if ( ( authSession != null ) && ( authSession.checkAccess( renderRequest.getServerName() ) ) ) {
                    OrderLogic.updateAuthSession(userOrderId, authSession );
                }
            }

            Long currencyID = PortletUtils.getLong( renderRequest, ShopPortlet.NAME_ID_CURRENCY_SHOP );

            String addForm =
                ServletTools.getHiddenItem( ContainerConstants.NAME_TYPE_CONTEXT_PARAM, CTX_TYPE_INVOICE ) +
                ServletTools.getHiddenItem( ShopPortlet.NAME_ID_CURRENCY_SHOP, currencyID ) +
                ServletTools.getHiddenItem( ShopPortlet.NAME_ID_GROUP_SHOP,
                    PortletUtils.getInt( renderRequest, ShopPortlet.NAME_ID_GROUP_SHOP, 0 ) ) +
                ServletTools.getHiddenItem( ShopPortlet.NAME_ID_SHOP_PARAM, shop.getShopId() );

            String statusSend = PortletUtils.getString(renderRequest, COMPLETE_SEND_ORDER, "");
            if ("true".equalsIgnoreCase(statusSend)) {

                PortletURL url = renderResponse.createRenderURL();
                url.setParameter(ContainerConstants.NAME_TYPE_CONTEXT_PARAM, ShopPortlet.CTX_TYPE_SHOP);
                addParameters(url, currencyID, shopGroupId, shop.getShopId());

                String shopUrl = "<a href=\"" +url.toString()+ "\">";

                String str = null;
                Object args1[] = {url.toString()};

                final String sendOrderName = "invoice.order-send-complete";
                String s = bundle.getString( sendOrderName );
                if ( s != null ) {
                    str = MessageFormat.format( s, args1 );
                } else {
                    str = "Ваш заказ N" + userOrderId + " успешно отослан. " + shopUrl + "Продолжить</a>";
                }

                args1 = null;
                out.write( str );
                session.removeAttribute( ShopPortlet.USER_ORDER_ID, PortletSession.APPLICATION_SCOPE );
                return;
            }

/*
            if ( order.getAuthSession() == null ) {

                String backURL = StringTools.rewriteURL( PortletUtils.url( "mill.invoice", renderRequest, renderResponse ) );

                out.write( "<table cellspacing=\"2\" cellpadding=\"2\">\n" );
                out.write( "<tr>\n" );
                out.write( "<td class=\"pricedata\" rowspan=\"2\">\n" );
                out.write( bundle.getString( "invoice.attention" ) );
                out.write( "<br>\n" );

                if ( Boolean.TRUE.equals( isRegisterAllowed ) ) {

                    out.write( bundle.getString( "invoice.register" ) );
                    out.write( "\n" );
                    out.write( "<a href=\"" );
                    out.write( PortletUtils.url( "mill.register", renderRequest, renderResponse ) + '&' +
                        LoginUtils.NAME_TOURL_PARAM + '=' + backURL );
                    out.write( "\">" );
                    out.write( bundle.getString( "button.next" ) );
                    out.write( " " );
                    out.write( "</a>\n" );

                }
                out.write( "</td>\n" +
                    "<form method=\"POST\" action=\"" + PortletUtils.ctx( renderRequest ) + "\">\n" +
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
            PortletURL url = renderResponse.createRenderURL();
            url.setParameter(ContainerConstants.NAME_TYPE_CONTEXT_PARAM, ShopPortlet.CTX_TYPE_SHOP);
            addParameters(url, currencyID, shopGroupId, shop.getShopId());

            out.write( "<a href=\"" + url.toString() + "\">" );

            out.write( bundle.getString( "invoice.continue_select" ) );
            out.write( "</a>" );
            out.write( "</td>\n</tr>\n</table>\n<br>" );
            out.write( bundle.getString( "invoice.your_select" ) );

            out.write( "<table border=\"0\" cellpadding=\"2px\" cellspacing=\"2px\">\n" );
            List<ShopOrder> shopOrders = CommerceDaoFactory.getOrderDao().getShopOrders(userOrderId);
            for (ShopOrder shopOrder : shopOrders) {
                if ( shopOrder.getShopOrderItems().size()>0 ) {
                    Shop shopBean = CommerceDaoFactory.getShopDao().getShop(shopOrder.getShopId());
                    out.write( "<tr>\n<td colspan=\"6\" align=\"left\" border=\"0\">\n" );
                    out.write( shopBean.getShopNameForPriceList() );
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
                    BigDecimal orderSumm = new BigDecimal(0);
                    int currentPrecision = 0;
                    boolean isFirst=true;
                    for (ShopOrderItem item : shopOrder.getShopOrderItems()) {
                        if ( isFirst ) {
                            isFirst=false;
                            currentCurrency = item.getResultCurrencyName();
                            currentPrecision = item.getPrecisionResult();
                        }

                        BigDecimal itemFullPrice =
                            NumberTools.multiply(
                                item.getResultPrice(), item.getCountItem(), item.getPrecisionResult()
                            );

                        orderSumm = orderSumm.add(itemFullPrice);

                        out.write( "<tr>\n" );
                        out.write( "<td class=\"priceData\">" );
                        out.write( item.getShopItemName() );
                        out.write( "</td>\n" );
                        out.write( "<form method=\"GET\" action=\"" + PortletUtils.ctx( renderRequest ) + "\">\n" );
                        out.write( addForm );
                        out.write( ServletTools.getHiddenItem( "set_id_item", item.getShopItemId() ) );
                        out.write( ServletTools.getHiddenItem( "action", "set" ) );
                        out.write( "<td class=\"priceData\" align=\"right\">" );
                        out.write( NumberTools.truncate( item.getResultPrice(), currentPrecision ).toString() );
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
                        out.write( NumberTools.truncate( itemFullPrice, currentPrecision ).toString() );
                        out.write( "</td>\n" );
                        out.write( "<td class=\"priceData\" align=\"center\">" );
                        out.write( item.getResultCurrencyName() );
                        out.write( "</td>\n" );
                        out.write( "</form>\n" );
                        out.write( "<form method=\"GET\" action=\"" + PortletUtils.ctx( renderRequest ) + "\">\n" );
                        out.write( addForm );
                        out.write( ServletTools.getHiddenItem( "del_id_item", item.getShopItemId() ) );
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

                    out.write( " " + NumberTools.truncate( orderSumm, currentPrecision )+ " " + currentCurrency );
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

/*
            // button for send invoice
            if ( !order.getShopOrders().isEmpty() && authSession!=null &&
                 !StringUtils.isBlank( orderEmail ) && isActivateEmailOrder ) {

                out.write( "<br>\n" );
                out.write( "<form method=\"GET\" action=\"" + PortletUtils.ctx( renderRequest ) + "\">\n" );
                out.write( addForm );
                out.write( ServletTools.getHiddenItem( "action", "send" ) );
                out.write( "<input type=\"submit\" value=\"" );
                out.write( bundle.getString( "invoice.send_button" ) );

                out.write( "\">\n" );
                out.write( "</form>" );

            }
*/
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

    private void addParameters(PortletURL url, Long currencyId, int shopGroupId, Long shopId) {
        url.setParameter(ShopPortlet.NAME_ID_CURRENCY_SHOP , ""+currencyId);
        url.setParameter(ShopPortlet.NAME_ID_GROUP_SHOP,  ""+shopGroupId);
        url.setParameter(ShopPortlet.NAME_ID_SHOP_PARAM , shopId.toString());
    }

    private void sendInvoice(ResourceBundle bundle, AuthSession authSession, Long userOrderId, ActionResponse actionResponse) {

        // Todo rewrite from constans
        final boolean isActivateEmailOrder = false;

        String s = bundle.getString( "reg.send_order.1" );
        String orderCustomString = MessageFormat.format( s, "" + userOrderId);

        User ui = authSession.getUser();
        String orderAdminString =
            "Заказчик: " + StringTools.getUserName(ui.getMiddleName(), ui.getFirstName(), ui.getLastName()) +
             "\n\n" + orderCustomString;

        List<ShopOrder> shopOrders = CommerceDaoFactory.getOrderDao().getShopOrders(userOrderId);

        for (ShopOrder shopOrder : shopOrders) {
            orderCustomString = processUserEmail(shopOrder, bundle, orderCustomString);

            if ( log.isDebugEnabled() ) {
                log.debug( "Your order N" + userOrderId + "\n" + orderCustomString );
            }

            if ( Boolean.TRUE.equals( isActivateEmailOrder ) ) {
                orderAdminString = processAdminEmail(shopOrder, bundle, orderAdminString);
            }
        }

        if ( log.isDebugEnabled() ) {
            log.debug( "send admin order: your order N" + userOrderId + "\n" + orderAdminString );
        }

        actionResponse.setRenderParameter(COMPLETE_SEND_ORDER, "true");

        throw new IllegalStateException("Need switch to user Portal mail service");
/*
                MailMessage.sendMessage( orderCustomString,
                    order.getAuthSession().getUserInfo().getEmail(),
                    orderEmail,
                    "Your order N" + order.getUserOrderId(),
                    GenericConfig.getMailSMTPHost() );


                if ( Boolean.TRUE.equals( isActivateEmailOrder ) ) {
                    MailMessage.sendMessage( orderAdminString,
                        orderEmail,
                        orderEmail,
                        "Order N" + order.getUserOrderId(),
                        GenericConfig.getMailSMTPHost() );
                }
*/

    }

    private String processUserEmail(ShopOrder shopOrder, ResourceBundle bundle, String orderCustomString) {
        String s;
        if ( shopOrder.getShopOrderItems().size()>0 ) {
            ShopOrderItem itemTemp = shopOrder.getShopOrderItems().get(0);
            Shop shopBean = CommerceDaoFactory.getShopDao().getShop(shopOrder.getShopId());

            s = bundle.getString( "reg.send_order.shop-header" );
            orderCustomString +=
                MessageFormat.format( s,
                    "" + shopOrder.getShopId(),
                    shopBean.getShopName(),
                    shopBean.getShopCode(),
                    itemTemp.getResultCurrencyName());

            int idx=1;
            for (ShopOrderItem item : shopOrder.getShopOrderItems()) {
                BigDecimal itemFullPrice =
                    NumberTools.multiply(
                        item.getResultPrice(), item.getCountItem(), item.getPrecisionResult()
                    );

                s = bundle.getString( "reg.send_order.2" );
                orderCustomString +=
                    MessageFormat.format( s,
                        "" + idx++,
                        item.getShopItemName(),
                        NumberTools.truncate( item.getResultPrice(), item.getPrecisionResult() ),
                        item.getResultCurrencyName(),
                        "" + item.getCountItem(),
                        NumberTools.truncate( itemFullPrice, item.getPrecisionResult() ),
                        "",
                        "" + shopOrder.getShopId(),
                        "" + item.getShopItemId()
                    );
            }
        }
        return orderCustomString;
    }

    private String processAdminEmail(ShopOrder shopOrder, ResourceBundle bundle, String orderAdminString) {
        String s;
        ShopOrderItem itemTemp = shopOrder.getShopOrderItems().get(0);
        Shop shopBean = CommerceDaoFactory.getShopDao().getShop(shopOrder.getShopId());

        s = bundle.getString( "reg.send_order.shop-header" );
        orderAdminString +=
            MessageFormat.format( s,
                "" + shopOrder.getShopId(),
                shopBean.getShopName(),
                shopBean.getShopCode(),
                itemTemp.getResultCurrencyName());

        BigDecimal orderSumm = new BigDecimal(0);
        boolean isFirst=true;
        int index=0;
        String currentCurrency = "";
        int currentPrecision = 0;
        for (ShopOrderItem item : shopOrder.getShopOrderItems()) {
            index++;
            if ( isFirst ) {
                isFirst=false;
                currentCurrency = item.getResultCurrencyName();
                if ( item.getPrecisionResult() != null ) {
                    currentPrecision = item.getPrecisionResult();
                }
            }

            BigDecimal itemFullPrice =
                NumberTools.multiply(
                    item.getResultPrice(), item.getCountItem(), item.getPrecisionResult()
                );

            orderSumm = orderSumm.add(itemFullPrice);

            s = bundle.getString( "reg.send_order.2" );
            orderAdminString +=
                MessageFormat.format( s,
                    "" + index,
                    item.getShopItemName(),
                    NumberTools.truncate( item.getResultPrice(), currentPrecision ),
                    item.getResultCurrencyName(),
                    "" + item.getCountItem(),
                    NumberTools.truncate( itemFullPrice, currentPrecision ),
                    "",
                    shopOrder.getShopId(),
                    item.getShopItemId());
        }
        orderAdminString +=
            "Total summ of invoice: " +
                NumberTools.truncate( NumberTools.truncate( orderSumm, currentPrecision ), currentPrecision ) +
                " " + currentCurrency + "\n\n";
        return orderAdminString;
    }
}
