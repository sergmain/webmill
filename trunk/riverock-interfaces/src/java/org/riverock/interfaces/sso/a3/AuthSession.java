package org.riverock.interfaces.sso.a3;

import java.security.Principal;
import java.util.List;

/**
 * @author SergeMaslyukov
 *         Date: 05.11.2005
 *         Time: 1:50:28
 *         $Id$
 */
public interface AuthSession extends Principal {
    public boolean isUserInRole( String roleName );
    public String getUserLogin();
    public String getUserPassword();
    
    public boolean checkAccess( final String serverName );

    public String getGrantedUserId();
    public List<Long> getGrantedUserIdList();

    public String getGrantedCompanyId();
    public List<Long> getGrantedCompanyIdList();
    public String getGrantedGroupCompanyId();
    public List<Long> getGrantedGroupCompanyIdList();
    public String getGrantedHoldingId();
    public List<Long> getGrantedHoldingIdList();

    public Long checkCompanyId(Long companyId);
    public Long checkGroupCompanyId(Long groupCompanyId);
    public Long checkHoldingId(Long holdingId);

    public boolean checkRigthOnUser( Long id_auth_user_check, Long id_auth_user_owner );

    public UserInfo getUserInfo();

    public AuthInfo getAuthInfo();
    public AuthInfo getAuthInfo(Long authInfoId );
}
