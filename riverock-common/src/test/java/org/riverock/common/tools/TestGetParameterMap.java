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
