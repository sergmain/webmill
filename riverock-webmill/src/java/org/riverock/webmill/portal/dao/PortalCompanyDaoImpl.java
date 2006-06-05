/*
 * org.riverock.webmill -- Portal framework implementation
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
package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.dao.PortalCompanyDao;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:59:09
 *         $Id$
 */
public class PortalCompanyDaoImpl implements PortalCompanyDao {
    private AuthSession authSession = null;

    PortalCompanyDaoImpl(AuthSession authSession) {
        this.authSession = authSession;
    }

    public Company getCompany(Long id) {
        return InternalDaoFactory.getInternalCompanyDao().getCompany( id, authSession );
    }

    public List<Company> getCompanyList() {
        return InternalDaoFactory.getInternalCompanyDao().getCompanyList( authSession );
    }

    public Long processAddCompany(Company companyBean, String userLogin, Long holdingId) {
        return InternalDaoFactory.getInternalCompanyDao().processAddCompany( companyBean, userLogin, holdingId, authSession );
    }

    public void processSaveCompany(Company companyBean) {
        InternalDaoFactory.getInternalCompanyDao().processSaveCompany( companyBean, authSession );
    }

    public void processDeleteCompany(Company companyBean) {
        InternalDaoFactory.getInternalCompanyDao().processDeleteCompany( companyBean, authSession );
    }
}
