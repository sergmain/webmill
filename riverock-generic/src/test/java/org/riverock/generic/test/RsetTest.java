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
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.exception.GenericException;
import org.riverock.common.tools.MainTools;
import org.riverock.common.tools.RsetTools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: Admin
 * Date: Feb 15, 2003
 * Time: 5:18:08 PM
 *
 * $Id$
 */
public class RsetTest
{
    public static void main(String s[])
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
        test( "ORACLE" );
//        test( "MSSQL-JTDS" );
//        test( "HSQLDB" );
//        test( "IBM-DB2" );
//        test( "MYSQL" );
    }

    public static void test(String nameConnect)
        throws Exception
    {
        System.out.println( "run test fro '"+nameConnect+"' connection" );

        DatabaseAdapter db_ = DatabaseAdapter.getInstance( nameConnect );
        String sql_ =
            "select a.*, c.CUSTOM_LANGUAGE " +
            "from   WM_NEWS_ITEM a, WM_NEWS_LIST b, WM_PORTAL_SITE_LANGUAGE c "+
            "where  a.ID=? and a.IS_DELETED=0 and a.id_news=b.id_news and " +
            "       b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(sql_);
            RsetTools.setLong(ps, 1, new Long(2) );

            rs = ps.executeQuery();
            if (rs.next())
            {
                byte[] bytes = rs.getBytes("HEADER");

                MainTools.writeToFile( GenericConfig.getGenericDebugDir()+"rset-bytes."+db_.getClass().getName(), bytes );
            }
            else
                System.out.println( "record not found" );

        }
        catch (Exception e)
        {
            throw new GenericException(e.toString());
        }
        finally
        {
            DatabaseManager.close(db_, rs, ps);
            db_ = null;
            rs = null;
            ps = null;
        }
    }
}
