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
package org.riverock.commerce.manager.currency_precision;

import org.riverock.commerce.bean.CurrencyPrecisionBean;
import org.riverock.commerce.manager.currency.Currency;

/**
 * User: SergeMaslyukov
 * Date: 06.09.2006
 * Time: 1:50:44
 * <p/>
 * $Id$
 */
public class CurrencyPrecisionExtendedBean {
    private CurrencyPrecisionBean currencyPrecisionBean=null;
    private Currency currency =null;

    public CurrencyPrecisionExtendedBean() {
    }

    public CurrencyPrecisionExtendedBean(CurrencyPrecisionBean currencyPrecisionBean, Currency currency) {
        this.currencyPrecisionBean = currencyPrecisionBean;
        this.currency = currency;
    }

    public CurrencyPrecisionBean getCurrencyPrecisionBean() {
        return currencyPrecisionBean;
    }

    public Currency getCurrencyBean() {
        return currency;
    }
}

