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
import java.math.BigDecimal;
import java.util.Date;

import org.riverock.commerce.manager.std_currency.StandardCurrencyBean;

/**
 * @author Sergei Maslyukov
 *         Date: 31.08.2006
 *         Time: 20:42:41
 *         <p/>
 *         $Id$
 */
public class CurrencyExtendedBean implements Serializable {
    private static final long serialVersionUID = 55957005701L;

    private CurrencyBean currencyBean  = null;
    private BigDecimal realCurs=null;
    private BigDecimal currentCurs=null;
    private BigDecimal standardCurrencyCurrentCurs=null;
    private Date cursChangeDate=null;
    private Date statndardCurrencyCursChangeDate=null;
    private boolean isCurrencyUsed=false;
    private StandardCurrencyBean standardCurrencyBean=null;
    private BigDecimal percent=null;

    public CurrencyExtendedBean() {
    }

    public CurrencyExtendedBean(CurrencyExtendedBean bean) {
        this.currencyBean = bean.getCurrencyBean();
        this.realCurs = bean.getRealCurs();
        this.currentCurs = bean.getCurrentCurs();
        this.standardCurrencyCurrentCurs = bean.getStandardCurrencyCurrentCurs();
        this.cursChangeDate = bean.getCursChangeDate();
        this.statndardCurrencyCursChangeDate = bean.getStatndardCurrencyCursChangeDate();
        isCurrencyUsed = bean.isCurrencyUsed();
        this.standardCurrencyBean = bean.getStandardCurrencyBean();
        this.percent = bean.getPercent();
    }

    public CurrencyBean getCurrencyBean() {
        return currencyBean;
    }

    public void setCurrencyBean(CurrencyBean currencyBean) {
        this.currencyBean = currencyBean;
    }

    public BigDecimal getRealCurs() {
        return realCurs;
    }

    public void setRealCurs(BigDecimal realCurs) {
        this.realCurs = realCurs;
    }

    public BigDecimal getCurrentCurs() {
        return currentCurs;
    }

    public void setCurrentCurs(BigDecimal currentCurs) {
        this.currentCurs = currentCurs;
    }

    public BigDecimal getStandardCurrencyCurrentCurs() {
        return standardCurrencyCurrentCurs;
    }

    public void setStandardCurrencyCurrentCurs(BigDecimal standardCurrencyCurrentCurs) {
        this.standardCurrencyCurrentCurs = standardCurrencyCurrentCurs;
    }

    public Date getCursChangeDate() {
        return cursChangeDate;
    }

    public void setCursChangeDate(Date cursChangeDate) {
        this.cursChangeDate = cursChangeDate;
    }

    public Date getStatndardCurrencyCursChangeDate() {
        return statndardCurrencyCursChangeDate;
    }

    public void setStatndardCurrencyCursChangeDate(Date statndardCurrencyCursChangeDate) {
        this.statndardCurrencyCursChangeDate = statndardCurrencyCursChangeDate;
    }

    public boolean isCurrencyUsed() {
        return isCurrencyUsed;
    }

    public void setCurrencyUsed(boolean currencyUsed) {
        isCurrencyUsed = currencyUsed;
    }

    public StandardCurrencyBean getStandardCurrencyBean() {
        return standardCurrencyBean;
    }

    public void setStandardCurrencyBean(StandardCurrencyBean standardCurrencyBean) {
        this.standardCurrencyBean = standardCurrencyBean;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }
}
