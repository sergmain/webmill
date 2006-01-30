package org.riverock.webmill.portal.dao;

import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.interfaces.portal.dao.PortalAuthDao;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 2:07:09
 *         $Id$
 */
public class PortalAuthDaoImpl implements PortalAuthDao {
    private AuthSession authSession = null;

    PortalAuthDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

}
