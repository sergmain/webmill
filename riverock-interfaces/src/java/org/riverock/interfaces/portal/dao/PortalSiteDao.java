package org.riverock.interfaces.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.Site;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 13:54:22
 */
public interface PortalSiteDao {
    public List<Site> getSites();
    public Site getSite( Long siteId );
    public Site getSite( String siteName );
    public Long createSite(Site site);
    public Long createSiteWithVirtualHost(Site site, List<String> hosts);

    public void updateSite(Site site);
    public void updateSiteWithVirtualHost(Site site, List<String> hosts);

    public void deleteSite(Long siteId);
}
