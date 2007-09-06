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
package org.riverock.portlet.cms.article;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.PortletName;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.Article;
import org.riverock.portlet.tools.FacesTools;
import org.riverock.portlet.cms.article.bean.SiteBean;
import org.riverock.portlet.cms.article.bean.ArticleBean;
import org.riverock.portlet.cms.article.bean.SiteLanguageBean;
import org.riverock.portlet.cms.article.bean.SiteExtended;

/**
 * @author Sergei Maslyukov
 *         Date: 25.08.2006
 *         Time: 21:07:45
 */
public class ArticleService implements Serializable {
    private final static Logger log = Logger.getLogger(ArticleService.class);

    public ArticleService() {
    }

    public List<SelectItem> getSiteList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<Site> sites = FacesTools.getPortalSpiProvider().getPortalSiteDao().getSites();

        for (Site site : sites) {
            if (site.getSiteId() == null) {
                throw new IllegalStateException("siteIdd is null, name: " + site.getSiteName());
            }

            list.add(new SelectItem(site.getSiteId(), site.getSiteName()));
        }
        return list;
    }

    public List<SelectItem> getPortletList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<PortletName> portletNames = FacesTools.getPortalSpiProvider().getPortalPortletNameDao().getPortletNameList();

        for (PortletName portletName : portletNames) {
            if (portletName.getPortletId() == null) {
                throw new IllegalStateException("id is null, name: " + portletName.getPortletName());
            }

            list.add(new SelectItem(portletName.getPortletId(), portletName.getPortletName()));
        }
        return list;
    }

    public List<Site> getSites() {
        List<Site> list = new ArrayList<Site>();
        List<Site> sites = FacesTools.getPortalSpiProvider().getPortalSiteDao().getSites();
        for (Site site : sites) {
            list.add(new SiteBean(site));
        }
        return list;
    }

    public Site getSite(Long siteId) {
        Site site = FacesTools.getPortalSpiProvider().getPortalSiteDao().getSite(siteId);
        return new SiteBean(site);
    }

    public List<ArticleBean> getArticleList(Long siteLanguageId, boolean isXml) {
        List<ArticleBean> beans = new ArrayList<ArticleBean>();
        List<Article> list = FacesTools.getPortalSpiProvider().getPortalCmsArticleDao().getArticleList(siteLanguageId, isXml);
        for (Article article : list) {
            beans.add( new ArticleBean(article));
        }
        return beans;
    }

    public List<SiteLanguageBean> getSiteLanguageList(Long siteId) {
        List<SiteLanguageBean> list = new ArrayList<SiteLanguageBean>();
        List<SiteLanguage> siteLanguages = FacesTools.getPortalSpiProvider().getPortalSiteLanguageDao().getSiteLanguageList(siteId);
        for (SiteLanguage item : siteLanguages) {
            list.add(new SiteLanguageBean(item));
        }
        return list;
    }

    public SiteExtended getSiteExtended(Long siteId) {
        SiteExtended siteExtended = new SiteExtended();
        siteExtended.setSite(FacesTools.getPortalSpiProvider().getPortalSiteDao().getSite(siteId));
        Long companyId = siteExtended.getSite().getCompanyId();
        Company company = FacesTools.getPortalSpiProvider().getPortalCompanyDao().getCompany(companyId);
        if (log.isDebugEnabled()) {
            log.debug("companyId: " + companyId);
            log.debug("company: " + company);
        }
        siteExtended.setCompany(company);
        return siteExtended;
    }

    public SiteLanguage getSiteLanguage(Long siteLanguageId) {
        SiteLanguage siteLanguage = FacesTools.getPortalSpiProvider().getPortalSiteLanguageDao().getSiteLanguage(siteLanguageId);
        if (siteLanguage != null) {
            return new SiteLanguageBean(siteLanguage);
        } else {
            return new SiteLanguageBean();
        }
    }

    public ArticleBean getArticle(Long articleId) {
        return new ArticleBean(FacesTools.getPortalSpiProvider().getPortalCmsArticleDao().getArticle(articleId));
    }
}

