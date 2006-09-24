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
package org.riverock.commerce.manager.currency_precision;

import org.riverock.commerce.bean.CurrencyPrecisionBean;
import org.riverock.commerce.manager.currency.CurrencyBean;

/**
 * User: SergeMaslyukov
 * Date: 06.09.2006
 * Time: 1:50:44
 * <p/>
 * $Id$
 */
public class CurrencyPrecisionExtendedBean {
    private CurrencyPrecisionBean currencyPrecisionBean=null;
    private CurrencyBean currencyBean=null;

    public CurrencyPrecisionExtendedBean() {
    }

    public CurrencyPrecisionExtendedBean(CurrencyPrecisionBean currencyPrecisionBean, CurrencyBean currencyBean) {
        this.currencyPrecisionBean = currencyPrecisionBean;
        this.currencyBean = currencyBean;
    }

    public CurrencyPrecisionBean getCurrencyPrecisionBean() {
        return currencyPrecisionBean;
    }

    public CurrencyBean getCurrencyBean() {
        return currencyBean;
    }
}

