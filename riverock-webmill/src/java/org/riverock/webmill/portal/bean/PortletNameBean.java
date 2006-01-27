package org.riverock.webmill.portal.bean;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 16:39:36
 *         $Id$
 */
public class PortletNameBean {
    private Long portletId; // _idSiteCtxType;
    private String name;  // _type

    public Long getPortletId() {
        return portletId;
    }

    public void setPortletId(Long portletId) {
        this.portletId = portletId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
