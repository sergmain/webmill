/*
 * org.riverock.webmill -- Portal framework implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
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
        return InternalDaoFactory.getInternalSiteDao().getSites(authSession);
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

    public Long createSiteWithVirtualHost(Site site, List<String> hosts) {
        return InternalDaoFactory.getInternalSiteDao().createSite(site, hosts);
    }

    public void updateSite(Site site) {
        InternalDaoFactory.getInternalSiteDao().updateSite(site);
    }

    public void updateSiteWithVirtualHost(Site site, List<String> hosts) {
        InternalDaoFactory.getInternalSiteDao().updateSite(site, hosts);
    }

    public void deleteSite(Long siteId) {
        InternalDaoFactory.getInternalSiteDao().deleteSite(siteId);
    }
}
