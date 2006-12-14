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
import org.riverock.commerce.tools.HibernateUtils;

/**
 * User: SergeMaslyukov
 * Date: 13.12.2006
 * Time: 22:42:40
 * <p/>
 * $Id$
 */
public class OrderDaoImpl implements OrderDao {

    public Long createUserOrder(Long userId, Date createDate) {
        if (createDate==null) {
            return null;
        }
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(userId);
        userOrder.setCreateDate( createDate );
        session.save(userOrder);
        session.getTransaction().commit();
        return userOrder.getUserOrderId();
    }

    public void bindUserToUserOrder(Session session, Long userOrderId, Long userId) {
        if (userId ==null || userOrderId==null) {
            return;
        }

        UserOrder userOrder = getUserOrder(session, userOrderId);
        if (userOrder!=null) {
            userOrder.setUserId(userId);
            session.update(userOrder);
        }
    }

    public UserOrder getUserOrder(Session session, Long userOrderId) {
        UserOrder bean = (UserOrder)session.createQuery(
            "select userOrder from org.riverock.commerce.bean.UserOrder userOrder " +
                "where userOrder.userOrderId=:userOrderId ")
            .setLong("userOrderId", userOrderId)
            .uniqueResult();
        return bean;
    }

    public void deleteUserOrder(Session session, Long userOrderId) {
        session.createQuery
            ("delete org.riverock.commerce.bean.UserOrder userOrder where userOrder.userOrderId=:userOrderId ")
            .setLong("userOrderId", userOrderId)
            .executeUpdate();
    }

    public void setNewQuantity(Long siteId, Long userOrderId, Long shopItemId, int count) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

        UserOrder bean = (UserOrder)session.createQuery(
            "select userOrder from org.riverock.commerce.bean.UserOrder userOrder " +
                "where userOrder.userOrderId=:userOrderId ")
            .setLong("userOrderId", userOrderId)
            .uniqueResult();
        if (bean!=null) {

        }

        session.getTransaction().commit();
    }

    public void deleteShopItem(Long siteId, Long userOrderId, Long shopItemId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<ShopOrder> getShopOrders(Long userOrderId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int countItemsInUserOrder(Long userOrderId) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
