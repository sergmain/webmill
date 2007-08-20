/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.webmill.portal.url.interpreter;

import java.util.Locale;

import org.apache.log4j.Logger;

import org.riverock.common.tools.StringTools;
import org.riverock.webmill.portal.url.RequestContext;
import org.riverock.webmill.portal.url.RequestContextParameter;

/**
 * $Id$
 */
public final class PageidUrlInterpreter implements UrlInterpreter {
    private final static Logger log = Logger.getLogger(PageidUrlInterpreter.class);

    public PageidUrlInterpreter() {
    }

    public RequestContext interpret(RequestContextParameter factoryParameter) {

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
            log.debug("siteId: " + factoryParameter.getSiteId() );
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
