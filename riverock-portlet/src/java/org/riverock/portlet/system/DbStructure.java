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
package org.riverock.portlet.system;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.db.factory.ORAconnect;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.schema.db.structure.DbViewType;
import org.riverock.webmill.config.WebmillConfig;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

public class DbStructure
{
    private static Logger log = Logger.getLogger(DbStructure.class);

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

        DatabaseAdapter dbOra = DatabaseAdapter.getInstance(false, "ORACLE");
//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "IBM-DB2");

//        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "ORACLE_AAA");
        DbSchemaType schema = DatabaseManager.getDbStructure(dbOra );

//        schema.setViews( db_.getViewList( "MILLENNIUM", "%"));
//        schema.setTables( db_.getTableList( "MILLENNIUM", "%"));
//        schema.setTables(db_.getTableList("MILLENNIUM", "AUTH_ARM"));

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

            table.setFields(dbOra.getFieldsList(table.getSchema(), table.getName()));
            table.setPrimaryKey(dbOra.getPrimaryKey(table.getSchema(), table.getName()));
            table.setImportedKeys(dbOra.getImportedKeys(table.getSchema(), table.getName()));
            table.setData(dbOra.getDataTable(table));
        }

        String encoding = "UTF-8";
        String outputSchemaFile = WebmillConfig.getWebmillDebugDir()+"schema.xml";
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
        InputSource inSrc = new InputSource(new FileInputStream( WebmillConfig.getWebmillDebugDir()+"schema-mill.xml" ));
        millSchema = (DbSchemaType) Unmarshaller.unmarshal(DbSchemaType.class, inSrc);

        DatabaseAdapter db_ = DatabaseAdapter.getInstance(false, "ORACLE");

        if (!(db_ instanceof ORAconnect))
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
                    db_.setDataTable(table);
                }
                else
                    System.out.println("skip table " + table.getName());

            }
        }



        if (!(db_ instanceof ORAconnect))
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
                        db_.dropView(view);
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
    }

}
