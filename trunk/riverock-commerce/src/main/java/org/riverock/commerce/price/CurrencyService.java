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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.commerce.bean.CurrencyCurs;
import org.riverock.commerce.bean.StandardCurrency;
import org.riverock.commerce.bean.CustomCurrency;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;

/**
 * Author: mill
 * Date: Dec 9, 2002
 * Time: 11:41:02 AM
 * <p/>
 * $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public final class CurrencyService {
    private final static Logger log = Logger.getLogger( CurrencyService.class );

    public static CurrencyCurs getCurrentCurs( DatabaseAdapter db_, Long idCurrency, Long idSite ) {
        return CommerceDaoFactory.getCommonCurrencyDao().getCurrentCurs(db_, idCurrency, idSite);
    }

    public static CurrencyCurs getStandardCurrencyCurs( DatabaseAdapter db_, Long idStandardCurrency ) {
        return CommerceDaoFactory.getCommonCurrencyDao().getStandardCurrencyCurs(db_, idStandardCurrency);
    }

    public static List<StandardCurrency> getStandardCurrencyList( DatabaseAdapter db_ ) throws PriceException {

        String sql_ =
            "SELECT ID_STD_CURR, NAME_STD_CURR, CONVERT_CURRENCY, IS_DELETED " +
            "FROM   WM_CASH_CURRENCY_STD ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = db_.prepareStatement( sql_ );

            rs = ps.executeQuery();

            List<StandardCurrency> currencies = new ArrayList<StandardCurrency>();
            while( rs.next() ) {
                StandardCurrency currency = new StandardCurrency();

                currency.setStandardCurrencyCode( RsetTools.getString( rs, "CONVERT_CURRENCY" ) );
                currency.setStandardCurrencyName( RsetTools.getString( rs, "NAME_STD_CURR" ) );
                currency.setStandardCurrencyId( RsetTools.getLong( rs, "ID_STD_CURR" ) );

                currency.setCurrentCurs( getStandardCurrencyCurs( db_, currency.getStandardCurrencyId() ) );

                currencies.add( currency );
            }
            return currencies;
        }
        catch( Exception exc ) {
            String es = "Error getStandardCurrencyList()";
            log.error( es, exc );
            throw new PriceException( es, exc );
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
    }

    public static CurrencyItem getCurrencyItemByCode( CustomCurrency list, String nameCurrency ) {
        if( log.isDebugEnabled() ) {
            log.debug( "list " + list + ", nameCurrency " + nameCurrency );
        }

        if( nameCurrency == null || list == null ) {
            return null;
        }


        for (CurrencyItem item : list.getCurrencies()) {
            if( log.isDebugEnabled() ) {
                log.debug( "nameCurrency " + nameCurrency + ", item.getStandardCurrencyCode() " + item.getCurrencyCode() );
            }

            if( item.getCurrencyCode() != null && item.getCurrencyCode().equals( nameCurrency ) ) {
                return item;
            }
        }
        return null;
    }

    public static CurrencyItem getCurrencyItem( CustomCurrency list, Long idCurrency ) {
        if( list == null || idCurrency == null ) {
            return null;
        }

        for (CurrencyItem item : list.getCurrencies()) {
            if( idCurrency.equals( item.getCurrencyId() ) ) {
                return item;
            }
        }
        return null;
    }

    public static StandardCurrency getStandardCurrencyItem( CustomCurrency list, Long idCurrency ) {
        if( list == null || idCurrency == null || list.getStandardCurrencies() == null )
            return null;

        for (StandardCurrency item : list.getStandardCurrencies()) {
            if( idCurrency.equals( item.getStandardCurrencyId() ) ) {
                return item;
            }
        }
        return null;
    }
}
