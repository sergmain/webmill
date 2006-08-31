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

import org.apache.log4j.Logger;

import org.riverock.commerce.dao.CommerceDaoFactory;

/**
 * @author Sergei Maslyukov
 *         Date: 31.08.2006
 *         Time: 20:41:15
 *         <p/>
 *         $Id$
 */
public class CurrencyAction implements Serializable {
    private final static Logger log = Logger.getLogger( CurrencyAction.class );

    private static final long serialVersionUID = 2057005501L;

    private CurrencySessionBean currencySessionBean = null;

    public CurrencyAction() {
    }

    public CurrencySessionBean getCurrencySessionBean() {
        return currencySessionBean;
    }

    public void setCurrencySessionBean(CurrencySessionBean currencySessionBean) {
        this.currencySessionBean = currencySessionBean;
    }

    // Add currency

    public String addCurrency() {
        log.debug("Start addCurrency()");

        currencySessionBean.setCurrencyBean( new CurrencyBean() );

        return "currency-add";
    }

    public String processAddCurrency() {
        Long id = CommerceDaoFactory.getCurrencyDao().createCurrency( currencySessionBean.getCurrencyBean() );
        currencySessionBean.setCurrentCurrencyId( id );
        loadCurrentCurrency();
        return "currency";
    }

    public String cancelAddCurrency() {
        setSessionBean(null);
        return "currency";
    }

    // Add currency curs

    public String addCurs() {
        log.debug("Start addCurs()");

        currencySessionBean.setCurrentCurs( null );

        return "currency-curs-add";
    }

    public String processAddCurs() {
        CommerceDaoFactory.getCurrencyDao().addCurrencyCurs(
            currencySessionBean.getCurrentCurrencyId(), currencySessionBean.getCurrentCurs()
        );
        loadCurrentCurrency();
        return "currency";
    }

    public String cancelAddCurs() {
        currencySessionBean.setCurrentCurs( null );
        return "currency";
    }

    // Edit currency

    public String processEditCurrency() {
        CommerceDaoFactory.getCurrencyDao().updateCurrency( currencySessionBean.getCurrencyBean() );
        return "currency";
    }

    public String cancelEditCurrency() {
        loadCurrentCurrency();
        return "currency";
    }

    public String processDeleteCurrency() {
        CommerceDaoFactory.getCurrencyDao().deleteCurrency( currencySessionBean.getCurrentCurrencyId() );
        setSessionBean(null);
        return "currency";
    }

    public String selectCurrency() {
        loadCurrentCurrency();
        return "currency";
    }

    private void loadCurrentCurrency() {
        CurrencyBean bean = CommerceDaoFactory.getCurrencyDao().getCurrency( currencySessionBean.getCurrentCurrencyId() );
        setSessionBean( new CurrencyBean(bean) );
    }

    private void setSessionBean(CurrencyBean bean) {
        currencySessionBean.setCurrencyBean(bean);
    }
}
