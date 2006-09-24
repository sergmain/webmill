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

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 0:44:37
 *         $Id$
 */
public interface PortalDaoProvider {
    public PortalCommonDao getPortalCommonDao();
    public PortalAuthDao getPortalAuthDao();
    public PortalCompanyDao getPortalCompanyDao();
    public PortalHoldingDao getPortalHoldingDao();

    public PortalCatalogDao getPortalCatalogDao();
    public PortalPortletNameDao getPortalPortletNameDao();
    public PortalSiteDao getPortalSiteDao();
    public PortalSiteLanguageDao getPortalSiteLanguageDao();
    public PortalTemplateDao getPortalTemplateDao();
    public PortalVirtualHostDao getPortalVirtualHostDao();
    public PortalXsltDao getPortalXsltDao();

    public PortalCssDao getPortalCssDao();
    public PortalUserMetadataDao getPortalUserMetadataDao();
    public PortalUserDao getPortalUserDao();
}
