/*
 * org.riverock.webmill -- Portal framework implementation
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
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
 *
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