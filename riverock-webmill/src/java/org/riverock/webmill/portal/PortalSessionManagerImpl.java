package org.riverock.webmill.portal;

import javax.portlet.ActionRequest;
import javax.portlet.PortletSession;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.PortalSessionManager;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.common.tools.StringTools;
import org.riverock.sso.a3.AuthSessionImpl;

/**
 * @author SergeMaslyukov
 *         Date: 31.01.2006
 *         Time: 14:31:31
 *         $Id$
 */
public class PortalSessionManagerImpl implements PortalSessionManager {
    private final static Logger log = Logger.getLogger( PortalSessionManagerImpl.class );
    private ActionRequest actionRequest = null;
    private ClassLoader classLoader = null;

    PortalSessionManagerImpl( ClassLoader classLoader, ActionRequest actionRequest ) {
        this.actionRequest = actionRequest;
	this.classLoader = classLoader;
    }

    public boolean createSession(String userLogin, String userPassword) {

	ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );

	if (log.isDebugEnabled()) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
                log.debug("PortalSessionManagerImpl class loader:\n" + cl +"\nhash: "+ cl.hashCode() );
	}

        PortletSession session = actionRequest.getPortletSession();
        AuthSession auth_ = (AuthSession)actionRequest.getUserPrincipal();

        // don't check if login or password is null.
        if ( StringTools.isEmpty(userLogin) || StringTools.isEmpty(userLogin) ) {
            return false;
        }

        if (auth_==null) {
            auth_ = new AuthSessionImpl( userLogin, userPassword );
        }

	if (log.isDebugEnabled()) {
		ClassLoader cl = auth_.getClass().getClassLoader();
                log.debug("AuthSessionImpl class loader:\n" + cl +"\nhash: "+ cl.hashCode() );
	}

        final boolean checkAccess = auth_.checkAccess( actionRequest.getServerName() );
        if ( log.isDebugEnabled() ) {
            log.debug(
                "checkAccess status for server name '"+actionRequest.getServerName()+"' " +
                " is " + checkAccess + ", auth: " + auth_ );
        }
        if (checkAccess) {
            session.setAttribute( org.riverock.sso.main.Constants.AUTH_SESSION, auth_);
            return true;
        }
        else {
            session.removeAttribute( org.riverock.sso.main.Constants.AUTH_SESSION );

            // User not correctly input authorization data
            // remove all objects from session
//            PortletService.cleanSession( session );

            return false;
        }
                }
                finally {
                    Thread.currentThread().setContextClassLoader( oldLoader );
                }
    }
}
