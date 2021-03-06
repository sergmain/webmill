package org.riverock.webmill.portal.template;

import java.util.Map;
import java.util.HashMap;

/**
 * User: SMaslyukov
 * Date: 26.07.2007
 * Time: 17:47:21
 *
 * $Id: PortalFrontController.java 1370 2007-08-28 13:53:47Z serg_main $
 */
public class PortalTemplateManagerFactory {
    
    private final static Map<Long, PortalTemplateManager> managers = new HashMap<Long, PortalTemplateManager>(PortalTemplateManagerImpl.INIT_COUNT_OF_SITE);

    public static PortalTemplateManager getInstance(Long siteId) {
        PortalTemplateManager manager = managers.get(siteId);
        if (manager==null) {
            synchronized(PortalTemplateManagerImpl.class) {
                manager = managers.get(siteId);
                if (manager!=null) {
                    return null;
                }
                manager = new PortalTemplateManagerImpl(siteId);
                managers.put(siteId, manager);
                return manager;
            }
        }
        return manager;
    }

    public static void destroyAll() {
        if (managers!=null) {
            for (PortalTemplateManager portalTemplateManager : managers.values()) {
                portalTemplateManager.destroy();
            }
            managers.clear();
        }
    }
}
