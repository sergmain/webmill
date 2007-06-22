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

import org.hibernate.Query;
import org.hibernate.Session;

import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.webmill.portal.bean.CatalogBean;
import org.riverock.webmill.portal.bean.NewsBean;
import org.riverock.webmill.portal.bean.SiteLanguageBean;
import org.riverock.webmill.utils.HibernateUtils;

/**
 * @author Sergei Maslyukov
 *         Date: 08.11.2006
 *         Time: 17:35:14
 *         <p/>
 *         $Id$
 */
public class HibernateSiteLanguageDaoImpl implements InternalSiteLanguageDao {
    public List<SiteLanguage> getSiteLanguageList(Long siteId) {
        if (siteId==null) {
            throw new IllegalArgumentException("siteId is null");
        }
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            List<SiteLanguageBean> cssList = (List)session.createQuery(
                "select siteLanguage from org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
                "where siteLanguage.siteId = :site_id")
                .setLong("site_id", siteId)
                .list();
            session.getTransaction().commit();
            return (List)cssList;
        }
        finally {
            session.close();
        }
    }

    public SiteLanguage getSiteLanguage(Long siteLanguageId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery(
                "select siteLanguage from org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
                "where siteLanguage.siteLanguageId = :site_language_id");
            query.setLong("site_language_id", siteLanguageId);
            SiteLanguageBean bean = (SiteLanguageBean)query.uniqueResult();
            session.getTransaction().commit();
            return bean;
        }
        finally {
            session.close();
        }
    }

    public SiteLanguage getSiteLanguage(Long siteId, String languageLocale) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery(
                "select siteLanguage from org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
                "where siteLanguage.siteId = :site_id and siteLanguage.customLanguage = :custom_language ");
            query.setLong("site_id", siteId);
            query.setString("custom_language", languageLocale);
            SiteLanguageBean bean = (SiteLanguageBean)query.uniqueResult();
            session.getTransaction().commit();
            return bean;
        }
        finally {
            session.close();
        }
    }

    public Long createSiteLanguage(SiteLanguage siteLanguage) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            SiteLanguageBean bean = new SiteLanguageBean(siteLanguage);
            session.save(bean);
            session.flush();
            session.getTransaction().commit();
            return bean.getSiteLanguageId();
        }
        finally {
            session.close();
        }
    }

    public void updateSiteLanguage(SiteLanguage siteLanguage) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            Query query = session.createQuery(
                "select siteLanguage from org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
                "where siteLanguage.siteLanguageId = :site_language_id");
            query.setLong("site_language_id", siteLanguage.getSiteLanguageId());
            SiteLanguageBean bean = (SiteLanguageBean)query.uniqueResult();
            if (bean!=null) {
                bean.setCustomLanguage(siteLanguage.getCustomLanguage());
                bean.setNameCustomLanguage(siteLanguage.getNameCustomLanguage());
                bean.setSiteId(siteLanguage.getSiteId());
            }
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteSiteLanguage(Long siteLanguageId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            deleteSiteLanguage(session, siteLanguageId);

            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteSiteLanguageForSite(Long siteId) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            Query query = session.createQuery(
                "select siteLanguage from org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
                "where siteLanguage.siteId = :site_id");
            query.setLong("site_id", siteId);
            List<SiteLanguageBean> list = query.list();
            for (SiteLanguageBean bean : list) {
                deleteSiteLanguage(session, bean.getSiteLanguageId());
                session.delete(bean);
            }
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    private static void deleteSiteLanguage(Session session, Long siteLanguageId) {
        session.createQuery(
            "delete org.riverock.webmill.portal.bean.ArticleBean article " +
                "where article.siteLanguageId=:siteLanguageId")
            .setLong("siteLanguageId", siteLanguageId)
            .executeUpdate();

        List<NewsBean> beans = session.createQuery(
            "select news " +
                "from  org.riverock.webmill.portal.bean.NewsBean news, " +
                "      org.riverock.webmill.portal.bean.NewsGroupBean newsGroup " +
                "where news.newsGroupId=newsGroup.newsGroupId and newsGroup.siteLanguageId=:siteLanguageId")
            .setLong("siteLanguageId", siteLanguageId)
            .list();

        for (NewsBean newsBean : beans) {
            session.delete(newsBean);
        }

        session.createQuery(
            "delete org.riverock.webmill.portal.bean.NewsGroupBean newsGroup " +
                "where newsGroup.siteLanguageId=:siteLanguageId")
            .setLong("siteLanguageId", siteLanguageId)
            .executeUpdate();

        session.createQuery(
            "delete org.riverock.webmill.portal.bean.TemplateBean template " +
                "where template.siteLanguageId=:siteLanguageId")
            .setLong("siteLanguageId", siteLanguageId)
            .executeUpdate();

        List<CatalogBean> catalogItems = session.createQuery(
            "select catalog " +
                "from  org.riverock.webmill.portal.bean.CatalogBean catalog, " +
                "      org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang " +
                "where catalog.catalogLanguageId=catalogLang.catalogLanguageId and " +
                "      catalogLang.siteLanguageId=:siteLanguageId")
            .setLong("siteLanguageId", siteLanguageId)
            .list();
        for (CatalogBean catalogItem : catalogItems) {
            session.delete(catalogItem);
        }

        session.createQuery(
            "delete org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang " +
                "where catalogLang.siteLanguageId=:siteLanguageId")
            .setLong("siteLanguageId", siteLanguageId)
            .executeUpdate();

        session.createQuery(
            "delete org.riverock.webmill.portal.bean.PortalXsltBean xslt " +
                "where xslt.siteLanguageId = :siteLanguageId")
            .setLong("siteLanguageId", siteLanguageId)
            .executeUpdate();

        session.createQuery(
            "delete org.riverock.webmill.portal.bean.SiteLanguageBean siteLanguage " +
            "where siteLanguage.siteLanguageId = :site_language_id")
            .setLong("site_language_id", siteLanguageId)
            .executeUpdate();
    }
}
