package org.riverock.interfaces.sso.a3;

import java.util.List;

import org.riverock.interfaces.sso.a3.bean.AuthParameterBean;
import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.Holding;

/**
 * @author SergeMaslyukov
 *         Date: 31.01.2006
 *         Time: 15:28:42
 *         $Id$
 */
public interface AuthProvider {
    public boolean isUserInRole( AuthSession authSession, String role_ );
    public boolean checkAccess( AuthSession authSession, String serverName );
    public void setParameters( List<List<AuthParameterBean>> params );

    public UserInfo getUserInfo( AuthSession authSession );

    public String getGrantedUserId( AuthSession authSession );
    public List<Long> getGrantedUserIdList( AuthSession authSession );

    public String getGrantedCompanyId(AuthSession authSession);
    public List<Long> getGrantedCompanyIdList( AuthSession authSession );

//    public String getGrantedGroupCompanyId( AuthSession authSession );
//    public List<Long> getGrantedGroupCompanyIdList( AuthSession authSession );

    public String getGrantedHoldingId( AuthSession authSession );
    public List<Long> getGrantedHoldingIdList( AuthSession authSession );

    public Long checkCompanyId(Long companyId, AuthSession authSession );
//    public Long checkGroupCompanyId(Long groupCompanyId, AuthSession authSession );
    public Long checkHoldingId(Long holdingId, AuthSession authSession );

    public boolean checkRigthOnUser( Long id_auth_user_check, Long id_auth_user_owner );

    public AuthInfo getAuthInfo(AuthSession authSession);
    public AuthInfo getAuthInfo(AuthSession authSession, Long authUserId);
    public List<AuthInfo> getAuthInfoList(AuthSession authSession);

    public RoleBean getRole(AuthSession authSession, Long roleId );

    // get list of all roles
    public List<RoleBean> getRoleList( AuthSession authSession );
    public List<RoleBean> getRoleList( AuthSession authSession, Long authUserId );

    // get list of granted roles for specific user
    public List<RoleBean> getUserRoleList( AuthSession authSession );

    public Long addRole( AuthSession authSession, RoleBean roleBean);
    public void updateRole( AuthSession authSession, RoleBean roleBean );
    public void deleteRole( AuthSession authSession, RoleBean roleBean );

    public Long addUser( AuthSession authSession, AuthUserExtendedInfo infoAuth );
    public void updateUser( AuthSession authSession, AuthUserExtendedInfo infoAuth );
    public void deleteUser( AuthSession authSession, AuthUserExtendedInfo infoAuth );

    public List<UserInfo> getUserList( AuthSession authSession );
    public List<Company> getCompanyList( AuthSession authSession );
//    public List<GroupCompany> getGroupCompanyList( AuthSession authSession );
    public List<Holding> getHoldingList( AuthSession authSession );
}

