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
    public final static String URI_CTX_MANAGER = "/ctx";

    public final static String CTX_TYPE_INDEX = "mill.index";
    public final static String NAME_LANG_PARAM = "mill.lang";
    public final static String NAME_TYPE_CONTEXT_PARAM    = "mill.context";
    public final static String NAME_TEMPLATE_CONTEXT_PARAM    = "mill.template";


    public static final String PORTAL_QUERY_METHOD_ATTRIBUTE = "org.riverock.webmill.PORTAL_QUERY_METHOD_ATTRIBUTE";
    public static final String PORTAL_COOKIE_MANAGER_ATTRIBUTE = "org.riverock.webmill.PORTAL_COOKIE_MANAGER_ATTRIBUTE";

    public static final String PORTAL_QUERY_STRING_ATTRIBUTE = "org.riverock.webmill.PORTAL_QUERY_STRING_ATTRIBUTE";
    public static final String PORTAL_COOKIES_ATTRIBUTE = "org.riverock.webmill.PORTAL_COOKIES_ATTRIBUTE";
    public static final String PORTAL_AUTH_ATTRIBUTE = "org.riverock.webmill.PORTAL_AUTH_ATTRIBUTE";
    public static final String PORTAL_RESOURCE_BUNDLE_ATTRIBUTE = "org.riverock.webmill.PORTAL_RESOURCE_BUNDLE_ATTRIBUTE";
    public static final String PORTAL_TEMPLATE_NAME_ATTRIBUTE = "org.riverock.webmill.PORTAL_TEMPLATE_NAME_ATTRIBUTE";

    public static final String PORTAL_TEMPLATE_PARAMETERS_ATTRIBUTE = "org.riverock.webmill.PORTAL_TEMPLATE_PARAMETERS_ATTRIBUTE";
    public static final String PORTAL_PORTLET_METADATA_ATTRIBUTE = "org.riverock.webmill.PORTAL_PORTLET_METADATA_ATTRIBUTE";

    /** value of current catalog item id (id of menu item) */
    public static final String PORTAL_CURRENT_CATALOG_ID_ATTRIBUTE = "org.riverock.webmill.PORTAL_CURRENT_CATALOG_ID_ATTRIBUTE";
    public static final String PORTAL_PORTLET_CONFIG_ATTRIBUTE = "org.riverock.webmill.PORTAL_PORTLET_CONFIG_ATTRIBUTE";
    public static final String PORTAL_PORTAL_CONTEXT_PATH = "org.riverock.webmill.PORTAL_PORTAL_CONTEXT_PATH";
    public static final String PORTAL_CURRENT_CONTAINER = "org.riverock.webmill.PORTAL_CURRENT_CONTAINER";
    public static final String PORTAL_PORTAL_DAO_PROVIDER = "org.riverock.webmill.PORTAL_PORTAL_DAO_PROVIDER";
    public static final String PORTAL_PORTAL_SESSION_MANAGER = "org.riverock.webmill.PORTAL_PORTAL_SESSION_MANAGER";
    public static final String PORTAL_PORTLET_NAMESPACE_ATTRIBUTE = "org.riverock.webmill.PORTAL_PORTLET_NAMESPACE_ATTRIBUTE";


    public static final String PORTAL_REMOTE_ADDRESS_ATTRIBUTE = "org.riverock.webmill.PORTAL_REMOTE_ADDRESS_ATTRIBUTE";
    public static final String PORTAL_USER_AGENT_ATTRIBUTE = "org.riverock.webmill.PORTAL_USER_AGENT_ATTRIBUTE";

    public static final String PORTAL_PORTLET_CODE_ATTRIBUTE = "org.riverock.webmill.PORTAL_PORTLET_CODE_ATTRIBUTE";
    public static final String PORTAL_PORTLET_XML_ROOT_ATTRIBUTE = "org.riverock.webmill.PORTAL_PORTLET_XML_ROOT_ATTRIBUTE";

    public static final String PORTAL_INFO_ATTRIBUTE = "org.riverock.webmill.PORTAL_INFO_ATTRIBUTE";

    public static final String type_portlet = "type-portlet";
//    public static final String is_url = "is-url";
    public static final String is_xml = "is-xml";
    public static final String name_portlet_id = "name-portlet-id";
    public static final String locale_name_package = "locale-name-package";
    public static final String name_portlet_code_string = "name-portlet-code-string";
    public static final String class_name_get_list = "class-name-get-list";
    public static final String always_process_as_action = "always-process-as-action";

    public static final String PORTAL_PROP_SITE_ID = "webmill.site-id";
    public static final String PORTAL_PROP_COMPANY_ID = "webmill.company-id";
    public static final String PORTAL_PROP_ADMIN_EMAIL = "webmill.admin-email";

    public static final String jAVAX_PORTLET_CONFIG = "javax.portlet.config";
    public static final String jAVAX_PORTLET_REQUEST = "javax.portlet.request";
    public static final String jAVAX_PORTLET_RESPONSE = "javax.portlet.response";

    public static final String jAVAX_SERVLET_REQEUST_URI = "javax.servlet.request_uri";
    public static final String jAVAX_SERVLET_CONTEXT_PATH = "javax.servlet.context_path";
    public static final String jAVAX_SERVLET_SERVLET_PATH = "javax.servlet.servlet_path";
    public static final String jAVAX_SERVLET_PATH_INFO = "javax.servlet.path_info";
    public static final String jAVAX_SERVLET_QUERY_STRING = "javax.servlet.query_string";
}
