/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
package org.riverock.portlet.manager.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author SergeMaslyukov
 *
 */
public class CompanyService implements Serializable {
    private static final long serialVersionUID = 2055005515L;

	public CompanyService() {
	}

	public List<Company> getCompanyList() {
		List<Company> list = FacesTools.getPortalDaoProvider().getPortalCompanyDao().getCompanyList();
		if (list==null) {
			return null;
		}
		
		Iterator<Company> iterator = list.iterator();
		List<Company> companies = new ArrayList<Company>();
		while(iterator.hasNext()) {
			Company company = iterator.next();
            CompanyBean bean = new CompanyBean(company);
            if (StringUtils.isBlank(bean.getName())) {
                bean.setName("<empty company name>");
            }
            companies.add( bean );
		}
		return companies;
	}
}
