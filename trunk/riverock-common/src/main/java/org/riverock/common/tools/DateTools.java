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

package org.riverock.common.tools;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 *
 * $Id$
 *
 */
public final class DateTools
{

    public static java.sql.Timestamp getCurrentTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static java.util.Date getDateWithMask( final String date, final String mask )
        throws java.text.ParseException {
        if (date == null || mask == null)
            return null;

        SimpleDateFormat dFormat = new SimpleDateFormat(mask);

        return dFormat.parse(date);
    }

    public static String getStringDate( final java.util.Date date, final String mask, final Locale loc, final TimeZone tz) {
        if (date == null) return null;

        SimpleDateFormat df = new SimpleDateFormat(mask, loc);
        df.setTimeZone( tz );

        return df.format( date );
    }

    public static Calendar getCalendarWithMask( final String date, final String mask )
            throws java.text.ParseException
    {
        if (date == null || mask == null)
            return null;

        SimpleDateFormat dFormat = new SimpleDateFormat(mask);
//        String currDate = getStringDate(c_, mask, Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTime(dFormat.parse(date));

        return c;
    }

    public static Calendar getCalendarWithMask( final Calendar c_, final String mask )
            throws java.text.ParseException
    {
        return getCalendarWithMask(c_, mask, Locale.ENGLISH);
    }

    public static Calendar getCalendarWithMask( final Calendar c_, final String mask, final Locale locale )
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

    public static boolean compareDate( final java.sql.Date d1, final java.sql.Date d2 )
    {
        if ((d1 == null) || (d2 == null))
            return false;

        Calendar c1 = new GregorianCalendar();
        c1.setTime(d1);
        Calendar c2 = new GregorianCalendar();
        c2.setTime(d2);

        return compareDate(c1, c2);
    }

    public static boolean compareDate( final Calendar c1, final Calendar c2, final String mask )
            throws java.text.ParseException
    {
        return compareDate(
                getCalendarWithMask(c1, mask),
                getCalendarWithMask(c2, mask)
        );
    }

    public static boolean compareDate( final Calendar c1, final Calendar c2 )
    {
        if ((c1 == null) || (c2 == null))
            return false;

        if (c1.before(c2))
            return false;

        if (c2.before(c1))
            return false;

        return true;
    }

    public static String getStringDate( final Calendar c, final String mask ) {
        return DateFormatUtils.format(c.getTimeInMillis(), mask, c.getTimeZone(), Locale.ENGLISH);
//        return getStringDate(c, mask, Locale.ENGLISH);
    }

    public static String getStringDate( final Calendar c, final String mask, final Locale loc ) {
        if (c == null) return null;
        return DateFormatUtils.format(c.getTimeInMillis(), mask, c.getTimeZone(), loc);
//        SimpleDateFormat df = new SimpleDateFormat(mask, loc);
//        df.setTimeZone(c.getTimeZone());
//        return df.format(c.getTime());
    }

    public static String getCurrentDate( final String mask, final Locale loc, final TimeZone timeZone ) {
        return DateFormatUtils.format(System.currentTimeMillis(), mask, timeZone, loc);
//        return getStringDate(new GregorianCalendar(timeZone), mask, loc);
    }

    public static String getCurrentDate( final String mask, final TimeZone timeZone ) {
        return DateFormatUtils.format(System.currentTimeMillis(), mask, timeZone, Locale.ENGLISH);
//        return getStringDate(new GregorianCalendar(timeZone), mask, Locale.ENGLISH);
    }
}