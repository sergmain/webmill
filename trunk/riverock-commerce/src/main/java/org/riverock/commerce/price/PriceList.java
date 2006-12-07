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
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.commerce.bean.ShopOrderItem;
import org.riverock.commerce.bean.Invoice;
import org.riverock.commerce.bean.ShopOrder;

/**
 * $Id$
 */
public class PriceList {
    private static Logger log = Logger.getLogger( PriceList.class );

    public PriceList() {
    }

/*
    public static double calcPrice( double priceItem, Object object[] ) {

        if( log.isDebugEnabled() ) {
            log.debug( "calc price. price for round " + priceItem );
            log.debug( "calc price: len=" + object.length );
        }

        // search object of AuthSession for calculate discount
        double price = priceItem;

        AuthSession auth = null;
        Shop shop = null;
        int precision = 2;

        for( int i = 0; i < object.length; i++ ) {
            Object obj = object[i];

            if( log.isDebugEnabled() )
                log.debug( "calc price #1: obj[" + i + "]=" + obj );

            if( ( obj != null ) && ( obj instanceof AuthSession ) ) {
                auth = ( AuthSession ) obj;

            }
            else if( ( obj != null ) && ( obj instanceof Shop ) ) {
                shop = ( Shop ) obj;
            }
            else if( ( obj != null ) && ( obj instanceof CurrencyPrecision ) ) {
                if( log.isDebugEnabled() )
                    log.debug( "Redefine default value of precision" );

                precision = ( ( CurrencyPrecision ) obj ).getPrecision();
            }
        }

        if( auth != null ) {
            // TODO in just time discount for all users is 0 (zero)
            int discount = 0;
            price = price * ( 100 - discount ) / 100;
        }

        // производим округление
        if( shop != null ) {
            if( log.isDebugEnabled() ) {
                log.debug( "calc price: shop comma=" + precision );
                log.debug( "calc price. shop.discount - " + shop.getDiscount() );

            }

            price = price * ( 100 - shop.getDiscount() ) / 100;
            if( log.isDebugEnabled() )
                log.debug( "calc price. price - " + price + " shop.commas - " + precision );

            price = NumberTools.truncate( price, precision );

            if( log.isDebugEnabled() )
                log.debug( "calc price. price - " + price );

        }
        return price;
    }
*/

/*
    // –асчет значени€ итоговой суммы дл€ заказа
    public static double calcSummPrice( Vector v, Object object[] ) {

        if( log.isDebugEnabled() )
            log.debug( "calc price: len=" + object.length );

        double discountUser = 0;
        AuthSession auth = null;
        Shop shop = null;
        int precision = 2;

        // search object of AuthSession for calculate discount
        for( int i = 0; i < object.length; i++ ) {
            Object obj = object[i];

            if( log.isDebugEnabled() )
                log.debug( "calc price #1: obj[" + i + "]=" + obj );

            if( ( obj != null ) && ( obj instanceof AuthSession ) ) {
                auth = ( AuthSession ) obj;
            }
            else if( ( obj != null ) && ( obj instanceof Shop ) ) {
                shop = ( Shop ) obj;
            }
            else if( ( obj != null ) && ( obj instanceof CurrencyPrecision ) ) {
                if( log.isDebugEnabled() )
                    log.debug( "Redefine default value of precision" );

                precision = ( ( CurrencyPrecision ) obj ).getPrecision();
            }
        }

        if( auth != null ) {
            // TODO in just time discount for all users is 0 (zero)
            int discount = 0;
            if( log.isDebugEnabled() )
                log.debug( "calc price: user discount=" + 0 );

            discountUser = ( 100 - discount ) / 100;
        }

        double discountShop = 0;
        if( shop != null ) {
            discountShop = shop.getDiscount();
        }

        // производим округление
        double summOrder = 0;
        for( int i = 0; i < v.size(); i++ ) {
            Object obj = v.elementAt( i );

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
*/

    private static void controllLoop( int idx, Invoice invoice ) {
        if( log.isDebugEnabled() ) {
            log.debug( "Check loop #" + idx );
            for (ShopOrder shopOrder : invoice.getShopOrders()) {
                for (ShopOrderItem item : shopOrder.getShopOrderItems()) {
                    log.debug( "id_item - " + item.getShopItem().getItemId() + ", isInDb - " + item.getInDb() );
                }
            }
            log.debug( "" );
        }
    }

    public static void synchronizeOrderWithDb( DatabaseAdapter dbDyn, Invoice invoice)
        throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql_ =
                "select a.* from WM_PRICE_ORDER a where a.ID_ORDER_V2=?";

