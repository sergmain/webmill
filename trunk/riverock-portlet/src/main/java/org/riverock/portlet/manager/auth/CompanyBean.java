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
package org.riverock.portlet.manager.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SergeMaslyukov
 *         Date: 02.01.2006
 *         Time: 20:53:12
 *         $Id$
 */
public class CompanyBean implements Serializable {
    private static final long serialVersionUID = 2043005504L;

    private String companyName = null;
    private Long companyId = null;
    private List<AuthUserExtendedInfoImpl> userBeans = new ArrayList<AuthUserExtendedInfoImpl>();

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId( Long companyId ) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName( String companyName ) {
        this.companyName = companyName;
    }

    public List<AuthUserExtendedInfoImpl> getUserBeans() {
        return userBeans;
    }

    public void setUserBeans( List<AuthUserExtendedInfoImpl> userBeans ) {
        this.userBeans = userBeans;
    }

    public void addUserBeans( AuthUserExtendedInfoImpl userBean ) {
        userBeans.add( userBean );
    }
}
