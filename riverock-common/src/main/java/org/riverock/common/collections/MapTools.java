/*
 * org.riverock.common - Supporting classes, interfaces, and utilities
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
package org.riverock.common.collections;

import java.util.Map;

/**
 * User: SergeMaslyukov
 * Date: 12.01.2005
 * Time: 10:08:39
 * $Id: MapTools.java 1492 2009-10-30 14:02:35Z serg_main $
 */
public class MapTools {

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
            }
        }
        return i_;
    }
}
