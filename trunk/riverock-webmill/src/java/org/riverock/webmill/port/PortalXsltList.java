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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.webmill.exception.PortalException;

/**
 * $Id$
 */
public final class PortalXsltList {
    private final static Logger log = Logger.getLogger(PortalXsltList.class);

    public Map<String, PortalXslt> hash = new HashMap<String, PortalXslt>();

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

    public PortalXslt getXslt(String lang) {
        if (lang == null) {
            return null;
        }

        if (log.isDebugEnabled()) {
            log.debug("XsltList.size - " + hash.size());
        }

        return (PortalXslt) hash.get(lang);
    }

    public static PortalXsltList getInstance(DatabaseAdapter db_, Long idSite) throws PortalException {

        if (log.isDebugEnabled()) {
            log.debug("XsltList. serverName  ID - " + idSite);
        }

        PortalXsltList list = new PortalXsltList();

        String sql_ =
            "select a.CUSTOM_LANGUAGE, d.ID_SITE_XSLT " +
            "from   WM_PORTAL_SITE_LANGUAGE a, WM_PORTAL_XSLT d " +
            "where  a.ID_SITE=? and " +
            "       a.ID_SITE_SUPPORT_LANGUAGE=d.ID_SITE_SUPPORT_LANGUAGE and " +
            "       d.IS_CURRENT=1";

        PreparedStatement ps = null;
        ResultSet rset = null;

        Map<String, PortalXslt> tempHash = new HashMap<String, PortalXslt>();
        try {
            ps = db_.prepareStatement(sql_);

            ps.setObject(1, idSite);
            rset = ps.executeQuery();
            while (rset.next()) {
                String lang = StringTools.getLocale(RsetTools.getString(rset, "CUSTOM_LANGUAGE")).toString();
                Long id = RsetTools.getLong(rset, "ID_SITE_XSLT");

                if (log.isDebugEnabled()) {
                    log.debug("XsltList. lang - " + lang);
                    log.debug("XsltList. id - " + id);
                }

                PortalXslt item = PortalXslt.getInstance(db_, id);
                item.xsltLang = lang;

                tempHash.put(lang, item);
            }
        }
        catch (Throwable e) {
            final String es = "Error create PortalXsltList";
            log.error(es, e);
            throw new PortalException(es, e);
        }
        finally {
            DatabaseManager.close(rset, ps);
            rset = null;
            ps = null;
        }

        if (log.isDebugEnabled()) {
            log.debug("XsltList. count of templates - " + tempHash.size());
        }

        if (tempHash.size() == 0)
            return null;

        list.hash = tempHash;

        return list;
    }
}