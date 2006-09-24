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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.commerce.bean.CurrencyCurrentCurs;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.portlet.schema.price.CurrencyCurrentCursType;
import org.riverock.portlet.schema.price.CustomCurrencyItemType;
import org.riverock.portlet.schema.price.CustomCurrencyType;
import org.riverock.portlet.schema.price.StandardCurrencyItemType;
import org.riverock.portlet.schema.price.StandardCurrencyType;

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

    public static CurrencyCurrentCursType getCurrentCurs( DatabaseAdapter db_, Long idCurrency, Long idSite ) {
        CurrencyCurrentCurs curs = CommerceDaoFactory.getCommonCurrencyDao().getCurrentCurs(db_, idCurrency, idSite);
        if (curs==null) {
            return null;
        }
        CurrencyCurrentCursType bean = new CurrencyCurrentCursType();
        bean.setIdCurrency(idCurrency);
        bean.setDateChange(curs.getDate());
        bean.setCurs(curs.getCurs().doubleValue());

        return bean;
    }

    public static CurrencyCurrentCursType getStandardCurrencyCurs( DatabaseAdapter db_, Long idStandardCurrency ) {
        CurrencyCurrentCurs curs = CommerceDaoFactory.getCommonCurrencyDao().getStandardCurrencyCurs(db_, idStandardCurrency);
        if (curs==null) {
            return null;
        }
        CurrencyCurrentCursType bean = new CurrencyCurrentCursType();
        bean.setIdCurrency(idStandardCurrency);
        bean.setDateChange(curs.getDate());
        bean.setCurs(curs.getCurs().doubleValue());

        return bean;
    }

    public static StandardCurrencyType getStandardCurrencyList( DatabaseAdapter db_ ) throws PriceException {

        String sql_ =
            "SELECT ID_STD_CURR, NAME_STD_CURR, CONVERT_CURRENCY, IS_DELETED " +
            "FROM   WM_CASH_CURRENCY_STD ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        StandardCurrencyType list = new StandardCurrencyType();
        try {
            ps = db_.prepareStatement( sql_ );

            rs = ps.executeQuery();

            List<StandardCurrencyItemType> v = new ArrayList<StandardCurrencyItemType>();
            while( rs.next() ) {
                StandardCurrencyItemType currency = new StandardCurrencyItemType();

                currency.setCurrencyCode( RsetTools.getString( rs, "CONVERT_CURRENCY" ) );
                currency.setCurrencyName( RsetTools.getString( rs, "NAME_STD_CURR" ) );
                currency.setIdStandardCurrency( RsetTools.getLong( rs, "ID_STD_CURR" ) );

                currency.setCurrentCurs( getStandardCurrencyCurs( db_, currency.getIdStandardCurrency() ) );

                v.add( currency );
            }
            list.setStandardCurrencyList( ( ArrayList ) v );
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
        return list;
    }

    public static CustomCurrencyItemType getCurrencyItemByCode( CustomCurrencyType list, String nameCurrency ) {
        if( log.isDebugEnabled() )
            log.debug( "list " + list + ", nameCurrency " + nameCurrency );

        if( nameCurrency == null || list == null )
            return null;


        for( int i = 0; i < list.getCurrencyListCount(); i++ ) {
            CustomCurrencyItemType item = list.getCurrencyList( i );

            if( log.isDebugEnabled() )
                log.debug( "nameCurrency " + nameCurrency + ", item.getCurrencyCode() " + item.getCurrencyCode() );

            if( item.getCurrencyCode() != null && item.getCurrencyCode().equals( nameCurrency ) )
                return item;
        }
        return null;
    }

    public static CustomCurrencyItemType getCurrencyItem( CustomCurrencyType list, Long idCurrency ) {
        if( list == null || idCurrency == null )
            return null;

        for( int i = 0; i < list.getCurrencyListCount(); i++ ) {
            CustomCurrencyItemType item = list.getCurrencyList( i );

            if( idCurrency.equals( item.getIdCurrency() ) )
                return item;
        }
        return null;
    }

    public static StandardCurrencyItemType getStandardCurrencyItem( CustomCurrencyType list, Long idCurrency ) {
        if( list == null || idCurrency == null || list.getStandardCurrencyList() == null )
            return null;

        for( int i = 0; i < list.getStandardCurrencyList().getStandardCurrencyListCount(); i++ ) {
            StandardCurrencyItemType item =
                list.getStandardCurrencyList().getStandardCurrencyList( i );

            if( idCurrency.equals( item.getIdStandardCurrency() ) )
                return item;
        }
        return null;
    }
}
