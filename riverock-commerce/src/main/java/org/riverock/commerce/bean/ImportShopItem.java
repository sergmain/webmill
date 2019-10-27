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

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.TableGenerator;

/**
 * @author Sergei Maslyukov
 *         Date: 11.12.2006
 *         Time: 21:01:28
 *         <p/>
 *         $Id$
 */
@Entity
@Table(name="wm_price_import_table" )
@TableGenerator(
    name="TABLE_PRICE_IMPORT_TABLE",
    table="wm_portal_ids",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_price_import_table",
    allocationSize = 1,
    initialValue = 1
)
public class ImportShopItem implements Serializable  {
//IS_GROUP, ID, ID_MAIN, NAME, PRICE, CURRENCY, IS_TO_LOAD,
// FOR_DELETE, ARTIKUL, ID_SHOP, IS_NEW, SHOP_CODE

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_PRICE_IMPORT_TABLE")
    @Column(name="ID_UPLOAD_PRICE")
    private Long importShopItem;

    @Column(name="IS_GROUP")
    private boolean isGroup;

    @Column(name="ID")
    private long itemId;

    @Column(name="ID_MAIN")
    private long parentItemId;

    @Column(name="NAME")
    private String name;

    @Column(name="PRICE")
    private BigDecimal price;

    @Column(name="CURRENCY")
    private String currencyCode;

    @Column(name="SHOP_CODE")
    private String shopCode;

    @Column(name="ID_SHOP")
    private Long shopId;

    public ImportShopItem() {
    }

    public ImportShopItem(boolean group, long itemId, long parentItemId, String name, BigDecimal price, String currencyCode, String shopCode) {
        isGroup = group;
        this.itemId = itemId;
        this.parentItemId = parentItemId;
        this.name = name;
        this.price = price;
        this.currencyCode = currencyCode;
        this.shopCode = shopCode;
    }

    public Long getImportShopItem() {
        return importShopItem;
    }

    public void setImportShopItem(Long importShopItem) {
        this.importShopItem = importShopItem;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getParentItemId() {
        return parentItemId;
    }

    public void setParentItemId(long parentItemId) {
        this.parentItemId = parentItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }
}
