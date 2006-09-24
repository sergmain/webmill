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
public class CurrencyBean implements Serializable {

    private static final long serialVersionUID = 55957005501L;

    private Long currencyId =null;
    private Long standardCurrencyId =null;
    private Long siteId =null;
    private BigDecimal percent =null;
    private String currencyName = null;
    private String currencyCode = null;
    private boolean isUsed=false;
    private boolean isUseStandard=false;
    private List<CurrencyCurs> curses = new ArrayList<CurrencyCurs>();

    public CurrencyBean() {
    }

    public CurrencyBean(CurrencyBean currencyBean) {
        this.currencyId = currencyBean.getCurrencyId();
        this.currencyName = currencyBean.getCurrencyName();
        this.currencyCode = currencyBean.getCurrencyCode();
        this.isUsed = currencyBean.isUsed();
        this.isUseStandard = currencyBean.isUseStandard();
        this.curses = currencyBean.getCurses();
        this.standardCurrencyId = currencyBean.getStandardCurrencyId();
        this.siteId = currencyBean.getSiteId();
        this.percent = currencyBean.getPercent();
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

