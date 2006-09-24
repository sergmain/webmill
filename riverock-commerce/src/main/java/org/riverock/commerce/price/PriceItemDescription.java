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
 * Time: 2:35:54 PM
 *
 * $Id$
 */
public class PriceItemDescription {
    private static Logger log = Logger.getLogger( PriceItemDescription.class );

    private static Map<Long, PriceItemDescription> descMap = new HashMap<Long, PriceItemDescription>();
    private Map<Long, String> description = new HashMap<Long, String>();

    public String getItemDescription( long id ) {
        return ( String ) description.get( id );
    }

    public void reinit() {
        synchronized( syncObject ) {
            descMap.clear();
        }
        lastReadData = 0;
    }

    public void terminate( java.lang.Long id_ ) {
        synchronized( syncObject ) {
            descMap.clear();
        }
        lastReadData = 0;
    }


    private static long lastReadData = 0;
    private final static long LENGTH_TIME_PERIOD = 10000;
    private static Object syncObject = new Object();

    public static PriceItemDescription getInstance( Long siteId )
        throws PriceException {
        PriceItemDescription desc = null;
        if( siteId != null )
            desc = ( PriceItemDescription ) descMap.get( siteId );


        synchronized( syncObject ) {
            if( ( ( System.currentTimeMillis() - lastReadData ) > LENGTH_TIME_PERIOD )
                || ( desc == null ) ) {
                if( log.isDebugEnabled() ) log.debug( "#15.01.03 reinit cached value " );

                desc = new PriceItemDescription( siteId );
                descMap.put( siteId, desc );
            }
            else if( log.isDebugEnabled() ) log.debug( "Get from cache" );

        }
        lastReadData = System.currentTimeMillis();
        return desc;
    }

    static String sql_ = null;
    static {
        sql_ =
            "select a.ID_ITEM, a.TEXT " +
            "from   WM_PRICE_ITEM_DESCRIPTION a, WM_PRICE_LIST b, WM_PRICE_SHOP_LIST c " +
            "where  a.ID_ITEM=b.ID_ITEM and b.ID_SHOP=c.ID_SHOP and c.ID_SITE=?";

        try {
            SqlStatement.registerSql( sql_, PriceItemDescription.class );
        }
        catch( Throwable e ) {
            log.error( "Exception in registerSql, sql\n" + sql_, e );
            //Todo throw RuntimeException
        }
    }

    public PriceItemDescription() {
    }

    private PriceItemDescription( Long idSite )
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
                description.put( RsetTools.getLong( rs, "ID_ITEM" ), RsetTools.getString( rs, "TEXT" ) );
            }
        }
        catch( Throwable e ) {
            String es = "Exception in PriceItemDescription()";
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
