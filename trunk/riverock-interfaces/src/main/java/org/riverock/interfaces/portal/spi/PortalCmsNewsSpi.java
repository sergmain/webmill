package org.riverock.interfaces.portal.spi;

import java.util.List;

import org.riverock.interfaces.portal.bean.NewsGroup;
import org.riverock.interfaces.portal.bean.News;
import org.riverock.interfaces.portal.dao.PortalCmsNewsDao;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:44:34
 * $Id$
 */
public interface PortalCmsNewsSpi extends PortalCmsNewsDao {

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
