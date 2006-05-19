package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:24:19
 *         $Id$
 */
public interface InternalCompanyDao {
    
    public Company getCompany( String companyName );
    public Company getCompany( Long id, AuthSession authSession );

    public Long processAddCompany( Company companyBean, Long holdingId );
    public Long processAddCompany( Company companyBean, String userLogin, Long holdingId, AuthSession authSession );

    public void processSaveCompany( Company companyBean, AuthSession authSession );
    public void processDeleteCompany( Company companyBean, AuthSession authSession );

    public List<Company> getCompanyList( AuthSession authSession );

}
