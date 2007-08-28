package org.riverock.webmill.portal.url.definition_provider;

import org.riverock.webmill.container.portlet.bean.PortletDefinition;

/**
 * User: SMaslyukov
 * Date: 20.08.2007
 * Time: 21:22:10
 */
public interface PortletDefinitionProvider {
    PortletDefinition getPortletDefinition(String fullPortletName);
}
