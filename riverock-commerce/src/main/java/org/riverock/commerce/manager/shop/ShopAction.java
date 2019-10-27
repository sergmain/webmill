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

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.commerce.jsf.FacesTools;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.bean.Shop;
import org.riverock.commerce.bean.Currency;
import org.riverock.interfaces.ContainerConstants;

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

        Shop shop = new Shop();
        Long siteId = new Long( FacesTools.getPortletRequest().getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        shop.setSiteId(siteId);
        shopSessionBean.setShopBean(shop);

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

        Shop shop = CommerceDaoFactory.getShopDao().getShop( shopSessionBean.getCurrentShopId() );
        if (shop ==null) {
            return "shop";
        }

        shopSessionBean.setShopBean(shop);
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
        Shop bean = CommerceDaoFactory.getShopDao().getShop( shopSessionBean.getCurrentShopId() );
        if (bean==null) {
            setSessionBean( null );
            return;
        }

        Currency defaultCurrency =null;
        if (bean.getDefaultCurrencyId()!=null) {
            defaultCurrency =CommerceDaoFactory.getCurrencyDao().getCurrency(bean.getDefaultCurrencyId());
        }

        Currency invoiceCurrency =null;
        if (bean.getInvoiceCurrencyId()!=null) {
            invoiceCurrency =CommerceDaoFactory.getCurrencyDao().getCurrency(bean.getDefaultCurrencyId());
        }

        setSessionBean( new ShopExtendedBean(bean, defaultCurrency, invoiceCurrency) );
    }

    private void setSessionBean(ShopExtendedBean shopExtendedBean) {
        shopSessionBean.setShopExtendedBean(shopExtendedBean);
    }
}
