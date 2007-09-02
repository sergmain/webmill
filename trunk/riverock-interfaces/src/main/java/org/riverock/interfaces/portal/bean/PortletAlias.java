package org.riverock.interfaces.portal.bean;

/**
 * User: SergeMaslyukov
 * Date: 02.09.2007
 * Time: 12:12:58
 */
public interface PortletAlias {
    
    Long getPortletAliasId();

    Long getSiteId();

    Long getTemplateId();

    Long getPortletNameId();

    String getShortUrl();
}
