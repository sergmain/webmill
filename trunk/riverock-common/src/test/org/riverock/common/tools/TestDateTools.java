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



/**

 * User: serg_main

 * Date: 30.10.2003

 * Time: 12:52:36

 * @author Serge Maslyukov

 * $Id$

 */



package org.riverock.common.tools;



import junit.framework.TestCase;

import org.apache.log4j.Logger;



import java.sql.Timestamp;

import java.util.Date;

import java.util.Locale;

import java.util.TimeZone;



public class TestDateTools  extends TestCase

{

    private static Logger log = Logger.getLogger( "org.riverock.tools.common.TestDateTools" );



    public TestDateTools(String msg)

    {

        super(msg);

    }



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





    public static class IntGSD

    {

        public Timestamp t = null;

        public String s = null;

        public String m = null;

        public Locale l = null;

        public TimeZone tz = null;

        public boolean isException = false;

        public boolean isResultNull = false;



        public IntGSD(){}



        public IntGSD(Timestamp t_, String s_, String m_, boolean isException_, boolean isResultNull_)

        {

            this.t = t_;

            this.s = s_;

            this.m = m_;

            this.isException = isException_;

            this.isResultNull = isResultNull_;

        }



        public IntGSD(Timestamp t_, String s_, String m_, Locale l_, TimeZone tz_, boolean isException_)

        {

            this.t = t_;

            this.s = s_;

            this.m = m_;

            this.l = l_;

            this.tz = tz_;

            this.isException = isException_;

        }

    }



    public void testGetStringDate()

        throws Exception

    {

        IntGSD[] testGetStringDateArray =

            {

                new IntGSD(

                    new Timestamp(70, 12-1, 18, 23, 4, 45, 0),

                    "18/12/1970 23:04:45.000",

                    "dd/MM/yyyy HH:mm:ss.SSS",

                    Locale.ENGLISH,

                    TimeZone.getTimeZone("Europe/Moscow"),

                    false

                ),

                new IntGSD(

                    new Timestamp(70, 12-1, 18, 23, 4, 45, 0),

                    "18/12/1970 23:04:45.000",

                    "dd/MM/yyyy HH:mm:ss.SSS",

                    Locale.ENGLISH,

                    TimeZone.getTimeZone("Europe/Moscow"),

                    false

                )



            };



        for (int i=0; i<testGetStringDateArray.length; i++)

        {

            try

            {

                String result = DateTools.getStringDate(

                    testGetStringDateArray[i].t,

                    testGetStringDateArray[i].m,

                    testGetStringDateArray[i].l,

                    testGetStringDateArray[i].tz

                    );



//                System.out.println("result "+result);



                if (testGetStringDateArray[i].isResultNull)

                    assertEquals(true, result==null);

                else

                {

                    assertEquals(true, result!=null);



                    assertTrue(

                        "\nError, result "+result+", origin "+testGetStringDateArray[i].s,

                        result.equals(testGetStringDateArray[i].s)

                    );

                }

            }

            catch(Exception exc)

            {

                assertEquals(true, testGetStringDateArray[i].isException);



                if (!testGetStringDateArray[i].isException)

                {

                    System.out.println(

                        "array index - "+i+", string - "+

                        testGetStringDateArray[i].s+

                        ",mask - "+testGetStringDateArray[i].m +

                        ",timestamp - "+testGetStringDateArray[i].t

                    );

                    throw exc;

                }

            }

        }

    }



/*

    public static String getStringDate(java.util.Date date, String mask, Locale loc)

    {

        return getStringDate(date, mask, loc, TimeZone.getTimeZone("Europe/Moscow"));

    }

*/



/*

    public static Calendar getCalendarWithMask(String date, String mask)

            throws java.text.ParseException

    {

        if (date == null || mask == null)

            return null;



        SimpleDateFormat dFormat = new SimpleDateFormat(mask);

        Calendar c = Calendar.getInstance();

        c.clear();

        c.setTime(dFormat.parse(date));



        return c;

    }



    public static Calendar getCalendarWithMask(Calendar c_, String mask)

            throws java.text.ParseException

    {

        return getCalendarWithMask(c_, mask, Locale.ENGLISH);

    }



    public static Calendar getCalendarWithMask(Calendar c_, String mask, Locale locale)

            throws java.text.ParseException

    {

        if (c_ == null)

            return null;



        SimpleDateFormat dFormat = new SimpleDateFormat(mask);

        String currDate = getStringDate(c_, mask, locale);

        Calendar c = (Calendar) c_.clone();

        c.clear();

        c.setTime(dFormat.parse(currDate));



        return c;

    }



    public static boolean compareDate(java.sql.Date d1, java.sql.Date d2)

    {

        if ((d1 == null) || (d2 == null))

            return false;



        Calendar c1 = new GregorianCalendar();

        c1.setTime(d1);

        Calendar c2 = new GregorianCalendar();

        c2.setTime(d2);



        return compareDate(c1, c2);

    }



    public static boolean compareDate(Calendar c1, Calendar c2, String mask)

            throws java.text.ParseException

    {

        return compareDate(

                getCalendarWithMask(c1, mask),

                getCalendarWithMask(c2, mask)

        );

    }



    public static boolean compareDate(Calendar c1, Calendar c2)

    {

        if ((c1 == null) || (c2 == null))

            return false;



        if (c1.before(c2))

            return false;



        if (c2.before(c1))

            return false;



        return true;

    }



    public static String getStringDate(Calendar c, String mask)

    {

        return getStringDate(c, mask, Locale.ENGLISH);

    }



    public static String getStringDate(Calendar c, String mask, Locale loc)

    {

        if (c == null) return null;



        SimpleDateFormat df = new SimpleDateFormat(mask, loc);

        df.setTimeZone(c.getTimeZone());

        return df.format(c.getTime());

    }



    public static String getCurrentDate(String mask, Locale loc, TimeZone tz)

    {

        return getStringDate(new GregorianCalendar(tz), mask, loc);

    }



    public static String getCurrentDate(String mask, TimeZone tz)

    {

        return getStringDate(new GregorianCalendar(tz), mask, Locale.ENGLISH);

    }

*/

}

