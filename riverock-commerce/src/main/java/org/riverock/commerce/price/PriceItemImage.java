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
package org.riverock.commerce.price;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.io.FileInputStream;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;

/**
 * User: Admin
 * Date: Dec 19, 2002
 * Time: 3:19:17 PM
 * <p/>
 * $Id: PriceItemImage.java 1140 2006-12-14 11:46:52Z serg_main $
 */
public class PriceItemImage {
    private static Logger log = Logger.getLogger( PriceItemImage.class );

    private static Map<Long, PriceItemImage> imageMap = new HashMap<Long, PriceItemImage>();
    private Map<Long, String> image = new HashMap<Long, String>();

    public PriceItemImage() {
    }

    public String getItemImage( Long id ) {
        return image.get( id );
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
    private static final Object syncObject = new Object();

    public static PriceItemImage getInstance( Long siteId ) {

        try {
            PriceItemImage image = null;
            if( siteId != null ) {
                image = imageMap.get( siteId );
            }

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
        catch (Throwable e) {
            String es = "Error get image for item";
            log.error(es, e);
        }
        return null;
    }

    static String sql_ = null;
    static {
        sql_ =
            "select c.ID_ITEM, c.ID_IMAGE_DIR, d.NAME_FILE IMAGE_FILE_NAME " +
            "from   WM_PRICE_LIST a, WM_PRICE_SHOP_LIST b, WM_IMAGE_PRICE_ITEMS c, WM_IMAGE_DIR d " +
            "where  c.ID_IMAGE_DIR = d.ID_IMAGE_DIR and a.ID_SHOP=b.ID_SHOP and " +
            "       b.ID_SITE=? and a.ID_ITEM=c.ID_ITEM ";
    }

    private PriceItemImage( Long idSite )
        throws PriceException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
//            ps = db_.prepareStatement( sql_ );

            RsetTools.setLong( ps, 1, idSite );
            FileInputStream fis= new FileInputStream("a");
            fis.close();
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
    }
}
