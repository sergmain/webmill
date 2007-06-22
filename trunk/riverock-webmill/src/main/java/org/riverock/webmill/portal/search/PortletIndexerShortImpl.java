package org.riverock.webmill.portal.search;

import org.riverock.interfaces.portal.search.PortletIndexerShort;

/**
 * User: SMaslyukov
 * Date: 01.06.2007
 * Time: 18:13:32
 */
public class PortletIndexerShortImpl implements PortletIndexerShort {
    private Object id;
    private String portletName;
    private String className;
    private ClassLoader classLoader;

    public PortletIndexerShortImpl(ClassLoader classLoader, String className, Object id, String portletName) {
        this.classLoader = classLoader;
        this.className = className;
        this.id = id;
        this.portletName = portletName;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public String getClassName() {
        return className;
    }

    public Object getId() {
        return id;  
    }

    public String getPortletName() {
        return portletName;
    }
}
