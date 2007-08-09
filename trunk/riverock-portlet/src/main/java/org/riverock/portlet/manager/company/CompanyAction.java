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
package org.riverock.portlet.manager.company;

import java.io.Serializable;

import org.riverock.portlet.tools.FacesTools;
import org.riverock.common.utils.PortletUtils;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.portlet.main.AuthSessionBean;

/**
 * @author SergeMaslyukov
 *
 */
public class CompanyAction implements Serializable {
    private static final long serialVersionUID = 2055005501L;

	private CompanySessionBean sessionBean = null;
	private AuthSessionBean authSessionBean = null;

    public static final String[] ROLES = new String[]{"webmill.portal-manager"};

	public CompanyAction() {
	}

	public AuthSessionBean getAuthSessionBean() {
		return authSessionBean;
	}

	public void setAuthSessionBean(AuthSessionBean authSessionBean) {
		this.authSessionBean = authSessionBean;
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

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

		Long companyId = FacesTools.getPortalDaoProvider().getPortalCompanyDao().processAddCompany(
			sessionBean.getCompany(),
			authSessionBean.getUserLogin(),
			authSessionBean.getHoldingId()
            );
		sessionBean.setCurrentCompanyId( companyId );
		loadCurrentCompany();

		return "company";
	}

	public String cancelAddCompany() {
		loadCurrentCompany();
		return "company";
	}

	public String processEditCompany() {
        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

		FacesTools.getPortalDaoProvider().getPortalCompanyDao().processSaveCompany(sessionBean.getCompany() );
		
		return "company";
	}

	public String cancelEditCompany() {
		loadCurrentCompany();
		return "company";
	}

	public String processDeleteCompany() {
        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

		FacesTools.getPortalDaoProvider().getPortalCompanyDao().processDeleteCompany(sessionBean.getCompany());
		sessionBean.setCompany( null );
		return "company";
	}
	
	public String selectCompany() {
		loadCurrentCompany();
		return "company";
	}

	private void loadCurrentCompany() {
		Company bean = FacesTools.getPortalDaoProvider().getPortalCompanyDao().getCompany(
			sessionBean.getCurrentCompanyId()
        );
		sessionBean.setCompany( bean );
	}
}
