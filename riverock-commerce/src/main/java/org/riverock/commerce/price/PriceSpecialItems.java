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
import java.util.List;
import java.util.ArrayList;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.common.tools.RsetTools;

/**
 *
 *  $Id$
 *
 */

public class PriceSpecialItems
{

    public List items = new ArrayList(4);

    protected void finalize() throws Throwable
    {
        if (items != null)
        {
            items.clear();
            items = null;
        }

        super.finalize();
    }

    public PriceSpecialItems()
    {
    }

    public PriceSpecialItems(DatabaseAdapter db_, Long id_shop, Long siteId)
            throws PriceException
    {

        String sql_ = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        sql_ =
                "select a.id_item, a.id_shop, a.id, a.id_main, a.item, a.price, " +
                "	a.currency, e.name_currency, a.quantity " +
                "from 	WM_PRICE_LIST a, cash_currency e " +
                "where	e.ID_SITE=? and " +
                "	a.currency = e.currency and a.id_shop = ? and a.is_special = 1 and " +
                "	a.absolete = 0 and a.is_group=0";

        try
        {
            ps = db_.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, siteId);
            RsetTools.setLong(ps, 2, id_shop);

            rs = ps.executeQuery();


//            while (rs.next())
//                items.add(new PriceListItemExtend(db_, rs, true));


        }
        catch (Exception e)
        {
            throw new PriceException(e.toString());
        }
        finally
        {
            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (Exception e01)
                {
                }
            }
            if (ps != null)
            {
                try
                {
                    ps.close();
                }
                catch (Exception e02)
                {
                }
            }
        }
    }


    public static void set(DatabaseAdapter db_, Long id_shop, Long id_item)
            throws PriceException
    {

        PreparedStatement ps = null;

        try
        {
            ps = db_.prepareStatement(
                    "update WM_PRICE_LIST a " +
                    "set    is_special = 1 " +
                    "where  a.id_shop=? and a.id_item=? "
            );

            RsetTools.setLong(ps, 1, id_shop);
            RsetTools.setLong(ps, 2, id_item);

            ps.executeUpdate();

        }
        catch (Exception e1)
        {
            throw new PriceException(e1.toString());
        }
        finally
        {
            if (ps != null)
            {
                try
                {
                    ps.close();
                    ps = null;
                }
                catch (Exception e)
                {
                }
            }
        }
    }
}