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
import org.apache.commons.lang.StringUtils;

import org.riverock.common.tools.StringTools;
import org.riverock.webmill.portal.url.interpreter.UrlInterpreterResult;
import org.riverock.webmill.portal.url.interpreter.UrlInterpreterParameter;
import org.riverock.interfaces.ContainerConstants;

/**
 * $Id: PageidUrlInterpreter.java 1362 2007-08-28 10:17:16Z serg_main $
 */
public final class PageidUrlInterpreter implements UrlInterpreter {
    private final static Logger log = Logger.getLogger(PageidUrlInterpreter.class);

    public PageidUrlInterpreter() {
    }

    public UrlInterpreterResult interpret(UrlInterpreterParameter factoryParameter) {

        log.debug("Start process as '/pageid', format request: [/<CONTEXT>]/pageid/<LOCALE>/<CONTEXT_ID>/...");
        // format request: [/<CONTEXT>]/pageid/<LOCALE>/<CONTEXT_ID>/...

        String path = factoryParameter.getPathInfo();
        if (StringUtils.isBlank(path) || path.equals("/")) {
            return null;
        }

        if (!path.equals(ContainerConstants.PAGEID_SERVLET_NAME) && !path.startsWith(ContainerConstants.PAGEID_SERVLET_NAME+'/')) {
            return null;
        }
        path = path.substring(ContainerConstants.PAGEID_SERVLET_NAME.length());

        if (log.isDebugEnabled()) {
            log.debug("path: " + path);
        }

        int idxSlash = path.indexOf('/', 1);
        if (idxSlash == -1) {
            log.warn("Bad format of URL for /pageid context: " + path);
            return null;
        }

        Locale locale = StringTools.getLocale(path.substring(1, idxSlash));

        int idx = path.indexOf('/', idxSlash + 1);
        String pageId;
        if (idx == -1) {
            pageId = path.substring(idxSlash + 1);
        }
        else {
            pageId = path.substring(idxSlash + 1, idx);
        }

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

        UrlInterpreterResult urlInterpreterResult = UrlInterpreterUtils.getRequestContextBean(factoryParameter, ctxId);
        if (log.isDebugEnabled()) {
            log.debug("result of pageid interpreter: " + urlInterpreterResult);
        }
        return urlInterpreterResult;
    }
}
