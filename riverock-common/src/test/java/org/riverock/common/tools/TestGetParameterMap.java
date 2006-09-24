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
