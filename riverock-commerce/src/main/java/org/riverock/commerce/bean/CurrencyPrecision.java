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

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

/**
 * User: SergeMaslyukov
 * Date: 06.09.2006
 * Time: 0:07:34
 * <p/>
 * $Id$
 */
@Entity
@Table(name="wm_price_shop_precision")
@TableGenerator(
    name="TABLE_PRICE_SHOP_PRECISION",
    table="wm_portal_ids",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_price_shop_precision",
    allocationSize = 1,
    initialValue = 1
)
public class CurrencyPrecision implements Serializable {
    private static final long serialVersionUID = 2625005325L;
    
    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_PRICE_SHOP_PRECISION")
    @Column(name="ID_PRICE_SHOP_PRECISION")
    private Long currencyPrecisionId;
                    
    @Column(name="ID_CURRENCY")
    private Long currencyId;

    @Column(name="ID_SHOP")
    private Long shopId;

    @Column(name="PRECISION_SHOP")
    private Integer precision;

    public CurrencyPrecision() {
    }

    public CurrencyPrecision(CurrencyPrecision bean) {
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
