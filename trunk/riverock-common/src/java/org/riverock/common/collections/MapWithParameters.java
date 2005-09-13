package org.riverock.common.collections;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * User: SergeMaslyukov
 * Date: 19.12.2004
 * Time: 18:49:30
 * $Id$
 */
public final class MapWithParameters {

    public static void put( final Map map, final String key, final Object value ) {
        Object obj = map.get( key );
        if (obj==null) {
            map.put( key, value );
        }
        else if (obj instanceof List ) {
            ((List)obj).add( value );
        }
        else {
            map.remove( key );
            List list = new ArrayList();
            list.add( obj );
            list.add( value );
            map.put( key, list );
        }
    }
}
