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

 * $Id$

 */

package org.riverock.portlet.price;



import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.Vector;



import org.riverock.common.tools.NumberTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.portlet.schema.price.CurrencyPrecisionType;

import org.riverock.portlet.schema.price.OrderType;

import org.riverock.portlet.schema.price.ShopOrderType;

import org.riverock.portlet.schema.price.OrderItemType;

import org.riverock.portlet.portlets.model.OrderLogic;

import org.riverock.sso.a3.AuthSession;



import org.apache.log4j.Logger;



public class PriceList

{

    private static Logger cat = Logger.getLogger( PriceList.class );



    public PriceList()

    {

    }



/*

    public String shopCode = "";

    public Vector items; //



    protected void finalize() throws Throwable

    {

        shopCode = null;

        if (items != null)

        {

            items.clear();

            items = null;

        }



        super.finalize();

    }



    public void setItem(Vector v_)

    {

        items = v_;

    }



    public Vector getItem()

    {

        return items;

    }





// Эти методы нужны для преобразования XML файла в класс.

    private final String charset = "Cp1251"; // Default - russian win charset



    public void setShopCode(String name_)

        throws java.io.UnsupportedEncodingException

    {

        if (name_ == null)

        {

            if (cat.isDebugEnabled())

                cat.debug("#111.00 is null");



            shopCode = "";

            return;

        }

        if (cat.isDebugEnabled())

        {

            cat.debug("#111.01 " + name_);

            cat.debug("#111.02 " + name_.toUpperCase());

        }



        shopCode = StringTools.convertString(

            name_,

            System.getProperties().getProperty("file.encoding"),

            charset

        ).toUpperCase();



        if (cat.isDebugEnabled())

            cat.debug("#111.03 " + shopCode);



    }



    public String getShopCode()

    {

        return shopCode;

    }

*/



    public static double calcPrice(double priceItem, Object object[])

    {



        if (cat.isDebugEnabled())

        {

            cat.debug("calc price. price for round " + priceItem);

            cat.debug("calc price: len=" + object.length);

        }



        // Ищем MainUserInfo для расчета скидки пользователя

        double price = priceItem;



        Object obj = null;

        AuthSession auth = null;

        Shop shop = null;

        int precision = 2;



        for (int i = 0; i < object.length; i++)

        {

            obj = object[i];



            if (cat.isDebugEnabled())

                cat.debug("calc price #1: obj[" + i + "]=" + obj);



            if ((obj != null) && (obj instanceof AuthSession))

            {

                auth = (AuthSession) obj;



            }

            else if ((obj != null) && (obj instanceof Shop))

            {

                shop = (Shop) obj;

            }

            else if ((obj != null) && (obj instanceof CurrencyPrecisionType))

            {

                if (cat.isDebugEnabled())

                    cat.debug("Redefine default value of precision");



                precision = ((CurrencyPrecisionType) obj).getPrecision().intValue();

            }

        }



        if (auth != null)

        {

            if (cat.isDebugEnabled())

                cat.debug("calc price: user discount=" + auth.getUserInfo().getDiscount());



            if (auth.getUserInfo().getDiscount()!=null)

                price = price * (100 - auth.getUserInfo().getDiscount().doubleValue()) / 100;

        }



        // производим округление

        if (shop != null)

        {

            if (cat.isDebugEnabled())

            {

                cat.debug("calc price: shop comma=" + precision);

                cat.debug("calc price. shop.discount - " + shop.discount);



            }



            price = price * (100 - shop.discount) / 100;

            if (cat.isDebugEnabled())

                cat.debug("calc price. price - " + price + " shop.commas - " + precision);



            price = NumberTools.truncate(price, precision);



            if (cat.isDebugEnabled())

                cat.debug("calc price. price - " + price);



        }

        return price;

    }



// Расчет значения итоговой суммы для заказа

    public static double calcSummPrice(Vector v, Object object[])

