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
