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
import java.util.ArrayList;

import org.hibernate.Session;

import org.riverock.commerce.bean.Shop;
import org.riverock.commerce.bean.ShopItem;
import org.riverock.commerce.tools.HibernateUtils;
import org.riverock.commerce.price.PriceGroupItem;

/**
 * @author Sergei Maslyukov
 *         Date: 01.09.2006
 *         Time: 21:45:03
 *         <p/>
 *         $Id: PriceCurrency.java 950 2006-09-01 18:11:51Z serg_main $
 */
public class ShopDaoImpl implements ShopDao {

    public Shop getShop(Long shopId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Shop bean = (Shop)session.createQuery(
            "select shop from org.riverock.commerce.bean.Shop shop " +
                "where shop.shopId=:shopId")
            .setLong("shopId", shopId)
            .uniqueResult();
        session.getTransaction().commit();
        return bean;
    }

    public Shop getShop(Long shopId, Long siteId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Shop bean = (Shop)session.createQuery(
            "select shop from org.riverock.commerce.bean.Shop shop " +
                "where shop.shopId=:shopId and shop.siteId=:siteId")
            .setLong("shopId", shopId)
            .setLong("siteId", siteId)
            .uniqueResult();
        session.getTransaction().commit();
        return bean;
    }

    public List<Shop> getShopList(Long siteId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List<Shop> list = session.createQuery(
            "select shop from org.riverock.commerce.bean.Shop shop " +
                "where shop.isClosed=false and shop.siteId=:siteId")
            .setLong("siteId", siteId)
            .list();
        session.getTransaction().commit();
        return list;
    }

    /**
     *
     * @param shop shop object to create
     * @return PK value
     */
    public Long createShop(Shop shop) {
        if (shop==null) {
            return null;
        }
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        try {
            session.save(shop);
            return shop.getShopId();
        }
        finally {
            session.flush();
            session.getTransaction().commit();
        }
    }

    public void updateShop(Shop shop) {
        if (shop ==null || shop.getShopId()==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        try {
            session.update(shop);
        }
        finally {
            session.flush();
            session.getTransaction().commit();
        }
    }

    public void deleteShop(Long shopId) {
        if (shopId==null) {
            return;
        }
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        session.createQuery(
            "update from org.riverock.commerce.bean.Shop shop " +
                "set shop.isClosed=true " +
                "where shop.shopId=:shopId")
            .setLong("shopId", shopId)
            .executeUpdate();
        session.getTransaction().commit();
    }

    public ShopItem getShopItem(Long shopItemId) {
        if (shopItemId==null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        ShopItem bean = (ShopItem)session.createQuery(
            "select shopItem from org.riverock.commerce.bean.ShopItem shopItem " +
                "where shopItem.shopItemId=:shopItemId")
            .setLong("shopItemId", shopItemId)
            .uniqueResult();
        session.getTransaction().commit();
        return bean;
    }

    public ShopItem getShopItem(Long shopId, Long itemId) {
        if (shopId==null || itemId==null) {
            return null;
        }

        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        ShopItem bean = (ShopItem)session.createQuery(
            "select shopItem from org.riverock.commerce.bean.ShopItem shopItem " +
                "where shopItem.shopId=:shopId and shopItem.itemId=:itemId")
            .setLong("shopId", shopId)
            .setLong("itemId", itemId)
            .uniqueResult();
        session.getTransaction().commit();
        return bean;
    }

    public List<PriceGroupItem> getGroupList( Long idGroup, Long idShop, Long idSite ) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

//        "select a.ITEM, a.ID " +
//        "from   WM_PRICE_LIST a, WM_PRICE_SHOP_LIST b " +
//        "where  a.ID_SHOP=b.ID_SHOP and a.ABSOLETE=0 and " +
//        "       b.ID_SITE=? and a.ID_MAIN=? and a.ID_SHOP=? and a.IS_GROUP=1 " +
//        "order by a.ITEM asc ";

        List<Object[]> list = session.createQuery(
            "select shopItem.item, shopItem.itemId " +
                "from  org.riverock.commerce.bean.ShopItem shopItem, " +
                "      org.riverock.commerce.bean.Shop shop " +
                "where shopItem.shopId=shop.shopId and shopItem.obsolete=false and " +
                "      shop.siteId=:siteId and shopItem.parentItemId=:parentItemId and shopItem.shopId=:shopId and " +
                "      shopItem.isGroup=true and shopItem.obsolete=false " +
                "order by shopItem.item asc")
            .setLong("parentItemId", idGroup)
            .setLong("siteId", idSite)
            .setLong("shopId", idShop)
            .list();
        session.getTransaction().commit();
        List<PriceGroupItem> v = new ArrayList<PriceGroupItem>(list.size());
        for (Object[] objects : list) {
            PriceGroupItem item = new PriceGroupItem( (String)objects[0], (Long)objects[1] );
            v.add(item);
        }
        return v;
    }

    public List<ShopItem> getShopItemList(Long shopId, Long parentItemId, String sortBy, int sortDirection) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        String sql = "select shopItem " +
            "from  org.riverock.commerce.bean.ShopItem shopItem " +
            "where shopItem.parentItemId=:parentItemId and shopItem.shopId=:shopId and " +
            "      shopItem.isGroup=false and shopItem.obsolete=false ";

//        sql +=
//            "select a.* " +
//                "from   WM_PRICE_LIST a " +
//                "where  a.ID_MAIN=? and a.ID_SHOP=? and a.ABSOLETE=0 and a.IS_GROUP=0 ";
//
        
        if ("item".equals(sortBy)) {
            sql += (" order by shopItem.item " + (sortDirection == 0 ? "ASC" : "DESC"));
        }
        else if ("price".equals(sortBy)) {
            sql += (" order by shopItem.price " + (sortDirection == 0 ? "ASC" : "DESC"));
        }

        List<ShopItem> list = session.createQuery(sql)
            .setLong("parentItemId", parentItemId)
            .setLong("shopId", shopId)
            .list();
        session.getTransaction().commit();
        return list;
    }

/*
    public List<ShopItem> getShopItemList(Long siteId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List<ShopItem> list = session.createQuery(
            "select shop from org.riverock.commerce.bean.ShopItem shopItems " +
                "where shop.isOpened=false =:siteId")
            .setLong("siteId", siteId)
            .list();
        session.getTransaction().commit();
        return list;
    }
*/

}
