package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.dao.PortalVirtualHostDao;
import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 14:16:08
 */
public class PortalVirtualHostDaoImpl implements PortalVirtualHostDao {
    private AuthSession authSession = null;

    PortalVirtualHostDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

    public List<VirtualHost> getVirtualHostsFullList() {
        return InternalDaoFactory.getInternalVirtualHostDao().getVirtualHostsFullList();
    }

    public List<VirtualHost> getVirtualHosts(Long siteId) {
        return InternalDaoFactory.getInternalVirtualHostDao().getVirtualHosts(siteId);
    }

    public Long createVirtualHost(VirtualHost virtualHost) {
        return InternalDaoFactory.getInternalVirtualHostDao().createVirtualHost(virtualHost);
    }
}
