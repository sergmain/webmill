/*
 * org.riverock.webmill.admin - Webmill portal admin web application
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
        List<CompanyBean> list = DaoFactory.getWebmillInitDao().getCompanyList();
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
