/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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

/**
 * User: Admin
 * Date: Dec 14, 2002
 * Time: 2:22:59 PM
 *
 * $Id$
 */
package org.riverock.portlet.test.cases;

import java.sql.DatabaseMetaData;
import java.sql.Types;
import java.util.Enumeration;
import java.util.Vector;

import junit.framework.TestCase;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.portlet.member.MemberFile;
import org.riverock.portlet.member.ModuleManager;
import org.riverock.generic.schema.db.structure.DbFieldType;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.schema.db.structure.DbViewType;
import org.riverock.generic.schema.db.structure.DbPrimaryKeyType;
import org.riverock.generic.schema.db.structure.DbPrimaryKeyColumnType;
import org.riverock.generic.schema.db.structure.DbImportedPKColumnType;
import org.riverock.portlet.schema.member.ClassQueryType;
import org.riverock.portlet.schema.member.ContentType;
import org.riverock.portlet.schema.member.FieldsType;
import org.riverock.portlet.schema.member.ModuleType;
import org.riverock.portlet.schema.member.QueryAreaType;
import org.riverock.portlet.schema.member.RestrictType;
import org.riverock.portlet.schema.member.TableType;
import org.riverock.portlet.schema.member.TargetModuleType;
import org.riverock.portlet.schema.member.RelateClassType;
import org.riverock.portlet.schema.member.types.ContentTypeActionType;
import org.riverock.portlet.schema.member.types.FieldsTypeDbTypeType;
import org.riverock.portlet.schema.member.types.FieldsTypeJspTypeType;
import org.riverock.portlet.schema.member.types.RestrictTypeTypeType;
import org.riverock.portlet.schema.member.types.TargetModuleTypeActionType;

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
            DatabaseAdapter db_ = DatabaseAdapter.getInstance( "ORACLE");

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