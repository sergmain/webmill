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



/**

 * Author: mill

 * Date: Apr 30, 2003

 * Time: 1:45:46 PM

 *

 * $Id$

 */



package org.riverock.test.cache;



import org.apache.log4j.Logger;



import junit.framework.TestCase;

import org.riverock.generic.main.CacheItemV2;

import org.riverock.test.cache.helper.TestCacheHelperClass1;

import org.riverock.test.cache.helper.TestCacheHelperClass2;

import org.riverock.test.cache.helper.TestCacheHelperClass3;

import org.riverock.test.cache.helper.TestCacheItem;

import org.riverock.generic.tools.XmlTools;

import org.riverock.generic.startup.InitParam;



public class TestRebuildCacheClass extends TestCase

{

    private static Logger log = Logger.getLogger( TestRebuildCacheClass.class );



    public TestRebuildCacheClass(String testName)

    {

        super(testName);

    }



    public void tearDown()

        throws Exception

    {

        System.out.println("start tearDown()");

    }



    private static final int MIN_COUNT = 4;

    private static final int MAX_COUNT = 10;





    public void doTest()

        throws Exception

    {

        org.riverock.generic.startup.StartupApplication.init();



//        CacheItemV2.CountClassInCache = new Integer(0);

        System.out.println("CountClassInCache " + CacheItemV2.getCountClassInCache());

        TestCacheItem item = null;

        {

            TestCacheHelperClass1 obj1 = null;

            int countLoop = (new TestCacheHelperClass1()).maxCountItems();

            for (int i = 0; i < countLoop; i++)

            {

                obj1 = TestCacheHelperClass1.getInstance(null, i);

                item = obj1.item;

                System.out.println("i: " + i + ", id" + item.id + ", value " + item.value);

            }

            System.out.println("Max of TestCacheHelperClass1 " + obj1.maxCountItems());

//            for (int i = 0; i < MAX_COUNT; i++)

//            {

//                obj1 = TestCacheHelperClass1.getInstance(null, i);

//                item = obj1.item;

//                System.out.println("i: " + i + ", id" + item.id + ", value " + item.value);

//            }

//            System.out.println("Max of TestCacheHelperClass1 " + obj1.maxCountItems());

        }

        {

            TestCacheHelperClass2 obj2 = null;

            int countLoop = (new TestCacheHelperClass2()).maxCountItems();

            for (int i = 0; i < countLoop; i++)

            {

                obj2 = TestCacheHelperClass2.getInstance(null, i);

                item = obj2.item;

                System.out.println("i: " + i + ", id" + item.id + ", value " + item.value);

            }

            System.out.println("Max of TestCacheHelperClass2 " + obj2.maxCountItems());

//            for (int i = 0; i < MAX_COUNT; i++)

//            {

//                obj2 = TestCacheHelperClass2.getInstance(null, i);

//                item = obj2.item;

//                System.out.println("i: " + i + ", id" + item.id + ", value " + item.value);

//            }

//            System.out.println("Max of TestCacheHelperClass2 " + obj2.maxCountItems());

        }

        {

            TestCacheHelperClass3 obj3 = null;

            int countLoop = (new TestCacheHelperClass3()).maxCountItems();

            for (int i = 0; i < countLoop; i++)

            {

                obj3 = TestCacheHelperClass3.getInstance(null, i);

                item = obj3.item;

                System.out.println("i: " + i + ", id" + item.id + ", value " + item.value);

            }

            System.out.println("Max of TestCacheHelperClass3 " + obj3.maxCountItems());

//            for (int i = 0; i < MAX_COUNT; i++)

//            {

//                obj3 = TestCacheHelperClass3.getInstance(null, i);

//                item = obj3.item;

//                System.out.println("i: " + i + ", id" + item.id + ", value " + item.value);

//            }

//            System.out.println("Max of TestCacheHelperClass3 " + obj3.maxCountItems());

        }

        log.fatal("start test cache");

        {

            TestCacheHelperClass1 obj1 = null;

            int countLoop = (new TestCacheHelperClass1()).maxCountItems();

            for (int i = 0; i < countLoop; i++)

            {

                obj1 = TestCacheHelperClass1.getInstance(null, i);

                item = obj1.item;

                System.out.println("i: " + i + ", id" + item.id + ", value " + item.value);

            }

            System.out.println("Max of TestCacheHelperClass1 " + obj1.maxCountItems());

        }

        {

            TestCacheHelperClass3 obj3 = null;

            int countLoop = (new TestCacheHelperClass3()).maxCountItems();

            for (int i = 0; i < countLoop; i++)

            {

                obj3 = TestCacheHelperClass3.getInstance(null, i);

                item = obj3.item;

                System.out.println("i: " + i + ", id" + item.id + ", value " + item.value);

            }

            System.out.println("Max of TestCacheHelperClass3 " + obj3.maxCountItems());

        }

        {

            TestCacheHelperClass2 obj2 = null;

            int countLoop = (new TestCacheHelperClass2()).maxCountItems();

            for (int i = 0; i < countLoop; i++)

            {

                obj2 = TestCacheHelperClass2.getInstance(null, i);

                item = obj2.item;

                System.out.println("i: " + i + ", id" + item.id + ", value " + item.value);

            }

            System.out.println("Max of TestCacheHelperClass2 " + obj2.maxCountItems());

        }

        System.out.println("CountClassInCache " + CacheItemV2.getCountClassInCache());

    }

}

