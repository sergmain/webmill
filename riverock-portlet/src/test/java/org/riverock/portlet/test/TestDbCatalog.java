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
