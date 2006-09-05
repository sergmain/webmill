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
