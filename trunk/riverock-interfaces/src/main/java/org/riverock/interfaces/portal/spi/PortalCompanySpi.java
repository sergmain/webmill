package org.riverock.interfaces.portal.spi;

import java.util.List;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.dao.PortalCompanyDao;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:38:50
 * $Id$
 */
public interface PortalCompanySpi extends PortalCompanyDao {
    public Company getCompany( Long id );
    public List<Company> getCompanyList();
    public Long processAddCompany( Company companyBean, String userLogin, Long holdingId );
    public void processSaveCompany( Company companyBean );
    public void processDeleteCompany( Company companyBean );
}
