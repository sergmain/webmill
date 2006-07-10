/*
 * org.riverock.portlet -- Portlet Library
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
package org.riverock.portlet.member;

import java.io.File;
import java.io.FileFilter;

import org.apache.log4j.Logger;

import org.riverock.generic.main.CacheDirectory;
import org.riverock.generic.main.CacheFile;
import org.riverock.generic.main.ExtensionFileFilter;
import org.riverock.generic.startup.StartupServlet;
import org.riverock.portlet.schema.member.ContentType;
import org.riverock.portlet.schema.member.ModuleType;
import org.riverock.portlet.schema.member.QueryAreaType;
import org.riverock.portlet.schema.member.SqlCacheType;
import org.riverock.portlet.schema.member.types.ContentTypeActionType;

/**
 * $Id$
 */
public final class ModuleManager {
    private final static Logger log = Logger.getLogger( ModuleManager.class );

    private final static FileFilter memberFilter = new ExtensionFileFilter( ".xml" );

    private static final int REREAD_DELAY = 1000 * 30;  // scan every 30 seconds
    private final String MILL_MEMBER_DIR = "member";

    private CacheDirectory mainDir = null;
    private CacheDirectory userDir = null;

    private MemberFile mainMemberFile[] = null;
    private MemberFile userMemberFile[] = null;

    private boolean isUserDirectoryExists = true;
    private String rootDir = null;

    private static ModuleManager moduleManager = null;

    public static ModuleManager getInstance(String rootDir) {
        if (moduleManager==null) {
            moduleManager = new ModuleManager(rootDir);
        }
        return moduleManager;
    }

    private ModuleManager(String rootDir) {
        this.rootDir = rootDir;
    }

    private void destroy() {
        mainDir = null;
        userDir = null;
        mainMemberFile = null;
        userMemberFile = null;
    }

    public MemberFile[] getMemberFileArray() {
        MemberFile[] temp = new MemberFile[getCountFile()];

        int idx = 0;
        if( mainMemberFile != null ) {
            for( final MemberFile newVar : mainMemberFile ) {
                if( newVar != null )
                    temp[idx++] = newVar;
            }
        }
        if( userMemberFile != null ) {
            for( final MemberFile newVar : userMemberFile ) {
                if( newVar != null )
                    temp[idx++] = newVar;
            }
        }

        return temp;
    }

    public int getCountFile() {
        return
            ( mainMemberFile == null ? 0 : mainMemberFile.length ) +
            ( userMemberFile == null ? 0 : userMemberFile.length );
    }

    public int getCountModule() {
        int count = 0;
        if( mainMemberFile != null ) {
            for( final MemberFile newVar : mainMemberFile ) {
                if( newVar != null )
                    count += newVar.getMemberModuleCount();
            }
        }
        if( userMemberFile != null ) {
            for( final MemberFile newVar : userMemberFile ) {
                if( newVar != null )
                    count += newVar.getMemberModuleCount();
            }
        }

        return count;
    }


    public ContentType getContent( String moduleName, int actionType ) {
        ModuleType mod = getModule( moduleName );
        if( mod == null )
            return null;

        for( int i = 0; i < mod.getContentCount(); i++ ) {
            ContentType cnt = mod.getContent( i );
            if( cnt.getAction().getType() == actionType )
                return cnt;
        }
        return null;
    }

    private String getCustomDir() {
        return null;
    }

    public static synchronized void reinit() {
        moduleManager.destroy();
        moduleManager = null;
    }

