package org.riverock.webmill.container.definition;

/**
 * User: SMaslyukov
 * Date: 03.07.2007
 * Time: 20:19:38
 */
public class DefinitionProcessorFactory {
    private final static WebXmlDefinitionProcessor WEB_XML_DEFINITION_PROCESSOR = new WebXmlDefinitionProcessorImpl();
    private final static PortletDefinitionProcessor PORTLET_DEFINITION_PROCESSOR = new JaxbPortletDefinitionProcessorImpl();

    public static PortletDefinitionProcessor getPortletDefinitionProcessor() {
        return PORTLET_DEFINITION_PROCESSOR;
    }

    public static WebXmlDefinitionProcessor getWebXmlDefinitionProcessor() {
        return WEB_XML_DEFINITION_PROCESSOR;
    }
}
