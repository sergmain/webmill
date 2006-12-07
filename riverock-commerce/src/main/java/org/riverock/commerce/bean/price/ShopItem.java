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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Class ShopItem.
 * 
 * User: SergeMaslyukov
 * Date: 02.12.2006
 * Time: 19:37:32
 */
public class ShopItem implements Serializable {

    /**
     * Field itemId
     */
    private java.lang.Long itemId;

    /**
     * Field shopId
     */
    private java.lang.Long shopId;

    /**
     * Field isGroup
     */
    private boolean isGroup = false;

    /**
     * Field idMain
     */
    private java.lang.Long idMain = 0L;

    /**
     * Field isSpecial
     */
    private boolean isSpecial = false;

    /**
     * Field isManual
     */
    private boolean isManual = false;

    /**
     * Field idStorageStatus
     */
    private java.lang.Long idStorageStatus = 0L;

    /**
     * Field id
     */
    private java.lang.Long id;

    /**
     * Field item
     */
    private java.lang.String item;

    /**
     * Field absolete
     */
    private java.lang.Integer absolete = 0;

    /**
     * Field currency
     */
    private java.lang.String currency;

    /**
     * Field quantity
     */
    private java.lang.Long quantity;

    /**
     * Field addDate
     */
    private java.util.Date addDate;

    /**
     * Field price
     */
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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getIdMain() {
        return idMain;
    }

    public void setIdMain(Long idMain) {
        this.idMain = idMain;
    }

    public Long getIdStorageStatus() {
        return idStorageStatus;
    }

    public void setIdStorageStatus(Long idStorageStatus) {
        this.idStorageStatus = idStorageStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getAbsolete() {
        return absolete;
    }

    public void setAbsolete(Integer absolete) {
        this.absolete = absolete;
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
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
