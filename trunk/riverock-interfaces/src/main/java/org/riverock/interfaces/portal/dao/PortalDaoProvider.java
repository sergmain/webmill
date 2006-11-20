/*
 * org.riverock.interfaces - Common classes and interafces shared between projects
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
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
    public PortalCmsArticleDao getPortalCmsArticleDao();
    public PortalCmsNewsDao getPortalCmsNewsDao();
}
