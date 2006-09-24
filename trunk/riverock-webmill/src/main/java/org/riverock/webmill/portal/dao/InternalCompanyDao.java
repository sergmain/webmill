/*
 * org.riverock.webmill - Portal framework implementation
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
package org.riverock.webmill.portal.dao;

import java.util.List;
import java.io.Serializable;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:24:19
 *         $Id$
 */
public interface InternalCompanyDao extends Serializable {
    
    public Company getCompany( String companyName );
    public Company getCompany( Long id, AuthSession authSession );

    public Long processAddCompany( Company companyBean, Long holdingId );
    public Long processAddCompany( Company companyBean, String userLogin, Long holdingId, AuthSession authSession );

    public void processSaveCompany( Company companyBean, AuthSession authSession );
    public void processDeleteCompany( Company companyBean, AuthSession authSession );

    public List<Company> getCompanyList( AuthSession authSession );

}
