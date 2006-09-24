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

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.main.CacheFactory;
import org.riverock.portlet.core.GetWmCashCurrencyWithIdSiteList;
import org.riverock.portlet.schema.core.WmCashCurrencyItemType;
import org.riverock.portlet.schema.core.WmCashCurrencyListType;
import org.riverock.portlet.schema.price.CustomCurrencyType;
import org.riverock.sql.cache.SqlStatement;

/**
 * Author: mill
 * Date: Dec 9, 2002
 * Time: 11:41:02 AM
 *
 * $Id$
 */
public class CurrencyList {
    private static Logger log = Logger.getLogger( CurrencyList.class );

    private static CacheFactory cache = new CacheFactory( CurrencyList.class);

    public CustomCurrencyType list = new CustomCurrencyType();

    public CurrencyList() {
    }

    public void reinit()
    {
        cache.reinit();
    }

    public static CurrencyList getInstance(Long id__) throws PriceException {
        try {
            return (CurrencyList) cache.getInstanceNew(id__);
        } catch (Throwable e) {
            String es = "Error in getInstance( Long id__)";
            log.error(es, e);
            throw new PriceException(es, e);
        }
    }

    private static void fillRealCurrencyData( CustomCurrencyType currency ) throws Exception {
        if (currency==null)
            return;

        for (int i=0; i< currency.getCurrencyListCount(); i++) {
            CurrencyItem item = (CurrencyItem)currency.getCurrencyList( i );
            item.fillRealCurrencyData( currency.getStandardCurrencyList() );
        }
    }

    static {
        try {
            SqlStatement.registerRelateClass( CurrencyList.class, GetWmCashCurrencyWithIdSiteList.class );
        }
        catch (Exception exception) {
            log.error("Exception in ", exception);
        }
    }

    public CurrencyList(Long idSite) throws Exception {
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            WmCashCurrencyListType currList = GetWmCashCurrencyWithIdSiteList.getInstance(db_, idSite).item;
            for (int i=0; i<currList.getWmCashCurrencyCount(); i++)
            {
                WmCashCurrencyItemType item = currList.getWmCashCurrency(i);
                CurrencyItem currency = new CurrencyItem(db_, item);
                list.addCurrencyList(currency);
                list.setStandardCurrencyList( CurrencyService.getStandardCurrencyList(db_) );
            }
        }
        catch (Error e) {
            log.error("Error get currency list", e);
            throw e;
        }
        catch (Exception e) {
            log.error("Exception get currency list", e);
            throw e;
        }
        finally {
            DatabaseManager.close(db_);
        }
        fillRealCurrencyData( list );
    }
}
