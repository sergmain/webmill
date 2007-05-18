/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.portlet.webclip;

/**
 * User: SergeMaslyukov
 * Date: 17.09.2006
 * Time: 17:28:27
 * <p/>                                 
 * $Id$
 */
public class WebclipConstants {

    public static final String WEBMILL_WIKI_WEBCLIP = "webmill.wiki-webclip";
    public static final String WEBCLIP_ID_PREF = "webclip.webclip_id";
    public static final String URL_SOURCE_PREF = "webclip.url";
    public static final String NEW_HREF_PREFIX_PREF = "webclip.new_prefix";
    public static final String HREF_START_PAGE_PREF = "webclip.href_start_page";

    static final String PROXY_PORT_PREF = "webclip.proxy_port";
    static final String PROXY_HOST_PREF = "webclip.proxy_host";
    static final String PROXY_LOGIN_PREF = "webclip.proxy_login";
    static final String PROXY_PASSWORD_PREF = "webclip.proxy_password";

    static final String RIVEROCK_WEBLICP_INDEX_JSP = "/riverock/webclip/webclip.jsp";
    static final String WEBCLIP_BEAN = "webclipBean";

    
    /**
     * Save parameters in preferences
     */
    static final String SAVE_PARAM_ACTION = "save-param";
    /**
     * process content
     */
    static final String PROCESS_CONTENT_ACTION = "process-content";
    /**
     * Save parameters in preferences, get origin content and process content
     */
    static final String SAVE_GET_PROCESS_ACTION = "save-get-process";
    /**
     * get origin content
     */
    static final String GET_ORIGIN_CONTENT_ACTION = "get-origin-content";

    static final String SOURCE_URL_PARAM = "sourceUrl";
    static final String NEW_HREF_PREFIX_PARAM = "newHrefPrefix";
    static final String HREF_START_PAGE_PARAM = "hrefStartPart";
    static final String WEBCLIP_ERROR_MESSAGE_PARAM = "webclip_error_message";
    static final String WEBCLIP_ERROR_MESSAGE = "webclipErrorMessage";


    static final int DEFAULT_READ_TIMEOUT = 20000;

    static final int TABLE_NODE_TYPE = 1;
    static final int DIV_NODE_TYPE = 2;

    public static final int MAX_TIME_NOT_REFRESH_DATA = 1000*60*60*3;
}
