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


import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.common.tools.RsetTools;

/**
 *
 *  $Id$
 *
 */
public class ShopBasketItems
{
    private static Logger cat = Logger.getLogger(ShopBasketItems.class);

    public Long id_item;
    public int count;
    public String item;
    public double price;
    public String currency;

    protected void finalize() throws Throwable
    {
        item = null;
        currency = null;

        super.finalize();
    }

    public ShopBasketItems(Long i, int c)
    {
        id_item = i;
        count = c;
    }

    public void initItem(DatabaseAdapter db_)
            throws PriceException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql_ = "select * from WM_PRICE_LIST where ID_ITEM = ?";

        try
        {
            ps = db_.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, id_item);

            rs = ps.executeQuery();
            if (rs.next())
            {
                item = RsetTools.getString(rs, "ITEM");
                price = RsetTools.getDouble( rs, "PRICE" );
                currency = RsetTools.getString(rs, "CURRENCY");
            }
        }
        catch (Exception e)
        {
            cat.error(e);
            throw new PriceException(e.toString());
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
            if (ps != null)
            {
                try
                {
                    ps.close();
                    ps = null;
                }
                catch (Exception e02)
                {
                }
            }
        }
    }
}