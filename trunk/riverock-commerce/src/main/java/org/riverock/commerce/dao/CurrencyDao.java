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

import java.math.BigDecimal;
import java.util.List;

import org.riverock.commerce.manager.currency.CurrencyBean;

/**
 * @author Sergei Maslyukov
 *         Date: 31.08.2006
 *         Time: 21:53:06
 *         <p/>
 *         $Id$
 */
public interface CurrencyDao {

    void addCurrencyCurs(Long currencyId, BigDecimal curs);

    void updateCurrency(CurrencyBean currencyBean);

    void deleteCurrency(Long currencyId);

    CurrencyBean getCurrency(Long currencyId);

    Long createCurrency(CurrencyBean currencyBean);

    List<CurrencyBean> getCurrencyList(Long siteId);
}
