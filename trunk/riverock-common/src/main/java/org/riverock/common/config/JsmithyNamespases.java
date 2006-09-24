/*
 * org.riverock.common - Supporting classes and utilities
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
package org.riverock.common.config;

/**
 * User: serg_main
 * Date: 10.02.2004
 * Time: 17:52:58
 *
 * @author Serge Maslyukov
 *         $Id$
 */
public class JsmithyNamespases {
    public final static String[][] namespace = new String[][] {
        {"mill-core", "http://webmill.askmore.info/mill/xsd/mill-core.xsd"},
        {"mill-auth", "http://webmill.askmore.info/mill/xsd/mill-auth.xsd"},
        {"mill-db-stuct", "http://webmill.askmore.info/mill/xsd/mill-database-structure.xsd"},

        {"jsmithy-", "http://generic.jsmithy.com/xsd/jsmithy-database-structure.xsd"},
        {"jsmithy-sso", "http://sso.jsmithy.com/xsd/jsmithy-sso.xsd"},
        {"jsmithy-sso-core", "http://sso.jsmithy.com/xsd/jsmithy-sso-core.xsd"},

        {"riverock-db-stuct", "http://generic.riverock.org/xsd/riverock-database-structure.xsd"},
        {"riverock-sso", "http://sso.riverock.org/xsd/riverock-sso.xsd"},
        {"riverock-sso-core", "http://sso.riverock.org/xsd/riverock-sso-core.xsd"},

        {"portlet", "http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd"}
    };
}
