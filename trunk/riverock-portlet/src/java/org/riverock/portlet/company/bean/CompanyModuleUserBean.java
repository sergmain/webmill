package org.riverock.portlet.company.bean;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.interfaces.sso.a3.AuthException;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.sso.a3.AuthInfo;
import org.riverock.sso.a3.InternalAuthProvider;

public class CompanyModuleUserBean implements Serializable {
    private static final long serialVersionUID = 2055005503L;

    private static final Logger log = Logger.getLogger( CompanyModuleUserBean.class );

    private AuthSession authSession = null;

    public CompanyModuleUserBean() {
        this.authSession = ( AuthSession ) FacesTools.getUserPrincipal();
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

    public Long getUserId() {
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

    public boolean isCompany() {
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
            throw new IllegalStateException( es, e );
        }
        return authInfo.getUseCurrentFirm() == 1;
    }

    public boolean isGroupCompany() {
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
            throw new IllegalStateException( es, e );
        }
        return authInfo.getService() == 1;
    }

    public boolean isHolding() {
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
            throw new IllegalStateException( es, e );
        }
        return authInfo.getRoad() == 1;
    }

    public Long getGroupCompanyId() {
        if( authSession == null ) {
            return null;
        }
        AuthInfo authInfo = null;
        try {
            authInfo = InternalAuthProvider.getAuthInfo( authSession );
        	return authInfo.getServiceID();
        }
        catch( AuthException e ) {
            String es = "Error get group company Id";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
    }

    public Long getHoldingId() {
        if( authSession == null ) {
            return null;
        }
        AuthInfo authInfo = null;
        try {
            authInfo = InternalAuthProvider.getAuthInfo( authSession );
        	return authInfo.getRoadID();
        }
        catch( AuthException e ) {
            String es = "Error get holding Id";
            log.error( es, e );
            throw new IllegalStateException( es, e );
        }
    }

    public AuthInfo getAuthInfo() throws AuthException {
        if (authSession!=null) {
            return InternalAuthProvider.getAuthInfo( authSession );
        }
        return null;
    }

}
