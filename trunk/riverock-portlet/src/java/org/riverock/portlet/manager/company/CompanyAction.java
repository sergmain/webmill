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
