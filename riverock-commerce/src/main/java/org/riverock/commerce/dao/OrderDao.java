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

import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import org.riverock.commerce.bean.UserOrder;
import org.riverock.commerce.bean.ShopOrder;

/**
 * User: SergeMaslyukov
 * Date: 13.12.2006
 * Time: 22:41:38
 * <p/>
 * $Id$
 */
public interface OrderDao {
    Long createUserOrder(Long userId, Date createDate);
    
    void bindUserToUserOrder(Session session, Long userOrderId, Long userId);

    UserOrder getUserOrder(Session session, Long userOrderId);

    void deleteUserOrder(Session session, Long userOrderId);

    void setNewQuantity(Long siteId, Long userOrderId, Long shopItemId, int count);

    void deleteShopItem(Long siteId, Long userOrderId, Long shopItemId);

    List<ShopOrder> getShopOrders(Long userOrderId);

    int countItemsInUserOrder(Long userOrderId);
}
