/**
 * License
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

/**
 * @author Sergei Maslyukov
 * 16.05.2006
 * 20:39:26
 *
 *
 */
public class SiteService implements Serializable {
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
        siteExtended.setCompany(
            FacesTools.getPortalDaoProvider().getPortalCompanyDao().getCompany(siteExtended.getSite().getCompanyId())
        );
        return siteExtended;
    }

    public SiteLanguage getSiteLanguage(Long siteLanguageId) {
        SiteLanguage siteLanguage = new SiteLanguageBean(
            FacesTools.getPortalDaoProvider().getPortalSiteLanguageDao().getSiteLanguage(siteLanguageId)
        );
        return siteLanguage;
    }

    public Template getTemplate(Long templateId) {
        Template template = new TemplateBean(
            FacesTools.getPortalDaoProvider().getPortalTemplateDao().getTemplate(templateId)
        );
        return template;
    }

    public Xslt getXslt(Long xsltId) {
        Xslt xslt = new XsltBean(
            FacesTools.getPortalDaoProvider().getPortalXsltDao().getXslt(xsltId)
        );
        return xslt;
    }

    public Css getCss(Long cssId) {
        Css css = new CssBean(
            FacesTools.getPortalDaoProvider().getPortalCssDao().getCss(cssId)
        );
        return css;
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