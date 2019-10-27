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
package org.riverock.portlet.cms.news;

import java.io.Serializable;

import org.riverock.portlet.cms.news.bean.NewsBean;
import org.riverock.portlet.cms.news.bean.NewsGroupBean;
import org.riverock.portlet.cms.news.bean.SiteExtended;
import org.riverock.portlet.manager.site.bean.SiteLanguageBean;

/**
 * @author Sergei Maslyukov
 *         Date: 23.08.2006
 *         Time: 15:16:32
 */
public class NewsSessionBean implements Serializable {
    private static final long serialVersionUID = 2058883508L;

    public static final int UNKNOWN_TYPE = 0;
    public static final int SITE_TYPE = 1;
    public static final int SITE_LANGUAGE_TYPE = 2;
    public static final int NEWS_GROUP_TYPE = 3;
    public static final int NEWS_ITEM_TYPE = 4;

    private Long id = null;
    private int objectType=0;

    private Long currentNewsId =null;
    private Long currentNewsGroupId =null;
    private Long currentSiteId=null;

    private SiteExtended siteExtended = null;
    private SiteLanguageBean siteLanguage = null;
    private NewsGroupBean newsGroup = null;
    private NewsBean news = null;

    public Long getCurrentSiteId() {
        return currentSiteId;
    }

    public void setCurrentSiteId(Long currentSiteId) {
        this.currentSiteId = currentSiteId;
    }

    public Long getCurrentNewsGroupId() {
        return currentNewsGroupId;
    }

    public void setCurrentNewsGroupId(Long currentNewsGroupId) {
        this.currentNewsId = null;
        this.currentNewsGroupId = currentNewsGroupId;
    }

    public Long getCurrentNewsId() {
        return currentNewsId;
    }

    public void setCurrentNewsId(Long currentNewsId) {
        this.currentNewsGroupId = null;
        this.currentNewsId = currentNewsId;
    }

    public NewsGroupBean getNewsGroup() {
        return newsGroup;
    }

    public void setNewsGroup(NewsGroupBean newsGroup) {
        this.newsGroup = newsGroup;
    }

    public NewsBean getNews() {
        return news;
    }

    public void setNews(NewsBean news) {
        this.news = news;
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

    public int getNewsGroupType() {
        return NEWS_GROUP_TYPE;
    }

    public int getNewsType() {
        return NEWS_ITEM_TYPE;
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
