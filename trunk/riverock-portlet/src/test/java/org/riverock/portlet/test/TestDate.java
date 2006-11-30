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
 * User: Admin
 * Date: Dec 12, 2002
 * Time: 11:07:46 AM
 *
 * $Id$
 */
package org.riverock.portlet.test;

import org.riverock.common.tools.DateTools;
import org.riverock.common.utils.DateUtils;
import org.riverock.common.startup.StartupApplication;

import java.util.*;
import java.text.SimpleDateFormat;

public class TestDate
{
    public static void main(String args[])
        throws Exception
    {
        StartupApplication.init();

        String mask = "dd.MMM.yyyy HH:mm:ss";

        Calendar cal = GregorianCalendar.getInstance();
        String currDate = DateUtils.getCurrentDate( mask, TimeZone.getDefault() );
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
