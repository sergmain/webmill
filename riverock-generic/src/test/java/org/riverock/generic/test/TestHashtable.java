/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
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
package org.riverock.generic.test;

import java.util.Hashtable;
import java.util.Enumeration;

/**
 * User: Admin
 * Date: Mar 8, 2003
 * Time: 1:41:26 PM
 *
 * $Id$
 */
public class TestHashtable
{
    public static void main(String args[])
        throws Exception
    {
        Hashtable hash = new Hashtable(20);

        hash.put( "1", "Object 1");
        hash.put( "2", "Object 2");
        hash.put( "3", "Object 3");
        hash.put( "4", "Object 4");

        hash.put( "2", "Object 222");

        for (Enumeration e = hash.elements() ; e.hasMoreElements() ;) {
            System.out.println(e.nextElement());
         }

        System.out.println("count element - "+hash.size());
        hash.put( "5", "Object 5");
        System.out.println("count element - "+hash.size());

        String key1 = new String("1");
        String key2 = new String("1");

        System.out.println("key 1 - "+key1.getClass());
        System.out.println("key 2 - "+key2.getClass());
        System.out.println("key1==key2  - "+(key1==key2));

        System.out.println("object with key 1 - "+hash.get(key1));
        System.out.println("object with key 2 - "+hash.get(key2));

    }
}
