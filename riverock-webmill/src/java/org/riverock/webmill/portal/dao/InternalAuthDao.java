package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.UserInfo;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.AuthUserExtendedInfo;
import org.riverock.interfaces.sso.a3.bean.RoleBean;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 19:33:27
 *         $Id$
 */
public interface InternalAuthDao {
    public String getGrantedUserId(String userLogin);
    public String getGrantedCompanyId(String userLogin);

    public List<Long> getGrantedUserIdList(String userLogin);
    public List<Long> getGrantedCompanyIdList(String userLogin);
//    public String getGrantedGroupCompanyId(String userLogin);
//    public List<Long> getGrantedGroupCompanyIdList(String userLogin);

    public String getGrantedHoldingId(String userLogin);
    public List<Long> getGrantedHoldingIdList(String userLogin);

    public Long checkCompanyId(Long companyId, String userLogin);
    public Long checkHoldingId(Long holdingId, String userLogin);

    public boolean checkRigthOnUser( Long id_auth_user_check, Long id_auth_user_owner );

    public boolean isUserInRole( String userLogin, String userPassword, final String role_ );

    public RoleBean getRole( AuthSession authSession , Long roleId);
    public List<RoleBean> getUserRoleList( AuthSession authSession );

    public AuthInfo getAuthInfo(String userLogin, String userPassword);
    public AuthInfo getAuthInfo(Long authUserId);
    public List<AuthInfo> getAuthInfoList(AuthSession authSession);

    public boolean checkAccess( String userLogin, String userPassword, final String serverName ) ;

    public UserInfo getUserInfo(String userLogin);

    public List<RoleBean> getRoleList( AuthSession authSession );
    public List<RoleBean> getRoleList( AuthSession authSession, Long authUserId );

    public Long addRole( AuthSession authSession, RoleBean roleBean);
    public void updateRole( AuthSession authSession, RoleBean roleBean );
    public void deleteRole( AuthSession authSession, RoleBean roleBean );

    public Long addUser(AuthSession authSession, AuthUserExtendedInfo infoAuth);
    public void updateUser(AuthSession authSession, AuthUserExtendedInfo infoAuth);
    public void deleteUser(AuthSession authSession, AuthUserExtendedInfo infoAuth);
    public List<UserInfo> getUserList(AuthSession authSession);

}
