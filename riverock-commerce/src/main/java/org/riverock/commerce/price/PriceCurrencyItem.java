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

import java.sql.ResultSet;

import org.riverock.common.tools.RsetTools;

/**
 * $Id$
 */

public class PriceCurrencyItem
{
    public Long currencyID;
    public String currencyName;
    public double currencyCurs;
    public String selectCurrentCurrency= "";

    protected void finalize() throws Throwable
    {
        currencyName = null;

        super.finalize();
    }

    public PriceCurrencyItem(ResultSet rs)
            throws PriceException
    {
        set(rs);
    }

    public PriceCurrencyItem(){}

    public void set(ResultSet rs)
            throws PriceException
    {
        try
        {
            currencyID = RsetTools.getLong(rs, "id_currency");
            currencyName = RsetTools.getString(rs, "currency_name");
            currencyCurs = RsetTools.getDouble(rs, "currency_curs").doubleValue();
        }
        catch (Exception e)
        {
            throw new PriceException(e.toString());
        }
    }
}