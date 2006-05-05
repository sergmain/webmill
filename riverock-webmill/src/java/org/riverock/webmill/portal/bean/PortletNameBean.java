package org.riverock.webmill.portal.bean;

import org.riverock.interfaces.portal.bean.PortletName;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 16:39:36
 *         $Id$
 */
public class PortletNameBean implements PortletName {
    private Long portletId;
    private String portletName;
    private boolean isActive;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Long getPortletId() {
        return portletId;
    }

    public void setPortletId(Long portletId) {
        this.portletId = portletId;
    }

    public String getPortletName() {
        return portletName;
    }

    public void setPortletName(String portletName) {
        this.portletName = portletName;
    }
}
