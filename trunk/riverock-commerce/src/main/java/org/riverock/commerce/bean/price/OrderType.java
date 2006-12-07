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
 * Заказ, объединяющий все
 * наименования из разных магазов
 * 
 * @version $Revision$ $Date$
 */
public class OrderType implements Serializable {

    /**
     * Field orderId
     */
    private java.lang.Long orderId;

    /**
     * Field serverName
     */
    private java.lang.String serverName;

    /**
     * Field shopOrdertListList
     */
    private List<ShopOrder> shopOrdertListList = new ArrayList<ShopOrder>();

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public List<ShopOrder> getShopOrdertListList() {
        if (shopOrdertListList==null) {
            shopOrdertListList = new ArrayList<ShopOrder>();
        }
        return shopOrdertListList;
    }

    public void setShopOrdertListList(List<ShopOrder> shopOrdertListList) {
        this.shopOrdertListList = shopOrdertListList;
    }
}