    public void init() {
        if( mainDir == null ) {
            final String cacheDirectory = rootDir + File.separatorChar + MILL_MEMBER_DIR;
            if (log.isDebugEnabled()) {
                log.debug( "Create cached directory: " + cacheDirectory );
            }
            mainDir = new CacheDirectory( cacheDirectory, memberFilter );
        }
        StartupServlet.test();
        if( mainMemberFile == null || !mainDir.isUseCache() ) {
            if( mainDir.isNeedReload() ) {
                final String cacheDirectory = rootDir + File.separatorChar + MILL_MEMBER_DIR;
                if (log.isDebugEnabled()) {
                    log.debug( "Reinit cached directory: " + cacheDirectory );
                }
                mainDir = new CacheDirectory( cacheDirectory, memberFilter );
            }

            if( log.isDebugEnabled() )
                log.debug( "#2.001 read list file" );

            mainDir.processDirectory();

            if( log.isDebugEnabled() )
                log.debug( "#2.003 array of files - " + mainDir.getFileArray() );

            CacheFile cacheFile[] = mainDir.getFileArray();
            mainMemberFile = null;
            mainMemberFile = new MemberFile[cacheFile.length];
            for( int i = 0; i < mainDir.getFileArray().length; i++ ) {
                mainMemberFile[i] = new MemberFile( cacheFile[i].getFile() );
            }
        }
        // init Custom member module list
        if( isUserDirectoryExists ) {
            if( userDir == null ) {
                String customMemberDir = getCustomDir();
                if( customMemberDir != null && customMemberDir.length() != 0 ) {
                    userDir = new CacheDirectory( customMemberDir,
                        memberFilter,
                        REREAD_DELAY // scan every 30 seconds
                    );
                }
                else
                    return;
            }

            if( userMemberFile == null || !userDir.isUseCache() ) {
                if( userDir.isNeedReload() ) {
                    String customMemberDir = getCustomDir();
                    if( customMemberDir != null && customMemberDir.length() != 0 ) {
                        userDir = new CacheDirectory( customMemberDir,
                            memberFilter,
                            REREAD_DELAY
                        );
                    }
                }

                if( log.isDebugEnabled() )
                    log.debug( "#2.001 read list file" );

                userDir.processDirectory();

                if( log.isDebugEnabled() )
                    log.debug( "#2.003 array of files - " + userDir.getFileArray() );

                CacheFile cacheFile[] = userDir.getFileArray();
                userMemberFile = null;
                userMemberFile = new MemberFile[cacheFile.length];
                for( int i = 0; i < userDir.getFileArray().length; i++ ) {
                    userMemberFile[i] = new MemberFile( cacheFile[i].getFile() );
                }
            }
        }
    }

    public ModuleType getModule( String moduleName ) {
        init();
        for( final MemberFile newVar : mainMemberFile ) {
            if( newVar == null )
                continue;

            ModuleType mod = newVar.getModule( moduleName );
            if( mod != null ) {
                if( log.isDebugEnabled() )
                    log.debug( "Module '" + moduleName + "' is found in main directory" );

                for( int i = 0; i < mod.getContentCount(); i++ ) {
                    ContentType content = mod.getContent( i );
                    QueryAreaType qa = content.getQueryArea();
                    if( qa.getSqlCache() == null )
                        qa.setSqlCache( new SqlCacheType() );

                    if( Boolean.FALSE.equals( qa.getSqlCache().getIsInit() ) ) {
                        switch( content.getAction().getType() ) {
                            case ContentTypeActionType.INDEX_TYPE:
                                break;
                            case ContentTypeActionType.INSERT_TYPE:
                                break;
                            case ContentTypeActionType.CHANGE_TYPE:
                                break;
                            case ContentTypeActionType.DELETE_TYPE:
                                break;
                            default:
                                log.error( "unknown type of content - " + content.getAction().toString() );
                        }
                    }
                }
                return mod;
            }
        }

        if( log.isDebugEnabled() )
            log.debug( "Module '" + moduleName + "' in main directory not found" );

        if( userMemberFile != null ) {
            for( final MemberFile newVar : userMemberFile ) {
                if( newVar == null )
                    continue;

                ModuleType mod = newVar.getModule( moduleName );
                if( mod != null ) {
                    if( log.isDebugEnabled() )
                        log.debug( "Module '" + moduleName + "' is found in custom directory" );

                    return mod;
                }
            }
        }

        if( log.isDebugEnabled() )
            log.debug( "Module '" + moduleName + "' not found" );

        return null;
    }
}