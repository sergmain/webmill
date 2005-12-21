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

package org.riverock.webmill.portal.context;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.exception.PortalPersistenceException;
import org.riverock.webmill.portal.ContextFactory;

/**
 * $Id$
 */
public final class PageidContextFactory extends ContextFactory {
    private final static Logger log = Logger.getLogger(PageidContextFactory.class);

    private PageidContextFactory(ContextFactoryParameter factoryParameter) throws PortalException, PortalPersistenceException {
        super(factoryParameter);
    }

    public static ContextFactory getInstance(ContextFactoryParameter factoryParameter) throws PortalException, PortalPersistenceException {
        PageidContextFactory factory = new PageidContextFactory(factoryParameter);
        if (factory.getDefaultCtx()!=null)
            return factory;
        else
            return null;
    }

    protected Long initPortalParameters(ContextFactoryParameter factoryParameter) throws PortalException {

        log.debug("Start process as '/pageid', format request: /<CONTEXT>/pageid/<LOCALE>/<CONTEXT_ID>/...");
        // format request: /<CONTEXT>/pageid/<LOCALE>/<CONTEXT_ID>/...

        try {
            String path = factoryParameter.getRequest().getPathInfo();
            if (log.isDebugEnabled()) {
                log.debug( "path: " + path + ", content type: " +factoryParameter.getRequest().getContentType() );
            }
            if (path==null || path.equals("/")) {
                return null;
            }

            int idxSlash = path.indexOf('/', 1);
            String localeFromUrl = null;
            String pageId = null;
            if (idxSlash==-1) {
                localeFromUrl = path.substring(1);
                pageId = getRealLocale().toString();
            }
            else {
                localeFromUrl = path.substring(1, idxSlash);
                int idxLocale = path.indexOf('/', idxSlash+1);
                if (idxLocale==-1)
                    pageId = path.substring(idxSlash+1);
                else
                    pageId = path.substring(idxSlash+1, idxLocale);
            }

            Long ctxId = DatabaseManager.getLongValue(
                factoryParameter.getAdapter(),
                "select a.ID_SITE_CTX_CATALOG " +
                "from   WM_PORTAL_CATALOG a, WM_PORTAL_CATALOG_LANGUAGE b, WM_PORTAL_SITE_LANGUAGE c " +
                "where  a.ID_SITE_CTX_LANG_CATALOG=b.ID_SITE_CTX_LANG_CATALOG and " +
                "       b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and " +
                "       c.ID_SITE=? and lower(c.CUSTOM_LANGUAGE)=? and a.ID_SITE_CTX_CATALOG=?",
                new Object[]{
                    factoryParameter.getPortalInfo().getSiteId(),
                    StringTools.getLocale( localeFromUrl ).toString().toLowerCase(), new Long(pageId) }
            );

            return ctxId;

        } catch (Exception e) {
            String es = "Error get ID of context from DB";
            log.error(es, e);
            throw new PortalException(es, e);
        }
    }

    protected void prepareParameters( HttpServletRequest httpRequest, Map httpRequestParameter ) {
    }
}
