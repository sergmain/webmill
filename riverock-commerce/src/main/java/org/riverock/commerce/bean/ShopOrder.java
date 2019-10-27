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
package org.riverock.commerce.bean;

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
     * Field shopOrderItems
     */
    private List<ShopOrderItem> shopOrderItems = new ArrayList<ShopOrderItem>();

    /**
     * Field discount
     */
    private Discount discount;

    /**
     * Field orderUrl
     */
    private java.lang.String orderUrl;

    /**
     * Field orderName
     */
    private java.lang.String orderName;


    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public List<ShopOrderItem> getShopOrderItems() {
        if (shopOrderItems ==null) {
            shopOrderItems = new ArrayList<ShopOrderItem>();
        }
        return shopOrderItems;
    }

    public void setShopOrderItems(List<ShopOrderItem> shopOrderItems) {
        this.shopOrderItems = shopOrderItems;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public String getOrderUrl() {
        return orderUrl;
    }

    public void setOrderUrl(String orderUrl) {
        this.orderUrl = orderUrl;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
}
