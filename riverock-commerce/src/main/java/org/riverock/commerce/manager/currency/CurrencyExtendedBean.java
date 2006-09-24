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
