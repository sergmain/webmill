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

import org.riverock.interfaces.portal.bean.Holding;
import org.riverock.interfaces.portal.dao.PortalHoldingDao;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:59:09
 *         $Id$
 */
public class PortalHoldingDaoImpl implements PortalHoldingDao {
    private AuthSession authSession = null;
    private ClassLoader classLoader = null;

    PortalHoldingDaoImpl(AuthSession authSession, ClassLoader classLoader) {
        this.authSession = authSession;
        this.classLoader = classLoader;
    }

    public Holding loadHolding(Long id) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalHoldingDao().loadHolding( id, authSession );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public List<Holding> getHoldingList() {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalHoldingDao().getHoldingList( authSession );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public Long processAddHolding(Holding holdingBean ) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            return InternalDaoFactory.getInternalHoldingDao().processAddHolding( holdingBean, authSession );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void processSaveHolding(Holding holdingBean) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            InternalDaoFactory.getInternalHoldingDao().processSaveHolding( holdingBean, authSession );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }

    public void processDeleteHolding(Holding holdingBean) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            InternalDaoFactory.getInternalHoldingDao().processDeleteHolding( holdingBean, authSession );
        }
        finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
    }
}
