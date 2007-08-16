/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
import org.apache.commons.lang.time.DateFormatUtils;

import org.riverock.common.tools.DateTools;

public class TestTimestamp {
    private final static Logger cat = Logger.getLogger(TestTimestamp.class);

    public TestTimestamp() {
    }

    private static void printZoneInfo() {
        String zone[] = TimeZone.getAvailableIDs(1 * 60 * 60 * 1000);
        for (String aZone : zone) {
            System.out.println("zone info " + aZone);
        }
    }

    public static void main(String args[])
        throws Exception {
        Timestamp t = new Timestamp(System.currentTimeMillis());

        SimpleDateFormat df = new SimpleDateFormat("dd.MMMMM.yyyy HH:mm:ss", Locale.ENGLISH);
        SimpleTimeZone pdt = new SimpleTimeZone(8 * 60 * 60 * 1000, "Asia/Irkutsk");

        int springDTS = 1;
        int fallDTS = 1;
        pdt.setStartRule(Calendar.MARCH, springDTS, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        pdt.setEndRule(Calendar.OCTOBER, fallDTS, Calendar.SUNDAY, 2 * 60 * 60 * 1000);

        df.setTimeZone(pdt);

        String s = df.format(t);
        System.out.println(s);

        org.riverock.common.startup.StartupApplication.init();

        s = DateFormatUtils.format(t, "dd.MMMMM.yyyy HH:mm:ss.SSS", TimeZone.getDefault(), Locale.ENGLISH);
        System.out.println(s);

        t = DateTools.getCurrentTime();
        System.out.println("#1.1 " + t);

        SimpleDateFormat df1 = new SimpleDateFormat("dd.MMMMM.yyyy HH:mm:ss.SSS", Locale.ENGLISH);
        s = df1.format(t);
        System.out.println("#1.2 " + s);
        System.out.println("#1.3 " + t);

//        System.out.println( "Version "+System.getProperty("java.version") );

/*
        long m = DateTools.getCalendarWithMask(new GregorianCalendar(CurrentTimeZone.getTZ()), "dd.MM.yyyy HH:mm:ss.SSS").getTimeInMillis();
        t =  new Timestamp( m );
        SimpleDateFormat df2 = new SimpleDateFormat("dd.MMMMM.yyyy HH:mm:ss.SSS", Locale.ENGLISH);
        s = df2.format( t );
        System.out.println( s );
*/

        s = DateFormatUtils.format(new Timestamp(System.currentTimeMillis()), "dd.MMMMM.yyyy HH:mm:ss.SSS", TimeZone.getDefault(), Locale.ENGLISH);
        System.out.println(s);

        System.out.println("current time " + DateFormatUtils.format(System.currentTimeMillis(), "dd.MMMMM.yyyy HH:mm:ss.SSS", TimeZone.getDefault(), Locale.ENGLISH));

        printZoneInfo();
    }
}
