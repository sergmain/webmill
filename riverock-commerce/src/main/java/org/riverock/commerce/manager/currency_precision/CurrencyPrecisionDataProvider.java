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

import org.riverock.commerce.bean.ShopBean;
import org.riverock.commerce.jsf.FacesTools;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.manager.shop.ShopExtendedBean;
import org.riverock.webmill.container.ContainerConstants;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * User: SergeMaslyukov
 * Date: 06.09.2006
 * Time: 0:05:32
 * <p/>
 * $Id: PriceCurrency.java 950 2006-09-01 18:11:51Z serg_main $
 */
public class CurrencyPrecisionDataProvider implements Serializable {
    private final static Logger log = Logger.getLogger(CurrencyPrecisionDataProvider.class);
    private static final long serialVersionUID = 3815005509L;

    private CurrencyPrecisionSessionBean currencyPrecisionSessionBean = null;
    private CurrencyPrecisionService currencyPrecisionService = null;

    private ShopExtendedBean shopExtendedBean=null;

    public CurrencyPrecisionService getCurrencyPrecisionService() {
        return currencyPrecisionService;
    }

    public void setCurrencyPrecisionService(CurrencyPrecisionService currencyPrecisionService) {
        this.currencyPrecisionService = currencyPrecisionService;
    }

    public CurrencyPrecisionSessionBean getCurrencyPrecisionSessionBean() {
        return currencyPrecisionSessionBean;
    }

    public void setCurrencyPrecisionSessionBean(CurrencyPrecisionSessionBean currencyPrecisionSessionBean) {
        this.currencyPrecisionSessionBean = currencyPrecisionSessionBean;
    }

    public List<ShopBean> getShopList() {
        Long siteId = new Long( FacesTools.getPortletRequest().getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );

        List<ShopBean> list = CommerceDaoFactory.getShopDao().getShopList(siteId);
        if (list==null) {
            return new ArrayList<ShopBean>();
        }
        return list;
    }

    public ShopExtendedBean getShopExtendedBean() {
        if (currencyPrecisionSessionBean.getObjectType()!=currencyPrecisionSessionBean.getShopType()) {
            throw new IllegalStateException("Query shop info with not shop type, current type: " + currencyPrecisionSessionBean.getObjectType());
        }
        Long shopId = currencyPrecisionSessionBean.getCurrentShopId();
        if (log.isDebugEnabled()) {
            log.debug("get extended info for shopId " + shopId);
        }
        if (shopExtendedBean==null) {
            shopExtendedBean=currencyPrecisionService.getShopExtended(shopId);
        }
        if (!shopExtendedBean.getShopBean().getShopId().equals(shopId)) {
            log.warn("Mismatch shopId");
            shopExtendedBean=currencyPrecisionService.getShopExtended(shopId);
        }

        return shopExtendedBean;
    }
}