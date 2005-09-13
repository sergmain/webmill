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
import java.util.Map;
import java.util.Collection;

import org.riverock.webmill.container.portlet.PortletContainerException;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;

/**
 * User: SergeMaslyukov
 * Date: 06.12.2004
 * Time: 14:36:12
 * $Id$
 */
public final class PortletResourceBundleProvider {

    private static Map<String, PortletResourceBundle> portlets = new HashMap<String, PortletResourceBundle>();

    static Map getPortletsMapInternal(){
        return portlets;
    }

    public static final Object syncObj = new Object();
    public static PortletResourceBundle getInstance( final PortletDefinition portletDefinition, Collection<String> supportedLocales ) throws PortletContainerException {

        final String portletName = portletDefinition.getPortletName();

        Object obj = portlets.get( portletName );
        if (obj!=null) {
            return (PortletResourceBundle)obj;
        }

        PortletResourceBundle bundle = null;
        synchronized( syncObj ) {
            obj = portlets.get( portletName );
            if (obj!=null) {
                return (PortletResourceBundle)obj;
            }

            bundle = PortletResourceBundle.getInstance( portletDefinition, supportedLocales );
            portlets.put( portletName, bundle );
        }
        return bundle;
    }
}
