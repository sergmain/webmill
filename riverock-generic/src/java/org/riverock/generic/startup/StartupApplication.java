/*
 * org.riverock.generic -- Database connectivity classes
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
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
 *
 */

package org.riverock.generic.startup;

import java.io.File;

import org.riverock.common.config.PropertiesProvider;
import org.riverock.common.config.ConfigService;
import org.riverock.common.config.ConfigException;
import org.riverock.generic.config.GenericConfig;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;

/**
 * $Id$
 */

public final class StartupApplication
{
    private static Logger cat = Logger.getLogger( "org.riverock.generic.startup.StartupApplication" );

    private static boolean isInit = false;

    private final static String DEFAULT_DIR_NAME = "mill";
    public static void init() throws ConfigException{
        init(DEFAULT_DIR_NAME);
    }
    public static void init(String defaultNameDir)
        throws ConfigException
    {
        if (!isInit)
        {
            PropertiesProvider.setApplicationPath(
                System.getProperties().getProperty("user.dir")
            );

            PropertiesProvider.setConfigPath(
                PropertiesProvider.getApplicationPath() +
                (PropertiesProvider.getApplicationPath().endsWith(File.separator)?"":File.separator) +
                defaultNameDir
            );

            PropertiesProvider.setIsServletEnv( false );

            String millLogPath = PropertiesProvider.getConfigPath() + File.separatorChar + "log";
            File tempDir = new File(millLogPath);
            if (!tempDir.exists())
            {
                tempDir.mkdir();
            }
            System.setProperty("mill.logging.path", millLogPath);

            PropertyConfigurator.configure(
                PropertiesProvider.getConfigPath() + File.separatorChar + "mill.log4j.properties"
            );
            cat.info("Application path: " + PropertiesProvider.getApplicationPath() );

            isInit = true;
        }
        ConfigService.initLocale();
        GenericConfig.getTZ();
    }

}
