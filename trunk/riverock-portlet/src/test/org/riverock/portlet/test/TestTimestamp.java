/*
 * org.riverock.portlet -- Portlet Library
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
 * Date: Mar 31, 2003
 * Time: 10:41:57 AM
 *
 * $Id$
 */

package org.riverock.portlet.test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.common.tools.DateTools;
import org.riverock.generic.utils.DateUtils;

public class TestTimestamp
{
    private static Log cat = LogFactory.getLog( "org.riverock.portlet.test.TestTimestamp" );

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
