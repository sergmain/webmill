package org.riverock.webmill.portal.dao;

import org.riverock.interfaces.portal.dao.PortalAuthDao;
import org.riverock.interfaces.portal.dao.PortalCompanyDao;
import org.riverock.interfaces.portal.dao.PortalDaoProvider;
import org.riverock.interfaces.portal.dao.PortalCommonDao;
import org.riverock.interfaces.portal.dao.PortalHoldingDao;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 1:30:20
 *         $Id$
 */
public class PortalDaoProviderImpl implements PortalDaoProvider {
    private PortalAuthDao portalAuthDao = null;
    private PortalCompanyDao portalCompanyDao = null;
    private PortalHoldingDao portalHoldingDao = null;
    private PortalCommonDao portalCommonDao = null;

    public PortalDaoProviderImpl(AuthSession authSession) {
        this.portalCompanyDao = new PortalCompanyDaoImpl(authSession);
        this.portalHoldingDao = new PortalHoldingDaoImpl(authSession);
        this.portalAuthDao = new PortalAuthDaoImpl(authSession);
    }

    public PortalCommonDao getPortalCommonDao() {
        return portalCommonDao;
    }

    public PortalAuthDao getPortalAuthDao() {
        return portalAuthDao;
    }

    public PortalCompanyDao getPortalCompanyDao() {
        return portalCompanyDao;
    }

    public PortalHoldingDao getPortalHoldingDao() {
		return portalHoldingDao;
	}

}
