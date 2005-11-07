package org.riverock.module.web.user;

import java.security.Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.sso.a3.AuthInfo;
import org.riverock.sso.a3.InternalAuthProvider;
import org.riverock.module.exception.ModuleException;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.sso.a3.AuthException;

/**
 * @author SMaslyukov
 *         Date: 04.05.2005
 *         Time: 16:56:56
 *         $Id$
 */
public class WebmillModuleUserImpl implements ModuleUser {
    private static final Log log = LogFactory.getLog(WebmillModuleUserImpl.class);

    private AuthSession authSession = null;
    public WebmillModuleUserImpl(Principal userPrincipal) {
        this.authSession = (AuthSession) userPrincipal;
    }

    public String getName() {
        if (authSession==null) {
            return null;
        }
        else {
            return authSession.getName();
        }
    }

    public String getAddress() {
        if (authSession==null) {
            return null;
        }
        else {
            return authSession.getUserInfo().getAddress();
        }
    }

    public Long getId() {
        if (authSession==null) {
            return null;
        }
        else {
            return authSession.getUserInfo().getUserId();
        }
    }

    public String getUserLogin() {
        if (authSession==null) {
            return null;
        }
        else {
            return authSession.getUserLogin();
        }
    }

    public boolean isCompany() throws ModuleException {
        AuthInfo authInfo = null;
        try {
            authInfo = InternalAuthProvider.getAuthInfo(authSession);
        }
        catch (AuthException e) {
            String es = "Error check access level";
            log.error( es, e );
            throw new ModuleException( es, e );
        }
        return authInfo.getUseCurrentFirm()==1;
    }

    public boolean isGroupCompany()  throws ModuleException {
        AuthInfo authInfo = null;
        try {
            authInfo = InternalAuthProvider.getAuthInfo(authSession);
        }
        catch (AuthException e) {
            String es = "Error check access level";
            log.error( es, e );
            throw new ModuleException( es, e );
        }
        return authInfo.getService()==1;
    }

    public boolean isHolding()  throws ModuleException {
        AuthInfo authInfo = null;
        try {
            authInfo = InternalAuthProvider.getAuthInfo(authSession);
        }
        catch (AuthException e) {
            String es = "Error check access level";
            log.error( es, e );
            throw new ModuleException( es, e );
        }
        return authInfo.getRoad()==1;
    }
}
