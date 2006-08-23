package org.riverock.portlet.manager.menu.bean;

import java.io.Serializable;

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
    private CompanyBean company = null;

    public SiteExtended(){
    }

    public SiteExtended(SiteBean siteBean, CompanyBean company){
        this.site=siteBean;
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
}
