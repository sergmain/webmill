package org.riverock.portlet.manager.users;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:52:35
 *         $Id$
 */
public class PortalUserDaoFactory {
    private static PortalUserDao portalUserDao = new PortalUserDaoImpl();

    public static PortalUserDao getPortalUserDao() {
        return portalUserDao;
    }
}
