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
package org.riverock.commerce.dao;

import java.util.List;

import org.riverock.commerce.bean.Shop;
import org.riverock.commerce.bean.ShopItem;
import org.riverock.commerce.price.PriceGroupItem;

/**
 * @author Sergei Maslyukov
 *         Date: 01.09.2006
 *         Time: 21:44:58
 *         <p/>
 *         $Id$
 */
public interface ShopDao {
    Shop getShop(Long shopId);
    Shop getShop(Long shopId, Long siteId);

    Long createShop(Shop shop);

    void updateShop(Shop shop);

    void deleteShop(Long shopId);

    List<Shop> getShopList(Long siteId);

    /**
     * get shopItem record by PK
     * 
     * @param shopItemId PK
     * @return
     */
    ShopItem getShopItem(Long shopItemId);

    ShopItem getShopItem(Long shopId, Long itemId);

    List<PriceGroupItem> getGroupList( Long idGroup, Long idShop, Long idSite );

    List<ShopItem> getShopItemList(Long shopId, Long parentItemId, String sortBy, int sortDirection);
}
