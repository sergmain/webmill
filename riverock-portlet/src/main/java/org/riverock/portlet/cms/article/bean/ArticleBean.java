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
package org.riverock.portlet.cms.article.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Sergei Maslyukov
 *         Date: 25.08.2006
 *         Time: 21:06:11
 */
public class ArticleBean implements Serializable {
    private static final long serialVersionUID = 2057332507L;

    private Long articleId = null;
    private String articleName=null;
    private String articleCode=null;
    private String articleText=null;
    private boolean isXml=false;
    private Long siteLanguageId=null;
    private Date created=null;

    public ArticleBean() {
    }

    public ArticleBean(ArticleBean articleBean) {
        this.articleId = articleBean.getArticleId();
        this.articleName = articleBean.getArticleName();
        this.articleCode = articleBean.getArticleCode();
        this.articleText = articleBean.getArticleText();
        this.isXml = articleBean.isXml();
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
}
