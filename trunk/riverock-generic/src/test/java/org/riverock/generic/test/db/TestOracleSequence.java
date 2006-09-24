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
package org.riverock.generic.test.db;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.tools.XmlTools;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

/**
 * User: Admin
 * Date: Feb 22, 2003
 * Time: 7:28:46 PM
 *
 * $Id$
 */
public class TestOracleSequence
{
    private static void doTest( String nameConnection )
        throws Exception
    {
        DatabaseAdapter db_ = DatabaseAdapter.getInstance( nameConnection );

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
            db_ = DatabaseAdapter.getInstance("ORACLE");

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
