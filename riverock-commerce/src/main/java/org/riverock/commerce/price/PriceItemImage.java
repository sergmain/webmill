/*
 * org.riverock.commerce - Commerce application
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.commerce.price;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;



import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.sql.cache.SqlStatement;

/**
 * User: Admin
 * Date: Dec 19, 2002
 * Time: 3:19:17 PM
 * <p/>
 * $Id$
 */
public class PriceItemImage {
    private static Logger log = Logger.getLogger( PriceItemImage.class );

    private static Map<Long, PriceItemImage> imageMap = new HashMap<Long, PriceItemImage>();
    private Map<Long, String> image = new HashMap<Long, String>();

    public PriceItemImage() {
    }

    public String getItemImage( Long id ) {
        return ( String ) image.get( id );
    }

    public void reinit() {
        synchronized( syncObject ) {
            imageMap.clear();
        }
        lastReadData = 0;
    }

    public void terminate( java.lang.Long id_ ) {
        synchronized( syncObject ) {
            imageMap.clear();
        }
        lastReadData = 0;
    }

    private static long lastReadData = 0;
    private final static long LENGTH_TIME_PERIOD = 100000;
    private static Object syncObject = new Object();

    public static PriceItemImage getInstance( Long siteId )
        throws PriceException {
        PriceItemImage image = null;
        if( siteId != null )
            image = ( PriceItemImage ) imageMap.get( siteId );


        synchronized( syncObject ) {
            if( ( ( System.currentTimeMillis() - lastReadData ) > LENGTH_TIME_PERIOD )
                || ( image == null ) ) {
                if( log.isDebugEnabled() ) log.debug( "#15.01.03 reinit cached value " );

                image = new PriceItemImage( siteId );
                imageMap.put( siteId, image );
            }
            else if( log.isDebugEnabled() ) log.debug( "Get from cache" );

        }
        lastReadData = System.currentTimeMillis();
        return image;
    }

    static String sql_ = null;
    static {
        sql_ =
            "select c.ID_ITEM, c.ID_IMAGE_DIR, d.NAME_FILE IMAGE_FILE_NAME " +
            "from   WM_PRICE_LIST a, WM_PRICE_SHOP_LIST b, WM_IMAGE_PRICE_ITEMS c, WM_IMAGE_DIR d " +
            "where  c.ID_IMAGE_DIR = d.ID_IMAGE_DIR and a.ID_SHOP=b.ID_SHOP and " +
            "       b.ID_SITE=? and a.ID_ITEM=c.ID_ITEM ";

        try {
            SqlStatement.registerSql( sql_, PriceItemImage.class );
        }
        catch( Throwable e ) {
            log.error( "Exception in registerSql, sql\n" + sql_, e );
            // Todo throw RuntimeException
        }
    }

    private PriceItemImage( Long idSite )
        throws PriceException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            ps = db_.prepareStatement( sql_ );

            RsetTools.setLong( ps, 1, idSite );

            rs = ps.executeQuery();
            while( rs.next() ) {
                image.put( RsetTools.getLong( rs, "ID_ITEM" ), RsetTools.getString( rs, "IMAGE_FILE_NAME" ) );
            }
        }
        catch( Throwable e ) {
            String es = "Exception in PriceItemImage()";
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
