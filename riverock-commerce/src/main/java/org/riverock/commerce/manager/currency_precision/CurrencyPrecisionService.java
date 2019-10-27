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

import org.riverock.commerce.manager.shop.ShopExtendedBean;
import org.riverock.commerce.bean.Currency;
import org.riverock.commerce.bean.Shop;
import org.riverock.commerce.dao.CommerceDaoFactory;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 06.09.2006
 * Time: 0:05:46
 * <p/>
 * $Id: PriceCurrency.java 950 2006-09-01 18:11:51Z serg_main $
 */
public class CurrencyPrecisionService implements Serializable {
    private final static Logger log = Logger.getLogger(CurrencyPrecisionDataProvider.class);

    private static final long serialVersionUID = 3815005515L;

    public CurrencyPrecisionService() {
    }

    public ShopExtendedBean getShopExtended(Long shopId) {
        Shop bean = CommerceDaoFactory.getShopDao().getShop( shopId );
        if (log.isDebugEnabled()) {
            log.debug("Shop: " + bean);
        }
        if (bean==null) {
            return null;
        }

        Currency defaultCurrency =null;
        if (bean.getDefaultCurrencyId()!=null) {
            defaultCurrency =CommerceDaoFactory.getCurrencyDao().getCurrency(bean.getDefaultCurrencyId());
        }

        Currency invoiceCurrency =null;
        if (bean.getInvoiceCurrencyId()!=null) {
            invoiceCurrency =CommerceDaoFactory.getCurrencyDao().getCurrency(bean.getDefaultCurrencyId());
        }

        return new ShopExtendedBean(bean, defaultCurrency, invoiceCurrency);
    }
}