/*
 * org.riverock.common - Supporting classes, interfaces, and utilities
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.riverock.common.tools.XmlTools;

/**
 * $Id$
 */
public class ConfigObject {
    public static final String LOCAL_CONFIG_PARAM_NAME = "is-local-config";

    private String nameConfigFile = null;

    private Object configObject = null;

    public ConfigObject() {
    }

    public Object getConfigObject() {
        return configObject;
    }

    public static <T> ConfigObject load(String nameJndiCtx, String nameConfigParam, String nameConfigFile_, Class<T> configClass) {
        ConfigObject config = new ConfigObject();
        File configFile = null;
        if (PropertiesProvider.getIsServletEnv()) {
            boolean isLocal = "true".equalsIgnoreCase(PropertiesProvider.getParameter(LOCAL_CONFIG_PARAM_NAME));
            if (isLocal) {
                config.nameConfigFile =
                    PropertiesProvider.getApplicationPath() +
                        File.separatorChar +
                        "WEB-INF" +
                        File.separatorChar +
                        PropertiesProvider.getParameter(nameConfigParam);
            }
            else {
                String name = "java:comp/env/" + nameJndiCtx;
                try {
                    InitialContext ic = new InitialContext();
                    config.nameConfigFile = (String) ic.lookup(name);
                }
                catch (NamingException e) {
                    String es = "Error get value from JDNI context. Name: " + name;
                    try {
                        InitialContext ic = new InitialContext();
                        Map map = ic.getEnvironment();
                        Iterator<Map.Entry> iterator = map.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry entry = iterator.next();
                        }
                    }
                    catch (Throwable th) {
                        //
                    }
                    throw new ConfigException(es, e);
                }
            }
        }
        else {
            String defURL = null;
            if (PropertiesProvider.getConfigPath() == null) {
                String es = "Config path not resolved";
                throw new IllegalStateException(es);
            }

            defURL = PropertiesProvider.getConfigPath() +
                (PropertiesProvider.getConfigPath().endsWith(File.separator) ? "" : File.separator) +
                nameConfigFile_;

            config.nameConfigFile = defURL;
        }

        if (config.nameConfigFile == null) {
            String errorString = "name of config file not determinated";
            throw new IllegalArgumentException(errorString);
        }

        config.nameConfigFile = config.nameConfigFile.replace(
            File.separatorChar == '/' ? '\\' : '/', File.separatorChar
        );

        configFile = new File(config.nameConfigFile);

        if (!configFile.exists()) {
            String errorString = "Config file '" + configFile.getName() + "' not found";
            throw new IllegalArgumentException(errorString);
        }

        try {
            // System.out.println("Start unmarshal config file: " + configFile);
            FileInputStream stream = new FileInputStream(configFile);
            config.configObject = XmlTools.getObjectFromXml(configClass, stream);
        }
        catch (Throwable e) {
            String es = "Error while unmarshalling config file ";
            throw new ConfigException(es, e);
        }

        return config;
    }
}
