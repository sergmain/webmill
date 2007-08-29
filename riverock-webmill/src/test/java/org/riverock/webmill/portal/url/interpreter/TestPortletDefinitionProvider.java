package org.riverock.webmill.portal.url.interpreter;

import java.io.InputStream;

import org.riverock.webmill.container.definition.DefinitionProcessorFactory;
import org.riverock.webmill.container.definition.PortletDefinitionProcessor;
import org.riverock.webmill.container.portlet.bean.PortletApplication;
import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.portal.url.definition_provider.PortletDefinitionProvider;

/**
 * User: SMaslyukov
 * Date: 21.08.2007
 * Time: 11:26:08
 */
public class TestPortletDefinitionProvider implements PortletDefinitionProvider {

    private PortletApplication portletApplication;

    public TestPortletDefinitionProvider() {
        PortletDefinitionProcessor portletDefinitionProcessor = DefinitionProcessorFactory.getPortletDefinitionProcessor();

        InputStream inputStream = TestPortletDefinitionProvider.class.getResourceAsStream("/xml/site/portlet.xml");
        portletApplication = portletDefinitionProcessor.process(inputStream);
    }

    public PortletDefinition getPortletDefinition(String fullPortletName) {
        for (PortletDefinition portletDefinition : portletApplication.getPortlet()) {
            if (portletDefinition.getFullPortletName().equals(fullPortletName)) {
                return portletDefinition;
            }
        }
        return null;
    }
}
