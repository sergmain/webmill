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
package org.riverock.commerce.manager.std_currency;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.commerce.dao.CommerceDaoFactory;

/**
 * @author Sergei Maslyukov
 *         Date: 29.08.2006
 *         Time: 20:41:33
 */
public class StandardCurrencyAction implements Serializable {
    private final static Logger log = Logger.getLogger( StandardCurrencyAction.class );

    private static final long serialVersionUID = 7767005319L;

    private StandardCurrencySessionBean standardCurrencySessionBean = null;

    public StandardCurrencyAction() {
    }

    public StandardCurrencySessionBean getStandardCurrencySessionBean() {
        return standardCurrencySessionBean;
    }

    public void setStandardCurrencySessionBean(StandardCurrencySessionBean standardCurrencySessionBean) {
        this.standardCurrencySessionBean = standardCurrencySessionBean;
    }

    // Add standard currency

    public String addStandardCurrency() {
        log.debug("Start addStandardCurrency()");

        standardCurrencySessionBean.setStandardCurrencyBean( new StandardCurrencyBean() );

        return "standard-currency-add";
    }

    public String processAddStandardCurrency() {
        Long id = CommerceDaoFactory.getStandardCurrencyDao().createStandardCurrency( standardCurrencySessionBean.getStandardCurrencyBean() );
        standardCurrencySessionBean.setCurrentStandardCurrencyId( id );
        loadCurrentStandardCurrency();
        return "standard-currency";
    }

    public String cancelAddStandardCurrency() {
        setSessionBean(null);
        return "standard-currency";
    }

    // Add standard currency curs

    public String addCurs() {
        log.debug("Start addCurs()");

        standardCurrencySessionBean.setCurrentCurs( null );

        return "standard-currency-curs-add";
    }

    public String processAddCurs() {
        CommerceDaoFactory.getStandardCurrencyDao().addStandardCurrencyCurs(
            standardCurrencySessionBean.getCurrentStandardCurrencyId(), standardCurrencySessionBean.getCurrentCurs()
        );
        loadCurrentStandardCurrency();
        return "standard-currency";
    }

    public String cancelAddCurs() {
        standardCurrencySessionBean.setCurrentCurs( null );
        return "standard-currency";
    }

    // Edit standard currency

    public String processEditStandardCurrency() {
        CommerceDaoFactory.getStandardCurrencyDao().updateStandardCurrency( standardCurrencySessionBean.getStandardCurrencyBean() );
        return "standard-currency";
    }

    public String cancelEditStandardCurrency() {
        loadCurrentStandardCurrency();
        return "standard-currency";
    }

    public String processDeleteStandardCurrency() {
        CommerceDaoFactory.getStandardCurrencyDao().deleteStandardCurrency( standardCurrencySessionBean.getCurrentStandardCurrencyId() );
        setSessionBean(null);
        return "standard-currency";
    }

    public String selectStandardCurrency() {
        loadCurrentStandardCurrency();
        return "standard-currency";
    }

    private void loadCurrentStandardCurrency() {
        StandardCurrencyBean bean = CommerceDaoFactory.getStandardCurrencyDao().getStandardCurrency( standardCurrencySessionBean.getCurrentStandardCurrencyId() );
        setSessionBean( new StandardCurrencyBean(bean) );
    }

    private void setSessionBean(StandardCurrencyBean bean) {
        standardCurrencySessionBean.setStandardCurrencyBean(bean);
    }
}
