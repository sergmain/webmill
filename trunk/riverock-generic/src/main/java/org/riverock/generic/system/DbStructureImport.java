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

import org.apache.log4j.Logger;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.db.DatabaseStructureManager;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.schema.db.structure.DbSequenceType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.schema.db.structure.DbViewType;
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
@SuppressWarnings({"UnusedAssignment"})
public class DbStructureImport {
    private static Logger log = Logger.getLogger(DbStructureImport.class);

    public static void importStructure(String fileName, boolean isData, String dbAlias) throws Exception {
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance(dbAlias);
            importStructure(fileName, isData, db_);
        }
        finally {
            if (db_ != null) {
                db_.commit();
            }
            DatabaseAdapter.close(db_);
            db_ = null;
        }
    }

    public static void importStructure(String fileName, boolean isData, DatabaseAdapter db_) throws Exception {
            int i = 0;
            log.debug("Unmarshal data from file " + fileName);
            InputSource inSrc = new InputSource(new FileInputStream(fileName));
            DbSchemaType millSchema = (DbSchemaType) Unmarshaller.unmarshal(DbSchemaType.class, inSrc);

            for (i = 0; i < millSchema.getTablesCount(); i++) {
                DbTableType table = millSchema.getTables(i);
                if (table.getName().toLowerCase().startsWith("tb_"))
                    continue;

                if (!DatabaseManager.isSkipTable(table.getName())) {
                    try {
                        log.debug("create table " + table.getName());
                        db_.createTable(table);
                    }
                    catch (Exception e) {
                        log.debug("Error create table " + table.getName(), e);
                        throw e;
                    }
                    if (isData) {
                        DatabaseStructureManager.setDataTable(db_, table);
                    }
                }
                else
                    log.debug("skip table " + table.getName());

            }

            for (i = 0; i < millSchema.getViewsCount(); i++) {
                DatabaseManager.createWithReplaceAllView(db_, millSchema);
                DbViewType view = millSchema.getViews(i);
                try {
                    log.debug("create view " + view.getName());
                    db_.createView(view);
                }
                catch (Exception e) {
                    if (db_.testExceptionViewExists(e)) {
                        log.debug("view " + view.getName() + " already exists");
                        log.debug("drop view " + view.getName());
                        DatabaseStructureManager.dropView(db_, view);
                        log.debug("create view " + view.getName());
                        try {
                            db_.createView(view);
                        }
                        catch (Exception e1) {
                            log.error("Error create view - " + e1.toString());
                        }
                    }
                    else {
                        log.debug("Error create view - " + e.toString());
                    }
                }
            }
            DatabaseManager.createWithReplaceAllView(db_, millSchema);

            for (i = 0; i < millSchema.getSequencesCount(); i++) {
                DbSequenceType seq = millSchema.getSequences(i);
                try {
                    log.debug("create sequence " + seq.getName());
                    db_.createSequence(seq);
                }
                catch (Exception e) {
                    log.debug("Error create sequence " + seq.getName(), e);
                    throw e;
                }
            }
    }

    public static void main(String args[]) throws Exception {
        if (args.length < 3) {
            System.out.println("Command line format: <DB_ALIAS> <IMPORT_FILE> <REPORT_FILE>");
            return;
        }
        StartupApplication.init();
        final String dbAlias = args[0];
        final String fileName = args[1];
        DbStructureImport.importStructure(fileName, true, dbAlias);
    }

}
