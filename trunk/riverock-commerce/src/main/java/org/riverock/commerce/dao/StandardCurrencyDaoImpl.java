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

import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Timestamp;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import org.riverock.commerce.bean.StandardCurrencyCurs;
import org.riverock.commerce.bean.StandardCurrency;
import org.riverock.commerce.tools.HibernateUtils;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.annotation.schema.db.CustomSequence;
import org.riverock.common.tools.RsetTools;

/**
 * @author Sergei Maslyukov
 *         Date: 29.08.2006
 *         Time: 20:45:52
 */
public class StandardCurrencyDaoImpl implements StandardCurrencyDao {
    private final static Logger log = Logger.getLogger( StandardCurrencyDaoImpl.class );

    /**
     * list of curs for currency not initialized
     *
     * @return List<StandardCurrency>
     */
    public List<StandardCurrency> getStandardCurrencyList() {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List<StandardCurrency> list = session.createQuery(
            "select stdCurr from org.riverock.commerce.bean.StandardCurrency stdCurr ")
            .list();
        session.getTransaction().commit();
        return list;
    }

    public Long createStandardCurrency(StandardCurrency standardCurrencyBean) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequence seq = new CustomSequence();
            seq.setSequenceName( "seq_WM_CASH_CURRENCY_STD" );
            seq.setTableName( "WM_CASH_CURRENCY_STD" );
            seq.setColumnName( "ID_STD_CURR" );
            Long id = adapter.getSequenceNextValue( seq );

            String sql_ =
                "insert into WM_CASH_CURRENCY_STD"+
                "(ID_STD_CURR, NAME_STD_CURR, CONVERT_CURRENCY, IS_DELETED)"+
                "values"+
                "( ?,  ?,  ?,  ?)";

            ps = adapter.prepareStatement(sql_);

            ps.setLong(1, id );
            ps.setString(2, standardCurrencyBean.getStandardCurrencyName() );
            ps.setString(3, standardCurrencyBean.getStandardCurrencyCode() );
            ps.setInt(4, 0 );

            ps.executeUpdate();

