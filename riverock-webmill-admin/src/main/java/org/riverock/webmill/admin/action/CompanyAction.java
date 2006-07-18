/*
 * org.riverock.webmill.admin - Webmill portal admin web application
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 * Riverock - The Open-source Java Development Community,
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
        Long companyId = DaoFactory.getWebmillInitDao().processAddCompany( companySessionBean.getCompany() );
        companySessionBean.setCurrentCompanyId( companyId );
        loadCurrentCompany();

        return "company";
    }

    public String cancelAddCompany() {
        loadCurrentCompany();
        return "company";
    }

    public String processEditCompany() {
        DaoFactory.getWebmillInitDao().processSaveCompany(companySessionBean.getCompany() );

        return "company";
    }

    public String cancelEditCompany() {
        loadCurrentCompany();
        return "company";
    }

    public String processDeleteCompany() {
        DaoFactory.getWebmillInitDao().processDeleteCompany(companySessionBean.getCompany());
        companySessionBean.setCompany( null );
        return "company";
    }

    public String selectCompany() {
        loadCurrentCompany();
        return "company";
    }

    private void loadCurrentCompany() {
        CompanyBean bean = DaoFactory.getWebmillInitDao().getCompany( companySessionBean.getCurrentCompanyId() );
        companySessionBean.setCompany( bean );
    }
}
