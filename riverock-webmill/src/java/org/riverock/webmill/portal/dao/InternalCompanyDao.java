package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.Holding;
import org.riverock.interfaces.portal.bean.GroupCompany;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:24:19
 *         $Id$
 */
public interface InternalCompanyDao {
    
    public Company loadCompany( Long id, AuthSession authSession );
    public Long processAddCompany( Company companyBean, String userLogin, Long groupCompanyId, Long holdingId, AuthSession authSession );
    public void processSaveCompany( Company companyBean, AuthSession authSession );
    public void processDeleteCompany( Company companyBean, AuthSession authSession );

    public List<Company> getCompanyList( AuthSession authSession );
    public List<GroupCompany> getGroupCompanyList(AuthSession authSession);
    public List<Holding> getHoldingList(AuthSession authSession);
}
