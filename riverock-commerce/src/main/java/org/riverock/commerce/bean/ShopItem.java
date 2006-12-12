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
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

/**
 * Class ShopItem.
 * 
 * User: SergeMaslyukov
 * Date: 02.12.2006
 * Time: 19:37:32
 */
@Entity
@Table(name="wm_price_list")
@TableGenerator(
    name="TABLE_PRICE_LIST",
    table="wm_portal_ids",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_price_shop_list",
    allocationSize = 1,
    initialValue = 1
)
public class ShopItem implements Serializable {
// ID_ITEM, ID_SHOP, IS_GROUP, ID, ID_MAIN, ITEM, ABSOLETE, CURRENCY,
// QUANTITY, ADD_DATE, IS_SPECIAL, IS_MANUAL, ID_STORAGE_STATUS, PRICE

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_PRICE_LIST")
    @Column(name="ID_ITEM")
    private java.lang.Long shopItemId;

    @Column(name="ID_SHOP")
    private java.lang.Long shopId;

    @Column(name="IS_GROUP")
    private boolean isGroup = false;

    @Column(name="ID")
    private java.lang.Long itemId;

    @Column(name="ID_MAIN")
    private java.lang.Long parentItemId;

    @Column(name="IS_SPECIAL")
    private boolean isSpecial = false;

    @Column(name="IS_MANUAL")
    private boolean isManual = false;

    @Column(name="ID_STORAGE_STATUS")
    private java.lang.Long idStorageStatus = 0L;

    @Column(name="ITEM")
    private java.lang.String item;

    @Column(name="ABSOLETE")
    private boolean obsolete = false;

    @Column(name="CURRENCY")
    private java.lang.String currency;

    @Column(name="QUANTITY")
    private Long quantity;

    @Column(name="ADD_DATE")
    private java.util.Date addDate;

    @Column(name="PRICE")
    private BigDecimal price;

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }

    public boolean isManual() {
        return isManual;
    }

    public void setManual(boolean manual) {
        isManual = manual;
    }

    public Long getShopItemId() {
        return shopItemId;
    }

    public void setShopItemId(Long shopItemId) {
        this.shopItemId = shopItemId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getParentItemId() {
        if (parentItemId==null) {
            parentItemId=0L;
        }
        return parentItemId;
    }

    public void setParentItemId(Long parentItemId) {
        this.parentItemId = parentItemId;
    }

    public Long getIdStorageStatus() {
        return idStorageStatus;
    }

    public void setIdStorageStatus(Long idStorageStatus) {
        this.idStorageStatus = idStorageStatus;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public boolean getObsolete() {
        return obsolete;
    }

    public void setObsolete(boolean obsolete) {
        this.obsolete = obsolete;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public BigDecimal getPrice() {
        if (price==null) {
            price=BigDecimal.ZERO;
        }
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
