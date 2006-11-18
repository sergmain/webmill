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

import org.hibernate.Session;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.webmill.portal.bean.ArticleBean;
import org.riverock.webmill.portal.bean.NewsBean;
import org.riverock.webmill.portal.bean.NewsGroupBean;
import org.riverock.webmill.utils.HibernateUtils;

import java.util.List;

/**
 * User: SergeMaslyukov
 * Date: 19.11.2006
 * Time: 0:37:35
 * <p/>
 * $Id$
 */
public class HibernateCmsDaoImpl implements InternalCmsDao {
    
    public void deleteArticleForSite(DatabaseAdapter adapter, Long siteId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List<ArticleBean> beans = session.createQuery(
            "select article " +
                "from org.riverock.webmill.portal.bean.ArticleBean as article, " +
                "     org.riverock.webmill.portal.bean.SiteBean site " +
                "where article.siteLanguageId=site.siteLanguageId and site.siteId=:siteId")
            .setLong("siteId", siteId)
            .list();

        for (ArticleBean articleBean : beans) {
            session.delete(articleBean);
        }
        session.getTransaction().commit();
    }

    public void deleteArticleForSiteLanguage(DatabaseAdapter adapter, Long siteLanguageId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List<ArticleBean> beans = session.createQuery(
            "select article " +
                "from org.riverock.webmill.portal.bean.ArticleBean as article " +
                "where article.siteLanguageId=:siteLanguageId")
            .setLong("siteLanguageId", siteLanguageId)
            .list();

        for (ArticleBean articleBean : beans) {
            session.delete(articleBean);
        }
        session.getTransaction().commit();
    }

    public void deleteNewsForSite(DatabaseAdapter adapter, Long siteId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
        List<NewsBean> beans = session.createQuery(
            "select news " +
                "from  org.riverock.webmill.portal.bean.NewsBean news, " +
                "      org.riverock.webmill.portal.bean.NewsGroupBean newsGroup, " +
                "      org.riverock.webmill.portal.bean.SiteBean site " +
                "where news.newsGroupId=newsGroup.newsGroupId and " +
                "      newsGroup.siteLanguageId=site.siteLanguageId and site.siteId=:siteId")
            .setLong("siteId", siteId)
            .list();

        for (NewsBean newsBean : beans) {
            session.delete(newsBean);
        }

        List<NewsGroupBean> groupBeans = session.createQuery(
            "select newsGroup " +
                "from  org.riverock.webmill.portal.bean.NewsGroupBean newsGroup, " +
                "      org.riverock.webmill.portal.bean.SiteBean site " +
                "where newsGroup.siteLanguageId=site.siteLanguageId and site.siteId=:siteId")
            .setLong("siteId", siteId)
            .list();

        for (NewsGroupBean newsGroupBean : groupBeans) {
            session.delete(newsGroupBean);
        }

        session.getTransaction().commit();
    }

    public void deleteNewsForSiteLanguage(DatabaseAdapter adapter, Long siteLanguageId) {
        Session session = HibernateUtils.getSession();
        session.beginTransaction();
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

        session.getTransaction().commit();
    }
}
