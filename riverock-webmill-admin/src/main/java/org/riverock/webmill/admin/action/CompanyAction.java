/*
 * org.riverock.webmill.admin - Webmill portal admin web application
 *
 * For more information about Webmill portal, please visit project site
 * http://webmill.riverock.org
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community,
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
package org.riverock.webmill.admin.action;

import java.io.Serializable;

import org.riverock.webmill.admin.CompanySessionBean;
import org.riverock.webmill.admin.dao.DaoFactory;
import org.riverock.webmill.admin.bean.CompanyBean;

/**
 * @author SergeMaslyukov
 *         Date: 13.07.2006
 *         Time: 21:55:44
 *         $Id: PortalUserSessionBean.java 753 2006-07-10 07:53:57Z serg_main $
 */
public class CompanyAction implements Serializable {
    private static final long serialVersionUID = 2055005501L;

    private CompanySessionBean companySessionBean = null;

    public CompanyAction() {
    }

    public CompanySessionBean getCompanySessionBean() {
        return companySessionBean;
    }

    public void setCompanySessionBean(CompanySessionBean companySessionBean) {
        this.companySessionBean = companySessionBean;
    }

    public String addCompany() {
        companySessionBean.setCompany( new CompanyBean() );

        return "company-add";
    }

    public String processAddCompany() {
        Long companyId = DaoFactory.getWebmillAdminDao().processAddCompany( companySessionBean.getCompany() );
        companySessionBean.setCurrentCompanyId( companyId );
        loadCurrentCompany();

        return "company";
    }

    public String cancelAddCompany() {
        loadCurrentCompany();
        return "company";
    }

    public String processEditCompany() {
        DaoFactory.getWebmillAdminDao().processSaveCompany(companySessionBean.getCompany() );

        return "company";
    }

    public String cancelEditCompany() {
        loadCurrentCompany();
        return "company";
    }

    public String processDeleteCompany() {
        DaoFactory.getWebmillAdminDao().processDeleteCompany(companySessionBean.getCompany());
        companySessionBean.setCompany( null );
        return "company";
    }

    public String selectCompany() {
        loadCurrentCompany();
        return "company";
    }

    private void loadCurrentCompany() {
        CompanyBean bean = DaoFactory.getWebmillAdminDao().getCompany( companySessionBean.getCurrentCompanyId() );
        companySessionBean.setCompany( bean );
    }
}
