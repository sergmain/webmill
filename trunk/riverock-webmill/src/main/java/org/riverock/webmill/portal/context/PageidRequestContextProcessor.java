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

/**
 * $Id$
 */
public final class PageidRequestContextProcessor implements RequestContextProcessor {
    private final static Logger log = Logger.getLogger(PageidRequestContextProcessor.class);

    public PageidRequestContextProcessor() {
    }

    public RequestContext parseRequest(RequestContextParameter factoryParameter) {

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
        if (idxSlash == -1) {
            log.warn("Bad format of URL for /pageid context: " + path);
            return null;
        }

        Locale locale = StringTools.getLocale(path.substring(1, idxSlash));

        int idx = path.indexOf('/', idxSlash + 1);
        String pageId;
        if (idx == -1)
            pageId = path.substring(idxSlash + 1);
        else
            pageId = path.substring(idxSlash + 1, idx);

        if (log.isDebugEnabled())  {
            log.debug("siteId: " + factoryParameter.getPortalInfo().getSiteId() );
            log.debug("locale: " + locale.toString() );
            log.debug("pageId: " + pageId);
        }

        Long ctxId;
        try {
            ctxId = new Long(pageId);
        }
        catch (java.lang.NumberFormatException e) {
            log.warn("NumberFormatException error of parsing pageid: " + pageId);
            return null;
        }

        return RequestContextUtils.getRequestContextBean(factoryParameter, ctxId);
    }
}
