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
    public static final String IBM_PORTLET_DTD_PIBLIC_ID = "-//IBM//DTD Portlet Application 1.1//EN";
    public static final String SUN_WEBXML_23_DTD_PIBLIC_ID = "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN";

    public static final String DTD_PACKAGE = "/org/riverock/common/xml/dtd/";
    public static final String PORTLET_DTD = "portlet_1.1.dtd";
    public static final String WEBXML_23_DTD = "web-app_2_3.dtd";

    public static Map<String, String> entityMap = null;
    static {
        entityMap = new HashMap<String, String>();
        entityMap.put( IBM_PORTLET_DTD_PIBLIC_ID, DTD_PACKAGE+PORTLET_DTD);
        entityMap.put( SUN_WEBXML_23_DTD_PIBLIC_ID, DTD_PACKAGE+WEBXML_23_DTD);
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
