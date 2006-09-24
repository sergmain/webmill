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
package org.riverock.commerce.price;

import java.util.ResourceBundle;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.portlet.schema.portlet.shop.CurrentBasketType;
import org.riverock.portlet.schema.price.OrderType;
import org.riverock.commerce.invoice.InvoicePortlet;
import org.riverock.webmill.container.tools.PortletService;
import org.riverock.webmill.container.ContainerConstants;

/**
 *
 * $Author$
 *
 * $Id$
 *
 */
public final class ShopBasket {

    public static CurrentBasketType getInstance(
        final OrderType order, final ShopPageParam shopParam,
        final RenderRequest renderRequest, final RenderResponse renderResponse,
        final ResourceBundle bundle) {

        if (order == null || OrderLogic.getCountItem(order)==0)
            return null;

        CurrentBasketType basket = new CurrentBasketType();

        basket.setCurrentBasketName( bundle.getString( "price.invoice" ) );

        basket.setItemInBasket( OrderLogic.getCountItem(order) );

        PortletURL portletURL = renderResponse.createRenderURL();
        portletURL.setParameter( ContainerConstants.NAME_TYPE_CONTEXT_PARAM, InvoicePortlet.CTX_TYPE_INVOICE );
        portletURL.setParameter( ShopPortlet.NAME_ID_GROUP_SHOP, PortletService.getInt( renderRequest, ShopPortlet.NAME_ID_GROUP_SHOP, 0 ).toString() );
        portletURL.setParameters( shopParam.currencyURL );
        portletURL.setParameter( ShopPortlet.NAME_ID_SHOP_PARAM, shopParam.id_shop.toString() );

        basket.setCurrentBasketUrl( portletURL.toString() );
        return basket;
    }

}