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
 * Class WmCashCurrencyItemType.
 * 
 * User: SergeMaslyukov
 * Date: 02.12.2006
 * Time: 19:37:32
 */
public class WmCashCurrencyItemType implements Serializable {

    /**
     * Field currencyId
     */
    private Long currencyId;

    /**
     * Field isUsed
     */
    private boolean isUsed = false;

    /**
     * Field isUseStandart
     */
    private boolean isUseStandart = false;

    /**
     * Field idStandartCurs
     */
    private Long idStandartCurs;

    /**
     * Field siteId
     */
    private Long siteId;

    /**
     * Field currency
     */
    private String currency;

    /**
     * Field currencyName
     */
    private String currencyName;

    /**
     * Field percentValue
     */
    private Double percentValue;

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public boolean isUseStandart() {
        return isUseStandart;
    }

    public void setUseStandart(boolean useStandart) {
        isUseStandart = useStandart;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public Long getIdStandartCurs() {
        return idStandartCurs;
    }

    public void setIdStandartCurs(Long idStandartCurs) {
        this.idStandartCurs = idStandartCurs;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Double getPercentValue() {
        return percentValue;
    }

    public void setPercentValue(Double percentValue) {
        this.percentValue = percentValue;
    }
}
