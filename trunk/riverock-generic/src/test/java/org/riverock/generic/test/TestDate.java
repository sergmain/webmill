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
package org.riverock.generic.test;

import org.riverock.common.tools.DateTools;
import org.riverock.generic.tools.CurrentTimeZone;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * User: Admin
 * Date: Dec 12, 2002
 * Time: 11:07:46 AM
 *
 * $Id$
 */
public class TestDate
{
    public static void main(String args[])
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();

        String mask = "dd.MMM.yyyy HH:mm:ss";

        Calendar cal = GregorianCalendar.getInstance();
        String currDate =
            DateTools.getStringDate(
                new GregorianCalendar(CurrentTimeZone.getTZ()), mask, Locale.ENGLISH
            );
//            DateUtils.getCurrentDate( mask );

        System.out.println("current date/time "+currDate);

        currDate = DateTools.getStringDate( cal, mask );
        System.out.println(currDate);

        Date date = cal.getTime();

        Calendar cal1 = new GregorianCalendar();
        cal1.setTime( date );

        System.out.println( DateTools.getStringDate( cal1, mask, new Locale("en", "US") ) );

//        Locale loc = new Locale("en_US");
        Locale loc = new Locale("en", "US","Test");
//        Locale loc = Locale.UK;

        SimpleDateFormat df = new SimpleDateFormat(mask,  loc);
        df.setTimeZone(cal1.getTimeZone());
        System.out.println(df.format(cal1.getTime()));
    }
}
