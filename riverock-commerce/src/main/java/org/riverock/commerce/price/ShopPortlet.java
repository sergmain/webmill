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

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.commerce.bean.Shop;
import org.riverock.common.portlet.GenericWebmillPortlet;

/**
 * User: SergeMaslyukov
 * Date: 07.12.2004
 * Time: 12:22:43
 * $Id: ShopPortlet.java 1229 2007-06-28 11:25:40Z serg_main $
 */
public final class ShopPortlet extends GenericWebmillPortlet {
    public final static String CTX_TYPE_SHOP = "mill.shop";
    public final static String CURRENT_SHOP_ID = "WEBMILL.COMMERCE.CURRENT_SHOP_ID";
    public final static String USER_ORDER_ID = "WEBMILL.COMMERCE.ORDER_SESSION";
    public final static String ID_SHOP_SESSION = "MILL.ID_SHOP_SESSION";
    public final static String NAME_INVOICE_NEW_COUNT_PARAM = "mill.invoice.count";
    public final static String NAME_SHOP_SORT_BY = "mill.shop.sort";
    public final static String NAME_SHOP_SORT_DIRECT = "mill.shop.sort.direct";
    public final static String NAME_ID_GROUP_SHOP = "mill.id_group";
    public final static String NAME_ID_CURRENCY_SHOP   = "mill.id_currency";
    public final static String NAME_COUNT_ADD_ITEM_SHOP    = "mill.count_add";
    public final static String NAME_ADD_ID_ITEM = "mill.add_id";
    public final static String NAME_ID_SHOP_PARAM = "mill.id_shop";

    public ShopPortlet(){}

    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) {
    }

    public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
        Shop shop = OrderLogic.prepareCurrenctRequest( renderRequest );
        if (shop == null) {
            throw new PortletException("Current shop not initialized");
        }

        ShopPage shopPage = new ShopPage( shop);
        doRender(renderRequest, renderResponse, shopPage);
    }
}
