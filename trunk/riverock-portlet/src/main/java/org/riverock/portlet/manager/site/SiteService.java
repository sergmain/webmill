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
package org.riverock.portlet.manager.site;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.Css;
import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.SiteLanguage;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.interfaces.portal.bean.Xslt;
import org.riverock.portlet.manager.site.bean.CssBean;
import org.riverock.portlet.manager.site.bean.SiteBean;
import org.riverock.portlet.manager.site.bean.SiteExtended;
import org.riverock.portlet.manager.site.bean.SiteLanguageBean;
import org.riverock.portlet.manager.site.bean.TemplateBean;
import org.riverock.portlet.manager.site.bean.XsltBean;
import org.riverock.portlet.tools.FacesTools;

/**
 * @author Sergei Maslyukov
 * 16.05.2006
 * 20:39:26
 *
 *
 */
public class SiteService implements Serializable {
    private final static Logger log = Logger.getLogger(SiteService.class);
    private static final long serialVersionUID = 2058005507L;

    public SiteService() {
    }

    public List<SelectItem> getSiteList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<Site> sites = FacesTools.getPortalDaoProvider().getPortalSiteDao().getSites();

        for (Site site : sites) {
            if (site.getSiteId() == null) {
                throw new IllegalStateException("siteIdd is null, name: " + site.getSiteName());
            }

            list.add(new SelectItem(site.getSiteId(), site.getSiteName()));
        }
        return list;
    }

    @SuppressWarnings({"RedundantStringConstructorCall"})
    public List<SelectItem> getCompanyList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<Company> companies = FacesTools.getPortalDaoProvider().getPortalCompanyDao().getCompanyList();

        for (Company company : companies) {
            if (company.getId() == null) {
                throw new IllegalStateException("id is null, name: " + company.getName());
            }
            String companyName = company.getName();
            if (StringUtils.isBlank(companyName)) {
                companyName = "<empty company name>";
            }
            list.add(new SelectItem(company.getId().toString(), companyName));
        }
        return list;
    }

    public Site getSite(Long siteId) {
        Site site = FacesTools.getPortalDaoProvider().getPortalSiteDao().getSite(siteId);
        return new SiteBean(site);
    }

    public List<Site> getSites() {
        List<Site> list = new ArrayList<Site>();
        List<Site> sites = FacesTools.getPortalDaoProvider().getPortalSiteDao().getSites();
        for (Site site: sites) {
            list.add( new SiteBean(site) );
        }
        return list;
    }

    public List<SiteLanguage> getSiteLanguageList(Long siteId) {
        List<SiteLanguage> list = new ArrayList<SiteLanguage>();
        List<SiteLanguage> siteLanguages = FacesTools.getPortalDaoProvider().getPortalSiteLanguageDao().getSiteLanguageList(siteId);
        for (SiteLanguage item: siteLanguages) {
            list.add( new SiteLanguageBean(item) );
        }
        return list;
    }

    public List<Template> getTemplateList(Long siteLanguageId) {
        List<Template> list = new ArrayList<Template>();
        List<Template> templates = FacesTools.getPortalDaoProvider().getPortalTemplateDao().getTemplateLanguageList(siteLanguageId);
        for (Template item: templates) {
            list.add( new TemplateBean(item) );
        }
        return list;
    }

    public List<Xslt> getXsltList(Long siteLanguageId) {
        List<Xslt> list = new ArrayList<Xslt>();
        List<Xslt> xslts = FacesTools.getPortalDaoProvider().getPortalXsltDao().getXsltList(siteLanguageId);
        for (Xslt xslt: xslts) {
            list.add( new XsltBean(xslt) );
        }
        return list;
    }

    public SiteExtended getSiteExtended(Long siteId) {
        SiteExtended siteExtended = new SiteExtended();
        siteExtended.setSite(FacesTools.getPortalDaoProvider().getPortalSiteDao().getSite(siteId));
        List<VirtualHost> virtualHosts = FacesTools.getPortalDaoProvider().getPortalVirtualHostDao().getVirtualHosts(siteExtended.getSite().getSiteId());
        List<String> hosts = new ArrayList<String>();
        for (VirtualHost host : virtualHosts) {
            hosts.add(host.getHost().toLowerCase());
        }
        siteExtended.setVirtualHosts(hosts);
        Long companyId = siteExtended.getSite().getCompanyId();
        Company company = FacesTools.getPortalDaoProvider().getPortalCompanyDao().getCompany(companyId);
        if (log.isDebugEnabled()) {
            log.debug("companyId: " + companyId);
            log.debug("company: " + company);
        }
        siteExtended.setCompany(company);
        return siteExtended;
    }

    public SiteLanguage getSiteLanguage(Long siteLanguageId) {
        SiteLanguage siteLanguage = FacesTools.getPortalDaoProvider().getPortalSiteLanguageDao().getSiteLanguage(siteLanguageId);
	if (siteLanguage!=null) {
		return new SiteLanguageBean(siteLanguage);
	}
	else {
        	return new SiteLanguageBean();
	}
    }

    public Template getTemplate(Long templateId) {
	Template bean = FacesTools.getPortalDaoProvider().getPortalTemplateDao().getTemplate(templateId);
	if (bean!=null) {
        	return new TemplateBean(bean);
	}
	else {
        	return new TemplateBean();
	}
    }

    public Xslt getXslt(Long xsltId) {
	Xslt bean = FacesTools.getPortalDaoProvider().getPortalXsltDao().getXslt(xsltId);
	if (bean!=null) {
        	return new XsltBean(bean);
	}
	else {
        	return new XsltBean();
	}
    }

    public Css getCss(Long cssId) {
	Css css = FacesTools.getPortalDaoProvider().getPortalCssDao().getCss(cssId);
	if (css!=null) {
        	return new CssBean(css);
	}
	else {
		return new CssBean();
	}
    }

    public List<? extends Css> getCssList(Long siteId) {
        List<Css> list = new ArrayList<Css>();
        List<Css> cssList = FacesTools.getPortalDaoProvider().getPortalCssDao().getCssList(siteId);
        for (Css css: cssList) {
            list.add( new CssBean(css) );
        }
        return list;
    }
}
