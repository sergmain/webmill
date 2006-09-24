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