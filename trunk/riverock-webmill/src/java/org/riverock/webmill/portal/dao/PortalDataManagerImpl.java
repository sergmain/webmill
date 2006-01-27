package org.riverock.webmill.portal.dao;

import org.riverock.interfaces.portal.PortalDataManager;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 1:30:20
 *         $Id$
 */
public class PortalDataManagerImpl implements PortalDataManager {
    private AuthSession authSession = null;

    public PortalDataManagerImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

}
