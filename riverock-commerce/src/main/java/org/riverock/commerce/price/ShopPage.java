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
package org.riverock.commerce.price;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.XmlTools;
import org.riverock.commerce.tools.SiteUtils;
import org.riverock.commerce.bean.Shop;
import org.riverock.commerce.schema.shop.ShopPageType;
import org.riverock.interfaces.ContainerConstants;
import org.riverock.interfaces.portlet.PortletResultContent;
import org.riverock.interfaces.portlet.PortletResultObject;
import org.riverock.common.utils.PortletUtils;

/**
 *
 * $Author: serg_main $
 *
 * $Id: ShopPage.java 1229 2007-06-28 11:25:40Z serg_main $
 *
 */
public final class ShopPage implements PortletResultObject, PortletResultContent {
    private final static Logger log = Logger.getLogger( ShopPage.class );

    private ShopPageType shopPage = new ShopPageType();
    private String itemDirect = "ASC";
    private String priceDirect = "ASC";
    private Shop shop;
    private RenderRequest renderRequest = null;
    private RenderResponse renderResponse = null;
    private PortletConfig portletConfig = null;
    private ResourceBundle resourceBundle = null;

    public void setParameters( RenderRequest renderRequest, RenderResponse renderResponse, PortletConfig portletConfig ) {
        this.renderRequest = renderRequest;
        this.renderResponse = renderResponse;
        this.portletConfig = portletConfig;
        this.resourceBundle = this.portletConfig.getResourceBundle( renderRequest.getLocale() );
    }

    protected void finalize() throws Throwable {
        shopPage = null;
        super.finalize();
    }

    public byte[] getXml() throws Exception {
        return getXml( "ShopPage" );
    }

    private final static Object syncDebug = new Object();
    public byte[] getXml( String rootElement ) throws Exception {
        if ( log.isDebugEnabled() ) {
            synchronized(syncDebug) {
                try {
                    log.debug( "Unmarshal ShopPage object" );
                    FileOutputStream w = new FileOutputStream( SiteUtils.getTempDir()+File.separatorChar+"portlet-shop.xml" );
                    w.write(XmlTools.getXml( shopPage, rootElement ));
                    w.flush();
                    w.close();
                    w = null;
                } catch (Throwable e) {
                    log.debug("Error marshal debug", e);
                }
            }
        }
        return XmlTools.getXml( shopPage, rootElement );
    }

    public byte[] getPlainHTML() {
        return null;
    }

    public ShopPage(Shop shop) {
        this.shop=shop;
    }

