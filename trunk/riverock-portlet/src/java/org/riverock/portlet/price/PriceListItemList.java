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



import java.io.UnsupportedEncodingException;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;



import javax.servlet.http.HttpSession;



import org.riverock.common.config.ConfigException;

import org.riverock.common.tools.NumberTools;

import org.riverock.common.tools.RsetTools;

import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.tools.XmlTools;

import org.riverock.portlet.main.Constants;

import org.riverock.portlet.portlets.ShopPageParam;

import org.riverock.portlet.portlets.model.OrderLogic;

import org.riverock.portlet.schema.portlet.shop.ItemListType;

import org.riverock.portlet.schema.portlet.shop.PriceFieldNameType;

import org.riverock.portlet.schema.portlet.shop.PriceItemType;

import org.riverock.portlet.schema.price.CurrencyPrecisionType;

import org.riverock.portlet.schema.price.CustomCurrencyItemType;

import org.riverock.portlet.schema.price.OrderType;

import org.riverock.webmill.config.WebmillConfig;

import org.riverock.webmill.portlet.CtxURL;

import org.riverock.webmill.portlet.PortletParameter;



import org.apache.log4j.Logger;



public class PriceListItemList

{

    private static Logger log = Logger.getLogger( PriceListItemList.class );



    private static org.riverock.portlet.schema.portlet.shop.HiddenParamType getHidden(String name, String value)

    {

        org.riverock.portlet.schema.portlet.shop.HiddenParamType hidden = new org.riverock.portlet.schema.portlet.shop.HiddenParamType();

        hidden.setHiddenParamName(name);

        hidden.setHiddenParamValue(value);

        return hidden;

    }



    private static Object syncObj  = new Object();

    public static ItemListType getInstance(DatabaseAdapter db_,

        ShopPageParam shopParam, PortletParameter param

        )

        throws PriceException

