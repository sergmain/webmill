package org.riverock.portlet.manager.site.bean;

import java.io.Serializable;
import java.util.List;

import org.riverock.interfaces.portal.bean.Company;
import org.riverock.interfaces.portal.bean.Site;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 19:33:55
 */
public class SiteExtended implements Serializable {
    private static final long serialVersionUID = 2058005301L;

    private SiteBean site = null;
    private List<String> virtualHosts = null;
    private CompanyBean company = null;

    public SiteExtended(){
    }

    public SiteExtended(SiteBean siteBean, List<String> virtualHosts, CompanyBean company){
        this.site=siteBean;
        this.virtualHosts=virtualHosts;
        this.company=company;
    }

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

    public List<String> getVirtualHosts() {
        return virtualHosts;
    }

    public void setVirtualHosts(List<String> virtualHosts) {
        this.virtualHosts = virtualHosts;
    }
}
