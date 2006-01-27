package org.riverock.portlet.company.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author SergeMaslyukov
 *
 */
public class CompanyManagedBean implements Serializable {
    private static final long serialVersionUID = 2055005505L;

	private CompanySessionBean companySessionBean = null;
	private CompanyModuleUserBean companyModuleUser = null;

	public CompanyManagedBean() {
	}

	public CompanyModuleUserBean getCompanyModuleUser() {
		return companyModuleUser;
	}

	public void setCompanyModuleUser(CompanyModuleUserBean companyModuleUser) {
		this.companyModuleUser = companyModuleUser;
	}
	
	public CompanySessionBean getCompanySessionBean() {
		return companySessionBean;
	}

	public void setCompanySessionBean(CompanySessionBean companySessionBean) {
		this.companySessionBean = companySessionBean;
	}
	
	public List<CompanyBean> getCompanyList() {
		return companySessionBean.getCompanyDAO().getCompanyList( companyModuleUser.getAuthSession() );
	}

}
