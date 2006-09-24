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