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
import java.sql.SQLException;

import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseStructureManager;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.startup.StartupApplication;

/**
 * Author: mill
 * Date: Nov 28, 2002
 * Time: 3:10:19 PM
 * <p/>
 * $Id$
 * <p/>
 * import data from XML file to DB
 */
public class DbStructureCreateTable {

    /**
     * origin xml DB file:
     * ../riverock-webmill-db/xml/webmill-schema.xml
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        if (args.length < 3) {
            System.out.println("Command line format: <DB_ALIAS> <IMPORT_FILE> <TABLE_NAME>");
            return;
        }
        StartupApplication.init();
        final String dbAlias = args[0];
        final String fileName = args[1];
        final String tableName = args[2];
        DbStructureCreateTable.importStructure(fileName, true, dbAlias, tableName);
    }

    public static void importStructure(String fileName, boolean isData, String dbAlias, String tableName)
        throws Exception {
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance(dbAlias);
            System.out.println("db connect - " + db_.getClass().getName());

            int i;

            System.out.println("Unmarshal data from file " + fileName);
            InputSource inSrc = new InputSource(new FileInputStream(fileName));
            DbSchemaType millSchema = (DbSchemaType) Unmarshaller.unmarshal(DbSchemaType.class, inSrc);

            for (i = 0; i < millSchema.getTablesCount(); i++) {
                DbTableType table = millSchema.getTables(i);
                if (!tableName.equalsIgnoreCase(table.getName())) {
                    System.out.println("skip table: " + table.getName());
                    continue;
                }

                try {
                    System.out.println("create table " + table.getName());
                    db_.createTable(table);
                }
                catch (SQLException e) {
                    if (db_.testExceptionTableExists(e)) {
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

                break;
            }
        }
        finally {
            if (db_ != null) {
                db_.commit();
            }
            DatabaseAdapter.close(db_);
        }
    }
}
