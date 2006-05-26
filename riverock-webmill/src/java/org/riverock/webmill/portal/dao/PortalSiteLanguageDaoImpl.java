package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.dao.PortalSiteLanguageDao;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 14:10:47
 */
public class PortalSiteLanguageDaoImpl implements PortalSiteLanguageDao {
    private AuthSession authSession = null;

    PortalSiteLanguageDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

    public List<SiteLanguage> getSiteLanguageList(Long siteId) {
        return InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguageList(siteId);
    }

    public SiteLanguage getSiteLanguage(Long siteLanguageId) {
        return InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguage(siteLanguageId);
    }

    public SiteLanguage getSiteLanguage(Long siteId, String languageLocale) {
        return InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguage(siteId, languageLocale);
    }

    public Long createSiteLanguage(SiteLanguage siteLanguage) {
        return InternalDaoFactory.getInternalSiteLanguageDao().createSiteLanguage(siteLanguage);
    }
}
