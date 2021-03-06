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
package org.riverock.webmill.container.resource;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.riverock.webmill.container.portlet.bean.PortletDefinition;

/**
 * User: SergeMaslyukov
 * Date: 10.02.2005
 * Time: 0:22:19
 * $Id: PortletResourceBundle.java 1055 2006-11-14 17:56:15Z serg_main $
 */
public final class PortletResourceBundle {

    private Map<String, ResourceBundle> portletLocales = new HashMap<String, ResourceBundle>();
    private PortletDefinition portletDefinition = null;
    private ClassLoader classLoader = null;

    public static PortletResourceBundle getInstance(final PortletDefinition portletDefinition, ClassLoader classLoader) {
        return new PortletResourceBundle( portletDefinition, classLoader );
    }

    private PortletResourceBundle( final PortletDefinition portletDefinition, ClassLoader classLoader ) {
        this.portletDefinition = portletDefinition;
        this.classLoader = classLoader;
    }

    public ResourceBundle getResourceBundle( final Locale locale ) {

        if (locale==null) {
            System.out.println("Locale for resource bundle is null");
            return null;
        }

        ResourceBundle resourceBundle = portletLocales.get( locale.toString() );

        if (resourceBundle!=null) {
            return resourceBundle;
        }
        synchronized(PortletResourceBundle.class) {
            resourceBundle = portletLocales.get( locale.toString() );
            if (resourceBundle!=null) {
                return resourceBundle;
            }
            resourceBundle = PortletResourceBundleWithLocale.getInstance( portletDefinition, locale, classLoader );
            portletLocales.put( locale.toString(), resourceBundle );
        }
        return resourceBundle;
    }
}
