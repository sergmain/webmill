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

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.riverock.webmill.container.portlet.extend.GenericWebmillPortlet;

/**
 * User: SergeMaslyukov
 * Date: 07.12.2004
 * Time: 12:22:43
 * $Id$
 */
public final class ShopPortlet extends GenericWebmillPortlet {
    public final static String CTX_TYPE_SHOP = "mill.shop";
    public final static String CURRENT_SHOP = "MILL.CURRENT_SHOP";
    public final static String ORDER_SESSION = "MILL.ORDER_SESSION";
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
        OrderLogic.process( renderRequest );
        ShopPage shopPage = new ShopPage();
        doRender(renderRequest, renderResponse, shopPage);
    }
}
