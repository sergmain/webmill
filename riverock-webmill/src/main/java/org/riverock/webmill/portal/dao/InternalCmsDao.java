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

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.interfaces.portal.bean.Article;
import org.riverock.interfaces.portal.bean.NewsGroup;
import org.riverock.interfaces.portal.bean.News;

/**
 * @author Sergei Maslyukov
 *         Date: 24.05.2006
 *         Time: 18:10:18
 */
public interface InternalCmsDao {
    public void deleteArticleForSite(DatabaseAdapter adapter, Long siteId);
    public void deleteArticleForSiteLanguage(DatabaseAdapter adapter, Long siteLanguageId);
    public void deleteNewsForSite(DatabaseAdapter adapter, Long siteId);
    public void deleteNewsForSiteLanguage(DatabaseAdapter adapter, Long siteLanguageId);

    List<Article> getArticleList(Long siteLanguageId, boolean isXml);

    Article getArticle(Long articleId);

    Long createArticle(Article article);

    void updateArticle(Article article);

    void deleteArticle(Long articleId);

    List<NewsGroup> getNewsGroupList(Long siteLanguageId);

    List<News> getNewsList(Long newsGroupId);

    NewsGroup getNewsGroup(Long newsGroupId);

    News getNews(Long newsId);

    Long createNews(News news);

    void updateNews(News news);

    void deleteNews(Long newsId);

    Long createNewsGroup(NewsGroup newsGroup);

    void deleteNewsGroup(Long newsGroupId);

    void updateNewsGroup(NewsGroup newsGroup);
    
}
