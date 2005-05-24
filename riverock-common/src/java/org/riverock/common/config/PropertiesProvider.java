/*
 * org.riverock.common -- Supporting classes, interfaces, and utilities
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
package org.riverock.common.config;

import java.util.Map;
import java.util.HashMap;

/**
 * User: serg_main
 * Date: 28.11.2003
 * Time: 19:05:41
 *
 * @author Serge Maslyukov
 *         $Id$
 */
public class PropertiesProvider {
    private static boolean isServletEnv = false;
    private static String applicationPath = null;
    private static String configPath = null;
    private static Map parameters = new HashMap();

    private final static String lang[] = {"ru"};

    public static String[] getLocaleLanguage() {
        return lang;
    }

    public static String getConfigPath() {
        return configPath;
    }

    public static void setConfigPath(String configPath) {
        PropertiesProvider.configPath = configPath;
    }

    public static String getApplicationPath() {
        return applicationPath;
    }

    public static void setApplicationPath(String applicationPath) {
        PropertiesProvider.applicationPath = applicationPath;
    }

    public static boolean getIsServletEnv() {
        return isServletEnv;
    }

    public static void setIsServletEnv(boolean servletEnv) {
        isServletEnv = servletEnv;
    }

    public static Map getParameters() {
        return parameters;
    }

    public static void setParameters(Map parameters) {
        PropertiesProvider.parameters = parameters;
    }

    public static Object getParameter(String key) {
        if (key==null) {
            return null;
        }
        return PropertiesProvider.parameters.get( key );
    }
}
