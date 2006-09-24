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

import junit.framework.TestCase;
import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.schema.db.structure.DbPrimaryKeyType;
import org.riverock.generic.tools.XmlTools;

/**
 * User: Admin
 * Date: Mar 3, 2003
 * Time: 6:43:34 PM
 *
 * $Id$
 */
public class TestDbService
{
    public static void main(String s[])
        throws Exception
    {
        org.riverock.generic.startup.StartupApplication.init();
        DatabaseAdapter db_ = DatabaseAdapter.getInstance( "MSSQL-JTDS" );
        DbSchemaType schema = DatabaseManager.getDbStructure(db_ );
        DatabaseAdapter dbOra = DatabaseAdapter.getInstance( "ORACLE" );
        DbSchemaType schemaOracle = DatabaseManager.getDbStructure(dbOra );

        DbTableType sourceTableOracle =
            DatabaseManager.getTableFromStructure( schemaOracle, "WM_PRICE_SHOP_LIST");

        DbTableType checkTable = DatabaseManager.getTableFromStructure( schema, "WM_PRICE_SHOP_LIST");
        checkTable.setData( null );

//        DbService.duplicateTable(db_, sourceTable, sourceTable.getName()+"_TEMP");

        DbPrimaryKeyType pk = sourceTableOracle.getPrimaryKey();
        if (pk==null)
            System.out.println("PK is null");

        System.out.println("add primary key '"+pk.getColumns(0).getPkName()+"'");

        DatabaseManager.addPrimaryKey(db_, checkTable, sourceTableOracle.getPrimaryKey());

//        byte[] sourceByte = XmlTools.getXml( sourceTable, null );
//        byte[] targetByte = XmlTools.getXml( targetTable, null );

    }
}
