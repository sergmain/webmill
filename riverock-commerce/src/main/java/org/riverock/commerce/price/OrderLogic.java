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
package org.riverock.commerce.price;

import java.math.BigDecimal;
import java.util.Date;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import org.riverock.commerce.bean.*;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.tools.HibernateUtils;
import org.riverock.common.tools.NumberTools;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.container.tools.PortletService;

/**
 * Author: mill
 * Date: Dec 11, 2002
 * Time: 8:28:58 AM
 * <p/>
 * $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public final class OrderLogic {
    private final static Logger log = Logger.getLogger( OrderLogic.class );

    public static Shop prepareCurrenctRequest( final RenderRequest renderRequest ) throws PortletException {
        PortletSession session = renderRequest.getPortletSession( true );
        Long shopId = PortletService.getLong( renderRequest, ShopPortlet.NAME_ID_SHOP_PARAM );
        AuthSession authSession = (AuthSession)renderRequest.getUserPrincipal();
        Shop shop = prepareCurrentShop(shopId, session);
        prepareInvoice(shop, session, renderRequest, authSession);
        return shop;
    }

    private static void prepareInvoice(Shop shop, PortletSession session, RenderRequest renderRequest, AuthSession authSession) {
        Invoice order = null;
        // если текущий магаз определен, то ищем в сессии заказ, связанный с этим магазом.
        // если заказа в сессии нет, то создаем
        if( shop != null && shop.getShopId() != null ) {
            order = (Invoice) getFromSession(session, ShopPortlet.ORDER_SESSION);

            if( log.isDebugEnabled() ) {
                log.debug( "order object - " + order );
            }

            if( order == null ) {
                if( log.isDebugEnabled() ) {
                    log.debug( "Create new order" );
                }

                ShopOrder shopOrder = new ShopOrder();
                shopOrder.setShopId( shop.getShopId() );

                order = new Invoice();
                order.setUserOrderId( initAuthSession(( AuthSession ) renderRequest.getUserPrincipal() ) );
            }

            // если заказ создан ранее и юзер прошел авторизацию,
            // помещаем авторизационные данные в заказ
            if ( order != null && authSession != null ) {
                if( authSession.checkAccess( renderRequest.getServerName() ) ) {
                    if( log.isDebugEnabled() ) {
                        log.debug( "updateAuthSession" );
                    }

                    updateAuthSession(order, authSession );
                }
            }

            Long id_item = PortletService.getLong( renderRequest, ShopPortlet.NAME_ADD_ID_ITEM );
            int count = PortletService.getInt( renderRequest, ShopPortlet.NAME_COUNT_ADD_ITEM_SHOP, 0 );

            // если при вызове было указано какое либо количество определенного наименования,
            // то помещаем это наименование в заказ
            if( log.isDebugEnabled() )
                log.debug( "set new count of item. id_item - " + id_item + " count - " + count );

            removeFromSession(session, ShopPortlet.ORDER_SESSION);
            setInSession(session, ShopPortlet.ORDER_SESSION, order );
        }
    }

    private static Shop prepareCurrentShop(Long shopId, PortletSession session) {
        if( log.isDebugEnabled() ) {
            if( shopId != null )
                log.debug( "shopId " + shopId);
            else
                log.debug( "shopId is null" );
        }

        // get current shop from session
        Long currentShopId = (Long)getFromSession(session, ShopPortlet.CURRENT_SHOP_ID);
        Shop currentShop = CommerceDaoFactory.getShopDao().getShop(currentShopId);

        if( log.isDebugEnabled() ) {
            log.debug( "currentShop " + currentShop);
            if( currentShop != null ) {
                log.debug( "currentShop.shopId - " + currentShop.getShopId() );
            }
        }

        Shop shop=null;
        // if in the session not exist a current shop and queried concrete shop,
        // then create this shop and put it in session
        if( currentShop == null && shopId != null ) {
            if( log.isDebugEnabled() ) {
                log.debug( "currentShop is null and shopId is not null " );
            }

            shop = CommerceDaoFactory.getShopDao().getShop(shopId);
        }
        // если в сессии есть текущий магазин и
        // код вызванного магазина совпадает с кодом мкгаза в сессии,
        // юзаем его (тот, который в сессии)
        else {
            if ( currentShop != null && currentShop.getShopId().equals(shopId)) {
                if( log.isDebugEnabled() ) {
                    log.debug( "currentShop is not null and currentShop.shopId == shopId " );
                }
                shop = currentShop;
            }
            // если в сессии есть текущий магазин и
            // код вызванного магазина не совпадает с кодом магаза в сессии,
            // заменяем магаз в сессии на магаз с вызываемым кодом
            else {
                if( currentShop!=null && !currentShop.getShopId().equals(shopId) ) {
                    if( log.isDebugEnabled() ) {
                        log.debug( "#11.22.09 create shop instance with shopId - " + shopId);
                    }

                    shop = CommerceDaoFactory.getShopDao().getShop(shopId);
                }
            }
        }
        if (shop!=null) {
            setInSession(session, ShopPortlet.CURRENT_SHOP_ID, shop.getShopId());
        }
        else {
            removeFromSession(session, ShopPortlet.CURRENT_SHOP_ID);
        }

        // теперь в shop находится текущий магаз ( тот который в сессии )
        // если его создание прошло успешно - магаз с вызываемым кодом действительно есть,
        // иначе shop == null

        if( log.isDebugEnabled() ) {
            log.debug( "Current shop: " + shop);
            if( shop != null )
            {
                log.debug( "Current shop.shopId " + shop.getShopId() );
            }
        }
        return shop;
    }

    private static void removeFromSession(PortletSession session, String key) {
        session.removeAttribute( key, PortletSession.APPLICATION_SCOPE );
    }

    private static void setInSession(PortletSession session, String key, Object value) {
        session.setAttribute( key, value, PortletSession.APPLICATION_SCOPE );
    }

    private static Object getFromSession(PortletSession session, String key) {
        return session.getAttribute( key, PortletSession.APPLICATION_SCOPE );
    }

    private static Long initAuthSession(final AuthSession authSession) {
        Long userId = authSession != null && authSession.getUser() != null ? authSession.getUser().getUserId() : null;
        return CommerceDaoFactory.getOrderDao().createUserOrder(userId, new Date());
    }

    public static void updateAuthSession(final Invoice order, final AuthSession authSession) {
        Session hibernateSession = HibernateUtils.getSession();
        hibernateSession.beginTransaction();

        if( authSession != null ) {
            Long userId = authSession.getUser().getUserId();
            if( userId != null ) {
                CommerceDaoFactory.getOrderDao().bindUserToUserOrder(hibernateSession, order.getUserOrderId(), userId);
            }
            else {
                throw new IllegalStateException("userId not found for user login: " +authSession.getUserLogin());
            }
        }
        hibernateSession.getTransaction().commit();
    }

/*
    public static void addItem(Session hibernateSession, ShopOrderItem item) throws Exception {
        if( item == null ) {
            throw new Exception( "Error add item to order. Item is null" );
        }

        if( log.isDebugEnabled() )
            log.debug( "Add new count of item. id_item - " + item.getShopItem().getShopItemId() + " count - " + item.getCountItem() );

        hibernateSession.save(item);

        PreparedStatement ps = null;

            String sql_ = null;
            sql_ =
                "insert into WM_PRICE_ORDER " +
                "(ID_PRICE_ORDER_V2, ID_ORDER_V2, ID_ITEM, COUNT, ITEM, PRICE, " +
                "CURRENCY, PRICE_RESULT, CODE_CURRENCY_RESULT, NAME_CURRENCY_RESULT," +
                "PRECISION_CURRENCY_RESULT )" +
                "values " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

            if( log.isDebugEnabled() ) {
                log.debug( "insert new item to order" );
            }

            RsetTools.setLong( ps, 1, seqValue );
            RsetTools.setLong( ps, 2, idOrder );
            RsetTools.setLong( ps, 3, item.getShopItem().getShopItemId() );
            RsetTools.setInt( ps, 4, item.getCountItem() );
            ps.setString( 5, item.getShopItem().getItem() );
            RsetTools.setBigDecimal( ps, 6, item.getShopItem().getPrice() );
            ps.setString( 7, item.getCurrencyItem().getCurrencyCode() );
            RsetTools.setBigDecimal( ps, 8, item.getResultPrice() );
            ps.setString( 9, item.getResultCurrency().getCurrencyCode() );
            ps.setString( 10, item.getResultCurrency().getCurrencyName() );
            RsetTools.setInt( ps, 11, item.getPrecisionResult() );

            int update = ps.executeUpdate();

            if( log.isDebugEnabled() ) {
                log.debug( "count of inserted record - " + update );
            }
    }

*/
    public static ShopOrderItem initOrderItem(Long shopItemId, long siteId, Long userOrderId) throws Exception {
        try {
            ShopItem shopItem = CommerceDaoFactory.getShopDao().getShopItem(shopItemId);
            if (shopItem!=null) {
                ShopOrderItem item = new ShopOrderItem();
                item.setShopItem(shopItem);
                item.setUserOrderId(userOrderId);
                
                final CurrencyManager currencyManager = CurrencyManager.getInstance( siteId );

                CurrencyItem currencyItem = CurrencyService.getCurrencyItemByCode( currencyManager.getCurrencyList(), item.getShopItem().getCurrency() );
                currencyItem.fillRealCurrencyData( currencyManager.getCurrencyList().getStandardCurrencies() );

                item.setCurrencyItem( currencyItem );

                Shop shop = CommerceDaoFactory.getShopDao().getShop(item.getShopItem().getShopId());

                if( log.isDebugEnabled() ) {
                    log.debug( "currencyCode " + item.getShopItem().getCurrency() );
                    log.debug( "currencyItem " + currencyItem );
                    log.debug( "item.price " + item.getShopItem().getPrice() );
                    if( currencyItem != null ) {
                        log.debug( "currencyItem.isRealInit " + currencyItem.isRealInit() );
                        log.debug( "currencyItem.getRealCurs " + currencyItem.getRealCurs() );
                    }
                }

                if( log.isDebugEnabled() )
                    log.debug( "new price will be calculated - " + ( currencyItem != null && currencyItem.isRealInit() ) );

                if( currencyItem != null && currencyItem.isRealInit() ) {

                    BigDecimal resultPrice = new BigDecimal(0);

                    if( log.isDebugEnabled() ) {
                        log.debug( "item idShop - " + item.getShopItem().getShopId() );
                        log.debug( "shop idShop - " + shop.getShopId() );
                        log.debug( "item idCurrency - " + item.getCurrencyItem().getCurrencyId() );
                        log.debug( "shop idOrderCurrency - " + shop.getInvoiceCurrencyId() );
                        log.debug( "код валюты наименования совпадает с валютой в которой выводить заказ - " +
                            ( item.getCurrencyItem().getCurrencyId() == shop.getInvoiceCurrencyId() ) );
                    }

                    // если код валюты наименования совпадает с валютой в которой выводить заказ
                    int precisionValue = 2;
                    CurrencyPrecision precision = null;

                    CurrencyPrecisionList currencyPrecisionList = new CurrencyPrecisionList(shop.getShopId());
                    if( item.getCurrencyItem().getCurrencyId().equals( shop.getInvoiceCurrencyId() ) ) {
                        item.setResultCurrency( item.getCurrencyItem() );
                        precision = getPrecisionValue( currencyPrecisionList, currencyItem.getCurrencyId() );
                        if( precision != null && precision.getPrecision() != null ) {
                            precisionValue = precision.getPrecision();
                        }

                        if( item.getShopItem().getPrice() != null )
                            resultPrice = NumberTools.truncate( item.getShopItem().getPrice(), precisionValue );
                        else {
                            if( log.isDebugEnabled() ) {
                                log.info( "price is null" );
                            }
                        }
                    }
                    else {
                        precision = getPrecisionValue( currencyPrecisionList, shop.getInvoiceCurrencyId() );
                        if( precision != null && precision.getPrecision() != null )
                            precisionValue = precision.getPrecision();

                        CurrencyItem defaultCurrency =
                            CurrencyService.getCurrencyItem( currencyManager.getCurrencyList(), shop.getInvoiceCurrencyId() );

                        item.setResultCurrency( defaultCurrency );

                        if( log.isDebugEnabled() ) {
                            log.debug("currency: " + item.getCurrencyItem());
                            log.debug("default currency: " + defaultCurrency);
                        }
                        BigDecimal crossCurs = item.getCurrencyItem().getRealCurs().divide(defaultCurrency.getRealCurs());

                        if( item.getShopItem().getPrice() != null ) {
                            resultPrice =
                                NumberTools.truncate( item.getShopItem().getPrice(), precisionValue )
                                .multiply( crossCurs );

                            if( log.isDebugEnabled() ) {
                                log.debug( "crossCurs - " + crossCurs + " price - " +
                                    NumberTools.truncate( item.getShopItem().getPrice(), precisionValue ) +
                                    " result price - " + resultPrice );
                            }
                        }
                        else {
                            if( log.isDebugEnabled() ) {
                                log.info( "price is null" );
                            }
                        }
                    }
                    item.setResultPrice( NumberTools.truncate( resultPrice, precisionValue ) );
                    item.setPrecisionResult( precisionValue );
                }
                else {
                    boolean isReal = false;
                    if( currencyItem != null )
                        isReal = Boolean.TRUE.equals( currencyItem.isRealInit() );

                    throw new PriceException( "Price for item can not calculated. CurrencyItem is " +
                        ( currencyItem == null ? "" : "not " ) + "null, is real curs init - " + isReal );
                }
                return item;
            }
            return null;
        }
        catch( Exception e ) {
            log.error( "error init item", e );
            throw e;
        }
    }

    private static CurrencyPrecision getPrecisionValue( CurrencyPrecisionList precList, Long idCurrency ) {
        CurrencyPrecision prec;
        prec = precList.getCurrencyPrecision( idCurrency );
        if( prec == null ) {
            if( log.isDebugEnabled() ) {
                log.info( "Precison not found for currencyId " + idCurrency );
            }
            return null;
        }
        return prec;
    }
}