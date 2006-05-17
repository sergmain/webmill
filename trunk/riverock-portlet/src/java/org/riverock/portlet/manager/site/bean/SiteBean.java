/**
 * License
 * 
 */
package org.riverock.portlet.manager.site.bean;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.Site;

/**
 * @author Sergei Maslyukov
 * 16.05.2006
 * 21:10:07
 *
 *
 */
public class SiteBean implements Serializable, Site {
    private static final long serialVersionUID = 2057005502L;

	private Long siteId=null;
	private Long companyId;
	private String siteName;
	private Boolean cssDynamic;
	private String cssFile;
	private Boolean registerAllowed;
	private String defLanguage;
	private String defCountry;
	private String defVariant;
	private String adminEmail;

	public SiteBean(){
	}

	public SiteBean(Site site){
		this.siteId = site.getSiteId();
		this.companyId = site.getCompanyId();
		this.siteName = site.getSiteName();
		this.cssDynamic = site.getCssDynamic();
		this.cssFile = site.getCssFile();
		this.registerAllowed = site.getRegisterAllowed();
		this.defLanguage = site.getDefLanguage();
		this.defCountry = site.getDefCountry();
		this.defVariant = site.getDefVariant();
		this.adminEmail = site.getAdminEmail();
	}
	
    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Boolean getCssDynamic() {
        return cssDynamic;
    }

    public void setCssDynamic(Boolean cssDynamic) {
        this.cssDynamic = cssDynamic;
    }

    public String getCssFile() {
        return cssFile;
    }

    public void setCssFile(String cssFile) {
        this.cssFile = cssFile;
    }

    public Boolean getRegisterAllowed() {
        return registerAllowed;
    }

    public void setRegisterAllowed(Boolean registerAllowed) {
        this.registerAllowed = registerAllowed;
    }

    public String getDefLanguage() {
        return defLanguage;
    }

    public void setDefLanguage(String defLanguage) {
        this.defLanguage = defLanguage;
    }

    public String getDefCountry() {
        return defCountry;
    }

    public void setDefCountry(String defCountry) {
        this.defCountry = defCountry;
    }

    public String getDefVariant() {
        return defVariant;
    }

    public void setDefVariant(String defVariant) {
        this.defVariant = defVariant;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }
}
