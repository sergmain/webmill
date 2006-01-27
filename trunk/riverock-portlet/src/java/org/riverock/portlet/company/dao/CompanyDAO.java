package org.riverock.portlet.company.dao;

import java.util.List;
import org.riverock.portlet.company.bean.CompanyBean;
import org.riverock.interfaces.sso.a3.AuthSession;

public interface CompanyDAO {

	public CompanyBean loadCompany( Long id, AuthSession authSession );
	public List<CompanyBean> getCompanyList( AuthSession authSession );
    public Long processAddCompany( CompanyBean companyBean, String userLogin, Long groupCompanyId, Long holdingId, AuthSession authSession );
    public void processSaveCompany( CompanyBean companyBean, AuthSession authSession );
    public void processDeleteCompany( CompanyBean companyBean, AuthSession authSession );
}