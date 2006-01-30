package org.riverock.webmill.a3.provider;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.sso.a3.*;
import org.riverock.sso.schema.config.AuthProviderParametersListType;
import org.riverock.sso.main.MainUserInfo;
import org.riverock.interfaces.sso.a3.UserInfo;
import org.riverock.interfaces.sso.a3.AuthInfo;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.portal.dao.InternalAuthDao;
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
    private final static Logger log = Logger.getLogger( InternalAuthProvider.class );

    private InternalAuthDao authDao = InternalDaoFactory.getInternalAuthDao();

    public InternalAuthProvider() {
    }

    public boolean checkAccess( AuthSession authSession, final String serverName ) {
        return authDao.checkAccess( authSession.getUserLogin(), authSession.getUserPassword(), serverName );
    }

    public void setParameters( final AuthProviderParametersListType params ) {
    }

    public boolean isUserInRole( AuthSession authSession, final String role_ ) {
        return authDao.isUserInRole(authSession.getUserLogin(), authSession.getUserPassword(), role_);
    }

    public UserInfo initUserInfo( AuthSession authSession ) {
        try {
            return MainUserInfo.getInstance( authSession.getUserLogin() );
        }
        catch( Exception e ) {
            final String es = "Error user info for user login: " + authSession.getUserLogin();
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
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

    public AuthInfo getAuthInfo(AuthSession authSession, Long authUserId) {
        final AuthInfo currentAuthInfo = getAuthInfo( authSession );
        final AuthInfo authInfo = authDao.getAuthInfo( authUserId );
        if( currentAuthInfo==null || authInfo==null ||
            !checkRigthOnUser(currentAuthInfo.getAuthUserId(), authInfo.getAuthUserId() ) ) {
            return null;
        }
        return authInfo;
    }
}
