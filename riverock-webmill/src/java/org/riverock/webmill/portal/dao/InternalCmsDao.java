package org.riverock.webmill.portal.dao;

import org.riverock.generic.db.DatabaseAdapter;

/**
 * @author Sergei Maslyukov
 *         Date: 24.05.2006
 *         Time: 18:10:18
 */
public interface InternalCmsDao {
    public void deleteArticleForSite(DatabaseAdapter adapter, Long siteId);
    public void deleteArticleForSiteLanguage(DatabaseAdapter adapter, Long siteLanguageId);
    public void deleteNewsForSite(DatabaseAdapter adapter, Long siteId);
    public void deleteNewsForSiteLanguage(DatabaseAdapter adapter, Long siteLanguageId);
}
