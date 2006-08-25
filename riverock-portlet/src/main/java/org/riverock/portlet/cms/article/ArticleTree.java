/*
 * org.riverock.portlet -- Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
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
package org.riverock.portlet.cms.article;

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
import org.riverock.portlet.cms.article.bean.ArticleBean;

/**
 * @author Sergei Maslyukov
 *         Date: 25.08.2006
 *         Time: 21:09:08
 */
public class ArticleTree implements Serializable {
    private final static Logger log = Logger.getLogger(ArticleTree.class);
    private static final long serialVersionUID = 2057005500L;

    private ArticleService articleService = null;
    private TreeState treeState=null;

    private ArticleSessionBean articleSessionBean = null;

    public ArticleTree() {
        treeState = new TreeStateBase();
        treeState.setTransient(true);
    }

    public ArticleService getArticleService() {
        return articleService;
    }

    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    public ArticleSessionBean getArticleSessionBean() {
        return articleSessionBean;
    }

    public void setArticleSessionBean(ArticleSessionBean articleSessionBean) {
        this.articleSessionBean = articleSessionBean;
    }

    public TreeModel getArticleTree() {
        log.info("Invoke getArticleTree()");

        TreeNode rootNode = getPrepareArticleTree();
        TreeModel treeModel = new TreeModelBase(rootNode);
        treeModel.setTreeState(treeState);

        return treeModel;
    }

    private TreeNode getPrepareArticleTree() {

        log.info("Invoke getPrepareArticleTree()");

        TreeNode treeRoot = new TreeNodeBase("tree-root", "tree-root", false);
        if (articleSessionBean.getCurrentSiteId()!=null) {

            Site site = articleService.getSite(articleSessionBean.getCurrentSiteId());

            TreeNodeBase siteNode = new TreeNodeBase("site", site.getSiteName(), site.getSiteId().toString(), false);
            treeRoot.getChildren().add(siteNode);

            for (SiteLanguage siteLanguage : articleService.getSiteLanguageList(site.getSiteId())) {
                TreeNodeBase siteLanguageNode = new TreeNodeBase(
                    "site-language",
                    siteLanguage.getNameCustomLanguage() + " (" + siteLanguage.getCustomLanguage() + ")",
                    siteLanguage.getSiteLanguageId().toString(),
                    false);
                treeRoot.getChildren().add(siteLanguageNode);

                TreeNodeBase articleListNode = new TreeNodeBase("article-list", "Article list", siteLanguage.getSiteLanguageId().toString(), false);
                siteLanguageNode.getChildren().add(articleListNode);

                for (ArticleBean articleBean : articleService.getArticleList(siteLanguage.getSiteLanguageId())) {
                    TreeNodeBase articleNode = new TreeNodeBase(
                        "article",
                        articleBean.getArticleName(),
                        articleBean.getArticleId().toString(),
                        false);
                    articleListNode.getChildren().add(articleNode);
                }

            }
        }
        return treeRoot;
    }
}
