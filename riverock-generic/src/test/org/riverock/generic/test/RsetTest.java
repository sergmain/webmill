/*

 * org.riverock.generic -- Database connectivity classes

 * 

 * Copyright (C) 2004, Riverock Software, All Rights Reserved.

 * 

 * Riverock -- The Open-source Java Development Community

 * http://www.riverock.org

 * 

 * 

 * This library is free software; you can redistribute it and/or

 * modify it under the terms of the GNU Lesser General Public

 * License as published by the Free Software Foundation; either

 * version 2.1 of the License, or (at your option) any later version.

 *

 * This library is distributed in the hope that it will be useful,

 * but WITHOUT ANY WARRANTY; without even the implied warranty of

 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU

 * Lesser General Public License for more details.

 *

 * You should have received a copy of the GNU Lesser General Public

 * License along with this library; if not, write to the Free Software

 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 *

 */



/**

 * User: Admin

 * Date: Feb 15, 2003

 * Time: 5:18:08 PM

 *

 * $Id$

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



        DatabaseAdapter db_ = DatabaseAdapter.getInstance( false, nameConnect );

        String sql_ =

            "select a.*, c.CUSTOM_LANGUAGE " +

            "from MAIN_NEWS a, MAIN_LIST_NEWS b, SITE_SUPPORT_LANGUAGE c "+

            "where a.ID=? and a.IS_DELETED=0 and a.id_news=b.id_news and " +

            "b.ID_SITE_SUPPORT_LANGUAGE=c.ID_SITE_SUPPORT_LANGUAGE";



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

            DatabaseManager.close(rs, ps);

            rs = null;

            ps = null;

        }

    }

}

