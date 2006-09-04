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

import org.riverock.commerce.manager.std_currency.StandardCurrencyBean;
import org.riverock.commerce.bean.CurrencyCurrentCurs;

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
    private StandardCurrencyBean standardCurrencyBean=null;
    private BigDecimal realCurs=null;

    private CurrencyCurrentCurs currentCurs=null;
    private CurrencyCurrentCurs currentStandardCurs=null;

    public CurrencyExtendedBean() {
    }

    public CurrencyExtendedBean(CurrencyBean currencyBean, StandardCurrencyBean standardCurrencyBean, BigDecimal realCurs, CurrencyCurrentCurs currentCurs, CurrencyCurrentCurs currentStandardCurs) {
        this.currencyBean = currencyBean;
        this.standardCurrencyBean = standardCurrencyBean;
        this.realCurs = realCurs;
        this.currentCurs = currentCurs;
        this.currentStandardCurs = currentStandardCurs;
    }

    public String getStandardCurrencyName() {
        if (standardCurrencyBean==null) {
            return "";
        }
        return standardCurrencyBean.getStandardCurrencyName();
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

    public StandardCurrencyBean getStandardCurrencyBean() {
        return standardCurrencyBean;
    }

    public CurrencyCurrentCurs getCurrentCurs() {
        return currentCurs;
    }

    public CurrencyCurrentCurs getCurrentStandardCurs() {
        return currentStandardCurs;
    }
}
