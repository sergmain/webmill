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
package org.riverock.commerce.dao;

import java.util.List;
import java.math.BigDecimal;

import org.riverock.commerce.manager.std_currency.StandardCurrencyBean;
import org.riverock.commerce.manager.currency.CurrencyBean;

/**
 * @author Sergei Maslyukov
 *         Date: 29.08.2006
 *         Time: 20:45:20
 */
public interface StandardCurrencyDao {
    List<StandardCurrencyBean> getStandardCurrencyList();

    Long createStandardCurrency(StandardCurrencyBean standardCurrencyBean);

    void updateStandardCurrency(StandardCurrencyBean standardCurrencyBean);

    void deleteStandardCurrency(Long standardCurrencyId);

    StandardCurrencyBean getStandardCurrency(Long standardCurrencyId);

    void addStandardCurrencyCurs(Long standardCurrencyId, BigDecimal currentCurs);
}