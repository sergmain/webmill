package org.riverock.portlet.manager.navigation.bean;

import org.riverock.interfaces.portal.bean.PortletAlias;

/**
 * User: SergeMaslyukov
 * Date: 04.09.2007
 * Time: 22:19:45
 * $Id$
 */
public class PortletAliasBean implements PortletAlias {

    private Long portletAliasId;

    private Long siteId;

    private Long templateId;

    private Long portletNameId;

    private String shortUrl;

    private String portletName;

    private String templateName;

    public PortletAliasBean() {
    }

    public PortletAliasBean(PortletAlias portletAlias, String portletName, String templateName) {
        this.portletAliasId = portletAlias.getPortletAliasId();
        this.siteId = portletAlias.getSiteId();
        this.templateId = portletAlias.getTemplateId();
        this.portletNameId = portletAlias.getPortletNameId();
        this.shortUrl = portletAlias.getShortUrl();
        this.portletName = portletName;
        this.templateName = templateName;
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
