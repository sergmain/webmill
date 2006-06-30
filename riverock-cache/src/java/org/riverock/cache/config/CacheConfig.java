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
package org.riverock.cache.config;

/**
 * User: serg_main
 * Date: 28.11.2003
 * Time: 22:01:57
 *
 * @author Serge Maslyukov
 *         $Id$
 */
public final class CacheConfig {
/*
    private final static Logger log = Logger.getLogger(CacheConfig.class);

    // used in web.xml file
    public static final String CONFIG_FILE_PARAM_NAME = "cache-config-file";
    // name of local config file
    private static final String NAME_CONFIG_FILE = "jsmithy-cache.xml";

    // name of config file in JNDI
    public final static String JNDI_CACHE_CONFIG_FILE = "jsmithy/cache/ConfigFile";

    private static ConfigObject configObject = null;
    private static boolean isConfigProcessed = false;
    private static HashMap<String, CacheClassItemType> classMap = new HashMap<String, CacheClassItemType>();

    public static CacheConfigType getConfig() {
        return (CacheConfigType) configObject.getConfigObject();
    }

    private final static Object syncReadConfig = new Object();

    private static void readConfig()
        throws ConfigException {
        if (isConfigProcessed)
            return;

        synchronized (syncReadConfig) {
            if (isConfigProcessed)
                return;

            configObject = ConfigObject.load(JNDI_CACHE_CONFIG_FILE, CONFIG_FILE_PARAM_NAME, NAME_CONFIG_FILE, CacheConfigType.class);

            if (log.isDebugEnabled())
                log.debug("#15.006");

            if (getConfig().getClassList() != null) {
                for (int i = 0; i < getConfig().getClassList().getClassItemCount(); i++) {
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

    public static CacheClassItemType getClassDefinition(String className) {
        if (!isConfigProcessed)
            readConfig();

        if (className == null)
            return null;

        return classMap.get(className);
    }
*/
}
