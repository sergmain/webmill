package org.riverock.interfaces.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.SiteLanguage;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 13:55:03
 */
public interface PortalSiteLanguageDao {
    public List<SiteLanguage> getSiteLanguageList( Long siteId );
    public SiteLanguage getSiteLanguage(Long siteLanguageId);
    public SiteLanguage getSiteLanguage(String languageLocale);
    public Long createSiteLanguage(SiteLanguage siteLanguage);
}