    {



        if (shopParam.id_currency==null)

            throw new PriceException("currency for this shop not defined");



        String sql_ = null;

        PreparedStatement ps = null;

        ResultSet rs = null;



        ItemListType items = new ItemListType();

        PriceFieldNameType fieldName = new PriceFieldNameType();

        fieldName.setNameItem("Наименование");

        fieldName.setNamePrice("Цена");

        fieldName.setNameCurrency("Валюта");



        items.setPriceFieldName(fieldName);

        try

        {

            fieldName.setNameToInvoice(

                shopParam.sm.getStr("price.to_invoice")

            );

            sql_ =

                "select a.* " +

                "from PRICE_LIST a " +

                "where a.ID_MAIN=? and a.ID_SHOP=? and a.ABSOLETE=0 and a.IS_GROUP=0 ";



            if ("item".equals(shopParam.sortBy))

            {

                sql_ += ("order by a.ITEM " + (shopParam.sortDirect == 0 ? "ASC" : "DESC"));

            }

            else if ("price".equals(shopParam.sortBy))

            {

                sql_ += ("order by a.PRICE " + (shopParam.sortDirect == 0 ? "ASC" : "DESC"));

            }



            if (log.isDebugEnabled())

                log.debug("sql "+sql_);



            ps = db_.prepareStatement(sql_);



            if (log.isDebugEnabled())

                log.debug("shopParam.id_group "+shopParam.id_group+", " +

                    "shopParam.id_shop "+shopParam.id_shop

                );



            RsetTools.setLong(ps, 1, shopParam.id_group);

            RsetTools.setLong(ps, 2, shopParam.id_shop);



            rs = ps.executeQuery();



            HttpSession session = param.getRequest().getSession();

            OrderType order = (OrderType) session.getAttribute(Constants.ORDER_SESSION);

            Shop shop = Shop.getInstance(db_, shopParam.id_shop);

            while (rs.next())

            {

                Long idPk = RsetTools.getLong(rs, "ID_ITEM");



                PriceItemType item = new PriceItemType();

                item.setItemID(RsetTools.getLong(rs, "ID"));

                item.setItemName(RsetTools.getString(rs, "ITEM"));



                item.setItemToInvoice(

                    shopParam.isProcessInvoice ?

                    param.getResponse().encodeURL(CtxURL.ctx()) :

                    null

                );



                item.setItemToInvoiceCountParam(

                    shopParam.isProcessInvoice ?

                    Constants.NAME_COUNT_ADD_ITEM_SHOP :

                    null

                );



                item.setItemInBasket(OrderLogic.isItemInBasket(idPk, order) ? "InBasket" : null);



                String currencyCode = RsetTools.getString(rs, "CURRENCY");

                if (log.isDebugEnabled())

                    log.debug("currencyCode "+ currencyCode);



                CurrencyItem currencyItem =

                    (CurrencyItem) CurrencyService.getCurrencyItemByCode(

                        CurrencyManager.getInstance(db_, param.getJspPage().p.sites.getIdSite()).getCurrencyList(), currencyCode

                    );



                if (log.isDebugEnabled())

                    log.debug("currencyItem "+currencyItem);



                currencyItem.fillRealCurrencyData(

                    CurrencyManager.getInstance(db_, param.getJspPage().p.sites.getIdSite()).getCurrencyList().getStandardCurrencyList()

                );



                double resultPrice = 0;

                double rsetPrice = RsetTools.getDouble(rs,"PRICE", new Double(0)).doubleValue();



                if (log.isDebugEnabled())

                    log.debug("currencyItem.getIdCurrency()"+currencyItem.getIdCurrency()+", shopParam.id_currency "+ shopParam.id_currency);

                int precisionValue = 0;

                if (currencyItem.getIdCurrency().equals(shopParam.id_currency) ||  shopParam.id_currency.longValue()==0 )

                {

                    item.setItemCurrencyID( currencyItem.getIdCurrency() );

                    item.setItemNameCurrency( currencyItem.getCurrencyName() );



                    precisionValue = getPrecisionValue( shop, currencyItem.getIdCurrency() );

                    resultPrice = NumberTools.truncate(rsetPrice, precisionValue);

                }

                else

                {

                    precisionValue = getPrecisionValue( shop, shopParam.id_currency );



                    CustomCurrencyItemType targetCurrency =

                        CurrencyService.getCurrencyItem(CurrencyManager.getInstance(db_, param.getJspPage().p.sites.getIdSite()).getCurrencyList(), shopParam.id_currency);



                    if (log.isDebugEnabled())

                    {

                        log.debug("targetCurrency "+targetCurrency);

                        log.debug("item "+item);

                    }

                    item.setItemCurrencyID( targetCurrency.getIdCurrency() );

                    item.setItemNameCurrency( targetCurrency.getCurrencyName() );



                    if (log.isDebugEnabled())

                    {

                        synchronized(syncObj)

                        {

                            try {

                                XmlTools.writeToFile(item, WebmillConfig.getWebmillDebugDir()+"schema-currency-item.xml");

                            }catch(Exception e)

                            {

                                log.error("Exception write item to file", e);

                            }

                            try {

                                XmlTools.writeToFile(targetCurrency, WebmillConfig.getWebmillDebugDir()+"schema-currency-default.xml");

                            }catch(Exception e)

                            {

                                log.error("Exception write targetCurrency to file", e);

                            }



                            log.debug("default curs - " + targetCurrency.getRealCurs());

                        }

                    }



                    double crossCurs =

                        currencyItem.getRealCurs().doubleValue() / targetCurrency.getRealCurs().doubleValue();



                    resultPrice =

                        NumberTools.truncate(rsetPrice, precisionValue) *

                        crossCurs;



                    if (log.isDebugEnabled())

                    {

                        log.debug("crossCurs - " + crossCurs + " price - " +

                            NumberTools.truncate(rsetPrice, precisionValue) +

                            " result price - " + resultPrice);

                    }

                }

                item.setItemPrice(

                    NumberTools.toString(

                        NumberTools.truncate(resultPrice, precisionValue),

                        precisionValue

                    )

                );



                item.setItemDescription(null);



                PriceItemImage image = PriceItemImage.getInstance(shopParam.idSite);

                if (image != null)

                    item.setItemImageFileName(image.getItemImage(idPk));



                if (shopParam.isProcessInvoice)

                {

                    item.addHiddenParam(getHidden(Constants.NAME_ID_SHOP_PARAM, "" + shopParam.id_shop));

                    item.addHiddenParam(getHidden(Constants.NAME_ID_GROUP_SHOP, "" + RsetTools.getLong(rs, "ID_MAIN")));

                    item.addHiddenParam(getHidden(Constants.NAME_ID_CURRENCY_SHOP, "" + shopParam.id_currency));

                    item.addHiddenParam(getHidden(Constants.NAME_ADD_ID_ITEM, "" + idPk));

                    item.addHiddenParam(getHidden(Constants.NAME_TYPE_CONTEXT_PARAM, Constants.CTX_TYPE_SHOP));

                    item.addHiddenParam(getHidden(Constants.NAME_TEMPLATE_CONTEXT_PARAM, shopParam.nameTemplate));

                    item.addHiddenParam(getHidden(Constants.NAME_SHOP_SORT_BY, shopParam.sortBy));

                    item.addHiddenParam(getHidden(Constants.NAME_SHOP_SORT_DIRECT, "" + shopParam.sortDirect));

                }



                items.addPriceItem(item);

            }

        }

        catch (SQLException e)

        {

            String es = "Error  execute sql ";

            log.error(es, e);

            throw new PriceException(es+e.getMessage());

        }

        catch (ConfigException e)

        {

            String es = "Error get configuration ";

            log.error(es, e);

            throw new PriceException(es+e.getMessage());

        }

        catch (UnsupportedEncodingException e)

        {

            String es = "Error get localized string ";

            log.error(es, e);

            throw new PriceException(es+e.getMessage());

        }

        catch (Exception e)

        {

            String es = "Error ";

            log.error(es, e);

            throw new PriceException(es+e.getMessage());

        }

        finally

        {

            DatabaseManager.close( rs, ps );

            rs = null;

            ps = null;

        }



        if (items.getPriceItemCount() == 0)

            return null;



        return items;

    }



    private static int getPrecisionValue( Shop shop, Long idCurrency )

    {

        CurrencyPrecisionType prec = shop.precisionList.getCurrencyPrecision( idCurrency );

        if (prec==null)

            return 2;



        return prec.getPrecision()!=null?prec.getPrecision().intValue():0;

    }



}