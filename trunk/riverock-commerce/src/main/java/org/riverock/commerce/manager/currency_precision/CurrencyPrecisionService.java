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

import org.riverock.commerce.manager.shop.ShopExtendedBean;
import org.riverock.commerce.manager.currency.CurrencyBean;
import org.riverock.commerce.bean.ShopBean;
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
        ShopBean bean = CommerceDaoFactory.getShopDao().getShop( shopId );
        if (log.isDebugEnabled()) {
            log.debug("ShopBean: " + bean);
        }
        if (bean==null) {
            return null;
        }

        CurrencyBean defaultCurrencyBean=null;
        if (bean.getDefaultCurrencyId()!=null) {
            defaultCurrencyBean=CommerceDaoFactory.getCurrencyDao().getCurrency(bean.getDefaultCurrencyId());
        }

        CurrencyBean invoiceCurrencyBean=null;
        if (bean.getInvoiceCurrencyId()!=null) {
            invoiceCurrencyBean=CommerceDaoFactory.getCurrencyDao().getCurrency(bean.getDefaultCurrencyId());
        }

        return new ShopExtendedBean(bean, defaultCurrencyBean, invoiceCurrencyBean);
    }
}