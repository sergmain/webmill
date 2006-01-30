package org.riverock.webmill.portal.listener;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

/**
 * @author smaslyukov
 *         Date: 08.07.2005
 *         Time: 18:56:09
 *         $Id$
 */
public class PortalContextListener implements ServletContextListener {
    private final static Logger log = Logger.getLogger(PortalContextListener.class);

    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        if (log.isDebugEnabled()) {
            log.debug("New context is initialized");
            log.debug("   context name: " + context.getServletContextName());
            log.debug("   context realPath: " + context.getRealPath("/"));
        }
    }

    public void contextDestroyed(ServletContextEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
