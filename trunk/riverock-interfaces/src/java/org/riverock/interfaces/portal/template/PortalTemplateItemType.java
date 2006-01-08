package org.riverock.interfaces.portal.template;

/**
 * @author SergeMaslyukov
 *         Date: 01.01.2006
 *         Time: 9:45:11
 *         $Id$
 */
public interface PortalTemplateItemType {
    /**
     * The custom type
     */
    public static final int CUSTOM_TYPE = 0;

    /**
     * The portlet type
     */
    public static final int PORTLET_TYPE = 1;

    /**
     * The dynamic type
     */
    public static final int DYNAMIC_TYPE = 2;

    /**
     * The file type
     */
    public static final int FILE_TYPE = 3;

    public int getType();
    public String toString();
}
