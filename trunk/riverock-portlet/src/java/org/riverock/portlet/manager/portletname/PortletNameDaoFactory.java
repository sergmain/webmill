package org.riverock.portlet.manager.portletname;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:52:35
 *         $Id$
 */
public class PortletNameDaoFactory {
    private static PortletNameDao portletNameDao = new PortletNameDaoImpl();

    public static PortletNameDao getPortletNameDao() {
        return portletNameDao;
    }
}
