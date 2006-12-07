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

import org.riverock.commerce.price.CurrencyItem;
import org.riverock.commerce.bean.ShopItem;
import org.riverock.commerce.bean.price.DiscountType;

/**
 * Class ShopOrderItem.
 *
 * User: SergeMaslyukov
 * Date: 02.12.2006
 * Time: 19:37:32
 */
public class ShopOrderItem implements Serializable {

    /**
     * Field countItem
     */
    private java.lang.Integer countItem;

    /**
     * Field isInDb
     */
    private java.lang.Boolean isInDb;

    /**
     * Field originId
     */
    private java.lang.Long originId;

    /**
     * Field resultPrice
     */
    private BigDecimal resultPrice;

    /**
     * Field precisionResult
     */
    private java.lang.Integer precisionResult;

    /**
     * Field discount
     */
    private DiscountType discount;

    /**
     * Field currencyItem
     */
    private CurrencyItem currencyItem;

    /**
     * Field resultCurrency
     */
    private CurrencyItem resultCurrency;

    private ShopItem shopItem;

    public ShopItem getShopItem() {
        return shopItem;
    }

    public void setShopItem(ShopItem shopItem) {
        this.shopItem = shopItem;
    }

    public Integer getCountItem() {
        return countItem;
    }

    public void setCountItem(Integer countItem) {
        this.countItem = countItem;
    }

    public Boolean getInDb() {
        return isInDb;
    }

    public void setInDb(Boolean inDb) {
        isInDb = inDb;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
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

    public DiscountType getDiscount() {
        return discount;
    }

    public void setDiscount(DiscountType discount) {
        this.discount = discount;
    }

    public CurrencyItem getCurrencyItem() {
        return currencyItem;
    }

    public void setCurrencyItem(CurrencyItem currencyItem) {
        this.currencyItem = currencyItem;
    }

    public CurrencyItem getResultCurrency() {
        return resultCurrency;
    }

    public void setResultCurrency(CurrencyItem resultCurrency) {
        this.resultCurrency = resultCurrency;
    }
}
