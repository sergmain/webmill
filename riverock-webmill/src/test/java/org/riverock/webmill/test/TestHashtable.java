/*
 * org.riverock.webmill - Webmill portal with support jsr-168, xml/xslt and others things.
 * For more information, please visit project site http://webmill.riverock.org
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
 * http://www.riverock.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

/**
 * User: Admin
 * Date: Mar 8, 2003
 * Time: 1:41:26 PM
 *
 * $Id$
 */
package org.riverock.webmill.test;

import java.util.HashMap;
import java.util.Map;

public class TestHashtable
{
    public static void main(String args[])
        throws Exception
    {
        Map<String, String> hash = new HashMap<String, String>(20);

        hash.put( "1", "Object 1");
        hash.put( "2", "Object 2");
        hash.put( "3", "Object 3");
        hash.put( "4", "Object 4");

        hash.put( "2", "Object 222");

/*
        for (Enumeration e = hash.elements() ; e.hasMoreElements() ;) {
            System.out.println( e.nextElement() );
         }
*/
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
