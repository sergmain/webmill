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

import org.riverock.commerce.bean.CurrencyCurrentCurs;
import org.riverock.generic.db.DatabaseAdapter;

/**
 * @author Sergei Maslyukov
 *         Date: 01.09.2006
 *         Time: 19:29:37
 *         <p/>
 *         $Id$
 */
public interface CommonCurrencyDao {
    CurrencyCurrentCurs getCurrentCurs(Long currencyId, Long siteId);

    CurrencyCurrentCurs getCurrentCurs(DatabaseAdapter db, Long currencyId, Long siteId);

    CurrencyCurrentCurs getStandardCurrencyCurs(Long currencyId);

    CurrencyCurrentCurs getStandardCurrencyCurs( DatabaseAdapter db, Long standardCurrencyId );
}
