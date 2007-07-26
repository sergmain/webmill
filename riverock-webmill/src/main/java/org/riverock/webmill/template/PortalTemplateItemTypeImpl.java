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
package org.riverock.webmill.template;

import java.util.Map;
import java.util.HashMap;

/**
 * Class TemplateItemTypeTypeType.
 *
 * @version $Revision$ $Date$
 */
public class PortalTemplateItemTypeImpl implements PortalTemplateItemType {


    /**
     * The instance of the custom type
     */
    public static final PortalTemplateItemType CUSTOM = new PortalTemplateItemTypeImpl(CUSTOM_TYPE, "custom");

    /**
     * The instance of the portlet type
     */
    public static final PortalTemplateItemType PORTLET = new PortalTemplateItemTypeImpl(PORTLET_TYPE, "portlet");

    /**
     * The instance of the dynamic type
     */
    public static final PortalTemplateItemType DYNAMIC = new PortalTemplateItemTypeImpl(DYNAMIC_TYPE, "dynamic");

    /**
     * The instance of the file type
     */
    public static final PortalTemplateItemType FILE = new PortalTemplateItemTypeImpl(FILE_TYPE, "file");

    /**
     * Field _memberTable
     */
    private static Map<String, PortalTemplateItemType> _memberTable = init();

    /**
     * Field type
     */
    private int type = -1;

    /**
     * Field stringValue
     */
    private java.lang.String stringValue = null;


    private PortalTemplateItemTypeImpl(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    }

    /**
     * Method getType
     * <p/>
     * Returns the type of this TemplateItemTypeTypeType
     *
     * @return int
     */
    public int getType() {
        return this.type;
    }

    /**
     * Method init
     *
     * @return Hashtable
     */
    private static Map<String, PortalTemplateItemType> init() {
        Map<String, PortalTemplateItemType> members = new HashMap<String, PortalTemplateItemType>();
        members.put("custom", CUSTOM);
        members.put("portlet", PORTLET);
        members.put("dynamic", DYNAMIC);
        members.put("file", FILE);
        return members;
    }

    /**
     * Method toString
     * <p/>
     * Returns the String representation of this
     * TemplateItemTypeTypeType
     *
     * @return String
     */
    public java.lang.String toString() {
        return this.stringValue;
    }

    /**
     * Method valueOf
     * <p/>
     * Returns a new TemplateItemTypeTypeType based on the given
     * String value.
     *
     * @param string
     * @return TemplateItemTypeTypeType
     */
    public static PortalTemplateItemType valueOf(String string) {
        String err = "'" + string + "' is not a valid PortalTemplateItemType";
        if (string == null) {
            throw new IllegalArgumentException(err);
        }

        PortalTemplateItemType obj = null;
        obj = _memberTable.get(string);
        if (obj == null) {
            throw new IllegalArgumentException(err);
        }
        return obj;
    }
}
