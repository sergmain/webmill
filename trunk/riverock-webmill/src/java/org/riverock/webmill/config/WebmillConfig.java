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

 * User: serg_main

 * Date: 28.11.2003

 * Time: 21:45:01

 * @author Serge Maslyukov

 * $Id$

 */



package org.riverock.webmill.config;



import java.io.File;



import org.riverock.webmill.schema.config.WebmillConfigType;

import org.riverock.webmill.schema.config.CustomDirsType;

import org.riverock.webmill.main.Constants;

import org.riverock.common.config.ConfigObject;

import org.riverock.common.config.ConfigException;



import org.apache.log4j.Logger;



public class WebmillConfig

{



    private static Logger log = Logger.getLogger("org.riverock.generic.config.GenericConfig");



    public static String contextName = "";

    private static ConfigObject configObject = null;



    private static boolean isConfigProcessed = false;



    public static WebmillConfigType getConfig()

    {

        return (WebmillConfigType) configObject.getConfigObject();

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



            configObject = ConfigObject.load(Constants.JNDI_WEBMILL_CONFIG_FILE, "jsmithy-webmill.xml", WebmillConfigType.class);



            if (log.isDebugEnabled())

                log.debug("#15.006");



            isConfigProcessed = true;

        }

    }



//-----------------------------------------------------

// PUBLIC SECTION

//-----------------------------------------------------



    private static Object syncTempDir = new Object();



    public static String getWebmillTempDir()

        throws ConfigException

    {

        if (log.isDebugEnabled())

            log.debug("#15.937");



        if (!isConfigProcessed)

            readConfig();



        if (log.isDebugEnabled())

            log.debug("#15.938");



        synchronized (syncTempDir)

        {

            if (Boolean.FALSE.equals(getConfig().getIsTempDirInit()))

            {

                String dir = getConfig().getWebmillTempDir();

                if (File.separatorChar == '\\')

                {

                    dir.replace('/', '\\');

                }

                else

                {

                    dir.replace('\\', '/');

                }

                if (!dir.endsWith(File.separator))

                    dir += File.separator;



                File dirTest = new File(dir);

                if (!dirTest.exists())

                {

                    log.error("Specified temp directory '" + dir + "' not exists. Set to default java input/output temp directory");

                    dir = System.getProperty("java.io.tmpdir");

                }

                getConfig().setWebmillTempDir(dir);

                getConfig().setIsTempDirInit(Boolean.TRUE);

            }

            return getConfig().getWebmillTempDir();

        }

    }



    private static Object syncDebug = new Object();



    public static String getWebmillDebugDir()

        throws ConfigException

    {

        if (log.isDebugEnabled())

            log.debug("#15.937.1");



        if (!isConfigProcessed)

            readConfig();



        if (log.isDebugEnabled())

            log.debug("#15.938.1");



        if (getConfig().getIsDebugDirInit().booleanValue())

            return getConfig().getWebmillDebugDir();



        synchronized (syncDebug)

        {

            if (getConfig().getIsDebugDirInit().booleanValue())

                return getConfig().getWebmillDebugDir();



            String dir = getConfig().getWebmillDebugDir();

            if (File.separatorChar == '\\')

            {

                dir.replace('/', '\\');

            }

            else

            {

                dir.replace('\\', '/');

            }

            if (!dir.endsWith(File.separator))

                dir += File.separator;



            File dirTest = new File(dir);

            if (!dirTest.exists())

            {

                log.warn("Specified debug directory '" + dir + "' not exists. Set to default java input/output temp directory");

                dir = System.getProperty("java.io.tmpdir");

            }

            getConfig().setWebmillDebugDir(dir);

            getConfig().setIsDebugDirInit(Boolean.TRUE);



            return getConfig().getWebmillDebugDir();

        }

    }



    public static String getMainLanguage()

        throws ConfigException

    {

        if (log.isDebugEnabled())

            log.debug("#15.903");



        if (!isConfigProcessed)

            readConfig();



        if (log.isDebugEnabled())

            log.debug("#15.904 Main language: " + getConfig().getMainLanguage());



        return getConfig().getMainLanguage();

    }

    public static String getServerCharset()

        throws ConfigException

    {

        if (log.isDebugEnabled())

            log.debug("#15.905");



        if (!isConfigProcessed)

            readConfig();



        if (log.isDebugEnabled())

            log.debug("#15.906");



        return getConfig().getServerCharset();

    }





    public static String getHtmlCharset()

        throws ConfigException

    {

        if (log.isDebugEnabled())

            log.debug("#15.923");



        if (!isConfigProcessed)

            readConfig();



        if (log.isDebugEnabled())

            log.debug("#15.924");



        return getConfig().getHtmlCharset();

    }



    public static boolean getIsSafePage()

        throws ConfigException

    {

        if (log.isDebugEnabled())

            log.debug("#Get isSafePage");



        if (!isConfigProcessed)

            readConfig();



        if (log.isDebugEnabled())

            log.debug("#Done get isSafePage");





        return (getConfig().getIsSafePage()!=null?getConfig().getIsSafePage().booleanValue():false);

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



    public static String getCustomPortletDir()

        throws ConfigException

    {

        if (log.isDebugEnabled())

            log.debug("#16.927");



        if (!isConfigProcessed)

            readConfig();



        if (log.isDebugEnabled())

            log.debug("#16.928");



        CustomDirsType dirs = getConfig().getCustomDirs();

        if (dirs==null)

            return null;



        return dirs.getCustomPortletDir();

    }



    public static String getCustomMemberDir()

        throws ConfigException

    {

        if (log.isDebugEnabled())

            log.debug("#16.901");



        if (!isConfigProcessed)

            readConfig();



        if (log.isDebugEnabled())

            log.debug("#16.902");



        CustomDirsType dirs = getConfig().getCustomDirs();

        if (dirs==null)

            return null;



        return dirs.getCustomMemberDir();

    }



    public static String getCustomApplDir()

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



        return dirs.getCustomApplicationDir();

    }



}

