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

/**
 * @author SergeMaslyukov
 *         Date: 05.12.2005
 *         Time: 20:21:34
 *         $Id$
 */
public class InternalDaoFactory {
    public static InternalDao internalDao = new InternalDaoImpl();
    public static InternalCompanyDao internalCompanyDao = new InternalCompanyDaoImpl();
    public static InternalHoldingDao internalHoldingDao = new InternalHoldingDaoImpl();
    public static InternalAuthDao internalAuthDao = new InternalAuthDaoImpl();
    public static InternalSiteDao internalSiteDao = new InternalSiteDaoImpl();
    public static InternalXsltDao internalXsltDao = new InternalXsltDaoImpl();
    public static InternalVirtualHostDao internalVirtualHostDao = new InternalVirtualHostDaoImpl();
    public static InternalSiteLanguageDao internalSiteLanguageDao = new InternalSiteLanguageDaoImpl();
    public static InternalTemplateDao internalTemplateDao = new InternalTemplateDaoImpl();
    public static InternalPortletNameDao internalPortletNameDao = new InternalPortletNameDaoImpl();
    public static InternalCatalogDao internalCatalogDao = new InternalCatalogDaoImpl();
    public static InternalCssDao internalCssDao = new InternalCssDaoImpl();

    public static InternalCssDao getInternalCssDao() {
        return internalCssDao;
    }

    public static InternalCatalogDao getInternalCatalogDao() {
        return internalCatalogDao;
    }

    public static InternalPortletNameDao getInternalPortletNameDao() {
        return internalPortletNameDao;
    }

    public static InternalTemplateDao getInternalTemplateDao() {
        return internalTemplateDao;
    }

    public static InternalSiteLanguageDao getInternalSiteLanguageDao() {
        return internalSiteLanguageDao;
    }

    public static InternalVirtualHostDao getInternalVirtualHostDao() {
        return internalVirtualHostDao;
    }

    public static InternalXsltDao getInternalXsltDao() {
        return internalXsltDao;
    }

    public static InternalSiteDao getInternalSiteDao() {
        return internalSiteDao;
    }

    public static InternalDao getInternalDao() {
        return internalDao;
    }

    public static InternalCompanyDao getInternalCompanyDao() {
        return internalCompanyDao;
    }

    public static InternalHoldingDao getInternalHoldingDao() {
        return internalHoldingDao;
    }

    public static InternalAuthDao getInternalAuthDao() {
        return internalAuthDao;
    }
}
