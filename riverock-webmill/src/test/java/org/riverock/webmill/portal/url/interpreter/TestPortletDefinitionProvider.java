package org.riverock.webmill.portal.url.interpreter;

import org.riverock.webmill.portal.url.definition_provider.PortletDefinitionProvider;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;

/**
 * User: SMaslyukov
 * Date: 21.08.2007
 * Time: 11:26:08
 */
public class TestPortletDefinitionProvider implements PortletDefinitionProvider {
    public PortletDefinition getPortletDefinition(String fullPortletName) {
        PortletDefinition definition=null;
//        definition = new PortletDefinition();
        return definition; 
    }
}
