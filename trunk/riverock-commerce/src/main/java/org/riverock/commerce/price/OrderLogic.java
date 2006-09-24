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
package org.riverock.commerce.price;

import java.io.File;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;

import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.NumberTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.portlet.core.GetWmPriceListItem;
import org.riverock.portlet.schema.price.CurrencyPrecisionType;
import org.riverock.portlet.schema.price.CustomCurrencyItemType;
import org.riverock.portlet.schema.price.OrderItemType;
import org.riverock.portlet.schema.price.OrderType;
import org.riverock.portlet.schema.price.ShopOrderType;
import org.riverock.commerce.shop.bean.ShopOrder;
import org.riverock.commerce.tools.SiteUtils;
import org.riverock.commerce.bean.ShopBean;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.webmill.container.ContainerConstants;
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

    public OrderLogic() {
    }

    public static void process( final RenderRequest renderRequest ) throws PortletException {
        DatabaseAdapter dbDyn = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();

            PortletSession session = renderRequest.getPortletSession( true );
            Long idShop = PortletService.getLong( renderRequest, ShopPortlet.NAME_ID_SHOP_PARAM );
            Long siteId = new Long( renderRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );


            if( log.isDebugEnabled() ) {
                if( idShop != null )
                    log.debug( "idShop " + idShop );
                else
                    log.debug( "idShop is null" );
            }

            // get current shop from session
            Object fromSession = getFromSession(session, ShopPortlet.CURRENT_SHOP);
            if (log.isDebugEnabled()) {
                log.debug("fromSession: " + fromSession);
                if (fromSession!=null) {
                    log.debug("fromSession class name: " + fromSession.getClass().getName());
                }
            }
            ShopBean tempShop = ( ShopBean ) fromSession;

            if( log.isDebugEnabled() ) {
                log.debug( "tempShop " + tempShop );
                if( tempShop != null )
                {
                    log.debug( "tempShop.idShop - " + tempShop.getShopId() );
                }
            }

            ShopBean shopBean = null;
            // если в сессии текущего магазина нет, но вызван какой-то конкретный магазин
            // создаем новый магазин и помещаем в сессию
            if( tempShop == null && idShop != null ) {
                if( log.isDebugEnabled() )
                    log.debug( "tempShop is null and idShop is not null " );

                shopBean = CommerceDaoFactory.getShopDao().getShop(idShop);
                setInSession(session, ShopPortlet.CURRENT_SHOP, shopBean);
            }
            // если в сессии есть текущий магазин и
            // код вызванного магазина совпадает с кодом мкгаза в сессии,
            // юзаем его (тот, который в сессии)
            else {
                if( tempShop != null &&
                    ( idShop == null || idShop.equals( tempShop.getShopId() ) ) ) {
                    if( log.isDebugEnabled() )
                        log.debug( "tempShop is not null and tempShop.idShop == idShop " );

                    shopBean = tempShop;
                }
// если в сессии есть текущий магазин и
// код вызванного магазина не совпадает с кодом магаза в сессии,
// заменяем магаз в сессии на магаз с вызываемым кодом
                else {
                    if( tempShop != null && idShop != null && !idShop.equals( tempShop.getShopId() ) ) {
                        if( log.isDebugEnabled() ) {
                            log.debug( "#11.22.09 create shop instance with idShop - " + idShop );
                        }

                        shopBean = CommerceDaoFactory.getShopDao().getShop(idShop);

                        if( log.isDebugEnabled() )
                        {
                            log.debug( "idShop of created shop - " + shopBean.getShopId() );
                        }

                        removeFromSession(session, ShopPortlet.CURRENT_SHOP);
                        setInSession(session, ShopPortlet.CURRENT_SHOP, shopBean);
                    }
                }
            }
            // теперь в shop находится текущий магаз ( тот который в сессии )
            // если его создание прошло успешно - магаз с вызываемым кодом действительно есть,
            // иначе shop == null

            if( log.isDebugEnabled() ) {
                log.debug( "shop object " + shopBean );
                if( shopBean != null )
                {
                    log.debug( "shop.id_shop " + shopBean.getShopId() );
                }
            }

            ShopOrder order = null;
            // если текущий магаз определен, то ищем в сессии заказ, связанный с этим магазом.
            // если заказа в сессии нет, то создаем
            if( shopBean != null && shopBean.getShopId() != null ) {
                order = ( ShopOrder ) getFromSession(session, ShopPortlet.ORDER_SESSION);

                if( log.isDebugEnabled() )
                    log.debug( "order object - " + order );

                if( order == null ) {
                    if( log.isDebugEnabled() )
                        log.debug( "Create new order" );

                    order = new ShopOrder();
                    order.setServerName( renderRequest.getServerName() );

                    ShopOrderType shopOrder = new ShopOrderType();
                    shopOrder.setIdShop( shopBean.getShopId() );

                    order.addShopOrdertList( shopOrder );
                    initAuthSession( dbDyn, order, ( AuthSession ) renderRequest.getUserPrincipal() );
                }

                // если заказ создан ранее и юзер прошел авторизацию,
                // помещаем авторизационные данные в заказ
                if( ( order != null ) && ( order.getAuthSession() == null ) ) {
                    AuthSession authSession = ( AuthSession ) renderRequest.getUserPrincipal();
                    if( ( authSession != null ) && ( authSession.checkAccess( renderRequest.getServerName() ) ) ) {
                        if( log.isDebugEnabled() )
                            log.debug( "updateAuthSession" );

                        updateAuthSession( dbDyn, order, authSession );
                    }
                }

                Long id_item = PortletService.getLong( renderRequest, ShopPortlet.NAME_ADD_ID_ITEM );
                int count = PortletService.getInt( renderRequest, ShopPortlet.NAME_COUNT_ADD_ITEM_SHOP, 0 );

                // если при вызове было указано какое либо количество определенного наименования,
                // то помещаем это наименование в заказ
                if( log.isDebugEnabled() )
                    log.debug( "set new count of item. id_item - " + id_item + " count - " + count );

                if( ( id_item != null ) && ( count > 0 ) ) {
                    if( log.isDebugEnabled() ) {
                        log.debug( "add item to order" );
                        log.debug( "id_order " + order.getIdOrder() );
                        log.debug( "id_item " + id_item );
                        log.debug( "count " + count );
                    }

                    addItem( dbDyn, order, id_item, count, siteId );
                }
                removeFromSession(session, ShopPortlet.ORDER_SESSION);
                setInSession(session, ShopPortlet.ORDER_SESSION, order );
            }

            dbDyn.commit();

        }
        catch( Exception e ) {
            try {
                if (dbDyn!=null) {
                    dbDyn.rollback();
                }
            }
            catch( Exception e1 ) {
                // catch rollback error
            }

            final String es = "Error processing OrderLogic";
            log.error( es, e );
            throw new PortletException( es, e );
        }
        finally {
            DatabaseManager.close( dbDyn );
            dbDyn = null;
        }
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

    public static void initAuthSession( final DatabaseAdapter dbDyn, final OrderType order, final AuthSession authSession )
        throws Exception {
        String sql_ = "";
        PreparedStatement ps = null;
        try {
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "SEQ_WM_PRICE_RELATE_USER_ORDER" );
            seq.setTableName( "WM_PRICE_RELATE_USER_ORDER" );
            seq.setColumnName( "ID_ORDER_V2" );

            order.setIdOrder( dbDyn.getSequenceNextValue( seq ) );

            sql_ =
                "insert into WM_PRICE_RELATE_USER_ORDER " +
                "(ID_ORDER_V2, DATE_CREATE, ID_USER)" +
                "values " +
                "(?,  " + dbDyn.getNameDateBind() + ", ? )";

            ps = dbDyn.prepareStatement( sql_ );

            RsetTools.setLong( ps, 1, order.getIdOrder() );
            dbDyn.bindDate( ps, 2, DateTools.getCurrentTime() );

            if( authSession != null && authSession.getUserInfo() != null ) {
                RsetTools.setLong( ps, 3, authSession.getUserInfo().getUserId() );
            }
            else
                ps.setNull( 3, Types.NUMERIC );

            int i = ps.executeUpdate();

            if( log.isDebugEnabled() )
                log.debug( "count of inserted record - " + i );
        }
        catch( Exception e1 ) {
            log.error( "order.getIdOrder() " + order.getIdOrder() );
            log.error( "authSession " + authSession );
            if( authSession != null && authSession.getUserInfo() != null )
                log.error( "authSession.getUserInfo().getIdUser() " + authSession.getUserInfo().getUserId() );

            log.error( "Error init AuthSession", e1 );
            throw e1;
        }
        finally {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public static void updateAuthSession( final DatabaseAdapter dbDyn, final OrderType order, final AuthSession authSession )
        throws Exception {
        String sql_ = "";
        PreparedStatement ps = null;
        try {
            if( authSession != null ) {
                Long userId = authSession.getUserInfo().getUserId();
                if( userId != null ) {
                    sql_ =
                        "update WM_PRICE_RELATE_USER_ORDER " +
                            "set    ID_USER = ? " +
                            "where  ID_ORDER_V2 = ? ";

                    ps = dbDyn.prepareStatement( sql_ );

                    RsetTools.setLong( ps, 1, userId );
                    RsetTools.setLong( ps, 2, order.getIdOrder() );
                }
                else {
                    throw new IllegalStateException("userId not found for user login: " +authSession.getUserLogin());
                }

                int i = ps.executeUpdate();

                if( log.isDebugEnabled() )
                    log.debug( "count of updated record - " + i );

            }
        }
        catch( Exception e1 ) {
            final String es = "Error update authSession";
            log.error( es, e1 );
            throw new PriceException( es, e1 );
        }
        finally {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public static void removeOrder( final DatabaseAdapter dbDyn, final OrderType order )
        throws Exception {

        String sql_ =
            "delete from WM_PRICE_RELATE_USER_ORDER " +
            "where ID_ORDER_V2 = ? ";

        PreparedStatement ps = null;
        try {
            ps = dbDyn.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, order.getIdOrder() );

            int i = ps.executeUpdate();

            if( log.isDebugEnabled() )
                log.debug( "count of deleted record - " + i );

        }
        catch( Exception e ) {
            final String es = "Error remove id_item_ to shop basket";
            log.error( es, e );
            throw new PriceException( es, e );
        }
        finally {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public static boolean isItemInBasket( final Long idItem, final OrderType order ) {
        if( order == null || idItem == null )
            return false;

        for( int i = 0; i < order.getShopOrdertListCount(); i++ ) {
            ShopOrderType shopOrder = order.getShopOrdertList( i );
            for( int k = 0; k < shopOrder.getOrderItemListCount(); k++ ) {
                OrderItemType item = shopOrder.getOrderItemList( k );
                if( idItem.equals( item.getIdItem() ) )
                    return true;
            }
        }
        return false;
    }

    public static void addItem( final DatabaseAdapter dbDyn, final OrderType order, final Long idItem, final int count, long siteId )
        throws Exception {
        if( log.isDebugEnabled() )
            log.debug( "Add new count of item. id_item - " + idItem + " count - " + count );

        PreparedStatement ps = null;

        try {
            ShopOrderType shopOrder = null;

            // в методе initItem выводится дополнительная информация в DEBUG
            OrderItemType item = initItem( dbDyn, idItem, siteId );
            if( log.isDebugEnabled() )
                log.debug( "idShop of created item - " + item.getIdShop() );

            item.setCountItem( count );
            boolean isNotInOrder = true;

            // если в заказе есть магазин и наименование, то изменяем количество
            for( int i = 0; i < order.getShopOrdertListCount(); i++ ) {
                ShopOrderType shopOrderTemp = order.getShopOrdertList( i );
                if( log.isDebugEnabled() )
                    log.debug( "shopOrder.idShop - " + shopOrderTemp.getIdShop() );

                if( shopOrderTemp.getIdShop().equals( item.getIdShop() ) ) {
                    if( log.isDebugEnabled() )
                        log.debug( "Нужный магазин найден. Ищем нужное наименование, idItem - " + idItem );

                    shopOrder = shopOrderTemp;
                    for( int k = 0; k < shopOrderTemp.getOrderItemListCount(); k++ ) {
                        OrderItemType orderItem = shopOrderTemp.getOrderItemList( k );
                        if( orderItem.getIdItem().equals( idItem ) ) {
                            if( log.isDebugEnabled() )
                                log.debug( "Нужное наименвание найдено, old count " + orderItem.getCountItem() + ". Устанавливаем новое количество " + count );

                            orderItem.setCountItem( count );
                            isNotInOrder = false;
                            break;
                        }
                    }
                }
            }

            if( log.isDebugEnabled() ) {
                if( isNotInOrder && shopOrder != null ) {
                    log.debug( "Нужный магазин найден, но наименования в нем нет." );
                }
            }

            // если в заказе нет магазина, то создаем магазин помещаем туда нименование
            if( shopOrder == null ) {
                if( log.isDebugEnabled() )
                    log.debug( "Нужного магазина не найдено. Создаем новый с кодом - " + item.getIdShop() );

                shopOrder = new ShopOrderType();
                shopOrder.setIdShop( item.getIdShop() );
                shopOrder.addOrderItemList( item );

                order.addShopOrdertList( shopOrder );

                isNotInOrder = false;
            }
            // если в заказе есть магазина, но нет наименования - помещаем наименование в заказ
            if( isNotInOrder ) {
                if( log.isDebugEnabled() )
                    log.debug( "Магазин есть но наименование не помещено в него. Помещаем" );

                shopOrder.addOrderItemList( item );
            }

            if( log.isDebugEnabled() ) {
                log.debug( "Try update count of existing item" );
                log.debug( "count - " + count );
                log.debug( "idItem - " + idItem );
                log.debug( "idOrder - " + order.getIdOrder() );
            }

            String sql_ =
                "update WM_PRICE_ORDER " +
                "set COUNT=? " +
                "where ID_ITEM=? and ID_ORDER_V2=? ";

            ps = dbDyn.prepareStatement( sql_ );

            ps.setInt( 1, count );
            RsetTools.setLong( ps, 2, idItem );
            RsetTools.setLong( ps, 3, order.getIdOrder() );

            int update = ps.executeUpdate();

            if( log.isDebugEnabled() )
                log.debug( "count of updated record - " + update );

            if( update == 0 )
                addItem( dbDyn, order.getIdOrder(), item );

        }
        catch( Exception e ) {
            log.error( "Error add id_item_ to shop basket", e );
            throw e;
        }
        finally {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public static void addItem( DatabaseAdapter dbDyn, Long idOrder, OrderItemType item )
        throws Exception {
        if( item == null )
            throw new Exception( "Error add item to order. Item is null" );

        if( log.isDebugEnabled() )
            log.debug( "Add new count of item. id_item - " + item.getIdItem() + " count - " + item.getCountItem() );

        PreparedStatement ps = null;

        try {
            String sql_ = null;

            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName( "seq_WM_price_order" );
            seq.setTableName( "WM_PRICE_ORDER" );
            seq.setColumnName( "ID_PRICE_ORDER_V2" );
            Long seqValue = dbDyn.getSequenceNextValue( seq );

            sql_ =
                "insert into WM_PRICE_ORDER " +
                "(ID_PRICE_ORDER_V2, ID_ORDER_V2, ID_ITEM, COUNT, ITEM, PRICE, " +
                "CURRENCY, PRICE_RESULT, CODE_CURRENCY_RESULT, NAME_CURRENCY_RESULT," +
                "PRECISION_CURRENCY_RESULT )" +
                "values " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

            if( log.isDebugEnabled() ) {
                log.debug( "insert new item to order" );
                log.debug( "id sequnce - " + seqValue );
                log.debug( "id_order - " + idOrder );
            }

            ps = dbDyn.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, seqValue );
            RsetTools.setLong( ps, 2, idOrder );
            RsetTools.setLong( ps, 3, item.getIdItem() );
            RsetTools.setInt( ps, 4, item.getCountItem() );
            ps.setString( 5, item.getItem() );
            RsetTools.setDouble( ps, 6, item.getPrice() );

            ps.setString( 7, item.getCurrencyItem().getCurrencyCode() );

            RsetTools.setDouble( ps, 8, item.getWmPriceItemResult() );
            ps.setString( 9, item.getResultCurrency().getCurrencyCode() );
            ps.setString( 10, item.getResultCurrency().getCurrencyName() );
            RsetTools.setInt( ps, 11, item.getPrecisionResult() );

            int update = ps.executeUpdate();

            if( log.isDebugEnabled() )
                log.debug( "count of inserted record - " + update );

        }
        catch( Exception e ) {
            log.error( "Error add id_item to shop basket", e );
            throw e;
        }
        finally {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public static void setItem( DatabaseAdapter dbDyn, OrderType order, Long idItem, int count, long siteId )
        throws Exception {
        boolean isNotInOrder = true;
        if( idItem == null )
            return;

        for( int i = 0; i < order.getShopOrdertListCount(); i++ ) {
            ShopOrderType shopOrder = order.getShopOrdertList( i );
            for( int k = 0; k < shopOrder.getOrderItemListCount(); k++ ) {
                OrderItemType item = shopOrder.getOrderItemList( k );
                if( idItem.equals( item.getIdItem() ) ) {
                    item.setCountItem( count );
                    isNotInOrder = false;
                    break;
                }
            }
        }

        if( isNotInOrder )
            addItem( dbDyn, order, idItem, count, siteId );

        String sql_ =
            "update WM_PRICE_ORDER " +
            "set COUNT = ? " +
            "where ID_ORDER_V2 = ? and ID_ITEM = ? ";

        PreparedStatement ps = null;
        try {
            ps = dbDyn.prepareStatement( sql_ );
            ps.setInt( 1, count );
            RsetTools.setLong( ps, 2, order.getIdOrder() );
            RsetTools.setLong( ps, 3, idItem );

            int update = ps.executeUpdate();

            if( log.isDebugEnabled() )
                log.debug( "count of updated record - " + update );

        }
        catch( Exception e ) {
            final String es = "Error set new count";
            log.error( es, e );
            throw new PriceException( es, e );
        }
        finally {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public static void delItem( DatabaseAdapter dbDyn, OrderType order, Long id_item )
        throws Exception {
        if( id_item == null )
            return;

        boolean isDeleted = false;
        for( int i = 0; i < order.getShopOrdertListCount(); i++ ) {
            ShopOrderType shopOrder = order.getShopOrdertList( i );
            for( int k = 0; k < shopOrder.getOrderItemListCount(); k++ ) {
                OrderItemType item = shopOrder.getOrderItemList( k );
                if( id_item.equals( item.getIdItem() ) ) {
                    shopOrder.removeOrderItemList( item );
                    // в данной версии подразумевается, что в заказе не может быть
                    // двух магазинов с одинаковым кодом товар
                    isDeleted = true;
                    break;
                }
            }
        }

        if( log.isDebugEnabled() ) {
            log.debug( "isDeleted - " + isDeleted );
        }

        String sql_ = "delete from WM_PRICE_ORDER where ID_ORDER_V2=? and ID_ITEM=? ";

        PreparedStatement ps = null;
        try {
            ps = dbDyn.prepareStatement( sql_ );

            RsetTools.setLong( ps, 1, order.getIdOrder() );
            RsetTools.setLong( ps, 2, id_item );

            int update = ps.executeUpdate();

            if( log.isDebugEnabled() )
                log.debug( "count of deleted record - " + update );

            dbDyn.commit();
        }
        catch( Exception e ) {
            final String es = "Error delete item from order";
            log.error( es, e );
            throw new PriceException( es, e );
        }
        finally {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public static void clear( DatabaseAdapter dbDyn, OrderType order )
        throws Exception {
        order.setShopOrdertList( new ArrayList() );

        String sql_ = "delete from WM_PRICE_ORDER where ID_ORDER_V2 = ? ";

        PreparedStatement ps = null;
        try {
            ps = dbDyn.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, order.getIdOrder() );

            int update = ps.executeUpdate();

            if( log.isDebugEnabled() )
                log.debug( "count of deleted record - " + update );

        }
        catch( Exception e ) {
            final String es = "Error clear order";
            log.error( es, e );
            throw new PriceException( es, e );
        }
        finally {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    private final static Object syncInitItemObj = new Object();

    public static OrderItemType initItem( DatabaseAdapter db_, Long idItem, long siteId )
        throws Exception {
        OrderItemType item = new OrderItemType();

        try {
            GetWmPriceListItem itemTemp = GetWmPriceListItem.getInstance( db_, idItem );

            final CurrencyManager currencyManager = CurrencyManager.getInstance( siteId );
            if( itemTemp.isFound ) {

                GetWmPriceListItem.copyItem( itemTemp.item, item );

                CurrencyItem currencyItem =
                    ( CurrencyItem ) CurrencyService.getCurrencyItemByCode( currencyManager.getCurrencyList(), item.getCurrency() );
                currencyItem.fillRealCurrencyData( currencyManager.getCurrencyList().getStandardCurrencyList() );

                item.setCurrencyItem( currencyItem );

                ShopBean shopBean = CommerceDaoFactory.getShopDao().getShop(item.getIdShop());

                if( log.isDebugEnabled() ) {
                    log.debug( "currencyCode " + item.getCurrency() );
                    log.debug( "currencyItem " + currencyItem );
                    log.debug( "item.price " + item.getPrice() );
                    if( currencyItem != null ) {
                        log.debug( "currencyItem.isRealInit " + currencyItem.getIsRealInit() );
                        log.debug( "currencyItem.getRealCurs " + currencyItem.getRealCurs() );
                    }
                }

                if( log.isDebugEnabled() )
                    log.debug( "new price will be calculated - " + ( currencyItem != null && Boolean.TRUE.equals( currencyItem.getIsRealInit() ) ) );

                if( currencyItem != null && Boolean.TRUE.equals( currencyItem.getIsRealInit() ) ) {

                    double resultPrice = 0;

                    if( log.isDebugEnabled() ) {
                        log.debug( "item idShop - " + item.getIdShop() );
                        log.debug( "shop idShop - " + shopBean.getShopId() );
                        log.debug( "item idCurrency - " + item.getCurrencyItem().getIdCurrency() );
                        log.debug( "shop idOrderCurrency - " + shopBean.getInvoiceCurrencyId() );
                        log.debug( "код валюты наименования совпадает с валютой в которой выводить заказ - " +
                            ( item.getCurrencyItem().getIdCurrency() == shopBean.getInvoiceCurrencyId() ) );
                    }

                    // если код валюты наименования совпадает с валютой в которой выводить заказ
                    int precisionValue = 2;
                    CurrencyPrecisionType precision = null;
                    if( item.getCurrencyItem().getIdCurrency().equals( shopBean.getInvoiceCurrencyId() ) ) {
                        item.setResultCurrency( item.getCurrencyItem() );

                        precision = getPrecisionValue( shopBean.getPrecisionList(), currencyItem.getIdCurrency() );
                        if( precision != null && precision.getPrecision() != null )
                            precisionValue = precision.getPrecision();

                        if( item.getPrice() != null )
                            resultPrice = NumberTools.truncate( item.getPrice(), precisionValue );
                        else {
                            if( log.isDebugEnabled() ) {
                                log.info( "price is null" );
                            }
                        }
                    }
                    else {
                        precision = getPrecisionValue( shopBean.getPrecisionList(), shopBean.getInvoiceCurrencyId() );
                        if( precision != null && precision.getPrecision() != null )
                            precisionValue = precision.getPrecision();

                        CustomCurrencyItemType defaultCurrency =
                            CurrencyService.getCurrencyItem( currencyManager.getCurrencyList(), shopBean.getInvoiceCurrencyId() );

                        item.setResultCurrency( defaultCurrency );

                        if( log.isDebugEnabled() ) {
                            synchronized( syncInitItemObj ) {
                                FileWriter w = new FileWriter( SiteUtils.getTempDir() + File.separatorChar + "schema-currency-item.xml" );
                                FileWriter w1 = new FileWriter( SiteUtils.getTempDir() + File.separatorChar + "schema-currency-default.xml" );

                                Marshaller.marshal( item.getCurrencyItem(), w );
                                Marshaller.marshal( defaultCurrency, w1 );

                                w.flush();
                                w.close();
                                w = null;
                                w1.flush();
                                w1.close();
                                w1 = null;

                                log.debug( "item curs - " + item.getCurrencyItem().getRealCurs() );
                                log.debug( "default curs - " + defaultCurrency.getRealCurs() );
                            }
                        }
                        double crossCurs = 0;

                        crossCurs = item.getCurrencyItem().getRealCurs() / defaultCurrency.getRealCurs();

                        if( item.getPrice() != null ) {
                            resultPrice =
                                NumberTools.truncate( item.getPrice(), precisionValue ) *
                                crossCurs;

                            if( log.isDebugEnabled() ) {
                                log.debug( "crossCurs - " + crossCurs + " price - " +
                                    NumberTools.truncate( item.getPrice(), precisionValue ) +
                                    " result price - " + resultPrice );
                            }
                        }
                        else {
                            if( log.isDebugEnabled() ) {
                                log.info( "price is null" );
                            }
                        }
                    }
                    item.setWmPriceItemResult( NumberTools.truncate( resultPrice, precisionValue ) );
                    item.setPrecisionResult( precisionValue );
                }
                else {
                    boolean isReal = false;
                    if( currencyItem != null )
                        isReal = Boolean.TRUE.equals( currencyItem.getIsRealInit() );

                    throw new PriceException( "Price for item can not calculated. CurrencyItem is " +
                        ( currencyItem == null ? "" : "not " ) + "null, is real curs init - " + isReal );
                }
                return item;
            }

        }
        catch( Exception e ) {
            log.error( "error init item", e );
            throw e;
        }
        return item;
    }

    private static CurrencyPrecisionType getPrecisionValue( CurrencyPrecisionList precList, Long idCurrency ) {
        CurrencyPrecisionType prec;
        prec = precList.getCurrencyPrecision( idCurrency );
        if( prec == null ) {
            if( log.isDebugEnabled() ) {
                log.info( "Precison not found for currencyId " + idCurrency );
            }
            return null;
        }
        return prec;
    }

    public static int getCountItem( OrderType order ) {

        if( order == null )
            return 0;
        int count = 0;
        for( int i = 0; i < order.getShopOrdertListCount(); i++ ) {
            ShopOrderType shopOrder = order.getShopOrdertList( i );
            count += shopOrder.getOrderItemListCount();
        }
        return count;
    }
}