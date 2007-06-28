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

import org.riverock.commerce.bean.Shop;
import org.riverock.commerce.jsf.FacesTools;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.manager.shop.ShopExtendedBean;
import org.riverock.interfaces.ContainerConstants;
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

    public List<Shop> getShopList() {
        Long siteId = new Long( FacesTools.getPortletRequest().getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );

        List<Shop> list = CommerceDaoFactory.getShopDao().getShopList(siteId);
        if (list==null) {
            return new ArrayList<Shop>();
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
        if (!shopExtendedBean.getShop().getShopId().equals(shopId)) {
            log.warn("Mismatch shopId");
            shopExtendedBean=currencyPrecisionService.getShopExtended(shopId);
        }

        return shopExtendedBean;
    }
}