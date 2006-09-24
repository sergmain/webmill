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

import java.util.Date;

import org.riverock.interfaces.portal.user.UserMetadataItem;
import org.riverock.interfaces.portal.dao.PortalUserMetadataDao;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author Sergei Maslyukov
 *         Date: 26.05.2006
 *         Time: 21:18:50
 */
public class PortalUserMetadataDaoImpl implements PortalUserMetadataDao {
    private AuthSession authSession = null;
    private ClassLoader classLoader = null;

    PortalUserMetadataDaoImpl(AuthSession authSession, ClassLoader classLoader) {
        this.authSession = authSession;
        this.classLoader = classLoader;
    }

    public UserMetadataItem getMetadata(String userLogin, Long siteId, String metadataName) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalUserMetadataDao().getMetadata(userLogin, siteId, metadataName);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void setMetadataIntValue(String userLogin, Long siteId, String metadataName, Long intValue) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            InternalDaoFactory.getInternalUserMetadataDao().setMetadataIntValue(userLogin, siteId, metadataName, intValue);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void setMetadataStringValue(String userLogin, Long siteId, String metadataName, String stringValue) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            InternalDaoFactory.getInternalUserMetadataDao().setMetadataStringValue(userLogin, siteId, metadataName, stringValue);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void setMetadataDateValue(String userLogin, Long siteId, String metadataName, Date dateValue) {
        InternalDaoFactory.getInternalUserMetadataDao().setMetadataDateValue(userLogin, siteId, metadataName, dateValue);
    }
}
