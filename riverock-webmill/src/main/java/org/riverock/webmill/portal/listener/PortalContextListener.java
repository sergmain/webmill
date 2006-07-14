/*
 * org.riverock.webmill -- Portal framework implementation
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 *
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */
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