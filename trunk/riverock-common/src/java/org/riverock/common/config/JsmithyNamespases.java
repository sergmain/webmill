/*
 * org.riverock.common -- Supporting classes, interfaces, and utilities
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

/**
 * User: serg_main
 * Date: 10.02.2004
 * Time: 17:52:58
 * @author Serge Maslyukov
 * $Id$
 */

package org.riverock.common.config;

public class JsmithyNamespases
{
    public final static String[][] namespace = new String[][]
    {
        { "mill-core",     "http://webmill.askmore.info/mill/xsd/mill-core.xsd" },
        { "mill-auth",     "http://webmill.askmore.info/mill/xsd/mill-auth.xsd" },
        { "mill-db-stuct", "http://webmill.askmore.info/mill/xsd/mill-database-structure.xsd" },

        { "jsmithy-",         "http://generic.jsmithy.com/xsd/jsmithy-database-structure.xsd" },
        { "jsmithy-sso",      "http://sso.jsmithy.com/xsd/jsmithy-sso.xsd" },
        { "jsmithy-sso-core", "http://sso.jsmithy.com/xsd/jsmithy-sso-core.xsd" },

        { "riverock-db-stuct", "http://generic.riverock.org/xsd/riverock-database-structure.xsd" },
        { "riverock-sso",      "http://sso.riverock.org/xsd/riverock-sso.xsd" },
        { "riverock-sso-core", "http://sso.riverock.org/xsd/riverock-sso-core.xsd" },

        { "portlet",       "http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd" }
    };
}
