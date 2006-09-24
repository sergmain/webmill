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
package org.riverock.commerce.manager.currency_precision;

import org.apache.log4j.Logger;
import org.riverock.commerce.bean.CurrencyPrecisionBean;
import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.manager.currency.CurrencyBean;

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
        CurrencyPrecisionBean bean = CommerceDaoFactory.getCurrencyPrecisionDao().getCurrencyPrecision(
            currencyPrecisionSessionBean.getCurrentCurrencyPrecisionId() 
        );
        if (log.isDebugEnabled()) {
            log.debug("CurrencyPrecisionBean; " + bean);
        }
        if (bean==null) {
            currencyPrecisionSessionBean.setCurrencyPrecisionBean(null);
            return;
        }

        CurrencyBean currencyBean = CommerceDaoFactory.getCurrencyDao().getCurrency(bean.getCurrencyId());
        if (currencyBean==null) {
            log.debug("CurrencyBean is null");
            currencyPrecisionSessionBean.setCurrencyPrecisionBean(null);
            return;
        }

        CurrencyPrecisionExtendedBean currencyPrecisionExtendedBean = new CurrencyPrecisionExtendedBean(bean, currencyBean);
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

