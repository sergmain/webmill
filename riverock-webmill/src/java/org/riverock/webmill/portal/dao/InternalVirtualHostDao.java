package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.VirtualHost;

/**
 * @author Sergei Maslyukov
 *         Date: 02.05.2006
 *         Time: 17:54:24
 */
public interface InternalVirtualHostDao {
    public List<VirtualHost> getVirtualHostsFullList();
    public List<VirtualHost> getVirtualHosts(Long siteId);
    public Long createVirtualHost(VirtualHost virtualHost);
}
