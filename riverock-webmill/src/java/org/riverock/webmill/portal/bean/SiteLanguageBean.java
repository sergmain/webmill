package org.riverock.webmill.portal.bean;

import java.io.Serializable;

import org.riverock.interfaces.portal.bean.SiteLanguage;

/**
 * @author SergeMaslyukov
 *         Date: 27.01.2006
 *         Time: 14:52:56
 *         $Id$
 */
public class SiteLanguageBean implements Serializable,  SiteLanguage {
    private static final long serialVersionUID = 1055005503L;

    private Long siteLanguageId;
    private Long siteId;
    private String customLanguage;
    private String nameCustomLanguage;

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