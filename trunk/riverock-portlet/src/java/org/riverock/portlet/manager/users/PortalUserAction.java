/*
 * org.riverock.portlet -- Portlet Library
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
package org.riverock.portlet.manager.users;

import java.io.Serializable;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.interfaces.portal.bean.User;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:55:44
 *         $Id$
 */
public class PortalUserAction implements Serializable {
    private static final long serialVersionUID = 2057005501L;

    private PortalUserSessionBean portalUserSessionBean = null;
    private AuthSessionBean authSessionBean = null;

    public PortalUserAction() {
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean(AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

    public PortalUserSessionBean getSessionBean() {
        return portalUserSessionBean;
    }

    public void setPortalUserSessionBean(PortalUserSessionBean portalUserSessionBean) {
        this.portalUserSessionBean = portalUserSessionBean;
    }

    public String addPortalUser() {
        portalUserSessionBean.setPortalUser(new PortalUserBeanImpl());

        return "portal-user-add";
    }

    public String processAddPortalUser() {
        Long id = FacesTools.getPortalDaoProvider().getPortalUserDao().addUser(portalUserSessionBean.getPortalUser());

        portalUserSessionBean.setCurrentPortalUserId(id);
        loadCurrentPortalUser();
        return "portal-user";
    }

    public String cancelAddPortalUser() {
        loadCurrentPortalUser();
        return "portal-user";
    }

    public String processEditPortalUser() {
        FacesTools.getPortalDaoProvider().getPortalUserDao().updateUser(portalUserSessionBean.getPortalUser());
        return "portal-user";
    }

    public String cancelEditPortalUser() {
        loadCurrentPortalUser();
        return "portal-user";
    }

    public String processDeletePortalUser() {
        FacesTools.getPortalDaoProvider().getPortalUserDao().deleteUser(portalUserSessionBean.getPortalUser());
        portalUserSessionBean.setPortalUser(null);
        return "portal-user";
    }

    public String selectPortalUser() {
        loadCurrentPortalUser();
        return "portal-user";
    }

    private void loadCurrentPortalUser() {
        if (portalUserSessionBean.getCurrentPortalUserId() == null) {
            portalUserSessionBean.setPortalUser(null);
            return;
        }
        User user = FacesTools.getPortalDaoProvider().getPortalUserDao().getUser(portalUserSessionBean.getCurrentPortalUserId());
        portalUserSessionBean.setPortalUser( new PortalUserBeanImpl(user) );
    }
}
