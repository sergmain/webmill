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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.List;
import java.util.ArrayList;

import org.riverock.webmill.portal.template.parser.ParsedTemplateElement;

/**
 * @author SergeMaslyukov
 *         Date: 08.03.2006
 *         Time: 2:29:58
 *         $Id: NamespaceFactory.java 1375 2007-08-28 19:35:44Z serg_main $
 */
public class NamespaceFactory {
    private static ConcurrentMap<String, Namespace> namespaces = new ConcurrentHashMap<String, Namespace>();
    private static ConcurrentMap<String, List<Namespace>> namespaceByPortletName = new ConcurrentHashMap<String, List<Namespace>>();
    private static int index = 1;

    public static int getTemplateUniqueIndex(ParsedTemplateElement element, int tempalteItemIndex) {
        switch (element.getType()) {
            case XSLT:
                return ("xslt"+element.getXslt().getName()).hashCode();
            case DYNAMIC:
                return ("dynamic").hashCode();
            case PORTLET:
                return ("portlet-"+element.getPortlet().getName()+'-'+element.getPortlet().getCode()+'-'+element.getPortlet().getXmlRoot()).hashCode();
            case STRING:
                return element.hashCode();
            case INCLUDE:
                return "include".hashCode();
            default:
                throw new IllegalStateException("Unknown type of template element. "+element.getClass().getName());
        }
    }

    /**
     * The getNamespaces method must return a valid identifier as defined in the 3.8 Identifier
     * Section of the Java Language Specification Second Edition.
     *
     * @param fullPortletName full portlet name. Format: [["application-id":]"portlet-id":]"portlet-name
     * <br>For example: webmill:login-portlet-id:login-portlet
     * @param templateName template name
     * @param tempalteUniqueIndex intex if item in template
     * @return namespace
     */
    public static Namespace getNamespace(String fullPortletName, String templateName, int tempalteUniqueIndex) {
        String n = "idx-" + tempalteUniqueIndex + "_" + templateName + '_' + fullPortletName;

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
            List<Namespace> namespaces = namespaceByPortletName.get(fullPortletName);
            if (namespaces==null) {
                namespaces = new ArrayList<Namespace>();
                namespaceByPortletName.put(fullPortletName, namespaces);
            }
            namespaces.add(namespace);
            return namespace;
        }
    }

    /**
     * Get all namespaces for portlet name
     * @param fullPortletName portlet name
     * @return list of namespaces
     */
    public static List<Namespace> getNamespaces(String fullPortletName) {
        List<Namespace> list = new ArrayList<Namespace>();
        List<Namespace> namespaces = namespaceByPortletName.get(fullPortletName);
        if (namespaces!=null) {
            list.addAll(namespaces);
        }
        return list;
    }

    public static NamespaceMapper getNamespaceMapper() {
        return new NamespaceMapperImpl();
    }
}
