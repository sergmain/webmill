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
