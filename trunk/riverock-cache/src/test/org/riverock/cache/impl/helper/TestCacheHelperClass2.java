/*

 * org.riverock.cache -- Generic cache implementation

 * 

 * Copyright (C) 2004, Riverock Software, All Rights Reserved.

 * 

 * Riverock -- The Open-source Java Development Community

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

 *

 */



package org.riverock.cache.impl.helper;



import org.riverock.generic.db.DBconnect;

import org.riverock.generic.main.CacheItemV2;



public class TestCacheHelperClass2 extends CacheItemV2

{

     private static TestCacheHelperClass2 dummy = new TestCacheHelperClass2();



    public TestCacheItem item = new TestCacheItem();



    public int maxCountItems()

    {

        return 7;

    }



    public long maxTimePeriod()

    {

        return 100000;

    }



     public TestCacheHelperClass2(){

         super();

     }



     public String getNameClass()

     {

         return "org.riverock.test.cache.helper.TestCacheHelperClass2";

     }



     public static TestCacheHelperClass2 getInstance(DBconnect db__, Long id__)

         throws Exception

     {

         return getInstance(db__, id__.longValue());

     }



     public static TestCacheHelperClass2 getInstance(DBconnect db__, long id__)

         throws Exception

     {

         return (TestCacheHelperClass2) dummy.getInstanceNew(db__, id__);

     }



     public TestCacheHelperClass2(DBconnect db_, long id)

         throws Exception

     {

         this(db_, new Long(id));

     }



     public TestCacheHelperClass2(DBconnect db_, Long id)

         throws Exception

     {

         item = new TestCacheItem(id.longValue(),  (long)(Math.random()*1000000L));

     }

}

