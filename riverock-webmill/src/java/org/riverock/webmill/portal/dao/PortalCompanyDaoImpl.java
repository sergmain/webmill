package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.dao.PortalCompanyDao;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:59:09
 *         $Id$
 */
public class PortalCompanyDaoImpl implements PortalCompanyDao {
    private AuthSession authSession = null;

    PortalCompanyDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

    public Company getCompany(Long id) {
        return InternalDaoFactory.getInternalCompanyDao().getCompany( id, authSession );
    }

    public List<Company> getCompanyList() {
        return InternalDaoFactory.getInternalCompanyDao().getCompanyList( authSession );
    }

    public Long processAddCompany(Company companyBean, String userLogin, Long holdingId) {
        return InternalDaoFactory.getInternalCompanyDao().processAddCompany( companyBean, userLogin, holdingId, authSession );
    }

    public void processSaveCompany(Company companyBean) {
        InternalDaoFactory.getInternalCompanyDao().processSaveCompany( companyBean, authSession );
    }

    public void processDeleteCompany(Company companyBean) {
        InternalDaoFactory.getInternalCompanyDao().processDeleteCompany( companyBean, authSession );
    }
}
