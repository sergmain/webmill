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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import org.riverock.commerce.bean.Shop;
import org.riverock.commerce.bean.ShopItem;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.annotation.schema.db.CustomSequence;
import org.riverock.common.tools.RsetTools;

/**
 * @author Sergei Maslyukov
 *         Date: 01.09.2006
 *         Time: 21:45:03
 *         <p/>
 *         $Id: PriceCurrency.java 950 2006-09-01 18:11:51Z serg_main $
 */
public class ShopDaoImpl implements ShopDao {
    private final static Logger log = Logger.getLogger( ShopDaoImpl.class );

    public Shop getShop(Long shopId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement(
                "select ID_SHOP, NAME_SHOP, IS_CLOSE, IS_PROCESS_INVOICE, ID_TYPE_SHOP_1, ID_TYPE_SHOP_2, " +
                "       NAME_SHOP_FOR_PRICE_LIST, IS_NEED_RECALC, ORDER_EMAIL, ID_SITE, CODE_SHOP, " +
                "       LAST_DATE_UPLOAD, DATE_CALC_QUANTITY, NEW_ITEM_DAYS, IS_ACTIVATE_EMAIL_ORDER, " +
                "       ID_CURRENCY, IS_DEFAULT_CURRENCY, IS_NEED_PROCESSING, COMMAS_COUNT, DISCOUNT, " +
                "       HEADER_STRING, FOOTER_STRING, ID_ORDER_CURRENCY " +
                "from   WM_PRICE_SHOP_LIST " +
                "where  ID_SHOP = ?"
            );
            RsetTools.setLong( ps, 1, shopId );
            rs = ps.executeQuery();

            if( rs.next() ) {
                Shop bean;
                bean = initShopBean(rs);

                return bean;
            }
            return null;
        }
        catch( Throwable e ) {
            final String es = "Exception create shop object";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db_, rs, ps );
        }
    }

    private Shop initShopBean(ResultSet rs) throws SQLException {
        Shop bean = new Shop();
        bean.setShopId( RsetTools.getLong( rs, "ID_SHOP" )  );
        bean.setSiteId( RsetTools.getLong( rs, "ID_SITE" ) );
        bean.setProcessInvoice(RsetTools.getInt( rs, "IS_PROCESS_INVOICE", 0) == 1);
        bean.setNeedProcessing(RsetTools.getInt( rs, "IS_NEED_PROCESSING", 0) == 1);
        bean.setNeedRecalc(RsetTools.getInt( rs, "IS_NEED_RECALC", 0) == 1) ;

        bean.setShopName(RsetTools.getString( rs, "NAME_SHOP" ));
        bean.setShopNameForPriceList(RsetTools.getString( rs, "NAME_SHOP_FOR_PRICE_LIST", "" ));

        bean.setFooter(RsetTools.getString( rs, "FOOTER_STRING" ));
        bean.setHeader(RsetTools.getString( rs, "HEADER_STRING" ));
        bean.setDateUpload(RsetTools.getTimestamp( rs, "LAST_DATE_UPLOAD" ));
        bean.setDateCalcQuantity(RsetTools.getTimestamp( rs, "DATE_CALC_QUANTITY" ));
        bean.setNewItemDays(RsetTools.getInt( rs, "NEW_ITEM_DAYS", 0));
        bean.setDigitsAfterComma(RsetTools.getInt( rs, "COMMAS_COUNT", 0));

        bean.setDefaultCurrencyId(RsetTools.getLong( rs, "ID_CURRENCY" ));
        bean.setInvoiceCurrencyId(RsetTools.getLong( rs, "ID_ORDER_CURRENCY" ));
        bean.setDiscount(RsetTools.getDouble( rs, "DISCOUNT", 0.0));

        if( bean.getDiscount() < 0 ) {
            bean.setDiscount(0);
        }

        if( bean.getDiscount() >= 100 ) {
            bean.setDiscount(99);
        }


        bean.setOpened(RsetTools.getInt( rs, "IS_CLOSE", 0)==0);

//        bean.setDefaultCurrency(RsetTools.getInt( rs, "IS_DEFAULT_CURRENCY", 0)==0);

        bean.setShopCode(RsetTools.getString( rs, "CODE_SHOP" ));
        bean.setId_type_shop_1(RsetTools.getLong( rs, "ID_TYPE_SHOP_1" ));
        bean.setId_type_shop_2(RsetTools.getLong( rs, "ID_TYPE_SHOP_2" ));

        bean.getPrecisionList().initCurrencyPrecision(bean.getShopId() );
        return bean;
    }

    /**
     *
     * @param shop shop object to create
     * @return PK value
     */
    public Long createShop(Shop shop) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            CustomSequence seq = new CustomSequence();
            seq.setSequenceName( "seq_WM_PRICE_SHOP_LIST" );
            seq.setTableName( "WM_PRICE_SHOP_LIST" );
            seq.setColumnName( "ID_SHOP" );
            Long id = adapter.getSequenceNextValue( seq );

            String sql_ =
                "insert into WM_PRICE_SHOP_LIST"+
                "(ID_SHOP, IS_CLOSE, IS_PROCESS_INVOICE, " +
                "       NAME_SHOP_FOR_PRICE_LIST, IS_NEED_RECALC, ID_SITE, CODE_SHOP, " +
                "       ID_CURRENCY, IS_DEFAULT_CURRENCY, IS_NEED_PROCESSING, COMMAS_COUNT, DISCOUNT, " +
                "       HEADER_STRING, FOOTER_STRING, ID_ORDER_CURRENCY, NAME_SHOP ) "+
                "values "+
                "( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

            ps = adapter.prepareStatement(sql_);

            ps.setLong(1, id );

            // TODO is_closed == (!isOpened)
            ps.setInt(2, shop.isOpened()?0:1 );
            ps.setInt(3, shop.isProcessInvoice()?1:0 );
            ps.setString(4, shop.getShopNameForPriceList() );
            ps.setInt(5, shop.isNeedRecalc()?1:0 );
            ps.setLong(6, shop.getSiteId() );
            ps.setString(7, shop.getShopCode() );
            ps.setLong(8, shop.getDefaultCurrencyId() );
            ps.setInt(9, shop.isDefaultCurrency()?1:0 );
            ps.setInt(10, shop.isNeedProcessing()?1:0 );
            ps.setInt(11, shop.getDigitsAfterComma() );
            ps.setBigDecimal(12, new BigDecimal(shop.getDiscount()) );
            ps.setString(13, shop.getShopCode() );
            ps.setString(14, shop.getShopCode() );
            ps.setLong(15, shop.getDefaultCurrencyId() );
            ps.setString(16, shop.getShopName() );

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
            String es = "Error create shop";
            log.error(es, e);
            throw new IllegalStateException( es, e);
        } finally {
            DatabaseManager.close(adapter, rs, ps);
        }
    }

    public void updateShop(Shop shop) {
        if (shop ==null) {
            return;
        }

        String sql_ =
            "update WM_PRICE_SHOP_LIST "+
            "set "+
            "    IS_CLOSE=? , " +
                "IS_PROCESS_INVOICE=? , " +
            "    NAME_SHOP_FOR_PRICE_LIST=? , " +
                "IS_NEED_RECALC=? , " +
                "ID_SITE=? , " +
                "CODE_SHOP=? , " +
            "    ID_CURRENCY=? , " +
                "IS_DEFAULT_CURRENCY=? , " +
                "IS_NEED_PROCESSING=? , " +
                "COMMAS_COUNT=? , " +
                "DISCOUNT=? , " +
            "    HEADER_STRING=? , " +
                "FOOTER_STRING=? , " +
                "ID_ORDER_CURRENCY=? , " +
                "NAME_SHOP=? "+
            "where ID_SHOP=?";

        PreparedStatement ps = null;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();

            ps = adapter.prepareStatement(sql_);

            ps = adapter.prepareStatement(sql_);

            // TODO is_closed == (!isOpened)
            ps.setInt(1, shop.isOpened()?0:1 );
            ps.setInt(2, shop.isProcessInvoice()?1:0 );
            ps.setString(3, shop.getShopNameForPriceList() );
            ps.setInt(4, shop.isNeedRecalc()?1:0 );
            ps.setLong(5, shop.getSiteId() );
            ps.setString(6, shop.getShopCode() );
            ps.setLong(7, shop.getDefaultCurrencyId() );
            ps.setInt(8, shop.isDefaultCurrency()?1:0 );
            ps.setInt(9, shop.isNeedProcessing()?1:0 );
            ps.setInt(10, shop.getDigitsAfterComma() );
            ps.setBigDecimal(11, new BigDecimal(shop.getDiscount()) );
            ps.setString(12, shop.getShopCode() );
            ps.setString(13, shop.getShopCode() );
            ps.setLong(14, shop.getDefaultCurrencyId() );
            ps.setString(15, shop.getShopName() );

            // prepare PK
            ps.setLong(16, shop.getShopId() );

            int i = ps.executeUpdate();
            if (log.isDebugEnabled()) {
                log.debug("count of updated records; " + i+", PK: " + shop.getShopId());
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
            String es = "Error update shop";
            log.error( es, e );
            throw new IllegalStateException( es, e );
       }
       finally {
            DatabaseManager.close(adapter, ps);
       }
    }

    public void deleteShop(Long shopId) {
        if (shopId==null) {
            return;
        }

        DatabaseAdapter dbDyn = null;
        try {
            dbDyn = DatabaseAdapter.getInstance();

            DatabaseManager.runSQL(
                dbDyn,
                "update WM_PRICE_SHOP_LIST set IS_CLOSE=1 where ID_SHOP=?",
                new Object[]{shopId}, new int[]{Types.DECIMAL}
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
            String es = "Error set 'close' status of shop ";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( dbDyn);
        }
    }

    public List<Shop> getShopList(Long siteId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement(
                "select ID_SHOP, NAME_SHOP, IS_CLOSE, IS_PROCESS_INVOICE, ID_TYPE_SHOP_1, ID_TYPE_SHOP_2, " +
                "       NAME_SHOP_FOR_PRICE_LIST, IS_NEED_RECALC, ORDER_EMAIL, ID_SITE, CODE_SHOP, " +
                "       LAST_DATE_UPLOAD, DATE_CALC_QUANTITY, NEW_ITEM_DAYS, IS_ACTIVATE_EMAIL_ORDER, " +
                "       ID_CURRENCY, IS_DEFAULT_CURRENCY, IS_NEED_PROCESSING, COMMAS_COUNT, DISCOUNT, " +
                "       HEADER_STRING, FOOTER_STRING, ID_ORDER_CURRENCY " +
                "from   WM_PRICE_SHOP_LIST " +
                "where  ID_SITE = ?"
            );
            RsetTools.setLong( ps, 1, siteId );
            rs = ps.executeQuery();

            List<Shop> list = new ArrayList<Shop>();
            while ( rs.next() ) {
                Shop bean = initShopBean(rs);
                list.add(bean);
            }
            return list;
        }
        catch( Throwable e ) {
            final String es = "Error get list of shops";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close( db_, rs, ps );
        }
    }

    public ShopItem getShopItem(Long shotItemId) {
        String sql =
            "select ID_ITEM, ID_SHOP, IS_GROUP, ID, ID_MAIN, ITEM, ABSOLETE, CURRENCY, QUANTITY, ADD_DATE, IS_SPECIAL, " +
            "       IS_MANUAL, ID_STORAGE_STATUS, PRICE " +
            "from   WM_PRICE_LIST where ID_ITEM=?";

        if (shotItemId==null)
            return null;

        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db = null;
        try {
            db = DatabaseAdapter.getInstance();
            ps = db.prepareStatement(sql);
            ps.setLong(1, shotItemId);

            rs = ps.executeQuery();
            if (rs.next()) {
                return fillShopItem(rs);
            }
            return null;
        }
        catch( Throwable e ) {
            final String es = "Error get shop item";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
        finally {
            DatabaseManager.close(db, rs, ps);
            rs = null;
            ps = null;
        }
    }

    private static ShopItem fillShopItem(ResultSet rs) throws java.sql.SQLException {
        ShopItem item = new ShopItem();

        long tempLong;
        int tempBoolean;
        String tempString;
        int tempInt;
        java.sql.Timestamp tempTimestamp = null;

        tempLong = rs.getLong( "ID_ITEM");
        item.setItemId( tempLong );
        tempLong = rs.getLong( "ID_SHOP");
        if (!rs.wasNull())
            item.setShopId( tempLong );
        tempBoolean = rs.getInt( "IS_GROUP");
        if (!rs.wasNull())
            item.setGroup( tempBoolean==1 );
        else
            item.setGroup( false );
        tempLong = rs.getLong( "ID");
        if (!rs.wasNull())
            item.setId( tempLong );
        tempLong = rs.getLong( "ID_MAIN");
        item.setIdMain( tempLong );
        tempString = rs.getString( "ITEM" );
        if (!rs.wasNull())
            item.setItem( tempString );
        tempInt = rs.getInt( "ABSOLETE");
        item.setObsolete( tempInt );
        tempString = rs.getString( "CURRENCY" );
        if (!rs.wasNull())
            item.setCurrency( tempString );
        tempLong = rs.getLong( "QUANTITY");
        if (!rs.wasNull())
            item.setQuantity( tempLong );
        tempTimestamp = rs.getTimestamp( "ADD_DATE" );
        if (!rs.wasNull())
            item.setAddDate( tempTimestamp );
        tempBoolean = rs.getInt( "IS_SPECIAL");
        if (!rs.wasNull())
            item.setSpecial( tempBoolean==1 );
        else
            item.setSpecial( false );
        tempBoolean = rs.getInt( "IS_MANUAL");
        if (!rs.wasNull())
            item.setManual( tempBoolean==1 );
        else
            item.setManual( false );
        tempLong = rs.getLong( "ID_STORAGE_STATUS");
        item.setIdStorageStatus( tempLong );

        item.setPrice( RsetTools.getBigDecimal(rs, "PRICE") );

        return item;
    }
}
