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
package org.riverock.generic.config;

import java.io.File;
import java.util.Hashtable;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import org.riverock.generic.main.Constants;
import org.riverock.generic.schema.config.CustomDirsType;
import org.riverock.generic.schema.config.DatabaseConnectionType;
import org.riverock.generic.schema.config.DateTimeSavingType;
import org.riverock.generic.schema.config.DateTimeSavingTypeSequence;
import org.riverock.generic.schema.config.GenericConfigType;
import org.riverock.common.config.ConfigObject;
import org.riverock.common.config.ConfigException;

import org.apache.log4j.Logger;

public class GenericConfig
{
    private static Logger log = Logger.getLogger( GenericConfig.class );

    public static String contextName = "";
//
    private static ConfigObject configObject = null;
    private static Hashtable dbConfig = null;
    private static TimeZone currentTimeZone = null;

    private static boolean isConfigProcessed = false;
    private static String configPrefix = "jsmithy";

    public static String getConfigPrefix() {
        return configPrefix;
    }

    public static void setConfigPrefix(String configPrefix) {
        GenericConfig.configPrefix = configPrefix;
    }

    public static GenericConfigType getConfig()
    {
        return (GenericConfigType)configObject.getConfigObject();
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
                Constants.JNDI_GENERIC_CONFIG_FILE , configPrefix+"-generic.xml", GenericConfigType.class
            );

            if (log.isDebugEnabled())
                log.debug("#15.006");

            if (dbConfig != null)
            {
                dbConfig.clear();
                dbConfig = null;
            }

            dbConfig = new Hashtable( getConfig().getDatabaseConnectionCount() );
            for (int i = 0; i < getConfig().getDatabaseConnectionCount(); i++)
            {
                DatabaseConnectionType dbc =  getConfig().getDatabaseConnection(i);
                dbConfig.put( dbc.getName(), dbc);
            }

            log.warn("Name default DB connect " + getConfig().getDefaultConnectionName());

            isConfigProcessed = true;
        }
    }

