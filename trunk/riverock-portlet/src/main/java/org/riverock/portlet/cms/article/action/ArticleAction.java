/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.portlet.cms.article.action;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.common.utils.PortletUtils;
import org.riverock.portlet.cms.article.ArticleDataProvider;
import org.riverock.portlet.cms.article.ArticleSessionBean;
import org.riverock.portlet.cms.article.bean.ArticleBean;
import org.riverock.portlet.main.AuthSessionBean;
import org.riverock.portlet.tools.FacesTools;

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

    public static final String[] ROLES = new String[]{"webmill.portal-manager","webmill.cms-manager","webmill.article-manager"};

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

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if (getSessionObject() != null) {

            ArticleBean article = getSessionObject();
            Long articleId = FacesTools.getPortalDaoProvider().getPortalCmsArticleDao().createArticle(article);
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

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if (getSessionObject() != null) {
            FacesTools.getPortalDaoProvider().getPortalCmsArticleDao().updateArticle(getSessionObject());
            cleadDataProviderObject();
            loadCurrentObject();
        }

        return "article";
    }

    public String cancelEditArticleAction() {
        log.info("Cancel article action.");
        loadCurrentObject();
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

        PortletUtils.checkRights(FacesTools.getPortletRequest(), ROLES);

        if (getSessionObject() != null) {
            FacesTools.getPortalDaoProvider().getPortalCmsArticleDao().deleteArticle(getSessionObject().getArticleId());
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
