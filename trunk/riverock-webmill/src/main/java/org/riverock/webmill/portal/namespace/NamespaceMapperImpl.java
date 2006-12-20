/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
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

    /**
     * decode name of http session attribute to portlet session attribute
     * @param namespace namespace
     * @param name portlet name
     * @return decoded name of attributes
     */
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
