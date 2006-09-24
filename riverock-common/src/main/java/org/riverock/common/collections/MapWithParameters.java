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
