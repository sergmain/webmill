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

import java.util.Date;
import java.io.Serializable;

/**
 * Class CustomCurrencyItemType.
 * 
 * User: SergeMaslyukov
 * Date: 02.12.2006
 * Time: 19:37:32
 */
public class CustomCurrencyItemType implements Serializable {

    /**
     * Field currencyId
     */
    private java.lang.Long currencyId;

    /**
     * Field currencyCode
     */
    private java.lang.String currencyCode;

    /**
     * Field currencyName
     */
    private java.lang.String currencyName;

    /**
     * Field isUsed
     */
    private boolean isUsed;

    /**
     * Field isUseStandardCurrency
     */
    private boolean isUseStandardCurrency=false;

    /**
     * Field percent
     */
    private java.lang.Double percent;

    /**
     * Field standardCurrencyId
     */
    private java.lang.Long standardCurrencyId;

    /**
     * Field siteId
     */
    private java.lang.Long siteId;

    /**
     * Field isRealInit
     */
    private boolean isRealInit = false;

    /**
     * Field realCurs
     */
    private java.lang.Double realCurs;

    /**
     * Field realDateChange
     */
    private java.util.Date realDateChange;

    /**
     * Field currentCurs
     */
    private CurrencyCurrentCursType currentCurs;

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public boolean isUseStandardCurrency() {
        return isUseStandardCurrency;
    }

    public void setUseStandardCurrency(boolean useStandardCurrency) {
        isUseStandardCurrency = useStandardCurrency;
    }

    public boolean isRealInit() {
        return isRealInit;
    }

    public void setRealInit(boolean realInit) {
        isRealInit = realInit;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public Long getStandardCurrencyId() {
        return standardCurrencyId;
    }

    public void setStandardCurrencyId(Long standardCurrencyId) {
        this.standardCurrencyId = standardCurrencyId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Double getRealCurs() {
        return realCurs;
    }

    public void setRealCurs(Double realCurs) {
        this.realCurs = realCurs;
    }

    public Date getRealDateChange() {
        return realDateChange;
    }

    public void setRealDateChange(Date realDateChange) {
        this.realDateChange = realDateChange;
    }

    public CurrencyCurrentCursType getCurrentCurs() {
        return currentCurs;
    }

    public void setCurrentCurs(CurrencyCurrentCursType currentCurs) {
        this.currentCurs = currentCurs;
    }

    public String toString() {
        return "[currencyId:"+currencyId+",currencyName:"+currencyName+",standardCurrencyId:"+standardCurrencyId+"]";
    }
}
