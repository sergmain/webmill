package org.riverock.portlet.manager.users;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.User;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:55:44
 *         $Id$
 */
public class PortalUserSessionBean implements Serializable {
    private static final long serialVersionUID = 2057005504L;

    private User portalUser = null;
    private Long currentPortalUserId = null;

    public PortalUserSessionBean() {
    }

    public User getPortalUser() {
        return portalUser;
    }

    public void setPortalUser(User portalUser) {
        this.portalUser = portalUser;
    }

    public Long getCurrentPortalUserId() {
        return currentPortalUserId;
    }

    public void setCurrentPortalUserId(Long currentPortalUserId) {
        this.currentPortalUserId = currentPortalUserId;
    }
}
