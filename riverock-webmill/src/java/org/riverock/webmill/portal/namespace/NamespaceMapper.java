package org.riverock.webmill.portal.namespace;

/**
 * Part of code used from Apache Pluto project, License Apache2
 *
 * @author SergeMaslyukov
 *         Date: 13.05.2006
 *         Time: 20:42:21
 *         $Id$
 */
public interface NamespaceMapper {

    public String encode(Namespace namespace, String name);

    public String encode(Namespace ns1, Namespace ns2, String name);

    public String decode(Namespace ns, String name);
}
