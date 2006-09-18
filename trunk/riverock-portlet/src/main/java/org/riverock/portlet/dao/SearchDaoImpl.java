/*
 * org.riverock.portlet - Portlet Library
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
 *
 * Riverock - The Open-source Java Development Community
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
