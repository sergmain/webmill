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
package org.riverock.commerce.price;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;



import org.apache.log4j.Logger;

import org.riverock.common.tools.NumberTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.portlet.schema.price.CurrencyPrecisionType;
import org.riverock.portlet.schema.price.OrderItemType;
import org.riverock.portlet.schema.price.OrderType;
import org.riverock.portlet.schema.price.ShopOrderType;

/**
 * $Id$
 */
public class PriceList {
    private static Logger log = Logger.getLogger( PriceList.class );

    public PriceList() {
    }

    public static double calcPrice( double priceItem, Object object[] ) {

        if( log.isDebugEnabled() ) {
            log.debug( "calc price. price for round " + priceItem );
            log.debug( "calc price: len=" + object.length );
        }

        // search object of AuthSession for calculate discount
        double price = priceItem;

        Object obj = null;
        AuthSession auth = null;
        Shop shop = null;
        int precision = 2;

        for( int i = 0; i < object.length; i++ ) {
            obj = object[i];

            if( log.isDebugEnabled() )
                log.debug( "calc price #1: obj[" + i + "]=" + obj );

            if( ( obj != null ) && ( obj instanceof AuthSession ) ) {
                auth = ( AuthSession ) obj;

            }
            else if( ( obj != null ) && ( obj instanceof Shop ) ) {
                shop = ( Shop ) obj;
            }
            else if( ( obj != null ) && ( obj instanceof CurrencyPrecisionType ) ) {
                if( log.isDebugEnabled() )
                    log.debug( "Redefine default value of precision" );

                precision = ( ( CurrencyPrecisionType ) obj ).getPrecision();
            }
        }

        if( auth != null ) {
            if( log.isDebugEnabled() )
                log.debug( "calc price: user discount=" + auth.getUserInfo().getDiscount() );

            if( auth.getUserInfo().getDiscount() != null )
                price = price * ( 100 - auth.getUserInfo().getDiscount() ) / 100;
        }

        // производим округление
        if( shop != null ) {
            if( log.isDebugEnabled() ) {
                log.debug( "calc price: shop comma=" + precision );
                log.debug( "calc price. shop.discount - " + shop.discount );

            }

            price = price * ( 100 - shop.discount ) / 100;
            if( log.isDebugEnabled() )
                log.debug( "calc price. price - " + price + " shop.commas - " + precision );

            price = NumberTools.truncate( price, precision );

            if( log.isDebugEnabled() )
                log.debug( "calc price. price - " + price );

        }
        return price;
    }

