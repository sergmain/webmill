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

/**
 * @author SergeMaslyukov
 *         Date: 05.12.2005
 *         Time: 20:21:34
 *         $Id$
 */
public class InternalDaoFactory {
    private static InternalCssDao internalCssDao = new HibernateCssDaoImpl();
    private static InternalXsltDao internalXsltDao = new HibernateXsltDaoImpl();
    private static InternalSiteDao internalSiteDao = new HibernateSiteDaoImpl();
    private static InternalSiteLanguageDao internalSiteLanguageDao = new HibernateSiteLanguageDaoImpl();
    private static InternalVirtualHostDao internalVirtualHostDao = new HibernateVirtualHostDaoImpl();
    private static InternalPortletNameDao internalPortletNameDao = new HibernatePortletNameDaoImpl();
    private static InternalAuthDao internalAuthDao = new HibernateAuthDaoImpl();
    private static InternalCompanyDao internalCompanyDao = new HibernateCompanyDaoImpl();
    private static InternalHoldingDao internalHoldingDao = new HibernateHoldingDaoImpl();
    private static InternalUserMetadataDao internalUserMetadataDao = new HibernateUserMetadataDaoImpl();
    private static InternalUserDao internalUserDao = new HibernateUserDaoImpl();
    private static InternalDao internalDao = new HibernateDaoImpl();
    private static InternalCatalogDao internalCatalogDao = new HibernateCatalogDaoImpl();
    private static InternalPreferencesDao internalPreferencesDao = new HibernatePreferencesDaoImpl();
    private static InternalTemplateDao internalTemplateDao = new HibernateTemplateDaoImpl();
    private static InternalCmsDao internalCmsDao = new HibernateCmsDaoImpl();

    public static InternalPreferencesDao getInternalPreferencesDao() {
        return internalPreferencesDao;
    }

    public static InternalUserDao getInternalUserDao() {
        return internalUserDao;
    }

    public static InternalUserMetadataDao getInternalUserMetadataDao() {
        return internalUserMetadataDao;
    }

    public static InternalCmsDao getInternalCmsDao() {
        return internalCmsDao;
    }

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

    static void setInternalAuthDao(InternalAuthDao internalAuthDao) {
        InternalDaoFactory.internalAuthDao = internalAuthDao;
    }

    static void setInternalCatalogDao(InternalCatalogDao internalCatalogDao) {
        InternalDaoFactory.internalCatalogDao = internalCatalogDao;
    }

    static void setInternalCmsDao(InternalCmsDao internalCmsDao) {
        InternalDaoFactory.internalCmsDao = internalCmsDao;
    }

    static void setInternalCompanyDao(InternalCompanyDao internalCompanyDao) {
        InternalDaoFactory.internalCompanyDao = internalCompanyDao;
    }

    static void setInternalCssDao(InternalCssDao internalCssDao) {
        InternalDaoFactory.internalCssDao = internalCssDao;
    }

    static void setInternalDao(InternalDao internalDao) {
        InternalDaoFactory.internalDao = internalDao;
    }

    static void setInternalHoldingDao(InternalHoldingDao internalHoldingDao) {
        InternalDaoFactory.internalHoldingDao = internalHoldingDao;
    }

    static void setInternalPortletNameDao(InternalPortletNameDao internalPortletNameDao) {
        InternalDaoFactory.internalPortletNameDao = internalPortletNameDao;
    }

    static void setInternalPreferencesDao(InternalPreferencesDao internalPreferencesDao) {
        InternalDaoFactory.internalPreferencesDao = internalPreferencesDao;
    }

    static void setInternalSiteDao(InternalSiteDao internalSiteDao) {
        InternalDaoFactory.internalSiteDao = internalSiteDao;
    }

    static void setInternalSiteLanguageDao(InternalSiteLanguageDao internalSiteLanguageDao) {
        InternalDaoFactory.internalSiteLanguageDao = internalSiteLanguageDao;
    }

    static void setInternalTemplateDao(InternalTemplateDao internalTemplateDao) {
        InternalDaoFactory.internalTemplateDao = internalTemplateDao;
    }

    static void setInternalUserDao(InternalUserDao internalUserDao) {
        InternalDaoFactory.internalUserDao = internalUserDao;
    }

    static void setInternalUserMetadataDao(InternalUserMetadataDao internalUserMetadataDao) {
        InternalDaoFactory.internalUserMetadataDao = internalUserMetadataDao;
    }

    static void setInternalVirtualHostDao(InternalVirtualHostDao internalVirtualHostDao) {
        InternalDaoFactory.internalVirtualHostDao = internalVirtualHostDao;
    }

    static void setInternalXsltDao(InternalXsltDao internalXsltDao) {
        InternalDaoFactory.internalXsltDao = internalXsltDao;
    }
}
