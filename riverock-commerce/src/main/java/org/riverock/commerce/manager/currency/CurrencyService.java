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