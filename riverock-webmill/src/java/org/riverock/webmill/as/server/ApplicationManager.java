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

/**
 * $Id$
 */
package org.riverock.webmill.as.server;

import java.io.File;
import java.io.FileFilter;

import org.riverock.generic.main.CacheDirectory;
import org.riverock.generic.main.CacheFile;
import org.riverock.generic.main.ExtensionFileFilter;
import org.riverock.webmill.main.Constants;
import org.riverock.webmill.schema.appl_server.ApplicationModuleType;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.common.config.ConfigException;
import org.riverock.common.config.PropertiesProvider;

import org.apache.log4j.Logger;

public class ApplicationManager
{
    private static Logger log = Logger.getLogger( "org.riverock.webmill.as.server.ApplicationManager" );

    private static FileFilter applFilter = new ExtensionFileFilter(".xml");

    private static CacheDirectory mainDir = null;
    private static CacheDirectory userDir = null;

    private static ApplicationFile mainApplFile[] = null;
    private static ApplicationFile userApplFile[] = null;

    private static boolean isUserDirectoryExists = true;

    private static boolean isInit = false;

    public static int getCountFile()
    {
        return
            (mainApplFile==null?0:mainApplFile.length)+
            (userApplFile==null?0:userApplFile.length);
    }

    public static int getCountModule()
    {
        int count = 0;
        if (mainApplFile!=null)
        {
            for (int i=0; i<mainApplFile.length; i++)
            {
                if (mainApplFile[i]!=null)
                    count += mainApplFile[i].getApplicationCount();
            }
        }
        if (userApplFile!=null)
        {
            for (int i=0; i<userApplFile.length; i++)
            {
                if (userApplFile[i]!=null)
                    count += userApplFile[i].getApplicationCount();
            }
        }

        return count;
    }

    private static String getCustomDir()
        throws ConfigException
    {
        String dir = null;
        dir = WebmillConfig.getCustomApplDir();
/*
        try {
            InitialContext ic = new InitialContext();
            dir = (String) ic.lookup("java:comp/env/"+ Constants.JNDI_CUSTOM_APPL_DIR );
            if (cat.isDebugEnabled())
                cat.debug("ConfigFile - "+ dir);
        }
        catch (NamingException e)
        {
            dir = null;
            cat.warn("Custom application dir enviropment in JNDI not defined", e);
        }
*/
        return dir;
    }

    public static void init()
            throws Exception
    {
        try
        {
            File dir = new File(PropertiesProvider.getConfigPath() + File.separator + Constants.MILL_APPL_DIR);
            if (!dir.exists())
            {
                log.warn("Directory '"+dir+"' not exists");
                return;
            }

            if (mainDir==null)
                mainDir = new CacheDirectory(
                    PropertiesProvider.getConfigPath() + File.separator + Constants.MILL_APPL_DIR,
                    applFilter);

            if (mainApplFile == null || !mainDir.isUseCache())
            {
                if (mainDir.isNeedReload())
                    mainDir = new CacheDirectory(
                        PropertiesProvider.getConfigPath() + File.separator + Constants.MILL_APPL_DIR,
                        applFilter
                    );

                if (log.isDebugEnabled())
                    log.debug("#2.001 read list file");

                mainDir.processDirectory();

                if (log.isDebugEnabled())
                    log.debug("#2.003 array of files - " + mainDir.getFileArray());

                CacheFile cacheFile[] = mainDir.getFileArray();

                if (log.isDebugEnabled())
                {
                    log.debug("CacheFile - " + cacheFile);
                    if ( cacheFile!=null )
                        log.debug( "cacheFile.length - "+cacheFile.length);
                }

                mainApplFile = null;
                mainApplFile = new ApplicationFile[cacheFile.length];
                for (int i=0; i<mainDir.getFileArray().length; i++)
                {
                    if (log.isDebugEnabled())
                        log.debug("CacheFile[i] - " + cacheFile[i]);

                    mainApplFile[i] = new ApplicationFile( cacheFile[i].getFile());
                }
            }

            // init Custom portlet list
            if (isUserDirectoryExists)
            {
                if (userDir==null)
                {
                    String customApplDir = getCustomDir();
                    if (customApplDir!=null && customApplDir.length()!=0)
                    {
//                        try
                        {
                            userDir = new CacheDirectory(
                                customApplDir,
                                applFilter,
                                1000*30 // сканировать директорий каждые 30 секунд
                            );
                        }
//                        catch( FileNotFoundException e )
//                        {
//                            isUserDirectoryExists = false;
//                            return;
//                        }
                    }
                    else
                        return;
                }

                if (userApplFile == null || !userDir.isUseCache())
                {
                    if (userDir.isNeedReload())
                    {
                        String customApplDir = null;
                        customApplDir = getCustomDir();
                        if (customApplDir!=null && customApplDir.length()!=0)
                        {
                            userDir = new CacheDirectory(
                                customApplDir,
                                applFilter,
                                1000*30 // сканировать директорий каждые 30 секунд
                            );
                        }
                    }

                    if (log.isDebugEnabled())
                        log.debug("#2.001 read list file");

                    userDir.processDirectory();

                    if (log.isDebugEnabled())
                        log.debug("#2.003 array of files - " + userDir.getFileArray());

                    CacheFile cacheFile[] = userDir.getFileArray();
                    userApplFile = null;
                    userApplFile = new ApplicationFile[cacheFile.length];
                    for (int i=0; i<userDir.getFileArray().length; i++)
                    {
                        userApplFile[i] = new ApplicationFile( cacheFile[i].getFile());
                    }
                }
            }
        }
        catch( Exception e)
        {
            log.error("error get application module", e);
            throw e;
        }
        isInit = true;
    }

    public static ApplicationModuleType getApplModule(String moduleName )
            throws Exception
    {
        if (moduleName==null)
            return null;

        init();

        for (int k=0; k < mainApplFile.length; k++)
        {
            if (mainApplFile[k]==null)
                continue;

            ApplicationFile mf = mainApplFile[k];
            ApplicationModuleType mod = mf.getApplModule(moduleName);
            if (mod != null)
            {
                if (log.isDebugEnabled())
                    log.debug("Module '" + moduleName + "' is found");

                return mod;
            }
        }

        if (log.isDebugEnabled())
            log.debug("Application '" + moduleName + "' in main directory not found");

        if (userApplFile!=null)
        {
            for (int k=0; k < userApplFile.length; k++)
            {
                if (userApplFile[k]==null)
                    continue;

                ApplicationFile mf = userApplFile[k];
                ApplicationModuleType desc = mf.getApplModule(moduleName);
                if (desc != null)
                {
                    if (log.isDebugEnabled())
                        log.debug("Application '" + moduleName + "' is found in custom directory");

                    return desc;
                }
            }
        }

        if (log.isDebugEnabled())
            log.debug("Application '" + moduleName + "' not found");

        return null;
    }
}