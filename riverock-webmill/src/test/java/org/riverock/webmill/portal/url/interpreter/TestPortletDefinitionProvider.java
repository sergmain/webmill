package org.riverock.webmill.portal.url.interpreter;

import org.riverock.webmill.portal.url.PortletDefinitionProvider;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;

/**
 * User: SMaslyukov
 * Date: 21.08.2007
 * Time: 11:26:08
 */
public class TestPortletDefinitionProvider implements PortletDefinitionProvider {
    public PortletDefinition getPortletDefinition(String fullPortletName) {
        PortletDefinition definition = new PortletDefinition();
        return definition; 
    }
}
