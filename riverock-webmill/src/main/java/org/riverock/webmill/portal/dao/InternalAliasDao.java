package org.riverock.webmill.portal.dao;

import java.util.List;
import java.util.Observer;

import org.riverock.interfaces.portal.bean.PortletAlias;
import org.riverock.interfaces.portal.bean.UrlAlias;
import org.riverock.webmill.portal.bean.PortletAliasBean;

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

    public List<PortletAliasBean> getPortletAliases(Long siteId);

    public UrlAlias getUrlAlias(Long urlAliasId);

    public Long createUrlAlias(UrlAlias urlAlias);

    public void updateUrlAlias(UrlAlias urlAlias);

    public void deleteUrlAlias(UrlAlias urlAlias);

    public List<UrlAlias> getUrlAliases(Long siteId);

    void addObserver(Observer o);
}
