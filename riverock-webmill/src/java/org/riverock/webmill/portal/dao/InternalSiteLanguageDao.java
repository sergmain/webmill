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
