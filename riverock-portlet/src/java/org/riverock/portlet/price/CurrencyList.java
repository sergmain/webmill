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

 * Author: mill

 * Date: Dec 9, 2002

 * Time: 11:41:02 AM

 *

 * $Id$

 */



package org.riverock.portlet.price;



import java.io.FileWriter;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.site.SiteListSite;

import org.riverock.generic.main.CacheFactory;

import org.riverock.portlet.core.GetCashCurrencyWithIdSiteList;

import org.riverock.portlet.schema.core.CashCurrencyItemType;

import org.riverock.portlet.schema.core.CashCurrencyListType;

import org.riverock.portlet.schema.price.CustomCurrencyType;

import org.riverock.webmill.config.WebmillConfig;



import org.apache.log4j.Logger;

import org.exolab.castor.xml.Marshaller;



public class CurrencyList

{

    private static Logger log = Logger.getLogger( CurrencyList.class );



    private static CacheFactory cache = new CacheFactory( CurrencyList.class.getName() );



    public CustomCurrencyType list = new CustomCurrencyType();



    public CurrencyList()

    {

    }



    public static CurrencyList getInstance(DatabaseAdapter db__, long id__)

        throws Exception

    {

        return getInstance(db__, new Long(id__) );

    }



    public static CurrencyList getInstance(DatabaseAdapter db__, Long id__)

        throws Exception

    {

        return (CurrencyList) cache.getInstanceNew(db__, id__);

    }



    private static void fillRealCurrencyData( CustomCurrencyType currency )

            throws Exception

    {

        if (currency==null)

            return;



        for (int i=0; i< currency.getCurrencyListCount(); i++)

        {

            CurrencyItem item = (CurrencyItem)currency.getCurrencyList( i );

            item.fillRealCurrencyData( currency.getStandardCurrencyList() );



        }

    }



    static

    {

        try

        {

            org.riverock.sql.cache.SqlStatement.registerRelateClass( new CurrencyList().getClass(), new GetCashCurrencyWithIdSiteList().getClass());

        }

        catch (Exception exception)

        {

            log.error("Exception in ", exception);

        }

    }

    public CurrencyList(DatabaseAdapter db_, Long idSite)

            throws Exception

    {

        try

        {

            CashCurrencyListType currList = GetCashCurrencyWithIdSiteList.getInstance(db_, idSite).item;

            for (int i=0; i<currList.getCashCurrencyCount(); i++)

            {

                CashCurrencyItemType item = currList.getCashCurrency(i);

                CurrencyItem currency = new CurrencyItem(db_, item);

                list.addCurrencyList(currency);

                list.setStandardCurrencyList( CurrencyService.getStandardCurrencyList(db_) );

            }

        }

        catch (Error e)

        {

            log.error("Error get currency list", e);

            throw e;

        }

        catch (Exception e)

        {

            log.error("Exception get currency list", e);

            throw e;

        }

        fillRealCurrencyData( list );

/*

        String sql_ =

            "select ID_CURRENCY, CURRENCY, IS_USED, NAME_CURRENCY, IS_USE_STANDART, " +

            "PERCENT_VALUE, ID_STANDART_CURS, ID_SITE " +

            "from CASH_CURRENCY " +

            "where ID_SITE=?";



        PreparedStatement ps = null;

        ResultSet rs = null;

        try

        {

            org.riverock.sql.cache.SqlStatement.registerSql( sql_, this.getClass() );



            ps = db_.prepareStatement(sql_);

            RsetTools.setLong(ps, 1, idSite.longValue());



            rs = ps.executeQuery();



            while (rs.next())

            {

                CurrencyItem currency = new CurrencyItem();

                currency.set(db_, rs);



                list.addCurrencyList(currency);

                list.setStandardCurrencyList( CurrencyService.getStandardCurrencyList(db_) );

            }

        }

        catch (Exception e)

        {

            log.error("Error get currency list", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }

        fillRealCurrencyData( list );

*/

    }



    public static void main(String args[])

            throws Exception

    {

        org.riverock.generic.startup.StartupApplication.init();



        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false);



        Long idSite = SiteListSite.getIdSite("me.askmore");



        long mills = System.currentTimeMillis();

        CurrencyList currList = CurrencyList.getInstance(db_, idSite);

        CustomCurrencyType list = currList.list;

        System.out.println("Currency list processed for "+(System.currentTimeMillis()-mills)+"ms.");



        FileWriter w = new FileWriter( WebmillConfig.getWebmillDebugDir()+"schema-currency.xml" );

        Marshaller.marshal(list, w);



        w.flush();

        w.close();

        w = null;

        DatabaseAdapter.close(db_);

        db_ = null;

    }



}

