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

import javax.persistence.*;

/**
 * Class ShopOrderItem.
 *
 * User: SergeMaslyukov
 * Date: 02.12.2006
 * Time: 19:37:32
 */
@Entity
@Table(name="wm_price_order")
@TableGenerator(
    name="TABLE_PRICE_ORDER",
    table="wm_portal_ids",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_price_order",
    allocationSize = 1,
    initialValue = 1
)
public class ShopOrderItem implements Serializable {

// , , , , , , CURRENCY, ARTIKUL,
// IS_DELETED, , , ,

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_PRICE_ORDER")
    @Column(name="ID_PRICE_ORDER_V2")
    private Long shopOrderItemId;

    @Column(name="COUNT")
    private Integer countItem;

    @Column(name="ID_ORDER_V2")
    private Long userOrderId;

    @Column(name="PRICE_RESULT")
    private BigDecimal resultPrice;

    @Column(name="PRECISION_CURRENCY_RESULT")
    private Integer precisionResult;

    @Column(name="CODE_CURRENCY_RESULT")
    private String resultCurrencyCode;

    @Column(name="NAME_CURRENCY_RESULT")
    private String resultCurrencyName;

    @Column(name="ID_ITEM")
    private Long shopItemId;

    @Column(name="ITEM")
    private String shopItemName;

    public String getResultCurrencyCode() {
        return resultCurrencyCode;
    }

    public void setResultCurrencyCode(String resultCurrencyCode) {
        this.resultCurrencyCode = resultCurrencyCode;
    }

    public String getResultCurrencyName() {
        return resultCurrencyName;
    }

    public void setResultCurrencyName(String resultCurrencyName) {
        this.resultCurrencyName = resultCurrencyName;
    }

    public String getShopItemName() {
        return shopItemName;
    }

    public void setShopItemName(String shopItemName) {
        this.shopItemName = shopItemName;
    }

    public Long getShopOrderItemId() {
        return shopOrderItemId;
    }

    public void setShopOrderItemId(Long shopOrderItemId) {
        this.shopOrderItemId = shopOrderItemId;
    }

    public Long getUserOrderId() {
        return userOrderId;
    }

    public void setUserOrderId(Long userOrderId) {
        this.userOrderId = userOrderId;
    }

    public Long getShopItemId() {
        return shopItemId;
    }

    public void setShopItemId(Long shopItemId) {
        this.shopItemId = shopItemId;
    }

    public Integer getCountItem() {
        return countItem;
    }

    public void setCountItem(Integer countItem) {
        this.countItem = countItem;
    }

    public BigDecimal getResultPrice() {
        return resultPrice;
    }

    public void setResultPrice(BigDecimal resultPrice) {
        this.resultPrice = resultPrice;
    }

    public Integer getPrecisionResult() {
        return precisionResult;
    }

    public void setPrecisionResult(Integer precisionResult) {
        this.precisionResult = precisionResult;
    }
}
