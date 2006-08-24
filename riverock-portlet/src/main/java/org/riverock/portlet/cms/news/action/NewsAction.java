package org.riverock.portlet.cms.news.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.cms.news.NewsSessionBean;
import org.riverock.portlet.cms.news.NewsDataProvider;
import org.riverock.portlet.cms.news.bean.NewsBean;
import org.riverock.portlet.cms.dao.CmsDaoFactory;

/**
 * @author Sergei Maslyukov
 *         Date: 23.08.2006
 *         Time: 15:20:51
 */
public class NewsAction  implements Serializable {
    private final static Logger log = Logger.getLogger(NewsAction.class);
    private static final long serialVersionUID = 2057111311L;

    private NewsSessionBean newsSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private NewsDataProvider newsDataProvider = null;

    public NewsAction() {
    }

    public void setNewsDataProvider(NewsDataProvider newsDataProvider) {
        this.newsDataProvider = newsDataProvider;
    }

    // getter/setter methods

    public NewsSessionBean getNewsSessionBean() {
        return newsSessionBean;
    }

    public void setNewsSessionBean(NewsSessionBean newsSessionBean) {
        this.newsSessionBean = newsSessionBean;
    }

    public void setAuthSessionBean(AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

// main select action
    public String selectNews() {
        log.info("Select news item action.");
        loadCurrentObject();

        return "news";
    }

// Add actions
    public String addNewsAction() {
        log.info("Add news item action.");

        NewsBean news = new NewsBean();
        news.setNewsId(newsSessionBean.getId());
        setSessionObject(news);

        return "news-add";
    }

    public String processAddNewsAction() {
        log.info("Procss add news item action.");
        if (getSessionObject() != null) {

            if (newsSessionBean.getCurrentNewsId() == null && newsSessionBean.getCurrentNewsGroupId() == null) {
                throw new IllegalStateException("Both currentNewsId and currentNewsGroupId are null");
            }

            if (newsSessionBean.getCurrentNewsId() != null && newsSessionBean.getCurrentNewsGroupId() != null) {
                throw new IllegalStateException("Both currentNewsId and currentNewsGroupId are not null");
            }

            NewsBean news = getSessionObject();
            if (newsSessionBean.getCurrentNewsGroupId() != null) {
                news.setNewsGroupId( newsSessionBean.getCurrentNewsGroupId() );
            } else {
                NewsBean newsItem = CmsDaoFactory.getCmsNewsDao().getNews(newsSessionBean.getCurrentNewsId());
                news.setNewsGroupId( newsItem.getNewsGroupId() );
            }

            Long newsId = CmsDaoFactory.getCmsNewsDao().createNews(news);
            setSessionObject(null);
            newsSessionBean.setId(newsId);
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "news";
    }

    public String cancelAddNewsAction() {
        log.info("Cancel add news item action.");

        setSessionObject(null);
        cleadDataProviderObject();

        return "news";
    }

// Edit actions
    public String editNewsAction() {
        log.info("Edit news action.");

//        Long newsGroupId = getSessionObject().getNewsGroupId();

        return "news-edit";
    }

    public String processEditNewsAction() {
        log.info("Save changes news item action.");

        if (getSessionObject() != null) {
            CmsDaoFactory.getCmsNewsDao().updateNews(getSessionObject());
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "news";
    }

    public String cancelEditNewsAction() {
        log.info("Cancel news item action.");

        return "news";
    }

// Delete actions
    public String deleteNewsAction() {
        log.info("delete news item action.");

        setSessionObject(newsDataProvider.getNews());

        return "news-delete";
    }

    public String cancelDeleteNewsAction() {
        log.info("Cancel delete news item action.");

        return "news";
    }

    public String processDeleteNewsAction() {
        log.info("Process delete news item action.");

        if (getSessionObject() != null) {
            CmsDaoFactory.getCmsNewsDao().deleteNews(getSessionObject().getNewsId());
            setSessionObject(null);
            newsSessionBean.setId(null);
            newsSessionBean.setObjectType(NewsSessionBean.UNKNOWN_TYPE);
            cleadDataProviderObject();
        }

        return "news";
    }

    private void setSessionObject(NewsBean bean) {
        newsSessionBean.setNews(bean);
    }

    private void loadCurrentObject() {
        log.debug("start loadCurrentObject()");

        newsSessionBean.setNews(newsDataProvider.getNews());
    }

    private void cleadDataProviderObject() {
        newsDataProvider.clearNews();
    }

    private NewsBean getSessionObject() {
        return newsSessionBean.getNews();
    }

}
