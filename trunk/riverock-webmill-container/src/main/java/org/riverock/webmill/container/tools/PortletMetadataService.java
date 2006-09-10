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
package org.riverock.webmill.container.tools;

import java.util.Map;

import javax.portlet.PortletRequest;

import org.riverock.webmill.container.ContainerConstants;

/**
 * @author smaslyukov
 *         Date: 02.08.2005
 *         Time: 20:50:36
 *         $Id$
 */
public class PortletMetadataService {
    public static boolean getMetadataBoolean( final PortletRequest portletRequest, final String key, final boolean defValue ) {
        String s = getMetadata(portletRequest, key);
        if (s==null) {
            return defValue;
        }

        return Boolean.valueOf(s);
    }

    public static String getMetadata( final PortletRequest portletRequest, final String key) {
        return getMetadata( portletRequest, key, null );
    }

    public static String getMetadata( final PortletRequest portletRequest, final String key, final String defValue ) {
        if (portletRequest==null || key==null) {
            return defValue;
        }

        Map<String, String> p = (Map<String, String>)portletRequest.getAttribute( ContainerConstants.PORTAL_PORTLET_METADATA_ATTRIBUTE );
        if (p==null) {
            return defValue;
        }

        String value = p.get( key );
        if ( ContainertStringUtils.isBlank(value) ) {
            return defValue;
        }

        return value;
    }
}
