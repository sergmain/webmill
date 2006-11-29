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

import org.riverock.common.config.ConfigException;

/**
 * @deprecated use org.riverock.common.startup.StartupApplication
 * @author Sergei Maslyukov
 *         Date: 29.11.2006
 *         Time: 22:14:40
 *         <p/>
 *         $Id$
 */
public class StartupApplication {

    /**
     * @deprecated
     * @throws ConfigException
     */
    public static void init() throws ConfigException {
        org.riverock.common.startup.StartupApplication.init();
    }

    /**
     * @deprecated 
     * @param defaultNameDir
     * @param log4jFileName
     * @param configPrefix
     * @throws ConfigException
     */
    public static void init(String defaultNameDir, String log4jFileName, String configPrefix) throws ConfigException {
        org.riverock.common.startup.StartupApplication.init(defaultNameDir, log4jFileName, configPrefix);
    }
}
