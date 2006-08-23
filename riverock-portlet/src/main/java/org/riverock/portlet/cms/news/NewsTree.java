package org.riverock.portlet.cms.news;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.apache.myfaces.custom.tree2.TreeState;
import org.apache.myfaces.custom.tree2.TreeStateBase;
import org.apache.myfaces.custom.tree2.TreeModel;
import org.apache.myfaces.custom.tree2.TreeNode;
import org.apache.myfaces.custom.tree2.TreeModelBase;
import org.apache.myfaces.custom.tree2.TreeNodeBase;

import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.portlet.cms.news.bean.NewsGroupBean;
import org.riverock.portlet.cms.news.bean.NewsBean;

/**
 * @author Sergei Maslyukov
 *         Date: 23.08.2006
 *         Time: 15:15:31
 */
public class NewsTree implements Serializable {
    private final static Logger log = Logger.getLogger(NewsTree.class);
    private static final long serialVersionUID = 2057005500L;

    private NewsService newsService = null;
    private TreeState treeState=null;

    private NewsSessionBean newsSessionBean = null;

    public NewsTree() {
        treeState = new TreeStateBase();
        treeState.setTransient(true);
    }

    public NewsService getNewsService() {
        return newsService;
    }

    public void setNewsService(NewsService newsService) {
        this.newsService = newsService;
    }

    public NewsSessionBean getNewsSessionBean() {
        return newsSessionBean;
    }

    public void setNewsSessionBean(NewsSessionBean newsSessionBean) {
        this.newsSessionBean = newsSessionBean;
    }

    public TreeModel getNewsTree() {
        log.info("Invoke getNewsTree()");

        TreeNode rootNode = getPrepareNewsTree();
        TreeModel treeModel = new TreeModelBase(rootNode);
        treeModel.setTreeState(treeState);

        return treeModel;
    }

    private TreeNode getPrepareNewsTree() {

        log.info("Invoke getPrepareNewsTree()");

        TreeNode treeRoot = new TreeNodeBase("tree-root", "tree-root", false);
        if (newsSessionBean.getCurrentSiteId()!=null) {

            Site site = newsService.getSite(newsSessionBean.getCurrentSiteId());

            TreeNodeBase siteNode = new TreeNodeBase("site", site.getSiteName(), site.getSiteId().toString(), false);
            treeRoot.getChildren().add(siteNode);

            for (SiteLanguage siteLanguage : newsService.getSiteLanguageList(site.getSiteId())) {
                TreeNodeBase siteLanguageNode = new TreeNodeBase(
                    "site-language",
                    siteLanguage.getNameCustomLanguage() + " (" + siteLanguage.getCustomLanguage() + ")",
                    siteLanguage.getSiteLanguageId().toString(),
                    false);
                treeRoot.getChildren().add(siteLanguageNode);

                TreeNodeBase newsGroupListNode = new TreeNodeBase("news-group-list", "News group list", siteLanguage.getSiteLanguageId().toString(), false);
                siteLanguageNode.getChildren().add(newsGroupListNode);

                for (NewsGroupBean newsGroupBean : newsService.getNewsGroupList(siteLanguage.getSiteLanguageId())) {
                    TreeNodeBase newsGroupNode = new TreeNodeBase(
                        "news-group",
                        newsGroupBean.getNewsGroupName(),
                        newsGroupBean.getNewsGroupId().toString(),
                        false);
                    newsGroupListNode.getChildren().add(newsGroupNode);

                    for (NewsBean news : newsService.getNewsList(newsGroupBean.getNewsGroupId())) {
                        TreeNodeBase newsNode = new TreeNodeBase(
                            "news-item",
                            news.getNewsHeader(),
                            news.getNewsId().toString(),
                            false);
                        newsGroupNode.getChildren().add(newsNode);
                    }
                }

            }
        }
        return treeRoot;
    }
}
