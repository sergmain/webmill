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
package org.riverock.commerce.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import org.riverock.commerce.bean.CurrencyCurrentCurs;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.tools.CurrentTimeZone;

/**
 * @author Sergei Maslyukov
 *         Date: 01.09.2006
 *         Time: 19:29:42
 *         <p/>
 *         $Id$
 */
public class CommonCurrencyDaoImpl implements CommonCurrencyDao {
    private final static Logger log = Logger.getLogger( CommonCurrencyDaoImpl.class );

    public CurrencyCurrentCurs getCurrentCurs(Long currencyId, Long siteId) {
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            return getCurrentCurs(db, currencyId, siteId);
        }
        catch (Exception e) {
            final String es = "Exception get current curs for currency";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close(db);
        }
    }

    public CurrencyCurrentCurs getCurrentCurs(DatabaseAdapter db, Long currencyId, Long siteId) {
        String sql_ =
            "select max(f.DATE_CHANGE) LAST_DATE " +
            "from   WM_CASH_CURR_VALUE f, WM_CASH_CURRENCY b " +
            "where  f.ID_CURRENCY=b.ID_CURRENCY and b.ID_SITE=? and f.ID_CURRENCY=? ";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = db.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, siteId );
            RsetTools.setLong( ps, 2, currencyId );

            rs = ps.executeQuery();

            Timestamp stamp=null;
            if( rs.next() ) {
                stamp = RsetTools.getTimestamp( rs, "LAST_DATE" );
            }

            if (stamp==null) {
                return null;
            }

            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;

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
                    "        DATE_CHANGE=?";

            ps = db.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, siteId );
            RsetTools.setLong( ps, 2, currencyId );
            ps.setTimestamp(3, stamp );

            rs = ps.executeQuery();

            if( rs.next() ) {
                CurrencyCurrentCurs curs = new CurrencyCurrentCurs();

                curs.setCurs( RsetTools.getBigDecimal( rs, "CURS" ) );
                curs.setDate( RsetTools.getTimestamp(rs, "DATE_CHANGE") );
                curs.setCurrencyId( currencyId );

                return curs;
            }
            return null;
        }
        catch( Exception e ) {
            final String es = "Exception get current curs";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( rs, ps );
        }
    }

    public CurrencyCurrentCurs getStandardCurrencyCurs(Long currencyId) {
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            return getStandardCurrencyCurs(db, currencyId);
        }
        catch (Exception e) {
            final String es = "Exception get currentcurs of standard currency";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close(db);
        }
    }

    public CurrencyCurrentCurs getStandardCurrencyCurs( DatabaseAdapter db, Long standardCurrencyId ) {
        String sql_ =
            "select max(z1.DATE_CHANGE) LAST_DATE " +
            "from   WM_CASH_CURS_STD z1, WM_CASH_CURRENCY_STD a2 " +
            "where  z1.IS_DELETED=0 and " +
            "       z1.ID_STD_CURR=a2.ID_STD_CURR and " +
            "       z1.ID_STD_CURR=?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = db.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, standardCurrencyId );

            rs = ps.executeQuery();

            Timestamp stamp = null;
            if( rs.next() ) {
                stamp = RsetTools.getTimestamp( rs, "LAST_DATE" );
            }
            if (stamp==null) {
                return null;
            }

            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;

            sql_ =
                "select  a2.*, a1.VALUE_CURS CURS, a1.DATE_CHANGE " +
                    "from    WM_CASH_CURS_STD a1, WM_CASH_CURRENCY_STD a2 " +
                    "where   a1.IS_DELETED=0 and " +
                    "        a1.ID_STD_CURR=a2.ID_STD_CURR and " +
                    "        a1.ID_STD_CURR=? and " +
                    "        a1.DATE_CHANGE= " + db.getNameDateBind();

            ps = db.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, standardCurrencyId );
            db.bindDate( ps, 2, stamp );

            rs = ps.executeQuery();

            if( rs.next() ) {
                CurrencyCurrentCurs curs = new CurrencyCurrentCurs();

                curs.setCurs( RsetTools.getBigDecimal( rs, "CURS" ) );
                curs.setDate( RsetTools.getTimestamp(rs, "DATE_CHANGE") );
                curs.setCurrencyId( standardCurrencyId );

                return curs;
            }
            return null;
        }
        catch( Exception e ) {
            final String es = "Exception get currentcurs of standard currency";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( rs, ps );
        }
    }
}
