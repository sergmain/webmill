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

import org.apache.log4j.Logger;

import org.riverock.commerce.bean.ShopBean;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.common.tools.RsetTools;

/**
 * @author Sergei Maslyukov
 *         Date: 01.09.2006
 *         Time: 21:45:03
 *         <p/>
 *         $Id$
 */
public class ShopDaoImpl implements ShopDao {
    private final static Logger log = Logger.getLogger( ShopDaoImpl.class );

    public ShopBean getShop(Long shopId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement( "select * from WM_PRICE_SHOP_LIST where ID_SHOP = ?" );
            RsetTools.setLong( ps, 1, shopId );
            rs = ps.executeQuery();

            if( rs.next() ) {
                ShopBean bean = new ShopBean();
                bean.id_shop = shopId;

                bean.isProcessInvoice = RsetTools.getInt( rs, "IS_PROCESS_INVOICE", 0) == 1;
                bean.isNeedProcessing = RsetTools.getInt( rs, "IS_NEED_PROCESSING", 0) == 1;
                bean.isNeedRecalc = RsetTools.getInt( rs, "IS_NEED_RECALC", 0) == 1 ;

                bean.name_shop = RsetTools.getString( rs, "NAME_SHOP" );
                bean.name_shop_for_price_list = RsetTools.getString( rs, "NAME_SHOP_FOR_PRICE_LIST", "" );

                bean.footer = RsetTools.getString( rs, "FOOTER_STRING" );
                bean.header = RsetTools.getString( rs, "HEADER_STRING" );
                bean.dateUpload = RsetTools.getCalendar( rs, "LAST_DATE_UPLOAD" );
                bean.dateCalcQuantity = RsetTools.getCalendar( rs, "DATE_CALC_QUANTITY" );
                bean.newItemDays = RsetTools.getInt( rs, "NEW_ITEM_DAYS", 0);
                bean.currencyID = RsetTools.getLong( rs, "ID_CURRENCY" );
                bean.idOrderCurrency = RsetTools.getLong( rs, "ID_ORDER_CURRENCY" );
                bean.discount = RsetTools.getDouble( rs, "DISCOUNT", 0.0);

                if( bean.discount < 0 )
                    bean.discount = 0;

                if( bean.discount >= 100 )
                    bean.discount = 99;


                bean.is_close = RsetTools.getInt( rs, "IS_CLOSE", 0);

                bean.code_shop = RsetTools.getString( rs, "CODE_SHOP" );
                bean.id_type_shop_1 = RsetTools.getLong( rs, "ID_TYPE_SHOP_1" );
                bean.id_type_shop_2 = RsetTools.getLong( rs, "ID_TYPE_SHOP_2" );

                bean.precisionList.initCurrencyPrecision( db_, bean.id_shop );

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
}
