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
import java.util.Calendar;



import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.exception.GenericException;
import org.riverock.generic.main.CacheFactory;
import org.riverock.sql.cache.SqlStatement;

/**
 * $Id$
 */
public final class Shop {
    private final static Logger log = Logger.getLogger( Shop.class );

    private static CacheFactory cache = new CacheFactory( Shop.class.getName() );

    public Long id_shop = null;
    public int is_close;
    public String name_shop;
    public String footer;
    public String header;
    public String code_shop = "";
    public String name_shop_for_price_list = "";
    public Calendar dateUpload;
    public Calendar dateCalcQuantity;
    public int newItemDays;

    // Валюта по умолчанию для отображения прайса
    public Long currencyID = null;
    // Валюта по умолчанию для отображения заказа
    public Long idOrderCurrency = null;

    public int is_default_currency = 1;

    public boolean isNeedProcessing = false;  // Нужен ли интерфейс для проведения финансовых транзакций
    public boolean isProcessInvoice = false; //  Нужен ли интерфейс для выписки счетов
    public boolean isNeedRecalc = false;//  Нужен ли интерфейс для пересчета из одной валюты в другую

//    public Vector precision = null;
    public CurrencyPrecisionList precisionList = new CurrencyPrecisionList();

    public double discount = 0;

    public Long id_type_shop_1 = null;
    public Long id_type_shop_2 = null;

    public static void reinit() {
        cache.reinit();
    }

    protected void finalize() throws Throwable {
        name_shop = null;
        footer = null;
        header = null;
        code_shop = null;
        name_shop_for_price_list = null;
//        order_email = null;
        dateUpload = null;
        dateCalcQuantity = null;

        super.finalize();
    }

    public Shop() {
    };

/*
    public static Long getShopID( DatabaseAdapter ora_, String codeShop )
        throws SQLException, DatabaseException {
        if( codeShop == null )
            return null;

        String sql_ = "select ID_SHOP from WM_PRICE_SHOP_LIST where CODE_SHOP = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = ora_.prepareStatement( sql_ );
            ps.setString( 1, codeShop.toUpperCase() );
            rs = ps.executeQuery();

            if( rs.next() )
                return RsetTools.getLong( rs, "ID_SHOP" );
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
        return null;
    }

    public static Long getShopID( DatabaseAdapter db_, PortletRequest portletRequest )
        throws Exception {

        if( portletRequest == null )
            return null;

        Long siteId = new Long( portletRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );

        String sql_ = "select ID_SHOP from WM_PRICE_SHOP_LIST where ID_SITE=?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = db_.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, siteId );
            rs = ps.executeQuery();

            if( rs.next() )
                return RsetTools.getLong( rs, "ID_SHOP" );
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
        return null;
    }
*/

    public static Shop getInstance( Long id ) throws PriceException {
        try {
            return ( Shop ) cache.getInstanceNew( id );
        }
        catch( GenericException genericException ) {
            final String es = "Exception in ";
            log.error( es, genericException );
            throw new PriceException( es, genericException );
        }
    }

    static String sql_ = null;

    static {
        sql_ =
            "select * from WM_PRICE_SHOP_LIST where ID_SHOP = ?";

        try {
            SqlStatement.registerSql( sql_, Shop.class );
        }
        catch( Exception e ) {
            log.error( "Exception in registerSql, sql\n" + sql_, e );
        }
        catch( Error e ) {
            log.error( "Error in registerSql, sql\n" + sql_, e );
        }
    }

    public Shop( Long id_shop_ ) throws PriceException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, id_shop_ );
            rs = ps.executeQuery();

            if( rs.next() ) {
                this.id_shop = id_shop_;

                isProcessInvoice = RsetTools.getInt( rs, "IS_PROCESS_INVOICE", 0) == 1;
                isNeedProcessing = RsetTools.getInt( rs, "IS_NEED_PROCESSING", 0) == 1;
                isNeedRecalc = RsetTools.getInt( rs, "IS_NEED_RECALC", 0) == 1 ;

                name_shop = RsetTools.getString( rs, "NAME_SHOP" );
                name_shop_for_price_list = RsetTools.getString( rs, "NAME_SHOP_FOR_PRICE_LIST", "" );

                footer = RsetTools.getString( rs, "FOOTER_STRING" );
                header = RsetTools.getString( rs, "HEADER_STRING" );
                dateUpload = RsetTools.getCalendar( rs, "LAST_DATE_UPLOAD" );
                dateCalcQuantity = RsetTools.getCalendar( rs, "DATE_CALC_QUANTITY" );
                newItemDays = RsetTools.getInt( rs, "NEW_ITEM_DAYS", 0);
                currencyID = RsetTools.getLong( rs, "ID_CURRENCY" );
                idOrderCurrency = RsetTools.getLong( rs, "ID_ORDER_CURRENCY" );
                discount = RsetTools.getDouble( rs, "DISCOUNT", 0.0);

                if( discount < 0 )
                    discount = 0;

                if( discount >= 100 )
                    discount = 99;


                is_close = RsetTools.getInt( rs, "IS_CLOSE", 0);

                code_shop = RsetTools.getString( rs, "CODE_SHOP" );
                id_type_shop_1 = RsetTools.getLong( rs, "ID_TYPE_SHOP_1" );
                id_type_shop_2 = RsetTools.getLong( rs, "ID_TYPE_SHOP_2" );
            }

            precisionList.initCurrencyPrecision( db_, id_shop );
        }
        catch( Throwable e ) {
            final String es = "Exception create shop object";
            log.error( es, e );
            throw new PriceException( es, e );
        }
        finally {
            DatabaseManager.close( db_, rs, ps );
            rs = null;
            ps = null;
            db_ = null;
        }
    }
}