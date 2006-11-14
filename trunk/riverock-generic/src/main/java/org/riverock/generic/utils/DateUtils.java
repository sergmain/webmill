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
package org.riverock.generic.utils;

import java.util.Locale;

import org.apache.commons.lang.time.DateFormatUtils;

import org.riverock.generic.tools.CurrentTimeZone;

/**
 *
 * $Id$
 *
 */
public class DateUtils {
    /**
     * @param date
     * @param mask
     * @param loc
     * @return String
     */
    public static String getStringDate(java.util.Date date, String mask, Locale loc) {
        return DateFormatUtils.format(date, mask, CurrentTimeZone.getTZ(), loc);
    }

    public static String getCurrentDate(String mask, Locale loc) {
        return DateFormatUtils.format(System.currentTimeMillis(), mask, CurrentTimeZone.getTZ(), loc);
    }

    public static String getCurrentDate(String mask) {
        return DateFormatUtils.format(System.currentTimeMillis(), mask, CurrentTimeZone.getTZ(), Locale.ENGLISH);
    }
}