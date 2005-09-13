package org.riverock.webmill.container.portlet;

import javax.portlet.RenderRequest;

import org.riverock.webmill.container.bean.SitePortletData;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;

/**
 * @author smaslyukov
 *         Date: 11.08.2005
 *         Time: 19:05:42
 *         $Id$
 */
public interface PortletContentCache {
    void invalidate(String portletName);

    SitePortletData getContent( PortletDefinition portletDefinition );

    void setContent( PortletDefinition portletDefinition, SitePortletData data, RenderRequest renderRequest );
}
