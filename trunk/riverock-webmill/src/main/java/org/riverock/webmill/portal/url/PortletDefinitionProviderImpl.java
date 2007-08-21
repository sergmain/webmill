package org.riverock.webmill.portal.url;

import org.apache.log4j.Logger;

import org.riverock.webmill.container.portlet.bean.PortletDefinition;
import org.riverock.webmill.container.portlet.PortletEntry;
import org.riverock.webmill.container.portlet.PortletContainerException;
import org.riverock.webmill.container.portlet.PortletContainer;

/**
 * User: SMaslyukov
 * Date: 20.08.2007
 * Time: 21:23:01
 */
public class PortletDefinitionProviderImpl implements PortletDefinitionProvider {
    private final static Logger log = Logger.getLogger(PortletDefinitionProviderImpl.class);

    private PortletContainer portletContainer=null;

    public PortletDefinitionProviderImpl(PortletContainer portletContainer) {
        this.portletContainer = portletContainer;
    }

    public PortletDefinition getPortletDefinition(String fullPortletName) {
        PortletEntry entry = null;
        try {
            entry = portletContainer.getPortletInstance(fullPortletName);
        }
        catch (PortletContainerException e) {
            log.error("Error get portlet '" + fullPortletName + "'", e);
        }
        if (entry == null) {
            log.warn("Instance for portlet name "+fullPortletName+" not found");
            return null;
        }
        return entry.getPortletDefinition();
    }
}
