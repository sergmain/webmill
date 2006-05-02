package org.riverock.webmill.portal.dao;

import org.riverock.interfaces.portal.bean.Site;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:24:19
 *         $Id$
 */
public interface InternalSiteDao {
    public Site getSiteBean( Long siteId );
    public Site getSiteBean( String siteName );
    public Long createSite(Site siteBean);
}
