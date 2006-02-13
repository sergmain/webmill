/**
 * 
 */
package org.riverock.portlet.company.bean;

import java.io.Serializable;
import org.riverock.interfaces.portal.bean.Company;

/**
 * @author SergeMaslyukov
 *
 */
public class CompanySessionBean implements Serializable {
    private static final long serialVersionUID = 2055005504L;

	private Company company = null;
	private Long currentCompanyId = null;

	public CompanySessionBean() {
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Long getCurrentCompanyId() {
		return currentCompanyId;
	}

	public void setCurrentCompanyId(Long currentCompanyId) {
		this.currentCompanyId = currentCompanyId;
	}
}
