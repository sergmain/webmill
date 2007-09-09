package org.riverock.webmill.portal.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Observer;

import org.riverock.interfaces.portal.bean.PortletAlias;
import org.riverock.interfaces.portal.bean.UrlAlias;
import org.riverock.webmill.portal.bean.UrlAliasBean;

/**
 * User: SergeMaslyukov
 * Date: 09.09.2007
 * Time: 22:27:32
 * $Id$
 */
public class OfflineInternalAliasDao implements InternalAliasDao {

    private List<UrlAlias> urlAliases = (List)Arrays.asList(
        new UrlAliasBean(1L, 16L, "/mill", "/"),
        new UrlAliasBean(2L, 16L, "/mill/ctx", "/"),
        new UrlAliasBean(3L, 16L, "/aaa", "/mill"),
        new UrlAliasBean(4L, 16L, "/ccc", "/aaa"),
        new UrlAliasBean(5L, 16L, "/ddd", "/rrr")
    );

    public PortletAlias getPortletAlias(Long portletAliasId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createPortletAlias(PortletAlias portletAlias) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updatePortletAlias(PortletAlias portletAlias) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deletePortletAlias(PortletAlias portletAlias) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<PortletAlias> getPortletAliases(Long siteId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public UrlAlias getUrlAlias(Long urlAliasId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Long createUrlAlias(UrlAlias urlAlias) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateUrlAlias(UrlAlias urlAlias) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteUrlAlias(UrlAlias urlAlias) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<UrlAlias> getUrlAliases(Long siteId) {
        return urlAliases;
    }

    public void addObserver(Observer o) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
