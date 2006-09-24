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

import org.apache.log4j.Logger;

import org.riverock.common.tools.DateTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.portlet.schema.core.WmCashCurrencyItemType;
import org.riverock.portlet.schema.price.CurrencyCurrentCursType;
import org.riverock.portlet.schema.price.CustomCurrencyItemType;
import org.riverock.portlet.schema.price.StandardCurrencyItemType;
import org.riverock.portlet.schema.price.StandardCurrencyType;

/**
 * User: Admin
 * Date: Dec 12, 2002
 * Time: 9:01:05 PM
 * <p/>
 * $Id$
 */
public class CurrencyItem extends CustomCurrencyItemType {
    private static Logger log = Logger.getLogger(CurrencyItem.class);

    public void fillRealCurrencyData(StandardCurrencyType stdCurrency) throws Exception {
        if (!Boolean.TRUE.equals(this.getIsUseStandardCurrency()) && this.getCurrentCurs() == null) {
            this.setRealCurs(0.0);
            this.setRealDateChange(DateTools.getCurrentTime());
            this.setIsRealInit(Boolean.FALSE);
            return;
        }

        if (Boolean.TRUE.equals(this.getIsUseStandardCurrency()) &&
            (stdCurrency == null || stdCurrency.getStandardCurrencyListCount() == 0))
            throw new Exception("Curs for currency " + this.getCurrencyName() +
                " can not calculated. Curs for standard currency not entered");

        CurrencyCurrentCursType curs = null;
        if (Boolean.TRUE.equals(this.getIsUseStandardCurrency())) {
            for (int k = 0; k < stdCurrency.getStandardCurrencyListCount(); k++) {
                StandardCurrencyItemType stdItem = stdCurrency.getStandardCurrencyList(k);
                if (stdItem.getIdStandardCurrency().equals(this.getIdStandardCurrency())) {
                    curs = stdItem.getCurrentCurs();
                    break;
                }
            }
            if (curs == null)
                throw new Exception("Error get curs for standard currency. Local currency - " + this.getCurrencyName());
        } else
            curs = this.getCurrentCurs();

        this.setRealCurs(curs.getCurs());
        this.setRealDateChange(curs.getDateChange());
        this.setIsRealInit(Boolean.TRUE);
    }

    public CurrencyItem() {
    }

    public CurrencyItem(DatabaseAdapter db_, WmCashCurrencyItemType item) {
        this.setCurrencyCode(item.getCurrency());
        this.setCurrencyName(item.getNameCurrency());
        this.setIdCurrency(item.getIdCurrency());
        this.setIdSite(item.getIdSite());
        this.setIdStandardCurrency(item.getIdStandartCurs());
        this.setIsUsed(item.getIsUsed());
        this.setIsUseStandardCurrency(item.getIsUseStandart());
        this.setPercent(item.getPercentValue());
        this.setCurrentCurs(CurrencyService.getCurrentCurs(db_, this.getIdCurrency(), this.getIdSite()));
        if (this.getCurrentCurs() == null) {
            log.warn("Current curs is null");
        }
    }
}
