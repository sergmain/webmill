package org.riverock.portlet.company.bean;

import java.io.Serializable;

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
		Long companyId = sessionBean.getCompanyDAO().processAddCompany(
			sessionBean.getCompany(), 
			companyModuleUser.getUserLogin(),
			companyModuleUser.getGroupCompanyId(),
			companyModuleUser.getHoldingId(),
            companyModuleUser.getAuthSession() );
		sessionBean.setCurrentCompanyId( companyId );
		return "company";
	}

	public String cancelAddCompany() {
		loadCurrentCompany();
		return "company";
	}

	public String processEditCompany() {
		sessionBean.getCompanyDAO().processSaveCompany(
			sessionBean.getCompany(), companyModuleUser.getAuthSession() );
		
		return "company";
	}

	public String cancelEditCompany() {
		loadCurrentCompany();
		return "company";
	}

	public String processDeleteCompany() {
		sessionBean.getCompanyDAO().processDeleteCompany(
			sessionBean.getCompany(), companyModuleUser.getAuthSession() );
		sessionBean.setCompany( null );
		return "company";
	}
	
	public String selectCompany() {
		loadCurrentCompany();
		return "company";
	}

	private void loadCurrentCompany() {
		CompanyBean bean = sessionBean.getCompanyDAO().loadCompany(
			sessionBean.getCurrentCompanyId(), companyModuleUser.getAuthSession() );
		sessionBean.setCompany( bean );
	}
}
