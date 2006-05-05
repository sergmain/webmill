package org.riverock.webmill.portal.dao;

import org.riverock.interfaces.portal.bean.Site;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:24:19
 *         $Id$
 */
public interface InternalSiteDao {
    public Site getSite( Long siteId );
    public Site getSite( String siteName );
    public Long createSite(Site site);
}
