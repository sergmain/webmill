package org.riverock.interfaces.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.Company;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:48:52
 *         $Id$
 */
public interface PortalCompanyDao {
    public Company loadCompany( Long id );
    public List<Company> getCompanyList();
    public Long processAddCompany( Company companyBean, String userLogin, Long holdingId );
    public void processSaveCompany( Company companyBean );
    public void processDeleteCompany( Company companyBean );
}
