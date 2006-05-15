package org.riverock.webmill.portal.namespace;

/**
 *  Part of code used from Apache Pluto project, License Apache2
 *
 * @author SergeMaslyukov
 *         Date: 13.05.2006
 *         Time: 20:44:02
 *         $Id$
 */
public class NamespaceMapperImpl implements NamespaceMapper {
    private static final String WEBMILL_PREFIX = "Webmill_";

    public NamespaceMapperImpl() {
    }

    // NamespaceMapper Impl ----------------------------------------------------

    public String encode(Namespace namespace, String name) {
        StringBuffer buffer = new StringBuffer(50);
        buffer.append(WEBMILL_PREFIX);
        buffer.append(namespace.getNamespace());
        buffer.append('_');
        buffer.append(name);
        return buffer.toString();
    }

    public String encode(Namespace namespace1,
                         Namespace namespace2,
                         String name) {
        StringBuilder buffer = new StringBuilder(50);
        buffer.append(WEBMILL_PREFIX);
        buffer.append(namespace1.getNamespace());
        buffer.append('_');
        buffer.append(namespace2.getNamespace());
        buffer.append('_');
        buffer.append(name);
        return buffer.toString();
    }

    public String decode(Namespace namespace, String name) {
        if (!name.startsWith(WEBMILL_PREFIX)) {
            return null;
        }
        StringBuilder buffer = new StringBuilder(50);
        buffer.append(WEBMILL_PREFIX);
        buffer.append(namespace.getNamespace());
        buffer.append('_');
        if (!name.startsWith(buffer.toString())) {
            return null;
        }
        return name.substring(buffer.length());
    }

}
