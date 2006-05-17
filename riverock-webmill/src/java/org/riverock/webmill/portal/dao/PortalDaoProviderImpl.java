package org.riverock.webmill.portal.dao;

import org.riverock.interfaces.portal.dao.*;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 1:30:20
 *         $Id$
 */
public class PortalDaoProviderImpl implements PortalDaoProvider {
    private PortalAuthDao portalAuthDao = null;
    private PortalCompanyDao portalCompanyDao = null;
    private PortalHoldingDao portalHoldingDao = null;
    private PortalCommonDao portalCommonDao = null;
    private PortalCatalogDao portalCatalogDao = null;
    private PortalPortletNameDao portalPortletNameDao = null;
    private PortalSiteDao portalSiteDao = null;
    private PortalSiteLanguageDao portalSiteLanguageDao = null;
    private PortalTemplateDao portalTemplateDao = null;
    private PortalVirtualHostDao portalVirtualHostDao = null;
    private PortalXsltDao portalXsltDao = null;

    public PortalDaoProviderImpl(AuthSession authSession) {
        this.portalCompanyDao = new PortalCompanyDaoImpl(authSession);
        this.portalHoldingDao = new PortalHoldingDaoImpl(authSession);
        this.portalAuthDao = new PortalAuthDaoImpl(authSession);

        this.portalCatalogDao = new PortalCatalogDaoImpl(authSession);
        this.portalPortletNameDao = new PortalPortletNameDaoImpl(authSession);
        this.portalSiteDao = new PortalSiteDaoImpl(authSession);
        this.portalSiteLanguageDao = new PortalSiteLanguageDaoImpl(authSession);
        this.portalTemplateDao = new PortalTemplateDaoImpl(authSession);
        this.portalVirtualHostDao = new PortalVirtualHostDaoImpl(authSession);
        this.portalXsltDao = new PortalXsltDaoImpl();
    }

    public PortalCommonDao getPortalCommonDao() {
        return portalCommonDao;
    }

    public PortalAuthDao getPortalAuthDao() {
        return portalAuthDao;
    }

    public PortalCompanyDao getPortalCompanyDao() {
        return portalCompanyDao;
    }

    public PortalHoldingDao getPortalHoldingDao() {
		return portalHoldingDao;
	}

    public PortalCatalogDao getPortalCatalogDao() {
        return portalCatalogDao;
    }

    public PortalPortletNameDao getPortalPortletNameDao() {
        return portalPortletNameDao;
    }

    public PortalSiteDao getPortalSiteDao() {
        return portalSiteDao;
    }

    public PortalSiteLanguageDao getPortalSiteLanguageDao() {
        return portalSiteLanguageDao;
    }

    public PortalTemplateDao getPortalTemplateDao() {
        return portalTemplateDao;
    }

    public PortalVirtualHostDao getPortalVirtualHostDao() {
        return portalVirtualHostDao;
    }

    public PortalXsltDao getPortalXsltDao() {
        return portalXsltDao;
    }
}
