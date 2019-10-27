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
package org.riverock.commerce.manager.currency_precision;

import org.apache.log4j.Logger;
import org.riverock.commerce.bean.CurrencyPrecision;
import org.riverock.commerce.bean.Currency;
import org.riverock.commerce.dao.CommerceDaoFactory;

import java.io.Serializable;

/**
 * User: SergeMaslyukov
 * Date: 06.09.2006
 * Time: 0:00:20
 * <p/>
 * $Id: PriceCurrency.java 950 2006-09-01 18:11:51Z serg_main $
 */
public class CurrencyPrecisionAction implements Serializable {
    private final static Logger log = Logger.getLogger( CurrencyPrecisionAction.class );

    private static final long serialVersionUID = 3817005501L;

    private CurrencyPrecisionSessionBean currencyPrecisionSessionBean = null;
    private CurrencyPrecisionDataProvider currencyPrecisionDataProvider = null;

    public CurrencyPrecisionAction() {
    }

    public CurrencyPrecisionDataProvider getCurrencyPrecisionDataProvider() {
        return currencyPrecisionDataProvider;
    }

    public void setCurrencyPrecisionDataProvider(CurrencyPrecisionDataProvider currencyPrecisionDataProvider) {
        this.currencyPrecisionDataProvider = currencyPrecisionDataProvider;
    }

    public CurrencyPrecisionSessionBean getCurrencyPrecisionSessionBean() {
        return currencyPrecisionSessionBean;
    }

    public void setCurrencyPrecisionSessionBean(CurrencyPrecisionSessionBean currencyPrecisionSessionBean) {
        this.currencyPrecisionSessionBean = currencyPrecisionSessionBean;
    }

    // Edit currencyPrecision

    public String editCurrencyPrecision() {
        log.debug("Start editCurrencyPrecision()");

        loadCurrentCurrencyPrecision();
        if (currencyPrecisionSessionBean.getCurrencyPrecisionBean()==null) {
            return "currency-precision";
        }

        return "currency-precision-edit";
    }

    public String processEditCurrencyPrecision() {
        CommerceDaoFactory.getCurrencyPrecisionDao().updateCurrencyPrecision(
            currencyPrecisionSessionBean.getCurrentCurrencyPrecisionId(), currencyPrecisionSessionBean.getCurrentCurrencyPrecision()
        );
        loadCurrentCurrencyPrecision();
        return "currency-precision";
    }

    public String cancelEditCurrencyPrecision() {
        loadCurrentCurrencyPrecision();
        return "currency-precision";
    }

    // common

    public String selectCurrencyPrecision() {
        loadCurrentCurrencyPrecision();
        return "currency-precision";
    }

    private void loadCurrentCurrencyPrecision() {
        CurrencyPrecision bean = CommerceDaoFactory.getCurrencyPrecisionDao().getCurrencyPrecision(
            currencyPrecisionSessionBean.getCurrentCurrencyPrecisionId() 
        );
        if (log.isDebugEnabled()) {
            log.debug("CurrencyPrecision; " + bean);
        }
        if (bean==null) {
            currencyPrecisionSessionBean.setCurrencyPrecisionBean(null);
            return;
        }

        Currency currency = CommerceDaoFactory.getCurrencyDao().getCurrency(bean.getCurrencyId());
        if (currency ==null) {
            log.debug("Currency is null");
            currencyPrecisionSessionBean.setCurrencyPrecisionBean(null);
            return;
        }

        CurrencyPrecisionExtendedBean currencyPrecisionExtendedBean = new CurrencyPrecisionExtendedBean(bean, currency);
        log.debug("Set currencyPrecisionSessionBean.setCurrencyPrecisionBean to "+currencyPrecisionExtendedBean);
        currencyPrecisionSessionBean.setCurrentCurrencyPrecision(bean.getPrecision());
        currencyPrecisionSessionBean.setCurrencyPrecisionBean(currencyPrecisionExtendedBean);
    }

    // Shop action

    public String selectShop() {
        if (log.isDebugEnabled()) {
            log.debug( "Select shop action." );
            log.debug( "articleSessionBean type: " +currencyPrecisionSessionBean.getObjectType() );
        }
        currencyPrecisionSessionBean.setShopExtendedBean( currencyPrecisionDataProvider.getShopExtendedBean() );
        return "currency-precision";
    }

    public String changeSite() {
        log.info( "Change shop action." );
        return "currency-precision";
    }

}

