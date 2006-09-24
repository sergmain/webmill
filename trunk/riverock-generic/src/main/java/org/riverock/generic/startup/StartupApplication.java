/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.generic.startup;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.riverock.common.config.ConfigException;
import org.riverock.common.config.ConfigService;
import org.riverock.common.config.PropertiesProvider;
import org.riverock.generic.config.GenericConfig;

/**
 * $Id$
 */

public final class StartupApplication
{
    private final static Logger log = Logger.getLogger( StartupApplication.class );

    private static boolean isInit = false;

    private final static String DEFAULT_DIR_NAME = "mill";
    public static void init() throws ConfigException{
        init(DEFAULT_DIR_NAME, "mill.log4j.properties", "jsmithy");
    }
    public static void init(String defaultNameDir, String log4jFileName, String configPrefix)
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
            System.setProperty("riverock.logging.path", millLogPath);

            PropertyConfigurator.configure(
                PropertiesProvider.getConfigPath() + File.separatorChar + log4jFileName
            );
            log.info("Application path: " + PropertiesProvider.getApplicationPath() );

            isInit = true;
        }
        ConfigService.initLocale();
        GenericConfig.setConfigPrefix(configPrefix);
        GenericConfig.getTZ();
    }

}
