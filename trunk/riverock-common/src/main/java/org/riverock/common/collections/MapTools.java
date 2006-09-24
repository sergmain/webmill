/*
 * org.riverock.common - Supporting classes and utilities
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
package org.riverock.common.collections;

import java.util.Map;

import org.apache.log4j.Logger;

import org.riverock.common.tools.ServletTools;

/**
 * User: SergeMaslyukov
 * Date: 12.01.2005
 * Time: 10:08:39
 * $Id$
 */
public class MapTools {

    private final static Logger log = Logger.getLogger( ServletTools.class );

    public static String getString( final Map map, final String f ) {
        return getString( map, f, null );
    }

    public static String getString( final Map map, final String f, final String def ) {
        Object obj = map.get( f );
        if ( obj != null ) {
            try {
                if (obj instanceof String) {
                    return (String)obj;
                }
                else if (obj instanceof String[]) {
                    return ((String[])obj)[0];
                }
                else {
                    throw new IllegalStateException("value in Map must String or String[] type. Current type: " + obj.getClass().getName());
                }
            }
            catch( Exception exc ) {
                // not rethrow exception 'cos this method return def value in this case
                log.warn( "Exception in getInt(), def value will be return", exc );
            }
        }
        return def;
    }

    public static Integer getInt( final Map map, final String f ) {
        return getInt( map, f, null );
    }

    public static Integer getInt( final Map map, final String f, final Integer def ) {
        Integer i_ = def;
        Object obj = map.get( f );
        if ( obj != null ) {
            try {
                if (obj instanceof String) {
                    i_ = new Integer( (String)obj );
                }
                else if (obj instanceof String[]) {
                    i_ = new Integer( ((String[])obj)[0] );
                }
                else {
                    throw new IllegalStateException("value in Map must String or String[] type. Current type: " + obj.getClass().getName());
                }
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
        if ( obj!= null) {
            try {
                if (obj instanceof String) {
                    i_ = new Long( (String)obj );
                }
                else if (obj instanceof String[]) {
                    i_ = new Long( ((String[])obj)[0] );
                }
                else {
                    throw new IllegalStateException("value in Map must String or String[] type. Current type: " + obj.getClass().getName());
                }
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
                if (obj instanceof String) {
                    String s_ = (String)obj;
                    s_ = s_.replace( ',', '.' );
                    i_ = new Float( s_ );
                }
                else if (obj instanceof String[]) {
                    String s_ = ((String[])obj)[0];
                    s_ = s_.replace( ',', '.' );
                    i_ = new Float( s_ );
                }
                else {
                    throw new IllegalStateException("value in Map must String or String[] type. Current type: " + obj.getClass().getName());
                }
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
                if (obj instanceof String) {
                    String s_ = (String)obj;
                    s_ = s_.replace( ',', '.' );
                    i_ = new Double( s_ );
                }
                else if (obj instanceof String[]) {
                    String s_ = ((String[])obj)[0];
                    s_ = s_.replace( ',', '.' );
                    i_ = new Double( s_ );
                }
                else {
                    throw new IllegalStateException("value in Map must String or String[] type. Current type: " + obj.getClass().getName());
                }
            }
            catch( Exception exc ) {
                // not rethrow exception 'cos this method return def value in this case
                log.warn( "Exception in getDouble(), def value will be return", exc );
            }
        }
        return i_;
    }
}
