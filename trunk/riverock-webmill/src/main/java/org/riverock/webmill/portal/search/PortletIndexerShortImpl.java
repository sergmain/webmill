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

    public PortletIndexerShortImpl(Object id, String portletName) {
        this.id = id;
        this.portletName = portletName;
    }

    public Object getId() {
        return id;  
    }

    public String getPortletName() {
        return portletName;
    }
}
