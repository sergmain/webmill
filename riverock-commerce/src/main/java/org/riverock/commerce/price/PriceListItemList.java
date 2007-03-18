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
import java.math.BigDecimal;
import java.util.List;
import java.util.ResourceBundle;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import org.riverock.commerce.bean.CurrencyPrecision;
import org.riverock.commerce.bean.Shop;
import org.riverock.commerce.bean.ShopItem;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.schema.shop.HiddenParamType;
import org.riverock.commerce.schema.shop.ItemListType;
import org.riverock.commerce.schema.shop.PriceFieldNameType;
import org.riverock.commerce.schema.shop.PriceItemType;
import org.riverock.commerce.tools.SiteUtils;
import org.riverock.common.tools.NumberTools;
import org.riverock.common.tools.XmlTools;
import org.riverock.webmill.container.ContainerConstants;

/**
 * $Author$
 * <p/>
 * $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public final class PriceListItemList {
    private final static Logger log = Logger.getLogger(PriceListItemList.class);

    private static HiddenParamType getHidden(String name, String value) {
        HiddenParamType hidden = new HiddenParamType();
        hidden.setHiddenParamName(name);
        hidden.setHiddenParamValue(value);
        return hidden;
    }

    private final static Object syncObj = new Object();

    public static ItemListType getInstance(
        ShopPageParam shopParam, RenderRequest renderRequest, RenderResponse renderResponse, ResourceBundle bundle)
        throws PriceException {

        if (shopParam.id_currency == null) {
            throw new PriceException("currency for this shop not defined");
        }

        ItemListType items = new ItemListType();
        PriceFieldNameType fieldName = new PriceFieldNameType();
        fieldName.setNameItem("Наименование");
        fieldName.setNamePrice("Цена");
        fieldName.setNameCurrency("Валюта");
        fieldName.setNameToInvoice(bundle.getString("price.to_invoice"));

        items.setPriceFieldName(fieldName);
        Long siteId = new Long(renderRequest.getPortalContext().getProperty(ContainerConstants.PORTAL_PROP_SITE_ID));
        Shop shop = CommerceDaoFactory.getShopDao().getShop(shopParam.id_shop, siteId);
        if (shop == null) {
            return items;
        }

        List<ShopItem> shopItems = CommerceDaoFactory.getShopDao().getShopItemList(shop.getShopId(), shopParam.id_group, shopParam.sortBy, shopParam.sortDirect);
        if (log.isDebugEnabled()) {
            log.debug("shopParam.id_group " + shopParam.id_group + ", " +
                "shopParam.id_shop " + shopParam.id_shop
            );
        }
        CurrencyPrecisionList currencyPrecisionList = new CurrencyPrecisionList(shop.getShopId());
        final CurrencyManager currencyManager = CurrencyManager.getInstance(siteId);
        for (ShopItem shopItem : shopItems) {

            PriceItemType item = new PriceItemType();
            item.setItemID(shopItem.getItemId());
            item.setItemName(shopItem.getItem());
            item.setItemToInvoice(
                shopParam.isProcessInvoice ?renderResponse.createRenderURL().toString() :null
            );
            item.setItemToInvoiceCountParam(
                shopParam.isProcessInvoice ?ShopPortlet.NAME_COUNT_ADD_ITEM_SHOP :null
            );

            item.setItemInBasket(shopItem.isItemInBasket() ? "InBasket" : null);

            String currencyCode = shopItem.getCurrency();
            if (log.isDebugEnabled()) {
                log.debug("currencyCode " + currencyCode);
            }
            CurrencyItem currencyItem = CurrencyService.getCurrencyItemByCode(
                currencyManager.getCurrencyList(), currencyCode
            );
            if (log.isDebugEnabled()) {
                log.debug("currencyItem " + currencyItem);
            }
            BigDecimal resultPrice = BigDecimal.ZERO;
            if (currencyItem != null) {

                currencyItem.fillRealCurrencyData(currencyManager.getCurrencyList().getStandardCurrencies());

                BigDecimal rsetPrice = shopItem.getPrice();

                if (log.isDebugEnabled()) {
                    log.debug("currencyItem.getCurrencyId()" + currencyItem.getCurrencyId() + ", " +
                        "shopParam.id_currency " + shopParam.id_currency);
                }
                int precisionValue = 0;
                if (currencyItem.getCurrencyId().equals(shopParam.id_currency) || shopParam.id_currency == 0) {
                    item.setItemCurrencyID(currencyItem.getCurrencyId());
                    item.setItemNameCurrency(currencyItem.getCurrencyName());

                    precisionValue = getPrecisionValue(currencyPrecisionList, currencyItem.getCurrencyId());
                    resultPrice = NumberTools.truncate(rsetPrice, precisionValue);
                }
                else {
                    precisionValue = getPrecisionValue(currencyPrecisionList, shopParam.id_currency);

                    CurrencyItem targetCurrency =
                        CurrencyService.getCurrencyItem(currencyManager.getCurrencyList(), shopParam.id_currency);

                    if (log.isDebugEnabled()) {
                        log.debug("item " + item);
                        log.debug("targetCurrency " + targetCurrency);
                        if (targetCurrency!=null) {
                            log.debug("default curs - " + targetCurrency.getRealCurs());
                        }
                    }
                    item.setItemCurrencyID(targetCurrency.getCurrencyId());
                    item.setItemNameCurrency(targetCurrency.getCurrencyName());

                    BigDecimal crossCurs = currencyItem.getRealCurs().divide(targetCurrency.getRealCurs());
                    resultPrice = NumberTools.truncate(rsetPrice, precisionValue).multiply(crossCurs);

                    if (log.isDebugEnabled()) {
                        log.debug("crossCurs - " + crossCurs + " price - " +
                            NumberTools.truncate(rsetPrice, precisionValue) +
                            " result price - " + resultPrice);
                    }
                }
                item.setItemPrice(NumberTools.truncate(resultPrice, precisionValue).toString());
            }
            else {
                item.setItemPrice("unknown");
            }

            item.setItemDescription(null);

/*
            PriceItemImage image = PriceItemImage.getInstance(shopParam.idSite);
            if (image != null) {
                item.setItemImageFileName(image.getItemImage(shopItem.getShopItemId()));
            }
*/

            if (shopParam.isProcessInvoice) {
                item.getHiddenParam().add(getHidden(ShopPortlet.NAME_ID_SHOP_PARAM, "" + shopParam.id_shop));
                item.getHiddenParam().add(getHidden(ShopPortlet.NAME_ID_GROUP_SHOP, "" + shopItem.getParentItemId()));
                item.getHiddenParam().add(getHidden(ShopPortlet.NAME_ID_CURRENCY_SHOP, "" + shopParam.id_currency));
                item.getHiddenParam().add(getHidden(ShopPortlet.NAME_ADD_ID_ITEM, "" + shopItem.getShopItemId()));
                item.getHiddenParam().add(getHidden(ContainerConstants.NAME_TYPE_CONTEXT_PARAM, ShopPortlet.CTX_TYPE_SHOP));
                item.getHiddenParam().add(getHidden(ShopPortlet.NAME_SHOP_SORT_BY, shopParam.sortBy));
                item.getHiddenParam().add(getHidden(ShopPortlet.NAME_SHOP_SORT_DIRECT, "" + shopParam.sortDirect));
            }
            items.getPriceItem().add(item);
        }

        if (items.getPriceItem().isEmpty())
            return null;

        return items;
    }

    private static int getPrecisionValue(CurrencyPrecisionList currencyPrecisionList, Long idCurrency) {
        CurrencyPrecision prec = currencyPrecisionList.getCurrencyPrecision(idCurrency);
        if (prec == null) {
            return 2;
        }

        return prec.getPrecision() != null ? prec.getPrecision() : 0;
    }

}