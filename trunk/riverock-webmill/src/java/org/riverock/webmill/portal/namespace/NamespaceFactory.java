package org.riverock.webmill.portal.namespace;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author SergeMaslyukov
 *         Date: 08.03.2006
 *         Time: 2:29:58
 *         $Id$
 */
public class NamespaceFactory {
    private static ConcurrentMap<String, Namespace> namespaces = new ConcurrentHashMap<String, Namespace>();
    private static int index = 1;

    /**
     * The getNamespace method must return a valid identifier as defined in the 3.8 Identifier
     * Section of the Java Language Specification Second Edition.
     *
     * @param fullPortletName
     * @return Namespace
     */
    public static Namespace getNamespace( String fullPortletName, String templateName, int tempalteItemIndex ) {
        String n = "idx-" + tempalteItemIndex + "_" + templateName + '_' + fullPortletName;

        Namespace namespace = namespaces.get(n);
        if (namespace!=null) {
            return namespace;
        }
        synchronized(NamespaceFactory.class) {
            namespace = namespaces.get(n);
            if (namespace!=null) {
                return namespace;
            }

            namespace = new NamespaceImpl( "ns"+(index++) );
            namespaces.put( n, namespace );
            return namespace;
        }
    }
}
