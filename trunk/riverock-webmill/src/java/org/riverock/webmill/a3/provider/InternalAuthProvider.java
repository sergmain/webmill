package org.riverock.webmill.a3.provider;

import java.io.Serializable;
import java.util.List;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.GroupCompany;
import org.riverock.interfaces.portal.bean.Holding;
import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.AuthProvider;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.AuthUserExtendedInfo;
import org.riverock.interfaces.sso.a3.UserInfo;
import org.riverock.interfaces.sso.a3.bean.AuthParameterBean;
import org.riverock.interfaces.sso.a3.bean.RoleBean;
import org.riverock.webmill.portal.dao.InternalAuthDao;
import org.riverock.webmill.portal.dao.InternalCompanyDao;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * User: Admin
 * Date: Sep 6, 2003
 * Time: 10:33:28 PM
 *
 * $Id$
 */
public final class InternalAuthProvider implements AuthProvider, Serializable {
    private static final long serialVersionUID = 4055005503L;

    private InternalAuthDao authDao = InternalDaoFactory.getInternalAuthDao();
    private InternalCompanyDao companyDao = InternalDaoFactory.getInternalCompanyDao();

    public InternalAuthProvider() {
    }

    public boolean checkAccess( AuthSession authSession, final String serverName ) {
        return authDao.checkAccess( authSession.getUserLogin(), authSession.getUserPassword(), serverName );
    }

    public void setParameters(List<List<AuthParameterBean>> params) {
    }

    public boolean isUserInRole( AuthSession authSession, final String role_ ) {
        return authDao.isUserInRole(authSession.getUserLogin(), authSession.getUserPassword(), role_);
    }

    public UserInfo getUserInfo( AuthSession authSession ) {
        return authDao.getUserInfo( authSession.getUserLogin() );
    }

    public String getGrantedUserId( AuthSession authSession ) {
        return authDao.getGrantedUserId( authSession.getUserLogin() );
    }

    public List<Long> getGrantedUserIdList( AuthSession authSession ) {
        return authDao.getGrantedUserIdList( authSession.getUserLogin() );
    }

    public String getGrantedCompanyId( AuthSession authSession ) {
        return authDao.getGrantedCompanyId( authSession.getUserLogin() );
    }

    public List<Long> getGrantedCompanyIdList( AuthSession authSession ) {
        return authDao.getGrantedCompanyIdList( authSession.getUserLogin() );
    }

    public String getGrantedGroupCompanyId( AuthSession authSession ) {
        return authDao.getGrantedGroupCompanyId( authSession.getUserLogin() );
    }

    public List<Long> getGrantedGroupCompanyIdList( AuthSession authSession ) {
        return authDao.getGrantedGroupCompanyIdList( authSession.getUserLogin() );
    }

    public String getGrantedHoldingId( AuthSession authSession ) {
        return authDao.getGrantedHoldingId( authSession.getUserLogin() );
    }

    public List<Long> getGrantedHoldingIdList( AuthSession authSession ) {
        return authDao.getGrantedHoldingIdList( authSession.getUserLogin() );
    }

    public Long checkCompanyId(Long companyId , AuthSession authSession ) {
        return authDao.checkCompanyId( companyId, authSession.getUserLogin() );
    }

    public Long checkGroupCompanyId(Long groupCompanyId, AuthSession authSession ) {
        return authDao.checkGroupCompanyId( groupCompanyId, authSession.getUserLogin() );
    }

    public Long checkHoldingId(Long holdingId, AuthSession authSession ) {
        return authDao.checkHoldingId( holdingId, authSession.getUserLogin() );
    }

    public boolean checkRigthOnUser(Long id_auth_user_check, Long id_auth_user_owner) {
        return authDao.checkRigthOnUser( id_auth_user_check, id_auth_user_owner );
    }

    public AuthInfo getAuthInfo(AuthSession authSession) {
        return authDao.getAuthInfo( authSession.getUserLogin(), authSession.getUserPassword() );
    }

    public List<AuthInfo> getAuthInfoList(AuthSession authSession) {
        return authDao.getAuthInfoList( authSession );
    }

    public AuthInfo getAuthInfo(AuthSession authSession, Long authUserId) {
        final AuthInfo currentAuthInfo = getAuthInfo( authSession );
        final AuthInfo authInfo = authDao.getAuthInfo( authUserId );
        if( currentAuthInfo==null || authInfo==null ||
            !checkRigthOnUser(currentAuthInfo.getAuthUserId(), authInfo.getAuthUserId() ) ) {
            return null;
        }
        return authInfo;
    }

    public RoleBean getRole( AuthSession authSession , Long roleId) {
        return authDao.getRole( authSession, roleId );
    }

    public List<RoleBean> getUserRoleList( AuthSession authSession ) {
        return authDao.getUserRoleList( authSession );
    }

    public List<RoleBean> getRoleList( AuthSession authSession ) {
        return authDao.getRoleList( authSession );
    }

    public List<RoleBean> getRoleList(AuthSession authSession, Long authUserId) {
        return authDao.getRoleList( authSession, authUserId );
    }

    public Long addRole( AuthSession authSession, RoleBean roleBean) {
        return authDao.addRole( authSession, roleBean );
    }

    public void updateRole( AuthSession authSession, RoleBean roleBean ) {
        authDao.updateRole( authSession, roleBean );
    }

    public void deleteRole( AuthSession authSession, RoleBean roleBean ) {
        authDao.deleteRole( authSession, roleBean );
    }

    public Long addUser(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        return authDao.addUser( authSession, infoAuth );
    }

    public void updateUser(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        authDao.updateUser( authSession, infoAuth );
    }

    public void deleteUser(AuthSession authSession, AuthUserExtendedInfo infoAuth) {
        authDao.deleteUser( authSession, infoAuth );
    }

    public List<UserInfo> getUserList(AuthSession authSession) {
        return authDao.getUserList( authSession );
    }

    public List<Company> getCompanyList(AuthSession authSession) {
        return companyDao.getCompanyList( authSession );
    }

    public List<GroupCompany> getGroupCompanyList(AuthSession authSession) {
        return companyDao.getGroupCompanyList( authSession );
    }

    public List<Holding> getHoldingList(AuthSession authSession) {
        return companyDao.getHoldingList( authSession );
    }

}
