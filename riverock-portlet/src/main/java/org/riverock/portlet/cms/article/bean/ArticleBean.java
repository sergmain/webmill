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
package org.riverock.portlet.cms.article.bean;

import java.io.Serializable;
import java.util.Date;

import org.riverock.interfaces.portal.bean.Article;

/**
 * @author Sergei Maslyukov
 *         Date: 25.08.2006
 *         Time: 21:06:11
 */
public class ArticleBean implements Article, Serializable {
    private static final long serialVersionUID = 2057332507L;

    private Long articleId = null;
    private String articleName=null;
    private String articleCode=null;
    private String articleText=null;
    private boolean isXml=false;
    private boolean isDeleted;
    private Long siteLanguageId=null;
    private Date created=null;
    private Long userId=null;

    public ArticleBean() {
    }

    public ArticleBean(Article articleBean) {
        this.articleId = articleBean.getArticleId();
        this.siteLanguageId = articleBean.getSiteLanguageId();
        this.articleName = articleBean.getArticleName();
        this.articleCode = articleBean.getArticleCode();
        this.articleText = articleBean.getArticleData();
        this.isXml = !articleBean.isPlain();
        this.userId = articleBean.getUserId();
        this.isDeleted = articleBean.isDeleted();
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getArticleText() {
        return articleText;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    public boolean isXml() {
        return isXml;
    }

    public void setXml(boolean xml) {
        isXml = xml;
    }

    public Long getSiteLanguageId() {
        return siteLanguageId;
    }

    public void setSiteLanguageId(Long siteLanguageId) {
        this.siteLanguageId=siteLanguageId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getPostDate() {
        return created;
    }

    public String getArticleData() {
        return articleText;
    }

    public boolean isPlain() {
        return !isXml;
    }

    public Long getUserId() {
        return userId;
    }

}
