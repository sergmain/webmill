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

import org.riverock.sql.cache.SqlStatement;
import org.riverock.portlet.schema.price.CustomCurrencyType;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.tools.XmlTools;
import org.riverock.common.tools.MainTools;
import org.riverock.webmill.config.WebmillConfig;

import org.apache.log4j.Logger;

/**
 * User: serg_main
 * Date: 06.02.2004
 * Time: 17:07:00
 * @author Serge Maslyukov
 * $Id$
 */
public class CurrencyManager
{
    private static Logger log = Logger.getLogger( CurrencyManager.class );

    static
    {
        Class p = new CurrencyManager().getClass();
        SqlStatement.registerRelateClass( p, new CurrencyList().getClass());
    }

    private CustomCurrencyType currencyList = null;

    public CustomCurrencyType getCurrencyList()
    {
        return currencyList;
    }

    public CurrencyManager(){}

    private static Object syncDebug = new Object();
    public static CurrencyManager getInstance(DatabaseAdapter db_, Long idSite)
        throws PriceException
    {
        CurrencyManager currency  = new CurrencyManager();
        currency.currencyList = CurrencyList.getInstance(db_, idSite).list;

        long mills = 0; // System.currentTimeMillis();

        if (log.isInfoEnabled())
            mills = System.currentTimeMillis();

        if (log.isDebugEnabled())
        {
            synchronized(syncDebug)
            {
                try
                {
                    byte[] originByte = XmlTools.getXml( currency.currencyList, null );
                    MainTools.writeToFile(WebmillConfig.getWebmillDebugDir()+"debug-custom-currency-type.xml", originByte);
                }
                catch(Exception e)
                {
                    log.error("error write debug information",e);
                }
            }
        }

        if (log.isInfoEnabled())
        {
            log.info("init currency list for "+(System.currentTimeMillis()-mills)+" milliseconds");
        }
        return currency;
    }

    public void reinit()
    {
    }

    public void terminate(java.lang.Long id_)
    {
    }

}
