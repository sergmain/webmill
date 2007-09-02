package org.riverock.interfaces.portal.spi;

import java.util.List;

import org.riverock.interfaces.portal.dao.PortalVirtualHostDao;
import org.riverock.interfaces.portal.bean.VirtualHost;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:55:04
 * $Id$
 */
public interface PortalVirtualHostSpi extends PortalVirtualHostDao {
    public List<VirtualHost> getVirtualHostsFullList();
    public List<VirtualHost> getVirtualHosts(Long siteId);
    public Long createVirtualHost(VirtualHost virtualHost);
}
