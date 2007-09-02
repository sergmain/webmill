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

import java.util.List;

import org.riverock.interfaces.portal.bean.NewsGroup;
import org.riverock.interfaces.portal.bean.News;
import org.riverock.interfaces.portal.spi.PortalCmsNewsSpi;

/**
 * @deprecated use org.riverock.interfaces.portal.spi.PortalCmsNewsSpi
 * @author Sergei Maslyukov
 *         Date: 20.11.2006
 *         Time: 21:00:43
 *         <p/>
 *         $Id$
 */
public interface PortalCmsNewsDao {

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
