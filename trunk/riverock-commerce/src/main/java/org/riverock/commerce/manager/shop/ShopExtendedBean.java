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

import org.riverock.commerce.bean.ShopBean;
import org.riverock.commerce.manager.currency.Currency;

/**
 * @author Sergei Maslyukov
 *         Date: 04.09.2006
 *         Time: 20:16:05
 *         <p/>
 *         $Id$
 */
public class ShopExtendedBean {
    private ShopBean shopBean=null;
    private Currency defaultCurrency = null;
    private Currency invoiceCurrency = null;

    public ShopExtendedBean(ShopBean shopBean, Currency defaultCurrency, Currency invoiceCurrency) {
        this.shopBean = shopBean;
        this.defaultCurrency = defaultCurrency;
        this.invoiceCurrency = invoiceCurrency;
    }

    public String getDefaultCurrencyName() {
        if (defaultCurrency !=null) {
            return defaultCurrency.getCurrencyName();
        }
        return "";
    }

    public String getInvoiceCurrencyName() {
        if (invoiceCurrency !=null) {
            return invoiceCurrency.getCurrencyName();
        }
        return "";
    }

    public ShopBean getShopBean() {
        return shopBean;
    }

    public void setShopBean(ShopBean shopBean) {
        this.shopBean = shopBean;
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
