package org.riverock.sso.dao;

import java.util.List;

import org.riverock.interfaces.sso.a3.AuthInfo;

/**
 * @author SergeMaslyukov
 *         Date: 24.01.2006
 *         Time: 10:08:08
 *         $Id$
 */
public interface AuthDao {
    public String getGrantedUserId(String userLogin);
    public List<Long> getGrantedUserIdList(String userLogin);
    public List<Long> getGrantedCompanyIdList(String userLogin);
    public String getGrantedGroupCompanyId(String userLogin);
    public List<Long> getGrantedGroupCompanyIdList(String userLogin);
    public String getGrantedHoldingId(String userLogin);
    public List<Long> getGrantedHoldingIdList(String userLogin);

    public Long checkCompanyId(Long companyId, String userLogin);
    public Long checkGroupCompanyId(Long groupCompanyId, String userLogin);
    public Long checkHoldingId(Long holdingId, String userLogin);

    public boolean checkRigthOnUser( Long id_auth_user_check, Long id_auth_user_owner );

    public boolean isUserInRole( String userLogin, String userPassword, final String role_ );

    public AuthInfo getAuthInfo(String userLogin, String userPassword);
    public AuthInfo getAuthInfo(Long authUserId);

    public boolean checkAccess( String userLogin, String userPassword, final String serverName ) ;
}
