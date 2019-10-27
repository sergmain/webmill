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
package org.riverock.commerce.manager.shop;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import javax.faces.model.SelectItem;

import org.riverock.commerce.bean.Currency;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.jsf.FacesTools;
import org.riverock.interfaces.ContainerConstants;

/**
 * @author Sergei Maslyukov
 *         Date: 04.09.2006
 *         Time: 17:03:41
 *         <p/>
 *         $Id: PriceCurrency.java 950 2006-09-01 18:11:51Z serg_main $
 */
public class ShopService implements Serializable {
    private static final long serialVersionUID = 3815005515L;

    public ShopService() {
    }

    public List<SelectItem> getCurrencyList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        Long siteId = new Long( FacesTools.getPortletRequest().getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        List<Currency> beans = CommerceDaoFactory.getCurrencyDao().getCurrencyList(siteId);
        for (Currency currency : beans) {
            if (currency.getCurrencyId()==null) {
                throw new IllegalStateException("getDefaultCurrencyId is null, currency name: " + currency.getCurrencyName());
            }

            list.add(new SelectItem(currency.getCurrencyId(), currency.getCurrencyName()));
        }
        return list;
    }
}