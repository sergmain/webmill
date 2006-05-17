package org.riverock.interfaces.portal.dao;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 0:44:37
 *         $Id$
 */
public interface PortalDaoProvider {
    public PortalCommonDao getPortalCommonDao();
    public PortalAuthDao getPortalAuthDao();
    public PortalCompanyDao getPortalCompanyDao();
    public PortalHoldingDao getPortalHoldingDao();

    public PortalCatalogDao getPortalCatalogDao();
    public PortalPortletNameDao getPortalPortletNameDao();
    public PortalSiteDao getPortalSiteDao();
    public PortalSiteLanguageDao getPortalSiteLanguageDao();
    public PortalTemplateDao getPortalTemplateDao();
    public PortalVirtualHostDao getPortalVirtualHostDao();
    public PortalXsltDao getPortalXsltDao();

}
