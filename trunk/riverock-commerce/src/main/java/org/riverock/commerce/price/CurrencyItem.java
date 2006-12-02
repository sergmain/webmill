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

import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.common.tools.DateTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.commerce.bean.price.*;
import org.riverock.commerce.bean.StandardCurrency;

/**
 * User: Admin
 * Date: Dec 12, 2002
 * Time: 9:01:05 PM
 * <p/>
 * $Id$
 */
public class CurrencyItem extends CustomCurrencyItemType {
    private static Logger log = Logger.getLogger(CurrencyItem.class);

    private CurrencyCurrentCursType currencyCurrentCurs;

    public CurrencyCurrentCursType getCurrencyCurrentCurs() {
        return currencyCurrentCurs;
    }

    public void setCurrencyCurrentCurs(CurrencyCurrentCursType currencyCurrentCurs) {
        this.currencyCurrentCurs = currencyCurrentCurs;
    }

    public void fillRealCurrencyData(List<StandardCurrency> stdCurrency) throws Exception {
        if (!this.isUseStandardCurrency() && this.getCurrencyCurrentCurs()==null) {
            this.setRealCurs(0.0);
            this.setRealDateChange(DateTools.getCurrentTime());
            this.setRealInit(false);
            return;
        }

        if (this.isUseStandardCurrency() && (stdCurrency == null || stdCurrency.isEmpty())) {
            throw new Exception("Curs for currency " + this.getCurrencyName() +
                " can not calculated. Curs for standard currency not entered");
        }

        CurrencyCurrentCursType curs = null;
        if (this.isUseStandardCurrency()) {
            for (StandardCurrency stdItem : stdCurrency) {
                if (stdItem.getStandardCurrencyId().equals(this.getStandardCurrencyId())) {
                    curs = stdItem.getCurrentCurs();
                    break;
                }
            }
            if (curs == null)
                throw new Exception("Error get curs for standard currency. Local currency - " + this.getCurrencyName());
        } else
            curs = this.getCurrencyCurrentCurs();

        this.setRealCurs(curs.getCurs()!=null?curs.getCurs():null);
        this.setRealDateChange(curs.getDateChange());
        this.setRealInit(true);
    }

    public CurrencyItem() {
    }

    public CurrencyItem(DatabaseAdapter db_, WmCashCurrencyItemType item) {
        this.setCurrencyCode(item.getCurrency());
        this.setCurrencyName(item.getCurrencyName());
        this.setCurrencyId(item.getCurrencyId());
        this.setSiteId(item.getSiteId());
        this.setStandardCurrencyId(item.getIdStandartCurs());
        this.setUsed(item.isUsed());
        this.setUseStandardCurrency(item.isUseStandart());
        this.setPercent(item.getPercentValue());
        this.setCurrencyCurrentCurs(CurrencyService.getCurrentCurs(db_, this.getCurrencyId(), this.getSiteId()));
        if (this.getCurrencyCurrentCurs() == null) {
            log.warn("Current curs is null");
        }
    }
}
