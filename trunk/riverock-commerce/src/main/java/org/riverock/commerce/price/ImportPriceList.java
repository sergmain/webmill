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

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import org.riverock.commerce.bean.ImportShopItem;
import org.riverock.commerce.schema.import_price.PriceListItemType;
import org.riverock.commerce.schema.import_price.PriceListType;
import org.riverock.commerce.schema.import_price.PricesType;
import org.riverock.commerce.tools.HibernateUtils;
import org.riverock.common.collections.TreeUtils;

/**
 * $Id$
 */
public class ImportPriceList {
    private static Logger log = Logger.getLogger(ImportPriceList.class);

    public static void process(final PricesType prices, Long id_site) throws PriceException {
        if (prices == null) {
            throw new PriceException(" #10.03");
        }

        try {
            Session session = HibernateUtils.getSession();
            session.beginTransaction();


            List shopCodes = session.createQuery(
                "select shop.shopCode from org.riverock.commerce.bean.Shop shop where shop.siteId=:siteId")
                .setLong("siteId", id_site)
                .list();

            session.createSQLQuery(
                "delete from WM_PRICE_IMPORT_TABLE where shop_code in (:shopCodes) ")
                .setParameterList("shopCodes", shopCodes)
                .executeUpdate();

            for (PriceListType price : prices.getPriceList()) {
                if (log.isDebugEnabled()) {
                    log.debug("shopCode " + price.getShopCode());
                    log.debug("Size vector: " + price.getItem().size());
                }

                List<PriceListItemType> items = (List)TreeUtils.rebuildTree((List)price.getItem());
                items = (List)TreeUtils.toPlainList((List) ImportPriceList.deleteUselessItem(items));

                for (PriceListItemType item : items) {
                    ImportShopItem importShopItem = new ImportShopItem(item.isIsGroup(), item.getId(), item.getParentID(), item.getNameItem(), item.getPrice(), item.getCurr(), price.getShopCode().toUpperCase());
                    session.save(importShopItem);
                }
            }

            ImportPriceProcess.process(session, id_site);

            session.getTransaction().commit();
        }
        catch (Exception e) {
            final String es = "error process import price-list";
            log.error(es, e);
            throw new PriceException(es, e);
        }
    }

    public static List<PriceListItemType> deleteUselessItem(List<PriceListItemType> items) {
        Iterator<PriceListItemType> it = items.iterator();
        while (it.hasNext()) {
            PriceListItemType item = it.next();
            if (StringUtils.equalsIgnoreCase(item.getIsLoad(), "NO")) {
                it.remove();
            }
        }
        for (PriceListItemType item : items) {
            item.setSubTree((List)deleteUselessItem((List)item.getSubTree()));
        }
        return items;
    }

}
