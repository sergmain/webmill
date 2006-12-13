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
 * Заказ, объединяющий все
 * наименования из разных магазов
 * 
 * @version $Revision$ $Date$
 */
public class Invoice implements Serializable {

    /**
     * Field userOrderId
     */
    private Long userOrderId;

//    private List<ShopOrder> shopOrders = new ArrayList<ShopOrder>();

    public Long getUserOrderId() {
        return userOrderId;
    }

    public void setUserOrderId(Long userOrderId) {
        this.userOrderId = userOrderId;
    }

/*
    public List<ShopOrder> getShopOrders() {
        if (shopOrders ==null) {
            shopOrders = new ArrayList<ShopOrder>();
        }
        return shopOrders;
    }

    public void setShopOrders(List<ShopOrder> shopOrders) {
        this.shopOrders = shopOrders;
    }
*/
}
