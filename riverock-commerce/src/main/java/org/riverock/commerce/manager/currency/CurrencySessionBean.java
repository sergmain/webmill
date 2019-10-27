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
package org.riverock.commerce.manager.currency;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.riverock.commerce.bean.Currency;
import org.riverock.commerce.bean.CurrencyCurs;

/**
 * @author Sergei Maslyukov
 *         Date: 31.08.2006
 *         Time: 20:42:17
 *         <p/>
 *         $Id$
 */
public class CurrencySessionBean implements Serializable {
    private static final long serialVersionUID = 5597005504L;

    private CurrencyExtendedBean currencyExtendedBean = null;
    private Long currentCurrencyId = null;
    private BigDecimal currentCurs =null;
    private Currency currency = null;

    public CurrencySessionBean() {
    }

    public Currency getCurrencyBean() {
        return currency;
    }

    public void setCurrencyBean(Currency currency) {
        this.currency = currency;
    }

    public CurrencyExtendedBean getCurrencyExtendedBean() {
        return currencyExtendedBean;
    }

    public void setCurrencyExtendedBean(CurrencyExtendedBean currencyExtendedBean) {
        this.currencyExtendedBean = currencyExtendedBean;
    }

    public BigDecimal getCurrentCurs() {
        return currentCurs;
    }

    public void setCurrentCurs(BigDecimal currentCurs) {
        this.currentCurs = currentCurs;
    }

    public Long getCurrentCurrencyId() {
        return currentCurrencyId;
    }

    public void setCurrentCurrencyId(Long currentCurrencyId) {
        this.currentCurrencyId = currentCurrencyId;
    }
}

