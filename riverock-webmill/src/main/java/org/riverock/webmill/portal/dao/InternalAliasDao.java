package org.riverock.webmill.portal.dao;

import java.util.List;

import org.riverock.interfaces.portal.bean.PortletAlias;
import org.riverock.interfaces.portal.bean.UrlAlias;

/**
 * User: SergeMaslyukov
 * Date: 04.09.2007
 * Time: 0:05:22
 * $Id$
 */
public interface InternalAliasDao {
    public PortletAlias getPortletAlias(Long portletAliasId);

    public Long createPortletAlias(PortletAlias portletAlias);

    public void updatePortletAlias(PortletAlias portletAlias);

    public void deletePortletAlias(PortletAlias portletAlias);

    public List<PortletAlias> getPortletAliases(Long siteId);

    public UrlAlias getUrlAlias(Long urlAliasId);

    public Long createUrlAlias(UrlAlias urlAlias);

    public void updateUrlAlias(UrlAlias urlAlias);

    public void deleteUrlAlias(UrlAlias urlAlias);

    public List<UrlAlias> getUrlAliases(Long siteId);
}
