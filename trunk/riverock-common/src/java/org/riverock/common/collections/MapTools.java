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
package org.riverock.common.collections;

import java.util.Map;

import org.riverock.common.tools.ServletTools;

import org.apache.log4j.Logger;

/**
 * User: SergeMaslyukov
 * Date: 12.01.2005
 * Time: 10:08:39
 * $Id$
 */
public class MapTools {

    private final static Logger log = Logger.getLogger( ServletTools.class );

    public static Integer getInt( final Map map, final String f ) {
        return getInt( map, f, null );
    }

    public static Integer getInt( final Map map, final String f, final Integer def ) {
        Integer i_ = def;
        Object obj = map.get( f );
        if ( obj != null ) {
            try {
                i_ = new Integer( obj.toString() );
            }
            catch( Exception exc ) {
                // not rethrow exception 'cos this method return def value in this case
                log.warn( "Exception in getInt(), def value will be return", exc );
            }
        }
        return i_;
    }

    public static Long getLong( final Map map, final String f ) {
        return getLong( map, f, null );
    }

    public static Long getLong( final Map map, final String f, final Long def ) {
        Long i_ = def;
        Object obj = map.get( f );
        if ( obj != null ) {
            try {
                i_ = new Long( obj.toString() );
            }
            catch( Exception exc ) {
                // not rethrow exception 'cos this method return def value in this case
                log.warn( "Exception in getLong(), def value will be return", exc );
            }
        }
        return i_;
    }

    public static Float getFloat( final Map map, final String f ) {
        return getFloat( map, f, null );
    }

    public static Float getFloat( final Map map, final String f, final Float def ) {
        Float i_ = def;
        Object obj = map.get( f );
        if ( obj != null ) {
            try {
                String s_ = obj.toString();
                s_ = s_.replace( ',', '.' );
                i_ = new Float( s_ );
            }
            catch( Exception exc ) {
                // not rethrow exception 'cos this method return def value in this case
                log.warn( "Exception in getFloat(), def value will be return", exc );
            }
        }
        return i_;
    }

    public static Double getDouble( final Map map, final String f ) {
        return getDouble( map, f, null );
    }

    public static Double getDouble( final Map map, final String f, final Double def ) {
        Double i_ = def;
        Object obj = map.get( f );
        if ( obj != null ) {
            try {
                String s_ = obj.toString();
                s_ = s_.replace( ',', '.' );
                i_ = new Double( s_ );
            }
            catch( Exception exc ) {
                // not rethrow exception 'cos this method return def value in this case
                log.warn( "Exception in getDouble(), def value will be return", exc );
            }
        }
        return i_;
    }
}
