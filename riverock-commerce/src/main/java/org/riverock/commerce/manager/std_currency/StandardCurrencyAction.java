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
package org.riverock.commerce.manager.std_currency;

import java.io.Serializable;

import org.riverock.commerce.dao.CommerceDaoFactory;

/**
 * @author Sergei Maslyukov
 *         Date: 29.08.2006
 *         Time: 20:41:33
 */
public class StandardCurrencyAction implements Serializable {

    private static final long serialVersionUID = 2057005501L;

    private StandardCurrencySessionBean standardCurrencySessionBean = null;

    public StandardCurrencyAction() {
    }

    public StandardCurrencySessionBean getStandardCurrencySessionBean() {
        return standardCurrencySessionBean;
    }

    public void setStandardCurrencySessionBean(StandardCurrencySessionBean standardCurrencySessionBean) {
        this.standardCurrencySessionBean = standardCurrencySessionBean;
    }

    public String addStandardCurrency() {
        standardCurrencySessionBean.setStandardCurrencyBean( new StandardCurrencyBean() );

        return "standard-currency-add";
    }

    public String processAddStandardCurrency() {
        Long id = CommerceDaoFactory.getCommerceDao().createStandardCurrency( standardCurrencySessionBean.getStandardCurrencyBean() );
        standardCurrencySessionBean.setCurrentstandardCurrencyId( id );
        loadCurrentStandardCurrency();
        return "standard-currency";
    }

    public String cancelAddStandardCurrency() {
        loadCurrentStandardCurrency();
        return "standard-currency";
    }

    public String processEditStandardCurrency() {
        CommerceDaoFactory.getCommerceDao().updateStandardCurrency( standardCurrencySessionBean.getStandardCurrencyBean() );
        return "standard-currency";
    }

    public String cancelEditStandardCurrency() {
        loadCurrentStandardCurrency();
        return "standard-currency";
    }

    public String processDeleteStandardCurrency() {
        CommerceDaoFactory.getCommerceDao().deleteStandardCurrency( standardCurrencySessionBean.getCurrentstandardCurrencyId() );
        standardCurrencySessionBean.setStandardCurrencyBean( null );
        return "standard-currency";
    }

    public String selectStandardCurrency() {
        loadCurrentStandardCurrency();
        return "standard-currency";
    }

    private void loadCurrentStandardCurrency() {
        StandardCurrencyBean bean = CommerceDaoFactory.getCommerceDao().getStandardCurrency( standardCurrencySessionBean.getCurrentstandardCurrencyId() );
        standardCurrencySessionBean.setStandardCurrencyBean( new StandardCurrencyBean(bean) );
    }
}
