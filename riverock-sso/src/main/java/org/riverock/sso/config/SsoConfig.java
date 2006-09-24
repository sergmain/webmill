/*
 * org.riverock.sso - Single Sign On implementation
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.riverock.sso.config;

import org.apache.log4j.Logger;

import org.riverock.common.config.ConfigException;
import org.riverock.common.config.ConfigObject;
import org.riverock.sso.schema.config.AuthType;
import org.riverock.sso.schema.config.SsoConfigType;

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

    public static SsoConfigType getConfig() {
        return (SsoConfigType)configObject.getConfigObject();
    }

    private final static Object syncReadConfig = new Object();
    private static void readConfig() throws ConfigException {
        if (isConfigProcessed)
            return;

        synchronized (syncReadConfig)
        {
            if (isConfigProcessed)
                return;

            if (log.isDebugEnabled()) {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                log.debug("SsoConfig classLoader: "+cl+"\nhash: "+cl.hashCode() );
                ClassLoader ccl = ConfigObject.class.getClassLoader();
                log.debug("ConfigObject classLoader: "+ccl+"\nhash: "+ccl.hashCode() );
            }

            configObject = ConfigObject.load(JNDI_SSO_CONFIG_FILE , CONFIG_FILE_PARAM_NAME,  NAME_CONFIG_FILE, SsoConfigType.class);

            if (log.isDebugEnabled())
                log.debug("#15.006");

            isConfigProcessed = true;
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
