package org.riverock.portlet.webclip.manager;

import java.io.Serializable;

/**
 * User: SMaslyukov
 * Date: 18.05.2007
 * Time: 16:29:34
 */
public class WebclipSessionBean implements Serializable {
    private static final long serialVersionUID = 7557005500L;

    private Long catalogLanguageId;
    private String urls;

    public WebclipSessionBean() {
    }

    public Long getCatalogLanguageId() {
        return catalogLanguageId;
    }

    public void setCatalogLanguageId(Long catalogLanguageId) {
        this.catalogLanguageId = catalogLanguageId;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }
}
