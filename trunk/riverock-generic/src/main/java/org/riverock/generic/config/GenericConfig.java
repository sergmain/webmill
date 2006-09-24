/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.generic.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import org.riverock.common.config.ConfigException;
import org.riverock.common.config.ConfigObject;
import org.riverock.generic.schema.config.DatabaseConnectionType;
import org.riverock.generic.schema.config.DateTimeSavingType;
import org.riverock.generic.schema.config.DateTimeSavingTypeSequence;
import org.riverock.generic.schema.config.GenericConfigType;
import org.riverock.generic.schema.config.PropertyType;

/**
 * $Id$
 */
public final class GenericConfig {
    private final static Logger log = Logger.getLogger(GenericConfig.class);

    public static final String CONFIG_FILE_PARAM_NAME = "generic-config-file";

    private static final String NAME_CONFIG_FILE = "-generic.xml";
    private static String configPrefix = "jsmithy";
    public final static String JNDI_GENERIC_CONFIG_FILE = "jsmithy/generic/ConfigFile";

    public final static String JNDI_DB_NAME = "webmill/db-name";
    public final static String JNDI_DB_FAMALY = "webmill/db-famaly";

    private static ConfigObject configObject = null;
    private static Map<String, DatabaseConnectionType> dbConfig = null;
    private static TimeZone currentTimeZone = null;

    private static boolean isConfigProcessed = false;
    private static String defaultConnectionName = null;
    private static String genericDebugDir = null;

    public static String getConfigPrefix() {
        return configPrefix;
    }

    public static void setConfigPrefix(final String configPrefix) {
        GenericConfig.configPrefix = configPrefix;
    }

    public static GenericConfigType getConfig() {
        return (GenericConfigType) configObject.getConfigObject();
    }

    private final static Object syncReadConfig = new Object();

    private static void readConfig() {

        if (isConfigProcessed) return;

        synchronized (syncReadConfig) {
            if (isConfigProcessed)
                return;

            configObject = ConfigObject.load(
                JNDI_GENERIC_CONFIG_FILE, CONFIG_FILE_PARAM_NAME, configPrefix + NAME_CONFIG_FILE, GenericConfigType.class
            );

            if (dbConfig != null) {
                dbConfig.clear();
                dbConfig = null;
            }

            // config not found as init parameter in web.xml file or as JDNI reference
            if (getConfig()==null) {
                throw new ConfigException("generic config object is null");
            }

            if (log.isDebugEnabled()) {
                log.debug("#15.006");
            }

            dbConfig = new HashMap<String, DatabaseConnectionType>(getConfig().getDatabaseConnectionCount());
            for (int i = 0; i < getConfig().getDatabaseConnectionCount(); i++) {
                DatabaseConnectionType dbc = getConfig().getDatabaseConnection(i);
                dbConfig.put(dbc.getName(), dbc);
            }

            if (log.isInfoEnabled()) {
                log.info("Name default DB connect " + getConfig().getDefaultConnectionName());
            }

            isConfigProcessed = true;
        }
    }

//-----------------------------------------------------
// PUBLIC SECTION
//-----------------------------------------------------

    private final static Object syncTZ = new Object();

