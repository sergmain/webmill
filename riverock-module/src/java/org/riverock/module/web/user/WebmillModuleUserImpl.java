package org.riverock.module.web.user;

import java.security.Principal;

import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SMaslyukov
 *         Date: 04.05.2005
 *         Time: 16:56:56
 *         $Id$
 */
public class WebmillModuleUserImpl implements ModuleUser {

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

    public boolean isCompany() {
        return authSession.getAuthInfo().isCompany();
    }

//    public boolean isGroupCompany() {
//        return authSession.getAuthInfo().isGroupCompany();
//    }

    public boolean isHolding() {
        return authSession.getAuthInfo().isHolding();
    }
}
