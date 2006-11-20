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
import java.util.Date;

import org.riverock.interfaces.portal.bean.News;

/**
 * @author Sergei Maslyukov
 *         Date: 23.08.2006
 *         Time: 15:49:45
 */
public class NewsBean implements News, Serializable {
    private static final long serialVersionUID = 2057332507L;

    private Long newsId;
    private Long newsGroupId;
    private Date newsDate;
    private String newsHeader;
    private String newsAnons;
    private String newsText;
    private boolean isDeleted=false;
    private boolean isPlain=false;

    public NewsBean() {
    }

    public NewsBean(News news) {
        this.newsId=news.getNewsId();
        this.newsGroupId=news.getNewsGroupId();
        this.newsDate=news.getPostDate();
        this.newsHeader=news.getNewsHeader();
        this.newsAnons=news.getNewsAnons();
        this.newsText=news.getNewsText();
        this.isDeleted=news.isDeleted();
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public Long getNewsGroupId() {
        return newsGroupId;
    }

    public void setNewsGroupId(Long newsGroupId) {
        this.newsGroupId = newsGroupId;
    }

    public Date getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(Date newsDate) {
        this.newsDate = newsDate;
    }

    public String getNewsHeader() {
        return newsHeader;
    }

    public void setNewsHeader(String newsHeader) {
        this.newsHeader = newsHeader;
    }

    public String getNewsAnons() {
        return newsAnons;
    }

    public void setNewsAnons(String newsAnons) {
        this.newsAnons = newsAnons;
    }

    public String getNewsText() {
        return newsText;
    }

    public Date getPostDate() {
        return newsDate;
    }

    public boolean isPlain() {
        return isPlain;
    }

    public void setNewsText(String newsText) {
        this.newsText = newsText;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

