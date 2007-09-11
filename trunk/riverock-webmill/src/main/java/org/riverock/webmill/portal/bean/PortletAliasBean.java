package org.riverock.webmill.portal.bean;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

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

    @Transient
    private String portletName;

    @Transient
    private String templateName;

    @Transient
    private Locale locale;

    public PortletAliasBean() {
    }

    public PortletAliasBean(Long portletAliasId, Long siteId, Long templateId, Long portletNameId, String shortUrl) {
        this.portletAliasId = portletAliasId;
        this.siteId = siteId;
        this.templateId = templateId;
        this.portletNameId = portletNameId;
        this.shortUrl = shortUrl;
    }

    public PortletAliasBean(PortletAlias portletAlias) {
        this.portletAliasId = portletAlias.getPortletAliasId();
        this.siteId = portletAlias.getSiteId();
        this.templateId = portletAlias.getTemplateId();
        this.portletNameId = portletAlias.getPortletNameId();
        this.shortUrl = portletAlias.getShortUrl();
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getPortletName() {
        return portletName;
    }

    public void setPortletName(String portletName) {
        this.portletName = portletName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
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
