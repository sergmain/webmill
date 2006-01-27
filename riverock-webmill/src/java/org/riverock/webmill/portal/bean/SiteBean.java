package org.riverock.webmill.portal.bean;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 14:29:04
 *         $Id$
 */
public class SiteBean {

    private Long siteId;
    private Long companyId;
    private Boolean isCssDynamic = false;
    private Boolean isRegisterAllowed = false;
    private Boolean isActivateEmailOrder = false;
    private String defLanguage;
    private String defCountry;
    private String defVariant;
    private String siteName;
    private String adminEmail;
    private String cssFile = "/front_styles.css";

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

    public Boolean getCssDynamic() {
        return isCssDynamic;
    }

    public void setCssDynamic(Boolean cssDynamic) {
        isCssDynamic = cssDynamic;
    }

    public Boolean getRegisterAllowed() {
        return isRegisterAllowed;
    }

    public void setRegisterAllowed(Boolean registerAllowed) {
        isRegisterAllowed = registerAllowed;
    }

    public Boolean getActivateEmailOrder() {
        return isActivateEmailOrder;
    }

    public void setActivateEmailOrder(Boolean activateEmailOrder) {
        isActivateEmailOrder = activateEmailOrder;
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

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getCssFile() {
        return cssFile;
    }

    public void setCssFile(String cssFile) {
        this.cssFile = cssFile;
    }

    public String getOrderEmail() {
        return orderEmail;
    }

    public void setOrderEmail(String orderEmail) {
        this.orderEmail = orderEmail;
    }

    private String orderEmail;

    public SiteBean() {}

}
