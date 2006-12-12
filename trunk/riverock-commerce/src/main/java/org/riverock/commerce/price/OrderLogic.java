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

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.math.BigDecimal;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;

import org.apache.log4j.Logger;

import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.NumberTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.annotation.schema.db.CustomSequence;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.commerce.bean.ShopOrderItem;
import org.riverock.commerce.bean.Invoice;
import org.riverock.commerce.bean.ShopOrder;
import org.riverock.commerce.bean.ShopItem;
import org.riverock.commerce.bean.Shop;
import org.riverock.commerce.bean.CurrencyPrecision;
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
            AuthSession authSession = (AuthSession)renderRequest.getUserPrincipal();


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
            Shop tempShop = (Shop) fromSession;

            if( log.isDebugEnabled() ) {
                log.debug( "tempShop " + tempShop );
                if( tempShop != null )
                {
                    log.debug( "tempShop.idShop - " + tempShop.getShopId() );
                }
            }

            Shop shop = null;
            // if in session not exists current shop and queried concrete shop,
            // then create this shop and put it in session
            if( tempShop == null && idShop != null ) {
                if( log.isDebugEnabled() )
                    log.debug( "tempShop is null and idShop is not null " );

                shop = CommerceDaoFactory.getShopDao().getShop(idShop);
                setInSession(session, ShopPortlet.CURRENT_SHOP, shop);
            }
            // ���� � ������ ���� ������� ������� �
            // ��� ���������� �������� ��������� � ����� ������ � ������,
            // ����� ��� (���, ������� � ������)
            else {
                if( tempShop != null &&
                    ( idShop == null || idShop.equals( tempShop.getShopId() ) ) ) {
                    if( log.isDebugEnabled() )
                        log.debug( "tempShop is not null and tempShop.idShop == idShop " );

                    shop = tempShop;
                }
                // ���� � ������ ���� ������� ������� �
                // ��� ���������� �������� �� ��������� � ����� ������ � ������,
                // �������� ����� � ������ �� ����� � ���������� �����
                else {
                    if( tempShop != null && idShop != null && !idShop.equals( tempShop.getShopId() ) ) {
                        if( log.isDebugEnabled() ) {
                            log.debug( "#11.22.09 create shop instance with idShop - " + idShop );
                        }

                        shop = CommerceDaoFactory.getShopDao().getShop(idShop);

                        if( log.isDebugEnabled() )
                        {
                            log.debug( "idShop of created shop - " + shop.getShopId() );
                        }

                        removeFromSession(session, ShopPortlet.CURRENT_SHOP);
                        setInSession(session, ShopPortlet.CURRENT_SHOP, shop);
                    }
                }
            }
            // ������ � shop ��������� ������� ����� ( ��� ������� � ������ )
            // ���� ��� �������� ������ ������� - ����� � ���������� ����� ������������� ����,
            // ����� shop == null

            if( log.isDebugEnabled() ) {
                log.debug( "shop object " + shop);
                if( shop != null )
                {
                    log.debug( "shop.id_shop " + shop.getShopId() );
                }
            }

            Invoice order = null;
            // ���� ������� ����� ���������, �� ���� � ������ �����, ��������� � ���� �������.
            // ���� ������ � ������ ���, �� �������
            if( shop != null && shop.getShopId() != null ) {
                order = (Invoice) getFromSession(session, ShopPortlet.ORDER_SESSION);

                if( log.isDebugEnabled() )
                    log.debug( "order object - " + order );

                if( order == null ) {
                    if( log.isDebugEnabled() ) {
                        log.debug( "Create new order" );
                    }

                    order = new Invoice();
                    order.setServerName( renderRequest.getServerName() );

                    ShopOrder shopOrder = new ShopOrder();
                    shopOrder.setShopId( shop.getShopId() );

                    order.getShopOrders().add( shopOrder );
                    initAuthSession( dbDyn, order, ( AuthSession ) renderRequest.getUserPrincipal() );
                }

                // ���� ����� ������ ����� � ���� ������ �����������,
                // �������� ��������������� ������ � �����
                if ( order != null && authSession != null ) {
                    if( authSession.checkAccess( renderRequest.getServerName() ) ) {
                        if( log.isDebugEnabled() ) {
                            log.debug( "updateAuthSession" );
                        }

                        updateAuthSession( dbDyn, order, authSession );
                    }
                }

                Long id_item = PortletService.getLong( renderRequest, ShopPortlet.NAME_ADD_ID_ITEM );
                int count = PortletService.getInt( renderRequest, ShopPortlet.NAME_COUNT_ADD_ITEM_SHOP, 0 );

                // ���� ��� ������ ���� ������� ����� ���� ���������� ������������� ������������,
                // �� �������� ��� ������������ � �����
                if( log.isDebugEnabled() )
                    log.debug( "set new count of item. id_item - " + id_item + " count - " + count );

                if( ( id_item != null ) && ( count > 0 ) ) {
                    if( log.isDebugEnabled() ) {
                        log.debug( "add item to order" );
                        log.debug( "id_order " + order.getOrderId() );
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

    public static void initAuthSession( final DatabaseAdapter dbDyn, final Invoice order, final AuthSession authSession )
        throws Exception {
        String sql_ = "";
        PreparedStatement ps = null;
        try {
            CustomSequence seq = new CustomSequence();
            seq.setSequenceName( "SEQ_WM_PRICE_RELATE_USER_ORDER" );
            seq.setTableName( "WM_PRICE_RELATE_USER_ORDER" );
            seq.setColumnName( "ID_ORDER_V2" );

            order.setOrderId( dbDyn.getSequenceNextValue( seq ) );

            sql_ =
                "insert into WM_PRICE_RELATE_USER_ORDER " +
                "(ID_ORDER_V2, DATE_CREATE, ID_USER)" +
                "values " +
                "(?,  " + dbDyn.getNameDateBind() + ", ? )";

            ps = dbDyn.prepareStatement( sql_ );

            RsetTools.setLong( ps, 1, order.getOrderId() );
            dbDyn.bindDate( ps, 2, DateTools.getCurrentTime() );

            if( authSession != null && authSession.getUser() != null ) {
                RsetTools.setLong( ps, 3, authSession.getUser().getUserId() );
            }
            else
                ps.setNull( 3, Types.NUMERIC );

            int i = ps.executeUpdate();

            if( log.isDebugEnabled() )
                log.debug( "count of inserted record - " + i );
        }
        catch( Exception e1 ) {
            log.error( "order.getOrderId() " + order.getOrderId() );
            log.error( "authSession " + authSession );
            if( authSession != null && authSession.getUser() != null )
                log.error( "authSession.getUserInfo().getIdUser() " + authSession.getUser().getUserId() );

            log.error( "Error init AuthSession", e1 );
            throw e1;
        }
        finally {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

    public static void updateAuthSession( final DatabaseAdapter dbDyn, final Invoice order, final AuthSession authSession )
        throws Exception {
        String sql_ = "";
        PreparedStatement ps = null;
        try {
            if( authSession != null ) {
                Long userId = authSession.getUser().getUserId();
                if( userId != null ) {
                    sql_ =
                        "update WM_PRICE_RELATE_USER_ORDER " +
                            "set    ID_USER = ? " +
                            "where  ID_ORDER_V2 = ? ";

                    ps = dbDyn.prepareStatement( sql_ );

                    RsetTools.setLong( ps, 1, userId );
                    RsetTools.setLong( ps, 2, order.getOrderId() );
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

    public static void removeOrder( final DatabaseAdapter dbDyn, final Invoice order )
        throws Exception {

        String sql_ =
            "delete from WM_PRICE_RELATE_USER_ORDER " +
            "where ID_ORDER_V2 = ? ";

        PreparedStatement ps = null;
        try {
            ps = dbDyn.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, order.getOrderId() );

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

    public static boolean isItemInBasket( final Long idItem, final Invoice order ) {
        if( order == null || idItem == null )
            return false;

        for (ShopOrder shopOrder : order.getShopOrders()) {
            for (ShopOrderItem item : shopOrder.getShopOrderItems()) {
                if( idItem.equals( item.getShopItem().getShopItemId() ) )
                    return true;
            }
        }
        return false;
    }

    public static void addItem( final DatabaseAdapter dbDyn, final Invoice order, final Long idItem, final int count, long siteId )
        throws Exception {
        if( log.isDebugEnabled() ) {
            log.debug( "Add new count of item. id_item - " + idItem + " count - " + count );
        }

        // � ������ initOrderItem ��������� �������������� ���������� � DEBUG
        ShopOrderItem item = initOrderItem(idItem, siteId );
        if( log.isDebugEnabled() ) {
            log.debug( "idShop of created item - " + item.getShopItem().getShopId() );
        }

        if (item==null) {
            return;
        }
        
        PreparedStatement ps = null;
        try {
            ShopOrder shopOrder = null;

            item.setCountItem( count );
            boolean isNotInOrder = true;

            // ���� � ������ ���� ������� � ������������, �� �������� ����������
            for (ShopOrder shopOrderTemp : order.getShopOrders()) {
                if( log.isDebugEnabled() ) {
                    log.debug( "shopOrder.idShop - " + shopOrderTemp.getShopId() );
                }

                if( shopOrderTemp.getShopId().equals( item.getShopItem().getShopId() ) ) {
                    if( log.isDebugEnabled() ) {
                        log.debug( "Shop exist. Start search item, idItem: " + idItem );
                    }

                    shopOrder = shopOrderTemp;
                    for (ShopOrderItem orderItem : shopOrderTemp.getShopOrderItems()) {
                        if( orderItem.getShopItem().getShopItemId().equals( idItem ) ) {
                            if( log.isDebugEnabled() ) {
                                log.debug( "������ ����������� �������, old count " + orderItem.getCountItem() + ". ������������� ����� ���������� " + count );
                            }

                            orderItem.setCountItem( count );
                            isNotInOrder = false;
                            break;
                        }
                    }
                }
            }

            if( log.isDebugEnabled() ) {
                if( isNotInOrder && shopOrder != null ) {
                    log.debug( "������ ������� ������, �� ������������ � ��� ���." );
                }
            }

            // ���� � ������ ��� ��������, �� ������� ������� �������� ���� �����������
            if( shopOrder == null ) {
                if( log.isDebugEnabled() )
                    log.debug( "������� �������� �� �������. ������� ����� � ����� - " + item.getShopItem().getShopId() );

                shopOrder = new ShopOrder();
                shopOrder.setShopId( item.getShopItem().getShopId() );
                shopOrder.getShopOrderItems().add( item );

                order.getShopOrders().add( shopOrder );

                isNotInOrder = false;
            }
            // ���� � ������ ���� ��������, �� ��� ������������ - �������� ������������ � �����
            if( isNotInOrder ) {
                if( log.isDebugEnabled() )
                    log.debug( "������� ���� �� ������������ �� �������� � ����. ��������" );

                shopOrder.getShopOrderItems().add( item );
            }

            if( log.isDebugEnabled() ) {
                log.debug( "Try update count of existing item" );
                log.debug( "count - " + count );
                log.debug( "idItem - " + idItem );
                log.debug( "idOrder - " + order.getOrderId() );
            }

            String sql_ =
                "update WM_PRICE_ORDER " +
                "set COUNT=? " +
                "where ID_ITEM=? and ID_ORDER_V2=? ";

            ps = dbDyn.prepareStatement( sql_ );

            ps.setInt( 1, count );
            RsetTools.setLong( ps, 2, idItem );
            RsetTools.setLong( ps, 3, order.getOrderId() );

            int update = ps.executeUpdate();

            if( log.isDebugEnabled() )
                log.debug( "count of updated record - " + update );

            if( update == 0 )
                addItem( dbDyn, order.getOrderId(), item );

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

    public static void addItem( DatabaseAdapter dbDyn, Long idOrder, ShopOrderItem item )
        throws Exception {
        if( item == null )
            throw new Exception( "Error add item to order. Item is null" );

        if( log.isDebugEnabled() )
            log.debug( "Add new count of item. id_item - " + item.getShopItem().getShopItemId() + " count - " + item.getCountItem() );

        PreparedStatement ps = null;

        try {
            String sql_ = null;

            CustomSequence seq = new CustomSequence();
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

    public static void setItem( DatabaseAdapter dbDyn, Invoice order, Long idItem, int count, long siteId )
        throws Exception {
        boolean isNotInOrder = true;
        if( idItem == null )
            return;

        for (ShopOrder shopOrder : order.getShopOrders()) {
            for (ShopOrderItem item : shopOrder.getShopOrderItems()) {
                if( idItem.equals( item.getShopItem().getShopItemId() ) ) {
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
            RsetTools.setLong( ps, 2, order.getOrderId() );
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

    public static void delItem( DatabaseAdapter dbDyn, Invoice order, Long id_item )
        throws Exception {
        if( id_item == null )
            return;

        // in current version all shops has its unique code of items
        boolean isDeleted = false;
        for (ShopOrder shopOrder : order.getShopOrders()) {
            Iterator<ShopOrderItem> it = shopOrder.getShopOrderItems().iterator();
            while (it.hasNext()) {
                ShopOrderItem item = it.next();
                if( id_item.equals( item.getShopItem().getShopItemId() ) ) {
                    it.remove();
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

            RsetTools.setLong( ps, 1, order.getOrderId() );
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

    public static void clear( DatabaseAdapter dbDyn, Invoice order ) throws Exception {
        order.setShopOrders( new ArrayList<ShopOrder>() );

        String sql_ = "delete from WM_PRICE_ORDER where ID_ORDER_V2 = ? ";

        PreparedStatement ps = null;
        try {
            ps = dbDyn.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, order.getOrderId() );

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

    public static ShopOrderItem initOrderItem(Long idItem, long siteId) throws Exception {
        try {
            ShopItem shopItem = CommerceDaoFactory.getShopDao().getShopItem(idItem);
            if (shopItem!=null) {
                ShopOrderItem item = new ShopOrderItem();
                item.setShopItem(shopItem);
                
                final CurrencyManager currencyManager = CurrencyManager.getInstance( siteId );

                CurrencyItem currencyItem =
                    CurrencyService.getCurrencyItemByCode( currencyManager.getCurrencyList(), item.getShopItem().getCurrency() );
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
                        log.debug( "��� ������ ������������ ��������� � ������� � ������� �������� ����� - " +
                            ( item.getCurrencyItem().getCurrencyId() == shop.getInvoiceCurrencyId() ) );
                    }

                    // ���� ��� ������ ������������ ��������� � ������� � ������� �������� �����
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

    public static int getCountItem( Invoice invoice ) {
        if( invoice == null ) {
            return 0;
        }
        int count = 0;
        for (ShopOrder shopOrder : invoice.getShopOrders()) {
            count += shopOrder.getShopOrderItems().size();
        }
        return count;
    }
}