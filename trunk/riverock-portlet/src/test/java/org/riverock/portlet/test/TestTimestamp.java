/*
 * org.riverock.portlet - Portlet Library
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
 * Author: mill
 * Date: Mar 31, 2003
 * Time: 10:41:57 AM
 *
 * $Id$
 */

package org.riverock.portlet.test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import org.riverock.common.tools.DateTools;
import org.riverock.generic.utils.DateUtils;

public class TestTimestamp
{
    private static Logger cat = Logger.getLogger( "org.riverock.portlet.test.TestTimestamp" );

    public TestTimestamp()
    {
    }

    private static void printZoneInfo()
    {
        String zone[] = TimeZone.getAvailableIDs(1 * 60 * 60 * 1000);
        for (int i=0; i<zone.length; i++)
            System.out.println("zone info "+zone[i]);
    }

    public static void main(String args[])
        throws Exception
    {
        Timestamp t = new Timestamp( System.currentTimeMillis() );

        SimpleDateFormat df = new SimpleDateFormat("dd.MMMMM.yyyy HH:mm:ss", Locale.ENGLISH);
        SimpleTimeZone pdt = new SimpleTimeZone(8 * 60 * 60 * 1000, "Asia/Irkutsk");

        int springDTS = 1;
        int fallDTS = 1;
        pdt.setStartRule(Calendar.MARCH, springDTS, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        pdt.setEndRule(Calendar.OCTOBER, fallDTS, Calendar.SUNDAY, 2 * 60 * 60 * 1000);

        df.setTimeZone( pdt );

        String s = df.format( t );
        System.out.println( s );

        org.riverock.generic.startup.StartupApplication.init();

        s = DateUtils.getStringDate(t, "dd.MMMMM.yyyy HH:mm:ss.SSS", Locale.ENGLISH);
        System.out.println( s );

        t = DateTools.getCurrentTime();
        System.out.println( "#1.1 "+t );

        SimpleDateFormat df1 = new SimpleDateFormat("dd.MMMMM.yyyy HH:mm:ss.SSS", Locale.ENGLISH);
        s = df1.format( t );
        System.out.println( "#1.2 "+s );
        System.out.println( "#1.3 "+t );

//        System.out.println( "Version "+System.getProperty("java.version") );

/*
        long m = DateTools.getCalendarWithMask(new GregorianCalendar(CurrentTimeZone.getTZ()), "dd.MM.yyyy HH:mm:ss.SSS").getTimeInMillis();
        t =  new Timestamp( m );
        SimpleDateFormat df2 = new SimpleDateFormat("dd.MMMMM.yyyy HH:mm:ss.SSS", Locale.ENGLISH);
        s = df2.format( t );
        System.out.println( s );
*/

        s = DateUtils.getStringDate(new Timestamp(System.currentTimeMillis()), "dd.MMMMM.yyyy HH:mm:ss.SSS", Locale.ENGLISH);
        System.out.println( s );

        System.out.println( "current time "+DateUtils.getCurrentDate("dd.MMMMM.yyyy HH:mm:ss.SSS", Locale.ENGLISH) );

        printZoneInfo();
    }
}
