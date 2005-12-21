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

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: Admin
 * Date: Dec 19, 2002
 * Time: 3:19:17 PM
 *
 * $Id$
 */
public class PriceItemImage
{
    private static Log log = LogFactory.getLog( PriceItemImage.class );

    private static Map imageMap = new HashMap();
    private Map image = new HashMap();

    public PriceItemImage(){}

    public String getItemImage( Long id )
    {
        return (String)image.get( id );
    }

    public void reinit()
    {
        synchronized(syncObject){
            imageMap.clear();
        }
        lastReadData = 0;
    }

    public void terminate(java.lang.Long id_)
    {
        synchronized(syncObject){
            imageMap.clear();
        }
        lastReadData = 0;
    }

    private static long lastReadData = 0;
    private final static long LENGTH_TIME_PERIOD = 100000;
    private static Object syncObject = new Object();

    public static PriceItemImage getInstance( Long siteId )
        throws PriceException
    {
        PriceItemImage image = null;
        if (siteId!=null)
            image = (PriceItemImage)imageMap.get(siteId);


        synchronized(syncObject)
        {
            if (((System.currentTimeMillis() - lastReadData) > LENGTH_TIME_PERIOD)
                || (image == null))
            {
                if (log.isDebugEnabled())log.debug("#15.01.03 reinit cached value ");

                image = new PriceItemImage( siteId );
                imageMap.put(siteId, image);
            }
            else
                if (log.isDebugEnabled()) log.debug("Get from cache");

        }
        lastReadData = System.currentTimeMillis();
        return image;
    }

    static String sql_ = null;
    static
    {
        sql_ =
            "select c.ID_ITEM, c.ID_IMAGE_DIR, d.NAME_FILE IMAGE_FILE_NAME "+
            "from   WM_PRICE_LIST a, WM_PRICE_SHOP_LIST b, WM_IMAGE_PRICE_ITEMS c, WM_IMAGE_DIR d "+
            "where  c.ID_IMAGE_DIR = d.ID_IMAGE_DIR and a.ID_SHOP=b.ID_SHOP and " +
            "       b.ID_SITE=? and a.ID_ITEM=c.ID_ITEM ";

        try
        {
            org.riverock.sql.cache.SqlStatement.registerSql( sql_, new PriceItemImage().getClass() );
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

    private PriceItemImage( Long idSite )
        throws PriceException
    {
        PreparedStatement ps = null;
        ResultSet rs = null;

        DatabaseAdapter db_ = null;
        try
        {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement( sql_ );

            RsetTools.setLong(ps, 1, idSite);

            rs = ps.executeQuery();
            while (rs.next()) {
                image.put( RsetTools.getLong(rs, "ID_ITEM"), RsetTools.getString(rs, "IMAGE_FILE_NAME") );
            }
        }
        catch(Throwable e) {
            String es = "Exception in PriceItemImage()";
            log.error(es, e);
            throw new PriceException(es, e);
        }
        finally
        {
            DatabaseManager.close( db_, rs, ps);
            rs = null;
            ps = null;
            db_ = null;
        }
    }
}
