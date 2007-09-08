package org.riverock.portlet.manager.navigation;

import org.apache.log4j.Logger;

import org.riverock.interfaces.portal.bean.PortletAlias;
import org.riverock.interfaces.portal.bean.Template;
import org.riverock.interfaces.portal.bean.UrlAlias;
import org.riverock.interfaces.portal.spi.PortalSpiProvider;
import org.riverock.portlet.tools.FacesTools;

/**
 * User: SergeMaslyukov
 * Date: 04.09.2007
 * Time: 22:17:57
 * $Id$
 */
public class NavigationSessionBean {
    private final static Logger log = Logger.getLogger(NavigationSessionBean.class);

    private PortletAlias portletAlias = null;
    private Long currentPortletAliasId = null;

    private UrlAlias urlAlias=null;
    private Long currentUrlAliasId=null;

    private Long dynamicTemplateId;
    private Long popupTemplateId;
    private Long maximazedTemplateId;

    private Long currentSiteId;
    private Long currentSiteLanguageId;

    public NavigationSessionBean() {
    }

    public Long getCurrentSiteLanguageId() {
        return currentSiteLanguageId;
    }

    public void setCurrentSiteLanguageId(Long currentSiteLanguageId) {
        this.currentSiteLanguageId = currentSiteLanguageId;
    }

    public Long getCurrentSiteId() {
        return currentSiteId;
    }

    public void setCurrentSiteId(Long currentSiteId) {
        this.currentSiteId = currentSiteId;
    }

    public Long getDynamicTemplateId() {
        return dynamicTemplateId;
    }

    public void setDynamicTemplateId(Long dynamicTemplateId) {
        this.dynamicTemplateId = dynamicTemplateId;
    }

    public Long getPopupTemplateId() {
        return popupTemplateId;
    }

    public void setPopupTemplateId(Long popupTemplateId) {
        this.popupTemplateId = popupTemplateId;
    }

    public Long getMaximazedTemplateId() {
        return maximazedTemplateId;
    }

    public void setMaximazedTemplateId(Long maximazedTemplateId) {
        this.maximazedTemplateId = maximazedTemplateId;
    }

    public PortletAlias getPortletAlias() {
        return portletAlias;
    }

    public void setPortletAlias(PortletAlias portletAlias) {
        this.portletAlias = portletAlias;
    }

    public Long getCurrentPortletAliasId() {
        return currentPortletAliasId;
    }

    public void setCurrentPortletAliasId(Long currentPortletAliasId) {
        this.currentPortletAliasId = currentPortletAliasId;
    }

    public UrlAlias getUrlAlias() {
        return urlAlias;
    }

    public void setUrlAlias(UrlAlias urlAlias) {
        this.urlAlias = urlAlias;
    }

    public Long getCurrentUrlAliasId() {
        return currentUrlAliasId;
    }

    public void setCurrentUrlAliasId(Long currentUrlAliasId) {
        this.currentUrlAliasId = currentUrlAliasId;
    }
}
