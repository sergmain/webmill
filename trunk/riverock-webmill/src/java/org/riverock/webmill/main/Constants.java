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

/**
 *
 * $Id$
 *
 */
package org.riverock.webmill.main;

import org.riverock.webmill.as.server.ApplicationServerConstants;

public class Constants
{
    public final static String NAME_LOCALE_COOKIE   = "webmill.lang.cookie";

    public static final String MEMBER_NAME_MOD_PARAM    = "mill.member.mod"; //module
    public static final String MEMBER_NAME_APPL_PARAM    = "mill.member.appl"; //appl

    /**
     * @ deprecated use ApplicationServerConstants.NAME_APPL_PARAM;
     */
//    public static final String NAME_APPL_PARAM = ApplicationServerConstants.NAME_APPL_PARAM;

    public static final String MILL_APPL_DIR    = "appl";
    public static final String MILL_PORTLET_DIR    = "portlet";

    public final static String NAME_TYPE_CONTEXT_PARAM    = "mill.context";
    public final static String NAME_TEMPLATE_CONTEXT_PARAM    = "mill.template";

    public final static String CTX_TYPE_INDEX = "mill.index";
    public final static String NAME_PORTLET_PARAM   = "mill.portlet";

    public final static String NAME_XMLROOT_PARAM = "mill.xmlroot";

    public final static String NAME_LANG_PARAM = "mill.lang";
    public final static String NAME_YEAR_PARAM = "mill.year";
    public final static String NAME_MONTH_PARAM = "mill.month";

//    public final static String CTX_TYPE_LOGIN_CHECK   = "mill.login_check";

    public final static String CTX_SERVLET_NAME = "/ctx";
    public final static String PAGE_SERVLET_NAME = "/page";
    public final static String PAGEID_SERVLET_NAME = "/pageid";
    public final static String URL_SERVLET_NAME = "/url";
    public final static String URI_CTX_MANAGER = CTX_SERVLET_NAME;

    public static final String NAME_TOURL_PARAM     = "mill.tourl";

//    public final static String PAGE_TITLE_SESSION = "MILL.PAGE_TITLE_SESSION";
}