package org.riverock.portlet.price;

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
