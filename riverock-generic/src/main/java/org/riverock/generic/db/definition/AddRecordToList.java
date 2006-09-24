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
package org.riverock.generic.db.definition;

import java.sql.PreparedStatement;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.db.DefinitionActionDataListType;

/**
 * User: Admin
 * Date: May 20, 2003
 * Time: 9:49:58 PM
 * <p/>
 * $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public class AddRecordToList implements DefinitionProcessingInterface {
    private static Logger log = Logger.getLogger(AddRecordToList.class);

    public AddRecordToList() {
    }

    public void processAction(DatabaseAdapter db_, DefinitionActionDataListType parameters) throws Exception {
        PreparedStatement ps = null;
        try {
            if (log.isDebugEnabled())
                log.debug("db connect - " + db_.getClass().getName());

            String seqName = DefinitionService.getString(parameters, "sequence_name", null);
            if (seqName == null) {
                String errorString = "Name of sequnce not found";
                log.error(errorString);
                throw new Exception(errorString);
            }

            String tableName = DefinitionService.getString(parameters, "name_table", null);
            if (tableName == null) {
                String errorString = "Name of table not found";
                log.error(errorString);
                throw new Exception(errorString);
            }

            String columnName = DefinitionService.getString(parameters, "name_pk_field", null);
            if (columnName == null) {
                String errorString = "Name of column not found";
                log.error(errorString);
                throw new Exception(errorString);
            }

            CustomSequenceType seqSite = new CustomSequenceType();
            seqSite.setSequenceName(seqName);
            seqSite.setTableName(tableName);
            seqSite.setColumnName(columnName);
            long seqValue = db_.getSequenceNextValue(seqSite);

            String valueColumnName = DefinitionService.getString(parameters, "name_value_field", null);
            if (valueColumnName == null) {
                String errorString = "Name of valueColumnName not found";
                log.error(errorString);
                throw new Exception(errorString);
            }

            String insertValue = DefinitionService.getString(parameters, "insert_value", null);
            if (insertValue == null) {
                String errorString = "Name of insertValue not found";
                log.error(errorString);
                throw new Exception(errorString);
            }

            String sql =
                "insert into " + tableName + " " +
                    "(" + columnName + "," + valueColumnName + ")" +
                    "values" +
                    "(?,?)";

            if (log.isDebugEnabled()) {
                log.debug(sql);
                log.debug("pk " + seqValue);
                log.debug("value " + insertValue);
            }

            ps = db_.prepareStatement(sql);
            ps.setLong(1, seqValue);
            ps.setString(2, insertValue);

            ps.executeUpdate();

            db_.commit();
        }
        catch (Exception e) {
            try {
                if (db_ != null) {
                    db_.rollback();
                }
            }
            catch (Exception e1) {
                // catch rollback error
            }
            log.error("Error insert value", e);
            throw e;
        }
        finally {
            DatabaseManager.close(ps);
            ps = null;
        }
    }
}
