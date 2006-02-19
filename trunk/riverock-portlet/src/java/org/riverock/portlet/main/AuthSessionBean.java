package org.riverock.portlet.main;

import java.io.Serializable;

import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.portlet.tools.FacesTools;

public class AuthSessionBean implements Serializable {
    private static final long serialVersionUID = 2055005503L;

    private AuthSession authSession = null;

    public AuthSessionBean() {
        this.authSession = ( AuthSession ) FacesTools.getUserPrincipal();
    }

    public AuthSession getAuthSession() {
        return authSession;
    }

    public String getName() {
        return authSession!=null?authSession.getName():null;
    }

    public String getAddress() {
        return authSession!=null?authSession.getUserInfo().getAddress():null;
    }

    public Long getId() {
        return authSession!=null?authSession.getUserInfo().getUserId():null;
    }

    public String getUserLogin() {
        return authSession!=null?authSession.getUserLogin():null;
    }

    public boolean isCompany() {
        return authSession!=null?authSession.getAuthInfo().isCompany():false;
    }

    public boolean isHolding() {
        return authSession!=null?authSession.getAuthInfo().isHolding():false;
    }

    public Long getHoldingId() {
        return authSession!=null?authSession.getAuthInfo().getHoldingId():null;
    }

}
