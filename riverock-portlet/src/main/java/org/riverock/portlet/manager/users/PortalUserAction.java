/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.portlet.manager.users;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.User;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.common.utils.PortletUtils;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:55:44
 *         $Id: PortalUserAction.java 1411 2007-09-06 20:27:38Z serg_main $
 */
public class PortalUserAction implements Serializable {
    private static final long serialVersionUID = 2057005501L;

    private PortalUserSessionBean portalUserSessionBean = null;
    private AuthSessionBean authSessionBean = null;

    public static final String[] ROLES = new String[]{"webmill.portal-manager","webmill.user-manager"};

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
        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        Long id = FacesTools.getPortalSpiProvider().getPortalUserDao().addUser(portalUserSessionBean.getPortalUser());

        portalUserSessionBean.setCurrentPortalUserId(id);
        loadCurrentPortalUser();
        return "portal-user";
    }

    public String cancelAddPortalUser() {
        loadCurrentPortalUser();
        return "portal-user";
    }

    public String processEditPortalUser() {
        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        FacesTools.getPortalSpiProvider().getPortalUserDao().updateUser(portalUserSessionBean.getPortalUser());
        return "portal-user";
    }

    public String cancelEditPortalUser() {
        loadCurrentPortalUser();
        return "portal-user";
    }

    public String processDeletePortalUser() {
        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        FacesTools.getPortalSpiProvider().getPortalUserDao().deleteUser(portalUserSessionBean.getPortalUser());
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
        User user = FacesTools.getPortalSpiProvider().getPortalUserDao().getUser(portalUserSessionBean.getCurrentPortalUserId());
        PortalUserBeanImpl portalUser = new PortalUserBeanImpl(user);
        Company company = FacesTools.getPortalSpiProvider().getPortalCompanyDao().getCompany(
            portalUser.getCompanyId()
        );
        portalUser.setCompanyName(company.getName());

        portalUserSessionBean.setPortalUser( portalUser );
    }
}
