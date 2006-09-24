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
import java.io.FileFilter;

import org.apache.log4j.Logger;

import org.riverock.common.config.ConfigException;

/**
 * @author Serge Maslyukov
 * 
 * $Id$
 */
public final class CacheDirectory {
    private final static Logger log = Logger.getLogger( CacheDirectory.class );

    private static final int REREAD_DELAY = 1000 * 10;
    private static final int NEVER_ACCESED = -1;

    private long lastAccessTime = NEVER_ACCESED;
    private long delayPeriod = REREAD_DELAY;

    private File currentDir = null;
    private CacheFile[] files = null;
    private FileFilter fileFilter = null;

    public String toString() {
        return "CacheDirectory=[isUseCache: " + isUseCache() + "; currentDir: " + currentDir + "; files: " + files + "; fileFilter: " + fileFilter + "]";
    }

    public CacheFile[] getFileArray() {
        if( files == null )
            return new CacheFile[]{};

        CacheFile[] cacheFiles = new CacheFile[files.length];
        System.arraycopy(files, 0, cacheFiles, 0, files.length);
        
        return cacheFiles;
    }

    public boolean isNeedReload() {
        if (currentDir == null)
            return false;

        File[] currentFiles = currentDir.listFiles(fileFilter);

        if (files == null)
            return false;

        if (files.length != currentFiles.length)
            return true;

        for (final CacheFile newVar : files) {
            File temp = newVar.getFile();
            File currFile = null;
            for (final File newVar1 : currentFiles) {
                currFile = newVar1;
                if (temp.getName().equals(currFile.getName()))
                    break;
                currFile = null;
            }
            if (currFile != null && temp.lastModified() == currFile.lastModified()) {
                if (log.isDebugEnabled())
                    log.debug("File " + temp.getName() + " was changed. Return status is true");

                return true;
            }
        }
        return false;
    }

    public boolean isUseCache() {
        if( System.currentTimeMillis() - lastAccessTime <= delayPeriod ) {
            if( log.isDebugEnabled() )
                log.debug( "#7.9.0 Use optimistic cached file" );

            return true;
        }

        lastAccessTime = System.currentTimeMillis();
        return false;
    }

    public void processDirectory() throws ConfigException {
        try {
            if( currentDir == null )
                return;

            File currentList[] = currentDir.listFiles( fileFilter );

            if( log.isDebugEnabled() )
                log.debug( "currentList " + currentList );

            if( currentList == null )
                return;

            if( log.isDebugEnabled() )
                log.debug( "#2.003 found " + currentList.length + " files" );

            files = new CacheFile[currentList.length];
            if( log.isDebugEnabled() )
                log.debug( "#1.004 " + currentList.length );

            for( int i = 0; i < currentList.length; i++ )
                files[i] = new CacheFile( currentList[i] );
        }
        catch( Exception e ) {
            String errorString = "error processing directory";
            log.error( errorString, e );
            throw new ConfigException( errorString, e );
        }
    }

    public CacheDirectory( String fileName, FileFilter filter ) throws ConfigException {
        this( fileName, filter, REREAD_DELAY );
    }

    public CacheDirectory( final File dir, final FileFilter filter ) throws ConfigException {
        this( dir, filter, REREAD_DELAY );
    }

    public CacheDirectory( final String dirName, final FileFilter filter, final long delayPeriod_ ) throws ConfigException {
        this( new File( dirName ), filter, delayPeriod_ );
    }

    public CacheDirectory( final File dir, final FileFilter filter, final long delayPeriod_ ) throws ConfigException {
        fileFilter = filter;
        currentDir = dir;

        if( currentDir == null ) {
            log.warn( "Path is null." );
            return;
        }

        if( !currentDir.exists() ) {
            String errorString = "Path '" + currentDir + "' is not exists.";
            log.warn( errorString );
            currentDir = null;
            return;
        }
        if( !currentDir.isDirectory() ) {
            String errorString = "Path '" + currentDir + "' is not directory.";
            log.warn( errorString );
            currentDir = null;
            return;
        }

        delayPeriod = delayPeriod_;
        lastAccessTime = System.currentTimeMillis();

        processDirectory();
    }
}