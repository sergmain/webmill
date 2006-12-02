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

/**
 * Class CurrencyPrecisionType.
 * 
 * User: SergeMaslyukov
 * Date: 02.12.2006
 * Time: 19:37:32
 */
public class CurrencyPrecisionType implements Serializable {

    /**
     * Field shopPrecisionId
     */
    private java.lang.Long shopPrecisionId;

    /**
     * Field currencyId
     */
    private java.lang.Long currencyId;

    /**
     * Field shopId
     */
    private java.lang.Long shopId;

    /**
     * Field precision
     */
    private java.lang.Integer precision;

    public Long getShopPrecisionId() {
        return shopPrecisionId;
    }

    public void setShopPrecisionId(Long shopPrecisionId) {
        this.shopPrecisionId = shopPrecisionId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }
}
