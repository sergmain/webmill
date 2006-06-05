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

import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.generic.db.DatabaseAdapter;

/**
 * @author Sergei Maslyukov
 *         Date: 03.05.2006
 *         Time: 17:12:39
 */
public interface InternalSiteLanguageDao {
    public List<SiteLanguage> getSiteLanguageList( Long siteId );
    public SiteLanguage getSiteLanguage(Long siteLanguageId);
    public SiteLanguage getSiteLanguage(Long siteId, String languageLocale);

    public Long createSiteLanguage(SiteLanguage siteLanguage);
    public void updateSiteLanguage(SiteLanguage siteLanguage);
    public void deleteSiteLanguage(Long siteLanguageId);

    public void deleteSiteLanguageForSite(DatabaseAdapter adapter, Long siteId);
}
