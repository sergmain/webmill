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
 */
package org.riverock.generic.test;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.annotation.schema.db.DbSchema;
import org.riverock.generic.annotation.schema.db.DbTable;
import org.riverock.generic.annotation.schema.db.DbPrimaryKey;

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
        org.riverock.common.startup.StartupApplication.init();
        DatabaseAdapter db_ = DatabaseAdapter.getInstance( "MSSQL-JTDS" );
        DbSchema schema = DatabaseManager.getDbStructure(db_ );
        DatabaseAdapter dbOra = DatabaseAdapter.getInstance( "ORACLE" );
        DbSchema schemaOracle = DatabaseManager.getDbStructure(dbOra );

        DbTable sourceTableOracle =
            DatabaseManager.getTableFromStructure( schemaOracle, "WM_PRICE_SHOP_LIST");

        DbTable checkTable = DatabaseManager.getTableFromStructure( schema, "WM_PRICE_SHOP_LIST");
        checkTable.setData( null );

//        DbService.duplicateTable(db_, sourceTable, sourceTable.getName()+"_TEMP");

        DbPrimaryKey pk = sourceTableOracle.getPrimaryKey();
        if (pk==null)
            System.out.println("PK is null");

        System.out.println("add primary key '"+pk.getColumns().get(0).getPkName()+"'");

        DatabaseManager.addPrimaryKey(db_, checkTable, sourceTableOracle.getPrimaryKey());

//        byte[] sourceByte = XmlTools.getXml( sourceTable, null );
//        byte[] targetByte = XmlTools.getXml( targetTable, null );

    }
}
