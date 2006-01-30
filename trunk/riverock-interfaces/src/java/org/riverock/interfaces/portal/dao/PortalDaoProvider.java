package org.riverock.interfaces.portal.dao;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 0:44:37
 *         $Id$
 */
public interface PortalDaoProvider {
    public PortalCommonDao getPortalCommonDao();
    public PortalAuthDao getPortalAuthDao();
    public PortalCompanyDao getPortalCompanyDao();
}
