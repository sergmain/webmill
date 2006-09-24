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
package org.riverock.generic.main;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * $Id$
 */
public class CacheFile {
    private static Logger log = Logger.getLogger(CacheFile.class);

    private long lastAccessTime = 0;
    private long delayPeriod = 1000 * 10;

    private long lastModified = -1;
    private File file = null;

    public File getFile() {
        return file;
    }

    public void setLastModified() {
        lastModified = file.lastModified();
    }

    public boolean isNeedReload() {
        return lastModified != file.lastModified();
    }

    public boolean isUseCache() {
        if (System.currentTimeMillis() - lastAccessTime <= delayPeriod) {
            if (log.isDebugEnabled())
                log.debug("#7.9.0 Use optimistic cached file");

            return true;
        }

        lastAccessTime = System.currentTimeMillis();
        return false;
    }

    protected void finalize() throws Throwable {
        file = null;

        super.finalize();
    }

    public CacheFile(File tempFile) throws Exception {
        this(tempFile, 1000 * 10);
    }

    public CacheFile(String fileName) throws Exception {
        this(new File(fileName), 1000 * 10);
    }

    public CacheFile(String fileName, long delayPeriod_) throws Exception {
        this(new File(fileName), delayPeriod_);
    }

    public CacheFile(File tempFile, long delayPeriod_) {
        if (tempFile == null) {
            String errorString = "Cache file to init is null";
            log.error(errorString);
            throw new IllegalStateException(errorString);
        }

        if (!tempFile.exists()) {
            String errorString = "Cache file " + file.getAbsolutePath() + " is not exists";
            log.error(errorString);
            throw new IllegalStateException(errorString);
        }

        if (log.isDebugEnabled()) {
            log.debug("Cache file " + tempFile);
            log.debug("Name file: " + tempFile.getName());
        }

        delayPeriod = delayPeriod_;
        file = tempFile;
        setLastModified();
    }

}
