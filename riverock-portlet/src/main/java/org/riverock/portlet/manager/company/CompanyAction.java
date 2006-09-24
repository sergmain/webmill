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
package org.riverock.portlet.manager.company;

import java.io.Serializable;

import org.riverock.portlet.tools.FacesTools;
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
		FacesTools.getPortalDaoProvider().getPortalCompanyDao().processSaveCompany(sessionBean.getCompany() );
		
		return "company";
	}

	public String cancelEditCompany() {
		loadCurrentCompany();
		return "company";
	}

	public String processDeleteCompany() {
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
			sessionBean.getCurrentCompanyId() );
		sessionBean.setCompany( bean );
	}
}
