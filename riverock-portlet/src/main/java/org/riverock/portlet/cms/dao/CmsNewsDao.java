package org.riverock.portlet.cms.dao;

import java.util.List;

import org.riverock.portlet.cms.news.bean.NewsGroupBean;
import org.riverock.portlet.cms.news.bean.NewsBean;

/**
 * @author Sergei Maslyukov
 *         Date: 23.08.2006
 *         Time: 16:28:05
 */
public interface CmsNewsDao {

    List<NewsGroupBean> getNewsGroupList(Long siteLanguageId);

    List<NewsBean> getNewsList(Long newsGroupId);

    NewsGroupBean getNewsGroup(Long newsGroupId);

    NewsBean getNews(Long newsId);

    Long createNews(NewsBean news);

    void updateNews(NewsBean news);

    void deleteNews(Long newsId);

    Long createNewsGroup(NewsGroupBean newsGroupBean);

    void deleteNewsGroup(Long newsGroupId);

    void updateNewsGroup(NewsGroupBean newsGroup);
}
