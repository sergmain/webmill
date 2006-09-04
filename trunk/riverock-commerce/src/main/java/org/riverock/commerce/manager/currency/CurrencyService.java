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
package org.riverock.commerce.manager.currency;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import javax.faces.model.SelectItem;

import org.riverock.commerce.manager.std_currency.StandardCurrencyBean;
import org.riverock.commerce.dao.CommerceDaoFactory;

/**
 * @author Sergei Maslyukov
 *         Date: 31.08.2006
 *         Time: 20:42:05
 *         <p/>
 *         $Id$
 */
public class CurrencyService implements Serializable {
    private static final long serialVersionUID = 5595005515L;

    public CurrencyService() {
    }

    public List<SelectItem> getStandardCurrencyList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<StandardCurrencyBean> beans = CommerceDaoFactory.getStandardCurrencyDao().getStandardCurrencyList();
        for (StandardCurrencyBean standardCurrencyBean : beans) {
            if (standardCurrencyBean.getStandardCurrencyId()==null) {
                throw new IllegalStateException("standardCurrencyId is null, currency name: " + standardCurrencyBean.getStandardCurrencyName());
            }

            list.add(new SelectItem(standardCurrencyBean.getStandardCurrencyId(), standardCurrencyBean.getStandardCurrencyName()));
        }
        return list;
    }
}