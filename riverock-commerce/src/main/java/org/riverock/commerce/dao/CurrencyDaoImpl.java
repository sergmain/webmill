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

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.sql.Timestamp;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import org.riverock.commerce.manager.currency.CurrencyBean;
import org.riverock.commerce.manager.currency.CurrencyCurs;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.annotation.schema.db.CustomSequence;
import org.riverock.common.tools.RsetTools;

/**
 * @author Sergei Maslyukov
 *         Date: 31.08.2006
 *         Time: 21:53:14
 *         <p/>
 *         $Id$
 */
public class CurrencyDaoImpl implements CurrencyDao {
    private final static Logger log = Logger.getLogger( CurrencyDaoImpl.class );

    /**
     * list of curs for currency not initialized
     *
     * @return List<CurrencyBean>
     */
    public List<CurrencyBean> getCurrencyList(Long siteId) {
        List<CurrencyBean> list = new ArrayList<CurrencyBean>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();

            ps = db_.prepareStatement(
                "select ID_CURRENCY, CURRENCY, IS_USED, NAME_CURRENCY, IS_USE_STANDART, ID_STANDART_CURS, ID_SITE, PERCENT_VALUE " +
                "from   WM_CASH_CURRENCY " +
                "where  ID_SITE=? "
            );
            ps.setLong(1, siteId);

            rs = ps.executeQuery();

            while (rs.next()) {
                CurrencyBean bean = initCurrencyBean(rs);
                list.add(bean);
            }
            return list;
        }
        catch (Throwable e) {
            final String es = "Error create list of currencies";
            log.error(es, e);
            throw new RuntimeException( es, e );
        }
        finally {
            DatabaseManager.close(db_, rs, ps);
        }
    }

    public Long createCurrency(CurrencyBean currencyBean) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequence seq = new CustomSequence();
            seq.setSequenceName( "seq_WM_CASH_CURRENCY" );
            seq.setTableName( "WM_CASH_CURRENCY" );
            seq.setColumnName( "ID_CURRENCY" );
            Long id = adapter.getSequenceNextValue( seq );

            String sql_ =
                "insert into WM_CASH_CURRENCY"+
                "(ID_CURRENCY, CURRENCY, IS_USED, NAME_CURRENCY, IS_USE_STANDART, ID_STANDART_CURS, ID_SITE, PERCENT_VALUE) "+
                "values "+
                "( ?, ?, ?, ?, ?, ?, ?, ?)";

            ps = adapter.prepareStatement(sql_);

            ps.setLong(1, id );
            ps.setString(2, currencyBean.getCurrencyCode() );
            ps.setInt(3, currencyBean.isUsed()?1:0 );
            ps.setString(4, currencyBean.getCurrencyName() );
            ps.setInt(5, currencyBean.isUseStandard()?1:0 );
            ps.setLong(6, currencyBean.getStandardCurrencyId() );
            ps.setLong(7, currencyBean.getSiteId() );
            ps.setBigDecimal(8, currencyBean.getPercent() );

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
            String es = "Error create currency";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter, rs, ps);
        }
    }

    public void updateCurrency(CurrencyBean currencyBean) {
        if (currencyBean==null) {
            return;
        }
        
        String sql_ =
            "update WM_CASH_CURRENCY "+
            "set "+
            "    NAME_CURRENCY=?, " +
            "    CURRENCY=?, " +
            "    IS_USED=?, " +
            "    IS_USE_STANDART=?, " +
            "    ID_STANDART_CURS=?, " +
            "    ID_SITE=?, " +
            "    PERCENT_VALUE=? "+
            "where ID_CURRENCY=?";

        PreparedStatement ps = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            ps = adapter.prepareStatement(sql_);

            ps.setString(1, currencyBean.getCurrencyName() );
            ps.setString(2, currencyBean.getCurrencyCode() );
            ps.setInt(3, currencyBean.isUsed()?1:0 );
            ps.setInt(4, currencyBean.isUseStandard()?1:0 );
            ps.setLong(5, currencyBean.getStandardCurrencyId() );
            ps.setLong(6, currencyBean.getSiteId() );
            ps.setBigDecimal(7, currencyBean.getPercent() );

            // prepare PK
            ps.setLong(8, currencyBean.getCurrencyId() );

            int i = ps.executeUpdate();
            if (log.isDebugEnabled()) {
                log.debug("count of updated records; " + i+", PK: " +currencyBean.getCurrencyId());
            }

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
            String es = "Error update currency";
            log.error( es, e );
            throw new IllegalStateException( es, e );
       }
       finally {
            DatabaseManager.close(adapter, ps);
       }
    }

    public void deleteCurrency(Long currencyId) {
        if (currencyId==null) {
            return;
        }

        DatabaseAdapter dbDyn = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();

            DatabaseManager.runSQL(
                dbDyn,
                "delete from WM_CASH_CURR_VALUE where ID_CURRENCY=?",
                new Object[]{currencyId}, new int[]{Types.DECIMAL}
            );

            DatabaseManager.runSQL(
                dbDyn,
                "delete from WM_CASH_CURRENCY where ID_CURRENCY=?",
                new Object[]{currencyId}, new int[]{Types.DECIMAL}
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
            String es = "Error delete currency";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( dbDyn);
        }
    }

    public CurrencyBean getCurrency(Long currencyId) {
        if (currencyId==null) {
            return null;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;

        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();

            ps = db_.prepareStatement(
                "select ID_CURRENCY, CURRENCY, IS_USED, NAME_CURRENCY, IS_USE_STANDART, ID_STANDART_CURS, ID_SITE, PERCENT_VALUE " +
                "from   WM_CASH_CURRENCY " +
                "where  ID_CURRENCY=? "
            );
            RsetTools.setLong(ps, 1, currencyId );

            rs = ps.executeQuery();

            if (rs.next()) {
                CurrencyBean currencyBean = initCurrencyBean(rs);
                currencyBean.setCurses( getCurrencyCurses(db_, currencyId) );
                return currencyBean;
            }
            return null;
        }
        catch (Throwable e) {
            final String es = "Error get currency";
            log.error(es, e);
            throw new RuntimeException( es, e );
        }
        finally {
            DatabaseManager.close(db_, rs, ps);
        }
    }

    public void addCurrencyCurs(Long currencyId, BigDecimal currentCurs) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequence seq = new CustomSequence();
            seq.setSequenceName( "seq_WM_CASH_CURR_VALUE" );
            seq.setTableName( "WM_CASH_CURR_VALUE" );
            seq.setColumnName( "ID_CURVAL" );
            Long id = adapter.getSequenceNextValue( seq );

            String sql_ =
                "insert into WM_CASH_CURR_VALUE"+
                "(ID_CURVAL, DATE_CHANGE, CURS, ID_CURRENCY)"+
                "values"+
                "( ?, ?, ?, ?)";

            ps = adapter.prepareStatement(sql_);

            ps.setLong(1, id );
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()) );
            ps.setBigDecimal(3, currentCurs );
            ps.setLong(4, currencyId );

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
            String es = "Error add currency curs";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter, rs, ps);
        }
    }

    private List<CurrencyCurs> getCurrencyCurses(DatabaseAdapter db_, Long currencyId) {
        List<CurrencyCurs> list = new ArrayList<CurrencyCurs>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = db_.prepareStatement(
                "select ID_CURRENCY, DATE_CHANGE, CURS, ID_CURVAL " +
                "from   WM_CASH_CURR_VALUE " +
                "where  ID_CURRENCY=? " +
                "order by DATE_CHANGE DESC "
            );

            ps.setLong(1, currencyId);
            rs = ps.executeQuery();

            while (rs.next()) {
                CurrencyCurs curs = initCurrencyCurs(rs);
                list.add(curs);
            }
            return list;
        }
        catch (Throwable e) {
            final String es = "Error create list of currencies";
            log.error(es, e);
            throw new RuntimeException( es, e );
        }
        finally {
            DatabaseManager.close( rs, ps);
        }
    }

    private CurrencyCurs initCurrencyCurs(ResultSet rs) throws SQLException {
        CurrencyCurs curs = new CurrencyCurs();

        // ID_CURRENCY, DATE_CHANGE, CURS, ID_CURVAL
        curs.setCurs( rs.getBigDecimal("CURS") );
        if (rs.wasNull()) {
            return null;
        }
        curs.setCreated( RsetTools.getTimestamp(rs, "DATE_CHANGE") );

        return curs;
    }

    private CurrencyBean initCurrencyBean(ResultSet rs) throws SQLException {
        CurrencyBean bean = new CurrencyBean();

        // ID_CURRENCY, CURRENCY, IS_USED, NAME_CURRENCY, IS_USE_STANDART, ID_STANDART_CURS, ID_SITE, PERCENT_VALUE
        bean.setCurrencyId( RsetTools.getLong(rs, "ID_CURRENCY"));
        bean.setCurrencyName( RsetTools.getString(rs, "NAME_CURRENCY") );
        bean.setCurrencyCode( RsetTools.getString(rs, "CURRENCY") );
        bean.setUsed( RsetTools.getInt(rs, "IS_USED", 0)==1);
        bean.setUseStandard( RsetTools.getInt(rs, "IS_USE_STANDART", 0)==1);
        bean.setStandardCurrencyId( RsetTools.getLong(rs, "ID_STANDART_CURS"));
        bean.setSiteId( RsetTools.getLong(rs, "ID_SITE"));
        bean.setPercent( RsetTools.getBigDecimal(rs, "PERCENT_VALUE", new BigDecimal(0.0)) );

        return bean;
    }
}
