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

import java.util.Locale;

import org.apache.log4j.Logger;

import org.riverock.common.tools.StringTools;
import org.riverock.webmill.portal.dao.InternalDaoFactory;

/**
 * $Id$
 */
public final class PageRequestContextProcessor implements RequestContextProcessor {
    private final static Logger log = Logger.getLogger(PageRequestContextProcessor.class);

    public PageRequestContextProcessor() {
    }

    public RequestContext parseRequest(RequestContextParameter factoryParameter) {

        log.debug(
            "Start process as 'page', format request: " +
            "/<CONTEXT>/page/<LOCALE>[/<num_of_portlet,portlet_param>]/name/<PAGE_NAME>[?<portlet_param>]..."
        );

        String path = factoryParameter.getRequest().getPathInfo();
        if (path==null || path.equals("/")) {
            return null;
        }

        int idxSlash = path.indexOf('/', 1);
        if (idxSlash==-1) {
            return null;
        }

        Locale locale = StringTools.getLocale(path.substring(1, idxSlash));
        String pageName = path.substring( idxSlash+1 );

        Long ctxId = InternalDaoFactory.getInternalCatalogDao().getCatalogItemId(
            factoryParameter.getPortalInfo().getSiteId(), locale, pageName
        );

        if (log.isDebugEnabled())  {
            log.debug("siteId: " + factoryParameter.getPortalInfo().getSiteId() );
            log.debug("locale: " + locale.toString() );
            log.debug("pageName: " + pageName);
            log.debug("ctxId: " + ctxId);
        }

        return RequestContextUtils.getRequestContextBean(factoryParameter, ctxId);
    }

}
