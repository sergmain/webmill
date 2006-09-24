/*
 * org.riverock.webmill - Portal framework implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
