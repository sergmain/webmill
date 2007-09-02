package org.riverock.interfaces.portal.spi;

import org.riverock.interfaces.portal.dao.PortalDaoProvider;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:47:56
 * $Id$
 */
public interface PortalSpiProvider extends PortalDaoProvider {
    public PortalCommonSpi getPortalCommonDao();
    public PortalAuthSpi getPortalAuthDao();
    public PortalCompanySpi getPortalCompanyDao();
    public PortalHoldingSpi getPortalHoldingDao();

    public PortalCatalogSpi getPortalCatalogDao();
    public PortalPortletNameSpi getPortalPortletNameDao();
    public PortalSiteSpi getPortalSiteDao();
    public PortalSiteLanguageSpi getPortalSiteLanguageDao();
    public PortalTemplateSpi getPortalTemplateDao();
    public PortalVirtualHostSpi getPortalVirtualHostDao();
    public PortalXsltSpi getPortalXsltDao();

    public PortalCssSpi getPortalCssDao();
    public PortalUserMetadataSpi getPortalUserMetadataDao();
    public PortalUserSpi getPortalUserDao();
    public PortalCmsArticleSpi getPortalCmsArticleDao();
    public PortalCmsNewsSpi getPortalCmsNewsDao();
    public PortalPreferencesSpi getPortalPreferencesDao();
}
