/*
 * org.riverock.webmill - Portal framework implementation
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
