package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.dao.PortalSiteDao;
import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 14:08:08
 */
public class PortalSiteDaoImpl implements PortalSiteDao {
    private AuthSession authSession = null;

    PortalSiteDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

    public List<Site> getSites() {
        return InternalDaoFactory.getInternalSiteDao().getSites();
    }

    public Site getSite(Long siteId) {
        return InternalDaoFactory.getInternalSiteDao().getSite(siteId);
    }

    public Site getSite(String siteName) {
        return InternalDaoFactory.getInternalSiteDao().getSite(siteName);
    }

    public Long createSite(Site site) {
        return InternalDaoFactory.getInternalSiteDao().createSite(site);
    }
}