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
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: SergeMaslyukov
 * Date: 19.12.2004
 * Time: 18:49:30
 * $Id$
 */
public final class MapWithParameters {

    public static void put( final Map<String, Object>  map, final String key, final Object value ) {
        Object obj = map.get( key );
        if (obj==null) {
            map.put( key, value );
        }
        else if (obj instanceof List ) {
            List<Object> objects = (List<Object>)obj;
            objects.add( value );
        }
        else {
            map.remove( key );
            List<Object> list = new ArrayList<Object>();
            list.add( obj );
            list.add( value );
            map.put( key, list );
        }
    }

    public static void putInStringList( final Map<String, List<String>>  map, final String key, final String value ) {
        List<String> obj = map.get( key );
        if (obj==null) {
            List<String> list = new ArrayList<String>();
            list.add( value );
            map.put( key, list );
        }
        else {
            obj.add( value );
        }
    }

    public static void putInStringList( final Map<String, List<String>>  map, final String key, final String[] values ) {
        List<String> obj = map.get( key );
        if (obj==null) {
            List<String> list = new ArrayList<String>();
            list.addAll( Arrays.asList(values) );
            map.put( key, list );
        }
        else {
            obj.addAll( Arrays.asList(values) );
        }
    }
}
