/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.7</a>, using an XML
 * Schema.
 * $Id$
 */

package org.riverock.webmill.container.schema.site.types;

import java.util.Map;
import java.util.HashMap;

/**
 * Class TemplateItemTypeTypeType.
 *
 * @version $Revision$ $Date$
 */
public class TemplateItemTypeTypeType {


    /**
     * The custom type
     */
    public static final int CUSTOM_TYPE = 0;

    /**
     * The instance of the custom type
     */
    public static final TemplateItemTypeTypeType CUSTOM = new TemplateItemTypeTypeType(CUSTOM_TYPE, "custom");

    /**
     * The portlet type
     */
    public static final int PORTLET_TYPE = 1;

    /**
     * The instance of the portlet type
     */
    public static final TemplateItemTypeTypeType PORTLET = new TemplateItemTypeTypeType(PORTLET_TYPE, "portlet");

    /**
     * The dynamic type
     */
    public static final int DYNAMIC_TYPE = 2;

    /**
     * The instance of the dynamic type
     */
    public static final TemplateItemTypeTypeType DYNAMIC = new TemplateItemTypeTypeType(DYNAMIC_TYPE, "dynamic");

    /**
     * The file type
     */
    public static final int FILE_TYPE = 3;

    /**
     * The instance of the file type
     */
    public static final TemplateItemTypeTypeType FILE = new TemplateItemTypeTypeType(FILE_TYPE, "file");

    /**
     * Field _memberTable
     */
    private static Map<String, TemplateItemTypeTypeType> _memberTable = init();

    /**
     * Field type
     */
    private int type = -1;

    /**
     * Field stringValue
     */
    private java.lang.String stringValue = null;


    private TemplateItemTypeTypeType(int type, java.lang.String value) {
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
    private static Map<String, TemplateItemTypeTypeType> init() {
        Map<String, TemplateItemTypeTypeType> members = new HashMap<String, TemplateItemTypeTypeType>();
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
    public static org.riverock.webmill.container.schema.site.types.TemplateItemTypeTypeType valueOf(java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid TemplateItemTypeTypeType";
            throw new IllegalArgumentException(err);
        }
        return (TemplateItemTypeTypeType) obj;
    }
}
