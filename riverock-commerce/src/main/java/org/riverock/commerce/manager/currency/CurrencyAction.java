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

import org.apache.log4j.Logger;

import org.riverock.commerce.dao.CommerceDaoFactory;
import org.riverock.commerce.bean.CurrencyCurrentCurs;
import org.riverock.commerce.bean.StandardCurrency;
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

        Currency currency = new Currency();
        currency.setUsed(true);
        Long siteId = new Long( FacesTools.getPortletRequest().getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        currency.setSiteId(siteId);
        currencySessionBean.setCurrencyBean(currency);

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

        Currency currency = CommerceDaoFactory.getCurrencyDao().getCurrency( currencySessionBean.getCurrentCurrencyId() );
        if (currency ==null) {
            return "currency";
        }

        currencySessionBean.setCurrencyBean(currency);
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
        Currency bean = CommerceDaoFactory.getCurrencyDao().getCurrency( currencySessionBean.getCurrentCurrencyId() );
        if (bean==null) {
            setSessionBean( new CurrencyExtendedBean() );
            return;
        }
        Long siteId = new Long( FacesTools.getPortletRequest().getPortalContext().getProperty( ContainerConstants.PORTAL_PROP_SITE_ID ) );
        CurrencyCurrentCurs currentCurs = CommerceDaoFactory.getCommonCurrencyDao().getCurrentCurs(bean.getCurrencyId(), siteId);

        StandardCurrency standardCurrencyBean=null;
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
