package org.riverock.interfaces.portal.spi;

import java.util.List;

import org.riverock.interfaces.portal.dao.PortalSiteLanguageDao;
import org.riverock.interfaces.portal.bean.SiteLanguage;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:51:18
 * $Id$
 */
public interface PortalSiteLanguageSpi extends PortalSiteLanguageDao {
    public List<SiteLanguage> getSiteLanguageList( Long siteId );
    public SiteLanguage getSiteLanguage(Long siteLanguageId);
    public SiteLanguage getSiteLanguage(Long siteId, String languageLocale);

    public Long createSiteLanguage(SiteLanguage siteLanguage);
    public void updateSiteLanguage(SiteLanguage siteLanguage);
    public void deleteSiteLanguage(Long siteLanguageId);
}
