package org.riverock.common.tools;

import java.util.Map;
import java.util.List;

import junit.framework.TestCase;

/**
 * User: SergeMaslyukov
 * Date: 04.02.2005
 * Time: 0:44:53
 * $Id$
 */
public class TestGetParameterMap extends TestCase {

    public void testParseContentType()
        throws Exception
    {
        Map map = null;

        map = ServletTools.getParameterMap( "aaa=bbb" );
        assertFalse("count wrong", map.size()!=1 );
        assertFalse("key wrong", map.get("aaa")==null );
        assertFalse("param wrong", !(map.get( "aaa") instanceof String ) );

        map = ServletTools.getParameterMap( "aaa=bbb&aaa=ccc" );
        assertFalse("count wrong", map.size()!=1 );
        assertFalse("key wrong", map.get("aaa")==null );
        assertFalse("param wrong, class: "+map.keySet().toArray()[0].getClass().getName(),
            !(map.get( "aaa" ) instanceof List ) );

        map = ServletTools.getParameterMap( "aaa=bbb&ccc=ddd" );
        assertFalse("count wrong", map.size()!=2 );
        assertFalse("key wrong", map.get("aaa")==null );
        assertFalse("key wrong", map.get("ccc")==null );
        assertFalse("param wrong",
            !(map.get( "aaa" ) instanceof String ) );
        assertFalse("param wrong",
            !(map.get( "ccc" ) instanceof String ) );

        map = ServletTools.getParameterMap( "aaa=bbb&ccc=ddd&aaa=ddd" );
        assertFalse("count wrong", map.size()!=2 );
        assertFalse("key wrong", map.get("aaa")==null );
        assertFalse("key wrong", map.get("ccc")==null );
        assertFalse("param wrong", !(map.get( "aaa" ) instanceof List ) );
        assertFalse("param wrong", !(map.get( "ccc" ) instanceof String ) );
    }
}
