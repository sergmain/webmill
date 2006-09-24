/*
 * org.riverock.portlet - Portlet Library
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

/**
 * User: Admin
 * Date: Feb 22, 2003
 * Time: 7:28:46 PM
 *
 * $Id$
 */
package org.riverock.portlet.test;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.common.tools.RsetTools;
import oracle.jdbc.driver.OracleConnection;

public class TestDbCatalog
{
    private static void doTest( String nameConnection )
        throws Exception
    {
        DatabaseAdapter db_ = DatabaseAdapter.getInstance( nameConnection );

        System.out.println(nameConnection + " Menu "+db_.getConnection().getCatalog());

        DatabaseMetaData db = db_.getConnection().getMetaData();

        System.out.println(nameConnection + " Schema term "+db.getSchemaTerm());
        System.out.println(nameConnection + " UserName term "+db.getUserName());

/*
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement( "select B from A_TEST");
            rs = ps.executeQuery();
            if ( rs.next() )
            {
                Object obj = rs.getObject( "B" );
                String s = ((OracleConnection)db_.conn).getSQLType(obj);
                Object jdbcObj = ((oracle.sql.Datum)obj).toJdbc();
                System.out.println("str - "+s);
                System.out.println("obj - "+obj);
                System.out.println("jdbc obj - "+jdbcObj);
            }
            else
                System.out.println("Record not found");
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
*/
    }

    public static void main(String[] s)
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
        doTest("ORACLE");
//        doTest("HSQLDB");
//        doTest("MSSQL-JTDS");
//        doTest("MYSQL");


    }

}
