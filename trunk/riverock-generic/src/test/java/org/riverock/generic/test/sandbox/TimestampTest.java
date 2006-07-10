/*
 * org.riverock.generic -- Database connectivity classes
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
package org.riverock.generic.test.sandbox;

import org.riverock.common.tools.DateTools;
import org.riverock.generic.tools.CurrentTimeZone;
import org.riverock.generic.utils.DateUtils;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.Date;

/**
 * Author: mill
 * Date: Mar 31, 2003
 * Time: 10:41:57 AM
 *
 * $Id$
 */
public class TimestampTest
{
//    private static Logger cat = Logger.getLogger( "org.riverock.test.TestTimestamp" );

    public TimestampTest()
    {
    }

    public static void main(String args[])
        throws Exception
    {
        Timestamp t = new Timestamp( System.currentTimeMillis() );
        System.out.println("t = " + t);
        Date date = new Date();
        System.out.println("date = " + date);

         System.out.println("date = " + DateTools.getStringDate(new Date(), "dd.MMMMM.yyyy HH:mm:ss.SSS", Locale.ENGLISH, TimeZone.getTimeZone(  "Europe/Moscow" )) );

        SimpleDateFormat df = new SimpleDateFormat("dd.MMMMM.yyyy HH:mm:ss", Locale.ENGLISH);
        SimpleTimeZone pdt = new SimpleTimeZone(8 * 60 * 60 * 1000, "Europe/Moscow");

        int springDTS = 1;
        int fallDTS = 1;
        pdt.setStartRule(Calendar.MARCH, springDTS, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        pdt.setEndRule(Calendar.OCTOBER, fallDTS, Calendar.SUNDAY, 2 * 60 * 60 * 1000);

        df.setTimeZone( pdt );

        String s = df.format( t );
        System.out.println( s );

        s = DateTools.getStringDate(new Date(), "dd.MMMMM.yyyy HH:mm:ss.SSS", Locale.ENGLISH, TimeZone.getTimeZone(  "Europe/Moscow" ));

        org.riverock.generic.startup.StartupApplication.init();

        s = DateTools.getStringDate(t, "dd.MMMMM.yyyy HH:mm:ss.SSS", Locale.ENGLISH, CurrentTimeZone.getTZ());
//            DateUtils.getStringDate(t, "dd.MMMMM.yyyy HH:mm:ss.SSS", Locale.ENGLISH);

        System.out.println( s );

        t = DateTools.getCurrentTime();
        System.out.println( "#1.1 "+t );

        SimpleDateFormat df1 = new SimpleDateFormat("dd.MMMMM.yyyy HH:mm:ss.SSS", Locale.ENGLISH);
        s = df1.format( t );
        System.out.println( "#1.2 "+s );
        System.out.println( "#1.3 "+t );

//        System.out.println( "Version "+System.getProperty("java.version") );

/*
        long m = DateUtils.getCalendarWithMask(new GregorianCalendar(CurrentTimeZone.getTZ()), "dd.MM.yyyy HH:mm:ss.SSS").getTimeInMillis();
        t =  new Timestamp( m );
        SimpleDateFormat df2 = new SimpleDateFormat("dd.MMMMM.yyyy HH:mm:ss.SSS", Locale.ENGLISH);
        s = df2.format( t );
        System.out.println( s );
*/

        s = DateTools.getStringDate(new Timestamp(System.currentTimeMillis()), "dd.MMMMM.yyyy HH:mm:ss.SSS", Locale.ENGLISH, CurrentTimeZone.getTZ());
//            DateUtils.getStringDate(new Timestamp(System.currentTimeMillis()), "dd.MMMMM.yyyy HH:mm:ss.SSS", Locale.ENGLISH);

        System.out.println( s );

        System.out.println( "current time "+DateUtils.getCurrentDate("dd.MMMMM.yyyy HH:mm:ss.SSS", Locale.ENGLISH) );

        printZoneInfo();
    }

    private static void printZoneInfo()
    {
        String zone[] = TimeZone.getAvailableIDs(1 * 60 * 60 * 1000);
        for (int i=0; i<zone.length; i++)
            System.out.println("zone info "+zone[i]);
    }
}