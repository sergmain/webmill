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
package org.riverock.portlet.cms.article.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.portlet.cms.dao.CmsDaoFactory;
import org.riverock.portlet.cms.article.ArticleSessionBean;
import org.riverock.portlet.cms.article.ArticleDataProvider;
import org.riverock.portlet.cms.article.bean.ArticleBean;
import org.riverock.portlet.main.AuthSessionBean;

/**
 * @author Sergei Maslyukov
 *         Date: 25.08.2006
 *         Time: 21:05:24
 */
public class ArticleAction implements Serializable {
    private final static Logger log = Logger.getLogger(ArticleAction.class);
    private static final long serialVersionUID = 2057111311L;

    private ArticleSessionBean articleSessionBean = null;
    private AuthSessionBean authSessionBean = null;
    private ArticleDataProvider articleDataProvider = null;

    public ArticleAction() {
    }

    public void setArticleDataProvider(ArticleDataProvider articleDataProvider) {
        this.articleDataProvider = articleDataProvider;
    }

    // getter/setter methods

    public ArticleSessionBean getArticleSessionBean() {
        return articleSessionBean;
    }

    public void setArticleSessionBean(ArticleSessionBean articleSessionBean) {
        this.articleSessionBean = articleSessionBean;
    }

    public void setAuthSessionBean(AuthSessionBean authSessionBean) {
        this.authSessionBean = authSessionBean;
    }

    // main select action
    public String selectArticle() {
        log.info("Select article action.");
        loadCurrentObject();

        return "article";
    }

    // Add actions
    public String addArticleAction() {
        log.info("Add article action.");

        ArticleBean article = new ArticleBean();
        article.setSiteLanguageId(articleSessionBean.getId());
        article.setXml(articleSessionBean.isXml());
        setSessionObject(article);

        return "article-add";
    }

    public String processAddArticleAction() {
        log.info("Procss add article action.");
        if (getSessionObject() != null) {

            ArticleBean article = getSessionObject();
            Long articleId = CmsDaoFactory.getCmsArticleDao().createArticle(article, authSessionBean.getAuthSession());
            setSessionObject(null);
            articleSessionBean.setId(articleId);
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "article";
    }

    public String cancelAddArticleAction() {
        log.info("Cancel add article action.");

        setSessionObject(null);
        cleadDataProviderObject();

        return "article";
    }

// Edit actions
    public String editArticleAction() {
        log.info("Edit article action.");

        return "article-edit";
    }

    public String processEditArticleAction() {
        log.info("Save changes article action.");

        if (getSessionObject() != null) {
            CmsDaoFactory.getCmsArticleDao().updateArticle(getSessionObject(), authSessionBean.getAuthSession());
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "article";
    }

    public String cancelEditArticleAction() {
        log.info("Cancel article action.");

        return "article";
    }

// Delete actions
    public String deleteArticleAction() {
        log.info("delete article action.");

        setSessionObject(articleDataProvider.getArticle());

        return "article-delete";
    }

    public String cancelDeleteArticleAction() {
        log.info("Cancel delete article action.");

        return "article";
    }

    public String processDeleteArticleAction() {
        log.info("Process delete article action.");

        if (getSessionObject() != null) {
            CmsDaoFactory.getCmsArticleDao().deleteArticle(getSessionObject().getArticleId());
            setSessionObject(null);
            articleSessionBean.setId(null);
            articleSessionBean.setObjectType(ArticleSessionBean.UNKNOWN_TYPE);
            cleadDataProviderObject();
        }

        return "article";
    }

    private void setSessionObject(ArticleBean bean) {
        articleSessionBean.setArticle(bean);
    }

    private void loadCurrentObject() {
        log.debug("start loadCurrentObject()");

        articleSessionBean.setArticle(articleDataProvider.getArticle());
    }

    private void cleadDataProviderObject() {
        articleDataProvider.clearArticle();
    }

    private ArticleBean getSessionObject() {
        return articleSessionBean.getArticle();
    }

}
