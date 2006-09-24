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