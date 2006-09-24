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
package org.riverock.commerce.bean;

import java.util.List;
import java.io.Serializable;

import org.riverock.commerce.manager.currency.CurrencyBean;
import org.riverock.commerce.manager.std_currency.StandardCurrencyBean;

/**
 * @author Sergei Maslyukov
 *         Date: 01.09.2006
 *         Time: 21:20:16
 *         <p/>
 *         $Id$
 */
public class CustomCurrency implements Serializable {
    private static final long serialVersionUID = 2625005163L;
    
    private List<CurrencyBean> currencies = null;
    private List<StandardCurrencyBean> standardCurrencies = null;

    public List<CurrencyBean> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<CurrencyBean> currencies) {
        this.currencies = currencies;
    }

    public List<StandardCurrencyBean> getStandardCurrencies() {
        return standardCurrencies;
    }

    public void setStandardCurrencies(List<StandardCurrencyBean> standardCurrencies) {
        this.standardCurrencies = standardCurrencies;
    }
}
