package org.riverock.portlet.cms.news.bean;

import java.io.Serializable;
import java.util.Locale;

import org.riverock.interfaces.portal.bean.Site;
import org.riverock.common.tools.StringTools;

/**
 * @author Sergei Maslyukov
 *         Date: 23.08.2006
 *         Time: 15:48:23
 */
public class SiteBean implements Serializable, Site {
    private static final long serialVersionUID = 2057005502L;

    private Long siteId=null;
    private Long companyId=null;
    private String siteName=null;
    private boolean cssDynamic;
    private String cssFile=null;
    private boolean registerAllowed;
    private String defLanguage=null;
    private String defCountry=null;
    private String defVariant=null;
    private String adminEmail=null;
    private String properties=null;
    private String portalCharset=null;

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
        this.properties = site.getProperties();
        this.portalCharset = site.getPortalCharset();
    }

    public String getSiteDefaultLocale() {
        if (getDefLanguage()==null && getDefCountry()==null && getDefVariant()==null) {
            return null;
        }

        if (getDefCountry()==null && getDefVariant()==null) {
            return new Locale(getDefLanguage()).toString();
        }
        else if (getDefVariant()==null) {
            return new Locale(getDefLanguage(), getDefCountry()).toString();
        }
        else {
            return new Locale(getDefLanguage(), getDefCountry(), getDefVariant()).toString();
        }
    }

    public void setSiteDefaultLocale(String localeString) {
        Locale locale = StringTools.getLocale(localeString);
        defLanguage = locale.getLanguage();
        defCountry = locale.getCountry();
        defVariant = locale.getVariant();
    }

    public String getPortalCharset() {
        return portalCharset;
    }

    public void setPortalCharset(String portalCharset) {
        this.portalCharset = portalCharset;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
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

    public boolean getCssDynamic() {
        return cssDynamic;
    }

    public void setCssDynamic(boolean cssDynamic) {
        this.cssDynamic = cssDynamic;
    }

    public String getCssFile() {
        return cssFile;
    }

    public void setCssFile(String cssFile) {
        this.cssFile = cssFile;
    }

    public boolean getRegisterAllowed() {
        return registerAllowed;
    }

    public void setRegisterAllowed(boolean registerAllowed) {
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
