/*
 * org.riverock.portlet -- Portlet Library
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
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
 *
 */
package org.riverock.portlet.manager.site;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

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
import org.apache.log4j.Logger;

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

    @SuppressWarnings({"RedundantStringConstructorCall"})
    public List<SelectItem> getCompanyList() {
        List<SelectItem> list = new ArrayList<SelectItem>();
        List<Company> companies = FacesTools.getPortalDaoProvider().getPortalCompanyDao().getCompanyList();

        for (Company company : companies) {
            if (company.getId() == null) {
                throw new IllegalStateException("id is null, name: " + company.getName());
            }

            // create new String - work around with different classloader issue
	    // Todo my be this wrong :)
            list.add(new SelectItem(new String(company.getId().toString()), new String(company.getName())));
        }
        return list;
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
        siteExtended.setSite( FacesTools.getPortalDaoProvider().getPortalSiteDao().getSite(siteId) );
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
