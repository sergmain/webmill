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

import org.riverock.portlet.cms.article.bean.ArticleBean;
import org.riverock.portlet.cms.article.bean.SiteExtended;
import org.riverock.portlet.manager.site.bean.SiteLanguageBean;

/**
 * @author Sergei Maslyukov
 *         Date: 25.08.2006
 *         Time: 21:08:37
 */
public class ArticleSessionBean implements Serializable {
    private static final long serialVersionUID = 2058883508L;

    public static final int UNKNOWN_TYPE = 0;
    public static final int SITE_TYPE = 1;
    public static final int SITE_LANGUAGE_TYPE = 2;
    public static final int ARTICLE_TYPE = 3;

    private Long id = null;
    private int objectType=0;

    private Long currentArticleId =null;
    private Long currentSiteId=null;

    private SiteExtended siteExtended = null;
    private SiteLanguageBean siteLanguage = null;
    private ArticleBean article = null;

    public Long getCurrentSiteId() {
        return currentSiteId;
    }

    public void setCurrentSiteId(Long currentSiteId) {
        this.currentSiteId = currentSiteId;
    }

    public Long getCurrentArticleId() {
        return currentArticleId;
    }

    public void setCurrentArticleId(Long currentArticleId) {
        this.currentArticleId = currentArticleId;
    }

    public ArticleBean getArticle() {
        return article;
    }

    public void setArticle(ArticleBean article) {
        this.article = article;
    }

    public SiteExtended getSiteExtended() {
        return siteExtended;
    }

    public void setSiteExtended(SiteExtended siteExtended) {
        this.siteExtended = siteExtended;
    }

    public SiteLanguageBean getSiteLanguage() {
        return siteLanguage;
    }

    public void setSiteLanguage(SiteLanguageBean siteLanguage) {
        this.siteLanguage = siteLanguage;
    }

    public int getSiteType() {
        return SITE_TYPE;
    }

    public int getSiteLanguageType() {
        return SITE_LANGUAGE_TYPE;
    }

    public int getArticleType() {
        return ARTICLE_TYPE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int objectType) {
        this.objectType = objectType;
    }
}
