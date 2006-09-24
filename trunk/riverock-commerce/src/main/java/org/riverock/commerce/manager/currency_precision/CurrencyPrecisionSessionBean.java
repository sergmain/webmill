/*
 * org.riverock.commerce - Commerce application
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.commerce.manager.currency_precision;

import org.riverock.commerce.manager.shop.ShopExtendedBean;

import java.io.Serializable;

/**
 * User: SergeMaslyukov
 * Date: 06.09.2006
 * Time: 0:05:12
 * <p/>
 * $Id$
 */
public class CurrencyPrecisionSessionBean implements Serializable {
    private static final long serialVersionUID = 3817005504L;

    public static final int UNKNOWN_TYPE = 0;
    public static final int SHOP_TYPE = 1;
    public static final int CURRENCY_PRECISION_TYPE = 2;

//    private Long id = null;
    private int objectType=0;

    private ShopExtendedBean shopExtendedBean = null;
    private Long currentShopId = null;
    private Long currentCurrencyPrecisionId = null;
    private CurrencyPrecisionExtendedBean currencyPrecisionBean = null;
    private Integer currentCurrencyPrecision=null;

    public CurrencyPrecisionSessionBean() {
    }

    public int getShopType() {
        return SHOP_TYPE;
    }

    public int getCurrencyPrecisionType() {
        return CURRENCY_PRECISION_TYPE;
    }

    public Integer getCurrentCurrencyPrecision() {
        return currentCurrencyPrecision;
    }

    public void setCurrentCurrencyPrecision(Integer currentCurrencyPrecision) {
        this.currentCurrencyPrecision = currentCurrencyPrecision;
    }

/*
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

*/
    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }

    public ShopExtendedBean getShopExtendedBean() {
        return shopExtendedBean;
    }

    public void setShopExtendedBean(ShopExtendedBean shopExtendedBean) {
        this.shopExtendedBean = shopExtendedBean;
    }

    public Long getCurrentShopId() {
        return currentShopId;
    }

    public void setCurrentShopId(Long currentShopId) {
        this.currentShopId = currentShopId;
    }

    public CurrencyPrecisionExtendedBean getCurrencyPrecisionBean() {
        return currencyPrecisionBean;
    }

    public void setCurrencyPrecisionBean(CurrencyPrecisionExtendedBean currencyPrecisionBean) {
        this.currencyPrecisionBean = currencyPrecisionBean;
    }

    public Long getCurrentCurrencyPrecisionId() {
        return currentCurrencyPrecisionId;
    }

    public void setCurrentCurrencyPrecisionId(Long currentCurrencyPrecisionId) {
        this.currentCurrencyPrecisionId = currentCurrencyPrecisionId;
    }
}


