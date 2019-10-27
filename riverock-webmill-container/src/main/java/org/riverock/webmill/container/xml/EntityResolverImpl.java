package org.riverock.webmill.container.xml;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * User: SergeMaslyukov
 * Date: 16.12.2006
 * Time: 17:28:08
 * <p/>
 * $Id$
 */
public class EntityResolverImpl implements EntityResolver {
    public static final String IBM_PORTLET_DTD_PUBLIC_ID = "-//IBM//DTD Portlet Application 1.1//EN";
    public static final String SUN_WEBXML_23_DTD_PUBLIC_ID = "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN";

    public static final String DTD_PACKAGE = "/org/riverock/webmill/container/xml/dtd";
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
