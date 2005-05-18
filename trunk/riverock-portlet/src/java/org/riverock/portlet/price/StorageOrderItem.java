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

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.common.tools.RsetTools;

import org.apache.log4j.Logger;

/**
 *
 *  $Id$
 *
 */
public final class StorageOrderItem {
    private final static Logger log = Logger.getLogger(StorageOrderItem.class);
    Long itemID;
    long quantity;

    public void save(DatabaseAdapter db_, long id_order)
            throws PriceException
    {
        PreparedStatement st = null;
        boolean retFlag = false;
        try
        {
            st = db_.prepareStatement(
                    "insert into PRICE_STORAGE_ORDER_items " +
                    "( ID_PRICE_STORAGE_ORDER_ITEMS, id, quantity, ID_PRICE_STORAGE_ORDER ) " +
                    "( select seq_PRICE_STORAGE_ORDER_items.nextval, ?, ?, ? from dual ) "
            );

            st.setObject(1, itemID);
            st.setLong(2, quantity);
            st.setLong(3, id_order);

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
    }

    public void update(DatabaseAdapter db_, Long idPK)
            throws PriceException
    {
        PreparedStatement st = null;
        try
        {
            st = db_.prepareStatement(
                    "update PRICE_STORAGE_ORDER_items " +
                    "set quantity =? " +
                    "where ID_PRICE_STORAGE_ORDER_ITEMS = ? "
            );

            st.setLong(1, quantity);
            st.setObject(2, idPK);

            st.executeUpdate();
        }
        catch (Exception e)
        {
            String es = "Error in update()";
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

    /**
     ѕровер€ем все ли наименовани€ идентичны. ≈сли нет, то замен€ем на новые значени€
     и устанавливаем is_processed в 0;
     */
    public boolean checkItem(DatabaseAdapter db_, long id_order)
            throws PriceException
    {
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean retFlag = false;
        try
        {
            st = db_.prepareStatement(
                    "select quantity, ID_PRICE_STORAGE_ORDER_ITEMS " +
                    "from PRICE_STORAGE_ORDER_items " +
                    "where ID_PRICE_STORAGE_ORDER=? and id = ? "
            );
            st.setLong(1, id_order);
            st.setObject(2, itemID);
            rs = st.executeQuery();

            if (rs.next())
            {
                Long quant = RsetTools.getLong(rs, "quantity", new Long(0) );
                Long idPK = RsetTools.getLong(rs, "ID_PRICE_STORAGE_ORDER_ITEMS");

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
                        st  = null;
                    }
                    catch (Exception e02)
                    {
                    }
                }
                if (quant.longValue() != quantity)
                {
                    update(db_, idPK);
                    retFlag = true;
                }
            }
            else
            {
                save(db_, id_order);
                retFlag = true;
            }

        }
        catch (Exception e)
        {
            String es = "Error in checkItem()";
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
                    st  = null;
                }
                catch (Exception e02)
                {
                }
            }
        }
        return retFlag;
    }

    public void print(PrintStream out)
    {

        out.println("StorageOrderItem: ");
        out.println("\t\titemID\t-> " + itemID );
        out.println("\t\tqwuantity\t-> " + quantity);

    }
}