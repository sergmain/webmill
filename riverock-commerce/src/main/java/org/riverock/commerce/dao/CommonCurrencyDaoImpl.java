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
package org.riverock.commerce.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import org.riverock.commerce.bean.CurrencyCurs;
import org.riverock.commerce.bean.StandardCurrencyCurs;
import org.riverock.commerce.tools.HibernateUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 01.09.2006
 *         Time: 19:29:42
 *         <p/>
 *         $Id$
 */
public class CommonCurrencyDaoImpl implements CommonCurrencyDao {
    private final static Logger log = Logger.getLogger( CommonCurrencyDaoImpl.class );

    public CurrencyCurs getCurrentCurs(Long currencyId, Long siteId) {
        Session session=null;
        try {
            session = HibernateUtils.getSession();
            session.beginTransaction();
            Timestamp stamp = (Timestamp)session.createQuery(
                "select max(f.date) " +
                    "from  org.riverock.commerce.bean.CurrencyCurs f, " +
                    "      org.riverock.commerce.bean.Currency b " +
                    "where f.currencyId=b.currencyId and b.siteId=:siteId and f.currencyId=:currencyId")
                .setLong("siteId", siteId)
                .setLong("currencyId", currencyId)
                .uniqueResult();

//        String sql_ =
//            "select max(f.DATE_CHANGE) LAST_DATE " +
//            "from   WM_CASH_CURR_VALUE f, WM_CASH_CURRENCY b " +
//            "where  f.ID_CURRENCY=b.ID_CURRENCY and b.ID_SITE=? and f.ID_CURRENCY=? ";

            if (stamp==null) {
                return null;
            }

            List curses = session.createQuery(
                "select a " +
                    "from  org.riverock.commerce.bean.CurrencyCurs a, " +
                    "      org.riverock.commerce.bean.Currency b " +
                    "where a.currencyId=b.currencyId and b.siteId=:siteId and a.currencyId=:currencyId and a.date=:dateChange " +
                    "order by a.currencyCursId desc")
                .setLong("siteId", siteId)
                .setLong("currencyId", currencyId)
                .setTimestamp("dateChange", stamp)
                .list();

            if (curses.isEmpty()) {
                return null;
            }

            return (CurrencyCurs)curses.get(0);
        }
        finally {
            if (session!=null) {
                session.getTransaction().commit();
            }
        }
    }

    public StandardCurrencyCurs getStandardCurrencyCurs(Long standardCurrencyId) {
        Session session=null;
        try {
            session = HibernateUtils.getSession();
            session.beginTransaction();
            Timestamp stamp = (Timestamp)session.createQuery(
                "select max(z1.created) " +
                    "from  org.riverock.commerce.bean.StandardCurrencyCurs z1, " +
                    "      org.riverock.commerce.bean.StandardCurrency a2 " +
                    "where z1.isDeleted=false and z1.standardCurrencyId=a2.standardCurrencyId and " +
                    "      z1.standardCurrencyId=:standardCurrencyId")
                .setLong("standardCurrencyId", standardCurrencyId)
                .uniqueResult();

//        String sql_ =
//            "select max(z1.DATE_CHANGE) LAST_DATE " +
//            "from   WM_CASH_CURS_STD z1, WM_CASH_CURRENCY_STD a2 " +
//            "where  z1.IS_DELETED=0 and " +
//            "       z1.ID_STD_CURR=a2.ID_STD_CURR and " +
//            "       z1.ID_STD_CURR=?";

            if (stamp==null) {
                return null;
            }

//            sql_ =
//                "select  a2.*, a1.VALUE_CURS CURS, a1.DATE_CHANGE " +
//                    "from    WM_CASH_CURS_STD a1, WM_CASH_CURRENCY_STD a2 " +
//                    "where   a1.IS_DELETED=0 and " +
//                    "        a1.ID_STD_CURR=a2.ID_STD_CURR and " +
//                    "        a1.ID_STD_CURR=? and " +
//                    "        a1.DATE_CHANGE= " + db.getNameDateBind();

            List curses = session.createQuery(
                "select a1 " +
                    "from  org.riverock.commerce.bean.StandardCurrencyCurs a1, " +
                    "      org.riverock.commerce.bean.StandardCurrency a2 " +
                    "where a1.isDeleted=false and a1.standardCurrencyId=a2.standardCurrencyId and " +
                    "      a1.standardCurrencyId=:standardCurrencyId and " +
                    "      a1.created=:dateChange " +
                    "order by a1.standardCurrencyCursId desc")
                .setLong("standardCurrencyId", standardCurrencyId)
                .setTimestamp("dateChange", stamp)
                .list();

            if (curses.isEmpty()) {
                return null;
            }

            return (StandardCurrencyCurs)curses.get(0);
        }
        finally {
            if (session!=null) {
                session.getTransaction().commit();
            }
        }

/*
            ps = db.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, standardCurrencyId );
            db.bindDate( ps, 2, stamp );

            rs = ps.executeQuery();

            if( rs.next() ) {
                CurrencyCurs curs = new CurrencyCurs();

                curs.setCurs( RsetTools.getBigDecimal( rs, "CURS" ) );
                curs.setDate( RsetTools.getTimestamp(rs, "DATE_CHANGE") );
                curs.setCurrencyId( standardCurrencyId );

                return curs;
            }
            return null;
*/
    }
}
