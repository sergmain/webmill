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
package org.riverock.webmill.portlet;

import java.io.File;
import java.io.FileFilter;

import org.riverock.generic.main.CacheDirectory;
import org.riverock.generic.main.CacheFile;
import org.riverock.generic.main.ExtensionFileFilter;
import org.riverock.generic.exception.FileManagerException;
import org.riverock.webmill.main.Constants;
import org.riverock.webmill.config.WebmillConfig;
import org.riverock.common.config.ConfigException;
import org.riverock.common.config.PropertiesProvider;
import org.riverock.interfaces.schema.javax.portlet.PortletType;

import org.apache.log4j.Logger;

/**
 * $Id$
 */
public final class PortletManager {
    private final static Logger log = Logger.getLogger( PortletManager.class );

    private static FileFilter portletFilter = new ExtensionFileFilter(".xml");

    private static CacheDirectory mainDir = null;
    private static CacheDirectory userDir = null;

    private static PortletFile mainPortletFile[] = null;
    private static PortletFile userPortletFile[] = null;

    private static boolean isUserDirectoryExists = true;
    private static final int REFRESH_DELAY_PERIOD = 1000*30;  // scan dir every 30 seconds

    public static PortletFile[] getPortletFileArray()
    {
        PortletFile[] temp = new PortletFile[getCountFile()];

        int idx = 0;
        if (mainPortletFile!=null)
        {
            for (int i=0; i<mainPortletFile.length; i++)
            {
                if (mainPortletFile[i]!=null)
                    temp[idx++] = mainPortletFile[i];
            }
        }
        if (userPortletFile!=null)
        {
            for (int i=0; i<userPortletFile.length; i++)
            {
                if (userPortletFile[i]!=null)
                    temp[idx++] = userPortletFile[i];
            }
        }

        return temp;
    }



    public static int getCountFile()
    {
        return
            (mainPortletFile==null?0:mainPortletFile.length)+
            (userPortletFile==null?0:userPortletFile.length);
    }

    public static int getCountPortlet()
    {
        int count = 0;
        if (mainPortletFile!=null)
        {
            for (int i=0; i<mainPortletFile.length; i++)
            {
                if (mainPortletFile[i]!=null)
                    count += mainPortletFile[i].getPortletDescriptionCount();
            }
        }
        if (userPortletFile!=null)
        {
            for (int i=0; i<userPortletFile.length; i++)
            {
                if (userPortletFile[i]!=null)
                    count += userPortletFile[i].getPortletDescriptionCount();
            }
        }

        return count;
    }

    private static String getCustomDir()
        throws ConfigException
    {
        String dir = null;
        dir = WebmillConfig.getCustomPortletDir();

        return dir;
    }

