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

import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.portlet.cms.article.bean.ArticleBean;
import org.riverock.portlet.cms.article.bean.SiteExtended;
import org.riverock.portlet.cms.dao.CmsDaoFactory;

/**
 * @author Sergei Maslyukov
 *         Date: 25.08.2006
 *         Time: 21:07:32
 */
public class ArticleDataProvider implements Serializable {
    private final static Logger log = Logger.getLogger(ArticleDataProvider.class);
    private static final long serialVersionUID = 2057005500L;

    private ArticleService articleService =null;
    private ArticleSessionBean articleSessionBean = null;

    private SiteExtended siteExtended = null;
    private SiteLanguage siteLanguage = null;
    private ArticleBean article = null;

    public ArticleDataProvider() {
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

    public SiteExtended getSiteExtended() {
        if (articleSessionBean.getObjectType()!=articleSessionBean.getSiteType()) {
            throw new IllegalStateException("Query site info with not site type, current type: " + articleSessionBean.getObjectType());
        }
        Long siteId = articleSessionBean.getId();
        if (siteExtended==null) {
            siteExtended= articleService.getSiteExtended(siteId);
        }
        if (!siteExtended.getSite().getSiteId().equals(siteId)) {
            log.warn("Mismatch siteId");
            siteExtended= articleService.getSiteExtended(siteId);
        }

        return siteExtended;
    }

    public void clearSite() {
        this.siteExtended=null;
    }

    public SiteLanguage getSiteLanguage() {
        if (articleSessionBean.getObjectType()!=articleSessionBean.getSiteLanguageType()) {
            throw new IllegalStateException("Query site language info with not site language type, current type: " + articleSessionBean.getObjectType());
        }
        Long siteLangaugeId = articleSessionBean.getId();
        if (siteLanguage==null) {
            siteLanguage = articleService.getSiteLanguage(siteLangaugeId);
        }
    if (siteLanguage.getSiteLanguageId()==null) {
        return siteLanguage;
    }
        if (!siteLanguage.getSiteLanguageId().equals(siteLangaugeId)) {
            log.warn("Mismatch siteLangaugeId");
            siteLanguage = articleService.getSiteLanguage(siteLangaugeId);
        }

        return siteLanguage;
    }

    public void clearSiteLanguage() {
        this.siteLanguage=null;
    }

    public void clearArticle() {
        this.article =null;
    }

    public ArticleBean getArticle() {
        if (articleSessionBean.getObjectType()!=articleSessionBean.getArticleType()) {
            throw new IllegalStateException("Query article info with not article type, current type: " + articleSessionBean.getObjectType());
        }
        Long articleId = articleSessionBean.getId();
        if (log.isDebugEnabled()) {
            log.debug("articleId: " + articleSessionBean.getId());
        }
        if (article ==null) {
            article = CmsDaoFactory.getCmsArticleDao().getArticle(articleId);
        }

        if (article.getArticleId()==null) {
            return article;
        }

        if (!article.getArticleId().equals(articleId)) {
            log.warn("Mismatch articleId");
            article = CmsDaoFactory.getCmsArticleDao().getArticle(articleId);
        }

        return article;
    }
}

