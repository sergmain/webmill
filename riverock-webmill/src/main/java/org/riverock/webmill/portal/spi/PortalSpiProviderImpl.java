/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.webmill.portal.spi;

import org.riverock.interfaces.portal.spi.*;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 1:30:20
 *         $Id: PortalSpiProviderImpl.java 1416 2007-09-08 18:12:27Z serg_main $
 */
public class PortalSpiProviderImpl implements PortalSpiProvider {
    private PortalAuthSpi portalAuthDao = null;
    private PortalCompanySpi portalCompanyDao = null;
    private PortalHoldingSpi portalHoldingDao = null;
    private PortalCommonSpi portalCommonDao = null;
    private PortalCatalogSpi portalCatalogDao = null;
    private PortalPortletNameSpi portalPortletNameDao = null;
    private PortalSiteSpi portalSiteDao = null;
    private PortalSiteLanguageSpi portalSiteLanguageDao = null;
    private PortalTemplateSpi portalTemplateDao = null;
    private PortalVirtualHostSpi portalVirtualHostDao = null;
    private PortalXsltSpi portalXsltDao = null;
    private PortalCssSpi portalCssDao = null;
    private PortalUserMetadataSpi portalUserMetadataDao = null;
    private PortalUserSpi portalUserDao = null;
    private PortalCmsArticleSpi portalCmsArticleDao = null;
    private PortalCmsNewsSpi portalCmsNewsDao = null;
    private PortalPreferencesSpi portalPreferencesDao = null;
    private PortalAliasSpi portalAliasSpi = null;

    public PortalSpiProviderImpl(AuthSession authSession, ClassLoader classLoader, Long siteId) {
        this.portalCompanyDao = new PortalCompanySpiImpl(authSession, classLoader, siteId);
        this.portalHoldingDao = new PortalHoldingSpiImpl(authSession, classLoader, siteId);
        this.portalAuthDao = new PortalAuthSpiImpl(authSession, classLoader, siteId);

        this.portalCatalogDao = new PortalCatalogSpiImpl(authSession, classLoader, siteId);
        this.portalPortletNameDao = new PortalPortletNameSpiImpl(authSession, classLoader, siteId);
        this.portalSiteDao = new PortalSiteSpiImpl(authSession, classLoader, siteId);
        this.portalSiteLanguageDao = new PortalSiteLanguageSpiImpl(authSession, classLoader, siteId);
        this.portalTemplateDao = new PortalTemplateSpiImpl(authSession, classLoader, siteId);
        this.portalVirtualHostDao = new PortalVirtualHostSpiImpl(authSession, classLoader, siteId);
        this.portalXsltDao = new PortalXsltSpiImpl(authSession, classLoader, siteId);
        this.portalCssDao = new PortalCssSpiImpl(authSession, classLoader, siteId);
        this.portalUserMetadataDao = new PortalUserMetadataSpiImpl(authSession, classLoader, siteId);
        this.portalUserDao = new PortalUserSpiImpl(authSession, classLoader, siteId);
        this.portalCmsArticleDao = new PortalCmsArticleSpiImpl(authSession, classLoader, siteId);
        this.portalCmsNewsDao = new PortalCmsNewSpiImpl(authSession, classLoader, siteId);
        this.portalPreferencesDao = new PortalPreferencesSpiImpl(authSession, classLoader, siteId);
        this.portalAliasSpi = new PortalAliasSpiImpl(authSession, classLoader, siteId);
    }

    public PortalAliasSpi getPortalAliasSpi() {
        return portalAliasSpi;
    }

    public PortalCmsArticleSpi getPortalCmsArticleDao() {
        return portalCmsArticleDao;
    }

    public PortalCmsNewsSpi getPortalCmsNewsDao() {
        return portalCmsNewsDao;
    }

    public PortalPreferencesSpi getPortalPreferencesDao() {
        return portalPreferencesDao;
    }

    public PortalUserSpi getPortalUserDao() {
        return portalUserDao;
    }

    public PortalUserMetadataSpi getPortalUserMetadataDao() {
        return portalUserMetadataDao;
    }

    public PortalCssSpi getPortalCssDao() {
        return portalCssDao;
    }

    public PortalCommonSpi getPortalCommonDao() {
        return portalCommonDao;
    }

    public PortalAuthSpi getPortalAuthDao() {
        return portalAuthDao;
    }

    public PortalCompanySpi getPortalCompanyDao() {
        return portalCompanyDao;
    }

    public PortalHoldingSpi getPortalHoldingDao() {
        return portalHoldingDao;
    }

    public PortalCatalogSpi getPortalCatalogDao() {
        return portalCatalogDao;
    }

    public PortalPortletNameSpi getPortalPortletNameDao() {
        return portalPortletNameDao;
    }

    public PortalSiteSpi getPortalSiteDao() {
        return portalSiteDao;
    }

    public PortalSiteLanguageSpi getPortalSiteLanguageDao() {
        return portalSiteLanguageDao;
    }

    public PortalTemplateSpi getPortalTemplateDao() {
        return portalTemplateDao;
    }

    public PortalVirtualHostSpi getPortalVirtualHostDao() {
        return portalVirtualHostDao;
    }

    public PortalXsltSpi getPortalXsltDao() {
        return portalXsltDao;
    }
}
