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

/**
 * User: serg_main
 * Date: 28.11.2003
 * Time: 21:45:01
 *
 * @author Serge Maslyukov
 *         $Id$
 */
public class WebmillConfig {
/*
    private static Logger log = Logger.getLogger(WebmillConfig.class);
    private static final String CONFIG_FILE_PARAM_NAME = "webmill-config-file";
    private static final String NAME_CONFIG_PARAM = "jsmithy-webmill.xml";
    private static final String JNDI_WEBMILL_CONFIG_FILE = "jsmithy/webmill/ConfigFile";

    private static ConfigObject configObject = null;

    private static boolean isConfigProcessed = false;

    private final static Object syncDebug = new Object();


    public static WebmillConfigType getConfig() {
        return (WebmillConfigType) configObject.getConfigObject();
    }

    private final static Object syncReadConfig = new Object();

    private static void readConfig() {
        if (isConfigProcessed) {
            return;
        }

        synchronized (syncReadConfig) {
            if (isConfigProcessed) {
                return;
            }

            configObject = ConfigObject.load(JNDI_WEBMILL_CONFIG_FILE, CONFIG_FILE_PARAM_NAME, NAME_CONFIG_PARAM, WebmillConfigType.class);

            if (log.isDebugEnabled()) {
                log.debug("#15.006");
            }

            isConfigProcessed = true;
        }
    }

//-----------------------------------------------------
// PUBLIC SECTION
//-----------------------------------------------------

    private final static Object syncTempDir = new Object();

    public static String getWebmillTempDir() {
        if (log.isDebugEnabled()) {
            log.debug("#15.937");
        }

        if (!isConfigProcessed) {
            readConfig();
        }

        if (log.isDebugEnabled()) {
            log.debug("#15.938");
        }

        synchronized (syncTempDir) {
            if (Boolean.FALSE.equals(getConfig().getIsTempDirInit())) {
                String dir = getConfig().getWebmillTempDir();

                dir = dir.replace(File.separatorChar == '/' ? '\\' : '/', File.separatorChar);

                File dirTest = new File(dir);
                if (!dirTest.exists()) {
                    log.error("Specified temp directory '" + dir + "' not exists. Set to default java input/output temp directory");
                    dir = System.getProperty("java.io.tmpdir");
                }

                if (!dir.endsWith(File.separator)) {
                    dir += File.separator;
                }

                getConfig().setWebmillTempDir(dir);
                getConfig().setIsTempDirInit(Boolean.TRUE);
            }
            return getConfig().getWebmillTempDir();
        }
    }

    public static String getWebmillDebugDir() {
        if (log.isDebugEnabled()) {
            log.debug("#15.937.1");
        }

        if (!isConfigProcessed) {
            readConfig();
        }

        if (log.isDebugEnabled()) {
            log.debug("#15.938.1");
        }

        if (getConfig().getIsDebugDirInit()) {
            return getConfig().getWebmillDebugDir();
        }

        synchronized (syncDebug) {
            if (getConfig().getIsDebugDirInit()) {
                return getConfig().getWebmillDebugDir();
            }

            String dir = getConfig().getWebmillDebugDir();
            dir = dir.replace(File.separatorChar == '/' ? '\\' : '/', File.separatorChar);

            if (!dir.endsWith(File.separator)) {
                dir += File.separator;
            }

            File dirTest = new File(dir);
            if (!dirTest.exists()) {
                log.warn("Specified debug directory '" + dir + "' not exists. Set to default java input/output temp directory");
                dir = System.getProperty("java.io.tmpdir");
            }
            getConfig().setWebmillDebugDir(dir);
            getConfig().setIsDebugDirInit(Boolean.TRUE);

            return getConfig().getWebmillDebugDir();
        }
    }

    public static String getMailSMTPHost() {
        if (log.isDebugEnabled())
            log.debug("#15.927");

        if (!isConfigProcessed)
            readConfig();

        if (log.isDebugEnabled())
            log.debug("#15.928");

        return getConfig().getMailHost();
    }
*/
}
