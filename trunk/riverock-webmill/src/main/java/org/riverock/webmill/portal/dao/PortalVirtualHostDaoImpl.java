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

import org.riverock.interfaces.portal.dao.PortalVirtualHostDao;
import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 14:16:08
 */
public class PortalVirtualHostDaoImpl implements PortalVirtualHostDao {
    private AuthSession authSession = null;
    private ClassLoader classLoader = null;

    PortalVirtualHostDaoImpl(AuthSession authSession, ClassLoader classLoader) {
        this.authSession = authSession;
        this.classLoader = classLoader;
    }

    public List<VirtualHost> getVirtualHostsFullList() {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalVirtualHostDao().getVirtualHostsFullList();
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public List<VirtualHost> getVirtualHosts(Long siteId) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalVirtualHostDao().getVirtualHosts(siteId);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Long createVirtualHost(VirtualHost virtualHost) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalVirtualHostDao().createVirtualHost(virtualHost);
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }
}
