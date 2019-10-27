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

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import org.riverock.commerce.bean.ShopItem;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.schema.shop.PositionItemType;
import org.riverock.commerce.schema.shop.PricePositionType;
import org.riverock.interfaces.ContainerConstants;

/**
 * Построение иерархии категорий товаров начиная от текущей и до корня вверх
 *
 * $Id: PriceListPosition.java 1229 2007-06-28 11:25:40Z serg_main $
 *
 */
public class PriceListPosition {
    private static Logger log = Logger.getLogger( PriceListPosition.class );

    public static PricePositionType getInstance(ShopPageParam shopParam, RenderResponse renderResponse)
        throws PriceException {

        if (log.isDebugEnabled()) {
            log.debug("idGroup "+shopParam.id_group);
        }

        PricePositionType position = new PricePositionType();
        if (shopParam.id_group==null) {
            return null;
        }

        Long topId = shopParam.id_group;
        while(true) {
            ShopItem shopItem = CommerceDaoFactory.getShopDao().getShopItem(shopParam.id_shop, topId);
            if (shopItem!=null) {
                PortletURL portletURL = renderResponse.createRenderURL();

                portletURL.setParameter(ContainerConstants.NAME_TYPE_CONTEXT_PARAM, ShopPortlet.CTX_TYPE_SHOP);
                portletURL.setParameter(ShopPortlet.NAME_ID_GROUP_SHOP, shopItem.getItemId().toString());
                portletURL.setParameters(shopParam.currencyURL);
                portletURL.setParameter(ShopPortlet.NAME_ID_SHOP_PARAM, shopParam.id_shop.toString() );

                if (shopParam.sortBy != null){
                    portletURL.setParameter(ShopPortlet.NAME_SHOP_SORT_BY, shopParam.sortBy);
                    portletURL.setParameter(ShopPortlet.NAME_SHOP_SORT_DIRECT, ""+shopParam.sortDirect);
                }
                PositionItemType positionItem = new PositionItemType();

                positionItem.setPositionName( shopItem.getItem() );
                positionItem.setPositionUrl( portletURL.toString() );
                positionItem.setIdGroupCurrent(shopItem.getItemId());
                positionItem.setIdGroupTop(shopItem.getParentItemId());

                position.getPositionItem().add(positionItem);
            }
            else {
                break;
            }
            topId = shopItem.getParentItemId();
        }
        List<PositionItemType> v = new ArrayList<PositionItemType>();

        if (log.isDebugEnabled()) {
            log.debug("Count of position: " + position.getPositionItem().size());
        }

        if (position.getPositionItem().isEmpty()) {
            return position;
        }

        Long id = 0L;
        int maxLoop = 100;
        while (--maxLoop>0) {
            for (PositionItemType item : position.getPositionItem()) {
                if (log.isDebugEnabled()) {
                    log.debug("Position id_curr: " + item.getIdGroupCurrent() + ", id_top: " + item.getIdGroupTop());
                }

                if (item.getIdGroupTop().equals( id )) {
                    v.add(item);
                    id = item.getIdGroupCurrent();
                }

                item = null;
            }
            if (shopParam.id_group.equals(id) ) {
                break;
            }
        }
        if (maxLoop==0) {
            throw new PriceException("Error build price position, max loop count processed");
        }

        position.getPositionItem().clear();
        position.getPositionItem().addAll( v );
        v = null;

        PortletURL portletURL = renderResponse.createRenderURL();

        portletURL.setParameter(ContainerConstants.NAME_TYPE_CONTEXT_PARAM, ShopPortlet.CTX_TYPE_SHOP);
        portletURL.setParameter(ShopPortlet.NAME_ID_GROUP_SHOP, "0");
        portletURL.setParameters(shopParam.currencyURL);
        portletURL.setParameter(ShopPortlet.NAME_ID_SHOP_PARAM, shopParam.id_shop.toString());

        if (shopParam.sortBy != null) {
            portletURL.setParameter(ShopPortlet.NAME_SHOP_SORT_BY, shopParam.sortBy);
            portletURL.setParameter(ShopPortlet.NAME_SHOP_SORT_DIRECT, ""+shopParam.sortDirect);
        }
        position.setTopLevelUrl( portletURL.toString() );

        return position;
    }
}