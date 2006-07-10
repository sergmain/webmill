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
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.faces.model.SelectItem;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.User;
import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author SergeMaslyukov
 *         Date: 26.02.2006
 *         Time: 15:55:44
 *         $Id$
 */
public class PortalUserService implements Serializable {
    private static final long serialVersionUID = 2055006515L;

    private AuthSessionBean authSessionBean = null;

    public PortalUserService() {
    }

    public AuthSessionBean getAuthSessionBean() {
        return authSessionBean;
    }

    public void setAuthSessionBean(AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

    public List<SelectItem> getCompanyList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<Company> companies = authSessionBean.getAuthSession().getCompanyList();

        for (Company companyBean : companies) {
            list.add(new SelectItem(companyBean.getId(), companyBean.getName()));
        }
        return list;
    }

    public List<User> getPortalUserList() {
        List<User> list = FacesTools.getPortalDaoProvider().getPortalUserDao().getUserList();
        if (list==null) {
            return null;
        }

        Iterator<User> iterator = list.iterator();
        List<User> portalUsers = new ArrayList<User>();
        while(iterator.hasNext()) {
            User portalUser = iterator.next();
            portalUsers.add( new PortalUserBeanImpl(portalUser) );
        }
        return portalUsers;
    }
}
