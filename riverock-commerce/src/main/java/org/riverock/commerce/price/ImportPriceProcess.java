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

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.hibernate.Session;

import org.riverock.commerce.bean.ImportShopItem;
import org.riverock.commerce.bean.ShopItem;

/**
 * User: Admin
 * Date: Dec 29, 2002
 * Time: 9:12:53 PM
 * <p/>
 * $Id: ImportPriceProcess.java 1136 2006-12-12 14:18:19Z serg_main $
 */
public class ImportPriceProcess {

    private static void moveItemToPrice(Session session, Long idSite) throws Exception {

        List<Object[]> importItems = session.createQuery(
            "select import, shop.shopId " +
                "from  org.riverock.commerce.bean.ImportShopItem import, " +
                "      org.riverock.commerce.bean.Shop shop " +
                "where import.shopCode=shop.shopCode and shop.siteId=:siteId")
            .setLong("siteId", idSite)
            .list();

        Set<Long> shopIds = new HashSet<Long>();
        for (Object[] importItem : importItems) {
            ImportShopItem importShopItem = (ImportShopItem)importItem[0];
            Long shopId = (Long)importItem[1];
            shopIds.add(shopId);

            ShopItem item = (ShopItem)session.createQuery(
                "select item " +
                    "from  org.riverock.commerce.bean.ShopItem item " +
                    "where item.shopId=:shopId and item.itemId=:itemId ")
                .setLong("shopId", shopId)
                .setLong("itemId", importShopItem.getItemId())
                .uniqueResult();

            if (item!=null) {
                item.setGroup(importShopItem.isGroup());
                item.setParentItemId(importShopItem.getParentItemId());
                item.setPrice(importShopItem.getPrice());
                item.setCurrency(importShopItem.getCurrencyCode());
                item.setObsolete(false);
                item.setItem(importShopItem.getName());
            }
            else {
                item = new ShopItem();
                item.setGroup(importShopItem.isGroup());
                item.setItemId( importShopItem.getItemId());
                item.setParentItemId(importShopItem.getParentItemId());
                item.setItem(importShopItem.getName());
                item.setPrice(importShopItem.getPrice());
                item.setCurrency(importShopItem.getCurrencyCode());
                item.setObsolete(false);
                item.setShopId(shopId);
                item.setAddDate( new Date() );
                session.save(item);
            }
        }

        // set date/time of upload
        session.createQuery(
            "update org.riverock.commerce.bean.Shop shop set shop.dateUpload=:dateUpload " +
                "where shop.shopId in (:shopIds)")
            .setTimestamp("dateUpload", new Date())
            .setParameterList("shopIds", shopIds)
            .executeUpdate();

    }

    public static void process(Session session, Long idSite) throws Exception {

        // Mark all records, which was uploaded first time
        session.createSQLQuery(
            "update WM_PRICE_IMPORT_TABLE set IS_NEW=1 where ID not in " +
                "(select z1.ID from WM_PRICE_LIST z1, WM_PRICE_SHOP_LIST x1 " +
                "where SHOP_CODE = x1.CODE_SHOP and " +
                "z1.ID_SHOP = x1.ID_SHOP and x1.ID_SITE=:siteId)")
            .setLong("siteId", idSite)
            .executeUpdate();
        
        // in all shops, for all items, set attribute 'obsolete' to true
        session.createSQLQuery(
            "update WM_PRICE_LIST set ABSOLETE=1 where ID_SHOP in " +
                "(select z1.ID_SHOP from WM_PRICE_SHOP_LIST z1, WM_PRICE_IMPORT_TABLE x1 " +
                "where z1.ID_SITE=:siteId and z1.CODE_SHOP=x1.SHOP_CODE)")
            .setLong("siteId", idSite)
            .executeUpdate();

        moveItemToPrice(session, idSite);

    }
}
