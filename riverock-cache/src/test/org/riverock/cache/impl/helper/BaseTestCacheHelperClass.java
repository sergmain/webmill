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



import org.apache.log4j.Logger;



import org.riverock.generic.main.CacheItemV2;

import org.riverock.generic.db.DatabaseAdapter;



public class BaseTestCacheHelperClass extends CacheItemV2

{

     private static Logger cat = Logger.getLogger( BaseTestCacheHelperClass.class );



     private static BaseTestCacheHelperClass dummy = new BaseTestCacheHelperClass();



     public TestCacheItem item = new TestCacheItem();



     public BaseTestCacheHelperClass(){}



     public int maxCountItems()

     {

         return 5;

     }



     public long maxTimePeriod()

     {

         return 100000;

     }



     public String getNameClass()

     {

         return "org.riverock.test.cache.helper.BaseTestCacheHelperClass";

     }



     public static BaseTestCacheHelperClass getInstance(DatabaseAdapter db__, Long id__)

         throws Exception

     {

         return getInstance(db__, id__.longValue());

     }



     public static BaseTestCacheHelperClass getInstance(DatabaseAdapter db__, long id__)

         throws Exception

     {

         return (BaseTestCacheHelperClass) dummy.getInstanceNew(db__, id__);

     }



     public TestCacheItem getData(DatabaseAdapter db_, long id)

         throws Exception

     {

         BaseTestCacheHelperClass obj = BaseTestCacheHelperClass.getInstance(db_, id);

         if (obj!=null)

              return obj.item;



         return new TestCacheItem();

     }



     public BaseTestCacheHelperClass(DatabaseAdapter db_, long id)

         throws Exception

     {

         this(db_, new Long(id));

     }



     public BaseTestCacheHelperClass(DatabaseAdapter db_, Long id)

         throws Exception

     {

          item = new TestCacheItem(id.longValue(),  (long)(Math.random()*1000000L));

     }

}

