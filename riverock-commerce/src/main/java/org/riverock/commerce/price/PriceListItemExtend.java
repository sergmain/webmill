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

/**
 * $Id$
 */
public class PriceListItemExtend extends PriceListItem
{
/*
    private static Logger cat = Logger.getLogger("org.riverock.commerce.price.PriceListItemExtend");

    public String desc = "";
    public String imageNameFile = null;
    public long imageID;
    public Vector hiddenParam = new Vector();

    private ShopPageParam shopParam = null;

    public String getItemToInvoice()
    {
        if (shopParam.isProcessInvoice)
            return param.response.encodeURL(ctxInstance.ctx());
        else
            return null;
    }

    public String getItemToInvoiceCountParam()
    {
        if (shopParam.isProcessInvoice)
            return Constants.NAME_COUNT_ADD_ITEM_SHOP;
        else
            return null;
    }

    public String getItemPrice()
        throws Exception
    {
        try
        {
            HttpSession session = param.request.getSession();
            OrderType order = (OrderType) session.getAttribute(Constants.ORDER_SESSION);
            Shop shop = (Shop) session.getAttribute(Constants.CURRENT_SHOP);

            return NumberTools.toString(PriceList.calcPrice(priceItem,
                new Object[]{shop, order.getAuthSession(), shopParam.precision}),
                shopParam.precision.getPrecision()
            );
        }
        catch (Exception e)
        {
            cat.error( "Erro get item price", e );
            throw e;
        }

    }

    public String getItemInBasket()
    {
        HttpSession session = param.request.getSession();
        OrderType order = (OrderType) session.getAttribute(Constants.ORDER_SESSION);

        return (OrderLogic.isItemInBasket(idPK, order) ? "InBasket" : null);
    }

    public void setParameter(PortletParameter param_)
    {
        this.param = param_;
        if (shopParam.isProcessInvoice)
        {
            hiddenParam.add(
                new HiddenParam(Constants.NAME_ID_SHOP_PARAM, "" + id_shop)
            );
            hiddenParam.add(
                new HiddenParam(Constants.NAME_ID_GROUP_SHOP, "" + id_group)
            );
            hiddenParam.add(
                new HiddenParam(Constants.NAME_ID_CURRENCY_SHOP, "" + id_currency)
            );
            hiddenParam.add(
                new HiddenParam(Constants.NAME_ADD_ID_ITEM, "" + idPK)
            );
            hiddenParam.add(
                new HiddenParam(Constants.NAME_ID_CURRENCY_SHOP, "" + id_currency)
            );
            hiddenParam.add(
                new HiddenParam(Constants.NAME_TYPE_CONTEXT_PARAM
                    , Constants.CTX_TYPE_SHOP)
            );
            hiddenParam.add(
                new HiddenParam(Constants.NAME_TEMPLATE_CONTEXT_PARAM
                    , shopParam.nameTemplate)
            );
            hiddenParam.add(
                new HiddenParam(Constants.NAME_SHOP_SORT_BY
                    , shopParam.sortBy)
            );
            hiddenParam.add(
                new HiddenParam(Constants.NAME_SHOP_SORT_DIRECT
                    , "" + shopParam.sortDirect)
            );
        }
    }

    public PriceListItemExtend(DatabaseAdapter db_, long id_shop, String serverName, long id_item)
        throws PriceException
    {
        super(db_, id_shop, serverName, id_item);

        if (cat.isDebugEnabled())
            cat.debug("#1.0002");

        initDescription(db_);

        if (cat.isDebugEnabled())
            cat.debug("#1.0003");

    }

    private void initDescription(DatabaseAdapter db_)
        throws PriceException
    {

        if (cat.isDebugEnabled())
            cat.debug("#3.0001");

        PreparedStatement ps = null;
        ResultSet rs = null;


        try
        {
            ps = db_.conn.prepareCall(
                "select a.text from WM_PRICE_ITEM_DESCRIPTION a " +
                "where  a.id_item = ? " +
                "order by ID_PRICE_ITEM_DESCRIPTION asc"
            );

            RsetTools.setLong(ps, 1, idPK);

            rs = ps.executeQuery();

            while (rs.next())
                desc += RsetTools.getString(rs, "TEXT");


        }
        catch (Exception e)
        {
            throw new PriceException(e.toString());
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }

        final String sql_ =
            "select b.name_file image_name_file, a.id_image_dir " +
            "from   WM_IMAGE_PRICE_ITEMS a, WM_IMAGE_DIR b " +
            "where  a.id_item = ? and a.id_image_dir = b.id_image_dir ";

        if (cat.isDebugEnabled())
            cat.debug("#3.0002");

        try
        {
            ps = db_.conn.prepareCall(sql_);
            RsetTools.setLong(ps, 1, idPK);

            rs = ps.executeQuery();

            if (rs.next())
            {
                imageNameFile = RsetTools.getString(rs, "image_name_file");
                imageID = RsetTools.getLong(rs, "id_image_dir");
            }
        }
        catch (Exception e)
        {
            throw new PriceException(e.toString());
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    public PriceListItemExtend(DatabaseAdapter db_, ResultSet rs__, ShopPageParam shopParam_)
        throws PriceException
    {
        super.set(rs__);

        shopParam = shopParam_;
        initDescription(db_);
    }
*/
}