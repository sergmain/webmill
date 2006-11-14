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
package org.riverock.generic.system;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import org.riverock.generic.config.GenericConfig;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.db.DatabaseStructureManager;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.schema.db.structure.DbViewType;

/**
 * Author: mill
 * Date: Nov 28, 2002
 * Time: 3:10:19 PM
 *
 * $Id$
 */
public class DbStructure
{
//    private static Logger cat = Logger.getLogger("org.riverock.system.DbStructure");

    public DbStructure()
    {

    }

    public static void main(String args[])
        throws Exception
    {

        org.riverock.generic.startup.StartupApplication.init();


//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "SAPDB_DBA");

//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "MSSQL-JTDS");
//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "HSQLDB");
//        schema.setViews( db_.getViewList( "SA", "%"));
//        schema.setTables(db_.getTableList("SA", "%"));

        DatabaseAdapter dbOra = DatabaseAdapter.getInstance( "ORACLE");
//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "IBM-DB2");

//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "ORACLE_AAA");
        DbSchemaType schema = DatabaseManager.getDbStructure(dbOra );

//        schema.setViews( db_.getViewList( "MILLENNIUM", "%"));
//        schema.setTables( db_.getTableList( "MILLENNIUM", "%"));
//        schema.setTables(db_.getTableList("MILLENNIUM", "WM_AUTH_APPLICATION"));

        int i = 0;
/*
            DatabaseMetaData db = db_.conn.getMetaData();
            ResultSet rs = db.getCrossReference( null,
                                            null,
                                            "SERV_LIST_LOGIN_SERVICE",
                                            null,
                                            null,
                                            "SERV_LIST_LOGIN_SERVICE");

            while (rs.next())
            {
                System.out.println("new record");
                ResultSetMetaData rsmd = rs.getMetaData();
                int numberOfColumns = rsmd.getColumnCount();

                for (int k=1; k<=numberOfColumns;k++)
                {
                    String name = rsmd.getColumnName(k);
                    String value = rs.getString(1);
                    System.out.println("field name - "+name+" value "+value);
                }
            }
*/

        for (i = 0; i < schema.getTablesCount(); i++)
        {
            DbTableType table = schema.getTables(i);
            System.out.println( "Table - " + table.getName() );

            table.setFields((ArrayList)DatabaseStructureManager.getFieldsList(dbOra.getConnection(), table.getSchema(), table.getName(), dbOra.getFamily()));
            table.setPrimaryKey(DatabaseStructureManager.getPrimaryKey(dbOra.getConnection(), table.getSchema(), table.getName()));
            table.setImportedKeys(DatabaseStructureManager.getImportedKeys(dbOra.getConnection(), table.getSchema(), table.getName()));
            table.setData(DatabaseStructureManager.getDataTable(dbOra.getConnection(), table));
        }

        String encoding = "UTF-8";
        String outputSchemaFile = GenericConfig.getGenericDebugDir()+"schema.xml";
        System.out.println("Marshal data to file");

        FileOutputStream fos = new FileOutputStream( outputSchemaFile );
        Marshaller marsh = new Marshaller(new OutputStreamWriter(fos, encoding));

//        FileWriter w = new FileWriter( outputSchemaFile );
//        Marshaller marsh = new Marshaller(w);

        marsh.setMarshalAsDocument( true );
        marsh.setEncoding( encoding );
        marsh.marshal( schema );

        System.out.println( "Unmarshal current data from file" );
        DbSchemaType millCurrSchema = null; //new DbStructureType();
        InputSource inCurrSrc = new InputSource( new FileInputStream( outputSchemaFile ));
        millCurrSchema = (DbSchemaType) Unmarshaller.unmarshal(DbSchemaType.class, inCurrSrc);

        for (; i < millCurrSchema.getViewsCount(); i++)
        {
            DbViewType view = millCurrSchema.getViews(i);
            System.out.println("View " + view.getName()+" text\n"+view.getText());
        }

        System.out.println("Unmarshal data from file");
        DbSchemaType millSchema = null; //new DbStructureType();
        InputSource inSrc = new InputSource(new FileInputStream( GenericConfig.getGenericDebugDir()+"schema-mill.xml" ));
        millSchema = (DbSchemaType) Unmarshaller.unmarshal(DbSchemaType.class, inSrc);

        DatabaseAdapter db_ = DatabaseAdapter.getInstance( "ORACLE" );

        if (!(db_ instanceof org.riverock.generic.db.factory.ORAconnect))
        {
            for (; i < millSchema.getTablesCount(); i++)
            {
                DbTableType table = millSchema.getTables(i);
                if (!DatabaseManager.isSkipTable(table.getName()))
                {
                    try
                    {
                        System.out.println("create table " + table.getName());
                        db_.createTable(table);
                    }
                    catch (SQLException e)
                    {
                        System.out.println("Error code "+e.getErrorCode());
                        if (db_.testExceptionTableExists(e))
                        {
                            System.out.println("table " + table.getName() + " already exists");
                            System.out.println("drop table " + table.getName());
                            db_.dropTable(table);
                            System.out.println("create table " + table.getName());
                            db_.createTable(table);
                        }
                        else
                            throw e;
                    }
                    DatabaseStructureManager.setDataTable(db_, table);
                }
                else
                    System.out.println("skip table " + table.getName());

            }
        }



        if (!(db_ instanceof org.riverock.generic.db.factory.ORAconnect))
        {
            for (i=0; i < millSchema.getViewsCount(); i++)
            {
                DatabaseManager.createWithReplaceAllView(db_, millSchema);
                DbViewType view = millSchema.getViews(i);
                try
                {
                    System.out.println("create view " + view.getName());
                    db_.createView( view );
                }
                catch (Exception e)
                {
                    if (db_.testExceptionViewExists(e))
                    {
                        System.out.println("view " + view.getName() + " already exists");
                        System.out.println("drop view " + view.getName());
                        DatabaseStructureManager.dropView(db_, view);
                        System.out.println("create view " + view.getName());
                        try
                        {
                            db_.createView(view);
                        }
                        catch(Exception e1)
                        {
                            System.out.println("Error create view - "+e1.toString());
                        }
                    }
                    else
                    {
                        System.out.println("Error create view - "+e.toString());
//                        throw e;
                    }
                }
            }
            DatabaseManager.createWithReplaceAllView(db_, millSchema);
        }
        DatabaseAdapter.close( db_ );
        DatabaseAdapter.close( dbOra );
    }

}
