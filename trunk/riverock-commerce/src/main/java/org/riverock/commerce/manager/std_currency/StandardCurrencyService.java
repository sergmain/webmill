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
package org.riverock.commerce.manager.std_currency;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.riverock.commerce.dao.CommerceDaoFactory;

/**
 * @author Sergei Maslyukov
 *         Date: 29.08.2006
 *         Time: 20:41:33
 */
public class StandardCurrencyService implements Serializable {
    private static final long serialVersionUID = 7765005515L;

    public StandardCurrencyService() {
    }

    public List<StandardCurrencyBean> getStandardCurrencyList() {
        List<StandardCurrencyBean> list = CommerceDaoFactory.getStandardCurrencyDao().getStandardCurrencyList();
        if (list==null) {
            return new ArrayList<StandardCurrencyBean>();
        }
        return list;
    }
}