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
package org.riverock.commerce.bean.price;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Часть заказа, относящаяся к
 * определенному магазину
 * 
 * User: SergeMaslyukov
 * Date: 02.12.2006
 * Time: 19:37:32
 */
public class ShopOrder implements Serializable {

    /**
     * Field shopId
     */
    private java.lang.Long shopId;

    /**
     * Field orderItemListList
     */
    private List<OrderItem> orderItemListList = new ArrayList<OrderItem>();

    /**
     * Field globalDiscount
     */
    private DiscountType globalDiscount;

    /**
     * Field currentOrderUrl
     */
    private java.lang.String currentOrderUrl;

    /**
     * Field currentOrderName
     */
    private java.lang.String currentOrderName;


    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public List<OrderItem> getOrderItemListList() {
        if (orderItemListList==null) {
            orderItemListList = new ArrayList<OrderItem>();
        }
        return orderItemListList;
    }

    public void setOrderItemListList(List<OrderItem> orderItemListList) {
        this.orderItemListList = orderItemListList;
    }

    public DiscountType getGlobalDiscount() {
        return globalDiscount;
    }

    public void setGlobalDiscount(DiscountType globalDiscount) {
        this.globalDiscount = globalDiscount;
    }

    public String getCurrentOrderUrl() {
        return currentOrderUrl;
    }

    public void setCurrentOrderUrl(String currentOrderUrl) {
        this.currentOrderUrl = currentOrderUrl;
    }

    public String getCurrentOrderName() {
        return currentOrderName;
    }

    public void setCurrentOrderName(String currentOrderName) {
        this.currentOrderName = currentOrderName;
    }
}
