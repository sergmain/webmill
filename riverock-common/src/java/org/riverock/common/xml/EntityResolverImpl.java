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
package org.riverock.common.xml;

import java.util.Map;
import java.util.HashMap;
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

    public static Map<String, String> entityMap = null;
    static {
        entityMap = new HashMap<String, String>();
        entityMap.put( IBM_PORTLET_DTD_PUBLIC_ID, DTD_PACKAGE+PORTLET_DTD);
        entityMap.put( SUN_WEBXML_23_DTD_PUBLIC_ID, DTD_PACKAGE+WEBXML_23_DTD);
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        String url = (String)entityMap.get( publicId );
        if (url==null) {
            return null;
        }
        InputStream stream = EntityResolverImpl.class.getResourceAsStream(url);
        return new InputSource( stream );
    }
}
