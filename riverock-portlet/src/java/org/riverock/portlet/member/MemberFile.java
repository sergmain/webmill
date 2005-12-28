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
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
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

    protected void finalize() throws Throwable {
        if( memberHash != null ) {
            memberHash.clear();
            memberHash = null;
        }
        super.finalize();
    }

    /**
     * @deprecated use Iterator
     * @return
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

        ModuleType moduleType = null;
        if( isUseCache ) {
            moduleType = memberHash.get( nameModule_ );
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

        moduleType = ( ModuleType ) memberHash.get( nameModule_ );
        if( log.isDebugEnabled() ) {
            log.debug( "#7.02 file not changed. Get module from cache" );
            log.debug( "moduleType: " + moduleType );
            log.debug( "Values in HashMap: " + memberHash.size() );
            Iterator iterator = memberHash.keySet().iterator();
            while( iterator.hasNext() ) {
                String key = ( String ) iterator.next();
                ModuleType type = ( ModuleType ) memberHash.get( key );
                log.debug( "key: " + key + ", value: " + type.getName() );
            }
        }

        return moduleType;
    }

    public MemberFile( File tempFile )
        throws Exception {
        super( tempFile, 1000 * 10 );

        if( log.isDebugEnabled() )
            log.debug( "Start unmarshalling file " + tempFile );

        try {
            if( log.isDebugEnabled() )
                log.debug( "Unmarshal new file: " + tempFile.getName() );

            processFile();
        }
        catch( Exception e ) {
            log.error( "Exception while unmarshalling member file + " + getFile(), e );
            if( memberHash != null )
                memberHash.clear();
            memberHash = null;
            throw e;
        }
    }

    private static Object syncObj = new Object();

    private void processFile() throws Exception {
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
                    }
                }
            }
        }
        catch( Exception e ) {
            String errorString = "Error processing member file " + getFile();
            log.error( errorString, e );
            throw e;
        }
    }
}
