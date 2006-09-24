/*
 * org.riverock.commerce - Commerce application
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
package org.riverock.commerce.tools;

import javax.portlet.PortletException;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * @author smaslyukov
 *         Date: 29.07.2005
 *         Time: 19:52:00
 *         $Id: ContentTypeTools.java 678 2006-06-05 19:19:35Z serg_main $
 */
public class ContentTypeTools {
    private final static Logger log = Logger.getLogger(ContentTypeTools.class);

    public static final String CONTENT_TYPE_UTF8 = "utf-8";
    public static final String CONTENT_TYPE_8859_1 = "8859_1";

    public static void setContentType(HttpServletResponse response, String charset) throws PortletException {

        final String type = "text/html; charset=" + charset;

        if (log.isDebugEnabled()) {
            log.debug("set new charset: " + type);
            log.debug("response: " + response);
        }

        try {
            response.setContentType(type);
        } catch (Exception e) {
            final String es = "Error set new content type to " + charset;
            log.error(es, e);
            throw new PortletException(es, e);
        }
    }

    public static void setContentType(RenderResponse response, String charset) throws PortletException {

        final String type = "text/html; charset=" + charset;

        if (log.isDebugEnabled()) {
            log.debug("set new charset: " + type);
            log.debug("response: " + response);
        }

        try {
            response.setContentType(type);
        } catch (Exception e) {
            final String es = "Error set new content type to " + charset;
            log.error(es, e);
            throw new PortletException(es, e);
        }
    }

}
