/**
 * 
 */
package org.riverock.portlet.company.bean;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import org.riverock.portlet.company.dao.CompanyDAO;

import org.apache.log4j.Logger;

/**
 * @author SergeMaslyukov
 *
 */
public class CompanySessionBean implements Serializable {
    private static final long serialVersionUID = 2055005504L;
    private static final Logger log = Logger.getLogger( CompanySessionBean.class );

	private CompanyBean company = null;
	private Long currentCompanyId = null;
	private CompanyDAO companyDAO = null;

	public CompanySessionBean() {
	}

	public CompanyBean getCompany() {
		return company;
	}

	public void setCompany(CompanyBean company) {
		this.company = company;
	}

	public Long getCurrentCompanyId() {
		return currentCompanyId;
	}

	public void setCurrentCompanyId(Long currentCompanyId) {
		this.currentCompanyId = currentCompanyId;
	}

	public CompanyDAO getCompanyDAO() {
		return companyDAO;
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}
}
