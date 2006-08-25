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
package org.riverock.portlet.cms.news;

import java.io.Serializable;

import org.apache.log4j.Logger;

import org.riverock.portlet.cms.news.bean.NewsGroupBean;
import org.riverock.portlet.cms.news.bean.NewsBean;
import org.riverock.portlet.cms.news.bean.SiteExtended;
import org.riverock.portlet.cms.dao.CmsDaoFactory;
import org.riverock.interfaces.portal.bean.SiteLanguage;

/**
 * @author Sergei Maslyukov
 *         Date: 23.08.2006
 *         Time: 15:18:57
 */
public class NewsDataProvider implements Serializable {
    private final static Logger log = Logger.getLogger(NewsDataProvider.class);
    private static final long serialVersionUID = 2057005500L;

    private NewsService newsService =null;
    private NewsSessionBean newsSessionBean = null;

    private SiteExtended siteExtended = null;
    private SiteLanguage siteLanguage = null;
    private NewsBean news = null;
    private NewsGroupBean newsGroup = null;

    public NewsDataProvider() {
    }

    public NewsService getNewsService() {
        return newsService;
    }

    public void setNewsService(NewsService newsService) {
        this.newsService = newsService;
    }

    public NewsSessionBean getNewsSessionBean() {
        return newsSessionBean;
    }

    public void setNewsSessionBean(NewsSessionBean newsSessionBean) {
        this.newsSessionBean = newsSessionBean;
    }

    public SiteExtended getSiteExtended() {
        if (newsSessionBean.getObjectType()!=newsSessionBean.getSiteType()) {
            throw new IllegalStateException("Query site info with not site type, current type: " + newsSessionBean.getObjectType());
        }
        Long siteId = newsSessionBean.getId();
        if (siteExtended==null) {
            siteExtended= newsService.getSiteExtended(siteId);
        }
        if (!siteExtended.getSite().getSiteId().equals(siteId)) {
            log.warn("Mismatch siteId");
            siteExtended= newsService.getSiteExtended(siteId);
        }

        return siteExtended;
    }

    public void clearSite() {
        this.siteExtended=null;
    }

    public SiteLanguage getSiteLanguage() {
        if (newsSessionBean.getObjectType()!=newsSessionBean.getSiteLanguageType()) {
            throw new IllegalStateException("Query site language info with not site language type, current type: " + newsSessionBean.getObjectType());
        }
        Long siteLangaugeId = newsSessionBean.getId();
        if (siteLanguage==null) {
            siteLanguage = newsService.getSiteLanguage(siteLangaugeId);
        }
    if (siteLanguage.getSiteLanguageId()==null) {
        return siteLanguage;
    }
        if (!siteLanguage.getSiteLanguageId().equals(siteLangaugeId)) {
            log.warn("Mismatch siteLangaugeId");
            siteLanguage = newsService.getSiteLanguage(siteLangaugeId);
        }

        return siteLanguage;
    }

    public void clearSiteLanguage() {
        this.siteLanguage=null;
    }

    public void clearNewsGroup() {
        this.newsGroup =null;
    }

    public NewsGroupBean getNewsGroup() {
        if (newsSessionBean.getObjectType()!=newsSessionBean.getNewsGroupType()) {
            throw new IllegalStateException("Query news group info with not news group type, current type: " + newsSessionBean.getObjectType());
        }
        Long newsGroupId = newsSessionBean.getId();
        if (newsGroup ==null) {
            newsGroup = newsService.getNewsGroup(newsGroupId);
        }

        if (newsGroup.getNewsGroupId()==null) {
            return newsGroup;
        }

        if (!newsGroup.getNewsGroupId().equals(newsGroupId)) {
            log.warn("Mismatch newsGroupId");
            newsGroup = newsService.getNewsGroup(newsGroupId);
        }

        return newsGroup;
    }

    public void clearNews() {
        this.news =null;
    }

    public NewsBean getNews() {
        if (newsSessionBean.getObjectType()!=newsSessionBean.getNewsType()) {
            throw new IllegalStateException("Query news info with not news type, current type: " + newsSessionBean.getObjectType());
        }
        Long newsId = newsSessionBean.getId();
        if (log.isDebugEnabled()) {
            log.debug("newsId: " + newsSessionBean.getId());
        }
        if (news ==null) {
            news = CmsDaoFactory.getCmsNewsDao().getNews(newsId);
        }

        if (news.getNewsId()==null) {
            return news;
        }

        if (!news.getNewsId().equals(newsId)) {
            log.warn("Mismatch newsId");
            news = CmsDaoFactory.getCmsNewsDao().getNews(newsId);
        }

        return news;
    }
}