    public PortletResultContent getInstance() throws PortletException {

        try {
            PortletSession session = renderRequest.getPortletSession();
            Long userOrderId = (Long) session.getAttribute( ShopPortlet.USER_ORDER_ID, PortletSession.APPLICATION_SCOPE);

            if (shop.getDefaultCurrencyId() == null) {
                throw new PortletException("Default currency not defined.<br>Login and configure shop.");
            }

            shopPage.setPriceHeader( shop.getHeader() );
            shopPage.setPriceFooter( shop.getFooter() );

            // TODO switch from TimeZone.getDefault() to site timezone
            shopPage.setDateUploadPrice( DateTools.getStringDate(shop.getDateUpload(), "dd MMM yyyy", renderRequest.getLocale(), TimeZone.getDefault()) );
            shopPage.setTimeUploadPrice( DateTools.getStringDate(shop.getDateUpload(), "HH:mm", renderRequest.getLocale(), TimeZone.getDefault()) );


            ShopPageParam shopParam = new ShopPageParam();

            shopParam.id_shop = shop.getShopId();
            shopParam.isProcessInvoice = shop.isProcessInvoice();

            shopPage.setIsProcessInvoice( shop.isProcessInvoice() ? "true" : "false" );


            shopParam.id_group = PortletUtils.getLong( renderRequest, ShopPortlet.NAME_ID_GROUP_SHOP, 0L );
            Long siteId = new Long( renderRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
            shopParam.setServerName( renderRequest.getServerName(), siteId );
            shopParam.id_currency = PortletUtils.getLong( renderRequest, ShopPortlet.NAME_ID_CURRENCY_SHOP);

            // If current currency not defined( page requested without concrete currency),
            // we will use default currency
            if (shopParam.id_currency == null)
            {
                shopParam.id_currency = shop.getDefaultCurrencyId();
            }

            shopParam.currencyURL.put( ShopPortlet.NAME_ID_CURRENCY_SHOP, shopParam.id_currency.toString() );

            if ((shopParam.id_currency == null) && (shop.isDefaultCurrency())){
                shopParam.currencyURL.clear();
                shopParam.id_currency = shop.getDefaultCurrencyId();
            }

            shopParam.sortBy = PortletUtils.getString(renderRequest, ShopPortlet.NAME_SHOP_SORT_BY, "item");
            shopParam.sortDirect = PortletUtils.getInt( renderRequest, ShopPortlet.NAME_SHOP_SORT_DIRECT, 1);

            PortletURL itemPortletURL = renderResponse.createRenderURL();
            itemPortletURL.setParameter( ContainerConstants.NAME_TYPE_CONTEXT_PARAM, ShopPortlet.CTX_TYPE_SHOP );
            itemPortletURL.setParameter( ShopPortlet.NAME_ID_GROUP_SHOP, shopParam.id_group.toString() );
            itemPortletURL.setParameters( shopParam.currencyURL );
            itemPortletURL.setParameter( ShopPortlet.NAME_ID_SHOP_PARAM, shopParam.id_shop.toString() );
            itemPortletURL.setParameter( ShopPortlet.NAME_ID_CURRENCY_SHOP, shopParam.id_currency.toString() );

            PortletURL pricePortletURL = renderResponse.createRenderURL();
            pricePortletURL.setParameter( ContainerConstants.NAME_TYPE_CONTEXT_PARAM, ShopPortlet.CTX_TYPE_SHOP );
            pricePortletURL.setParameter( ShopPortlet.NAME_ID_GROUP_SHOP, shopParam.id_group.toString() );
            pricePortletURL.setParameters( shopParam.currencyURL );
            pricePortletURL.setParameter( ShopPortlet.NAME_ID_SHOP_PARAM, shopParam.id_shop.toString() );
            pricePortletURL.setParameter(  ShopPortlet.NAME_ID_CURRENCY_SHOP, shopParam.id_currency.toString() );

            if ("item".equals(shopParam.sortBy)) {
                itemDirect = (shopParam.sortDirect == 0 ? "DESC" : "ASC");
                shopPage.setItemDirect( itemDirect );
            }
            else if ("price".equals(shopParam.sortBy)) {
                priceDirect = (shopParam.sortDirect == 0 ? "DESC" : "ASC");
                shopPage.setPriceDirect( priceDirect );
            }

            itemPortletURL.setParameter(ShopPortlet.NAME_SHOP_SORT_BY, "item");
            itemPortletURL.setParameter(ShopPortlet.NAME_SHOP_SORT_DIRECT, ""+("DESC".equals(itemDirect) ? 1 : 0));

            pricePortletURL.setParameter(ShopPortlet.NAME_SHOP_SORT_BY, "price");
            pricePortletURL.setParameter(ShopPortlet.NAME_SHOP_SORT_DIRECT, ""+("DESC".equals(priceDirect) ? 1 : 0));

            shopPage.setSortItemUrl( itemPortletURL.toString() );
            shopPage.setSortPriceUrl( pricePortletURL.toString() );

            if (shop.isNeedRecalc()) {
                shopPage.setCurrencyList(
                    PriceCurrency.getCurrencyList(
                        shopParam,
                        renderResponse.createRenderURL().toString(),
                        renderRequest,
                        resourceBundle )
                );
            }

            shopPage.setPricePosition( PriceListPosition.getInstance(shopParam, renderResponse) );
            shopPage.setCurrentBasket( ShopBasket.getInstance(userOrderId, shopParam, renderRequest, renderResponse, resourceBundle ) );
            shopPage.setGroupList( PriceGroup.getInstance(shopParam, renderResponse) );
            shopPage.setItemList( PriceListItemList.getInstance(shopParam, renderRequest, renderResponse, resourceBundle ) );
        } catch (Throwable e) {
            String es = "Error in getInstance()";
            log.error(es, e);
            throw new PortletException(es, e);
        }
        return this;
    }

    public PortletResultContent getInstance(Long id) throws PortletException {
        return getInstance();
    }

    public PortletResultContent getInstanceByCode( String portletCode_) throws PortletException {
        return getInstance();
    }
}
