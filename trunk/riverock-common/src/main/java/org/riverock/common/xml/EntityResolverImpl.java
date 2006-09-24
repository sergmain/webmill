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
package org.riverock.common.xml;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.EntityResolver;

/**
 * @author SMaslyukov
 *         Date: 26.04.2005
 *         Time: 14:35:01
 *         $Id$
 */
public class EntityResolverImpl implements EntityResolver {
    public static final String IBM_PORTLET_DTD_PUBLIC_ID = "-//IBM//DTD Portlet Application 1.1//EN";
    public static final String SUN_WEBXML_23_DTD_PUBLIC_ID = "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN";

    public static final String DTD_PACKAGE = "/org/riverock/common/xml/dtd/";
    public static final String PORTLET_DTD = "portlet_1.1.dtd";
    public static final String WEBXML_23_DTD = "web-app_2_3.dtd";

    private static Map<String, String> entityMap = null;
    static {
        Map<String, String> map = new HashMap<String, String>();
        map.put( IBM_PORTLET_DTD_PUBLIC_ID, DTD_PACKAGE+PORTLET_DTD);
        map.put( SUN_WEBXML_23_DTD_PUBLIC_ID, DTD_PACKAGE+WEBXML_23_DTD);
        entityMap = Collections.unmodifiableMap(map);
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (publicId==null) {
            return null;
        }
        
        String url = entityMap.get( publicId );
        if (url==null) {
            return null;
        }
        InputStream stream = EntityResolverImpl.class.getResourceAsStream(url);
        return new InputSource( stream );
    }

    public static Map<String, String> getEntityMap() {
        return entityMap;
    }
}
