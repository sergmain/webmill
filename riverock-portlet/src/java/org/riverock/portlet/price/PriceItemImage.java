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

 * Time: 3:19:17 PM

 *

 * $Id$

 */

package org.riverock.portlet.price;



import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.HashMap;



import org.riverock.common.config.ConfigException;

import org.riverock.common.tools.RsetTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.exception.DatabaseException;



import org.apache.log4j.Logger;



public class PriceItemImage

{

    private static Logger log = Logger.getLogger( PriceItemImage.class );



    private HashMap image = new HashMap();



    public PriceItemImage(){}



    public String getItemImage( Long id )

    {

        return (String)image.get( id );

    }



    public void reinit()

    {

        lastReadData = 0;

    }



    public void terminate(java.lang.Long id_)

    {

        lastReadData = 0;

    }



    private static long lastReadData = 0;

    private final static long LENGTH_TIME_PERIOD = 100000;

    private static PriceItemImage backupObject = null;

    private static Object syncObject = new Object();



    public static PriceItemImage getInstance( Long idSite )

        throws PriceException

    {

        if (log.isDebugEnabled())

        {

            log.debug("#15.01.01 lastReadData: " + lastReadData + ", current " + System.currentTimeMillis());

            log.debug("#15.01.02 LENGTH_TIME_PERIOD " + LENGTH_TIME_PERIOD + ", status " +

                (((System.currentTimeMillis() - lastReadData) > LENGTH_TIME_PERIOD)

                || (backupObject == null))

            );

        }



        if (((System.currentTimeMillis() - lastReadData) > LENGTH_TIME_PERIOD)

            || (backupObject == null))

        {

            synchronized(syncObject)

            {

                if (((System.currentTimeMillis() - lastReadData) > LENGTH_TIME_PERIOD)

                        || (backupObject == null))

                {

                    if (log.isDebugEnabled())

                    {

                        log.debug("#15.01.03 reinit cached value ");

                        log.debug("#15.01.04 old value " + backupObject);

                    }



                    backupObject = null;

                    backupObject = new PriceItemImage( idSite );



                    if (log.isDebugEnabled())

                        log.debug("#15.01.05 new value " + backupObject);

                }

                else

                {

                    if (log.isDebugEnabled())

                        log.debug("Get from cache");

                }



                if (log.isDebugEnabled())

                    log.debug("#15.01.09 ret value " + backupObject);



            }

        }

        lastReadData = System.currentTimeMillis();

        return backupObject;

    }



    static String sql_ = null;

    static

    {

        sql_ =

            "select c.ID_ITEM, c.ID_IMAGE_DIR, d.NAME_FILE IMAGE_FILE_NAME "+

            "from PRICE_LIST a, PRICE_SHOP_TABLE b, IMAGE_PRICE_ITEMS c, IMAGE_DIR d "+

            "where c.ID_IMAGE_DIR = d.ID_IMAGE_DIR and a.ID_SHOP=b.ID_SHOP and " +

            "b.ID_SITE=? and a.ID_ITEM=c.ID_ITEM ";



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

            db_ = DatabaseAdapter.getInstance(false);

            ps = db_.prepareStatement( sql_ );



            RsetTools.setLong(ps, 1, idSite);



            rs = ps.executeQuery();

            while (rs.next())

            {

                image.put(

                    RsetTools.getLong(rs, "ID_ITEM"),

                    RsetTools.getString(rs, "IMAGE_FILE_NAME")

                );

            }

        }

        catch(DatabaseException e)

        {

            log.error("Exception in PriceItemImage(...", e);

            throw new PriceException(e.getMessage());

        }

        catch(ConfigException e)

        {

            log.error("Exception in PriceItemImage(...", e);

            throw new PriceException(e.getMessage());

        }

        catch(SQLException e)

        {

            log.error("Exception in PriceItemImage(...", e);

            throw new PriceException(e.getMessage());

        }

        catch(Error e)

        {

            log.error("Error in PriceItemImage(...", e);

            throw e;

        }

        finally

        {

            DatabaseManager.close( db_);

            rs = null;

            ps = null;

            db_ = null;

        }

    }

}

