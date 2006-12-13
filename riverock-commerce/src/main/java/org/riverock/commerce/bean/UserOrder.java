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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * User: SergeMaslyukov
 * Date: 13.12.2006
 * Time: 22:37:00
 * <p/>
 * $Id$
 */
@Entity
@Table(name="wm_price_relate_user_order")
@TableGenerator(
    name="TABLE_PRICE_USER_ORDER",
    table="wm_portal_ids",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_price_relate_user_order",
    allocationSize = 1,
    initialValue = 1
)
public class UserOrder implements Serializable {
// ID_ORDER_V2, ID_USER, DATE_CREATE, IS_DELETED

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_PRICE_USER_ORDER")
    @Column(name="ID_ORDER_V2")
    private Long userOrderId;

    @Column(name="ID_USER")
    private Long userId;

    @Column(name="DATE_CREATE")
    private Date createDate;

    @Column(name="IS_DELETED")
    private boolean isDeleted;

    public Long getUserOrderId() {
        return userOrderId;
    }

    public void setUserOrderId(Long userOrderId) {
        this.userOrderId = userOrderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
