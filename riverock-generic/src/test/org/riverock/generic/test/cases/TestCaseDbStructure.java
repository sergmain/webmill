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

 * Date: Dec 14, 2002

 * Time: 2:22:59 PM

 *

 * $Id$

 */

package org.riverock.generic.test.cases;



import org.riverock.generic.db.DatabaseAdapter;



import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.schema.db.structure.*;

import junit.framework.TestCase;



public class TestCaseDbStructure extends TestCase

{

    private static boolean isUseAssertion = true;





    public TestCaseDbStructure(String testName)

    {

        super(testName);

    }



    private DbSchemaType schemaObj = null;

    private DbSchemaType getDbSchema()

        throws Exception

    {



        if (schemaObj==null)

        {

            DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "ORACLE");



            System.out.println("Get schema of DB");

            schemaObj = DatabaseManager.getDbStructure(db_ );

        }

        return schemaObj;

    }





    // проверяем список полей в QueryArea на наличие в базе данных

    public void testDbStructure()

        throws Exception

    {

        org.riverock.generic.startup.StartupApplication.init();

//

        DbSchemaType schema = getDbSchema();



        int countWrongPk = 0;

        for (int i=0; i<schema.getTablesCount(); i++)

        {

            DbTableType table = schema.getTables(i);

            DbPrimaryKeyType pk = table.getPrimaryKey();

            if (pk!=null)

            {

                for (int k=0; k<pk.getColumnsCount(); k++)

                {

                    DbPrimaryKeyColumnType col = pk.getColumns(k);

                    if (col.getPkName().length()>18)

                    {

                        System.out.println("Name of PK too long, table "+table.getName()+", PK name "+col.getPkName()+", len "+col.getPkName().length());

                        countWrongPk++;

                        break;

                    }

                }

            }

        }

        System.out.println("Count of wrong name PK - "+countWrongPk);

        int countWrongFk = 0;

        int totalFk = 0;

        for (int i=0; i<schema.getTablesCount(); i++)

        {

            DbTableType table = schema.getTables(i);

            for (int k=0; k<table.getImportedKeysCount(); k++)

            {

                totalFk++;

                DbImportedPKColumnType col = table.getImportedKeys(k);

                if (col.getFkName().length()>18)

                {

                    System.out.println("Name of FK too long, table "+table.getName()+", FK name "+col.getFkName()+", len "+col.getFkName().length());

                    countWrongFk++;

                }

            }

        }

        System.out.println("Count of total FK - "+totalFk+", wrong name FK - "+countWrongFk);

    }

}