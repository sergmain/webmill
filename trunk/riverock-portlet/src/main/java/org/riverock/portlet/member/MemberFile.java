/*
 * org.riverock.portlet - Portlet Library
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
package org.riverock.portlet.member;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import org.riverock.generic.main.CacheFile;
import org.riverock.portlet.schema.member.MemberArea;
import org.riverock.portlet.schema.member.ModuleType;
import org.riverock.portlet.tools.SiteUtils;

/**
 * $Id$
 */
public final class MemberFile extends CacheFile {
    private final static Logger log = Logger.getLogger( MemberFile.class );

    private Map<String, ModuleType> memberHash = null;

    /**
     * @deprecated use Iterator
     * @return Enumeration
     */
    public Enumeration getMemberModules() {
        if( memberHash == null )
            return null;

        return Collections.enumeration( memberHash.values() );
    }

    public int getMemberModuleCount() {
        if( memberHash == null )
            return 0;

        return memberHash.size();
    }

    public ModuleType getModule( String nameModule_ ) {
        if( memberHash == null || nameModule_ == null )
            return null;

        boolean isUseCache = isUseCache();
        if( log.isDebugEnabled() )
            log.debug( "#7.1.0  " + nameModule_ + ", isUseCache:" + isUseCache + ", " + getFile().getName() );

        if( isUseCache ) {
            ModuleType moduleType = memberHash.get( nameModule_ );
            if( log.isDebugEnabled() )
                log.debug( "moduleType: " + moduleType );

            return moduleType;
        }

        try {
            if( log.isDebugEnabled() ) {
                log.debug( "#7.00.01 " + getFile() );
                log.debug( "#7.01.00 check was file modified" );
            }

            if( isNeedReload() ) {

                if( log.isInfoEnabled() )
                    log.info( "Redeploy member file " + getFile().getName() );

                setLastModified();

                processFile();

                if( log.isDebugEnabled() )
                    log.debug( "#7.01.02 Unmarshal done" );

                return memberHash.get( nameModule_ );
            }
        }
        catch( Exception e ) {
            log.error( "Exception while get module " + nameModule_, e );
            return null;
        }

        ModuleType moduleType = memberHash.get( nameModule_ );
        if( log.isDebugEnabled() ) {
            log.debug( "#7.02 file not changed. Get module from cache" );
            log.debug( "moduleType: " + moduleType );
            log.debug( "Values in HashMap: " + memberHash.size() );
            for (Map.Entry<String, ModuleType> entry : memberHash.entrySet()) {
                log.debug("key: " + entry.getKey() + ", value: " + entry.getValue().getName());
            }
        }

        return moduleType;
    }

    public MemberFile( File tempFile ) {
        super( tempFile, 1000 * 10 );

        if( log.isDebugEnabled() ) {
            log.debug( "Start unmarshalling file " + tempFile );
        }
        processFile();
    }

    private final static Object syncObj = new Object();
    private void processFile() {
        try {
            log.debug( "start processFile()" );

            InputSource inSrc = new InputSource( new FileInputStream( getFile() ) );
            MemberArea ma = ( MemberArea ) Unmarshaller.unmarshal( MemberArea.class, inSrc );
            ma.validate();

            memberHash = new HashMap<String, ModuleType>();
            for( int i = 0; i < ma.getModuleCount(); i++ ) {
                ModuleType desc = ma.getModule( i );
                if( log.isDebugEnabled() ) {
                    log.debug( "put new module in filenaame hash, key: " + desc.getName() + ", " + desc.getName() );
                }
                memberHash.put( desc.getName(), desc );
            }


            if( log.isDebugEnabled() ) {
                synchronized( syncObj ) {
                    try {
                        FileWriter w = new FileWriter( SiteUtils.getTempDir() + File.separatorChar + System.currentTimeMillis() + '-' + getFile().getName() );
                        Marshaller.marshal( ma, w );
                    }
                    catch( Exception e ) {
                        //catch debug error
                    }
                }
            }
        }
        catch( Exception e ) {
            String errorString = "Error processing member file " + getFile();
            log.error( errorString, e );
            throw new IllegalStateException(errorString, e);
        }
    }
}
