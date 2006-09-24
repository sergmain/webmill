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

import org.riverock.commerce.bean.CurrencyPrecisionBean;

import java.util.List;

/**
 * User: SergeMaslyukov
 * Date: 06.09.2006
 * Time: 0:14:34
 * <p/>
 * $Id: PriceCurrency.java 950 2006-09-01 18:11:51Z serg_main $
 */
public interface CurrencyPrecisionDao {
    CurrencyPrecisionBean getCurrencyPrecision(Long currencyPrecisionId);

    List<CurrencyPrecisionBean> getCurrencyPrecisionList(Long shopId);

    void updateCurrencyPrecision(Long currencyPrecisionId, Integer currencyPrecision);
}
