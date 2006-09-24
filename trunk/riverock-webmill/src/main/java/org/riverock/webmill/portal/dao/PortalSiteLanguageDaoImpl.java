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
    private ClassLoader classLoader = null;

    PortalSiteLanguageDaoImpl(AuthSession authSession, ClassLoader classLoader) {
        this.authSession = authSession;
        this.classLoader = classLoader;
    }

    public List<SiteLanguage> getSiteLanguageList(Long siteId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguageList(siteId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public SiteLanguage getSiteLanguage(Long siteLanguageId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguage(siteLanguageId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public SiteLanguage getSiteLanguage(Long siteId, String languageLocale) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalSiteLanguageDao().getSiteLanguage(siteId, languageLocale);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Long createSiteLanguage(SiteLanguage siteLanguage) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalSiteLanguageDao().createSiteLanguage(siteLanguage);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void updateSiteLanguage(SiteLanguage siteLanguage) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            InternalDaoFactory.getInternalSiteLanguageDao().updateSiteLanguage(siteLanguage);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void deleteSiteLanguage(Long siteLanguageId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            InternalDaoFactory.getInternalSiteLanguageDao().deleteSiteLanguage(siteLanguageId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }
}