    // Расчет значения итоговой суммы для заказа
    public static double calcSummPrice( Vector v, Object object[] ) {

        if( log.isDebugEnabled() )
            log.debug( "calc price: len=" + object.length );

        // search object of AuthSession for calculate discount
        Object obj = null;
        double discountUser = 0;
        AuthSession auth = null;
        Shop shop = null;
        int precision = 2;

        for( int i = 0; i < object.length; i++ ) {
            obj = object[i];

            if( log.isDebugEnabled() )
                log.debug( "calc price #1: obj[" + i + "]=" + obj );

            if( ( obj != null ) && ( obj instanceof AuthSession ) ) {
                auth = ( AuthSession ) obj;
            }
            else if( ( obj != null ) && ( obj instanceof Shop ) ) {
                shop = ( Shop ) obj;
            }
            else if( ( obj != null ) && ( obj instanceof CurrencyPrecisionType ) ) {
                if( log.isDebugEnabled() )
                    log.debug( "Redefine default value of precision" );

                precision = ( ( CurrencyPrecisionType ) obj ).getPrecision();
            }
        }

        if( auth != null ) {
            if( log.isDebugEnabled() )
                log.debug( "calc price: user discount=" + auth.getUserInfo().getDiscount() );

            if( auth.getUserInfo().getDiscount() != null )
                discountUser = ( 100 - auth.getUserInfo().getDiscount() ) / 100;
        }

        double discountShop = 0;
        if( shop != null ) {
            discountShop = shop.discount;
        }

        // производим округление
        double summOrder = 0;
        for( int i = 0; i < v.size(); i++ ) {
            obj = v.elementAt( i );

            if( log.isDebugEnabled() )
                log.debug( "calc price #2: obj[" + i + "]=" + obj );

            if( ( obj != null ) && ( obj instanceof PriceListItemOrder ) ) {
                PriceListItemOrder item = ( PriceListItemOrder ) obj;

                if( log.isDebugEnabled() )
                    log.debug( "calc price: shop comma=" + precision );

                double itemPrice =
                    NumberTools.truncate( item.priceItem, precision );

                itemPrice = itemPrice * ( 100 - discountUser ) / 100;

                itemPrice = itemPrice * ( 100 - discountShop ) / 100;

                summOrder +=
                    NumberTools.truncate( itemPrice, precision ) *
                    item.qty;
            }
        }

        return summOrder;
    }

/*
    public static PriceSpecialItems getSpecialItems(DatabaseAdapter db_,
                                                    Shop shop, int currencyID, String serverName)
            throws PriceException
    {
        String sql_ = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        PriceSpecialItems specItems = new PriceSpecialItems();

        try
        {
            if (currencyID == 0)
            {
                sql_ =

                        "select a.* " +
                        "from 	v_b2c_items_price_simple a " +
                        "where	name_virtual_host = lower(?) and " +
                        "	a.id_shop = ? and a.is_group = 0 and is_special=1 " +
                        "order by a.id asc ";

                log.debug(sql_);

                ps = db_.conn.prepareCall(sql_);
                ps.setString(1, serverName);
                RsetTools.setLong(ps, 2, shop.id_shop);

            }
            else
            {
                sql_ =

                        "select  a.* " +
                        "from v_b2c_items_price_currency a " +
                        "where name_virtual_host = lower(?) and " +
                        "	a.id_shop = ? and is_special = 1 and " +
                        "	a.id_std_currency = ? " +
                        "order by a.id asc ";

                ps = db_.conn.prepareCall(sql_);
                ps.setString(1, serverName);
                RsetTools.setLong(ps, 2, shop.id_shop);
                ps.setInt(3, currencyID);

            }

            rs = ps.executeQuery();

            while (rs.next())
                specItems.items.add(new PriceListItemExtend(db_, rs));


        }
        catch (Exception e)
        {
            log.error("Error get special items", e);
            throw new PriceException(e.toString());
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
        return specItems;
    }
*/

/*
    public static Vector getOrderItems(DatabaseAdapter db_,
        long id_shop, int currencyID, Long siteId,
        long id_order)
        throws PriceException
    {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Vector retVector = null;

        try
        {
            if (currencyID == 0)
                throw new Exception("Default currency for this shop not defined");

            String sql_ =
                "select a.*, c.count qty " +
                "from   v_b2c_items_price_currency a, " +
                " 	    price_relate_user_order b, price_order c  " +
                "where  a.ID_SITE=? and " +
                "       a.id_shop=? and a.id_shop=b.id_shop and " +
                "       a.id_std_currency=? and " +
                "       a.id_item=c.id_item and " +
                "	    c.id_order=b.id_order and " +
                "	    b.id_order=? " +
                "order by a.ITEM asc ";

            ps = db_.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, siteId);
            RsetTools.setLong(ps, 2, id_shop);
            ps.setInt(3, currencyID);
            RsetTools.setLong(ps, 4, id_order);

            rs = ps.executeQuery();

            retVector = new Vector(25, 10);
            while (rs.next())
                retVector.add(new PriceListItemOrder(rs));

        }
        catch (Exception e)
        {
            throw new PriceException(e.toString());
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
        return retVector;
    }
*/

