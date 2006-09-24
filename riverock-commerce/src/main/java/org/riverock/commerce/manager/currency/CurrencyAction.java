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
package org.riverock.commerce.manager.currency;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.manager.std_currency.StandardCurrencyBean;
import org.riverock.commerce.bean.CurrencyCurrentCurs;
import org.riverock.commerce.jsf.FacesTools;
import org.riverock.webmill.container.ContainerConstants;

/**
 * @author Sergei Maslyukov
 *         Date: 31.08.2006
 *         Time: 20:41:15
 *         <p/>
 *         $Id$
 */
public class CurrencyAction implements Serializable {
    private final static Logger log = Logger.getLogger( CurrencyAction.class );

    private static final long serialVersionUID = 55957005571L;

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

        CurrencyBean currencyBean = new CurrencyBean();
        currencyBean.setUsed(true);
        Long siteId = new Long( FacesTools.getPortletRequest().getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        currencyBean.setSiteId(siteId);
        currencySessionBean.setCurrencyBean( currencyBean );

        return "currency-add";
    }

    public String processAddCurrency() {
        Long id = CommerceDaoFactory.getCurrencyDao().createCurrency( currencySessionBean.getCurrencyBean() );
        currencySessionBean.setCurrentCurrencyId( id );
        loadCurrentCurrency();
        return "currency";
    }

    public String cancelAddCurrency() {
        currencySessionBean.setCurrencyBean( null );
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

    public String editCurrency() {
        log.debug("Start editCurrency()");

        CurrencyBean currencyBean = CommerceDaoFactory.getCurrencyDao().getCurrency( currencySessionBean.getCurrentCurrencyId() );
        if (currencyBean==null) {
            return "currency";
        }

        currencySessionBean.setCurrencyBean( currencyBean );
        return "currency-edit";
    }

    public String processEditCurrency() {
        CommerceDaoFactory.getCurrencyDao().updateCurrency( currencySessionBean.getCurrencyBean() );
        loadCurrentCurrency();
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
        if (bean==null) {
            setSessionBean( new CurrencyExtendedBean() );
            return;
        }
        Long siteId = new Long( FacesTools.getPortletRequest().getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        CurrencyCurrentCurs currentCurs = CommerceDaoFactory.getCommonCurrencyDao().getCurrentCurs(bean.getCurrencyId(), siteId);

        StandardCurrencyBean standardCurrencyBean=null;
        if (bean.isUseStandard() && bean.getStandardCurrencyId()!=null) {
            standardCurrencyBean=CommerceDaoFactory.getStandardCurrencyDao().getStandardCurrency(bean.getStandardCurrencyId());
        }

        CurrencyCurrentCurs currentStandardCurs=null;
        if (standardCurrencyBean!=null) {
            currentStandardCurs=CommerceDaoFactory.getCommonCurrencyDao().getStandardCurrencyCurs(standardCurrencyBean.getStandardCurrencyId());
        }

        BigDecimal realCurs;
        if (bean.isUseStandard()) {
            realCurs=(currentStandardCurs!=null?currentStandardCurs.getCurs():null);
        }
        else {
            realCurs=(currentCurs!=null?currentCurs.getCurs():null);
        }
/*
        if (realCurs==null) {
            realCurs=new BigDecimal(1);
        }
*/

        setSessionBean( new CurrencyExtendedBean(bean,standardCurrencyBean, realCurs, currentCurs, currentStandardCurs ) );
    }

    private void setSessionBean(CurrencyExtendedBean currencyExtendedBean) {
        currencySessionBean.setCurrencyExtendedBean(currencyExtendedBean);
    }
}
