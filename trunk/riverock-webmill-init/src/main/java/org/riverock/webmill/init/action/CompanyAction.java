/*
 * org.riverock.webmill.init - Webmill portal initializer web application
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
package org.riverock.webmill.init.action;

import java.io.Serializable;

import org.riverock.webmill.init.CompanySessionBean;
import org.riverock.webmill.init.dao.DaoFactory;
import org.riverock.webmill.init.bean.CompanyBean;

/**
 * @author SergeMaslyukov
 *         Date: 13.07.2006
 *         Time: 21:55:44
 *         $Id: PortalUserSessionBean.java 753 2006-07-10 07:53:57Z serg_main $
 */
public class CompanyAction implements Serializable {
    private static final long serialVersionUID = 2055005501L;

    private CompanySessionBean sessionBean = null;

    public CompanyAction() {
    }

    public CompanySessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(CompanySessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public String addCompany() {
        sessionBean.setCompany( new CompanyBean() );

        return "company-add";
    }

    public String processAddCompany() {
        Long companyId = DaoFactory.getWebmillInitDao().processAddCompany( sessionBean.getCompany() );
        sessionBean.setCurrentCompanyId( companyId );
        loadCurrentCompany();

        return "company";
    }

    public String cancelAddCompany() {
        loadCurrentCompany();
        return "company";
    }

    public String processEditCompany() {
        DaoFactory.getWebmillInitDao().processSaveCompany(sessionBean.getCompany() );

        return "company";
    }

    public String cancelEditCompany() {
        loadCurrentCompany();
        return "company";
    }

    public String processDeleteCompany() {
        DaoFactory.getWebmillInitDao().processDeleteCompany(sessionBean.getCompany());
        sessionBean.setCompany( null );
        return "company";
    }

    public String selectCompany() {
        loadCurrentCompany();
        return "company";
    }

    private void loadCurrentCompany() {
        CompanyBean bean = DaoFactory.getWebmillInitDao().getCompany( sessionBean.getCurrentCompanyId() );
        sessionBean.setCompany( bean );
    }
}
