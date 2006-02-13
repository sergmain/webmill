package org.riverock.portlet.auth.user.bean;

import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:26:35
 *         $Id$
 */
public class ModuleUserBean {
    private AuthSession authSession = null;

    public ModuleUserBean() {
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

    public boolean isGroupCompany() {
        return authSession!=null?authSession.getAuthInfo().isGroupCompany():false;
    }

    public boolean isHolding() {
        return authSession!=null?authSession.getAuthInfo().isHolding():false;
    }
}
