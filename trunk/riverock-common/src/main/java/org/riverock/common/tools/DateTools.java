/*
 * org.riverock.common - Supporting classes and utilities
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
public final class DateTools {

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
    }

    public static String getStringDate( final Calendar c, final String mask, final Locale loc ) {
        if (c == null) return null;
        return DateFormatUtils.format(c.getTimeInMillis(), mask, c.getTimeZone(), loc);
    }

    public static String getCurrentDate( final String mask, final Locale loc, final TimeZone timeZone ) {
        return DateFormatUtils.format(System.currentTimeMillis(), mask, timeZone, loc);
    }

    public static String getCurrentDate( final String mask, final TimeZone timeZone ) {
        return DateFormatUtils.format(System.currentTimeMillis(), mask, timeZone, Locale.ENGLISH);
    }
}