    public static void init() throws FileManagerException {

        try {
        if (log.isDebugEnabled())
            log.debug("start init portlet CacheDirectory, mainDir "+mainDir);

        if (mainDir==null)
        {
            if (log.isDebugEnabled())
                log.debug("init portlet CacheDirectory. dir - "+
                    PropertiesProvider.getConfigPath() + File.separator + Constants.MILL_PORTLET_DIR);

            mainDir = new CacheDirectory(
                PropertiesProvider.getConfigPath() + File.separator + Constants.MILL_PORTLET_DIR,
                portletFilter
            );
        }

        if (log.isDebugEnabled())
            log.debug("mainPortletFile "+mainPortletFile);

        if (mainPortletFile == null || !mainDir.isUseCache())
        {
            if (mainDir.isNeedReload())
            {
                if (log.isDebugEnabled())
                    log.debug("reinit portlet CacheDirectory. dir - "+
                        PropertiesProvider.getConfigPath() + File.separator + Constants.MILL_PORTLET_DIR);

                mainDir = new CacheDirectory(
                    PropertiesProvider.getConfigPath() + File.separator + Constants.MILL_PORTLET_DIR,
                    portletFilter,
                    REFRESH_DELAY_PERIOD
                );
            }

            if (log.isDebugEnabled())
                log.debug("#2.001 read list file");

            mainDir.processDirectory();

            if (log.isDebugEnabled())
                log.debug("#2.003 array of files - " + mainDir.getFileArray());

            CacheFile cacheFile[] = mainDir.getFileArray();
            mainPortletFile = null;
            mainPortletFile = new PortletFile[cacheFile.length];
            for (int i=0; i<mainDir.getFileArray().length; i++)
            {
                mainPortletFile[i] = new PortletFile( cacheFile[i].getFile());
            }
        }

            if (log.isDebugEnabled())
                log.debug("isUserDirectoryExists - " + isUserDirectoryExists);

        // init Custom portlet list
        if (isUserDirectoryExists)
        {
            if (log.isDebugEnabled())
                log.debug("userDir - " + userDir);

            if (userDir==null)
            {
                String customPortletDir = getCustomDir();

                if (log.isDebugEnabled())
                    log.debug("Custom portlet directory - "+customPortletDir);

                if (customPortletDir!=null && customPortletDir.length()!=0)
                {
//                    try
                    {
                        userDir = new CacheDirectory(
                            customPortletDir,
                            portletFilter,
                            REFRESH_DELAY_PERIOD
                        );
                    }
//                    catch( FileNotFoundException e )
//                    {
//                        isUserDirectoryExists = false;
//                        return;
//                    }
                }
                else
                    return;
            }

            if (log.isDebugEnabled())
                log.debug("userDir "+userDir+", userPortletFile " +userPortletFile);

            if (userPortletFile == null || !userDir.isUseCache())
            {
                if (log.isDebugEnabled())
                {
                    log.debug("check need reload "+userDir);
                    if (userDir!=null) log.debug("check need reload "+userDir.isNeedReload());
                }

                if (userDir!=null && userDir.isNeedReload())
                {
                    if (log.isDebugEnabled())
                        log.debug("reload cache dir ");

                    String customPortletDir = getCustomDir();
                    if (customPortletDir!=null || customPortletDir.length()!=0)
                    {
                        userDir = new CacheDirectory(
                            customPortletDir,
                            portletFilter,
                            REFRESH_DELAY_PERIOD
                        );
                    }
                }

                if (log.isDebugEnabled())
                    log.debug("#2.001 read list file");

                userDir.processDirectory();

                if (log.isDebugEnabled())
                    log.debug("#2.003 array of files - " + userDir.getFileArray());

                CacheFile cacheFile[] = userDir.getFileArray();
                userPortletFile = null;
                userPortletFile = new PortletFile[cacheFile.length];
                for (int i=0; i<userDir.getFileArray().length; i++)
                {
                    PortletFile tempFile = new PortletFile( cacheFile[i].getFile());
                    if (tempFile==null)
                        log.warn("Object PortletFile for "+cacheFile[i].getFile()+" is null");
                    else
                        userPortletFile[i] = tempFile;
                }
            }
        }
        }
        catch(Exception e) {
            String es = "Exception in init()";
            log.error(es, e);
            throw new FileManagerException(es, e);
        }
        catch(Error e) {
            String es = "Error in init()";
            log.error(es, e);
            throw new FileManagerException(es, e);
        }
    }

    public static PortletType getPortletDescription( final String portletName )
            throws FileManagerException
    {
        if (portletName==null)
            return null;

        try {
        init();

            if (log.isDebugEnabled())
                log.debug("mainPortletFile " + mainPortletFile);

        if (mainPortletFile!=null)
        {
            if (log.isDebugEnabled())
                log.debug("mainPortletFile.length " + mainPortletFile.length);

            for (int k=0; k < mainPortletFile.length; k++)
            {
                if (mainPortletFile[k]==null)
                    continue;

                PortletFile mf = mainPortletFile[k];

                if (log.isDebugEnabled())
                    log.debug("PortletFile mf " + mf);

                if (mf!=null)
                {
                    PortletType desc = mf.getPortletDescription(portletName);
                    if (desc != null)
                    {
                        if (log.isDebugEnabled())
                            log.debug("Description for portlet " + portletName + " is found in main directory");

                        return desc;
                    }
                }
                else
                {
                    log.warn("in array 'mainPortletFile' found null object");
                }
            }
        }

        if (log.isDebugEnabled())
            log.debug("Description for portlet " + portletName + " in main directory not found");

        if (userPortletFile!=null)
        {
            for (int k=0; k < userPortletFile.length; k++)
            {
                if (userPortletFile[k]==null)
                    continue;

                PortletFile mf = userPortletFile[k];
                if (mf!=null)
                {
                    PortletType desc = mf.getPortletDescription(portletName);
                    if (desc != null)
                    {
                        if (log.isDebugEnabled())
                            log.debug("Description for portlet " + portletName + " is found in custom directory");

                        return desc;
                    }
                }
                else
                {
                    log.warn("in array 'userPortletFile' found null object");
                }
            }
        }

        if (log.isDebugEnabled())
            log.debug("Description for portlet " + portletName + " not found");

        return null;

        }
        catch(Exception e) {
            String es = "Exception exec PortletManager";
            log.error(es, e);
            throw new FileManagerException(es, e);
        }
        catch(Error e) {
            String es = "Error exec PortletManager";
            log.error(es, e);
            throw new FileManagerException(es, e);
        }
    }
}