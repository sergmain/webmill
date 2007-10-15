package org.riverock.portlet.webclip;

/**
 * User: SMaslyukov
* Date: 16.08.2007
* Time: 13:40:16
*/
class ExcludeElement {
    public static final int ID_ATTRIBUTE_TYPE = 1;
    public static final int CLASS_ATTRIBUTE_TYPE = 2;
    public static final int LONGDESC_ATTRIBUTE_TYPE = 3;
    public static final int SRC_ATTRIBUTE_TYPE = 4;

    String name;
    /**
     * type of attribute.
     * 1 - 'id' attr
     * 2 - 'class' attr
     * 3 - 'longdesc' attr
     * 4 - 'src' attr
     */
    int typeOfAttribute;
    String value;

    public ExcludeElement(String name, int typeOfAttribute, String value) {
        this.name = name;
        this.typeOfAttribute = typeOfAttribute;
        this.value = value;
    }
}
