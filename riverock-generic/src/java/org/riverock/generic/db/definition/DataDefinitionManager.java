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

/**
 * $Id$
 */
package org.riverock.generic.db.definition;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

import org.riverock.generic.main.CacheDirectory;
import org.riverock.generic.main.CacheFile;
import org.riverock.generic.main.Constants;
import org.riverock.generic.main.ExtensionFileFilter;
import org.riverock.generic.config.GenericConfig;
import org.riverock.common.config.ConfigException;
import org.riverock.common.config.PropertiesProvider;

public class DataDefinitionManager
{
    private static Logger log = Logger.getLogger( "org.riverock.generic.db.definition.DataDefinitionManager" );

    private static FileFilter definitionFilter = new ExtensionFileFilter(".xml");

    private static CacheDirectory mainDir = null;
    private static CacheDirectory userDir = null;

    private static DataDefinitionFile mainDefinitionFile[] = null;
    private static DataDefinitionFile userDefinitionFile[] = null;

    private static boolean isUserDirectoryExists = true;

    public static boolean isNeedReload()
        throws Exception
    {
        try {
        if (mainDir!=null && mainDir.isNeedReload())
            return true;

        if (userDir!=null && userDir.isNeedReload())
            return true;

        return false;
        }
        catch(Exception e) {
            log.error("Exception in isNeedReload()", e);
            throw e;
        }
        catch(Error e) {
            log.error("Error in isNeedReload()", e);
            throw e;
        }
    }

    public static DataDefinitionFile[] getDefinitionFileArray()
    {
        DataDefinitionFile[] temp = new DataDefinitionFile[getCountFile()];

        int idx = 0;
        if (mainDefinitionFile!=null)
        {
            for (int i=0; i<mainDefinitionFile.length; i++)
            {
                if (mainDefinitionFile[i]!=null)
                    temp[idx++] = mainDefinitionFile[i];
            }
        }
        if (userDefinitionFile!=null)
        {
            for (int i=0; i<userDefinitionFile.length; i++)
            {
                if (userDefinitionFile[i]!=null)
                    temp[idx++] = userDefinitionFile[i];
            }
        }

        return temp;
    }

    public static int getCountFile()
    {
        return
            (mainDefinitionFile==null?0:mainDefinitionFile.length)+
            (userDefinitionFile==null?0:userDefinitionFile.length);
    }

    private static String getCustomDir()
        throws ConfigException
    {
        String dir = null;
        dir = GenericConfig.getCustomDefinitionDir();
        return dir;
    }

    public static void init()
            throws Exception
    {
        try
        {
            File dir = new File(PropertiesProvider.getConfigPath() + File.separator + Constants.MILL_DEFINITION_DIR);
            if (!dir.exists())
            {
                log.warn("Directory '"+dir+"' not exists");
                return;
            }

            if (mainDir==null)
                mainDir = new CacheDirectory(dir, definitionFilter);

            if (mainDefinitionFile == null || !mainDir.isUseCache())
            {
                if (mainDir.isNeedReload())
                    mainDir = new CacheDirectory(
                        PropertiesProvider.getConfigPath() + File.separator + Constants.MILL_DEFINITION_DIR,
                        definitionFilter
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

                mainDefinitionFile = null;
                mainDefinitionFile = new DataDefinitionFile[cacheFile.length];
                for (int i=0; i<mainDir.getFileArray().length; i++)
                {
                    if (log.isDebugEnabled())
                        log.debug("CacheFile[i] - " + cacheFile[i]);

                    mainDefinitionFile[i] = new DataDefinitionFile( cacheFile[i].getFile());
                }
            }

            // init Custom portlet list
            if (isUserDirectoryExists)
            {
                if (userDir==null)
                {
                    String customDefinitionDir = getCustomDir();
                    if (customDefinitionDir!=null && customDefinitionDir.length()!=0)
                    {
                        try
                        {
                            userDir = new CacheDirectory(
                                customDefinitionDir,
                                definitionFilter,
                                1000*30 // сканировать директорий каждые 30 секунд
                            );
                        }
                        catch( FileNotFoundException e )
                        {
                            isUserDirectoryExists = false;
                            return;
                        }
                    }
                    else
                        return;
                }

                if (userDefinitionFile == null || !userDir.isUseCache())
                {
                    if (userDir.isNeedReload())
                    {
                        String customDefinitionDir = null;
                        customDefinitionDir = getCustomDir();
                        if (customDefinitionDir!=null && customDefinitionDir.length()!=0)
                        {
                            userDir = new CacheDirectory(
                                customDefinitionDir,
                                definitionFilter,
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
                    userDefinitionFile = null;
                    userDefinitionFile = new DataDefinitionFile[cacheFile.length];
                    for (int i=0; i<userDir.getFileArray().length; i++)
                    {
                        userDefinitionFile[i] = new DataDefinitionFile( cacheFile[i].getFile());
                    }
                }
            }
        }
        catch( Exception e)
        {
            log.error("error get application module", e);
            throw e;
        }
    }

/*
    public static DataDefinitionType getApplModule( String definitionName_ )
            throws Exception
    {
        init();

        for (int k=0; k < mainDefinitionFile.length; k++)
        {
                if (mainDefinitionFile[k]==null)
                continue;

            DataDefinitionFile mf = mainDefinitionFile[k];
            DataDefinitionType mod = mf.getDataDefinition(definitionName_);
            if (mod != null)
            {
                if (log.isDebugEnabled())
                    log.debug("Definition '" + definitionName_ + "' is found");

                return mod;
            }
        }

        if (log.isDebugEnabled())
            log.debug("Definition '" + definitionName_ + "' in main directory not found");

        if (userDefinitionFile!=null)
        {
            for (int k=0; k < userDefinitionFile.length; k++)
            {
                if (userDefinitionFile[k]==null)
                continue;

                DataDefinitionFile mf = userDefinitionFile[k];
                DataDefinitionType desc = mf.getDataDefinition( definitionName_ );
                if (desc != null)
                {
                    if (log.isDebugEnabled())
                        log.debug("Definition '" + definitionName_ + "' is found in custom directory");

                    return desc;
                }
            }
        }

        if (log.isDebugEnabled())
            log.debug("Definition '" + definitionName_ + "' not found");

        return null;
    }
*/
}