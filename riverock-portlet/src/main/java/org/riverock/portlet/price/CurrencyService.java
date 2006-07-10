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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;



import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.tools.CurrentTimeZone;
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
public final class CurrencyService {
    private final static Logger log = Logger.getLogger( CurrencyService.class );

    public static CurrencyCurrentCursType getCurrentCurs( DatabaseAdapter db_, Long idCurrency, Long idSite )
        throws PriceException {
        String sql_ =
            "select max(f.DATE_CHANGE) LAST_DATE " +
            "from   WM_CASH_CURR_VALUE f, WM_CASH_CURRENCY b " +
            "where  f.ID_CURRENCY=b.ID_CURRENCY and b.ID_SITE=? and f.ID_CURRENCY=? ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        Timestamp stamp = null;
        try {
            ps = db_.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, idSite );
            RsetTools.setLong( ps, 2, idCurrency );

            rs = ps.executeQuery();

            if( rs.next() ) {
                stamp = RsetTools.getTimestamp( rs, "LAST_DATE" );
            }
            else
                return null;
        }
        catch( Exception e ) {
            log.error( "Exception get max date of curs" );
            throw new PriceException( e.getMessage() );
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }

        if( log.isDebugEnabled() ) {
            try {
                SimpleDateFormat df = new SimpleDateFormat( "dd.MM.yyyy HH:mm:ss.SSS", Locale.ENGLISH );
                TimeZone tzTemp = CurrentTimeZone.getTZ();
                df.setTimeZone( tzTemp );

                String st = df.format( stamp );
                System.out.println( "ts in db " + st );
                log.debug( "Max date timestamp " + st + " ts " + stamp );

            }
            catch( Throwable th ) {
                System.out.println( "Error get timestamp " + th.toString() );
            }
        }
        sql_ =
            "select  a.ID_CURRENCY, a.DATE_CHANGE, a.CURS " +
            "from    WM_CASH_CURR_VALUE a, WM_CASH_CURRENCY b " +
            "where   a.ID_CURRENCY=b.ID_CURRENCY and " +
            "        b.ID_SITE=? and " +
            "        a.ID_CURRENCY=? and " +
            "        DATE_CHANGE = " + db_.getNameDateBind();

        ps = null;
        rs = null;
        try {
            ps = db_.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, idSite );
            RsetTools.setLong( ps, 2, idCurrency );
            db_.bindDate( ps, 3, stamp );

            rs = ps.executeQuery();

            if( rs.next() ) {
                CurrencyCurrentCursType curs = new CurrencyCurrentCursType();

                curs.setCurs( RsetTools.getDouble( rs, "CURS" ) );
                curs.setDateChange( RsetTools.getCalendar( rs, "DATE_CHANGE" ).getTime() );
                curs.setIdCurrency( idCurrency );

                return curs;
            }
            return null;
        }
        catch( Exception e ) {
            final String es = "Exception get current curs";
            log.error( es, e );
            throw new PriceException( es, e );
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
    }

    public static CurrencyCurrentCursType getStandardCurrencyCurs( DatabaseAdapter db_, Long idStandardCurrency )
        throws PriceException {
        String sql_ =
            "select max(z1.DATE_CHANGE) LAST_DATE " +
            "from   WM_CASH_CURS_STD z1, WM_CASH_CURRENCY_STD a2 " +
            "where  z1.IS_DELETED=0 and " +
            "       z1.ID_STD_CURR=a2.ID_STD_CURR and " +
            "       z1.ID_STD_CURR=?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        Timestamp stamp = null;
        try {
            ps = db_.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, idStandardCurrency );

            rs = ps.executeQuery();

            if( rs.next() ) {
                stamp = RsetTools.getTimestamp( rs, "LAST_DATE" );
            }
            else
                return null;
        }
        catch( Exception e ) {
            final String es = "Exception get max date of curs";
            log.error( es, e );
            throw new PriceException( es, e );
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }

        sql_ =
            "select  a2.*, a1.VALUE_CURS CURS, a1.DATE_CHANGE " +
            "from    WM_CASH_CURS_STD a1, WM_CASH_CURRENCY_STD a2 " +
            "where   a1.IS_DELETED=0 and " +
            "        a1.ID_STD_CURR=a2.ID_STD_CURR and " +
            "        a1.ID_STD_CURR=? and " +
            "        a1.DATE_CHANGE= " + db_.getNameDateBind();

        ps = null;
        rs = null;
        try {
            ps = db_.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, idStandardCurrency );
            db_.bindDate( ps, 2, stamp );

            rs = ps.executeQuery();

            if( rs.next() ) {
                CurrencyCurrentCursType curs = new CurrencyCurrentCursType();

                curs.setCurs( RsetTools.getDouble( rs, "CURS" ) );
                curs.setDateChange( RsetTools.getCalendar( rs, "DATE_CHANGE" ).getTime() );
                curs.setIdCurrency( idStandardCurrency );

                return curs;
            }
            return null;
        }
        catch( Exception e ) {
            final String es = "Exception get max date of curs";
            log.error( es, e );
            throw new PriceException( es, e );
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
    }

    public static StandardCurrencyType getStandardCurrencyList( DatabaseAdapter db_ )
        throws PriceException {

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
