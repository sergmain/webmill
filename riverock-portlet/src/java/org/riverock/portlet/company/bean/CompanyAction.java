package org.riverock.portlet.company.bean;

import java.io.Serializable;

import org.riverock.portlet.tools.FacesTools;
import org.riverock.interfaces.portal.bean.Company;

/**
 * @author SergeMaslyukov
 *
 */
public class CompanyAction implements Serializable {
    private static final long serialVersionUID = 2055005501L;

	private CompanySessionBean sessionBean = null;
	private CompanyModuleUserBean companyModuleUser = null;

	public CompanyAction() {
	}

	public CompanyModuleUserBean getCompanyModuleUser() {
		return companyModuleUser;
	}

	public void setCompanyModuleUser(CompanyModuleUserBean companyModuleUser) {
		this.companyModuleUser = companyModuleUser;
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
			companyModuleUser.getUserLogin(),
			companyModuleUser.getGroupCompanyId(),
			companyModuleUser.getHoldingId()
            );
		sessionBean.setCurrentCompanyId( companyId );
		return "company";
	}

	public String cancelAddCompany() {
		loadCurrentCompany();
		return "company";
	}

	public String processEditCompany() {
		FacesTools.getPortalDaoProvider().getPortalCompanyDao().processSaveCompany(
			sessionBean.getCompany() );
		
		return "company";
	}

	public String cancelEditCompany() {
		loadCurrentCompany();
		return "company";
	}

	public String processDeleteCompany() {
		FacesTools.getPortalDaoProvider().getPortalCompanyDao().processDeleteCompany(
			sessionBean.getCompany() );
		sessionBean.setCompany( null );
		return "company";
	}
	
	public String selectCompany() {
		loadCurrentCompany();
		return "company";
	}

	private void loadCurrentCompany() {
		Company bean = FacesTools.getPortalDaoProvider().getPortalCompanyDao().loadCompany(
			sessionBean.getCurrentCompanyId() );
		sessionBean.setCompany( bean );
	}
}
