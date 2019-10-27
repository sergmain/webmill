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

import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.commerce.bean.CustomCurrency;
import org.riverock.commerce.bean.Currency;
import org.riverock.commerce.dao.CommerceDaoFactory;

/**
 * User: serg_main
 * Date: 06.02.2004
 * Time: 17:07:00
 *
 * @author Serge Maslyukov
 *         $Id: CurrencyManager.java 1138 2006-12-12 18:37:24Z serg_main $
 */
public class CurrencyManager {
    private static Logger log = Logger.getLogger(CurrencyManager.class);

    private CustomCurrency currencyList = null;

    public CustomCurrency getCurrencyList() {
        return currencyList;
    }

    public CurrencyManager() {
    }

    public static CustomCurrency getCustomCurrencies(Long idSite) {
        CustomCurrency list = new CustomCurrency();
        List<Currency> currList = CommerceDaoFactory.getCurrencyDao().getCurrencyList(idSite);
        for (Currency item : currList) {
            list.getCurrencies().add( new CurrencyItem(item) );
            list.setStandardCurrencies( CommerceDaoFactory.getStandardCurrencyDao().getStandardCurrencyList() );
        }
        for (CurrencyItem item : list.getCurrencies()) {
            item.fillRealCurrencyData( list.getStandardCurrencies() );
        }
        return list;
    }

    public static CurrencyManager getInstance(Long idSite) throws PriceException {
        CurrencyManager currencyManager = new CurrencyManager();
        currencyManager.currencyList = getCustomCurrencies(idSite);

        long mills = 0; // System.currentTimeMillis();

        if (log.isInfoEnabled())
            mills = System.currentTimeMillis();

        if (log.isDebugEnabled()) {
            log.debug("CurrencyList: " + currencyManager.currencyList);
        }

        if (log.isInfoEnabled()) {
            log.info("init currency manager list for " + (System.currentTimeMillis() - mills) + " milliseconds");
        }
        return currencyManager;
    }

    public void reinit() {
    }

    public void terminate(java.lang.Long id_) {
    }

}
