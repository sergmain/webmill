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

/**
 * @author Sergei Maslyukov
 *         Date: 31.08.2006
 *         Time: 20:42:17
 *         <p/>
 *         $Id$
 */
public class CurrencySessionBean implements Serializable {
    private static final long serialVersionUID = 5597005504L;

    private CurrencyExtendedBean currencyExtendedBean = null;
    private Long currentCurrencyId = null;
    private BigDecimal currentCurs =null;
    private CurrencyBean currencyBean = null;

    public CurrencySessionBean() {
    }

    public CurrencyBean getCurrencyBean() {
        return currencyBean;
    }

    public void setCurrencyBean(CurrencyBean currencyBean) {
        this.currencyBean = currencyBean;
    }

    public CurrencyExtendedBean getCurrencyExtendedBean() {
        return currencyExtendedBean;
    }

    public void setCurrencyExtendedBean(CurrencyExtendedBean currencyExtendedBean) {
        this.currencyExtendedBean = currencyExtendedBean;
    }

    public BigDecimal getCurrentCurs() {
        return currentCurs;
    }

    public void setCurrentCurs(BigDecimal currentCurs) {
        this.currentCurs = currentCurs;
    }

    public Long getCurrentCurrencyId() {
        return currentCurrencyId;
    }

    public void setCurrentCurrencyId(Long currentCurrencyId) {
        this.currentCurrencyId = currentCurrencyId;
    }
}

