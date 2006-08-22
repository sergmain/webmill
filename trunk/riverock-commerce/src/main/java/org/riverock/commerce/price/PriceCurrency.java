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

        CustomCurrencyType list = null;

        Long siteId = new Long( portletRequest.getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        list = CurrencyManager.getInstance(siteId ).getCurrencyList();

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