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
package org.riverock.portlet.cms.news.bean;

import java.io.Serializable;

/**
 * @author Sergei Maslyukov
 *         Date: 23.08.2006
 *         Time: 15:49:39
 */
public class NewsGroupBean implements Serializable {
    private static final long serialVersionUID = 1056131127L;

    private Long siteLanguageId;
    private Long newsGroupId;
    private Long maxNews;
    private Integer orderValue;
    private String newsGroupName;
    private String newsGroupCode;
    private boolean isDeleted;

    public NewsGroupBean(){}

    public NewsGroupBean(NewsGroupBean item){
        this.siteLanguageId=item.getSiteLanguageId();
        this.newsGroupId=item.getNewsGroupId();
        this.maxNews=item.getMaxNews();
        this.orderValue=item.getOrderValue();
        this.newsGroupName=item.getNewsGroupName();
        this.newsGroupCode=item.getNewsGroupCode();
        this.isDeleted=item.isDeleted();
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Long getSiteLanguageId() {
        return siteLanguageId;
    }

    public void setSiteLanguageId(Long siteLanguageId) {
        this.siteLanguageId = siteLanguageId;
    }

    public Long getNewsGroupId() {
        return newsGroupId;
    }

    public void setNewsGroupId(Long newsGroupId) {
        this.newsGroupId = newsGroupId;
    }

    public Long getMaxNews() {
        return maxNews;
    }

    public void setMaxNews(Long maxNews) {
        this.maxNews = maxNews;
    }

    public Integer getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(Integer orderValue) {
        this.orderValue = orderValue;
    }

    public String getNewsGroupName() {
        return newsGroupName;
    }

    public void setNewsGroupName(String newsGroupName) {
        this.newsGroupName = newsGroupName;
    }

    public String getNewsGroupCode() {
        return newsGroupCode;
    }

    public void setNewsGroupCode(String newsGroupCode) {
        this.newsGroupCode = newsGroupCode;
    }
}
