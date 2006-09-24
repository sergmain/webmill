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
package org.riverock.commerce.manager.shop;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.commerce.jsf.FacesTools;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.bean.ShopBean;
import org.riverock.commerce.manager.currency.CurrencyBean;
import org.riverock.webmill.container.ContainerConstants;

/**
 * @author Sergei Maslyukov
 *         Date: 04.09.2006
 *         Time: 17:03:05
 *         <p/>
 *         $Id: PriceCurrency.java 950 2006-09-01 18:11:51Z serg_main $
 */
public class ShopAction implements Serializable {
    private final static Logger log = Logger.getLogger( ShopAction.class );

    private static final long serialVersionUID = 3817005501L;

    private ShopSessionBean shopSessionBean = null;

    public ShopAction() {
    }

    public ShopSessionBean getShopSessionBean() {
        return shopSessionBean;
    }

    public void setShopSessionBean(ShopSessionBean shopSessionBean) {
        this.shopSessionBean = shopSessionBean;
    }    // Add shop

    public String addShop() {
        log.debug("Start addShop()");

        ShopBean shopBean = new ShopBean();
        Long siteId = new Long( FacesTools.getPortletRequest().getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        shopBean.setSiteId(siteId);
        shopSessionBean.setShopBean( shopBean );

        return "shop-add";
    }

    public String processAddShop() {
        Long id = CommerceDaoFactory.getShopDao().createShop( shopSessionBean.getShopBean() );
        shopSessionBean.setCurrentShopId( id );
        loadCurrentShop();
        return "shop";
    }

    public String cancelAddShop() {
        shopSessionBean.setShopBean( null );
        return "shop";
    }

    // Edit shop

    public String editShop() {
        log.debug("Start editShop()");

        ShopBean shopBean = CommerceDaoFactory.getShopDao().getShop( shopSessionBean.getCurrentShopId() );
        if (shopBean==null) {
            return "shop";
        }

        shopSessionBean.setShopBean( shopBean );
        return "shop-edit";
    }

    public String processEditShop() {
        CommerceDaoFactory.getShopDao().updateShop( shopSessionBean.getShopBean() );
        loadCurrentShop();
        return "shop";
    }

    public String cancelEditShop() {
        loadCurrentShop();
        return "shop";
    }

    // Delete shop

    public String processDeleteShop() {
        CommerceDaoFactory.getShopDao().deleteShop( shopSessionBean.getCurrentShopId() );
        // For virtual delete
        loadCurrentShop();
        // for perm delete
//        setSessionBean(null);
        return "shop";
    }

    // common

    public String selectShop() {
        loadCurrentShop();
        return "shop";
    }

    private void loadCurrentShop() {
        ShopBean bean = CommerceDaoFactory.getShopDao().getShop( shopSessionBean.getCurrentShopId() );
        if (bean==null) {
            setSessionBean( null );
            return;
        }

        CurrencyBean defaultCurrencyBean=null;
        if (bean.getDefaultCurrencyId()!=null) {
            defaultCurrencyBean=CommerceDaoFactory.getCurrencyDao().getCurrency(bean.getDefaultCurrencyId());
        }

        CurrencyBean invoiceCurrencyBean=null;
        if (bean.getInvoiceCurrencyId()!=null) {
            invoiceCurrencyBean=CommerceDaoFactory.getCurrencyDao().getCurrency(bean.getDefaultCurrencyId());
        }

        setSessionBean( new ShopExtendedBean(bean, defaultCurrencyBean, invoiceCurrencyBean ) );
    }

    private void setSessionBean(ShopExtendedBean shopExtendedBean) {
        shopSessionBean.setShopExtendedBean(shopExtendedBean);
    }
}
