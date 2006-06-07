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

    private static CacheFactory cache = new CacheFactory( CurrencyList.class.getName() );

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
            db_ = null;
        }
        fillRealCurrencyData( list );
    }
}
