package org.riverock.portlet.manager.company;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.portlet.main.AuthSessionBean;

/**
 * @author SergeMaslyukov
 *
 */
public class CompanyService implements Serializable {
    private static final long serialVersionUID = 2055005515L;

	public CompanyService() {
	}

	public List<Company> getCompanyList() {
		List<Company> list = FacesTools.getPortalDaoProvider().getPortalCompanyDao().getCompanyList();
		if (list==null) {
			return null;
		}
		
		Iterator<Company> iterator = list.iterator();
		List<Company> companies = new ArrayList<Company>();
		while(iterator.hasNext()) {
			Company company = iterator.next();
			companies.add( new CompanyBean(company) );
		}
		return companies;
	}
}
