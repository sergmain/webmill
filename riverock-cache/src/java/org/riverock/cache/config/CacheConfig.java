/*

 * org.riverock.cache -- Generic cache implementation

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

 * User: serg_main

 * Date: 28.11.2003

 * Time: 22:01:57

 * @author Serge Maslyukov

 * $Id$

 */



package org.riverock.cache.config;



import java.io.File;

import java.util.Hashtable;

import java.util.HashMap;



import org.riverock.cache.impl.Constants;

import org.riverock.cache.schema.config.CacheConfigType;

import org.riverock.cache.schema.config.CacheClassItemType;

import org.riverock.common.config.ConfigException;

import org.riverock.common.config.ConfigObject;



import org.apache.log4j.Logger;



public class CacheConfig

{

    private static Logger log = Logger.getLogger( CacheConfig.class );



    public static String contextName = "";

//

    private static ConfigObject configObject = null;



    private static boolean isConfigProcessed = false;

    private static HashMap classMap = new HashMap();



    public static CacheConfigType getConfig()

    {

        return (CacheConfigType)configObject.getConfigObject();

    }



    private static Object syncReadConfig = new Object();

    private static void readConfig()

        throws ConfigException

    {

        if (isConfigProcessed)

            return;



        synchronized (syncReadConfig)

        {

            if (isConfigProcessed)

                return;



            configObject = ConfigObject.load(

                Constants.JNDI_CACHE_CONFIG_FILE , "jsmithy-cache.xml", CacheConfigType.class);



            if (log.isDebugEnabled())

                log.debug("#15.006");



            if (getConfig().getClassList()!=null)

            {

                for (int i=0; i<getConfig().getClassList().getClassItemCount(); i++)

                {

                    CacheClassItemType classItem = getConfig().getClassList().getClassItem(i);

                    classMap.put(classItem.getClassName(), classItem);

                }

            }



            isConfigProcessed = true;

        }

    }



//-----------------------------------------------------

// PUBLIC SECTION

//-----------------------------------------------------



    private static Object syncTempDir = new Object();

    public static String getCacheTempDir()

        throws ConfigException

    {

        if (log.isDebugEnabled())

            log.debug("#15.937");



        if (!isConfigProcessed)

            readConfig();



        if (log.isDebugEnabled())

            log.debug("#15.938");



        synchronized(syncTempDir)

        {

            if (Boolean.FALSE.equals( getConfig().getIsTempDirInit() ) )

            {

                String dir = getConfig().getCacheTempDir();

                if (File.separatorChar=='\\')

                {

                    dir.replace( '/', '\\');

                }

                else

                {

                    dir.replace( '\\', '/');

                }

                if (!dir.endsWith( File.separator ))

                    dir += File.separator;



                File dirTest = new File(dir);

                if ( !dirTest.exists() )

                {

                    log.error("Specified temp directory '"+dir+"' not exists. Set to default java input/output temp directory");

                    dir = System.getProperty("java.io.tmpdir");

                }

                getConfig().setCacheTempDir( dir );

                getConfig().setIsTempDirInit( Boolean.TRUE );

            }

            return getConfig().getCacheTempDir();

        }

    }



    private static Object syncDebug = new Object();

    public static String getMillDebugDir()

        throws ConfigException

    {

        if (log.isDebugEnabled())

            log.debug("#15.937.1");



        if (!isConfigProcessed)

            readConfig();



        if (log.isDebugEnabled())

            log.debug("#15.938.1");



        if (getConfig().getIsDebugDirInit().booleanValue() )

            return getConfig().getCacheDebugDir();



        synchronized(syncDebug)

        {

            if (getConfig().getIsDebugDirInit().booleanValue())

                return getConfig().getCacheDebugDir();



            String dir = getConfig().getCacheDebugDir();

            if (File.separatorChar=='\\')

            {

                dir.replace( '/', '\\');

            }

            else

            {

                dir.replace( '\\', '/');

            }

            if (!dir.endsWith( File.separator ))

                dir += File.separator;



            File dirTest = new File(dir);

            if ( !dirTest.exists() )

            {

                log.warn("Specified debug directory '"+dir+"' not exists. Set to default java input/output temp directory");

                dir = System.getProperty("java.io.tmpdir");

            }

            getConfig().setCacheDebugDir( dir );

            getConfig().setIsDebugDirInit( Boolean.TRUE );



            return getConfig().getCacheDebugDir();

        }

    }



    public static CacheClassItemType getClassDefinition(String className)

    {

        if (className==null)

            return null;



        return (CacheClassItemType)classMap.get(className);

    }

}

