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

/**
 *  $Id$
 */
package org.riverock.portlet.price;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.exception.GenericException;
import org.riverock.generic.main.CacheFactory;
import org.riverock.generic.site.SiteListSite;

public class Shop
{
    private static Logger log = Logger.getLogger( Shop.class );

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

//    public int is_activate_email_order = 0;
//    public String order_email;

    // ������ �� ��������� ��� ����������� ������
    public Long currencyID = null;
    // ������ �� ��������� ��� ����������� ������
    public Long idOrderCurrency = null;

    public int is_default_currency = 1;

    public boolean isNeedProcessing = false;  // ����� �� ��������� ��� ���������� ���������� ����������
    public boolean isProcessInvoice= false; //  ����� �� ��������� ��� ������� ������
    public boolean isNeedRecalc = false;//  ����� �� ��������� ��� ��������� �� ����� ������ � ������

//    public Vector precision = null;
    public CurrencyPrecisionList precisionList = new CurrencyPrecisionList();

    public double discount = 0;

    public Long id_type_shop_1 = null;
    public Long id_type_shop_2 = null;

    public static void reinit()
    {
        cache.reinit();
    }

    protected void finalize() throws Throwable
    {
        name_shop = null;
        footer= null;
        header = null;
        code_shop = null;
        name_shop_for_price_list = null;
//        order_email = null;
        dateUpload = null;
        dateCalcQuantity = null;

        super.finalize();
    }

    public Shop(){};

    public static Long getShopID(DatabaseAdapter ora_, String codeShop)
            throws SQLException
    {
        if (codeShop == null)
            return null;

        String sql_ = "select ID_SHOP from PRICE_SHOP_TABLE where CODE_SHOP = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = ora_.prepareStatement(sql_);
            ps.setString(1, codeShop.toUpperCase());
            rs = ps.executeQuery();

            if (rs.next())
                return RsetTools.getLong(rs, "ID_SHOP");
        }
        finally
        {
            org.riverock.generic.db.DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
        return null;
    }

    public static Long getShopID(DatabaseAdapter db_, PortletRequest portletRequest)
            throws Exception
    {

        if (portletRequest == null)
            return null;

        Long idSite = SiteListSite.getIdSite(portletRequest.getServerName());

        String sql_ = "select ID_SHOP from PRICE_SHOP_TABLE where ID_SITE=?";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, idSite);
            rs = ps.executeQuery();

            if (rs.next())
                return RsetTools.getLong(rs, "ID_SHOP");
        }
        finally
        {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }
        return null;
    }

    public static Shop getInstance(DatabaseAdapter db_, long id)
        throws PriceException
    {
        return getInstance(db_, new Long(id) );
    }

    public static Shop getInstance(DatabaseAdapter db_, Long id)
        throws PriceException
    {
        try
        {
            return (Shop) cache.getInstanceNew(db_, id);
        }
        catch (GenericException genericException)
        {
            log.error("Exception in ", genericException);
            throw new PriceException(genericException.getMessage());
        }
    }

    static String sql_ = null;
    static
    {
        sql_ =
            "select * from PRICE_SHOP_TABLE where ID_SHOP = ?";

        try {
            org.riverock.sql.cache.SqlStatement.registerSql( sql_, new Shop().getClass() );
        } catch (Exception e) {
            log.error( "Exception in registerSql, sql\n" + sql_, e );
        } catch (Error e) {
            log.error( "Error in registerSql, sql\n" + sql_, e );
        }
    }

    public Shop(DatabaseAdapter db_, Long id_shop_)
            throws PriceException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            ps = db_.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, id_shop_);
            rs = ps.executeQuery();

            if (rs.next())
            {
                this.id_shop = id_shop_;

                isProcessInvoice = (RsetTools.getInt(rs, "IS_PROCESS_INVOICE", new Integer(0)).intValue()==1?true:false);
                isNeedProcessing = (RsetTools.getInt(rs, "IS_NEED_PROCESSING", new Integer(0)).intValue()==1?true:false);
                isNeedRecalc = (RsetTools.getInt(rs, "IS_NEED_RECALC", new Integer(0)).intValue()==1?true:false);

                name_shop = RsetTools.getString(rs, "NAME_SHOP");
                name_shop_for_price_list = RsetTools.getString(rs, "NAME_SHOP_FOR_PRICE_LIST", "");

                footer = RsetTools.getString(rs, "FOOTER_STRING");
                header = RsetTools.getString(rs, "HEADER_STRING");

//                order_email = RsetTools.getString(rs, "ORDER_EMAIL");
                dateUpload = RsetTools.getCalendar(rs, "LAST_DATE_UPLOAD");
                dateCalcQuantity = RsetTools.getCalendar(rs, "DATE_CALC_QUANTITY");
                newItemDays = RsetTools.getInt(rs, "NEW_ITEM_DAYS", new Integer(0) ).intValue();
//                is_activate_email_order = RsetTools.getInt(rs, "IS_ACTIVATE_EMAIL_ORDER");

                currencyID = RsetTools.getLong(rs, "ID_CURRENCY");

                idOrderCurrency =
                        RsetTools.getLong(rs, "ID_ORDER_CURRENCY");

//                defaultCurrency = CurrencyItem.getCurrency(db_,
//                    RsetTools.getLong(rs, "ID_CURRENCY")
//                );

//                is_default_currency = RsetTools.getInt(rs, "is_default_currency");

                discount = RsetTools.getDouble(rs, "DISCOUNT", new Double(0)).doubleValue();

                if (discount <0)
                    discount = 0;

                if (discount >=100)
                    discount = 99;


                is_close = RsetTools.getInt(rs, "IS_CLOSE", new Integer(0) ).intValue();

                code_shop = RsetTools.getString(rs, "CODE_SHOP");
                id_type_shop_1 = RsetTools.getLong(rs, "ID_TYPE_SHOP_1");
                id_type_shop_2 = RsetTools.getLong(rs, "ID_TYPE_SHOP_2");
            }
        }
        catch (Exception e)
        {
            log.error("Exception create shop object", e);
            throw new PriceException(e.toString());
        }
        catch (Error e)
        {
            log.error("Eror create shop object", e);
            throw new PriceException(e.toString());
        }
        finally
        {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }

        precisionList.initCurrencyPrecision(db_, id_shop);
    }
}