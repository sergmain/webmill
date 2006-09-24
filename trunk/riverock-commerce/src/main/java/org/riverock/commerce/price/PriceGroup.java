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
        for (PriceGroupItem item : groupVector) {
            GroupItemType groupItem = new GroupItemType();
            groupItem.setGroupName(item.name);
            groupItem.setIdGroup(item.id_group);

            PortletURL portletURL = renderResponse.createRenderURL();
            portletURL.setParameter(ContainerConstants.NAME_TYPE_CONTEXT_PARAM, ShopPortlet.CTX_TYPE_SHOP);
            portletURL.setParameter(ShopPortlet.NAME_ID_GROUP_SHOP, item.id_group.toString());
            portletURL.setParameters(shopParam.currencyURL);
            portletURL.setParameter(ShopPortlet.NAME_ID_SHOP_PARAM, shopParam.id_shop.toString());

            if (shopParam.sortBy != null) {
                portletURL.setParameter(ShopPortlet.NAME_SHOP_SORT_BY, shopParam.sortBy);
                portletURL.setParameter(ShopPortlet.NAME_SHOP_SORT_DIRECT, "" + shopParam.sortDirect);
            }
            groupItem.setGroupUrl(portletURL.toString());

            group.addGroupItem(groupItem);
        }

        return group;
    }

    public static List<PriceGroupItem> getInstance( DatabaseAdapter db_, Long idGroup, Long idShop, Long idSite )
        throws PriceException {

        PreparedStatement ps = null;
        ResultSet rs = null;

        if( log.isDebugEnabled() )
            log.debug( "idGroup " + idGroup + ", idShop " + idShop + ", idSite " + idSite );

        List<PriceGroupItem> v = new ArrayList<PriceGroupItem>( 15 );
        try {
            String sql_ =
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