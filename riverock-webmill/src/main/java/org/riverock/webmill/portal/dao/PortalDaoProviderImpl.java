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
    private PortalCssDao portalCssDao = null;
    private PortalUserMetadataDao portalUserMetadataDao = null;
    private PortalUserDao portalUserDao = null;
    private PortalCmsArticleDao portalCmsArticleDao = null;
    private PortalCmsNewsDao portalCmsNewsDao = null;
    private PortalPreferencesDao portalPreferencesDao = null;

    public PortalDaoProviderImpl(AuthSession authSession, ClassLoader classLoader) {
        this.portalCompanyDao = new PortalCompanyDaoImpl(authSession, classLoader);
        this.portalHoldingDao = new PortalHoldingDaoImpl(authSession, classLoader);
        this.portalAuthDao = new PortalAuthDaoImpl(authSession, classLoader);

        this.portalCatalogDao = new PortalCatalogDaoImpl(authSession, classLoader);
        this.portalPortletNameDao = new PortalPortletNameDaoImpl(authSession, classLoader);
        this.portalSiteDao = new PortalSiteDaoImpl(authSession, classLoader);
        this.portalSiteLanguageDao = new PortalSiteLanguageDaoImpl(authSession, classLoader);
        this.portalTemplateDao = new PortalTemplateDaoImpl(authSession, classLoader);
        this.portalVirtualHostDao = new PortalVirtualHostDaoImpl(authSession, classLoader);
        this.portalXsltDao = new PortalXsltDaoImpl(authSession, classLoader);
        this.portalCssDao = new PortalCssDaoImpl(authSession, classLoader);
        this.portalUserMetadataDao = new PortalUserMetadataDaoImpl(authSession, classLoader);
        this.portalUserDao = new PortalUserDaoImpl(authSession, classLoader);
        this.portalCmsArticleDao = new PortalCmsArticleDaoImpl(authSession, classLoader);
        this.portalCmsNewsDao = new PortalCmsNewDaoImpl(authSession, classLoader);
        this.portalPreferencesDao = new PortalPreferencesDaoImpl(authSession, classLoader);
    }

    public PortalCmsArticleDao getPortalCmsArticleDao() {
        return portalCmsArticleDao;
    }

    public PortalCmsNewsDao getPortalCmsNewsDao() {
        return portalCmsNewsDao;
    }

    public PortalPreferencesDao getPortalPreferencesDao() {
        return portalPreferencesDao;
    }

    public PortalUserDao getPortalUserDao() {
        return portalUserDao;
    }

    public PortalUserMetadataDao getPortalUserMetadataDao() {
        return portalUserMetadataDao;
    }

    public PortalCssDao getPortalCssDao() {
        return portalCssDao;
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
