package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portal.dao.PortalPortletNameDao;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:59:09
 *         $Id$
 */
public class PortalPortletNameDaoImpl implements PortalPortletNameDao {
    private AuthSession authSession = null;

    PortalPortletNameDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

    public PortletName getPortletName(Long portletId) {
        return InternalDaoFactory.getInternalPortletNameDao().getPortletName(portletId);
    }

    public PortletName getPortletName(String portletName) {
        return InternalDaoFactory.getInternalPortletNameDao().getPortletName(portletName);
    }

    public List<PortletName> getPortletNameList() {
        return InternalDaoFactory.getInternalPortletNameDao().getPortletNameList();
    }

    public Long createPortletName(PortletName portletName) {
        return InternalDaoFactory.getInternalPortletNameDao().createPortletName(portletName);
    }

    public void updatePortletName(PortletName portletNameBean) {
        InternalDaoFactory.getInternalPortletNameDao().updatePortletName(portletNameBean);
    }

    public void deletePortletName(PortletName portletNameBean) {
        InternalDaoFactory.getInternalPortletNameDao().deletePortletName(portletNameBean);
    }
}
