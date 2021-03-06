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

import java.util.Date;

import org.riverock.interfaces.portal.spi.PortalUserMetadataSpi;
import org.riverock.interfaces.portal.user.UserMetadataItem;
import org.riverock.interfaces.sso.a3.AuthSession;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * @author Sergei Maslyukov
 *         Date: 26.05.2006
 *         Time: 21:18:50
 */
public class PortalUserMetadataSpiImpl implements PortalUserMetadataSpi {
    private AuthSession authSession = null;
    private ClassLoader classLoader = null;

    PortalUserMetadataSpiImpl(AuthSession authSession, ClassLoader classLoader, Long siteId) {
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
