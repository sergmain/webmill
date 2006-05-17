package org.riverock.portlet.manager.portletname;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.PortletName;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:55:44
 *         $Id$
 */
public class PortletNameBean implements Serializable, PortletName {
    private static final long serialVersionUID = 2057005507L;

    private Long portletId = null;
    private String portletName = null;
    private boolean isActive = false;

    public PortletNameBean() {
    }

    public PortletNameBean(PortletName bean) {
        this.portletId = bean.getPortletId();
        this.portletName = bean.getPortletName();
        this.isActive = bean.isActive();
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean equals( PortletName portletNameBean ) {
        if( portletNameBean == null || portletNameBean.getPortletId()==null || portletId==null ) {
            return false;
        }
        return portletNameBean.getPortletId().equals( portletId );
    }

    public String toString() {
        return "[name:" + portletName + ",id:" + portletId + "]";
    }
}
