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

/**
 *
 * $Author$
 *
 * $Id$
 *
 */
package org.riverock.portlet.price;

import java.util.ResourceBundle;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.portlet.main.Constants;
import org.riverock.portlet.schema.portlet.shop.CurrentBasketType;
import org.riverock.portlet.schema.price.OrderType;
import org.riverock.webmill.portlet.PortletTools;

public class ShopBasket {
//    private static Logger log = Logger.getLogger( ShopBasket.class );

    public static CurrentBasketType getInstance(
        OrderType order, ShopPageParam shopParam, RenderRequest renderRequest, RenderResponse renderResponse,
        ResourceBundle bundle) {

        if (order == null || OrderLogic.getCountItem(order) == 0)
            return null;

        CurrentBasketType basket = new CurrentBasketType();

//            basket.setCurrentBasketName( shopParam.sm.getStr("price.invoice") );
        basket.setCurrentBasketName( bundle.getString( "price.invoice" ) );

        basket.setItemInBasket( new Integer(OrderLogic.getCountItem(order)) );

        PortletURL portletURL = renderResponse.createRenderURL();
        portletURL.setParameter( org.riverock.webmill.main.Constants.NAME_TYPE_CONTEXT_PARAM, Constants.CTX_TYPE_INVOICE );
//        portletURL.setParameter( org.riverock.webmill.main.Constants.NAME_TEMPLATE_CONTEXT_PARAM, shopParam.nameTemplate );
        portletURL.setParameter( Constants.NAME_ID_GROUP_SHOP, PortletTools.getInt( renderRequest, Constants.NAME_ID_GROUP_SHOP ).toString() );
        portletURL.setParameters( shopParam.currencyURL );
        portletURL.setParameter( Constants.NAME_ID_SHOP_PARAM, shopParam.id_shop.toString() );

//            basket.setCurrentBasketUrl(
//                CtxInstance.url(Constants.CTX_TYPE_INVOICE) + '&' +
//                Constants.NAME_ID_CURRENCY_SHOP + '=' +
//                PortletTools.getInt( renderRequest, Constants.NAME_ID_CURRENCY_SHOP ) + '&' +
//                Constants.NAME_ID_GROUP_SHOP + '=' +
//                PortletTools.getInt( renderRequest, Constants.NAME_ID_GROUP_SHOP )
//            );
        basket.setCurrentBasketUrl( portletURL.toString() );
        return basket;
    }

}