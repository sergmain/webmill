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

/**
 * @author Sergei Maslyukov
 *         Date: 29.08.2006
 *         Time: 20:45:31
 */
public class CommerceDaoFactory {
    private final static StandardCurrencyDao STANDARD_CURRENCY_DAO = new StandardCurrencyDaoImpl();
    private final static CurrencyDao CURRENCY_DAO = new CurrencyDaoImpl();
    private final static CommonCurrencyDao COMMON_CURRENCY_DAO = new CommonCurrencyDaoImpl();
    private final static ShopDao SHOP_DAO = new ShopDaoImpl();
    private final static CurrencyPrecisionDao CURRENCY_PRECISION_DAO = new CurrencyPrecisionDaoImpl();

    public static StandardCurrencyDao getStandardCurrencyDao() {
        return STANDARD_CURRENCY_DAO;
    }

    public static CurrencyDao getCurrencyDao() {
        return CURRENCY_DAO;
    }

    public static CommonCurrencyDao getCommonCurrencyDao() {
        return COMMON_CURRENCY_DAO;
    }

    public static ShopDao getShopDao() {
        return SHOP_DAO;
    }

    public static CurrencyPrecisionDao getCurrencyPrecisionDao() {
        return CURRENCY_PRECISION_DAO;
    }
}
