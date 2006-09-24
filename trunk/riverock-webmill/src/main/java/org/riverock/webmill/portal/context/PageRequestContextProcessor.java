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

        if (ctxId==null) {
            return null;
        }
        return RequestContextUtils.getRequestContextBean(factoryParameter, ctxId);
    }

}
