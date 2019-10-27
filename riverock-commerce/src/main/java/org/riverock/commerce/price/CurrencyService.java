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
import org.hibernate.Session;

import org.riverock.commerce.bean.CurrencyCurs;
import org.riverock.commerce.bean.CustomCurrency;
import org.riverock.commerce.bean.StandardCurrency;
import org.riverock.commerce.bean.StandardCurrencyCurs;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.tools.HibernateUtils;

/**
 * Author: mill
 * Date: Dec 9, 2002
 * Time: 11:41:02 AM
 * <p/>
 * $Id: CurrencyService.java 1138 2006-12-12 18:37:24Z serg_main $
 */
@SuppressWarnings({"UnusedAssignment"})
public final class CurrencyService {
    private final static Logger log = Logger.getLogger( CurrencyService.class );

    public static CurrencyCurs getCurrentCurs( Long idCurrency, Long idSite ) {
        return CommerceDaoFactory.getCommonCurrencyDao().getCurrentCurs(idCurrency, idSite);
    }

    public static StandardCurrencyCurs getStandardCurrencyCurs( Long idStandardCurrency ) {
        return CommerceDaoFactory.getCommonCurrencyDao().getStandardCurrencyCurs(idStandardCurrency);
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