    {



        if (cat.isDebugEnabled())

            cat.debug("calc price: len=" + object.length);



        // Ищем MainUserInfo для расчета скидки пользователя

        Object obj = null;

        double discountUser = 0;

        AuthSession auth = null;

        Shop shop = null;

        int precision = 2;



        for (int i = 0; i < object.length; i++)

        {

            obj = object[i];



            if (cat.isDebugEnabled())

                cat.debug("calc price #1: obj[" + i + "]=" + obj);



            if ((obj != null) && (obj instanceof AuthSession))

            {

                auth = (AuthSession) obj;

            }

            else if ((obj != null) && (obj instanceof Shop))

            {

                shop = (Shop) obj;

            }

            else if ((obj != null) && (obj instanceof CurrencyPrecisionType))

            {

                if (cat.isDebugEnabled())

                    cat.debug("Redefine default value of precision");



                precision = ((CurrencyPrecisionType) obj).getPrecision().intValue();

            }

        }



        if (auth != null)

        {

            if (cat.isDebugEnabled())

                cat.debug("calc price: user discount=" + auth.getUserInfo().getDiscount());



            if (auth.getUserInfo().getDiscount()!=null)

                discountUser = (100 - auth.getUserInfo().getDiscount().doubleValue()) / 100;

        }



        double discountShop = 0;

        if (shop != null)

        {

            discountShop = shop.discount;

        }



        // производим округление

        double summOrder = 0;

        for (int i = 0; i < v.size(); i++)

        {

            obj = v.elementAt(i);



            if (cat.isDebugEnabled())

                cat.debug("calc price #2: obj[" + i + "]=" + obj);



            if ((obj != null) && (obj instanceof PriceListItemOrder))

            {

                PriceListItemOrder item = (PriceListItemOrder) obj;



                if (cat.isDebugEnabled())

                    cat.debug("calc price: shop comma=" + precision);



                double itemPrice =

                    NumberTools.truncate(item.priceItem, precision);



                itemPrice = itemPrice * (100 - discountUser) / 100;



                itemPrice = itemPrice * (100 - discountShop) / 100;



                summOrder +=

                    NumberTools.truncate(itemPrice, precision) *

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



                cat.debug(sql_);



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

            cat.error("Error get special items", e);

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

        long id_shop, int currencyID, String serverName,

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



            long idSite = SiteListSite.getIdSite(serverName);



            ps = db_.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, idSite);

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



    private static void controllLoop( int idx, OrderType order)

    {

        if (cat.isDebugEnabled())

        {

            cat.debug(" Контрольный цикл "+idx);

            for (int i = 0; i < order.getShopOrdertListCount(); i++)

            {

                ShopOrderType shopOrder = order.getShopOrdertList(i);

                for (int k = 0; k < shopOrder.getOrderItemListCount(); k++)

                {

                    OrderItemType item = shopOrder.getOrderItemList(k);

                    cat.debug("id_item - "+item.getIdItem()+", isInDb - " + item.getIsInDb());

                }

            }

            cat.debug("");

        }

    }



    public static void synchronizeOrderWithDb(DatabaseAdapter dbDyn, OrderType order)

        throws Exception

    {

        if (!dbDyn.isDynamic())

            throw new Exception("Error synchronize order with DB. DB connection is not dynamic");



        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            String sql_ =

                "select a.* from PRICE_ORDER_V2 a where a.ID_ORDER_V2=?";



            ps = dbDyn.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, order.getIdOrder());



            rs = ps.executeQuery();



            // Перед проверкой устанавливаем все наименования как отсутствующие в базе

            for (int i = 0; i < order.getShopOrdertListCount(); i++)

            {

                ShopOrderType shopOrder = order.getShopOrdertList(i);

                for (int k = 0; k < shopOrder.getOrderItemListCount(); k++)

                {

                    OrderItemType item = shopOrder.getOrderItemList(k);

                    item.setIsInDb( Boolean.FALSE );

                }

            }



            while (rs.next())

            {

                if (cat.isDebugEnabled())

                    cat.debug("do get from ResultSet ");



                Long idItem = RsetTools.getLong(rs, "ID_ITEM");



                if (cat.isDebugEnabled())

                    cat.debug("Check item from DB with idItem - "+idItem);



                // ищем в заказе из сессии товар, который есть в базе

                for (int i = 0; i < order.getShopOrdertListCount(); i++)

                {

                    ShopOrderType shopOrder = order.getShopOrdertList(i);



                    if (cat.isDebugEnabled())

                        controllLoop( 1, order);



                    if (cat.isDebugEnabled())

                    {

                        cat.debug("check shop. idShop  - " + shopOrder.getIdShop());

                        cat.debug("Count of items in shopOrder  - " + shopOrder.getOrderItemListCount());

                    }



                    for (int k = 0; k < shopOrder.getOrderItemListCount(); k++)

                    {

                        OrderItemType item = shopOrder.getOrderItemList(k);



                        if (cat.isDebugEnabled())

                        {

                            cat.debug("item object - " + item);

                            cat.debug("item isInDb - " + item.getIsInDb());

                            cat.debug("check item with id - " + idItem);

                            cat.debug("status compare id_item - "+(item.getIdItem() == idItem));

                        }



                        if (cat.isDebugEnabled())

                            controllLoop( 2, order);



                        if (item.getIdItem() == idItem)

                        {

                            item.setIsInDb( Boolean.TRUE );



                            if (cat.isDebugEnabled())

                                controllLoop( 3, order);



                            if (cat.isDebugEnabled())

                            {

                                cat.debug("item in order. check values of order");

                                cat.debug("item price "+item.getPriceItemResult());

                                cat.debug("db price "+RsetTools.getDouble(rs, "PRICE_RESULT"));

                                cat.debug("init count "+item.getCountItem());

                                cat.debug("db count "+RsetTools.getInt(rs, "COUNT"));

                                cat.debug("item code "+item.getResultCurrency().getCurrencyCode());

                                cat.debug("db code "+RsetTools.getString(rs, "CODE_CURRENCY_RESULT"));

                                cat.debug("status of compare order item data "+(item.getPriceItemResult() != RsetTools.getDouble(rs, "PRICE_RESULT") ||

                                    (item.getCountItem() != RsetTools.getInt(rs, "COUNT")) ||

                                    !item.getResultCurrency().getCurrencyCode().equals(RsetTools.getString(rs, "CODE_CURRENCY_RESULT"))

                                    )

                                );

                                cat.debug("price "+(item.getPriceItemResult() != RsetTools.getDouble(rs, "PRICE_RESULT")));

                                cat.debug("count "+(item.getCountItem() != RsetTools.getInt(rs, "COUNT")));

                                cat.debug("currency "+!item.getResultCurrency().getCurrencyCode().equals(RsetTools.getString(rs, "CODE_CURRENCY_RESULT")));

                            }



                            if (cat.isDebugEnabled())

                                controllLoop( 31, order);



                            // Заказ найден, сравниваем данные

                            if (item.getPriceItemResult() != RsetTools.getDouble(rs, "PRICE_RESULT") ||

                                item.getCountItem() != RsetTools.getInt(rs, "COUNT") ||

                                !item.getResultCurrency().getCurrencyCode().equals(RsetTools.getString(rs, "CODE_CURRENCY_RESULT"))

                            )

                            {

                                if (cat.isDebugEnabled())

                                    controllLoop( 32, order);



                                if (cat.isDebugEnabled())

                                {

                                    cat.debug("item in order not equals with item in DB");



                                    cat.debug("item price " + item.getPriceItemResult());

                                    cat.debug("db price " + RsetTools.getDouble(rs, "PRICE_RESULT"));

                                    cat.debug("item count " + item.getCountItem());

                                    cat.debug("db count " + RsetTools.getInt(rs, "COUNT"));

                                    cat.debug("item currency code " + item.getResultCurrency().getCurrencyCode());

                                    cat.debug("item currency code " + RsetTools.getString(rs, "CODE_CURRENCY_RESULT"));

                                }



                                PreparedStatement ps1 = null;

                                try

                                {

                                    String sqlDel =

                                        "delete from PRICE_ORDER_V2 a where a.ID_ORDER_V2=?";



                                    ps1 = dbDyn.prepareStatement(sqlDel);

                                    RsetTools.setLong(ps1, 1, order.getIdOrder());



                                    ps1.executeUpdate();

                                }

                                finally

                                {

                                    DatabaseManager.close(ps1);

                                    ps1 = null;

                                }



                                if (cat.isDebugEnabled())

                                    controllLoop( 33, order);



                                if (cat.isDebugEnabled())

                                    cat.debug("success delete item from db");



                                OrderLogic.addItem(dbDyn, order.getIdOrder(), item);



                                if (cat.isDebugEnabled())

                                    controllLoop( 34, order);



                            }



                            if (cat.isDebugEnabled())

                            {

                                cat.debug("do continue");

                                cat.debug("item idItem "+item.getIdItem()+" inDb "+item.getIsInDb());

                            }



                            if (cat.isDebugEnabled())

                                controllLoop( 35, order);



                            break;

                        }



                        if (cat.isDebugEnabled())

                            controllLoop( 4, order);

                    }

                }

            }



            if (cat.isDebugEnabled())

                controllLoop( 5, order);



            // записываем в базу все наименования которых нет в базе

            for (int i = 0; i < order.getShopOrdertListCount(); i++)

            {

                ShopOrderType shopOrder = order.getShopOrdertList(i);

                for (int k = 0; k < shopOrder.getOrderItemListCount(); k++)

                {

                    OrderItemType item = shopOrder.getOrderItemList(k);

                    if (cat.isDebugEnabled())

                    {

                        cat.debug("item object - " + item);

                        cat.debug("item idItem " + item.getIdItem());

                        cat.debug("item inDb - " + item.getIsInDb() );

                    }



                    if (! Boolean.TRUE.equals(item.getIsInDb()) )

                    {

                        if (cat.isDebugEnabled())

                            cat.debug("item not in db " + item.getIdItem());



                        OrderLogic.addItem(dbDyn, order.getIdOrder(), item);

                        item.setIsInDb( Boolean.TRUE );

                    }

                }

            }



        }

        catch (Exception e)

        {

            cat.error("Error synchronize order with DB", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }

    }



/*

    public static Vector getOrderItemsV2(DatabaseAdapter db_,

        String serverName,

        long id_order)

        throws PriceException

    {



        PreparedStatement ps = null;

        ResultSet rs = null;

        Vector retVector = null;



        try

        {

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



            long idSite = SiteListSite.getIdSite(serverName);



            ps = db_.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, idSite);

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



    public static Vector getPriceList(DatabaseAdapter db_,

        long id_shop,

        long currencyID,

        long id_main,

        String serverName

        )

        throws PriceException

    {



        String sql_ = null;

        PreparedStatement ps = null;

        ResultSet rs = null;

        Vector retVector = null;



        if (currencyID == 0)

            throw new PriceException("currency for this shop not defined");



        try

        {

            sql_ =

                "select * from v_b2c_items_price_currency " +

                "where ID_SITE=? and " +

                "       id_main = ? and id_shop = ? and " +

                "       id_std_currency = ? " +

                "order by id asc";



            long idSite = SiteListSite.getIdSite(serverName);



            ps = db_.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, idSite);

            RsetTools.setLong(ps, 2, id_main);

            RsetTools.setLong(ps, 3, id_shop);

            RsetTools.setLong(ps, 4, currencyID);



            rs = ps.executeQuery();



            retVector = new Vector(25, 10);

            while (rs.next())

                retVector.add(new PriceListItem(currencyID, rs));



        }

        catch (Exception e)

        {

            cat.error("Error getPriceList", e);

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

}