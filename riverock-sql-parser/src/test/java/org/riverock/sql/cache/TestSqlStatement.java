/*
 * org.riverock.sql - Classes for tracking database changes
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

/**
 * User: Admin
 * Date: May 10, 2003
 * Time: 6:16:50 PM
 *
 * $Id$
 */
package org.riverock.sql.cache;

import java.util.*;
import java.sql.Timestamp;

import org.riverock.sql.parser.Parser;

import org.apache.log4j.Logger;
import junit.framework.TestCase;

public class TestSqlStatement extends TestCase
{
    private static Logger log = Logger.getLogger( "org.riverock.sql.cache.TestSqlStatement" );

    public TestSqlStatement(String msg)
    {
        super(msg);
    }

/*
    public static class InternalTestDate
    {
        public String s = null;
        public String m = null;
        public int dd;
        public int MM;
        public int yyyy;
        public int HH;
        public int mm;
        public int ss;
        public int SSS;
        public boolean isException = false;
        public boolean isResultNull = false;


        public InternalTestDate(){}

        public InternalTestDate(String s_, String m_, boolean isException_, boolean isResultNull_)
        {
            this.s = s_;
            this.m = m_;
            this.isException = isException_;
            this.isResultNull = isResultNull_;
        }

        public InternalTestDate(String s_, String m_, int dd_, int MM_, int yyyy_, int HH_, int mm_, int ss_, int SSS_,boolean isException_)
        {
            this.s = s_;
            this.m = m_;
            this.dd = dd_;
            this.MM = MM_;
            this.yyyy = yyyy_;
            this.HH = HH_;
            this.mm = mm_;
            this.ss = ss_;
            this.SSS = SSS_;
            this.isException = isException_;
        }
    }

    public void testGetDateWithMask()
     throws Exception
    {
        InternalTestDate[] testDateWithMaskArray =
            {
                new InternalTestDate("18/12/1970 23:04:45", "dd/MM/yyyy HH:mm:ss", 18, 12, 1970, 23, 4, 45, 0, false),
                new InternalTestDate("18/12/2003 23:04:45", "dd/MM/yyyy HH:mm:ss", 18, 12, 2003, 23, 4, 45, 0, false),
                new InternalTestDate(null, "dd/MM/yyyy HH:mm:ss", false, true),
                new InternalTestDate(null, null, false, true),
                new InternalTestDate("18/12/2003 23:04:45", null, false, true),
                new InternalTestDate("18/12/2003f f23:04:45", "dd/MM/yyyy HH:aabb:ss", true, false)
            };



        for (int i=0; i<testDateWithMaskArray.length; i++)
        {
            try
            {
                Date d = DateTools.getDateWithMask(testDateWithMaskArray[i].s, testDateWithMaskArray[i].m);
                if (testDateWithMaskArray[i].isResultNull)
                    assertEquals(true, d==null);
                else
                {
                    assertEquals(true, d!=null);

                    assertEquals(testDateWithMaskArray[i].yyyy-1900, d.getYear());
                    assertEquals(testDateWithMaskArray[i].MM-1,d.getMonth());
                    assertEquals(testDateWithMaskArray[i].dd,d.getDate());
                    assertEquals(testDateWithMaskArray[i].HH,d.getHours());
                    assertEquals(testDateWithMaskArray[i].mm,d.getMinutes());
                    assertEquals(testDateWithMaskArray[i].ss,d.getSeconds());
                }
//            assertEquals(testDateWithMaskArray[i].SSS,d.get());
            }
            catch(Exception exc)
            {
                assertEquals(true, testDateWithMaskArray[i].isException);

                if (!testDateWithMaskArray[i].isException)
                {
                    System.out.println(
                        "array index - "+i+", string - "+
                        testDateWithMaskArray[i].s+
                        ",mask - "+testDateWithMaskArray[i].m
                    );
                    throw exc;
                }
            }
        }
    }
*/

    public static class IntGSD
    {
        public Class s = null;
        public Class m = null;
        public boolean isException = false;
        public boolean isResultNull = false;

        public IntGSD(){}

        public IntGSD(Class s_, Class m_, boolean isException_, boolean isResultNull_)
        {
            this.s = s_;
            this.m = m_;
            this.isException = isException_;
            this.isResultNull = isResultNull_;
        }

    }

    public void testRegisterRelateClass()
        throws Exception
    {
        IntGSD[] testRegisterRelateClass =
            {
                new IntGSD(TestSqlStatement.class, null, true, false ),
                new IntGSD(null, TestSqlStatement.class, true, false ),
                new IntGSD(TestSqlStatement.class, TestSqlStatement.class, true, false )

            };

        for (int i=0; i<testRegisterRelateClass.length; i++)
        {
            try
            {
                SqlStatement.registerRelateClass(
                    testRegisterRelateClass[i].s,
                    testRegisterRelateClass[i].m
                );

//                if (testRegisterRelateClass[i].isResultNull)
//                    assertEquals(true, result==null);
//                else
//                {
//                    assertEquals(true, result!=null);
//
//                    assertTrue(
//                        "\nError, result "+result+", origin "+testGetStringDateArray[i].s,
//                        result.equals(testGetStringDateArray[i].s)
//                    );
//                }
            }
            catch(Exception exc)
            {
                assertEquals(true, testRegisterRelateClass[i].isException);

                if (!testRegisterRelateClass[i].isException)
                {
                    System.out.println(
                        "array index - "+i+", string - "+
                        testRegisterRelateClass[i].s+
                        ",mask - "+testRegisterRelateClass[i].m
                    );
                    throw exc;
                }
            }
        }
    }
}
