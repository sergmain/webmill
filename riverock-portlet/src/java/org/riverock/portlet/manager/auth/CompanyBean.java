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
