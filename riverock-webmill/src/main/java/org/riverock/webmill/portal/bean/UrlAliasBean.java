package org.riverock.webmill.portal.bean;

import org.riverock.interfaces.portal.bean.UrlAlias;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:08:19
 */
public class UrlAliasBean implements UrlAlias {

    private Long urlAliasId;

    private Long siteId;

    private String url;

    private String alias;

    public Long getUrlAliasId() {
        return urlAliasId;
    }

    public void setUrlAliasId(Long urlAliasId) {
        this.urlAliasId = urlAliasId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
