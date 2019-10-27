/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.webmill.portal.dao;

import java.util.List;
import java.util.Observer;

import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:24:19
 *         $Id: InternalSiteDao.java 1352 2007-08-28 10:09:56Z serg_main $
 */
public interface InternalSiteDao {
    public List<Site> getSites();
    public List<Site> getSites(AuthSession authSession);
    public Site getSite( Long siteId );
    public Site getSite( String siteName );
    public Long createSite(Site site);
    public void updateSite(Site site);
    public void deleteSite(Long siteId);

    public void updateSite(Site site, List<VirtualHost> hosts);

    public Long createSite(Site site, List<VirtualHost> hosts);

    void addObserver(Observer o);
}
