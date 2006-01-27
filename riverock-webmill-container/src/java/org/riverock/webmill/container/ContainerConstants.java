/*
 * org.riverock.webmill.container -- Webmill portlet container implementation
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
package org.riverock.webmill.container;

/**
 * @author smaslyukov
 *         Date: 27.07.2005
 *         Time: 18:42:32
 *         $Id$
 */
public class ContainerConstants {

    public final static String PAGE_SERVLET_NAME = "/page";
    public final static String PAGEID_SERVLET_NAME = "/pageid";
    public final static String URL_SERVLET_NAME = "/url";
    public final static String URI_CTX_MANAGER = "/ctx";

    public static final String URL_PAGE = "/page-";

    public final static String CTX_TYPE_INDEX = "mill.index";
    public final static String NAME_LANG_PARAM = "mill.lang";
    public final static String NAME_TYPE_CONTEXT_PARAM    = "mill.context";
    public final static String NAME_TEMPLATE_CONTEXT_PARAM    = "mill.template";


    public static final String PORTAL_QUERY_METHOD_ATTRIBUTE = "javax.portlet.webmill.PORTAL_QUERY_METHOD_ATTRIBUTE";
    public static final String PORTAL_COOKIE_MANAGER_ATTRIBUTE = "javax.portlet.webmill.PORTAL_COOKIE_MANAGER_ATTRIBUTE";

    public static final String PORTAL_QUERY_STRING_ATTRIBUTE = "javax.portlet.webmill.PORTAL_QUERY_STRING_ATTRIBUTE";
    public static final String PORTAL_COOKIES_ATTRIBUTE = "javax.portlet.webmill.PORTAL_COOKIES_ATTRIBUTE";
    public static final String PORTAL_AUTH_ATTRIBUTE = "javax.portlet.webmill.PORTAL_AUTH_ATTRIBUTE";
    public static final String PORTAL_RESOURCE_BUNDLE_ATTRIBUTE = "javax.portlet.webmill.PORTAL_RESOURCE_BUNDLE_ATTRIBUTE";
    public static final String PORTAL_TEMPLATE_NAME_ATTRIBUTE = "javax.portlet.webmill.PORTAL_TEMPLATE_NAME_ATTRIBUTE";

    public static final String PORTAL_TEMPLATE_PARAMETERS_ATTRIBUTE = "javax.portlet.webmill.PORTAL_TEMPLATE_PARAMETERS_ATTRIBUTE";
    public static final String PORTAL_CURRENT_PORTLET_NAME_ATTRIBUTE = "javax.portlet.webmill.PORTAL_CURRENT_PORTLET_NAME_ATTRIBUTE";
    public static final String PORTAL_PORTLET_METADATA_ATTRIBUTE = "javax.portlet.webmill.PORTAL_PORTLET_METADATA_ATTRIBUTE";

    public static final String PORTAL_DEFAULT_CATALOG_ID_ATTRIBUTE = "javax.portlet.webmill.PORTAL_DEFAULT_CATALOG_ID_ATTRIBUTE";
    public static final String PORTAL_PORTLET_CONFIG_ATTRIBUTE = "javax.portlet.webmill.PORTAL_PORTLET_CONFIG_ATTRIBUTE";
    public static final String PORTAL_PORTAL_CONTEXT_PATH = "javax.portlet.webmill.PORTAL_PORTAL_CONTEXT_PATH";
    public static final String PORTAL_CURRENT_CONTAINER = "javax.portlet.webmill.PORTAL_CURRENT_CONTAINER";
    public static final String PORTAL_PORTAL_DATA_MANAGER = "javax.portlet.webmill.PORTAL_PORTAL_DATA_MANAGER";


    public static final String PORTAL_REMOTE_ADDRESS_ATTRIBUTE = "javax.portlet.webmill.PORTAL_REMOTE_ADDRESS_ATTRIBUTE";
    public static final String PORTAL_USER_AGENT_ATTRIBUTE = "javax.portlet.webmill.PORTAL_USER_AGENT_ATTRIBUTE";

    public static final String PORTAL_PORTLET_CODE_ATTRIBUTE = "javax.portlet.webmill.PORTAL_PORTLET_CODE_ATTRIBUTE";
    public static final String PORTAL_PORTLET_XML_ROOT_ATTRIBUTE = "javax.portlet.webmill.PORTAL_PORTLET_XML_ROOT_ATTRIBUTE";

    public static final String PORTAL_INFO_ATTRIBUTE = "javax.portlet.webmill.PORTAL_INFO_ATTRIBUTE";

    public static final String type_portlet = "type-portlet";
    public static final String is_url = "is-url";
    public static final String is_xml = "is-xml";
    public static final String name_portlet_id = "name-portlet-id";
    public static final String locale_name_package = "locale-name-package";
    public static final String name_portlet_code_string = "name-portlet-code-string";
    public static final String class_name_get_list = "class-name-get-list";
    public static final String is_direct_request = "is-direct-request";


    public static final String PORTAL_PROP_SITE_ID = "webmill.site-id";
    public static final String PORTAL_PROP_COMPANY_ID = "webmill.company-id";
    public static final String PORTAL_PROP_ADMIN_EMAIL = "webmill.admin-email";
}