    private static void controllLoop( int idx, OrderType order ) {
        if( log.isDebugEnabled() ) {
            log.debug( " Контрольный цикл " + idx );
            for( int i = 0; i < order.getShopOrdertListCount(); i++ ) {
                ShopOrderType shopOrder = order.getShopOrdertList( i );
                for( int k = 0; k < shopOrder.getOrderItemListCount(); k++ ) {
                    OrderItemType item = shopOrder.getOrderItemList( k );
                    log.debug( "id_item - " + item.getIdItem() + ", isInDb - " + item.getIsInDb() );
                }
            }
            log.debug( "" );
        }
    }

    public static void synchronizeOrderWithDb( DatabaseAdapter dbDyn, OrderType order )
        throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql_ =
                "select a.* from WM_PRICE_ORDER a where a.ID_ORDER_V2=?";

            ps = dbDyn.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, order.getIdOrder() );

            rs = ps.executeQuery();

            // Перед проверкой устанавливаем все наименования как отсутствующие в базе
            for( int i = 0; i < order.getShopOrdertListCount(); i++ ) {
                ShopOrderType shopOrder = order.getShopOrdertList( i );
                for( int k = 0; k < shopOrder.getOrderItemListCount(); k++ ) {
                    OrderItemType item = shopOrder.getOrderItemList( k );
                    item.setIsInDb( false );
                }
            }

            while( rs.next() ) {
                if( log.isDebugEnabled() )
                    log.debug( "do get from ResultSet " );

                Long idItem = RsetTools.getLong( rs, "ID_ITEM" );

                if( log.isDebugEnabled() )
                    log.debug( "Check item from DB with idItem - " + idItem );

                // ищем в заказе из сессии товар, который есть в базе
                for( int i = 0; i < order.getShopOrdertListCount(); i++ ) {
                    ShopOrderType shopOrder = order.getShopOrdertList( i );

                    if( log.isDebugEnabled() )
                        controllLoop( 1, order );

                    if( log.isDebugEnabled() ) {
                        log.debug( "check shop. idShop  - " + shopOrder.getIdShop() );
                        log.debug( "Count of items in shopOrder  - " + shopOrder.getOrderItemListCount() );
                    }

                    for( int k = 0; k < shopOrder.getOrderItemListCount(); k++ ) {
                        OrderItemType item = shopOrder.getOrderItemList( k );

                        if( log.isDebugEnabled() ) {
                            log.debug( "item object - " + item );
                            log.debug( "item isInDb - " + item.getIsInDb() );
                            log.debug( "check item with id - " + idItem );
                            log.debug( "status compare id_item - " + ( item.getIdItem() == idItem ) );
                        }

                        if( log.isDebugEnabled() )
                            controllLoop( 2, order );

                        if( item.getIdItem() == idItem ) {
                            item.setIsInDb( true );

                            if( log.isDebugEnabled() )
                                controllLoop( 3, order );

                            if( log.isDebugEnabled() ) {
                                log.debug( "item in order. check values of order" );
                                log.debug( "item price " + item.getWmPriceItemResult() );
                                log.debug( "db price " + RsetTools.getDouble( rs, "PRICE_RESULT" ) );
                                log.debug( "init count " + item.getCountItem() );
                                log.debug( "db count " + RsetTools.getInt( rs, "COUNT" ) );
                                log.debug( "item code " + item.getResultCurrency().getCurrencyCode() );
                                log.debug( "db code " + RsetTools.getString( rs, "CODE_CURRENCY_RESULT" ) );
                                log.debug( "status of compare order item data " + ( item.getWmPriceItemResult() != RsetTools.getDouble( rs, "PRICE_RESULT" ) ||
                                    ( item.getCountItem() != RsetTools.getInt( rs, "COUNT" ) ) ||
                                    !item.getResultCurrency().getCurrencyCode().equals( RsetTools.getString( rs, "CODE_CURRENCY_RESULT" ) )
                                    ) );
                                log.debug( "price " + ( item.getWmPriceItemResult() != RsetTools.getDouble( rs, "PRICE_RESULT" ) ) );
                                log.debug( "count " + ( item.getCountItem() != RsetTools.getInt( rs, "COUNT" ) ) );
                                log.debug( "currency " + !item.getResultCurrency().getCurrencyCode().equals( RsetTools.getString( rs, "CODE_CURRENCY_RESULT" ) ) );
                            }

                            if( log.isDebugEnabled() )
                                controllLoop( 31, order );

                            // Заказ найден, сравниваем данные
                            if( item.getWmPriceItemResult() != RsetTools.getDouble( rs, "PRICE_RESULT" ) ||
                                item.getCountItem() != RsetTools.getInt( rs, "COUNT" ) ||
                                !item.getResultCurrency().getCurrencyCode().equals( RsetTools.getString( rs, "CODE_CURRENCY_RESULT" ) )
                            ) {
                                if( log.isDebugEnabled() )
                                    controllLoop( 32, order );

                                if( log.isDebugEnabled() ) {
                                    log.debug( "item in order not equals with item in DB" );

                                    log.debug( "item price " + item.getWmPriceItemResult() );
                                    log.debug( "db price " + RsetTools.getDouble( rs, "PRICE_RESULT" ) );
                                    log.debug( "item count " + item.getCountItem() );
                                    log.debug( "db count " + RsetTools.getInt( rs, "COUNT" ) );
                                    log.debug( "item currency code " + item.getResultCurrency().getCurrencyCode() );
                                    log.debug( "item currency code " + RsetTools.getString( rs, "CODE_CURRENCY_RESULT" ) );
                                }

                                PreparedStatement ps1 = null;
                                try {
                                    String sqlDel =
                                        "delete from WM_PRICE_ORDER a where a.ID_ORDER_V2=?";

                                    ps1 = dbDyn.prepareStatement( sqlDel );
                                    RsetTools.setLong( ps1, 1, order.getIdOrder() );

                                    ps1.executeUpdate();
                                }
                                finally {
                                    DatabaseManager.close( ps1 );
                                    ps1 = null;
                                }

                                if( log.isDebugEnabled() )
                                    controllLoop( 33, order );

                                if( log.isDebugEnabled() )
                                    log.debug( "success delete item from db" );

                                OrderLogic.addItem( dbDyn, order.getIdOrder(), item );

                                if( log.isDebugEnabled() )
                                    controllLoop( 34, order );

                            }

                            if( log.isDebugEnabled() ) {
                                log.debug( "do continue" );
                                log.debug( "item idItem " + item.getIdItem() + " inDb " + item.getIsInDb() );
                            }

                            if( log.isDebugEnabled() )
                                controllLoop( 35, order );

                            break;
                        }

                        if( log.isDebugEnabled() )
                            controllLoop( 4, order );
                    }
                }
            }

            if( log.isDebugEnabled() )
                controllLoop( 5, order );

            // записываем в базу все наименования которых нет в базе
            for( int i = 0; i < order.getShopOrdertListCount(); i++ ) {
                ShopOrderType shopOrder = order.getShopOrdertList( i );
                for( int k = 0; k < shopOrder.getOrderItemListCount(); k++ ) {
                    OrderItemType item = shopOrder.getOrderItemList( k );
                    if( log.isDebugEnabled() ) {
                        log.debug( "item object - " + item );
                        log.debug( "item idItem " + item.getIdItem() );
                        log.debug( "item inDb - " + item.getIsInDb() );
                    }

                    if( !Boolean.TRUE.equals( item.getIsInDb() ) ) {
                        if( log.isDebugEnabled() )
                            log.debug( "item not in db " + item.getIdItem() );

                        OrderLogic.addItem( dbDyn, order.getIdOrder(), item );
                        item.setIsInDb( true );
                    }
                }
            }

        }
        catch( Exception e ) {
            log.error( "Error synchronize order with DB", e );
            throw e;
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
    }
}