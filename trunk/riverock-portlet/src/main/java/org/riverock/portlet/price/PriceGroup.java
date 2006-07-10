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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;



import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.portlet.schema.portlet.shop.GroupItemType;
import org.riverock.portlet.schema.portlet.shop.GroupListType;
import org.riverock.webmill.container.ContainerConstants;

/**
 * $Author$
 * <p/>
 * $Id$
 */
public final class PriceGroup {
    private final static Logger log = Logger.getLogger( PriceGroup.class );

    public static GroupListType getInstance( DatabaseAdapter db_, ShopPageParam shopParam, RenderResponse renderResponse )
        throws PriceException {
        List<PriceGroupItem> groupVector = getInstance( db_, shopParam.id_group, shopParam.id_shop, shopParam.idSite );

        if( groupVector == null || groupVector.isEmpty() )
            return null;

        if( log.isDebugEnabled() )
            log.debug( "move to GroupListType" );

        GroupListType group = new GroupListType();
        for( int i = 0; i < groupVector.size(); i++ ) {
            PriceGroupItem item = ( PriceGroupItem ) groupVector.get( i );

            GroupItemType groupItem = new GroupItemType();
            groupItem.setGroupName( item.name );
            groupItem.setIdGroup( item.id_group );

            PortletURL portletURL = renderResponse.createRenderURL();
            portletURL.setParameter( ContainerConstants.NAME_TYPE_CONTEXT_PARAM, ShopPortlet.CTX_TYPE_SHOP );
            portletURL.setParameter( ShopPortlet.NAME_ID_GROUP_SHOP, item.id_group.toString() );
            portletURL.setParameters( shopParam.currencyURL );
            portletURL.setParameter( ShopPortlet.NAME_ID_SHOP_PARAM, shopParam.id_shop.toString() );

//            String url = PortletService.url(
//                Constants.CTX_TYPE_SHOP, shopParam.nameTemplate) + '&' +
//                Constants.NAME_ID_GROUP_SHOP + '=' + item.id_group + '&' +
//                shopParam.currencyURL + '&' +
//                Constants.NAME_ID_SHOP_PARAM + '=' + shopParam.id_shop + '&' +
//                Constants.NAME_TYPE_CONTEXT_PARAM + '=' + Constants.CTX_TYPE_SHOP;

            if( shopParam.sortBy != null ) {
//                url += ("&" + Constants.NAME_SHOP_SORT_BY + '=' + shopParam.sortBy +
//                    '&' + Constants.NAME_SHOP_SORT_DIRECT + '=' + shopParam.sortDirect
//                    );
                portletURL.setParameter( ShopPortlet.NAME_SHOP_SORT_BY, shopParam.sortBy );
                portletURL.setParameter( ShopPortlet.NAME_SHOP_SORT_DIRECT, "" + shopParam.sortDirect );
            }
            groupItem.setGroupUrl( portletURL.toString() );

            group.addGroupItem( groupItem );
        }

        return group;
    }

    public static List<PriceGroupItem> getInstance( DatabaseAdapter db_, Long idGroup, Long idShop, Long idSite )
        throws PriceException {

        String sql_ = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        if( log.isDebugEnabled() )
            log.debug( "idGroup " + idGroup + ", idShop " + idShop + ", idSite " + idSite );

        List<PriceGroupItem> v = new ArrayList<PriceGroupItem>( 15 );
        try {
            sql_ =
                "select a.ITEM, a.ID " +
                "from   WM_PRICE_LIST a, WM_PRICE_SHOP_LIST b " +
                "where  a.ID_SHOP=b.ID_SHOP and a.ABSOLETE=0 and " +
                "       b.ID_SITE=? and a.ID_MAIN=? and a.ID_SHOP=? and a.IS_GROUP=1 " +
                "order by a.ITEM asc ";

            ps = db_.prepareStatement( sql_ );
            RsetTools.setLong( ps, 1, idSite );
            RsetTools.setLong( ps, 2, idGroup );
            RsetTools.setLong( ps, 3, idShop );

            rs = ps.executeQuery();

            while( rs.next() ) {
                PriceGroupItem item = new PriceGroupItem( RsetTools.getString( rs, "ITEM" ),
                    RsetTools.getLong( rs, "ID" ) );

                if( log.isDebugEnabled() )
                    log.debug( "group id - " + item.id_group + " name " + item.name );

                v.add( item );
            }

            if( log.isDebugEnabled() )
                log.debug( "size group vector - " + v.size() );

        }
        catch( Exception e ) {
            final String es = "Error get price group list ";
            log.error( es, e );
            throw new PriceException( es, e );
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }

        if( v.isEmpty() )
            return null;

        return v;
    }
}