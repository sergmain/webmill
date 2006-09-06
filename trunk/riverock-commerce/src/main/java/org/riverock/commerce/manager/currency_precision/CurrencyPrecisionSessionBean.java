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


