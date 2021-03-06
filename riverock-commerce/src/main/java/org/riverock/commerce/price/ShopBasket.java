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

import java.util.ResourceBundle;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.commerce.invoice.InvoicePortlet;
import org.riverock.commerce.schema.shop.CurrentBasketType;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.common.utils.PortletUtils;
import org.riverock.interfaces.ContainerConstants;

/**
 *
 * $Author: serg_main $
 *
 * $Id: ShopBasket.java 1229 2007-06-28 11:25:40Z serg_main $
 *
 */
public final class ShopBasket {

    public static CurrentBasketType getInstance(
        final Long userOrderId, final ShopPageParam shopParam,
        final RenderRequest renderRequest, final RenderResponse renderResponse,
        final ResourceBundle bundle) {

        if (userOrderId == null) {
            return null;
        }
        int count = CommerceDaoFactory.getOrderDao().countItemsInUserOrder(userOrderId);
        if (count==0) {
            return null;
        }

        CurrentBasketType basket = new CurrentBasketType();

        basket.setCurrentBasketName( bundle.getString( "price.invoice" ) );

        basket.setItemInBasket( count );

        PortletURL portletURL = renderResponse.createRenderURL();
        portletURL.setParameter( ContainerConstants.NAME_TYPE_CONTEXT_PARAM, InvoicePortlet.CTX_TYPE_INVOICE );
        portletURL.setParameter( ShopPortlet.NAME_ID_GROUP_SHOP, PortletUtils.getInt( renderRequest, ShopPortlet.NAME_ID_GROUP_SHOP, 0 ).toString() );
        portletURL.setParameters( shopParam.currencyURL );
        portletURL.setParameter( ShopPortlet.NAME_ID_SHOP_PARAM, shopParam.id_shop.toString() );

        basket.setCurrentBasketUrl( portletURL.toString() );
        return basket;
    }

}