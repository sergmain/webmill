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

package org.riverock.webmill.portlet.context;

import org.apache.log4j.Logger;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.exception.PortalPersistenceException;
import org.riverock.webmill.port.PortalInfo;
import org.riverock.webmill.portlet.ContextFactory;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * $Id$
 */
public class PageContextFactory extends ContextFactory {

    private static Logger log = Logger.getLogger(PageContextFactory.class);

    private PageContextFactory(DatabaseAdapter adapter, HttpServletRequest request, PortalInfo portalInfo) throws PortalException, PortalPersistenceException {
        super(adapter, request, portalInfo);
    }

    public static PageContextFactory getInstance(DatabaseAdapter db, HttpServletRequest request, PortalInfo portalInfo)
        throws PortalException, PortalPersistenceException {

        PageContextFactory factory = new PageContextFactory(db, request, portalInfo);

        return factory;
    }

    protected Long getCtxId(DatabaseAdapter db, HttpServletRequest request) throws PortalPersistenceException {

        log.debug(
            "Start process as 'page', format request: " +
            "/<CONTEXT>/page/<LOCALE>[/<num_of_portlet,portlet_param>]/name/<PAGE_NAME>[?<portlet_param>]..."
        );
        try {
            String path = request.getPathInfo();
            if (path==null || path.equals("/")) {
                return null;
            }

            int idxSlash = path.indexOf('/', 1);
            String localeFromUrl = null;
            String pageName = null;
            if (idxSlash==-1) {
                localeFromUrl = path.substring(1);
                pageName = getRealLocale().toString();
            }
            else {
                localeFromUrl = path.substring(1, idxSlash);
                int idxLocale = path.indexOf('/', idxSlash+1);
                if (idxLocale==-1)
                    pageName = path.substring(idxSlash+1);
                else
                    pageName = path.substring(idxSlash+1, idxLocale);
            }

            Long ctxId = DatabaseManager.getLongValue(
                    db,
                    "select a.ID_SITE_CTX_CATALOG " +
                    "from   SITE_CTX_CATALOG a, SITE_CTX_LANG_CATALOG b, SITE_SUPPORT_LANGUAGE c " +
                    "where  a.ID_SITE_CTX_LANG_CATALOG=b.ID_SITE_CTX_LANG_CATALOG and " +
                    "       b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and " +
                    "       c.ID_SITE=? and c.CUSTOM_LANGUAGE=? and " +
                    "       a.CTX_PAGE_URL=?",
                    new Object[]{portalInfo.getSiteId(), localeFromUrl, pageName}
            );

            return ctxId;

        } catch (SQLException e) {
            String es = "Error get ID of context from DB";
            log.error(es, e);
            throw new PortalPersistenceException(es, e);
        }
    }

    protected void prepareParameters( HttpServletRequest httpRequest )  throws PortalException {
        //To change body of created methods use File | Settings | File Templates.
    }
}
