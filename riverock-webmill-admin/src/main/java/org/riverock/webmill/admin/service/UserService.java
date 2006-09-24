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
package org.riverock.webmill.admin.service;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.faces.model.SelectItem;

import org.riverock.webmill.admin.bean.CompanyBean;
import org.riverock.webmill.admin.bean.UserBean;
import org.riverock.webmill.admin.dao.DaoFactory;

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
        List<CompanyBean> companies = DaoFactory.getWebmillAdminDao().getCompanyList();

        for (CompanyBean companyBean : companies) {
            list.add(new SelectItem(companyBean.getId(), companyBean.getName()));
        }
        return list;
    }

    public List<UserBean> getPortalUserList() {
        List<UserBean> list = DaoFactory.getWebmillAdminDao().getUserList();
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
