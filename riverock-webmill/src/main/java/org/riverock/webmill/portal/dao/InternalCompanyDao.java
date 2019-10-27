/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
package org.riverock.webmill.portal.dao;

import java.util.List;
import java.io.Serializable;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:24:19
 *         $Id: InternalCompanyDao.java 1062 2006-11-21 18:27:47Z serg_main $
 */
public interface InternalCompanyDao extends Serializable {
    
    public Company getCompany( String companyName );
    public Company getCompany( Long id, AuthSession authSession );

    public Long processAddCompany( Company company, Long holdingId );
    public Long processAddCompany( Company company, String userLogin, Long holdingId, AuthSession authSession );

    public void processSaveCompany( Company company, AuthSession authSession );
    public void processDeleteCompany( Company company, AuthSession authSession );

    public List<Company> getCompanyList( AuthSession authSession );

    /**
     * Get list of company without restriction. Deleted company (isDeleted==true) not included.
     * @return list of company
     */
    public List<Company> getCompanyList_notRestricted();

    /**
     * Get company without restriction. Deleted company (isDeleted==true) not returned.
     * @param companyId PK of company
     * @return company
     */
    public Company getCompany_notRestricted( Long companyId);

    /**
     * Updatet company without restriction. Access right on this company not checked
     * @param company company
     */
    public void processSaveCompany_notRestricted( Company company);

    /**
     * Delete company without restriction. Access right on this company not checked
     * @param company company
     */
    public void processDeleteCompany_notRestricted( Company company );
}
