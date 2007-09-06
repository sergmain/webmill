/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.manager.holding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.Holding;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:26:35
 *         $Id$
 */
public class HoldingDataProvider implements Serializable {
    private static final long serialVersionUID = 2043005511L;

    private AuthSessionBean authSessionBean = null;
    private HoldingSessionBean holdingSessionBean= null;

    public HoldingDataProvider() {
    }

    // getter/setter methods
    public void setHoldingSessionBean( HoldingSessionBean holdingSessionBean) {
        this.holdingSessionBean = holdingSessionBean;
    }

    public HoldingSessionBean getHoldingSessionBean() {
        return holdingSessionBean;
    }

    public HoldingBean getCurrentHolding() {
        return holdingSessionBean.getHoldingBean();
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean( AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

    public List<SelectItem> getCompanyList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
//        List<Company> companies = authSessionBean.getAuthSession().getCompanyList();
        List<Company> companies = FacesTools.getPortalSpiProvider().getPortalCompanyDao().getCompanyList();
        for (Company companyBean : companies) {
            if (!isAlreadyBinded(companyBean)) {
                String companyName = companyBean.getName();
                if (StringUtils.isBlank(companyName)) {
                    companyName = "<empty company name>";
                }
                list.add(new SelectItem(companyBean.getId(), companyName));
            }
        }
        return list;
    }

    private boolean isAlreadyBinded( Company companyBean ) {
        for (CompanyBean company : holdingSessionBean.getHoldingBean().getCompanies()) {
            if (company.getId().equals(companyBean.getId())) {
                return true;
            }
        }
        return false;
    }

    private List<HoldingBean> holdingBeans = null;

    public List<HoldingBean> getHoldingBeans() {
        if( holdingBeans == null ) {
            holdingBeans = initHoldingBeans( authSessionBean.getAuthSession() );
        }
        return holdingBeans;
    }

    public void reinitHoldingBeans() {
        holdingBeans = initHoldingBeans( authSessionBean.getAuthSession() );
    }

    private List<HoldingBean> initHoldingBeans( AuthSession authSession ) {
        List<HoldingBean> list = new ArrayList<HoldingBean>();

        if (authSession==null) {
            return list;
        }

        List<Holding> holdings = FacesTools.getPortalSpiProvider().getPortalHoldingDao().getHoldingList();
        List<Company> companies = authSession.getCompanyList();
        for (Holding holding : holdings) {
            HoldingBean holdingBean = new HoldingBean(holding);
            for (Long companyId : holding.getCompanyIdList()) {
                for (Company company : companies) {
                    if (company.getId().equals(companyId)) {
                        CompanyBean bean = new CompanyBean();
                        bean.setName(company.getName());
                        bean.setId(companyId);

                        holdingBean.addCompany(bean);
                    }
                }
            }
            list.add(holdingBean);
        }
        return list;
    }
}