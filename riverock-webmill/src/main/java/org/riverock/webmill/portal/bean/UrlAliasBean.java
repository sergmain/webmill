package org.riverock.webmill.portal.bean;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

import org.riverock.interfaces.portal.bean.UrlAlias;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:08:19
 */
@Entity
@Table(name="WM_PORTAL_URL_ALIAS")
@TableGenerator(
    name="TABLE_URL_ALIAS",
    table="WM_PORTAL_IDS",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_portal_url_alias",
    allocationSize=1,
    initialValue = 1
)public class UrlAliasBean implements UrlAlias {

    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_URL_ALIAS")
    @Column(name="ID_URL_ALIAS")
    private Long urlAliasId;

    @Column(name="ID_SITE")
    private Long siteId;

    @Column(name="URL")
    private String url;

    @Column(name="ALIAS")
    private String alias;

    public UrlAliasBean() {
    }

    public UrlAliasBean(Long urlAliasId, Long siteId, String url, String alias) {
        this.urlAliasId = urlAliasId;
        this.siteId = siteId;
        this.url = url;
        this.alias = alias;
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

    public String toString() {
        return "["+urlAliasId+";siteId="+siteId+";url="+url+";alias="+alias+"]";
    }
}
