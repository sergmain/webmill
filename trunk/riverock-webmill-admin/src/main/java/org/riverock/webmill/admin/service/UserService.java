/*
 * org.riverock.webmill.init - Webmill portal initializer web application
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 * Riverock - The Open-source Java Development Community,
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
package org.riverock.webmill.init.service;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.faces.model.SelectItem;

import org.riverock.webmill.init.bean.CompanyBean;
import org.riverock.webmill.init.bean.UserBean;
import org.riverock.webmill.init.dao.DaoFactory;

/**
 * @author SergeMaslyukov
 *         Date: 13.07.2006
 *         Time: 21:55:44
 *         $Id: PortalUserSessionBean.java 753 2006-07-10 07:53:57Z serg_main $
 */
public class UserService implements Serializable {
    private static final long serialVersionUID = 2055006515L;

    public UserService() {
    }
    public List<SelectItem> getCompanyList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<CompanyBean> companies = DaoFactory.getWebmillInitDao().getCompanyList();

        for (CompanyBean companyBean : companies) {
            list.add(new SelectItem(companyBean.getId(), companyBean.getName()));
        }
        return list;
    }

    public List<UserBean> getPortalUserList() {
        List<UserBean> list = DaoFactory.getWebmillInitDao().getUserList();
        if (list==null) {
            return null;
        }

        Iterator<UserBean> iterator = list.iterator();
        List<UserBean> portalUsers = new ArrayList<UserBean>();
        while(iterator.hasNext()) {
            UserBean portalUser = iterator.next();
            portalUsers.add( new UserBean(portalUser) );
        }
        return portalUsers;
    }
}
