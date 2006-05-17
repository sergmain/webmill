package org.riverock.interfaces.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.VirtualHost;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 13:57:58
 */
public interface PortalVirtualHostDao {
    public List<VirtualHost> getVirtualHostsFullList();
    public List<VirtualHost> getVirtualHosts(Long siteId);
    public Long createVirtualHost(VirtualHost virtualHost);
}
