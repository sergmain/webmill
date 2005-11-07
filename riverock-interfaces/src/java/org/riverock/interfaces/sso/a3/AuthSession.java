package org.riverock.interfaces.sso.a3;

import java.io.Serializable;
import java.security.Principal;

/**
 * @author SergeMaslyukov
 *         Date: 05.11.2005
 *         Time: 1:50:28
 *         $Id$
 */
public interface AuthSession extends Serializable, Principal {
    boolean isUserInRole( String roleName ) throws AuthException;
    public String getUserLogin();
    public void setUserLogin(String userLogin);
    public String getUserPassword();
    public void setUserPassword(String userPassword);
    public String getSessionId();
    public void setSessionId(String sessionId);
    public boolean checkAccess( final String serverName ) throws AuthException;

    public UserInfo getUserInfo();
}
