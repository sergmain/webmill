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

import org.apache.log4j.Logger;

import org.riverock.commerce.bean.price.CustomCurrencyType;
import org.riverock.commerce.bean.price.WmCashCurrencyItemType;
import org.riverock.commerce.bean.price.WmCashCurrencyListType;
import org.riverock.commerce.dao.GetWmCashCurrencyWithIdSiteList;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;

/**
 * Author: mill
 * Date: Dec 9, 2002
 * Time: 11:41:02 AM
 *
 * $Id$
 */
public class CurrencyList {
    private static Logger log = Logger.getLogger( CurrencyList.class );

    public CustomCurrencyType list = new CustomCurrencyType();

    public CurrencyList() {
    }

    public static CurrencyList getInstance(Long id__) throws PriceException {
        try {
            return new CurrencyList(id__);
        } catch (Throwable e) {
            String es = "Error in getInstance( Long id__)";
            log.error(es, e);
            throw new PriceException(es, e);
        }
    }

    private static void fillRealCurrencyData( CustomCurrencyType currency ) throws Exception {
        if (currency==null) {
            return;
        }

        for (CurrencyItem item : currency.getCurrencyList()) {
            item.fillRealCurrencyData( currency.getStandardCurrencyList() );
        }
    }

    public CurrencyList(Long idSite) throws Exception {
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            WmCashCurrencyListType currList = GetWmCashCurrencyWithIdSiteList.getInstance(db_, idSite).item;
            for (WmCashCurrencyItemType item : currList.getWmCashCurrencyList()) {
                list.getCurrencyList().add( new CurrencyItem(db_, item) );
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
