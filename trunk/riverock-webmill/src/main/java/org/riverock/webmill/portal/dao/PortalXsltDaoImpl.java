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

import java.util.Map;
import java.util.List;

import org.riverock.interfaces.portal.dao.PortalXsltDao;
import org.riverock.interfaces.portal.bean.Xslt;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 14:17:22
 */
public class PortalXsltDaoImpl implements PortalXsltDao {
    private AuthSession authSession = null;
    private ClassLoader classLoader = null;

    PortalXsltDaoImpl(AuthSession authSession, ClassLoader classLoader) {
        this.authSession = authSession;
        this.classLoader = classLoader;
    }

    public StringBuilder getXsltData(Long xsltId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalXsltDao().getXsltData(xsltId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    /**
     * key is language of site
     */
    public Map<String, Xslt> getCurrentXsltForSiteAsMap(Long siteId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalXsltDao().getCurrentXsltForSiteAsMap(siteId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Xslt getCurrentXslt(Long siteLanguageId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalXsltDao().getCurrentXslt(siteLanguageId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Xslt getXslt(Long xsltId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalXsltDao().getXslt(xsltId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Xslt getXslt(String xsltName, Long siteLanguageId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalXsltDao().getXslt(xsltName, siteLanguageId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Long createXslt(Xslt xslt) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalXsltDao().createXslt(xslt);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void updateXslt(Xslt xslt) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            InternalDaoFactory.getInternalXsltDao().updateXslt(xslt);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void deleteXslt(Long xsltId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            InternalDaoFactory.getInternalXsltDao().deleteXslt(xsltId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public List<Xslt> getXsltList(Long siteLanguageId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalXsltDao().getXsltList(siteLanguageId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }
}
