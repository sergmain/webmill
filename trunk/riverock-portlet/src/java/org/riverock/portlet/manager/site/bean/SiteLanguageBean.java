package org.riverock.portlet.manager.site.bean;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.SiteLanguage;

/**
 * @author Sergei Maslyukov
 *         Date: 17.05.2006
 *         Time: 17:38:58
 */
public class SiteLanguageBean implements Serializable, SiteLanguage {
    private static final long serialVersionUID = 2057005504L;

    private Long siteLanguageId;
    private Long siteId;
    private String customLanguage;
    private String nameCustomLanguage;

    public SiteLanguageBean() {
    }

    public SiteLanguageBean(SiteLanguage siteLanguage) {
        this.siteLanguageId = siteLanguage.getSiteLanguageId();
        this.siteId = siteLanguage.getSiteId();
        this.customLanguage = siteLanguage.getCustomLanguage();
        this.nameCustomLanguage = siteLanguage.getNameCustomLanguage();
    }

    public Long getSiteLanguageId() {
        return siteLanguageId;
    }

    public void setSiteLanguageId(Long siteLanguageId) {
        this.siteLanguageId = siteLanguageId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getCustomLanguage() {
        return customLanguage;
    }

    public void setCustomLanguage(String customLanguage) {
        this.customLanguage = customLanguage;
    }

    public String getNameCustomLanguage() {
        return nameCustomLanguage;
    }

    public void setNameCustomLanguage(String nameCustomLanguage) {
        this.nameCustomLanguage = nameCustomLanguage;
    }
}