    public static TimeZone getTZ() throws ConfigException {

        if (log.isDebugEnabled()) log.debug("GenericConfig.getTZ() #1");
        if (!isConfigProcessed) readConfig();
        if (currentTimeZone != null) return currentTimeZone;

        if (log.isDebugEnabled()) log.debug("GenericConfig.getTZ() #3 Set new TimeZone");

        synchronized (syncTZ) {

            if (currentTimeZone != null) return currentTimeZone;

            DateTimeSavingType dts = getConfig().getDTS();
            if (dts.getDateTimeSavingTypeSequence2() != null) {
                String nameTimeZone = dts.getDateTimeSavingTypeSequence2().getTimeZoneName();

                // work around NPE in TimeZone.getTimeZone
                if (nameTimeZone != null && nameTimeZone.trim().length() != 0) {

                    if (log.isDebugEnabled())
                        log.debug("GenericConfig.getTZ(). Set TimeZone to " + nameTimeZone);

                    TimeZone tz = TimeZone.getTimeZone(nameTimeZone);
                    if (tz == null) {
                        log.fatal("TimeZone '" + nameTimeZone + "' not found. You must correct NameTimeZone element in config file");
                        return null;
                    }
                    currentTimeZone = tz;
                    TimeZone.setDefault(currentTimeZone);
                    return tz;
                }
                log.fatal("<NameTimeZone> element not found. You must correct config file.");
                return null;
            } else if (dts.getDateTimeSavingTypeSequence() != null) {
                DateTimeSavingTypeSequence dtSeq = dts.getDateTimeSavingTypeSequence();

                currentTimeZone =
                    new SimpleTimeZone(
                        dtSeq.getRawOffset(),
                        dtSeq.getId(),
                        dtSeq.getStart().getMonth(),
                        dtSeq.getStart().getDay(),
                        dtSeq.getStart().getDayOfWeek(),
                        dtSeq.getStart().getTime(),
                        dtSeq.getEnd().getMonth(),
                        dtSeq.getEnd().getDay(),
                        dtSeq.getEnd().getDayOfWeek(),
                        dtSeq.getEnd().getTime()
                    );
                TimeZone.setDefault(currentTimeZone);
                return currentTimeZone;
            }
            log.fatal("<DTS> element present, but is empty. You must correct <DTS> element in config file.");
            return null;
        }
    }

    private final static Object syncDebugDir = new Object();

    public static void setGenericDebugDir(String genericDebugDir) {
        synchronized(syncDebugDir) {
            String dir = genericDebugDir;
            File dirTest = new File(dir);
            if (!dirTest.exists()) {
                log.warn("Specified debug directory '" + dir + "' not exists. Set to default java input/output temp directory");
                dir = System.getProperty("java.io.tmpdir");
            }

            if (!dirTest.canWrite()) {
                log.warn("Specified debug directory '" + dir + "' not writable. Set to default java input/output temp directory");
                dir = System.getProperty("java.io.tmpdir");
            }

            GenericConfig.genericDebugDir = dir;
        }
    }

    public static String getGenericDebugDir() {

        if (genericDebugDir==null) {
            synchronized(syncDebugDir) {
                if (genericDebugDir==null) {
                    genericDebugDir = System.getProperty("java.io.tmpdir");
                }
            }
        }

        return genericDebugDir;
    }

    public static DatabaseConnectionType getDatabaseConnection(final String connectionName) {

        if (log.isDebugEnabled()) log.debug("#15.909");

        if (!isConfigProcessed) readConfig();

        if (log.isDebugEnabled()) log.debug("#15.910");

        return dbConfig.get(connectionName);
    }

    public static void setDefaultConnectionName(final String defaultConnectionName_) {
        defaultConnectionName = defaultConnectionName_;
    }

    public static String getDefaultConnectionName() {

        // if defaultConnectionName is overrided, then return new value(not from config)
        if (defaultConnectionName != null)
            return defaultConnectionName;

        if (log.isDebugEnabled()) log.debug("#15.951");
        if (!isConfigProcessed) readConfig();
        if (log.isDebugEnabled()) log.debug("#15.952");

        return getConfig().getDefaultConnectionName();
    }

    public static PropertyType[] getProperty() {

        if (log.isDebugEnabled()) log.debug("#16.951");
        if (!isConfigProcessed) readConfig();
        if (log.isDebugEnabled()) log.debug("#16.952");

        return getConfig().getProperty();
    }

    public static List getPropertyList() {

        if (log.isDebugEnabled()) log.debug("#16.961");
        if (!isConfigProcessed) readConfig();
        if (log.isDebugEnabled()) log.debug("#16.962");

        return getConfig().getPropertyAsReference();
    }

    public static int getPropertyCount() {

        if (log.isDebugEnabled()) log.debug("#16.971");
        if (!isConfigProcessed) readConfig();
        if (log.isDebugEnabled()) log.debug("#16.972");

        return getConfig().getPropertyCount();
    }

    public static PropertyType getProperty(final int idx) {

        if (log.isDebugEnabled()) log.debug("#16.981");
        if (!isConfigProcessed) readConfig();
        if (log.isDebugEnabled()) log.debug("#16.982");

        return getConfig().getProperty(idx);
    }
}
