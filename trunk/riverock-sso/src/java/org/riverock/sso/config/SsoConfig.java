/*
 * org.riverock.sso -- Single Sign On implementation
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
package org.riverock.sso.config;

import java.io.File;

import org.riverock.common.config.ConfigObject;
import org.riverock.common.config.ConfigException;
import org.riverock.sso.main.Constants;
import org.riverock.sso.schema.config.SsoConfigType;
import org.riverock.sso.schema.config.AuthType;

import org.apache.log4j.Logger;

/**
 * User: serg_main
 * Date: 28.11.2003
 * Time: 22:38:10
 * @author Serge Maslyukov
 * $Id$
 */
public class SsoConfig {
    private static Logger log = Logger.getLogger(SsoConfig.class );

    private static final String CONFIG_FILE_PARAM_NAME = "sso-config-file";
    private static final String NAME_CONFIG_FILE = "sso.xml";
    private final static String JNDI_SSO_CONFIG_FILE = "jsmithy/sso/ConfigFile";

    private static ConfigObject configObject = null;

    private static boolean isConfigProcessed = false;

    public static SsoConfigType getConfig()
    {
        return (SsoConfigType)configObject.getConfigObject();
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

            configObject = ConfigObject.load(JNDI_SSO_CONFIG_FILE , CONFIG_FILE_PARAM_NAME,  NAME_CONFIG_FILE, SsoConfigType.class);
            if (log.isDebugEnabled())
                log.debug("#15.006");

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
                String dir = getConfig().getSsoTempDir();
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
                getConfig().setSsoTempDir( dir );
                getConfig().setIsTempDirInit( Boolean.TRUE );
            }
            return getConfig().getSsoTempDir();
        }
    }

    private static Object syncDebug = new Object();
    public static String getSsoDebugDir()
        throws ConfigException
    {
        if (log.isDebugEnabled())
            log.debug("#15.937.1");

        if (!isConfigProcessed)
            readConfig();

        if (log.isDebugEnabled())
            log.debug("#15.938.1");

        if (getConfig().getIsDebugDirInit().booleanValue() )
            return getConfig().getSsoDebugDir();

        synchronized(syncDebug)
        {
            if (getConfig().getIsDebugDirInit().booleanValue())
                return getConfig().getSsoDebugDir();

            String dir = getConfig().getSsoDebugDir();
            if (File.separatorChar=='\\')
                dir = dir.replace( '/', '\\');
            else
                dir = dir.replace( '\\', '/');

            if (!dir.endsWith( File.separator ))
                dir += File.separator;

            File dirTest = new File(dir);
            if ( !dirTest.exists() )
            {
                log.warn("Specified debug directory '"+dir+"' not exists. Set to default java input/output temp directory");
                dir = System.getProperty("java.io.tmpdir");
            }
            getConfig().setSsoDebugDir( dir );
            getConfig().setIsDebugDirInit( Boolean.TRUE );

            return getConfig().getSsoDebugDir();
        }
    }

    public static AuthType getAuth()
        throws ConfigException
    {
        if (log.isDebugEnabled())
            log.debug("#20.110");

        if (!isConfigProcessed)
            readConfig();

        if (log.isDebugEnabled())
            log.debug("#20.111");

        return getConfig().getAuth();
    }

}
