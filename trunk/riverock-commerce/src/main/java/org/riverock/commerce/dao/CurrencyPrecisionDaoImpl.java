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

import org.apache.log4j.Logger;
import org.riverock.commerce.bean.CurrencyPrecisionBean;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * User: SergeMaslyukov
 * Date: 06.09.2006
 * Time: 0:14:40
 * <p/>
 * $Id: PriceCurrency.java 950 2006-09-01 18:11:51Z serg_main $
 */
public class CurrencyPrecisionDaoImpl implements CurrencyPrecisionDao {
    private static Logger log = Logger.getLogger( CurrencyPrecisionDaoImpl.class );

    public CurrencyPrecisionBean getCurrencyPrecision(Long currencyPrecisionId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db=null;
        try {
            db=DatabaseAdapter.getInstance();

            String sql_ =
                "select ID_PRICE_SHOP_PRECISION, ID_CURRENCY, ID_SHOP, PRECISION_SHOP " +
                "from   WM_PRICE_SHOP_PRECISION " +
                "where  ID_PRICE_SHOP_PRECISION=?";

            ps = db.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, currencyPrecisionId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return initCurrencyPrecision(rs);
            }
            return null;
        }
        catch (Exception e) {
            String es = "Error get currency precision";
            log.error(es, e);
            throw new IllegalStateException(e);
        }
        finally {
            DatabaseManager.close( db, rs, ps );
        }
    }

    public List<CurrencyPrecisionBean> getCurrencyPrecisionList(Long shopId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db=null;
        try {
            db=DatabaseAdapter.getInstance();
            
            String sql_ =
                "select ID_PRICE_SHOP_PRECISION, ID_CURRENCY, ID_SHOP, PRECISION_SHOP " +
                "from   WM_PRICE_SHOP_PRECISION " +
                "where  ID_SHOP=?";

            ps = db.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, shopId);
            rs = ps.executeQuery();

            List<CurrencyPrecisionBean> list = new ArrayList<CurrencyPrecisionBean>();
            while (rs.next()) {
                CurrencyPrecisionBean prec = initCurrencyPrecision(rs);
                list.add( prec );
            }
            return list;
        }
        catch (Exception e) {
            String es = "Error get currency precision list";
            log.error(es, e);
            throw new IllegalStateException(e);
        }
        finally {
            DatabaseManager.close( db, rs, ps );
        }
    }

    public void updateCurrencyPrecision(Long currencyPrecisionId, Integer currencyPrecision) {
        if (currencyPrecisionId==null || currencyPrecision==null) {
            log.info("currencyPrecisionId: "+currencyPrecisionId+", currencyPrecision: " + currencyPrecision);
            return;
        }

        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();

            DatabaseManager.runSQL(
                db,
                "update WM_PRICE_SHOP_PRECISION set PRECISION_SHOP=? where ID_PRICE_SHOP_PRECISION=?",
                new Object[]{currencyPrecision, currencyPrecisionId}, new int[]{Types.DECIMAL, Types.DECIMAL}
            );

            db.commit();
        }
        catch( Exception e ) {
            try {
                if( db != null )
                    db.rollback();
            }
            catch( Exception e001 ) {
                //catch rollback error
            }
            String es = "Error update currency precision";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db);
        }
    }

    private CurrencyPrecisionBean initCurrencyPrecision(ResultSet rs) throws SQLException {
        CurrencyPrecisionBean prec = new CurrencyPrecisionBean();

        prec.setCurrencyPrecisionId( RsetTools.getLong(rs, "ID_PRICE_SHOP_PRECISION"));
        prec.setCurrencyId(RsetTools.getLong(rs, "ID_CURRENCY"));
        prec.setShopId(RsetTools.getLong(rs, "ID_SHOP"));
        prec.setPrecision(RsetTools.getInt(rs, "PRECISION_SHOP"));
        return prec;
    }

}
