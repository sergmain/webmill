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
 * User: Admin
 * Date: Dec 19, 2002
 * Time: 2:35:54 PM
 *
 * $Id$
 */
package org.riverock.portlet.price;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.common.tools.RsetTools;

import org.apache.log4j.Logger;

public class PriceItemDescription
{
    private static Logger log = Logger.getLogger(PriceItemDescription.class);

    private static Map descMap = new HashMap();
    private Map description = new HashMap();

    public String getItemDescription(long id)
    {
        return (String)description.get( new Long(id) );
    }

    public void reinit()
    {
        synchronized(syncObject){
            descMap.clear();
        }
        lastReadData = 0;
    }

    public void terminate(java.lang.Long id_)
    {
        synchronized(syncObject){
            descMap.clear();
        }
        lastReadData = 0;
    }


    private static long lastReadData = 0;
    private final static long LENGTH_TIME_PERIOD = 10000;
    private static Object syncObject = new Object();

    public static PriceItemDescription getInstance( Long siteId )
        throws PriceException
    {
        PriceItemDescription desc = null;
        if (siteId!=null)
            desc = (PriceItemDescription)descMap.get(siteId);


        synchronized(syncObject)
        {
            if (((System.currentTimeMillis() - lastReadData) > LENGTH_TIME_PERIOD)
                || (desc == null))
            {
                if (log.isDebugEnabled())log.debug("#15.01.03 reinit cached value ");

                desc = new PriceItemDescription( siteId );
                descMap.put(siteId, desc);
            }
            else
                if (log.isDebugEnabled()) log.debug("Get from cache");

        }
        lastReadData = System.currentTimeMillis();
        return desc;
    }

    static String sql_ = null;
    static
    {
        sql_ =
            "select a.ID_ITEM, a.TEXT " +
            "from PRICE_ITEM_DESCRIPTION a, PRICE_LIST b, PRICE_SHOP_TABLE c "+
            "where a.ID_ITEM=b.ID_ITEM and b.ID_SHOP=c.ID_SHOP and c.ID_SITE=?";

        try
        {
            org.riverock.sql.cache.SqlStatement.registerSql( sql_, new PriceItemDescription().getClass() );
        }
        catch(Exception e)
        {
            log.error("Exception in registerSql, sql\n"+sql_, e);
        }
        catch(Error e)
        {
            log.error("Error in registerSql, sql\n"+sql_, e);
        }
    }

    public PriceItemDescription(){}

    private PriceItemDescription( Long idSite )
        throws PriceException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;

        DatabaseAdapter db_ = null;
        try{
            db_ = DatabaseAdapter.getInstance(false);
            ps = db_.prepareStatement( sql_ );

            RsetTools.setLong(ps, 1, idSite);

            rs = ps.executeQuery();
            while (rs.next()){
                description.put(RsetTools.getLong(rs, "ID_ITEM"),RsetTools.getString(rs, "TEXT"));
            }
        }
        catch(Throwable e){
            String es = "Exception in PriceItemDescription()";
            log.error(es, e);
            throw new PriceException(es, e);
        }
        finally{
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;

            DatabaseAdapter.close( db_);
            db_ = null;
        }
    }
}
