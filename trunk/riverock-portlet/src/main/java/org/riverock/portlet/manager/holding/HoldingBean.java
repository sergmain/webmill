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
package org.riverock.portlet.manager.holding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.riverock.interfaces.portal.bean.Holding;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 20:53:12
 *         $Id$
 */
public class HoldingBean implements Serializable, Holding {
    private static final long serialVersionUID = 2043005504L;

    private String holdingName = null;
    private String holdingShortName = null;
    private Long holdingId = null;
    private List<CompanyBean> companies = new ArrayList<CompanyBean>();

    public HoldingBean() {
    }

    public HoldingBean( Holding holding ) {
    this.holdingName = holding.getName();
    this.holdingShortName = holding.getShortName();
    this.holdingId = holding.getId();
    }

    private Long newCompanyId = null;

    public Long getNewCompanyId() {
        return newCompanyId;
    }

    public void setNewCompanyId( Long newCompanyId ) {
        this.newCompanyId = newCompanyId;
    }

    public Long getId() {
        return holdingId;
    }

    public void setId( Long holdingId ) {
        this.holdingId = holdingId;
    }

    public String getName() {
        return holdingName;
    }

    public void setName( String holdingName ) {
        this.holdingName = holdingName;
    }

    public String getShortName() {
        return holdingShortName;
    }

    public void setShortName( String holdingShortName) {
        this.holdingShortName = holdingShortName;
    }

    public List<CompanyBean> getCompanies() {
        return companies;
    }

    public void setCompanies( List<CompanyBean> companies ) {
        this.companies = companies;
    }

    public void addCompany( CompanyBean company ) {
        companies.add( company );
    }

    public List<Long> getCompanyIdList() {
        List<Long> list = new ArrayList<Long>();
        if (companies==null) {
            return list;
        }

        for (CompanyBean company : companies) {
            list.add(company.getId());
        }
        return list;
    }
}
