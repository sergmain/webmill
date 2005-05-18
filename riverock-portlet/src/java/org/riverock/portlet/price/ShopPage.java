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

import java.io.FileWriter;
import java.util.ResourceBundle;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletConfig;

import org.riverock.common.tools.DateTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.tools.XmlTools;
import org.riverock.portlet.main.Constants;
import org.riverock.portlet.schema.portlet.shop.ShopPageType;
import org.riverock.portlet.schema.price.CurrencyPrecisionType;
import org.riverock.portlet.schema.price.OrderType;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.webmill.portlet.PortletResultObject;
import org.riverock.webmill.portlet.PortletTools;
import org.riverock.webmill.portlet.PortletResultContent;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;

/**
 *
 * $Author$
 *
 * $Id$
 *
 */
public final class ShopPage implements PortletResultObject, PortletResultContent {
    private final static Logger log = Logger.getLogger( ShopPage.class );

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
                FileWriter w = new FileWriter( WebmillConfig.getWebmillDebugDir()+"portlet-shop.xml" );
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

    public PortletResultContent getInstance( DatabaseAdapter db_ ) throws PortletException {

        try {
            PortletSession session = renderRequest.getPortletSession();
            OrderType order = (OrderType) session.getAttribute( Constants.ORDER_SESSION );

            shop = (Shop) session.getAttribute( Constants.CURRENT_SHOP );

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


            shopParam.id_group = PortletTools.getLong( renderRequest, Constants.NAME_ID_GROUP_SHOP, new Long(0) );
            shopParam.setServerName( renderRequest.getServerName() );
            shopParam.id_currency = PortletTools.getLong( renderRequest, Constants.NAME_ID_CURRENCY_SHOP);

            // If current currency not defined( page requested without concrete currency),
            // we will use default currency
            if (shopParam.id_currency == null)
                shopParam.id_currency = shop.currencyID;

            shopParam.currencyURL.put( Constants.NAME_ID_CURRENCY_SHOP, shopParam.id_currency.toString() );

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
            shopParam.sortBy = PortletTools.getString( renderRequest, Constants.NAME_SHOP_SORT_BY, "item");
            shopParam.sortDirect = PortletTools.getInt( renderRequest, Constants.NAME_SHOP_SORT_DIRECT, new Integer(1)).intValue();

            PortletURL itemPortletURL = renderResponse.createRenderURL();
            itemPortletURL.setParameter( org.riverock.webmill.main.Constants.NAME_TYPE_CONTEXT_PARAM, Constants.CTX_TYPE_SHOP );
            itemPortletURL.setParameter( Constants.NAME_ID_GROUP_SHOP, shopParam.id_group.toString() );
            itemPortletURL.setParameters( shopParam.currencyURL );
            itemPortletURL.setParameter( Constants.NAME_ID_SHOP_PARAM, shopParam.id_shop.toString() );
            itemPortletURL.setParameter( Constants.NAME_ID_CURRENCY_SHOP, shopParam.id_currency.toString() );

//            sortItemUrl = PortletTools.url(Constants.CTX_TYPE_SHOP, shopParam.nameTemplate)+'&'+
//                Constants.NAME_ID_GROUP_SHOP + '=' + shopParam.id_group + '&' +
//                shopParam.currencyURL + '&' +
//                Constants.NAME_ID_SHOP_PARAM + '=' + shopParam.id_shop + '&' +
//                Constants.NAME_ID_CURRENCY_SHOP + '=' + shopParam.id_currency + '&';

            PortletURL pricePortletURL = renderResponse.createRenderURL();
            pricePortletURL.setParameter( org.riverock.webmill.main.Constants.NAME_TYPE_CONTEXT_PARAM, Constants.CTX_TYPE_SHOP );
            pricePortletURL.setParameter( Constants.NAME_ID_GROUP_SHOP, shopParam.id_group.toString() );
            pricePortletURL.setParameters( shopParam.currencyURL );
            pricePortletURL.setParameter( Constants.NAME_ID_SHOP_PARAM, shopParam.id_shop.toString() );
            pricePortletURL.setParameter(  Constants.NAME_ID_CURRENCY_SHOP, shopParam.id_currency.toString() );

            if ("item".equals(shopParam.sortBy)) {
                itemDirect = (shopParam.sortDirect == 0 ? "DESC" : "ASC");
                shopPage.setItemDirect( itemDirect );
            }
            else if ("price".equals(shopParam.sortBy)) {
                priceDirect = (shopParam.sortDirect == 0 ? "DESC" : "ASC");
                shopPage.setPriceDirect( priceDirect );
            }

            itemPortletURL.setParameter(Constants.NAME_SHOP_SORT_BY, "item");
            itemPortletURL.setParameter(Constants.NAME_SHOP_SORT_DIRECT, ""+("DESC".equals(itemDirect) ? 1 : 0));
//            sortItemUrl +=
//                Constants.NAME_SHOP_SORT_BY + "=item&" +
//                Constants.NAME_SHOP_SORT_DIRECT + '=' + ("DESC".equals(itemDirect) ? 1 : 0);

            pricePortletURL.setParameter(Constants.NAME_SHOP_SORT_BY, "price");
            pricePortletURL.setParameter(Constants.NAME_SHOP_SORT_DIRECT, ""+("DESC".equals(priceDirect) ? 1 : 0));
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
        return this;
    }

    public PortletResultContent getInstance(DatabaseAdapter db__, Long id) throws PortletException {
        return getInstance( db__ );
    }

    public PortletResultContent getInstanceByCode(DatabaseAdapter db__, String portletCode_) throws PortletException {
        return getInstance( db__ );
    }
}
