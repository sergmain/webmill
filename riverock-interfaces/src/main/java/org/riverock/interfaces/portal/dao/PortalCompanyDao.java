/*
 * org.riverock.interfaces - Common classes and interafces shared between projects
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
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
package org.riverock.interfaces.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.Company;

/**
 * @author SergeMaslyukov
 *         Date: 30.01.2006
 *         Time: 1:48:52
 *         $Id$
 */
public interface PortalCompanyDao {
    public Company getCompany( Long id );
    public List<Company> getCompanyList();
    public Long processAddCompany( Company companyBean, String userLogin, Long holdingId );
    public void processSaveCompany( Company companyBean );
    public void processDeleteCompany( Company companyBean );
}
