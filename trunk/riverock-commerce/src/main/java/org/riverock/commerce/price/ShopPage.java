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

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import org.riverock.common.tools.DateTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.common.tools.XmlTools;
import org.riverock.commerce.bean.price.CurrencyPrecisionType;
import org.riverock.commerce.shop.bean.ShopOrder;
import org.riverock.commerce.tools.SiteUtils;
import org.riverock.commerce.bean.ShopBean;
import org.riverock.commerce.schema.shop.ShopPageType;
import org.riverock.webmill.container.ContainerConstants;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;
import org.riverock.webmill.container.tools.PortletService;

/**
 *
 * $Author$
 *
 * $Id$
 *
 */
public final class ShopPage implements PortletResultObject, PortletResultContent {
    private final static Logger log = Logger.getLogger( ShopPage.class );

    private ShopPageType shopPage = new ShopPageType();
    private ShopBean shopBean = null;
    private String itemDirect = "ASC";
    private String priceDirect = "ASC";
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

    public ShopPage() {
    }

    public PortletResultContent getInstance() throws PortletException {

        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance();
            PortletSession session = renderRequest.getPortletSession();
            ShopOrder order = (ShopOrder) session.getAttribute( ShopPortlet.ORDER_SESSION , PortletSession.APPLICATION_SCOPE);

            shopBean = (ShopBean) session.getAttribute( ShopPortlet.CURRENT_SHOP, PortletSession.APPLICATION_SCOPE );

            if (shopBean == null) {
                throw new PortletException("shop session not initialized");
            }

            if (shopBean.getDefaultCurrencyId() == null) {
                throw new PortletException("Default currency not defined.<br>Login and configure shop.");
            }

            shopPage.setPriceHeader( shopBean.getHeader() );
            shopPage.setPriceFooter( shopBean.getFooter() );

            shopPage.setDateUploadPrice( DateTools.getStringDate(shopBean.getDateUpload(), "dd MMM yyyy", renderRequest.getLocale()) );
            shopPage.setTimeUploadPrice( DateTools.getStringDate(shopBean.getDateUpload(), "HH:mm", renderRequest.getLocale()) );


            ShopPageParam shopParam = new ShopPageParam();

            shopParam.id_shop = shopBean.getShopId();
            shopParam.isProcessInvoice = shopBean.isProcessInvoice();

            shopPage.setIsProcessInvoice( shopBean.isProcessInvoice() ? "true" : "false" );


            shopParam.id_group = PortletService.getLong( renderRequest, ShopPortlet.NAME_ID_GROUP_SHOP, 0L );
            Long siteId = new Long( renderRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
            shopParam.setServerName( renderRequest.getServerName(), siteId );
            shopParam.id_currency = PortletService.getLong( renderRequest, ShopPortlet.NAME_ID_CURRENCY_SHOP);

            // If current currency not defined( page requested without concrete currency),
            // we will use default currency
            if (shopParam.id_currency == null)
            {
                shopParam.id_currency = shopBean.getDefaultCurrencyId();
            }

            shopParam.currencyURL.put( ShopPortlet.NAME_ID_CURRENCY_SHOP, shopParam.id_currency.toString() );

            if ((shopParam.id_currency == null) && (shopBean.isDefaultCurrency())){
                shopParam.currencyURL.clear();
                shopParam.id_currency = shopBean.getDefaultCurrencyId();
            }

            if (shopBean.getPrecisionList() != null) {
                CurrencyPrecisionType item = shopBean.getPrecisionList().getCurrencyPrecision(shopParam.id_currency);
                if (item != null)
                    shopParam.precision = item;
            }
            else {
                    log.warn("Precision not defined. Use default - 2 digit.");
            }

            // ��������� ���������� ��� ����������
            // sort_direct == 0 �������� ���������� �� �����������,
            // ����� ���������� �� ��������
            shopParam.sortBy = PortletService.getString(renderRequest, ShopPortlet.NAME_SHOP_SORT_BY, "item");
            shopParam.sortDirect = PortletService.getInt( renderRequest, ShopPortlet.NAME_SHOP_SORT_DIRECT, 1);

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

            if (shopBean.isNeedRecalc()) {
                shopPage.setCurrencyList(
                    PriceCurrency.getCurrencyList(
                        shopParam,
                        renderResponse.createRenderURL().toString(),
                        renderRequest,
                        resourceBundle )
                );
            }

            shopPage.setPricePosition( PriceListPosition.getInstance(db_, shopParam, renderResponse) );
            shopPage.setCurrentBasket( ShopBasket.getInstance(order, shopParam, renderRequest, renderResponse, resourceBundle ) );
            shopPage.setGroupList( PriceGroup.getInstance(db_, shopParam, renderResponse) );
            shopPage.setItemList( PriceListItemList.getInstance(db_, shopParam, renderRequest, renderResponse, resourceBundle ) );
        } catch (Throwable e) {
            String es = "Error in getInstance()";
            log.error(es, e);
            throw new PortletException(es, e);
        }
        finally {
            DatabaseManager.close(db_);
            db_ = null;
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