//-----------------------------------------------------
// PUBLIC SECTION
//-----------------------------------------------------

    private static Object syncTZ = new Object();
    public static TimeZone getTZ()
        throws ConfigException
    {
        if (log.isDebugEnabled())
            log.debug("GenericConfig.getTZ() #1");

        if (!isConfigProcessed)
            readConfig();

        if (currentTimeZone!=null)
            return currentTimeZone;

        if (log.isDebugEnabled())
            log.debug("GenericConfig.getTZ() #3 Set new TimeZone");

        synchronized(syncTZ)
        {
            if (currentTimeZone!=null)
                return currentTimeZone;

            DateTimeSavingType dts = getConfig().getDTS();
            if (dts.getDateTimeSavingTypeSequence2()!= null )
            {
                String nameTimeZone = dts.getDateTimeSavingTypeSequence2().getTimeZoneName();

                // work around NPE in TimeZone.getTimeZone
                if (nameTimeZone!=null && nameTimeZone.trim().length()!=0)
                {

                    if (log.isDebugEnabled())
                        log.debug("GenericConfig.getTZ(). Set TimeZone to "+nameTimeZone);

                    TimeZone tz = TimeZone.getTimeZone( nameTimeZone );
                    if (tz == null)
                    {
                        log.fatal("TimeZone '"+nameTimeZone+"' not found. You must correct NameTimeZone element in config file");
                        return null;
                    }
                    currentTimeZone = tz;
                    TimeZone.setDefault( currentTimeZone );
                    return tz;
                }
                log.fatal("<NameTimeZone> element not found. You must correct config file.");
                return null;
            }
            else if ( dts.getDateTimeSavingTypeSequence()!= null )
            {
                DateTimeSavingTypeSequence dtSeq = dts.getDateTimeSavingTypeSequence();

                currentTimeZone =
                    new SimpleTimeZone(
                        dtSeq.getRawOffset().intValue(),
                        dtSeq.getId(),
                        dtSeq.getStart().getMonth().intValue(),
                        dtSeq.getStart().getDay().intValue(),
                        dtSeq.getStart().getDayOfWeek().intValue(),
                        dtSeq.getStart().getTime().intValue(),
                        dtSeq.getEnd().getMonth().intValue(),
                        dtSeq.getEnd().getDay().intValue(),
                        dtSeq.getEnd().getDayOfWeek().intValue(),
                        dtSeq.getEnd().getTime().intValue()
                    );
                TimeZone.setDefault( currentTimeZone );
                return currentTimeZone;
            }
            log.fatal("<DTS> element present, but is empty. You must correct <DTS> element in config file.");
            return null;
        }
    }

    private static Object syncTempDir = new Object();
    public static String getGenericTempDir()
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
                String dir = getConfig().getGenericTempDir();
                if (File.separatorChar=='\\')
                    dir = dir.replace( '/', '\\');
                else
                    dir = dir.replace( '\\', '/');
                
                if (!dir.endsWith( File.separator ))
                    dir += File.separator;

                File dirTest = new File(dir);
                if ( !dirTest.exists() )
                {
                    log.error("Specified temp directory '"+dir+"' not exists. Set to default java input/output temp directory");
                    dir = System.getProperty("java.io.tmpdir");
                }
                getConfig().setGenericTempDir( dir );
                getConfig().setIsTempDirInit( Boolean.TRUE );
            }
            return getConfig().getGenericTempDir();
        }
    }

    private static Object syncDebug = new Object();
    public static String getGenericDebugDir()
        throws ConfigException
    {
        if (log.isDebugEnabled())
            log.debug("#15.937.1");

        if (!isConfigProcessed)
            readConfig();

        if (log.isDebugEnabled())
            log.debug("#15.938.1");

        if (getConfig().getIsDebugDirInit().booleanValue() )
            return getConfig().getGenericDebugDir();

        if (log.isDebugEnabled())
            log.debug("#15.938.2");

        synchronized(syncDebug)
        {
            if (getConfig().getIsDebugDirInit().booleanValue())
                return getConfig().getGenericDebugDir();

            String dir = getConfig().getGenericDebugDir();
            if (dir==null)
                dir = System.getProperty("java.io.tmpdir");
            else
            {
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
            }
            getConfig().setGenericDebugDir( dir );
            getConfig().setIsDebugDirInit( Boolean.TRUE );

            if (log.isDebugEnabled())
                log.debug("#15.938.3");

            return getConfig().getGenericDebugDir();
        }
    }

    public static DatabaseConnectionType getDatabaseConnection(String connectionName)
        throws ConfigException
    {
        if (log.isDebugEnabled())
            log.debug("#15.909");

        if (!isConfigProcessed)
            readConfig();

        if (log.isDebugEnabled())
            log.debug("#15.910");

        return (DatabaseConnectionType)dbConfig.get( connectionName );
    }


    public static String getDBconnectClassName(String connectionName)
        throws ConfigException
    {
        if (log.isDebugEnabled())
            log.debug("#15.911");

        if (!isConfigProcessed)
            readConfig();

        if (log.isDebugEnabled())
            log.debug("#15.912");

        return getDatabaseConnection(connectionName).getConnectionClass();
    }

    public static String getDefaultConnectionName()
        throws ConfigException
    {
        if (log.isDebugEnabled())
            log.debug("#15.951");

        if (!isConfigProcessed)
            readConfig();

        if (log.isDebugEnabled())
            log.debug("#15.952");

        return getConfig().getDefaultConnectionName();
    }
    public static String getMailSMTPHost()
        throws ConfigException
    {
        if (log.isDebugEnabled())
            log.debug("#15.927");

        if (!isConfigProcessed)
            readConfig();

        if (log.isDebugEnabled())
            log.debug("#15.928");

        return getConfig().getMailHost();
    }

    public static String getCustomDefinitionDir()
        throws ConfigException
    {
        if (log.isDebugEnabled())
            log.debug("#16.910");

        if (!isConfigProcessed)
            readConfig();

        if (log.isDebugEnabled())
            log.debug("#16.911");

        CustomDirsType dirs = getConfig().getCustomDirs();
        if (dirs==null)
            return null;

        return dirs.getCustomDataDefinitionDir();
    }

}
