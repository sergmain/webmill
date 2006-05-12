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
