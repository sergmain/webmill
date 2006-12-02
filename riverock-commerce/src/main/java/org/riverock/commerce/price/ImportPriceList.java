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

import org.apache.log4j.Logger;

import org.riverock.commerce.schema.import_price.PriceListItemType;
import org.riverock.commerce.schema.import_price.PriceListType;
import org.riverock.commerce.schema.import_price.PricesType;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;

/**
 * $Id$
 */
public class ImportPriceList {
    private static Logger log = Logger.getLogger(ImportPriceList.class);
    private static final int MAX_POSITION_IN_IMPORT_FILE = 5000;

/*
    public static void process(File file, Long id_site, DatabaseAdapter db)
        throws PriceException {

        try {
            InputSource inSrc = new InputSource(new FileInputStream(file));
            PricesType prices = (PricesType) Unmarshaller.unmarshal(PricesType.class, inSrc);
            process(prices, id_site, db);
        }
        catch (Exception e) {
            throw new PriceException(e.toString());
        }
    }
*/

    public static void process(final PricesType prices, Long id_site, DatabaseAdapter dbDyn) throws PriceException {
        if (prices == null) {
            throw new PriceException(" #10.03");
        }


        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql_ = null;
        PriceListItemType debugItem = null;
        try {
            dbDyn.getConnection().setAutoCommit(false);

            if (dbDyn.getFamily() != DatabaseManager.MYSQL_FAMALY) {
                sql_ =
                    "delete from WM_PRICE_IMPORT_TABLE where shop_code in " +
                        "( select shop_code from WM_PRICE_SHOP_LIST where ID_SITE=? )";

                ps = dbDyn.prepareStatement(sql_);
                RsetTools.setLong(ps, 1, id_site);
                ps.executeUpdate();
                ps.close();
                ps = null;
            } else {
                String sqlCheck = "";
                boolean isFound = false;
                ps = dbDyn.prepareStatement("select shop_code from WM_PRICE_SHOP_LIST where ID_SITE=?");
                RsetTools.setLong(ps, 1, id_site);
                rs = ps.executeQuery();
                boolean isFirst = true;
                while (rs.next()) {
                    isFound = true;
                    if (isFirst)
                        isFirst = false;
                    else
                        sqlCheck += ",";

                    sqlCheck += ("'" + rs.getString("shop_code") + "'");
                }
                rs.close();
                rs = null;
                ps.close();
                ps = null;

                if (isFound) {
                    sql_ =
                        "delete from WM_PRICE_IMPORT_TABLE where shop_code in ( " + sqlCheck + " )";

                    if (log.isDebugEnabled())
                        log.debug("sql " + sql_);

                    ps = dbDyn.prepareStatement(sql_);
                    ps.executeUpdate();
                    ps.close();
                    ps = null;
                }
            }

            int batchLoop = 0;
            int count = 0;

            sql_ =
                "insert into WM_PRICE_IMPORT_TABLE " +
                    "(is_group, id, id_main, name, price, currency, is_to_load, shop_code, ID_UPLOAD_PRICE) " +
                    "values (?,?,?,?,?,?,?,?,?)";

            Long id_upload_session = null;

            for (PriceListType price : prices.getPriceList()) {
                if (log.isDebugEnabled()) {
                    log.debug("shopCode " + price.getShopCode());
                    log.debug("Size vector: " + price.getItem().size());
                }

                for (PriceListItemType item : price.getItem()) {
                    if (count++ > MAX_POSITION_IN_IMPORT_FILE) break;
                    if (ps == null) {
                        ps = dbDyn.prepareStatement(sql_);
                    }

                    debugItem = item;

                    ps.setInt(1, item.isIsGroup()?1:0);
                    RsetTools.setLong(ps, 2, item.getItemID());
                    RsetTools.setLong(ps, 3, item.getParentID());
                    ps.setString(4, item.getNameItem());
                    RsetTools.setDouble(ps, 5, item.getPrice());
                    ps.setString(6, item.getCurr());
                    ps.setString(7, item.getIsLoad());
                    ps.setString(8, price.getShopCode().toUpperCase());
                    RsetTools.setLong(ps, 9, id_upload_session);

                    if (dbDyn.getIsBatchUpdate()) {
                        ps.addBatch();

//log.debug("To batch: "+batchLoop);

                        if (++batchLoop >= 200) {
//log.debug("#1.001");
                            int[] updateCounts = ps.executeBatch();
                            ps.close();
                            ps = null;
                            batchLoop = 0;
                        }
                    } else
                        ps.executeUpdate();

                } // for( int i=0...)
            } // for (int j=0...)

            if (dbDyn.getIsBatchUpdate()) {
                if (ps != null) // Not all record is flushed to DB
                {
                    int[] updateCounts = ps.executeBatch();
                    ps.close();
                    ps = null;
                }
            }

            ImportPriceProcess.process(dbDyn, id_site);

            dbDyn.commit();

        }
        catch (Exception e) {
            if (debugItem != null) {
                log.error("debugItem.getIsGroup() " + (Boolean.TRUE.equals(debugItem.isIsGroup()) ? 1 : 0));
                log.error("debugItem.getItemID() " + debugItem.getItemID());
                log.error("debugItem.getParentID() " + debugItem.getParentID());
                log.error("debugItem.getNameItem() " + debugItem.getNameItem());
                log.error("debugItem.getPrice() " + debugItem.getPrice());
                log.error("debugItem.getCurr() " + debugItem.getCurr());
                log.error("debugItem.getIsLoad().toString() " + debugItem.getIsLoad());
            } else
                log.error("debugItem is null");

            log.error("sql:\n" + sql_);
            final String es = "error process import price-list";
            log.error(es, e);
            try {
                if (dbDyn!=null) {
                    dbDyn.rollback();
                }
            }
            catch (Exception e11) {
                // catch rollback error
            }

            throw new PriceException(es, e);
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

}
