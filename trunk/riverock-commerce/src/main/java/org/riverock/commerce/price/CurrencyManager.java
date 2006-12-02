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
package org.riverock.commerce.price;

import java.io.File;

import org.apache.log4j.Logger;

import org.riverock.sql.cache.SqlStatement;
import org.riverock.commerce.bean.price.CustomCurrencyType;
import org.riverock.commerce.tools.SiteUtils;
import org.riverock.common.tools.XmlTools;
import org.riverock.common.tools.MainTools;

/**
 * User: serg_main
 * Date: 06.02.2004
 * Time: 17:07:00
 *
 * @author Serge Maslyukov
 *         $Id$
 */
public class CurrencyManager {
    private static Logger log = Logger.getLogger(CurrencyManager.class);

    static {
        SqlStatement.registerRelateClass(CurrencyManager.class, CurrencyList.class);
    }

    private CustomCurrencyType currencyList = null;

    public CustomCurrencyType getCurrencyList() {
        return currencyList;
    }

    public CurrencyManager() {
    }

    private final static Object syncDebug = new Object();

    public static CurrencyManager getInstance(Long idSite) throws PriceException {
        CurrencyManager currencyManager = new CurrencyManager();
        currencyManager.currencyList = CurrencyList.getInstance(idSite).list;

        long mills = 0; // System.currentTimeMillis();

        if (log.isInfoEnabled())
            mills = System.currentTimeMillis();

        if (log.isDebugEnabled()) {
            log.debug("CurrencyList: " + currencyManager.currencyList);
        }

        if (log.isInfoEnabled()) {
            log.info("init currency manager list for " + (System.currentTimeMillis() - mills) + " milliseconds");
        }
        return currencyManager;
    }

    public void reinit() {
    }

    public void terminate(java.lang.Long id_) {
    }

}
