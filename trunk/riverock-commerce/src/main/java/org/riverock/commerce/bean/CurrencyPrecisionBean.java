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

/**
 * User: SergeMaslyukov
 * Date: 06.09.2006
 * Time: 0:07:34
 * <p/>
 * $Id$
 */
public class CurrencyPrecisionBean implements Serializable {
    private static final long serialVersionUID = 2625005325L;
    
    private Long currencyPrecisionId;
    private Long currencyId;
    private Long shopId;
    private Integer precision;

    public CurrencyPrecisionBean() {
    }

    public CurrencyPrecisionBean(CurrencyPrecisionBean bean) {
        this.currencyPrecisionId = bean.getCurrencyPrecisionId();
        this.currencyId = bean.getCurrencyId();
        this.shopId = bean.getShopId();
        this.precision = bean.getPrecision();
    }

    public Long getCurrencyPrecisionId() {
        return currencyPrecisionId;
    }

    public void setCurrencyPrecisionId(Long currencyPrecisionId) {
        this.currencyPrecisionId = currencyPrecisionId;
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
