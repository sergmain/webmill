package org.riverock.webmill.portal;

import java.util.Collection;

import javax.servlet.ServletConfig;

import org.riverock.webmill.container.portlet.PortletContainer;
import org.riverock.webmill.container.portlet.PortalInstanceBase;
import org.riverock.webmill.portal.template.PortalTemplateManager;
import org.riverock.webmill.portal.xslt.XsltTransformerManager;
import org.riverock.interfaces.portal.search.PortalIndexer;

/**
 * User: SMaslyukov
 * Date: 26.07.2007
 * Time: 15:37:44
 */
public interface PortalInstance extends PortalInstanceBase {

    public int getPortalMajorVersion();

    public int getPortalMinorVersion();
    
    public Collection<String> getSupportedLocales();

    PortletContainer getPortletContainer();

    ServletConfig getPortalServletConfig();

    PortalIndexer getPortalIndexer();

    ClassLoader getPortalClassLoader();

    PortalTemplateManager getPortalTemplateManager();

    Long getSiteId();

    XsltTransformerManager getXsltTransformerManager();
}
