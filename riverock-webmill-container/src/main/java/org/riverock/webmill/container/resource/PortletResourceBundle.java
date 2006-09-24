/*
 * org.riverock.webmill.container - Webmill portlet container implementation
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
 * $Id$
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
