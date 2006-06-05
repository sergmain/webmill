/*
 * org.riverock.webmill -- Portal framework implementation
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
