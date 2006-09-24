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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import org.riverock.webmill.admin.bean.CompanyBean;
import org.riverock.webmill.admin.dao.DaoFactory;

/**
 * @author SergeMaslyukov
 *         Date: 13.07.2006
 *         Time: 21:55:44
 *         $Id: PortalUserSessionBean.java 753 2006-07-10 07:53:57Z serg_main $
 */
public class CompanyService implements Serializable {
    private static final long serialVersionUID = 2055005515L;

    public CompanyService() {
    }

    public List<CompanyBean> getCompanyList() {
        List<CompanyBean> list = DaoFactory.getWebmillAdminDao().getCompanyList();
        if (list==null) {
            return null;
        }

        Iterator<CompanyBean> iterator = list.iterator();
        List<CompanyBean> companies = new ArrayList<CompanyBean>();
        while(iterator.hasNext()) {
            CompanyBean company = iterator.next();
            CompanyBean bean = new CompanyBean(company);
            if (StringUtils.isBlank(bean.getName())) {
                bean.setName("<empty company name>");
            }
            companies.add( bean );
        }
        return companies;
    }
}
