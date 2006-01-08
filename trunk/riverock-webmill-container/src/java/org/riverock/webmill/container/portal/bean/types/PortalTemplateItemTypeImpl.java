/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.7</a>, using an XML
 * Schema.
 * $Id$
 */

package org.riverock.webmill.container.portal.bean.types;

import java.util.Map;
import java.util.HashMap;

import org.riverock.interfaces.portal.template.PortalTemplateItemType;

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
