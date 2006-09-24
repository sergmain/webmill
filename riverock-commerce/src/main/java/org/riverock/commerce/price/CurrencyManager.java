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
package org.riverock.commerce.price;

import java.io.File;

import org.apache.log4j.Logger;

import org.riverock.sql.cache.SqlStatement;
import org.riverock.portlet.schema.price.CustomCurrencyType;
import org.riverock.commerce.tools.SiteUtils;
import org.riverock.generic.tools.XmlTools;
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
        CurrencyManager currency = new CurrencyManager();
        currency.currencyList = CurrencyList.getInstance(idSite).list;

        long mills = 0; // System.currentTimeMillis();

        if (log.isInfoEnabled())
            mills = System.currentTimeMillis();

        if (log.isDebugEnabled()) {
            synchronized (syncDebug) {
                try {
                    byte[] originByte = XmlTools.getXml(currency.currencyList, null);
                    MainTools.writeToFile(SiteUtils.getTempDir() + File.separatorChar + "debug-custom-currency-type.xml", originByte);
                }
                catch (Throwable e) {
                    log.error("error write debug information", e);
                }
            }
        }

        if (log.isInfoEnabled()) {
            log.info("init currency list for " + (System.currentTimeMillis() - mills) + " milliseconds");
        }
        return currency;
    }

    public void reinit() {
    }

    public void terminate(java.lang.Long id_) {
    }

}
