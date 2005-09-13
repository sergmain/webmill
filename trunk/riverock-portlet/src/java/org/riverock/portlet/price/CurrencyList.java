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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.main.CacheFactory;
import org.riverock.generic.site.SiteListSite;
import org.riverock.generic.startup.StartupApplication;
import org.riverock.generic.tools.XmlTools;
import org.riverock.portlet.core.GetCashCurrencyWithIdSiteList;
import org.riverock.portlet.schema.core.CashCurrencyItemType;
import org.riverock.portlet.schema.core.CashCurrencyListType;
import org.riverock.portlet.schema.price.CustomCurrencyType;

/**
 * Author: mill
 * Date: Dec 9, 2002
 * Time: 11:41:02 AM
 *
 * $Id$
 */
public class CurrencyList
{
    private static Log log = LogFactory.getLog( CurrencyList.class );

    private static CacheFactory cache = new CacheFactory( CurrencyList.class.getName() );

    public CustomCurrencyType list = new CustomCurrencyType();

    public CurrencyList()
    {
    }

    public void reinit()
    {
        cache.reinit();
    }

    public static CurrencyList getInstance(DatabaseAdapter db__, long id__)
        throws Exception
    {
        return getInstance(db__, new Long(id__) );
    }

    public static CurrencyList getInstance(DatabaseAdapter db__, Long id__)
        throws PriceException
    {
        try {
            return (CurrencyList) cache.getInstanceNew(db__, id__);
        } catch (Throwable e) {
            String es = "Error in getInstance(DatabaseAdapter db__, Long id__)";
            log.error(es, e);
            throw new PriceException(es, e);
        }
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
    }

    public static void main(String args[]) throws Exception {

        StartupApplication.init();
        GenericConfig.setDefaultConnectionName( "MYSQL" );
        DatabaseAdapter db_ = DatabaseAdapter.getInstance();

        Long idSite = SiteListSite.getIdSite("me.askmore");

        long mills = System.currentTimeMillis();
        CurrencyList currList = CurrencyList.getInstance(db_, idSite);
        CustomCurrencyType list = currList.list;
        System.out.println("Currency list processed for "+(System.currentTimeMillis()-mills)+"ms.");

        final String fileName = GenericConfig.getGenericDebugDir()+"schema-currency.xml";
        System.out.println( "fileName = " + fileName );
        XmlTools.writeToFile( list, fileName );

        DatabaseAdapter.close(db_);
        db_ = null;
    }

}
