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
import org.riverock.webmill.exception.PortalException;
import org.riverock.webmill.portal.ContextFactory;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * $Id$
 */
public final class PageidContextFactory extends ContextFactory {
    private final static Logger log = Logger.getLogger(PageidContextFactory.class);

    private PageidContextFactory(ContextFactoryParameter factoryParameter) {
        super(factoryParameter);
    }

    public static ContextFactory getInstance(ContextFactoryParameter factoryParameter) {
        PageidContextFactory factory = new PageidContextFactory(factoryParameter);
        if (factory.getDefaultCtx()!=null)
            return factory;
        else
            return null;
    }

    protected Long initPortalParameters(ContextFactoryParameter factoryParameter) {

        log.debug("Start process as '/pageid', format request: /<CONTEXT>/pageid/<LOCALE>/<CONTEXT_ID>/...");
        // format request: /<CONTEXT>/pageid/<LOCALE>/<CONTEXT_ID>/...

        String path = factoryParameter.getRequest().getPathInfo();
        if (log.isDebugEnabled()) {
            log.debug("path: " + path + ", content type: " + factoryParameter.getRequest().getContentType());
        }
        if (path == null || path.equals("/")) {
            return null;
        }

        int idxSlash = path.indexOf('/', 1);
        String localeFromUrl = null;
        String pageId = null;
        if (idxSlash == -1) {
            localeFromUrl = path.substring(1);
            pageId = getRealLocale().toString();
        }
        else {
            localeFromUrl = path.substring(1, idxSlash);
            int idxLocale = path.indexOf('/', idxSlash + 1);
            if (idxLocale == -1)
                pageId = path.substring(idxSlash + 1);
            else
                pageId = path.substring(idxSlash + 1, idxLocale);
        }

        Long id = null;
        try {
            id = new Long(pageId);
        }
        catch (java.lang.NumberFormatException e) {
            log.warn("NumberFormatException error of parsing pageid: " + pageId);
            return null;
        }

        return InternalDaoFactory.getInternalDao().getCatalogId(
            factoryParameter.getPortalInfo().getSiteId(),  StringTools.getLocale(localeFromUrl), id
        );
    }

    protected void prepareParameters( HttpServletRequest httpRequest, Map<String, Object> httpRequestParameter ) {
    }
}
