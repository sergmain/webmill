package org.riverock.portlet.manager.navigation.bean;

import org.riverock.interfaces.portal.bean.UrlAlias;

/**
 * User: SergeMaslyukov
 * Date: 04.09.2007
 * Time: 22:21:11
 * $Id$
 */
public class UrlAliasBean implements UrlAlias {

    private Long urlAliasId;

    private Long siteId;

    private String url;

    private String alias;

    public UrlAliasBean() {
    }

    public UrlAliasBean(UrlAlias urlAlias) {
        this.urlAliasId = urlAlias.getUrlAliasId();
        this.siteId = urlAlias.getSiteId();
        this.url = urlAlias.getUrl();
        this.alias = urlAlias.getAlias();
    }

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
