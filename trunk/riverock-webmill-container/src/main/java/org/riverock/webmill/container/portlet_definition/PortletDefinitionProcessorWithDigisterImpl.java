/*
 * org.riverock.webmill.container - Webmill portlet container implementation
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
package org.riverock.webmill.container.portlet_definition;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;

import org.apache.commons.digester.Digester;

import org.riverock.webmill.container.portlet.bean.*;
import org.riverock.webmill.container.portlet_definition.PortletDefinitionProcessor;
import org.riverock.webmill.container.portlet.PortletContainerException;

/**
 * @author smaslyukov
 *         Date: 08.08.2005
 *         Time: 15:25:46
 *         $Id$
 */
public class PortletDefinitionProcessorWithDigisterImpl implements PortletDefinitionProcessor, Serializable {
    private static final long serialVersionUID = 50434672384237827L;

    public PortletApplication process(File portletFile) throws PortletContainerException {

        if (portletFile==null || !portletFile.exists()) {
            throw new PortletContainerException( "File "+portletFile.getName()+" not found");
        }

        try {
            final FileInputStream stream = new FileInputStream(portletFile);

            System.out.println("Digest file: " + portletFile.getName());

            Digester digester = new Digester();
            digester.setValidating(false);

            digester.addObjectCreate("portlet-app", PortletApplication.class);
            digester.addSetProperties("portlet-app", "id", "id");
            digester.addSetProperties("portlet-app", "version", "version");


            digester.addObjectCreate("portlet-app/portlet", PortletDefinition.class);
            digester.addSetNext("portlet-app/portlet", "addPortlet");

            digester.addSetProperties("portlet-app/portlet", "id", "id");
            digester.addBeanPropertySetter("portlet-app/portlet/portlet-name", "portletName");
            digester.addBeanPropertySetter("portlet-app/portlet/portlet-class", "portletClass");
            digester.addBeanPropertySetter("portlet-app/portlet/expiration-cache", "expirationCache");
            digester.addBeanPropertySetter("portlet-app/portlet/resource-bundle", "resourceBundle");

            digester.addCallMethod("portlet-app/portlet/supported-locale", "addSupportedLocale", 0, new Class[]{String.class});

            digester.addObjectCreate("portlet-app/portlet/display-name", DisplayName.class);
            digester.addSetProperties("portlet-app/portlet/display-name", "lang", "lang");
            digester.addBeanPropertySetter("portlet-app/portlet/display-name", "content");
            digester.addSetNext("portlet-app/portlet/display-name", "addDisplayName");

            digester.addObjectCreate("portlet-app/portlet/description", Description.class);
            digester.addSetProperties("portlet-app/portlet/description", "lang", "lang");
            digester.addBeanPropertySetter("portlet-app/portlet/description", "content");
            digester.addSetNext("portlet-app/portlet/description", "addDescription");

            digester.addObjectCreate("portlet-app/portlet/init-param", InitParam.class);
            digester.addSetProperties("portlet-app/portlet/init-param", "id", "id");
            digester.addBeanPropertySetter("portlet-app/portlet/init-param/name", "name");
            digester.addBeanPropertySetter("portlet-app/portlet/init-param/value", "value");
            digester.addSetNext("portlet-app/portlet/init-param", "addInitParam");

            digester.addObjectCreate("portlet-app/portlet/init-param/description", Description.class);
            digester.addSetProperties("portlet-app/portlet/init-param/description", "lang", "lang");
            digester.addBeanPropertySetter("portlet-app/portlet/init-param/description", "content");
            digester.addSetNext("portlet-app/portlet/init-param/description", "addDescription");

            digester.addObjectCreate("portlet-app/portlet/supports", Supports.class);
            digester.addSetProperties("portlet-app/portlet/supports", "id", "id");
            digester.addBeanPropertySetter("portlet-app/portlet/supports/mime-type", "mimeType");
            digester.addSetNext("portlet-app/portlet/supports", "addSupports");

            digester.addCallMethod("portlet-app/portlet/supports/portlet-mode", "addPortletMode", 0, new Class[]{String.class});

            digester.addObjectCreate("portlet-app/portlet/portlet-info", PortletInfo.class);
            digester.addSetNext("portlet-app/portlet/portlet-info", "setPortletInfo");
            digester.addBeanPropertySetter("portlet-app/portlet/portlet-info/title", "title");
            digester.addBeanPropertySetter("portlet-app/portlet/portlet-info/short-title", "shortTitle");
            digester.addCallMethod("portlet-app/portlet/portlet-info/keywords", "setKeywords", 0, new Class[]{String.class});
            digester.addBeanPropertySetter("portlet-app/portlet/resource-bundle", "resourceBundle");

            digester.addObjectCreate("portlet-app/portlet/portlet-preferences", Preferences.class);
            digester.addSetProperties("portlet-app/portlet/portlet-preferences", "id", "id");
            digester.addBeanPropertySetter("portlet-app/portlet/portlet-preferences/preferences-validator", "preferencesValidatorClass");
            digester.addSetNext("portlet-app/portlet/portlet-preferences", "setPreferences");

            digester.addObjectCreate("portlet-app/portlet/portlet-preferences/preference", Preference.class);
            digester.addSetProperties("portlet-app/portlet/portlet-preferences/preference", "id", "id");
            digester.addBeanPropertySetter("portlet-app/portlet/portlet-preferences/preference/name", "name");
            digester.addCallMethod("portlet-app/portlet/portlet-preferences/preference/value", "addValue", 0, new Class[]{String.class});
            digester.addCallMethod(
                "portlet-app/portlet/portlet-preferences/preference/read-only",
                "setReadOnly",
                0,
                new Class[] { Boolean.class });
            digester.addSetNext("portlet-app/portlet/portlet-preferences/preference", "addPreference");

            digester.addObjectCreate("portlet-app/user-attribute", UserAttribute.class);
            digester.addSetProperties("portlet-app/user-attribute", "id", "id");
            digester.addBeanPropertySetter("portlet-app/user-attribute/name", "name");
            digester.addSetNext("portlet-app/user-attribute", "addUserAttribute");

            digester.addObjectCreate("portlet-app/user-attribute/description", Description.class);
            digester.addSetProperties("portlet-app/user-attribute/description", "lang", "lang");
            digester.addBeanPropertySetter("portlet-app/user-attribute/description", "content");
            digester.addSetNext("portlet-app/user-attribute/description", "addDescription");

            digester.addObjectCreate("portlet-app/portlet/security-role-ref", SecurityRoleRef.class);
            digester.addSetProperties("portlet-app/portlet/security-role-ref", "id", "id");
            digester.addBeanPropertySetter("portlet-app/portlet/security-role-ref/role-name", "roleName");
            digester.addBeanPropertySetter("portlet-app/portlet/security-role-ref/role-link", "roleLink");
            digester.addSetNext("portlet-app/portlet/security-role-ref", "addSecurityRoleRef");

            digester.addObjectCreate("portlet-app/portlet/security-role-ref/description", Description.class);
            digester.addSetProperties("portlet-app/portlet/security-role-ref/description", "lang", "lang");
            digester.addBeanPropertySetter("portlet-app/portlet/security-role-ref/description", "content");
            digester.addSetNext("portlet-app/portlet/security-role-ref/description", "addDescription");

            PortletApplication st = (PortletApplication)digester.parse(stream);

            return st;
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            throw new PortletContainerException("Error parse portlet.xml file.", e);
        }
    }
}
