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
package org.riverock.webmill.config;

import java.io.File;
import java.nio.charset.Charset;

import org.riverock.webmill.schema.config.WebmillConfigType;
import org.riverock.webmill.schema.config.CustomDirsType;
import org.riverock.common.config.ConfigObject;
import org.riverock.common.tools.StringTools;

import org.apache.log4j.Logger;


/**
 * User: serg_main
 * Date: 28.11.2003
 * Time: 21:45:01
 * @author Serge Maslyukov
 * $Id$
 */
public class WebmillConfig {
    private static Logger log = Logger.getLogger(WebmillConfig.class);
    private static final String CONFIG_FILE_PARAM_NAME = "webmill-config-file";
    private static final String NAME_CONFIG_PARAM = "jsmithy-webmill.xml";
    private static final String JNDI_WEBMILL_CONFIG_FILE = "jsmithy/webmill/ConfigFile";

//    public static String contextName = "";
    private static ConfigObject configObject = null;

    private static boolean isConfigProcessed = false;

    public static WebmillConfigType getConfig()
    {
        return (WebmillConfigType) configObject.getConfigObject();
    }

    private static Object syncReadConfig = new Object();

    private static void readConfig() {
        if (isConfigProcessed)
            return;

        synchronized (syncReadConfig)
        {
            if (isConfigProcessed)
                return;

            configObject = ConfigObject.load(
                JNDI_WEBMILL_CONFIG_FILE, CONFIG_FILE_PARAM_NAME, NAME_CONFIG_PARAM, WebmillConfigType.class);

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
                    dir = dir.replace('/', '\\');
                else
                    dir = dir.replace('\\', '/');

                File dirTest = new File(dir);
                if (!dirTest.exists())
                {
                    log.error("Specified temp directory '" + dir + "' not exists. Set to default java input/output temp directory");
                    dir = System.getProperty("java.io.tmpdir");
                }

                if (!dir.endsWith(File.separator))
                    dir += File.separator;

                getConfig().setWebmillTempDir(dir);
                getConfig().setIsTempDirInit(Boolean.TRUE);
            }
            return getConfig().getWebmillTempDir();
        }
    }

    private static Object syncDebug = new Object();

    public static String getWebmillDebugDir() {
        if (log.isDebugEnabled()) log.debug("#15.937.1");

        if (!isConfigProcessed)
            readConfig();

        if (log.isDebugEnabled()) log.debug("#15.938.1");

        if (getConfig().getIsDebugDirInit())
            return getConfig().getWebmillDebugDir();

        synchronized (syncDebug)
        {
            if (getConfig().getIsDebugDirInit())
                return getConfig().getWebmillDebugDir();

            String dir = getConfig().getWebmillDebugDir();
            if (File.separatorChar == '\\')
                dir = dir.replace('/', '\\');
            else
                dir = dir.replace('\\', '/');

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
    {
        if (log.isDebugEnabled()) log.debug("#15.903");

        if (!isConfigProcessed)
            readConfig();

        if (log.isDebugEnabled()) {
            log.debug("#15.904 Main language: " +
                StringTools.getLocale( getConfig().getMainLanguage()).toString()
            );
        }

        return StringTools.getLocale( getConfig().getMainLanguage() ).toString();
    }
    public static String getServerCharset() {
        if (log.isDebugEnabled()) log.debug("#15.905");

        if (!isConfigProcessed)
            readConfig();

        if (log.isDebugEnabled()) log.debug("#15.906");

        return getConfig().getServerCharset();
    }


    public static String getHtmlCharset()
    {
        if (log.isDebugEnabled())
            log.debug("#15.923");

        if (!isConfigProcessed)
            readConfig();

        if (log.isDebugEnabled())
            log.debug("#15.924");

        return getConfig().getHtmlCharset();
    }

    public static Charset getCharset()
    {
        if (log.isDebugEnabled())
            log.debug("#15.923");

        if (!isConfigProcessed)
            readConfig();

        if (log.isDebugEnabled())
            log.debug("#15.924");

        return Charset.forName( getConfig().getHtmlCharset() );
    }

    public static boolean getIsSafePage()
    {
        if (log.isDebugEnabled())
            log.debug("#Get isSafePage");

        if (!isConfigProcessed)
            readConfig();

        if (log.isDebugEnabled())
            log.debug("#Done get isSafePage");


        return getConfig().getIsSafePage() == null ? false : getConfig().getIsSafePage();
    }

    public static String getMailSMTPHost()
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
