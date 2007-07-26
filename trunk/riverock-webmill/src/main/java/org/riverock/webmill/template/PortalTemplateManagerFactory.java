package org.riverock.webmill.template;

/**
 * User: SMaslyukov
 * Date: 26.07.2007
 * Time: 17:47:21
 */
public class PortalTemplateManagerFactory {
    
    public static PortalTemplateManager getInstance(Long siteId) {
        return PortalTemplateManagerImpl.getInstance(siteId);
    }
}
