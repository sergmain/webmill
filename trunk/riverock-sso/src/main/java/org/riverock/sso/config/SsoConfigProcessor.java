/*
 * org.riverock.sso - Single Sign On implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.sso.config;

import org.apache.log4j.Logger;

import org.riverock.common.config.ConfigObject;
import org.riverock.sso.annotation.schema.config.Auth;
import org.riverock.sso.annotation.schema.config.SsoConfig;

/**
 * User: serg_main
 * Date: 28.11.2003
 * Time: 22:38:10
 * @author Serge Maslyukov
 * $Id$
 */
public class SsoConfigProcessor {
    private static Logger log = Logger.getLogger(SsoConfigProcessor.class );

    private static final String CONFIG_FILE_PARAM_NAME = "sso-config-file";
    private static final String NAME_CONFIG_FILE = "sso.xml";
    private final static String JNDI_SSO_CONFIG_FILE = "jsmithy/sso/ConfigFile";

    private static ConfigObject configObject = null;

    private static boolean isConfigProcessed = false;

    public static SsoConfig getConfig() {
        return (SsoConfig)configObject.getConfigObject();
    }

    private final static Object syncReadConfig = new Object();
    private static void readConfig() {
        if (isConfigProcessed) {
            return;
        }

        synchronized (syncReadConfig)
        {
            if (isConfigProcessed) {
                return;
            }

            if (log.isDebugEnabled()) {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                log.debug("SsoConfigProcessor classLoader: "+cl+"\nhash: "+cl.hashCode() );
                ClassLoader ccl = ConfigObject.class.getClassLoader();
                log.debug("ConfigObject classLoader: "+ccl+"\nhash: "+ccl.hashCode() );
            }

            configObject = ConfigObject.load(
                JNDI_SSO_CONFIG_FILE , CONFIG_FILE_PARAM_NAME,  NAME_CONFIG_FILE, SsoConfig.class
            );

            if (log.isDebugEnabled()) {
                log.debug("#15.006");
            }

            isConfigProcessed = true;
        }
    }

    public static Auth getAuth() {

        if (log.isDebugEnabled()) {
            log.debug("#20.110");
        }

        if (!isConfigProcessed) {
            readConfig();
        }

        if (log.isDebugEnabled()) {
            log.debug("#20.111");
        }

        return getConfig().getAuth();
    }

}
