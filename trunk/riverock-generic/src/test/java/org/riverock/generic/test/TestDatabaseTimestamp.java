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

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.tools.CurrentTimeZone;
import org.riverock.common.tools.DateTools;
import org.apache.log4j.Logger;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Author: mill
 * Date: Mar 31, 2003
 * Time: 10:41:57 AM
 *
 * $Id$
 */
public class TestDatabaseTimestamp
{
//    private static Logger cat = Logger.getLogger( "org.riverock.test.TimestampTest" );

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
        Timestamp t = new Timestamp( System.currentTimeMillis() );

        org.riverock.generic.startup.StartupApplication.init();

        DatabaseAdapter db_ = DatabaseAdapter.getInstance( "ORACLE_PORT" );

        System.out.println("#1 insert");
        String stringDate = DateTools.getStringDate(t, "dd.MM.yyyy HH:mm:ss", Locale.ENGLISH, CurrentTimeZone.getTZ());
//            DateUtils.getStringDate(t, "dd.MM.yyyy HH:mm:ss", Locale.ENGLISH);

        System.out.println( stringDate );
        PreparedStatement ps = db_.prepareStatement(
            "insert into A_TIMESTAMP(d) values( (select to_date(?, 'dd.mm.yyyy hh24:mi:ss') from dual))"
        );
        ps.setString(1, stringDate);

        ps.executeUpdate();
        db_.commit();

        t = new Timestamp(
            DateTools.getDateWithMask("2003-04-09 15:28:12.0", "yyyy-MM-dd HH:mm:ss.SSS").getTime()
        );
        System.out.println("#2 insert");
        ps = db_.prepareStatement(
            "insert into A_TIMESTAMP values(?)"
        );
//        ps.setTimestamp( 1, t);
        Time time = new Time( t.getTime() );
        Date date = new Date( t.getDate() );
        long mills = t.getTime();
        mills = mills%(3600*24*1000);
        ps.setDate(1, date);
//        ps.setLong(2, mills);
        ps.setLong(2, 0);

        ps.executeUpdate();
        db_.commit();


        DatabaseAdapter.close( db_ );
        db_ = null;


    }
}
