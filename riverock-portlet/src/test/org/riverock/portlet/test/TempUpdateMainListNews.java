/*
 * org.riverock.portlet -- Portlet Library
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
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
 *
 */

/**
 * User: Admin
 * Date: Mar 3, 2003
 * Time: 6:43:34 PM
 *
 * $Id$
 */
package org.riverock.portlet.test;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.common.tools.RsetTools;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TempUpdateMainListNews
{
    public static void main(String s[])
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "HSQLDB");

        String sql_ =
            "select ID_NEWS from MAIN_LIST_NEWS";

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(sql_);

            rs = ps.executeQuery();

            while (rs.next())
            {
                Long id = RsetTools.getLong(rs, "ID_NEWS");
                if (id==null)
                    throw new Exception("ID_NEWS can not be null");

                String sql_temp_ =
                    "update MAIN_LIST_NEWS "+
                    "set ID_SITE_SUPPORT_LANGUAGE = "+
                    "( select ID_SITE_SUPPORT_LANGUAGE "+
                    "from SITE_SUPPORT_LANGUAGE b "+
                    "where ID_SITE = b.ID_SITE and "+
                    "ID_LANGUAGE =b.ID_LANGUAGE "+
                    ") where ID_NEWS=?";

                PreparedStatement ps1 = null;
                try
                {
                    ps1 = db_.prepareStatement(sql_temp_);

                    ps1.setLong(1, id.longValue()  );
                    ps1.executeUpdate();
                }
                catch(Exception e)
                {

                }
                finally
                {
                    DatabaseManager.close(ps1);
                    ps1 = null;
                }

            }
        }
        finally
        {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }

        db_.commit();

        DatabaseAdapter.close( db_ );
        db_ = null;

    }
}
