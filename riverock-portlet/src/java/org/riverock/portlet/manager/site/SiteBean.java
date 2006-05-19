/**
 * License
 * 
 */
package org.riverock.portlet.manager.site;

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

	
	private Long siteId=null;
	private Long companyId;
	private String siteName;
	private Boolean cssDynamic;
	private String cssFile;
	private Boolean registerAllowed;
	private String defLanguage;
	private String defCountry;
	private String defVariant;
	private String adminEmail;;

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
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1541837116369409115L;

	public Long getSiteId() {
		return siteId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public String getSiteName() {
		return siteName;
	}

	public Boolean getCssDynamic() {
		return cssDynamic;
	}

	public String getCssFile() {
		return cssFile;
	}

	public Boolean getRegisterAllowed() {
		return registerAllowed;
	}

	public String getDefLanguage() {
		return defLanguage;
	}

	public String getDefCountry() {
		return defCountry;
	}

	public String getDefVariant() {
		return defVariant;
	}

	public String getAdminEmail() {
		return adminEmail;
	}
}
