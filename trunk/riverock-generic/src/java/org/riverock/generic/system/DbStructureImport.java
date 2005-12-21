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
package org.riverock.generic.system;

import java.io.FileInputStream;
import java.sql.SQLException;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.db.DatabaseStructureManager;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.schema.db.structure.DbSequenceType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.schema.db.structure.DbViewType;
import org.riverock.generic.startup.StartupApplication;

import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;


/**
 * Author: mill
 * Date: Nov 28, 2002
 * Time: 3:10:19 PM
 *
 * $Id$
 *
 * import data from XML file to DB
 */
public class DbStructureImport
{
    public static void main(String args[]) throws Exception
    {
        if (args.length<3) {
            System.out.println( "Command line format: <DB_ALIAS> <IMPORT_FILE> <REPORT_FILE>" );
            return;
        }
        StartupApplication.init();
        final String dbAlias = args[0];
        final String fileName = args[1];
        DbStructureImport.importStructure(fileName, true, dbAlias);
    }

    public static void importStructure(String fileName, boolean isData, String dbAlias )
        throws Exception
    {
        DatabaseAdapter db_ = null;
        try
        {
            db_ = DatabaseAdapter.getInstance( dbAlias );
            System.out.println("db connect - "+db_.getClass().getName());

            int i = 0;

            System.out.println("Unmarshal data from file "+fileName);
            InputSource inSrc = new InputSource(new FileInputStream( fileName ));
            DbSchemaType millSchema = (DbSchemaType) Unmarshaller.unmarshal(DbSchemaType.class, inSrc);

            for (i=0; i<millSchema.getTablesCount(); i++)
            {
                DbTableType table = millSchema.getTables(i);
                if (table.getName().toLowerCase().startsWith("tb_") )
                    continue;

                if (!DatabaseManager.isSkipTable(table.getName()))
                {
                    try
                    {
                        System.out.println("create table " + table.getName());
                        db_.createTable(table);
                    }
                    catch (SQLException e)
                    {
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
                    if (isData)
                        DatabaseStructureManager.setDataTable(db_, table);
                }
                else
                    System.out.println("skip table " + table.getName());

            }

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
                    }
                }
            }
            DatabaseManager.createWithReplaceAllView(db_, millSchema);

            for (i=0; i < millSchema.getSequencesCount(); i++)
            {
                DbSequenceType seq = millSchema.getSequences(i);
                try
                {
                    System.out.println("create sequence " + seq.getName());
                    db_.createSequence( seq );
                }
                catch (Exception e)
                {
                    if (db_.testExceptionSequenceExists(e))
                    {
                        System.out.println("sequence " + seq.getName() + " already exists");
                        System.out.println("drop sequence " + seq.getName());
                        db_.dropSequence(seq.getName());
                        System.out.println("create sequence " + seq.getName());
                        try
                        {
                            db_.createSequence(seq);
                        }
                        catch(Exception e1)
                        {
                            System.out.println("Error create sequence - "+e1.toString());
                        }
                    }
                    else
                    {
                        System.out.println("Error create sequence - "+e.toString());
                    }
                }
            }
        }
        finally
        {
            if (db_!=null) {
                db_.commit();
            }
            DatabaseAdapter.close(db_);
            db_ = null;
        }
    }
}
