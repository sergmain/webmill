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

/**
 * @author SergeMaslyukov
 *         Date: 05.12.2005
 *         Time: 20:21:34
 *         $Id$
 */
public class InternalDaoFactory {
    public final static InternalCssDao internalCssDao = new HibernateCssDaoImpl();
    public final static InternalXsltDao internalXsltDao = new HibernateXsltDaoImpl();
    public final static InternalSiteDao internalSiteDao = new HibernateSiteDaoImpl();
    public final static InternalSiteLanguageDao internalSiteLanguageDao = new HibernateSiteLanguageDaoImpl();
    public final static InternalVirtualHostDao internalVirtualHostDao = new HibernateVirtualHostDaoImpl();
    public final static InternalPortletNameDao internalPortletNameDao = new HibernatePortletNameDaoImpl();
    public final static InternalAuthDao internalAuthDao = new HibernateAuthDaoImpl();

    public final static InternalCompanyDao internalCompanyDao = new InternalCompanyDaoImpl();
    public final static InternalHoldingDao internalHoldingDao = new InternalHoldingDaoImpl();
    public final static InternalUserMetadataDao internalUserMetadataDao = new InternalUserMetadataDaoImpl();
    public final static InternalUserDao internalUserDao = new InternalUserDaoImpl();

    public final static InternalDao internalDao = new InternalDaoImpl();
    public final static InternalTemplateDao internalTemplateDao = new InternalTemplateDaoImpl();
    public final static InternalCatalogDao internalCatalogDao = new InternalCatalogDaoImpl();
    public final static InternalCmsDao internalCmsDao = new InternalCmsDaoImpl();
    public final static InternalPreferencesDao internalPreferencesDao = new InternalPreferencesDaoImpl();

    public static InternalPreferencesDao getInternalPreferencesDao() {
        return internalPreferencesDao;
    }

    public static InternalUserDao getInternalUserDao() {
        return internalUserDao;
    }

    public static InternalUserMetadataDao getInternalUserMetadataDao() {
        return internalUserMetadataDao;
    }

    public static InternalCmsDao getInternalCmsDao() {
        return internalCmsDao;
    }

    public static InternalCssDao getInternalCssDao() {
        return internalCssDao;
    }

    public static InternalCatalogDao getInternalCatalogDao() {
        return internalCatalogDao;
    }

    public static InternalPortletNameDao getInternalPortletNameDao() {
        return internalPortletNameDao;
    }

    public static InternalTemplateDao getInternalTemplateDao() {
        return internalTemplateDao;
    }

    public static InternalSiteLanguageDao getInternalSiteLanguageDao() {
        return internalSiteLanguageDao;
    }

    public static InternalVirtualHostDao getInternalVirtualHostDao() {
        return internalVirtualHostDao;
    }

    public static InternalXsltDao getInternalXsltDao() {
        return internalXsltDao;
    }

    public static InternalSiteDao getInternalSiteDao() {
        return internalSiteDao;
    }

    public static InternalDao getInternalDao() {
        return internalDao;
    }

    public static InternalCompanyDao getInternalCompanyDao() {
        return internalCompanyDao;
    }

    public static InternalHoldingDao getInternalHoldingDao() {
        return internalHoldingDao;
    }

    public static InternalAuthDao getInternalAuthDao() {
        return internalAuthDao;
    }
}
