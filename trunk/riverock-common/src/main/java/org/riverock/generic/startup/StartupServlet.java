/*
 * org.riverock.common - Supporting classes, interfaces, and utilities
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.riverock.generic.startup;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * @deprecated use org.riverock.common.startup.StartupServlet
 * @author Sergei Maslyukov
 *         Date: 30.11.2006
 *         Time: 21:11:41
 *         <p/>
 *         $Id$
 */
public class StartupServlet {
    private org.riverock.common.startup.StartupServlet startupServlet = new org.riverock.common.startup.StartupServlet();

    /**
     * @deprecated
     * @param config
     * @throws ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        startupServlet.init(config);
    }

    /**
     * @deprecated
     */
    public void destroy() {
        startupServlet.destroy();
    }
}
