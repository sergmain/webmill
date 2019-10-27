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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * @author Sergei Maslyukov
 *         Date: 31.08.2006
 *         Time: 20:41:26
 *         <p/>
 *         $Id$
 */
@Entity
@Table(name="wm_cash_currency")
@TableGenerator(
    name="TABLE_CASH_CURRENCY",
    table="wm_portal_ids",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_cash_currency",
    allocationSize = 1,
    initialValue = 1
)
public class Currency implements Serializable {
    private static final long serialVersionUID = 55957005501L;

//ID_CURRENCY, CURRENCY, IS_USED, NAME_CURRENCY, IS_USE_STANDART, ID_STANDART_CURS, ID_SITE, PERCENT_VALUE
    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_CASH_CURRENCY")
    @Column(name="ID_CURRENCY")
    private Long currencyId = null;

    @Column(name="ID_STANDART_CURS")
    private Long standardCurrencyId =null;

    @Column(name="ID_SITE")
    private Long siteId =null;

    @Column(name="PERCENT_VALUE")
    private BigDecimal percent =null;

    @Column(name="NAME_CURRENCY")
    private String currencyName = null;

    @Column(name="CURRENCY")
    private String currencyCode = null;

    @Column(name="IS_USED")
    private boolean isUsed = false;

    @Column(name="IS_USE_STANDART")
    private boolean isUseStandard = false;

    public Currency() {
    }

    public Currency(Currency currency) {
        this.currencyId = currency.getCurrencyId();
        this.currencyName = currency.getCurrencyName();
        this.currencyCode = currency.getCurrencyCode();
        this.isUsed = currency.isUsed();
        this.isUseStandard = currency.isUseStandard();
        this.standardCurrencyId = currency.getStandardCurrencyId();
        this.siteId = currency.getSiteId();
        setPercent(currency.getPercent());
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        if (percent==null) {
            this.percent = BigDecimal.ZERO;
        }
        else {
            this.percent = percent;
        }
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getStandardCurrencyId() {
        return standardCurrencyId;
    }

    public void setStandardCurrencyId(Long standardCurrencyId) {
        this.standardCurrencyId = standardCurrencyId;
    }

    public boolean isUseStandard() {
        return isUseStandard;
    }

    public void setUseStandard(boolean useStandard) {
        isUseStandard = useStandard;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}

