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

import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.schema.shop.GroupItemType;
import org.riverock.commerce.schema.shop.GroupListType;
import org.riverock.webmill.container.ContainerConstants;

/**
 * $Author$
 * <p/>
 * $Id$
 */
public final class PriceGroup {
    private final static Logger log = Logger.getLogger( PriceGroup.class );

    public static GroupListType getInstance(ShopPageParam shopParam, RenderResponse renderResponse)
        throws PriceException {
        List<PriceGroupItem> groupVector = CommerceDaoFactory.getShopDao().getGroupList( shopParam.id_group, shopParam.id_shop, shopParam.idSite );

        if( groupVector.isEmpty() ) {
            return null;
        }

        if( log.isDebugEnabled() ) {
            log.debug( "move to GroupListType" );
        }

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

            group.getGroupItem().add(groupItem);
        }

        return group;
    }
}