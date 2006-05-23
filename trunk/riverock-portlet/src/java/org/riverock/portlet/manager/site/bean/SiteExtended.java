package org.riverock.portlet.manager.site.bean;

import java.util.List;
import java.util.Locale;
import java.io.Serializable;

import org.riverock.interfaces.portal.bean.Site;
import org.riverock.interfaces.portal.bean.VirtualHost;
import org.riverock.interfaces.portal.bean.Company;
import org.riverock.common.tools.StringTools;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 19:33:55
 */
public class SiteExtended implements Serializable {
    private static final long serialVersionUID = 2058005301L;

    private SiteBean site = null;
    private List<VirtualHost> virtualHosts = null;
    private CompanyBean company = null;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = new CompanyBean(company);
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = new SiteBean(site);
    }

    public List<VirtualHost> getVirtualHosts() {
        return virtualHosts;
    }

    public void setVirtualHosts(List<VirtualHost> virtualHosts) {
        this.virtualHosts = virtualHosts;
    }

    public String getSiteDefaultLocale() {
        if (site==null) {
            return null;
        }

        if (site.getDefCountry()==null && site.getDefVariant()==null) {
            return new Locale(site.getDefLanguage()).toString();
        }
        else if (site.getDefVariant()==null) {
            return new Locale(site.getDefLanguage(), site.getDefCountry()).toString();
        }
        else {
            return new Locale(site.getDefLanguage(), site.getDefCountry(), site.getDefVariant()).toString();
        }
    }

    public void setSiteDefaultLocale(String localeString) {
        Locale locale = StringTools.getLocale(localeString);
        site.setDefLanguage(locale.getLanguage());
        site.setDefCountry(locale.getCountry());
        site.setDefVariant(locale.getVariant());
    }
}
