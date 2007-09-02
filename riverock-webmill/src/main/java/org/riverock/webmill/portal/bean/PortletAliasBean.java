package org.riverock.webmill.portal.bean;

import org.riverock.interfaces.portal.bean.PortletAlias;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:13:35
 */
public class PortletAliasBean implements PortletAlias {
    
    private Long portletAliasId;

    private Long siteId;

    private Long templateId;

    private String portletName;

    private String shortUrl;

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

    public String getPortletName() {
        return portletName;
    }

    public void setPortletName(String portletName) {
        this.portletName = portletName;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