            adapter.commit();
            return id;
        } catch (Throwable e) {
            try {
                if (adapter!=null)
                    adapter.rollback();
            }
            catch(Throwable th) {
                // catch rollback error
            }
            String es = "Error create standard currency";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter, rs, ps);
        }
    }

    public void updateStandardCurrency(StandardCurrency standardCurrencyBean) {
        String sql_ =
            "update WM_CASH_CURRENCY_STD "+
            "set"+
            "    NAME_STD_CURR=?, " +
            "    CONVERT_CURRENCY=?, " +
            "    IS_DELETED=? "+
            "where ID_STD_CURR=?";

        PreparedStatement ps = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            ps = adapter.prepareStatement(sql_);

            ps.setString(1, standardCurrencyBean.getStandardCurrencyName() );
            ps.setString(2, standardCurrencyBean.getStandardCurrencyCode() );
            ps.setInt(3, standardCurrencyBean.isDeleted()?1:0 );
            // prepare PK
            ps.setLong(4, standardCurrencyBean.getStandardCurrencyId() );

            ps.executeUpdate();

            adapter.commit();
        }
        catch (Exception e) {
            try {
                if( adapter != null )
                    adapter.rollback();
            }
            catch( Exception e001 ) {
                //catch rollback error
            }
            String es = "Error update standard currency";
            log.error( es, e );
            throw new IllegalStateException( es, e );
       }
       finally {
            DatabaseManager.close(adapter, ps);
       }
    }

    public void deleteStandardCurrency(Long standardCurrencyId) {
        if (standardCurrencyId==null) {
            return;
        }

        DatabaseAdapter dbDyn = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();

            DatabaseManager.runSQL(
                dbDyn,
                "delete from WM_CASH_CURS_STD where ID_STD_CURS=?",
                new Object[]{standardCurrencyId}, new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                dbDyn,
                "update WM_CASH_CURRENCY_STD set IS_DELETED=1 where ID_STD_CURR=?",
                new Object[]{standardCurrencyId}, new int[]{Types.DECIMAL}
            );

            dbDyn.commit();
        }
        catch( Exception e ) {
            try {
                if( dbDyn != null )
                    dbDyn.rollback();
            }
            catch( Exception e001 ) {
                //catch rollback error
            }
            String es = "Error delete standard currency";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( dbDyn);
        }
    }

    public StandardCurrency getStandardCurrency(Long standardCurrencyId) {
        if (standardCurrencyId==null) {
            return null;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;

        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();

            ps = db_.prepareStatement(
                "select ID_STD_CURR, NAME_STD_CURR, CONVERT_CURRENCY, IS_DELETED " +
                "from   WM_CASH_CURRENCY_STD " +
                "where  IS_DELETED=0 and ID_STD_CURR=? "
            );
            RsetTools.setLong(ps, 1, standardCurrencyId );

            rs = ps.executeQuery();

            if (rs.next()) {
                StandardCurrency standardCurrencyBean = initStandardCurrencyBean(rs);
                standardCurrencyBean.setCurses( getStandardCurrencyCurses(db_, standardCurrencyId) );
                return standardCurrencyBean;
            }
            return null;
        }
        catch (Throwable e) {
            final String es = "Error get standard currency";
            log.error(es, e);
            throw new RuntimeException( es, e );
        }
        finally {
            DatabaseManager.close(db_, rs, ps);
        }
    }

    public void addStandardCurrencyCurs(Long standardCurrencyId, BigDecimal currentCurs) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequence seq = new CustomSequence();
            seq.setSequenceName( "seq_WM_CASH_CURS_STD" );
            seq.setTableName( "WM_CASH_CURS_STD" );
            seq.setColumnName( "ID_STD_CURS" );
            Long id = adapter.getSequenceNextValue( seq );

            String sql_ =
                "insert into WM_CASH_CURS_STD"+
                "(ID_STD_CURS, DATE_CHANGE, VALUE_CURS, IS_DELETED, ID_STD_CURR)"+
                "values"+
                "( ?,  ?,  ?,  ?, ?)";

            ps = adapter.prepareStatement(sql_);

            ps.setLong(1, id );
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()) );
            ps.setBigDecimal(3, currentCurs );
            ps.setInt(4, 0 );
            ps.setLong(5, standardCurrencyId );

            ps.executeUpdate();

            adapter.commit();
        } catch (Throwable e) {
            try {
                if (adapter!=null)
                    adapter.rollback();
            }
            catch(Throwable th) {
                // catch rollback error
            }
            String es = "Error add standard currency curs";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter, rs, ps);
        }
    }

    private List<StandardCurrencyCurs> getStandardCurrencyCurses(DatabaseAdapter db_, Long standardCurrencyId) {
        List<StandardCurrencyCurs> list = new ArrayList<StandardCurrencyCurs>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = db_.prepareStatement(
                "select ID_STD_CURS, DATE_CHANGE, VALUE_CURS, IS_DELETED, ID_STD_CURR " +
                "from   WM_CASH_CURS_STD " +
                "where ID_STD_CURR=? and IS_DELETED=0 " +
                "order by DATE_CHANGE DESC "
            );

            ps.setLong(1, standardCurrencyId);
            rs = ps.executeQuery();

            while (rs.next()) {
                StandardCurrencyCurs curs = initStandardCurrencyCurs(rs);
                list.add(curs);
            }
            return list;
        }
        catch (Throwable e) {
            final String es = "Error create list of standard currencies";
            log.error(es, e);
            throw new RuntimeException( es, e );
        }
        finally {
            DatabaseManager.close( rs, ps);
        }
    }

    private StandardCurrencyCurs initStandardCurrencyCurs(ResultSet rs) throws SQLException {
        StandardCurrencyCurs curs = new StandardCurrencyCurs();

        curs.setCurs( rs.getBigDecimal("VALUE_CURS") );
        if (rs.wasNull()) {
            return null;
        }
        curs.setCreated( RsetTools.getTimestamp(rs, "DATE_CHANGE") );

        return curs;
    }

    private StandardCurrency initStandardCurrencyBean(ResultSet rs) throws SQLException {
        StandardCurrency currency = new StandardCurrency();

        currency.setStandardCurrencyId( RsetTools.getLong(rs, "ID_STD_CURR"));
        currency.setStandardCurrencyName( RsetTools.getString(rs, "NAME_STD_CURR") );
        currency.setStandardCurrencyCode( RsetTools.getString(rs, "CONVERT_CURRENCY") );
        return currency;
    }

}
