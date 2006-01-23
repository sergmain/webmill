package org.riverock.portlet.company.dao;

import java.util.List;
import org.riverock.portlet.company.bean.CompanyBean;

public interface CompanyDAO {

	public CompanyBean loadCompany(Long id, String userLogin);
	public List<CompanyBean> getCompanyList( String userLogin );
    public Long processAddCompany( CompanyBean companyBean, String userLogin, Long groupCompanyId, Long holdingId );
    public void processSaveCompany( CompanyBean companyBean, String userLogin );
    public void processDeleteCompany( CompanyBean companyBean, String userLogin );
}