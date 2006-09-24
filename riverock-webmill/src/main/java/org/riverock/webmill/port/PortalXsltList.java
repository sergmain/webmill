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
package org.riverock.webmill.port;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.xslt.XsltTransformer;
import org.riverock.interfaces.portal.xslt.XsltTransformerManager;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * $Id$
 */
public final class PortalXsltList implements XsltTransformerManager {
    private final static Logger log = Logger.getLogger(PortalXsltList.class);

    public Map<String, XsltTransformer> hash = new HashMap<String, XsltTransformer>();

    static {
        try {
            SqlStatement.registerRelateClass(PortalXsltList.class, PortalXslt.class);
        }
        catch (Throwable exception) {
            final String es = "Exception in ";
            log.error(es, exception);
            throw new SqlStatementRegisterException(es, exception);
        }
    }

    public void reinit() {
    }

    protected void finalize() throws Throwable {
        if (hash != null) {
            hash.clear();
            hash = null;
        }

        super.finalize();
    }

    public PortalXsltList() {
    }

    private PortalXsltList(Map<String, XsltTransformer> map) {
        this.hash = map;
    }

    public XsltTransformer getXslt(String lang) {
        if (lang == null) {
            return null;
        }

        if (log.isDebugEnabled()) {
            log.debug("XsltList.size - " + hash.size());
        }

        return hash.get(lang);
    }

    public static PortalXsltList getInstance(Long siteId) {

        if (log.isDebugEnabled()) {
            log.debug("XsltList. serverName  ID - " + siteId);
        }

        Map<String, XsltTransformer> map = InternalDaoFactory.getInternalXsltDao().getTransformerForCurrentXsltMap( siteId );
        return new PortalXsltList( map );
    }
}