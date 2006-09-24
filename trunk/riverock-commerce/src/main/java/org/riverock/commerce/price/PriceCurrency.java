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

import javax.portlet.PortletRequest;

import org.riverock.portlet.schema.portlet.shop.CurrencyItemType;
import org.riverock.portlet.schema.portlet.shop.CurrencyListType;
import org.riverock.portlet.schema.portlet.shop.HiddenParamType;
import org.riverock.portlet.schema.price.CustomCurrencyItemType;
import org.riverock.portlet.schema.price.CustomCurrencyType;
import org.riverock.webmill.container.ContainerConstants;

/**
 *
 * $Id$
 *
 */
public class PriceCurrency {

    private static HiddenParamType getHidden(String name, String value)
    {
        HiddenParamType hidden = new HiddenParamType();
        hidden.setHiddenParamName(name);
        hidden.setHiddenParamValue(value);
        return hidden;
    }

    public static CurrencyListType getCurrencyList (
        ShopPageParam shopParam_, String switchCurrencyUrl_,
        PortletRequest portletRequest, ResourceBundle resourceBundle )
        throws PriceException {

        CurrencyListType currency = new CurrencyListType();

        try {
            currency.setNoCurrencyName( resourceBundle.getString( "price.wo-recalc" ) );
        }
        catch (java.util.MissingResourceException e) {
            currency.setNoCurrencyName( "error get string, price.wo-recalc" );
        }

        try {
            currency.setCurrencyNameSwitch( resourceBundle.getString( "price.recalculate" ) );
        }
        catch (java.util.MissingResourceException e) {
            currency.setNoCurrencyName( "error get string, price.recalculate" );
        }


        
        currency.setCurrencySwitchUrl(switchCurrencyUrl_);

        currency.setCurrencySelectParam(ShopPortlet.NAME_ID_CURRENCY_SHOP);

        currency.addHiddenParam( getHidden(ContainerConstants.NAME_LANG_PARAM, portletRequest.getLocale().toString()) );
        currency.addHiddenParam( getHidden(ShopPortlet.NAME_ID_GROUP_SHOP, "" + shopParam_.id_group));
        currency.addHiddenParam( getHidden(ShopPortlet.NAME_ID_SHOP_PARAM, "" + shopParam_.id_shop));
        currency.addHiddenParam( getHidden(ContainerConstants.NAME_TYPE_CONTEXT_PARAM, ShopPortlet.CTX_TYPE_SHOP));
        currency.addHiddenParam( getHidden(ShopPortlet.NAME_SHOP_SORT_BY, shopParam_.sortBy));
        currency.addHiddenParam( getHidden(ShopPortlet.NAME_SHOP_SORT_DIRECT, "" + shopParam_.sortDirect));

        Long siteId = new Long( portletRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        CustomCurrencyType list = CurrencyManager.getInstance(siteId).getCurrencyList();

        for (int i = 0; i < list.getCurrencyListCount(); i++) {
            CustomCurrencyItemType item = list.getCurrencyList(i);

            if (Boolean.TRUE.equals(item.getIsUsed()) ) {
                CurrencyItemType ic = new CurrencyItemType();

                ic.setCurrencyCurs(item.getRealCurs());
                ic.setCurrencyID(item.getIdCurrency());
                ic.setCurrencyName(item.getCurrencyName());

                if (ic.getCurrencyID() == shopParam_.id_currency)
                    ic.setSelectCurrentCurrency(" SELECTED ");

                currency.addCurrencyItem(ic);
            }
        }

        return currency;
    }
}