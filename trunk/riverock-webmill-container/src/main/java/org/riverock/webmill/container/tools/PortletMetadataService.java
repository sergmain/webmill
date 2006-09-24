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
