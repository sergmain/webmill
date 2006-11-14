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
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author Sergei Maslyukov
 *         Date: 05.09.2006
 *         Time: 18:06:21
 *         <p/>
 *         $Id$
 */
public class TestMapWithParameters extends TestCase {
    private static final String KEY = "key";
    private static final String VALUE = "value";

    public void testPut() throws Throwable {
        Map<String, Object> map = new HashMap<String, Object>();
        MapWithParameters.put(map, KEY, VALUE);
        assertEquals(map.get(KEY), VALUE);
        assertNotNull(map.get(KEY));
        MapWithParameters.put(map, KEY, VALUE);
        assertTrue(map.get(KEY) instanceof List);
        assertTrue(((List)map.get(KEY)).size()==2);
        MapWithParameters.put(map, KEY, VALUE);
        assertTrue(map.get(KEY) instanceof List);
        assertTrue(((List)map.get(KEY)).size()==3);
    }

    public void testConstructor() throws Throwable{
        new MapWithParameters();
    }

    public void testPutInStringList() throws Throwable{
        Map<String, List<String>> map = new HashMap<String, List<String>>();

        MapWithParameters.putInStringList(map, KEY, VALUE);
        assertTrue((map.get(KEY)).size()==1);
        MapWithParameters.putInStringList(map, KEY, VALUE);
        assertTrue((map.get(KEY)).size()==2);
        MapWithParameters.putInStringList(map, KEY, VALUE);
        assertTrue((map.get(KEY)).size()==3);

    }

    public void testPutArrayInStringList() throws Throwable{
        Map<String, List<String>> map = new HashMap<String, List<String>>();

        MapWithParameters.putInStringList(map, KEY, new String[]{VALUE});
        assertTrue((map.get(KEY)).size()==1);
        MapWithParameters.putInStringList(map, KEY, new String[]{VALUE, VALUE});
        assertTrue((map.get(KEY)).size()==3);
        MapWithParameters.putInStringList(map, KEY, new String[]{VALUE, VALUE, VALUE});
        assertTrue((map.get(KEY)).size()==6);

    }


}
