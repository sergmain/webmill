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
import org.riverock.commerce.manager.currency.CurrencyBean;

/**
 * @author Sergei Maslyukov
 *         Date: 04.09.2006
 *         Time: 20:16:05
 *         <p/>
 *         $Id$
 */
public class ShopExtendedBean {
    private ShopBean shopBean=null;
    private CurrencyBean defaultCurrencyBean  = null;
    private CurrencyBean invoiceCurrencyBean  = null;

    public ShopExtendedBean(ShopBean shopBean, CurrencyBean defaultCurrencyBean, CurrencyBean invoiceCurrencyBean) {
        this.shopBean = shopBean;
        this.defaultCurrencyBean = defaultCurrencyBean;
        this.invoiceCurrencyBean = invoiceCurrencyBean;
    }

    public String getDefaultCurrencyName() {
        if (defaultCurrencyBean!=null) {
            return defaultCurrencyBean.getCurrencyName();
        }
        return "";
    }

    public String getInvoiceCurrencyName() {
        if (invoiceCurrencyBean!=null) {
            return invoiceCurrencyBean.getCurrencyName();
        }
        return "";
    }

    public ShopBean getShopBean() {
        return shopBean;
    }

    public void setShopBean(ShopBean shopBean) {
        this.shopBean = shopBean;
    }

    public CurrencyBean getDefaultCurrencyBean() {
        return defaultCurrencyBean;
    }

    public void setDefaultCurrencyBean(CurrencyBean defaultCurrencyBean) {
        this.defaultCurrencyBean = defaultCurrencyBean;
    }

    public CurrencyBean getInvoiceCurrencyBean() {
        return invoiceCurrencyBean;
    }

    public void setInvoiceCurrencyBean(CurrencyBean invoiceCurrencyBean) {
        this.invoiceCurrencyBean = invoiceCurrencyBean;
    }
}
