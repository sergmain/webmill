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
package org.riverock.portlet.cms.news.bean;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.NewsGroup;

/**
 * @author Sergei Maslyukov
 *         Date: 23.08.2006
 *         Time: 15:49:39
 */
public class NewsGroupBean implements NewsGroup, Serializable {
    private static final long serialVersionUID = 1056131127L;

    private Long siteLanguageId;
    private Long newsGroupId;
    private Long maxNews;
    private Integer orderValue;
    private String newsGroupName;
    private String newsGroupCode;
    private boolean isDeleted;

    public NewsGroupBean(){}

    public NewsGroupBean(NewsGroup item){
        this.siteLanguageId=item.getSiteLanguageId();
        this.newsGroupId=item.getNewsGroupId();
        this.maxNews=new Long(item.getCountNewsPerGroup());
        this.orderValue=item.getOrderField();
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

    public int getCountNewsPerGroup() {
        if (maxNews==null)
            return 0;
        else
            return maxNews.intValue();
    }

    public int getOrderField() {
        return orderValue;
    }

}
