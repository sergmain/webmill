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

 * Author: mill

 * Date: Nov 28, 2002

 * Time: 3:10:19 PM

 *

 * $Id$

 */

package org.riverock.portlet.test;



import org.apache.log4j.Logger;



import org.riverock.generic.db.DatabaseAdapter;

import org.riverock.generic.db.DatabaseManager;

import org.riverock.generic.schema.db.structure.DbImportedPKColumnType;

import org.riverock.generic.schema.db.structure.DbSchemaType;

import org.riverock.generic.schema.db.structure.DbTableType;

import org.riverock.generic.schema.db.structure.DbImportedKeyListType;



public class TestForeignKeys

{

    private static Logger cat = Logger.getLogger("org.riverock.portlet.test.TestForeignKeys");



    public TestForeignKeys(){}



    public static void main(String args[])

        throws Exception

    {

        org.riverock.generic.startup.StartupApplication.init();



        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "ORACLE");

        DbSchemaType schema = DatabaseManager.getDbStructure(db_ );

        DbTableType testTable = null;



        for (int j=0; j<schema.getTablesCount(); j++)

        {

            DbTableType table = schema.getTables(j);

            if ( "A_TEST_1".equalsIgnoreCase(table.getName()) )

            {

                testTable = table;

/*

                DbImportedPKColumnType[] fkColumnList = DatabaseManager.getFkNames(table);

                System.out.println("key count: "+fkColumnList.length );

                for (int p=0; p<fkColumnList.length; p++)

                    System.out.println("key: "+fkColumnList[p].getFkName() );



                for (int p=0; p<fkColumnList.length; p++)

                {

                    DbImportedPKColumnType fkColumn = fkColumnList[p];



                    String sql =

                        "alter table "+table.getName()+" "+

                        "add CONSTRAINT "+

                        (

                        fkColumn.getFkName()==null || fkColumn.getFkName().length()==0

                        ?table.getName()+p+"_fk"

                        :fkColumn.getFkName()

                        ) +

                        " PRIMARY KEY (";



                    int seq = Integer.MIN_VALUE;

                    boolean isFirst = true;

                    for ( int i=0; i<table.getImportedKeysCount();i++ )

                    {

                        DbImportedPKColumnType currFkCol = table.getImportedKeys(i);

                        if (

                            (currFkCol.getPkSchemaName()==null && fkColumn.getPkSchemaName()!=currFkCol.getPkSchemaName()) ||

                            (currFkCol.getPkSchemaName()!=null && !currFkCol.getPkSchemaName().equals(fkColumn.getPkSchemaName()) ) ||



                            (currFkCol.getPkTableName()==null && fkColumn.getPkTableName()!=currFkCol.getPkTableName()) ||

                            (currFkCol.getPkTableName()!=null && !currFkCol.getPkTableName().equals(fkColumn.getPkTableName()) ) ||



                            (currFkCol.getFkSchemaName()==null && fkColumn.getFkSchemaName()!=currFkCol.getFkSchemaName()) ||

                            (currFkCol.getFkSchemaName()!=null && !currFkCol.getFkSchemaName().equals(fkColumn.getFkSchemaName()) ) ||



                            (currFkCol.getFkTableName()==null && fkColumn.getFkTableName()!=currFkCol.getFkTableName()) ||

                            (currFkCol.getFkTableName()!=null && !currFkCol.getFkTableName().equals(fkColumn.getFkTableName()) )

                        )

                            continue;



                        DbImportedPKColumnType column = null;

                        int seqTemp = Integer.MAX_VALUE;

                        for ( int k=0; k<table.getImportedKeysCount(); k++ )

                        {

                            DbImportedPKColumnType columnTemp = table.getImportedKeys(k);

                            if (

                                (columnTemp.getPkSchemaName()==null && fkColumn.getPkSchemaName()!=columnTemp.getPkSchemaName()) ||

                                (columnTemp.getPkSchemaName()!=null && !columnTemp.getPkSchemaName().equals(fkColumn.getPkSchemaName()) ) ||



                                (columnTemp.getPkTableName()==null && fkColumn.getPkTableName()!=columnTemp.getPkTableName()) ||

                                (columnTemp.getPkTableName()!=null && !columnTemp.getPkTableName().equals(fkColumn.getPkTableName()) ) ||



                                (columnTemp.getFkSchemaName()==null && fkColumn.getFkSchemaName()!=columnTemp.getFkSchemaName()) ||

                                (columnTemp.getFkSchemaName()!=null && !columnTemp.getFkSchemaName().equals(fkColumn.getFkSchemaName()) ) ||



                                (columnTemp.getFkTableName()==null && fkColumn.getFkTableName()!=columnTemp.getFkTableName()) ||

                                (columnTemp.getFkTableName()!=null && !columnTemp.getFkTableName().equals(fkColumn.getFkTableName()) )

                            )

                                continue;

                            if (seq < columnTemp.getKeySeq() && columnTemp.getKeySeq() < seqTemp )

                            {

                                seqTemp = columnTemp.getKeySeq();

                                column = columnTemp;

                            }

                        }

                        seq = column.getKeySeq();



                        if (!isFirst)

                            sql += ",";

                        else

                            isFirst = !isFirst;



                        sql += column.getFkColumnName();

                    }

                    sql += ")\n";

                    System.out.println( sql );

                }

*/

            }

        }



        DbImportedKeyListType fk = new DbImportedKeyListType();

        fk.setKeys( testTable.getImportedKeysAsReference() );

        db_.createForeignKey( fk );



    }

}

