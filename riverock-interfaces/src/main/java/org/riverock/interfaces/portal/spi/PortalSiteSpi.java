package org.riverock.interfaces.portal.spi;

import java.util.List;

import org.riverock.interfaces.portal.dao.PortalSiteDao;
import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.VirtualHost;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:50:14
 * $Id$
 */
public interface PortalSiteSpi extends PortalSiteDao {
    public List<Site> getSites();
    public Site getSite( Long siteId );
    public Site getSite( String siteName );
    public Long createSite(Site site);
    public Long createSiteWithVirtualHost(Site site, List<VirtualHost> hosts);

    public void updateSite(Site site);
    public void updateSiteWithVirtualHost(Site site, List<VirtualHost> hosts);

    public void deleteSite(Long siteId);
}
