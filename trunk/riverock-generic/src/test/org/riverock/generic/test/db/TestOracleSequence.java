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

 * Date: Feb 22, 2003

 * Time: 7:28:46 PM

 *

 * $Id$

 */

package org.riverock.generic.test.db;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.schema.db.structure.DbSchemaType;

import org.riverock.generic.schema.db.structure.DbTableType;

import org.riverock.generic.config.GenericConfig;

import org.riverock.generic.tools.XmlTools;



import java.sql.DatabaseMetaData;

import java.sql.ResultSet;



public class TestOracleSequence

{

    private static void doTest( String nameConnection )

        throws Exception

    {

        DatabaseAdapter db_ = DatabaseAdapter.getInstance(true, nameConnection);



        System.out.println(nameConnection + " Menu "+db_.getConnection().getCatalog());



        DatabaseMetaData db = db_.getConnection().getMetaData();



        System.out.println(nameConnection + " Schema term "+db.getSchemaTerm());

        System.out.println(nameConnection + " UserName term "+db.getUserName());

    }



    public static void main(String[] s)

        throws Exception

    {

        org.riverock.generic.startup.StartupApplication.init();



            String[] types = {""};



        ResultSet meta = null;

        DbSchemaType schema = new DbSchemaType();

        DatabaseAdapter db_ = null;

        String schemaPattern = "%";

//        String schemaPattern = "MILLENNIUM";

        String tablePattern = "%";

        try

        {

            db_ = DatabaseAdapter.getInstance(true, "ORACLE");



            DatabaseMetaData db = db_.getConnection().getMetaData();



            meta = db.getTables(

                null,

                schemaPattern,

                tablePattern,

                null //types

            );



            while (meta.next())

            {



                DbTableType table = new DbTableType();



                table.setSchema(meta.getString("TABLE_SCHEM"));

                table.setName(meta.getString("TABLE_NAME"));

                table.setType(meta.getString("TABLE_TYPE"));

                table.setRemark(meta.getString("REMARKS"));



                schema.addTables(table);

            }

        }

        finally

        {

        }

        XmlTools.writeToFile(schema, GenericConfig.getGenericDebugDir()+"list-db-object.xml");

    }



}

