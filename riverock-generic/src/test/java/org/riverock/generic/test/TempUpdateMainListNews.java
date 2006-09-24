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
import org.riverock.common.tools.RsetTools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: Admin
 * Date: Mar 3, 2003
 * Time: 6:43:34 PM
 *
 * $Id$
 */
public class TempUpdateMainListNews
{
    public static void main(String s[])
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
        DatabaseAdapter db_ = DatabaseAdapter.getInstance( "HSQLDB" );

        String sql_ =
            "select ID_NEWS from WM_NEWS_LIST";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(sql_);

            rs = ps.executeQuery();

            while (rs.next())
            {
                Long id = RsetTools.getLong(rs, "ID_NEWS");


                String sql_temp_ =
                    "update WM_NEWS_LIST "+
                    "set ID_SITE_SUPPORT_LANGUAGE = "+
                    "( select ID_SITE_SUPPORT_LANGUAGE "+
                    "from WM_PORTAL_SITE_LANGUAGE b "+
                    "where ID_SITE = b.ID_SITE and "+
                    "ID_LANGUAGE =b.ID_LANGUAGE "+
                    ") where ID_NEWS=?";

                PreparedStatement ps1 = null;
                try
                {
                    ps1 = db_.prepareStatement(sql_temp_);
                    ps1.setLong(1, id.longValue() );
                    ps1.executeUpdate();
                }
                catch(Exception e)
                {

                }
                finally
                {
                    org.riverock.generic.db.DatabaseManager.close(ps1);
                    ps1 = null;
                }

            }
        }
        finally
        {
            org.riverock.generic.db.DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }

        db_.commit();

        DatabaseAdapter.close( db_ );
        db_ = null;

    }
}
