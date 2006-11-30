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

import java.sql.*;
import java.util.Locale;
import java.util.TimeZone;
import java.text.ParseException;


import org.apache.log4j.Logger;

import org.riverock.common.utils.DateUtils;
import org.riverock.common.tools.DateTools;



@SuppressWarnings({"deprecation"})
public class TestDatabaseTimestamp
{
    private static Logger cat = Logger.getLogger( "org.riverock.portlet.test.TestTimestamp" );

    public TestDatabaseTimestamp()
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
        org.riverock.common.startup.StartupApplication.init();
    }

    private static void process(Connection conn) throws SQLException, ParseException {
        Timestamp t = new Timestamp( System.currentTimeMillis() );

        System.out.println("#1 insert");
        String stringDate = DateUtils.getStringDate(t, "dd.MM.yyyy HH:mm:ss", Locale.ENGLISH, TimeZone.getDefault());
        System.out.println( stringDate );
        PreparedStatement ps = conn.prepareStatement(
            "insert into A_TIMESTAMP(d) values( (select to_date(?, 'dd.mm.yyyy hh24:mi:ss') from dual))"
        );
        ps.setString(1, stringDate);

        ps.executeUpdate();
        conn.commit();

        t = new Timestamp(
            DateTools.getDateWithMask("2003-04-09 15:28:12.0", "yyyy-MM-dd HH:mm:ss.SSS").getTime()
        );
        System.out.println("#2 insert");
        ps = conn.prepareStatement(
            "insert into A_TIMESTAMP values(?)"
        );
//        ps.setTimestamp( 1, t);
        Time time = new Time( t.getTime() );
        Date date = new Date( t.getDate() );
        long mills = t.getTime();
        mills = mills%(3600*24*1000);
        ps.setDate(1, date);
//        RsetTools.setLong(ps, 2, mills);
        ps.setLong(2, 0);

        ps.executeUpdate();
        conn.commit();

        conn.close();
        conn = null;
    }
}
