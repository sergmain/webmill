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
package org.riverock.commerce.manager.currency;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

/**
 * @author Sergei Maslyukov
 *         Date: 31.08.2006
 *         Time: 20:41:26
 *         <p/>
 *         $Id$
 */
public class Currency implements Serializable {

    private static final long serialVersionUID = 55957005501L;

    private Long currencyId = null;
    private Long standardCurrencyId =null;
    private Long siteId =null;
    private BigDecimal percent =null;
    private String currencyName = null;
    private String currencyCode = null;
    private boolean isUsed = false;
    private boolean isUseStandard = false;
    private List<CurrencyCurs> curses = new ArrayList<CurrencyCurs>();

    public Currency() {
    }

    public Currency(Currency currency) {
        this.currencyId = currency.getCurrencyId();
        this.currencyName = currency.getCurrencyName();
        this.currencyCode = currency.getCurrencyCode();
        this.isUsed = currency.isUsed();
        this.isUseStandard = currency.isUseStandard();
        this.curses = currency.getCurses();
        this.standardCurrencyId = currency.getStandardCurrencyId();
        this.siteId = currency.getSiteId();
        this.percent = currency.getPercent();
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
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

    public List<CurrencyCurs> getCurses() {
        return curses;
    }

    public void setCurses(List<CurrencyCurs> curses) {
        this.curses = curses;
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

