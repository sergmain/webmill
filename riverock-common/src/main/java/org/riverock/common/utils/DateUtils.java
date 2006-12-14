/*
 * org.riverock.generic - Database connectivity classes, part of Webmill portal
 * For more information about Webmill portal, please visit project site
 * http://webmill.askmore.info
 *
 * Copyright (C) 2000-2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
 */
package org.riverock.common.utils;

import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @deprecated you must specify concrete timeZone
 * $Id$
 *
 */
public class DateUtils {

    /**
     * @deprecated  use org.apache.commons.lang.time.DateFormatUtils.format(date, mask, tz, loc);
     * @param date
     * @param mask
     * @param loc
     * @return String
     */
    public static String getStringDate(java.util.Date date, String mask, TimeZone tz, Locale loc) {
        return DateFormatUtils.format(date, mask, tz, loc);
    }

    /**
     * @deprecated  use org.apache.commons.lang.time.DateFormatUtils.format(System.currentTimeMillis(), mask, tz, loc);
     * @param mask
     * @param tz
     * @param loc
     * @return
     */
    public static String getCurrentDate(String mask, TimeZone tz, Locale loc) {
        return DateFormatUtils.format(System.currentTimeMillis(), mask, tz, loc);
    }

    /**
     * @deprecated use org.apache.commons.lang.time.DateFormatUtils.format(System.currentTimeMillis(), mask, tz, Locale.ENGLISH);
     * @param mask
     * @param tz
     * @return
     */
    public static String getCurrentDate(String mask, TimeZone tz) {
        return DateFormatUtils.format(System.currentTimeMillis(), mask, tz, Locale.ENGLISH);
    }

}