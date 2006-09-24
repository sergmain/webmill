/*
 * org.riverock.webmill.admin - Webmill portal admin web application
 *
 * For more information about Webmill portal, please visit project site
 * http://webmill.riverock.org
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community,
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.webmill.admin.action;

import java.io.Serializable;

import org.riverock.webmill.admin.bean.PortalUserSessionBean;
import org.riverock.webmill.admin.bean.UserBean;
import org.riverock.webmill.admin.bean.CompanyBean;
import org.riverock.webmill.admin.dao.DaoFactory;

/**
 * @author SergeMaslyukov
 *         Date: 13.07.2006
 *         Time: 21:55:44
 *         $Id: PortalUserSessionBean.java 753 2006-07-10 07:53:57Z serg_main $
 */
public class UserAction implements Serializable {
    private static final long serialVersionUID = 2057005501L;

    private PortalUserSessionBean portalUserSessionBean = null;

    public UserAction() {
    }

    public PortalUserSessionBean getSessionBean() {
        return portalUserSessionBean;
    }

    public void setPortalUserSessionBean(PortalUserSessionBean portalUserSessionBean) {
        this.portalUserSessionBean = portalUserSessionBean;
    }

    public String addPortalUser() {
        portalUserSessionBean.setPortalUser(new UserBean());

        return "portal-user-add";
    }

    public String processAddPortalUser() {
        Long id = DaoFactory.getWebmillAdminDao().addUser(portalUserSessionBean.getPortalUser());

        portalUserSessionBean.setCurrentPortalUserId(id);
        loadCurrentPortalUser();
        return "portal-user";
    }

    public String cancelAddPortalUser() {
        loadCurrentPortalUser();
        return "portal-user";
    }

    public String processEditPortalUser() {
        DaoFactory.getWebmillAdminDao().updateUser(portalUserSessionBean.getPortalUser());
        return "portal-user";
    }

    public String cancelEditPortalUser() {
        loadCurrentPortalUser();
        return "portal-user";
    }

    public String processDeletePortalUser() {
        DaoFactory.getWebmillAdminDao().deleteUser(portalUserSessionBean.getPortalUser());
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
        UserBean user = DaoFactory.getWebmillAdminDao().getUser(portalUserSessionBean.getCurrentPortalUserId());
        UserBean portalUser = new UserBean(user);
        CompanyBean company = DaoFactory.getWebmillAdminDao().getCompany(
            portalUser.getCompanyId()
        );
        portalUser.setCompanyName(company.getName());

        portalUserSessionBean.setPortalUser( portalUser );
    }
}
