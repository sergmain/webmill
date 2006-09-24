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

import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.portlet.core.GetWmPriceShopListWithIdSiteList;
import org.riverock.portlet.schema.core.WmPriceShopListItemType;
import org.riverock.portlet.schema.core.WmPriceShopListListType;
import org.riverock.portlet.schema.import_price.PriceListItemType;
import org.riverock.portlet.schema.import_price.PriceListType;
import org.riverock.portlet.schema.import_price.PricesType;


import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;


import org.apache.log4j.Logger;

/**
 * $Id$
 */
public class ImportPriceList {
    private static Logger log = Logger.getLogger( ImportPriceList.class );

    public static void process(File file, Long id_site, DatabaseAdapter db)
            throws PriceException
    {

        try
        {
            InputSource inSrc = new InputSource( new FileInputStream(file) );
            PricesType prices = (PricesType) Unmarshaller.unmarshal(PricesType.class, inSrc);
            process(prices, id_site, db);
        }
        catch(Exception e)
        {
            throw new PriceException(e.toString());
        }
    }

    public static void process(PricesType prices, Long id_site, DatabaseAdapter dbDyn)
            throws PriceException
    {
        PreparedStatement ps = null;

        String sql_ = null;
        PriceListItemType debugItem = null;
        try
        {

            if (log.isDebugEnabled())
            {
                log.debug("dbDyn - "+dbDyn);
                if (dbDyn!=null)
                    log.debug("dbDyn.conn - "+dbDyn.getConnection());
            }
            dbDyn.getConnection().setAutoCommit(false);

            if (dbDyn.getFamily()!=DatabaseManager.MYSQL_FAMALY)
            {
                sql_ =
                    "delete from WM_PRICE_IMPORT_TABLE where shop_code in " +
                    "( select shop_code from WM_PRICE_SHOP_LIST where ID_SITE=? )";

                ps = dbDyn.prepareStatement(sql_);
                RsetTools.setLong(ps, 1, id_site);
                ps.executeUpdate();
                ps.close();
                ps = null;
            }
            else
            {
                String sqlCheck = "";
                boolean isFound = false;

                WmPriceShopListListType shops =
                    GetWmPriceShopListWithIdSiteList.getInstance(dbDyn, id_site).item;

                boolean isFirst = true;
                for (int i=0; i<shops.getWmPriceShopListCount(); i++)
                {
                    WmPriceShopListItemType shop = shops.getWmPriceShopList(i);

                    isFound = true;
                    if (isFirst)
                        isFirst = false;
                    else
                        sqlCheck += ",";

                    sqlCheck += ("'"+shop.getCodeShop()+"'");
                }
                if (isFound)
                {
                    sql_ =
                        "delete from WM_PRICE_IMPORT_TABLE where shop_code in ( "+sqlCheck+" )";

                    if (log.isDebugEnabled())
                        log.debug("sql "+sql_);

                    ps = dbDyn.prepareStatement(sql_);
                    ps.executeUpdate();
                    ps.close();
                    ps = null;
                }
            }

            if (log.isDebugEnabled())
                log.debug("Start unmarshalling data");

            if (prices == null)
                throw new PriceException("������ ������� ����� �������. ��� ������ #10.03");

            int batchLoop = 0;
            int count = 0;

            sql_ =
                    "insert into WM_PRICE_IMPORT_TABLE " +
                    "(is_group, id, id_main, name, price, currency, is_to_load, shop_code, ID_UPLOAD_PRICE) " +
                    "values (?,?,?,?,?,?,?,?,?)";

            Long id_upload_session = null;

            for (int j = 0; j < prices.getPriceListCount(); j++)
            {
                PriceListType price = prices.getPriceList(j);
                if (log.isDebugEnabled())
                {
                    log.debug("shopCode " + price.getShopCode());
                    log.debug("Size vector: " + price.getItemCount());
                }

                for (int i = 0; (i < price.getItemCount()) && (count < 5000); i++, count++)
                {
                    if (ps == null)
                        ps = dbDyn.prepareStatement(sql_);

                    PriceListItemType item = price.getItem(i);
                    debugItem = item;

                    ps.setInt(1, Boolean.TRUE.equals(item.getIsGroup())?1:0 );
                    RsetTools.setLong(ps, 2, item.getItemID() );
                    RsetTools.setLong(ps, 3, item.getParentID() );
                    ps.setString(4, item.getNameItem() );
                    RsetTools.setDouble(ps, 5, item.getPrice() );
                    ps.setString(6, item.getCurr());
                    ps.setString(7, item.getIsLoad().toString() );
                    ps.setString(8, price.getShopCode().toUpperCase() );
                    RsetTools.setLong(ps, 9, id_upload_session);

                    if (dbDyn.getIsBatchUpdate())
                    {
                        ps.addBatch();

//log.debug("To batch: "+batchLoop);

                        if (++batchLoop >= 200)
                        {
//log.debug("#1.001");
                            int[] updateCounts = ps.executeBatch();
                            ps.close();
                            ps = null;
                            batchLoop = 0;
                        }
                    }
                    else
                        ps.executeUpdate();

                } // for( int i=0...)
            } // for (int j=0...)

            if ( dbDyn.getIsBatchUpdate())
            {
                if (ps != null) // Not all record is flushed to DB
                {
                    int[] updateCounts = ps.executeBatch();
                    ps.close();
                    ps = null;
                }
            }

            ImportPriceProcess.process( dbDyn, id_site );

            dbDyn.commit();

        }
        catch (Exception e)
        {
            if (debugItem!=null)
            {
                log.error("debugItem.getIsGroup() "+ (Boolean.TRUE.equals(debugItem.getIsGroup())?1:0) );
                log.error("debugItem.getItemID() "+ debugItem.getItemID() );
                log.error("debugItem.getParentID() "+ debugItem.getParentID() );
                log.error("debugItem.getNameItem() "+ debugItem.getNameItem() );
                log.error("debugItem.getPrice() "+ debugItem.getPrice() );
                log.error("debugItem.getCurr() "+ debugItem.getCurr());
                log.error("debugItem.getIsLoad().toString() "+ debugItem.getIsLoad().toString() );
            }
            else
                log.error("debugItem is null");

            log.error("sql:\n"+sql_);
            final String es = "error process import price-list";
            log.error( es, e );
            try {
                dbDyn.rollback();
            }
            catch (Exception e11) {
            }

            throw new PriceException( es, e );
        }
        finally
        {
            DatabaseManager.close( ps );
            ps = null;
        }
    }

}
