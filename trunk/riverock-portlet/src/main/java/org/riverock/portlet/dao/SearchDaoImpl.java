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
package org.riverock.portlet.dao;

import java.sql.Timestamp;
import java.sql.Types;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;

/**
 * User: SergeMaslyukov
 * Date: 17.09.2006
 * Time: 23:43:01
 * <p/>
 * $Id$
 */
@SuppressWarnings({"UnusedAssignment"})
public class SearchDaoImpl implements SearchDao {
    /**
     * Store search request in DB
     *
     * @param siteId Long
     * @param searchString String
     */
    public void storeRequest(Long siteId, String searchString) {
        DatabaseAdapter db =null;
        try {
            db = DatabaseAdapter.getInstance();
            DatabaseManager.runSQL(
                db,
                "insert into WM_PORTLET_SEARCH " +
                    "(ID_SITE, SEARCH_DATE, WORD)" +
                    "values" +
                    "(?, ?, ?)",
                new Object[]{siteId, new Timestamp(System.currentTimeMillis()) ,searchString},
                new int[]{Types.DECIMAL, Types.TIMESTAMP, Types.VARCHAR}
            );
            db.commit();
        }
        catch (Exception e) {
            try {
                if (db!=null)
                    db.rollback();
            }
            catch(Throwable th) {
                // catch rollback error
            }
            String es = "Error insert search words";
            throw new RuntimeException( es, e);
       }
       finally {
            DatabaseManager.close(db);
            db=null;
       }
    }
}
