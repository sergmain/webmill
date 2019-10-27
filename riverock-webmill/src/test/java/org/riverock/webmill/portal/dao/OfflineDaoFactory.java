package org.riverock.webmill.portal.dao;

/**
 * User: SMaslyukov
 * Date: 24.08.2007
 * Time: 10:38:41
 */
public class OfflineDaoFactory {
    public static void init() throws Exception {
        InternalDaoFactory.setInternalSiteDao( new OfflineInternalSiteDao() );
        InternalDaoFactory.setInternalSiteLanguageDao( new OfflineInternalSiteLanguageDao() );
        InternalDaoFactory.setInternalCatalogDao( new OfflineInternalCatalogDao() );
        InternalDaoFactory.setInternalTemplateDao( new OfflineInternalTemplateDao() );
        InternalDaoFactory.setInternalPortletNameDao( new OfflineInternalPortletNameDao() );
        InternalDaoFactory.setInternalAliasDao( new OfflineInternalAliasDao() );
    }
}
