package org.riverock.portlet.auth.bean;

import org.apache.log4j.Logger;

import org.riverock.interfaces.sso.a3.AuthException;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.sso.a3.AuthInfo;
import org.riverock.sso.a3.InternalAuthProvider;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 9:26:35
 *         $Id$
 */
public class ModuleUser {
    private static final Logger log = Logger.getLogger( ModuleUser.class );

    private AuthSession authSession = null;

    public ModuleUser() {
        this.authSession = ( AuthSession ) FacesTools.getUserPrincipal();
        ;
    }

    public String getName() {
        if( authSession == null ) {
            return null;
        }
        else {
            return authSession.getName();
        }
    }

    public String getAddress() {
        if( authSession == null ) {
            return null;
        }
        else {
            return authSession.getUserInfo().getAddress();
        }
    }

    public Long getId() {
        if( authSession == null ) {
            return null;
        }
        else {
            return authSession.getUserInfo().getUserId();
        }
    }

    public String getUserLogin() {
        if( authSession == null ) {
            return null;
        }
        else {
            return authSession.getUserLogin();
        }
    }

    public boolean isCompany() throws AuthException {
        if( authSession == null ) {
            return false;
        }
        AuthInfo authInfo = null;
        try {
            authInfo = InternalAuthProvider.getAuthInfo( authSession );
        }
        catch( AuthException e ) {
            String es = "Error check access level";
            log.error( es, e );
            throw new AuthException( es, e );
        }
        return authInfo.getUseCurrentFirm() == 1;
    }

    public boolean isGroupCompany() throws AuthException {
        if( authSession == null ) {
            return false;
        }
        AuthInfo authInfo = null;
        try {
            authInfo = InternalAuthProvider.getAuthInfo( authSession );
        }
        catch( AuthException e ) {
            String es = "Error check access level";
            log.error( es, e );
            throw new AuthException( es, e );
        }
        return authInfo.getService() == 1;
    }

    public boolean isHolding() throws AuthException {
        if( authSession == null ) {
            return false;
        }
        AuthInfo authInfo = null;
        try {
            authInfo = InternalAuthProvider.getAuthInfo( authSession );
        }
        catch( AuthException e ) {
            String es = "Error check access level";
            log.error( es, e );
            throw new AuthException( es, e );
        }
        return authInfo.getRoad() == 1;
    }

    public AuthInfo getAuthInfo() throws AuthException {
        if (authSession!=null) {
            return InternalAuthProvider.getAuthInfo( authSession );
        }
        return null;
    }
}
