package org.riverock.webmill.portal.bean;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

import org.riverock.interfaces.portal.bean.PortletAlias;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:13:35
 */
@Entity
@Table(name="WM_PORTAL_PORTLET_ALIAS")
@TableGenerator(
    name="TABLE_PORTLET_ALIAS",
    table="WM_PORTAL_IDS",
    pkColumnName = "sequence_name",
    valueColumnName = "sequence_next_value",
    pkColumnValue = "wm_portal_portlet_alias",
    allocationSize=1,
    initialValue = 1
)public class PortletAliasBean implements PortletAlias {
    
    @Id
    @GeneratedValue(strategy= GenerationType.TABLE, generator = "TABLE_PORTLET_ALIAS")
    @Column(name="ID_PORTLET_ALIAS")
    private Long portletAliasId;

    @Column(name="ID_SITE")
    private Long siteId;

    @Column(name="ID_TEMPLATE")
    private Long templateId;

    @Column(name="ID_PORTLET_NAME")
    private Long portletNameId;

    @Column(name="SHORT_URL")
    private String shortUrl;

    public PortletAliasBean() {
    }

    public PortletAliasBean(PortletAlias portletAlias) {
        this.portletAliasId = portletAlias.getPortletAliasId();
        this.siteId = portletAlias.getSiteId();
        this.templateId = portletAlias.getTemplateId();
        this.portletNameId = portletAlias.getPortletNameId();
        this.shortUrl = portletAlias.getShortUrl();
    }

    public Long getPortletAliasId() {
        return portletAliasId;
    }

    public void setPortletAliasId(Long portletAliasId) {
        this.portletAliasId = portletAliasId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getPortletNameId() {
        return portletNameId;
    }

    public void setPortletNameId(Long portletNameId) {
        this.portletNameId = portletNameId;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
