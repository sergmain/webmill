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

package org.riverock.portlet.price;

import java.io.PrintStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Vector;

import org.riverock.generic.db.DatabaseAdapter;

import org.apache.log4j.Logger;

public final class StorageOrder {
    private final static Logger log = Logger.getLogger( StorageOrder.class );
    String dateOrder = "";
    String numberOrder;
    long orderID;

    Vector items = new Vector();

    protected void finalize() throws Throwable
    {
        dateOrder= null;
        numberOrder = null;

        if (items != null)
        {
            items.clear();
            items = null;
        }

        super.finalize();
    }

    public static void setIsProcessed(DatabaseAdapter db_, long id_order, boolean isProcessed)
            throws PriceException
    {
        PreparedStatement st = null;
        boolean retFlag = false;
        try
        {
            st = db_.prepareStatement(
                    "update PRICE_STORAGE_ORDER " +
                    "set is_processed = ? " +
                    "where ID_PRICE_STORAGE_ORDER = ? "
            );

            st.setInt(1, (isProcessed?1:0));
            st.setLong(2, id_order);

            st.executeUpdate();
        }
        catch (Exception e)
        {
            throw new PriceException(e.toString());
        }
        finally
        {
            if (st != null)
            {
                try
                {
                    st.close();
                    st = null;
                }
                catch (Exception e02)
                {
                }
            }
        }
    }

    /**
     Возвращает значение первичного ключа если в базе уже есть такая накладная,
     или null
     */
    public Long checkOrderInDB(DatabaseAdapter db_, long id_shop)
        throws PriceException
    {
        PreparedStatement st = null;
        ResultSet rs = null;
        Long retID = null;
        try
        {
            st = db_.prepareStatement(
                    "select id_PRICE_STORAGE_ORDER from PRICE_STORAGE_ORDER " +
                    "where id_shop=? and date_order=to_date(?,'dd.mm.yyyy') and " +
                    "order_id = ? "
            );
            st.setLong(1, id_shop);
            st.setString(2, dateOrder);
            st.setLong(3, orderID);
            rs = st.executeQuery();

            if (rs.next())
                retID = new Long(rs.getLong("id_PRICE_STORAGE_ORDER"));

        }
        catch (Exception e)
        {
            String es = "Error in checkOrderInDB()";
            log.error( es, e );
            throw new PriceException(es, e);
        }
        finally
        {
            if (rs != null)
            {
                try
                {
                    rs.close();
                    rs = null;
                }
                catch (Exception e01)
                {
                }
            }
            if (st != null)
            {
                try
                {
                    st.close();
                    st = null;
                }
                catch (Exception e02)
                {
                }
            }
        }
        return retID;
    }

    /**
     Записываем в базу данные накладной. Возвращаем значение первичного
     ключа для данной накладной.
     */
    public Long save(DatabaseAdapter db_, long id_shop)
            throws PriceException
    {
        PreparedStatement st = null;
        boolean retFlag = false;
        long idPK;
        try
        {
            if (true) throw new PriceException("not implemented");

//            idPK = db_.getSequenceNextValue("seq_PRICE_STORAGE_ORDER");

            st = db_.prepareStatement(
                    "insert into PRICE_STORAGE_ORDER " +
                    "(id_price_storage_order, id_shop, date_order, number_order, " +
                    " order_id, is_processed )" +
                    " values " +
                    "( ?, ?, ?, ?, ?, 0) "
            );
            st.setLong(1, idPK);
            st.setLong(2, id_shop);
            st.setString(3, dateOrder);
            st.setString(4, numberOrder);
            st.setLong(5, orderID);

            st.executeUpdate();

        }
        catch (Exception e)
        {
            String es = "Error in save()";
            log.error( es, e );
            throw new PriceException(es, e);
        }
        finally
        {
            if (st != null)
            {
                try
                {
                    st.close();
                    st = null;
                }
                catch (Exception e02)
                {
                }
            }
        }
        return new Long(idPK);
    }

    public void process(DatabaseAdapter db_, long id_shop)
            throws PriceException
    {
        Long idPK = checkOrderInDB(db_, id_shop);
        if (idPK == null)
            idPK = save(db_, id_shop);

        if (idPK == null)
            throw new PriceException("PK not found");

        StorageOrderItem orderItem;

        boolean flag = false;
        for (int i = 0; i < items.size(); i++)
        {
            orderItem = (StorageOrderItem) items.elementAt(i);
            if (orderItem.checkItem(db_, idPK.longValue()))
                flag = true;
        }

        if (flag)
            setIsProcessed(db_, idPK.longValue(), false);

        calcQuantity(db_, id_shop);
    }

    public static void calcQuantity(DatabaseAdapter db_, long id_shop)
        throws PriceException
    {
        PreparedStatement st = null;
        try
        {
            st = db_.prepareStatement(
                    "insert into price_list " +
                    "(id_item, id_shop, is_group, id, id_main, absolete, quantity) " +
                    "(select seq_price_list.nextval, id_shop, 0, id, 0, 1, summ " +
                    "from( " +
                    "    select a.id_shop, b.id, sum(b.quantity) summ " +
                    "    from price_storage_order a, price_storage_order_items b " +
                    "    where a.id_shop = ? and a.id_price_storage_order=b.id_price_storage_order and  " +
                    "    id not in ( select x1.id from price_list x1 where x1.id_shop=a.id_shop) " +
                    "    group by a.id_shop, b.id " +
                    " )  " +
                    ") "
            );

            st.setLong(1, id_shop);

            st.executeUpdate();
            st.close();
            st = null;


            st = db_.prepareStatement(
                    "update price_list a " +
                    "set a.quantity =  " +
                    "( " +
                    "    select summ " +
                    "    from " +
                    "    ( " +
                    "        select a11.id_shop, b11.id, sum(b11.quantity) summ " +
                    "        from price_storage_order a11, price_storage_order_items b11 " +
                    "        where a11.id_shop = ? and a11.id_price_storage_order=b11.id_price_storage_order " +
                    "        group by a11.id_shop, b11.id " +
                    "    ) a1 " +
                    "    where  a1.id_shop = a.id_shop and a1.id=a.id " +
                    ") " +
                    "where a.id in  " +
                    "( " +
                    "    select id " +
                    "    from " +
                    "    ( " +
                    "        select a11.id_shop, b11.id " +
                    "        from price_storage_order a11, price_storage_order_items b11 " +
                    "        where a11.id_shop = ? and a11.id_price_storage_order=b11.id_price_storage_order " +
                    "    ) a1 " +
                    "    where  a1.id_shop = a.id_shop  " +
                    ") "
            );
            st.setLong(1, id_shop);
            st.setLong(2, id_shop);

            st.executeUpdate();

        }
        catch (Exception e)
        {
            String es = "Error in calcQuantity()";
            log.error( es, e );
            throw new PriceException(es, e);
        }
        finally
        {
            if (st != null)
            {
                try
                {
                    st.close();
                    st = null;
                }
                catch (Exception e02)
                {
                }
            }
        }

    }

    public void print(PrintStream out)
    {

        out.println("StorageOrder: ");
        out.println("\tdateOrder\t-> " + dateOrder);
        out.println("\tnumberOrder\t-> " + numberOrder);
        out.println("\torderID\t-> " + Long.toString(orderID));

        StorageOrderItem i;
        Enumeration e = items.elements();
        while (e.hasMoreElements())
        {
            i = (StorageOrderItem) e.nextElement();
            i.print(System.out);
        }
    }

}