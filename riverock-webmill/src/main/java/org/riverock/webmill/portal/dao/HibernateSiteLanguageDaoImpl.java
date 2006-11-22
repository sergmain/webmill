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

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.webmill.portal.bean.CatalogBean;
import org.riverock.webmill.portal.bean.CatalogLanguageBean;
import org.riverock.webmill.portal.bean.PortalXsltBean;
import org.riverock.webmill.portal.bean.SiteLanguageBean;
import org.riverock.webmill.portal.bean.TemplateBean;
import org.riverock.webmill.portal.bean.ArticleBean;
import org.riverock.webmill.portal.bean.NewsBean;
import org.riverock.webmill.portal.bean.NewsGroupBean;
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
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Query query = session.createQuery(
            "select siteLanguage from org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
            "where siteLanguage.siteId = :site_id");
        query.setLong("site_id", siteId);
        List<SiteLanguageBean> cssList = query.list();
        session.getTransaction().commit();
        return (List)cssList;
    }

    public SiteLanguage getSiteLanguage(Long siteLanguageId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        Query query = session.createQuery(
            "select siteLanguage from org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
            "where siteLanguage.siteLanguageId = :site_language_id");
        query.setLong("site_language_id", siteLanguageId);
        SiteLanguageBean bean = (SiteLanguageBean)query.uniqueResult();
        session.getTransaction().commit();
        return bean;
    }

    public SiteLanguage getSiteLanguage(Long siteId, String languageLocale) {
        Session session = HibernateUtils.getSession();
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

    public Long createSiteLanguage(SiteLanguage siteLanguage) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        SiteLanguageBean bean = new SiteLanguageBean(siteLanguage);
        session.save(bean);
        session.flush();
        session.getTransaction().commit();
        return bean.getSiteLanguageId();
    }

    public void updateSiteLanguage(SiteLanguage siteLanguage) {
        Session session = HibernateUtils.getSession();
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

    public void deleteSiteLanguage(Long siteLanguageId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();

/*
        InternalDaoFactory.getInternalCmsDao().deleteArticleForSiteLanguage(dbDyn, siteLanguageId);
        InternalDaoFactory.getInternalCmsDao().deleteNewsForSiteLanguage(dbDyn, siteLanguageId);
        InternalDaoFactory.getInternalTemplateDao().deleteTemplateForSiteLanguage(dbDyn, siteLanguageId);
*/

        deleteSiteLanguage(session, siteLanguageId);

        session.getTransaction().commit();
    }

    public void deleteSiteLanguageForSite(DatabaseAdapter adapter, Long siteId) {
        Session session = HibernateUtils.getSession();
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

    private static void deleteSiteLanguage(Session session, Long siteLanguageId) {
        List<ArticleBean> articleBeans = session.createQuery(
            "select article " +
                "from org.riverock.webmill.portal.bean.ArticleBean as article " +
                "where article.siteLanguageId=:siteLanguageId")
            .setLong("siteLanguageId", siteLanguageId)
            .list();

        for (ArticleBean articleBean : articleBeans) {
            session.delete(articleBean);
        }

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

        List<NewsGroupBean> groupBeans = session.createQuery(
            "select newsGroup " +
                "from  org.riverock.webmill.portal.bean.NewsGroupBean newsGroup " +
                "where newsGroup.siteLanguageId=:siteLanguageId")
            .setLong("siteLanguageId", siteLanguageId)
            .list();

        for (NewsGroupBean newsGroupBean : groupBeans) {
            session.delete(newsGroupBean);
        }

        List<TemplateBean> templateBeans = session.createQuery(
            "select template " +
                "from  org.riverock.webmill.portal.bean.TemplateBean as template " +
                "where template.siteLanguageId=:siteLanguageId")
            .setLong("siteLanguageId", siteLanguageId)
            .list();
        for (TemplateBean templateBean : templateBeans) {
            session.delete(templateBean);
        }

        List<CatalogBean> catalogItems = session.createQuery(
            "select catalog " +
                "from  org.riverock.webmill.portal.bean.CatalogBean as catalog, " +
                "      org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang " +
                "where catalog.catalogLanguageId=catalogLang.catalogLanguageId and " +
                "      catalogLang.siteLanguageId=:siteLanguageId")
            .setLong("siteLanguageId", siteLanguageId)
            .list();
        for (CatalogBean catalogItem : catalogItems) {
            session.delete(catalogItem);
        }

        List<CatalogLanguageBean> catalogLanguageBeans = session.createQuery(
            "select catalogLang " +
                "from  org.riverock.webmill.portal.bean.CatalogLanguageBean catalogLang " +
                "where catalogLang.siteLanguageId=:siteLanguageId")
            .setLong("siteLanguageId", siteLanguageId)
            .list();
        for (CatalogLanguageBean catalogLanguageBean : catalogLanguageBeans) {
            session.delete(catalogLanguageBean);
        }

        List<PortalXsltBean> xsltList = session.createQuery(
            "select xslt from org.riverock.webmill.portal.bean.PortalXsltBean as xslt " +
                "where xslt.siteLanguageId = :siteLanguageId")
            .setLong("siteLanguageId", siteLanguageId)
            .list();
        for (PortalXsltBean portalXsltBean : xsltList) {
            session.delete(portalXsltBean);
        }

        Query query = session.createQuery(
            "select siteLanguage from org.riverock.webmill.portal.bean.SiteLanguageBean as siteLanguage " +
            "where siteLanguage.siteLanguageId = :site_language_id");
        query.setLong("site_language_id", siteLanguageId);
        SiteLanguageBean bean = (SiteLanguageBean)query.uniqueResult();
        session.delete(bean);
    }
}