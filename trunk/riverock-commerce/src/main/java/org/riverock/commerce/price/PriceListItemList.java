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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import org.riverock.common.tools.NumberTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.common.tools.XmlTools;
import org.riverock.commerce.bean.price.CustomCurrencyItemType;
import org.riverock.commerce.shop.bean.ShopOrder;
import org.riverock.commerce.tools.SiteUtils;
import org.riverock.commerce.bean.ShopBean;
import org.riverock.commerce.bean.CurrencyPrecisionBean;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.schema.shop.HiddenParamType;
import org.riverock.commerce.schema.shop.ItemListType;
import org.riverock.commerce.schema.shop.PriceFieldNameType;
import org.riverock.commerce.schema.shop.PriceItemType;
import org.riverock.webmill.container.ContainerConstants;

/**
 *
 * $Author$
 *
 * $Id$
 *
 */
@SuppressWarnings({"UnusedAssignment"})
public final class PriceListItemList {
    private final static Logger log = Logger.getLogger( PriceListItemList.class );

    // dont edit return type - name must be with package
    private static HiddenParamType getHidden(String name, String value)
    {
        HiddenParamType hidden = new HiddenParamType();
        hidden.setHiddenParamName(name);
        hidden.setHiddenParamValue(value);
        return hidden;
    }

