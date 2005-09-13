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
package org.riverock.webmill.container.resource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Collection;

import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.tools.PortletService;

/**
 * User: SergeMaslyukov
 * Date: 10.02.2005
 * Time: 0:22:19
 * $Id$
 */
public final class PortletResourceBundle {

    private Map portletLocales = new HashMap();

    static PortletResourceBundle getInstance(final PortletDefinition portletDefinition, Collection<String> supportedLocales) {
        return new PortletResourceBundle(portletDefinition, supportedLocales);
    }

    private PortletResourceBundle( final PortletDefinition portletDefinition, Collection<String> supportedLocales ) {

        Iterator it = supportedLocales.iterator();
        while (it.hasNext()) {
            String locale = (String)it.next();

            portletLocales.put(
                locale,
                PortletResourceBundleWithLocale.getInstance( portletDefinition, locale )
            );
        }
    }

    public ResourceBundle getResourceBundle( final Locale locale ){
        if (locale==null)
            return null;

        ResourceBundle resourceBundle = null;
        resourceBundle = (ResourceBundle)portletLocales.get( locale.toString() );

        if (resourceBundle!=null)
            return resourceBundle;

        Locale temp = null;
        if (!PortletService.isEmpty( locale.getVariant()) ) {
            temp = new Locale( locale.getLanguage(), locale.getCountry() );
            resourceBundle = (ResourceBundle)portletLocales.get( temp.toString() );

            if (resourceBundle!=null) {
                return resourceBundle;
            }
        }

        if (!PortletService.isEmpty( locale.getCountry())) {
            temp = new Locale( locale.getLanguage());
            resourceBundle = (ResourceBundle)portletLocales.get( temp.toString() );

            if (resourceBundle!=null) {
                return resourceBundle;
            }
        }

        return new PortletResourceBundleEmpty();
    }
}