            ps = dbDyn.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, invoice.getOrderId() );

            rs = ps.executeQuery();

            // Before check, for all item we set flag isInDb to false
            for (ShopOrder shopOrder : invoice.getShopOrders()) {
                for (ShopOrderItem item : shopOrder.getShopOrderItems()) {
                    item.setInDb( false );
                }
            }

            while( rs.next() ) {
                if( log.isDebugEnabled() )
                    log.debug( "do get from ResultSet " );

                Long idItem = RsetTools.getLong( rs, "ID_ITEM" );

                if( log.isDebugEnabled() )
                    log.debug( "Check item from DB with idItem - " + idItem );

                // search in invoice from session item, which exist in db
                for (ShopOrder shopOrder : invoice.getShopOrders()) {

                    if( log.isDebugEnabled() ) {
                        controllLoop( 1, invoice);
                        log.debug( "check shop. idShop  - " + shopOrder.getShopId() );
                        log.debug( "Count of items in shopOrder  - " + shopOrder.getShopOrderItems().size() );
                    }

                    for (ShopOrderItem item : shopOrder.getShopOrderItems()) {

                        if( log.isDebugEnabled() ) {
                            log.debug( "item object - " + item );
                            log.debug( "item isInDb - " + item.getInDb() );
                            log.debug( "check item with id - " + idItem );
                            log.debug( "status compare id_item - " + ( item.getShopItem().getItemId() == idItem ) );
                        }

                        if( log.isDebugEnabled() ) {
                            controllLoop( 2, invoice);
                        }

                        if( item.getShopItem().getItemId() == idItem ) {
                            item.setInDb( true );

                            if( log.isDebugEnabled() )
                                controllLoop( 3, invoice);

                            if( log.isDebugEnabled() ) {
                                log.debug( "item in invoice. check values of invoice" );
                                log.debug( "item price " + item.getResultPrice() );
                                log.debug( "db price " + RsetTools.getDouble( rs, "PRICE_RESULT" ) );
                                log.debug( "init count " + item.getCountItem() );
                                log.debug( "db count " + RsetTools.getInt( rs, "COUNT" ) );
                                log.debug( "item code " + item.getResultCurrency().getCurrencyCode() );
                                log.debug( "db code " + RsetTools.getString( rs, "CODE_CURRENCY_RESULT" ) );
                                log.debug( "status of compare invoice item data " +
                                    ( !item.getResultPrice().equals( RsetTools.getBigDecimal( rs, "PRICE_RESULT") ) ||
                                    ( item.getCountItem() != RsetTools.getInt( rs, "COUNT" ) ) ||
                                    !item.getResultCurrency().getCurrencyCode().equals( RsetTools.getString( rs, "CODE_CURRENCY_RESULT" ) )
                                    ) );
                                log.debug( "price " + ( !item.getResultPrice().equals( RsetTools.getBigDecimal( rs, "PRICE_RESULT" ) ) ));
                                log.debug( "count " + ( item.getCountItem() != RsetTools.getInt( rs, "COUNT" ) ) );
                                log.debug( "currency " + !item.getResultCurrency().getCurrencyCode().equals( RsetTools.getString( rs, "CODE_CURRENCY_RESULT" ) ) );
                            }

                            if( log.isDebugEnabled() )
                                controllLoop( 31, invoice);

                            // «аказ найден, сравниваем данные
                            if ( !item.getResultPrice().equals( RsetTools.getBigDecimal( rs, "PRICE_RESULT" )) ||
                                item.getCountItem() != RsetTools.getInt( rs, "COUNT" ) ||
                                !item.getResultCurrency().getCurrencyCode().equals( RsetTools.getString( rs, "CODE_CURRENCY_RESULT" ) )
                                ) {
                                
                                if( log.isDebugEnabled() ) {
                                    controllLoop( 32, invoice);
                                }

                                if( log.isDebugEnabled() ) {
                                    log.debug( "item in invoice not equals with item in DB" );

                                    log.debug( "item price " + item.getResultPrice() );
                                    log.debug( "db price " + RsetTools.getBigDecimal( rs, "PRICE_RESULT" ) );
                                    log.debug( "item count " + item.getCountItem() );
                                    log.debug( "db count " + RsetTools.getInt( rs, "COUNT" ) );
                                    log.debug( "item currency code " + item.getResultCurrency().getCurrencyCode() );
                                    log.debug( "item currency code " + RsetTools.getString( rs, "CODE_CURRENCY_RESULT" ) );
                                }

                                PreparedStatement ps1 = null;
                                try {
                                    String sqlDel =
                                        "delete from WM_PRICE_ORDER where ID_ORDER_V2=?";

                                    ps1 = dbDyn.prepareStatement( sqlDel );
                                    RsetTools.setLong( ps1, 1, invoice.getOrderId() );

                                    ps1.executeUpdate();
                                }
                                finally {
                                    DatabaseManager.close( ps1 );
                                }

                                if( log.isDebugEnabled() )
                                    controllLoop( 33, invoice);

                                if( log.isDebugEnabled() )
                                    log.debug( "success delete item from db" );

                                OrderLogic.addItem( dbDyn, invoice.getOrderId(), item );

                                if( log.isDebugEnabled() )
                                    controllLoop( 34, invoice);

                            }

                            if( log.isDebugEnabled() ) {
                                log.debug( "do continue" );
                                log.debug( "item idItem " + item.getShopItem().getItemId() + " inDb " + item.getInDb() );
                            }

                            if( log.isDebugEnabled() ) {
                                controllLoop( 35, invoice);
                            }

                            break;
                        }

                        if( log.isDebugEnabled() ) {
                            controllLoop( 4, invoice);
                        }
                    }
                }
            }

            if( log.isDebugEnabled() ) {
                controllLoop( 5, invoice);
            }

            // write to db all items, which not exists in db
            for (ShopOrder shopOrder : invoice.getShopOrders()) {
                for (ShopOrderItem item : shopOrder.getShopOrderItems()) {
                    if( log.isDebugEnabled() ) {
                        log.debug( "item object - " + item );
                        log.debug( "item idItem " + item.getShopItem().getItemId() );
                        log.debug( "item inDb - " + item.getInDb() );
                    }

                    if( Boolean.FALSE.equals( item.getInDb() ) ) {
                        if( log.isDebugEnabled() ) {
                            log.debug( "item not in db " + item.getShopItem().getItemId() );
                        }

                        OrderLogic.addItem( dbDyn, invoice.getOrderId(), item );
                        item.setInDb( true );
                    }
                }
            }

        }
        catch( Exception e ) {
            log.error( "Error synchronize invoice with DB", e );
            throw e;
        }
        finally {
            DatabaseManager.close( rs, ps );
        }
    }
}