    private final static Object syncObj  = new Object();
    public static ItemListType getInstance(
        DatabaseAdapter db_, ShopPageParam shopParam,
        RenderRequest renderRequest, RenderResponse renderResponse, ResourceBundle bundle )
        throws PriceException {

        if (shopParam.id_currency==null) {
            throw new PriceException("currency for this shop not defined");
        }

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
            Long siteId = new Long( renderRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );

            fieldName.setNameToInvoice(
                bundle.getString("price.to_invoice")
            );
            sql_ =
                "select a.* " +
                "from   WM_PRICE_LIST a " +
                "where  a.ID_MAIN=? and a.ID_SHOP=? and a.ABSOLETE=0 and a.IS_GROUP=0 ";

            if ("item".equals(shopParam.sortBy)) {
                sql_ += ("order by a.ITEM " + (shopParam.sortDirect == 0 ? "ASC" : "DESC"));
            }
            else if ("price".equals(shopParam.sortBy)) {
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

            PortletSession session = renderRequest.getPortletSession();
            ShopOrder order = (ShopOrder) session.getAttribute(ShopPortlet.ORDER_SESSION, PortletSession.APPLICATION_SCOPE);
            ShopBean shop = CommerceDaoFactory.getShopDao().getShop(shopParam.id_shop);
            while (rs.next()) {
                Long idPk = RsetTools.getLong(rs, "ID_ITEM");

                PriceItemType item = new PriceItemType();
                item.setItemID(RsetTools.getLong(rs, "ID"));
                item.setItemName(RsetTools.getString(rs, "ITEM"));

                item.setItemToInvoice(
                    shopParam.isProcessInvoice ?
                    renderResponse.createRenderURL().toString() :
                    null
                );

                item.setItemToInvoiceCountParam(
                    shopParam.isProcessInvoice ?
                    ShopPortlet.NAME_COUNT_ADD_ITEM_SHOP :
                    null
                );

                item.setItemInBasket(OrderLogic.isItemInBasket(idPk, order) ? "InBasket" : null);

                String currencyCode = RsetTools.getString(rs, "CURRENCY");
                if (log.isDebugEnabled())
                    log.debug("currencyCode "+ currencyCode);

                final CurrencyManager currencyManager = CurrencyManager.getInstance( siteId );
                CurrencyItem currencyItem =
                    (CurrencyItem) CurrencyService.getCurrencyItemByCode(
                        currencyManager.getCurrencyList(), currencyCode
                    );

                if (log.isDebugEnabled())
                    log.debug("currencyItem "+currencyItem);

                double resultPrice = 0;
                if (currencyItem!=null) {

                    currencyItem.fillRealCurrencyData(
                        currencyManager.getCurrencyList().getStandardCurrencyList()
                    );

                    double rsetPrice = RsetTools.getDouble(rs, "PRICE", 0.0);

                    if (log.isDebugEnabled())
                        log.debug("currencyItem.getCurrencyId()" + currencyItem.getCurrencyId() + ", shopParam.id_currency " + shopParam.id_currency);
                    int precisionValue = 0;
                    if (currencyItem.getCurrencyId().equals(shopParam.id_currency) || shopParam.id_currency == 0) {
                        item.setItemCurrencyID(currencyItem.getCurrencyId());
                        item.setItemNameCurrency(currencyItem.getCurrencyName());

                        precisionValue = getPrecisionValue(shop, currencyItem.getCurrencyId());
                        resultPrice = NumberTools.truncate(rsetPrice, precisionValue);
                    } else {
                        precisionValue = getPrecisionValue(shop, shopParam.id_currency);

                        CustomCurrencyItemType targetCurrency =
                            CurrencyService.getCurrencyItem(currencyManager.getCurrencyList(), shopParam.id_currency);

                        if (log.isDebugEnabled()) {
                            log.debug("targetCurrency " + targetCurrency);
                            log.debug("item " + item);
                        }
                        item.setItemCurrencyID(targetCurrency.getCurrencyId());
                        item.setItemNameCurrency(targetCurrency.getCurrencyName());

                        if (log.isDebugEnabled()) {
                            synchronized (syncObj) {
                                try {
                                    XmlTools.writeToFile(targetCurrency, SiteUtils.getTempDir() + File.separatorChar + "schema-currency-default.xml");
                                }
                                catch (Throwable e) {
                                    log.error("Error write targetCurrency to debug file", e);
                                }

                                log.debug("default curs - " + targetCurrency.getRealCurs());
                            }
                        }

                        double crossCurs =
                            currencyItem.getRealCurs() / targetCurrency.getRealCurs();

                        resultPrice =
                            NumberTools.truncate(rsetPrice, precisionValue) *
                                crossCurs;

                        if (log.isDebugEnabled()) {
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
                }
                else {
                    item.setItemPrice("unknown");
                }
                
                item.setItemDescription(null);

                PriceItemImage image = PriceItemImage.getInstance(shopParam.idSite);
                if (image != null) {
                    item.setItemImageFileName(image.getItemImage(idPk));
                }

                if (shopParam.isProcessInvoice) {
                    item.getHiddenParam().add(getHidden(ShopPortlet.NAME_ID_SHOP_PARAM, "" + shopParam.id_shop));
                    item.getHiddenParam().add(getHidden(ShopPortlet.NAME_ID_GROUP_SHOP, "" + RsetTools.getLong(rs, "ID_MAIN")));
                    item.getHiddenParam().add(getHidden(ShopPortlet.NAME_ID_CURRENCY_SHOP, "" + shopParam.id_currency));
                    item.getHiddenParam().add(getHidden(ShopPortlet.NAME_ADD_ID_ITEM, "" + idPk));
                    item.getHiddenParam().add(getHidden(ContainerConstants.NAME_TYPE_CONTEXT_PARAM, ShopPortlet.CTX_TYPE_SHOP));
                    item.getHiddenParam().add(getHidden(ShopPortlet.NAME_SHOP_SORT_BY, shopParam.sortBy));
                    item.getHiddenParam().add(getHidden(ShopPortlet.NAME_SHOP_SORT_DIRECT, "" + shopParam.sortDirect));
                }

                if (log.isDebugEnabled()) {
                    synchronized(syncObj) {
                        try {
                            XmlTools.writeToFile(item, SiteUtils.getTempDir()+File.separatorChar+"schema-currency-item.xml");
                        }
                        catch(Throwable e) {
                            log.error("Exception write item to debug file", e);
                        }
                    }
                }
                items.getPriceItem().add(item);
            }
        }
        catch (SQLException e) {
            String es = "Error  execute sql ";
            log.error(es, e);
            throw new PriceException(es+e.getMessage());
        }
        catch (UnsupportedEncodingException e) {
            String es = "Error get localized string ";
            log.error(es, e);
            throw new PriceException(es, e);
        }
        catch (Exception e) {
            String es = "Error ";
            log.error(es, e);
            throw new PriceException(es, e);
        }
        finally {
            DatabaseManager.close( rs, ps );
            rs = null;
            ps = null;
        }

        if (items.getPriceItem().isEmpty())
            return null;

        return items;
    }

    private static int getPrecisionValue( ShopBean shopBean, Long idCurrency ) {
        CurrencyPrecisionBean prec = shopBean.getPrecisionList().getCurrencyPrecision( idCurrency );
        if (prec==null)
            return 2;

        return prec.getPrecision()!=null ?prec.getPrecision() :0;
    }

}