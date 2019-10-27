package org.riverock.webmill.portal.aliases;

import java.util.Observer;

import org.riverock.interfaces.portal.bean.PortletAlias;
import org.riverock.webmill.portal.bean.PortletAliasBean;

/**
 * User: SMaslyukov
 * Date: 23.08.2007
 * Time: 12:55:47
 */
public interface PortletAliaslProvider extends Observer {
    PortletAliasBean getAlias(Long siteId, String url);
}