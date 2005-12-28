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
public final class PageContextFactory extends ContextFactory {
    private final static Logger log = Logger.getLogger(PageContextFactory.class);

    private PageContextFactory(ContextFactoryParameter factoryParameter) throws PortalException, PortalPersistenceException {
        super(factoryParameter);
    }

    public static ContextFactory getInstance(ContextFactoryParameter factoryParameter)
        throws PortalException, PortalPersistenceException {

        PageContextFactory factory = new PageContextFactory(factoryParameter);
        if (factory.getDefaultCtx()!=null)
            return factory;
        else
            return null;
    }

    protected Long initPortalParameters(ContextFactoryParameter factoryParameter) throws PortalException {

        log.debug(
            "Start process as 'page', format request: " +
            "/<CONTEXT>/page/<LOCALE>[/<num_of_portlet,portlet_param>]/name/<PAGE_NAME>[?<portlet_param>]..."
        );
        try {
            String path = factoryParameter.getRequest().getPathInfo();
            if (path==null || path.equals("/")) {
                return null;
            }

            int idxSlash = path.indexOf('/', 1);
            String localeFromUrl = null;
            String pageName = null;
            if (idxSlash==-1) {
                return null;
            }
            else {
                localeFromUrl = path.substring(1, idxSlash);
                pageName = path.substring( idxSlash+1 );
            }

            Long ctxId = DatabaseManager.getLongValue(
                    factoryParameter.getAdapter(),
                    "select a.ID_SITE_CTX_CATALOG " +
                    "from   WM_PORTAL_CATALOG a, WM_PORTAL_CATALOG_LANGUAGE b, WM_PORTAL_SITE_LANGUAGE c " +
                    "where  a.ID_SITE_CTX_LANG_CATALOG=b.ID_SITE_CTX_LANG_CATALOG and " +
                    "       b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE and " +
                    "       c.ID_SITE=? and lower(c.CUSTOM_LANGUAGE)=? and " +
                    "       a.CTX_PAGE_URL=?",
                    new Object[]{
                        factoryParameter.getPortalInfo().getSiteId(),
                        StringTools.getLocale( localeFromUrl ).toString().toLowerCase(),
                        pageName}
            );

            if (log.isDebugEnabled())  {
                log.debug("siteId: " + factoryParameter.getPortalInfo().getSiteId() );
                log.debug("locale: " + StringTools.getLocale( localeFromUrl ).toString().toLowerCase());
                log.debug("pageName: " + pageName);
                log.debug("ctxId: " + ctxId);
            }
            return ctxId;

        } catch (Exception e) {
            String es = "Error get ID of context from DB";
            log.error(es, e);
            throw new PortalException(es, e);
        }
    }

    protected void prepareParameters( HttpServletRequest httpRequest, Map<String, Object> httpRequestParameter ) {

    }
}
