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
package org.riverock.generic.db.definition;

import java.io.File;
import java.io.FileFilter;

import org.apache.log4j.Logger;

import org.riverock.common.config.PropertiesProvider;
import org.riverock.generic.main.CacheDirectory;
import org.riverock.generic.main.CacheFile;
import org.riverock.generic.main.Constants;
import org.riverock.generic.main.ExtensionFileFilter;

/**
 * $Id$
 */
public final class DataDefinitionManager {
    private final static Logger log = Logger.getLogger(DataDefinitionManager.class);

    private static FileFilter definitionFilter = new ExtensionFileFilter(".xml");

    private static CacheDirectory mainDir = null;

    private static DataDefinitionFile mainDefinitionFile[] = null;

    public static boolean isNeedReload() {
        return mainDir != null && mainDir.isNeedReload();
    }

    public static DataDefinitionFile[] getDefinitionFileArray() {
        DataDefinitionFile[] temp = new DataDefinitionFile[getCountFile()];

        int idx = 0;
        if (mainDefinitionFile != null) {
            for (DataDefinitionFile aMainDefinitionFile : mainDefinitionFile) {
                if (aMainDefinitionFile != null)
                    temp[idx++] = aMainDefinitionFile;
            }
        }

        return temp;
    }

    public static int getCountFile() {
        return (mainDefinitionFile == null ? 0 : mainDefinitionFile.length);
    }

    public static void init() {
            File dir = new File(PropertiesProvider.getConfigPath() + File.separator + Constants.MILL_DEFINITION_DIR);
            if (!dir.exists()) {
                log.warn("Directory '" + dir + "' not exists");
                return;
            }

            if (mainDir == null)
                mainDir = new CacheDirectory(dir, definitionFilter);

            if (mainDefinitionFile == null || !mainDir.isUseCache()) {
                if (mainDir.isNeedReload())
                    mainDir = new CacheDirectory(
                        PropertiesProvider.getConfigPath() + File.separator + Constants.MILL_DEFINITION_DIR,
                        definitionFilter
                    );

                if (log.isDebugEnabled())
                    log.debug("#2.001 read list file");

                mainDir.processDirectory();

                if (log.isDebugEnabled())
                    log.debug("#2.003 array of files - " + mainDir.getFileArray());

                CacheFile cacheFile[] = mainDir.getFileArray();

                if (log.isDebugEnabled()) {
                    log.debug("CacheFile - " + cacheFile);
                    log.debug("cacheFile.length - " + cacheFile.length);
                }

                mainDefinitionFile = null;
                mainDefinitionFile = new DataDefinitionFile[cacheFile.length];
                System.arraycopy(cacheFile, 0, mainDefinitionFile, 0, cacheFile.length);
            }
    }
}