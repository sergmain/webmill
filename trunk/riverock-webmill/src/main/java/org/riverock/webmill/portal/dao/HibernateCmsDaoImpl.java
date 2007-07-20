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

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import org.riverock.common.exception.DatabaseException;
import org.riverock.interfaces.portal.bean.Article;
import org.riverock.interfaces.portal.bean.News;
import org.riverock.interfaces.portal.bean.NewsGroup;
import org.riverock.webmill.portal.bean.ArticleBean;
import org.riverock.webmill.portal.bean.NewsBean;
import org.riverock.webmill.portal.bean.NewsGroupBean;
import org.riverock.webmill.utils.HibernateUtils;

/**
 * User: SergeMaslyukov
 * Date: 19.11.2006
 * Time: 0:37:35
 * <p/>
 * $Id$
 */
public class HibernateCmsDaoImpl implements InternalCmsDao {
    private static Logger log = Logger.getLogger(HibernateCmsDaoImpl.class);

    public void deleteArticleForSite(Long siteId) {
        Session session = HibernateUtils.getSession();
        try {
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

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteArticleForSiteLanguage(Long siteLanguageId) {
        Session session = HibernateUtils.getSession();
        try {
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

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteNewsForSite(Long siteId) {
        Session session = HibernateUtils.getSession();
        try {
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

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteNewsForSiteLanguage(Long siteLanguageId) {
        Session session = HibernateUtils.getSession();
        try {
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

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public List<NewsGroup> getNewsGroupList(Long siteLanguageId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            List<NewsGroupBean> groupBeans = session.createQuery(
                "select newsGroup " +
                    "from  org.riverock.webmill.portal.bean.NewsGroupBean as newsGroup " +
                    "where newsGroup.isDeleted=false and newsGroup.siteLanguageId=:siteLanguageId")
                .setLong("siteLanguageId", siteLanguageId)
                .list();
            return (List)groupBeans;
        }
        finally {
            session.close();
        }
    }

    public List<News> getNewsList(Long newsGroupId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            List<NewsBean> beans = session.createQuery(
                "select news " +
                    "from  org.riverock.webmill.portal.bean.NewsBean news " +
                    "where news.isDeleted=false and news.newsGroupId=:newsGroupId " +
                    "order by news.postDate desc")
                .setLong("newsGroupId", newsGroupId)
                .list();
            for (NewsBean bean : beans) {
                Blob blob = bean.getNewsBlob();
                if (blob!=null) {
                    try {
                        bean.setNewsText( new String(blob.getBytes(1, (int)blob.length()), CharEncoding.UTF_8) );
                    }
                    catch (UnsupportedEncodingException e) {
                        String es = "Error get list of News";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                    catch (SQLException e) {
                        String es = "Error get news text";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                }
            }

            return (List)beans;
        }
        finally {
            session.close();
        }
    }

    public NewsGroup getNewsGroup(Long newsGroupId) {
        if (newsGroupId==null) {
            return null;
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            NewsGroupBean newsGroup = (NewsGroupBean)session.createQuery(
                "select newsGroup " +
                    "from  org.riverock.webmill.portal.bean.NewsGroupBean newsGroup " +
                    "where newsGroup.isDeleted=false and newsGroup.newsGroupId=:newsGroupId ")
                .setLong("newsGroupId", newsGroupId)
                .uniqueResult();
            return newsGroup;
        }
        finally {
            session.close();
        }
    }

    public News getNews(Long newsId) {
        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            NewsBean news = (NewsBean)session.createQuery(
                "select news " +
                    "from  org.riverock.webmill.portal.bean.NewsBean news " +
                    "where news.isDeleted=false and news.newsId=:newsId ")
                .setLong("newsId", newsId)
                .uniqueResult();
            if (news!=null) {
                Blob blob = news.getNewsBlob();
                if (blob!=null) {
                    try {
                        news.setNewsText( new String(blob.getBytes(1, (int)blob.length()), CharEncoding.UTF_8) );
                    }
                    catch (UnsupportedEncodingException e) {
                        String es = "Error get News";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                    catch (SQLException e) {
                        String es = "Error get news text";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                }
            }

            return news;
        }
        finally {
            session.close();
        }
    }

    public Long createNews(News news) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            NewsBean bean = new NewsBean(news);
            if (bean.getPostDate()==null) {
                bean.setPostDate(new Date());
            }
            if (StringUtils.isNotBlank(bean.getNewsText())) {
                try {
                    bean.setNewsBlob( Hibernate.createBlob(bean.getNewsText().getBytes(CharEncoding.UTF_8)));
                }
                catch (UnsupportedEncodingException e) {
                    String es = "Error create News";
                    log.error(es, e);
                    throw new DatabaseException(es, e);
                }
            }
            else {
                bean.setNewsBlob(null);
            }
            session.save(bean);

            session.flush();
            session.clear();
            session.getTransaction().commit();

            return bean.getNewsId();
        }
        finally {
            session.close();
        }
    }

    public void updateNews(News news) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            NewsBean bean = (NewsBean)session.createQuery(
                "select news " +
                    "from  org.riverock.webmill.portal.bean.NewsBean news " +
                    "where news.newsId=:newsId ")
                .setLong("newsId", news.getNewsId())
                .uniqueResult();
            if (bean!=null) {
                bean.setDeleted(news.isDeleted());
                bean.setNewsAnons(news.getNewsAnons());
                if (StringUtils.isNotBlank(news.getNewsText())) {
                    try {
                        bean.setNewsBlob( Hibernate.createBlob(news.getNewsText().getBytes(CharEncoding.UTF_8)));
                    }
                    catch (UnsupportedEncodingException e) {
                        String es = "Error update News";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                }
                else {
                    bean.setNewsBlob(null);
                }

                bean.setNewsGroupId(news.getNewsGroupId());
                bean.setNewsHeader(news.getNewsHeader());
                if (news.getPostDate()!=null)
                    bean.setPostDate(news.getPostDate());
                else
                    bean.setPostDate(new Date() );
            }

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteNews(Long newsId) {
        if (newsId==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();
            NewsBean news = (NewsBean)session.createQuery(
                "select news " +
                    "from  org.riverock.webmill.portal.bean.NewsBean news " +
                    "where news.isDeleted=false and news.newsId=:newsId ")
                .setLong("newsId", newsId)
                .uniqueResult();
            if (news!=null) {
                news.setDeleted(true);
            }

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public Long createNewsGroup(NewsGroup newsGroup) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            NewsGroupBean bean = new NewsGroupBean(newsGroup);
            session.save(bean);

            session.flush();
            session.clear();
            session.getTransaction().commit();

            return bean.getNewsGroupId();
        }
        finally {
            session.close();
        }
    }

    public void deleteNewsGroup(Long newsGroupId) {
        if (newsGroupId==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            NewsGroupBean newsGroup = (NewsGroupBean)session.createQuery(
                "select newsGroup " +
                    "from  org.riverock.webmill.portal.bean.NewsGroupBean newsGroup " +
                    "where newsGroup.isDeleted=false and newsGroup.newsGroupId=:newsGroupId ")
                .setLong("newsGroupId", newsGroupId)
                .uniqueResult();
            if (newsGroup!=null) {
                newsGroup.setDeleted(true);
            }

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void updateNewsGroup(NewsGroup newsGroup) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            NewsGroupBean bean = (NewsGroupBean)session.createQuery(
                "select newsGroup " +
                    "from  org.riverock.webmill.portal.bean.NewsGroupBean newsGroup " +
                    "where newsGroup.isDeleted=false and newsGroup.newsGroupId=:newsGroupId ")
                .setLong("newsGroupId", newsGroup.getNewsGroupId())
                .uniqueResult();
            if (bean!=null) {
                bean.setCountNewsPerGroup(newsGroup.getCountNewsPerGroup());
                bean.setDeleted(newsGroup.isDeleted());
                bean.setNewsGroupCode(newsGroup.getNewsGroupCode());
                bean.setNewsGroupName(newsGroup.getNewsGroupName());
                bean.setOrderField(newsGroup.getOrderField());
                bean.setSiteLanguageId(newsGroup.getSiteLanguageId());
            }

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public Article getArticleByCode(Long siteLanguageId, String articleCode) {
        if (siteLanguageId==null || articleCode==null) {
            return null;
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            ArticleBean article = (ArticleBean)session.createQuery(
                "select article " +
                    "from  org.riverock.webmill.portal.bean.ArticleBean as article " +
                    "where article.isDeleted=false and article.articleCode=:articleCode and " +
                    "      article.siteLanguageId=:siteLanguageId")
                .setString("articleCode", articleCode)
                .setLong("siteLanguageId", siteLanguageId)
                .uniqueResult();
            if (article!=null) {
                Blob blob = article.getArticleBlob();
                if (blob!=null) {
                    try {
                        article.setArticleData( new String(blob.getBytes(1, (int)blob.length()), CharEncoding.UTF_8) );
                    }
                    catch (UnsupportedEncodingException e) {
                        String es = "Error get Article by code.";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                    catch (SQLException e) {
                        String es = "Error get article data";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                }
            }

            return article;
        }
        finally {
            session.close();
        }
    }

    public List<Article> getArticleList(Long siteLanguageId, boolean isXml) {
        List<Article> list = new ArrayList<Article>();
        if (siteLanguageId==null) {
            return list;
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            List<ArticleBean> beans = session.createQuery(
                "select article " +
                    "from org.riverock.webmill.portal.bean.ArticleBean as article " +
                    "where article.isDeleted=false and article.isPlain=:isPlain and article.siteLanguageId=:siteLanguageId")
                .setBoolean("isPlain", !isXml)
                .setLong("siteLanguageId", siteLanguageId)
                .list();

            for (ArticleBean article : beans) {
                Blob blob = article.getArticleBlob();
                if (blob!=null) {
                    try {
                        article.setArticleData( new String(blob.getBytes(1, (int)blob.length()), CharEncoding.UTF_8) );
                    }
                    catch (UnsupportedEncodingException e) {
                        String es = "Error get list of Articles";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                    catch (SQLException e) {
                        String es = "Error get article data";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                }
            }
            return (List)beans;
        }
        finally {
            session.close();
        }
    }

    public Article getArticle(Long articleId) {
        if (articleId==null) {
            return null;
        }

        StatelessSession session = HibernateUtils.getStatelessSession();
        try {
            ArticleBean article = (ArticleBean)session.createQuery(
                "select article " +
                    "from  org.riverock.webmill.portal.bean.ArticleBean as article " +
                    "where article.isDeleted=false and article.articleId=:articleId")
                .setLong("articleId", articleId)
                .uniqueResult();
            if (article!=null) {
                Blob blob = article.getArticleBlob();
                if (blob!=null) {
                    try {
                        article.setArticleData( new String(blob.getBytes(1, (int)blob.length()), CharEncoding.UTF_8) );
                    }
                    catch (UnsupportedEncodingException e) {
                        String es = "Error get Article";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                    catch (SQLException e) {
                        String es = "Error get article data";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                }
            }

            return article;
        }
        finally {
            session.close();
        }
    }

    public Long createArticle(Article article) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            ArticleBean bean = new ArticleBean(article);
            if (bean.getPostDate()==null) {
                bean.setPostDate(new Date());
            }
            if (StringUtils.isNotBlank(bean.getArticleData())) {
                try {
                    bean.setArticleBlob( Hibernate.createBlob(bean.getArticleData().getBytes(CharEncoding.UTF_8)));
                }
                catch (UnsupportedEncodingException e) {
                    String es = "Error create Article";
                    log.error(es, e);
                    throw new DatabaseException(es, e);
                }
            }
            else {
                bean.setArticleBlob(null);
            }

            session.save(bean);

            session.flush();
            session.clear();
            session.getTransaction().commit();

            return bean.getArticleId();
        }
        finally {
            session.close();
        }
    }

    public void updateArticle(Article article) {
        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            ArticleBean bean = (ArticleBean)session.createQuery(
                "select article " +
                    "from  org.riverock.webmill.portal.bean.ArticleBean as article " +
                    "where article.articleId=:articleId ")
                .setLong("articleId", article.getArticleId())
                .uniqueResult();

            if (bean!=null) {
                bean.setUserId(article.getUserId());
                bean.setArticleCode(article.getArticleCode());
                bean.setArticleName(article.getArticleName());
                bean.setPostDate(article.getPostDate());
                bean.setPlain(article.isPlain());
                bean.setPostDate(article.getPostDate());
                bean.setPlain(article.isPlain());
                bean.setSiteLanguageId(article.getSiteLanguageId());
                if (StringUtils.isNotBlank(article.getArticleData())) {
                    try {
                        bean.setArticleBlob( Hibernate.createBlob(article.getArticleData().getBytes(CharEncoding.UTF_8)));
                    }
                    catch (UnsupportedEncodingException e) {
                        String es = "Error update Article";
                        log.error(es, e);
                        throw new DatabaseException(es, e);
                    }
                }
                else {
                    bean.setArticleBlob(null);
                }
            }

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }

    public void deleteArticle(Long articleId) {
        if (articleId==null) {
            return;
        }

        Session session = HibernateUtils.getSession();
        try {
            session.beginTransaction();

            ArticleBean bean = (ArticleBean)session.createQuery(
                "select article " +
                    "from  org.riverock.webmill.portal.bean.ArticleBean as article " +
                    "where article.articleId=:articleId ")
                .setLong("articleId", articleId)
                .uniqueResult();

            if (bean!=null) {
                session.delete(bean);
            }

            session.flush();
            session.clear();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }
}
