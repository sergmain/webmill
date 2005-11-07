/*
 * org.riverock.portlet -- Portlet Library
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
 *
 */
package org.riverock.portlet.price;

import java.io.File;
import java.io.FileWriter;
import java.util.ResourceBundle;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;

import org.riverock.common.tools.DateTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.tools.XmlTools;
import org.riverock.portlet.schema.portlet.shop.ShopPageType;
import org.riverock.portlet.schema.price.CurrencyPrecisionType;
import org.riverock.portlet.tools.RequestTools;
import org.riverock.portlet.tools.SiteUtils;
import org.riverock.portlet.shop.bean.ShopOrder;
import org.riverock.webmill.container.portlet.extend.PortletResultContent;
import org.riverock.webmill.container.portlet.extend.PortletResultObject;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.ContainerConstants;

/**
 *
 * $Author$
 *
 * $Id$
 *
 */
public final class ShopPage implements PortletResultObject, PortletResultContent {
    private final static Log log = LogFactory.getLog( ShopPage.class );

    private ShopPageType shopPage = new ShopPageType();;
    private Shop shop = null;
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

    private static Object syncDebug = new Object();
    public byte[] getXml( String rootElement ) throws Exception {
        if ( log.isDebugEnabled() ) {
            synchronized(syncDebug) {
                log.debug( "Unmarshal ShopPage object" );
                FileWriter w = new FileWriter( SiteUtils.getTempDir()+File.separatorChar+"portlet-shop.xml" );
                Marshaller marsh = new Marshaller( w );
                marsh.setMarshalAsDocument( true );
                marsh.setEncoding( "utf-8" );
                marsh.setRootElement( rootElement );
                marsh.marshal( shopPage );
                marsh = null;
                w.flush();
                w.close();
                w = null;
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
            ShopOrder order = (ShopOrder) session.getAttribute( ShopPortlet.ORDER_SESSION );

            shop = (Shop) session.getAttribute( ShopPortlet.CURRENT_SHOP );

            if (shop == null)
            {
                if (log.isDebugEnabled())
                    log.debug("Shop session not initialized");

                throw new PortletException("shop session not iitialized. Add mill.order_logic portlet to template");
            }

            if (shop.currencyID == null)
            {
                throw new PortletException("Default currency not defined.<br>Login and configure shop.");
            }

            shopPage.setPriceHeader( shop.header );
            shopPage.setPriceFooter( shop.footer );

            shopPage.setDateUploadPrice( DateTools.getStringDate(shop.dateUpload, "dd MMM yyyy", renderRequest.getLocale()) );
            shopPage.setTimeUploadPrice( DateTools.getStringDate(shop.dateUpload, "HH:mm", renderRequest.getLocale()) );


            ShopPageParam shopParam = new ShopPageParam();

            shopParam.id_shop = shop.id_shop;
            shopParam.isProcessInvoice = shop.isProcessInvoice;

            shopPage.setIsProcessInvoice( shop.isProcessInvoice ? "true" : "false" );


            shopParam.id_group = PortletService.getLong( renderRequest, ShopPortlet.NAME_ID_GROUP_SHOP, 0L );
            shopParam.setServerName( renderRequest.getServerName() );
            shopParam.id_currency = PortletService.getLong( renderRequest, ShopPortlet.NAME_ID_CURRENCY_SHOP);

            // If current currency not defined( page requested without concrete currency),
            // we will use default currency
            if (shopParam.id_currency == null)
                shopParam.id_currency = shop.currencyID;

            shopParam.currencyURL.put( ShopPortlet.NAME_ID_CURRENCY_SHOP, shopParam.id_currency.toString() );

            if ((shopParam.id_currency == null) && (shop.is_default_currency == 1)){
                shopParam.currencyURL.clear();
                shopParam.id_currency = shop.currencyID;
            }

            if (shop.precisionList != null) {
                CurrencyPrecisionType item = shop.precisionList.getCurrencyPrecision(shopParam.id_currency);
                if (item != null)
                    shopParam.precision = item;
            }
            else {
                    log.warn("Precision not defined. Use default - 2 digit.");
            }

            // установка параметров для сортировки
            // sort_direct == 0 означает сортировки по возрастанию,
            // иначе сортировка по убыванию
            shopParam.sortBy = RequestTools.getString( renderRequest, ShopPortlet.NAME_SHOP_SORT_BY, "item");
            shopParam.sortDirect = PortletService.getInt( renderRequest, ShopPortlet.NAME_SHOP_SORT_DIRECT, 1);

            PortletURL itemPortletURL = renderResponse.createRenderURL();
            itemPortletURL.setParameter( ContainerConstants.NAME_TYPE_CONTEXT_PARAM, ShopPortlet.CTX_TYPE_SHOP );
            itemPortletURL.setParameter( ShopPortlet.NAME_ID_GROUP_SHOP, shopParam.id_group.toString() );
            itemPortletURL.setParameters( shopParam.currencyURL );
            itemPortletURL.setParameter( ShopPortlet.NAME_ID_SHOP_PARAM, shopParam.id_shop.toString() );
            itemPortletURL.setParameter( ShopPortlet.NAME_ID_CURRENCY_SHOP, shopParam.id_currency.toString() );

//            sortItemUrl = PortletService.url(Constants.CTX_TYPE_SHOP, shopParam.nameTemplate)+'&'+
//                Constants.NAME_ID_GROUP_SHOP + '=' + shopParam.id_group + '&' +
//                shopParam.currencyURL + '&' +
//                Constants.NAME_ID_SHOP_PARAM + '=' + shopParam.id_shop + '&' +
//                Constants.NAME_ID_CURRENCY_SHOP + '=' + shopParam.id_currency + '&';

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
//            sortItemUrl +=
//                Constants.NAME_SHOP_SORT_BY + "=item&" +
//                Constants.NAME_SHOP_SORT_DIRECT + '=' + ("DESC".equals(itemDirect) ? 1 : 0);

            pricePortletURL.setParameter(ShopPortlet.NAME_SHOP_SORT_BY, "price");
            pricePortletURL.setParameter(ShopPortlet.NAME_SHOP_SORT_DIRECT, ""+("DESC".equals(priceDirect) ? 1 : 0));
//            sortPriceUrl +=
//                Constants.NAME_SHOP_SORT_BY + "=price&" +
//                Constants.NAME_SHOP_SORT_DIRECT + '=' + ("DESC".equals(priceDirect) ? 1 : 0);

            shopPage.setSortItemUrl( itemPortletURL.toString() );
            shopPage.setSortPriceUrl( pricePortletURL.toString() );

            if (shop.isNeedRecalc) {
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
