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
package org.riverock.commerce.price;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.commerce.bean.Currency;
import org.riverock.commerce.bean.CurrencyCurs;
import org.riverock.commerce.bean.StandardCurrency;
import org.riverock.common.tools.DateTools;

/**
 * User: Admin
 * Date: Dec 12, 2002
 * Time: 9:01:05 PM
 * <p/>
 * $Id$
 */
public class CurrencyItem {
    private static Logger log = Logger.getLogger(CurrencyItem.class);

    private CurrencyCurs currencyCurrentCurs;
    private Currency currency = new Currency();
    private BigDecimal realCurs;
    private Date realDateChange;
    private boolean isRealInit;

    public boolean isRealInit() {
        return isRealInit;
    }

    public void setRealInit(boolean realInit) {
        isRealInit = realInit;
    }

    public Date getRealDateChange() {
        return realDateChange;
    }

    public void setRealDateChange(Date realDateChange) {
        this.realDateChange = realDateChange;
    }

    public BigDecimal getRealCurs() {
        return realCurs;
    }

    public void setRealCurs(BigDecimal realCurs) {
        this.realCurs = realCurs;
    }

    public CurrencyCurs getCurrencyCurrentCurs() {
        return currencyCurrentCurs;
    }

    public void setCurrencyCurrentCurs(CurrencyCurs currencyCurrentCurs) {
        this.currencyCurrentCurs = currencyCurrentCurs;
    }

    public void fillRealCurrencyData(List<StandardCurrency> stdCurrency) throws Exception {
        if (!this.isUseStandard() && this.getCurrencyCurrentCurs()==null) {
            this.setRealCurs(new BigDecimal(0));
            this.setRealDateChange(DateTools.getCurrentTime());
            this.setRealInit(false);
            return;
        }

        if (this.isUseStandard() && (stdCurrency == null || stdCurrency.isEmpty())) {
            throw new Exception("Curs for currency " + this.getCurrencyName() +
                " can not calculated. Curs for standard currency not entered");
        }

        Date cursDate=null;
        BigDecimal cursValue=null;
        if (this.isUseStandard()) {
            for (StandardCurrency stdItem : stdCurrency) {
                if (stdItem.getStandardCurrencyId().equals(this.getStandardCurrencyId())) {
                    cursValue = stdItem.getCurrentCurs().getCurs()!=null?stdItem.getCurrentCurs().getCurs():null;
                    cursDate = stdItem.getCurrentCurs().getCreated()!=null?stdItem.getCurrentCurs().getCreated():null;
                    break;
                }
            }
            if (cursDate == null) {
                throw new Exception("Error get curs for standard currency. Local currency - " + this.getCurrencyName());
            }
        }
        else {
            cursValue = this.getCurrencyCurrentCurs().getCurs()!=null?this.getCurrencyCurrentCurs().getCurs():null;
            cursDate = this.getCurrencyCurrentCurs().getDate()!=null?this.getCurrencyCurrentCurs().getDate():null;
        }

        this.setRealCurs(cursValue);
        this.setRealDateChange(cursDate);
        this.setRealInit(true);
    }

    public CurrencyItem() {
    }

    public CurrencyItem(Currency item) {
        this.setCurrencyCode(item.getCurrencyCode());
        this.setCurrencyName(item.getCurrencyName());
        this.setCurrencyId(item.getCurrencyId());
        this.setSiteId(item.getSiteId());
        this.setStandardCurrencyId(item.getStandardCurrencyId());
        this.setUsed(item.isUsed());
        this.setUseStandard(item.isUseStandard());
        this.setPercent(item.getPercent());
        this.setCurrencyCurrentCurs(CurrencyService.getCurrentCurs(this.getCurrencyId(), this.getSiteId()));
        if (this.getCurrencyCurrentCurs() == null) {
            log.warn("Current curs is null");
        }
    }

    public BigDecimal getPercent() {
        return currency.getPercent();
    }

    public void setPercent(BigDecimal percent) {
        currency.setPercent(percent);
    }

    public Long getSiteId() {
        return currency.getSiteId();
    }

    public void setSiteId(Long siteId) {
        currency.setSiteId(siteId);
    }

    public Long getStandardCurrencyId() {
        return currency.getStandardCurrencyId();
    }

    public void setStandardCurrencyId(Long standardCurrencyId) {
        currency.setStandardCurrencyId(standardCurrencyId);
    }

    public boolean isUseStandard() {
        return currency.isUseStandard();
    }

    public void setUseStandard(boolean useStandard) {
        currency.setUseStandard(useStandard);
    }

    public boolean isUsed() {
        return currency.isUsed();
    }

    public void setUsed(boolean used) {
        currency.setUsed(used);
    }

    public List<CurrencyCurs> getCurses() {
        return currency.getCurses();
    }

    public void setCurses(List<CurrencyCurs> curses) {
        currency.setCurses(curses);
    }

    public Long getCurrencyId() {
        return currency.getCurrencyId();
    }

    public void setCurrencyId(Long currencyId) {
        currency.setCurrencyId(currencyId);
    }

    public String getCurrencyName() {
        return currency.getCurrencyName();
    }

    public void setCurrencyName(String currencyName) {
        currency.setCurrencyName(currencyName);
    }

    public String getCurrencyCode() {
        return currency.getCurrencyCode();
    }

    public void setCurrencyCode(String currencyCode) {
        currency.setCurrencyCode(currencyCode);
    }
}
