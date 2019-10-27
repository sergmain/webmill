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
package org.riverock.commerce.manager.shop;

import org.riverock.commerce.bean.Shop;
import org.riverock.commerce.bean.Currency;

/**
 * @author Sergei Maslyukov
 *         Date: 04.09.2006
 *         Time: 20:16:05
 *         <p/>
 *         $Id$
 */
public class ShopExtendedBean {
    private Shop shop =null;
    private Currency defaultCurrency = null;
    private Currency invoiceCurrency = null;

    public ShopExtendedBean(Shop shop, Currency defaultCurrency, Currency invoiceCurrency) {
        this.shop = shop;
        this.defaultCurrency = defaultCurrency;
        this.invoiceCurrency = invoiceCurrency;
    }

    public String getDefaultCurrencyName() {
        return defaultCurrency != null ? defaultCurrency.getCurrencyName() : "";
    }

    public String getInvoiceCurrencyName() {
        return invoiceCurrency != null ? invoiceCurrency.getCurrencyName() : "";
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Currency getDefaultCurrencyBean() {
        return defaultCurrency;
    }

    public void setDefaultCurrencyBean(Currency defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public Currency getInvoiceCurrencyBean() {
        return invoiceCurrency;
    }

    public void setInvoiceCurrencyBean(Currency invoiceCurrency) {
        this.invoiceCurrency = invoiceCurrency;
    }
}
