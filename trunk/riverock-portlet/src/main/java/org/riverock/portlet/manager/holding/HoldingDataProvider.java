/*
 * org.riverock.portlet - Portlet Library
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
        List<Company> companies = FacesTools.getPortalDaoProvider().getPortalCompanyDao().getCompanyList();
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

        List<Holding> holdings = FacesTools.getPortalDaoProvider().getPortalHoldingDao().getHoldingList();
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