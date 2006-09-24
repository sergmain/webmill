/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

                TreeNodeBase plainArticleListNode = new TreeNodeBase("plain-article-list", "Plain article list", siteLanguage.getSiteLanguageId().toString(), false);
                siteLanguageNode.getChildren().add(plainArticleListNode);

                for (ArticleBean articleBean : articleService.getArticleList(siteLanguage.getSiteLanguageId(), false)) {
                    TreeNodeBase articleNode = new TreeNodeBase(
                        "plain-article",
                        articleBean.getArticleName(),
                        articleBean.getArticleId().toString(),
                        false);
                    plainArticleListNode.getChildren().add(articleNode);
                }

                TreeNodeBase xmlArticleListNode = new TreeNodeBase("xml-article-list", "XML article list", siteLanguage.getSiteLanguageId().toString(), false);
                siteLanguageNode.getChildren().add(xmlArticleListNode);

                for (ArticleBean articleBean : articleService.getArticleList(siteLanguage.getSiteLanguageId(), true)) {
                    TreeNodeBase articleNode = new TreeNodeBase(
                        "xml-article",
                        articleBean.getArticleName(),
                        articleBean.getArticleId().toString(),
                        false);
                    xmlArticleListNode.getChildren().add(articleNode);
                }

            }
        }
        return treeRoot;
    }
}
