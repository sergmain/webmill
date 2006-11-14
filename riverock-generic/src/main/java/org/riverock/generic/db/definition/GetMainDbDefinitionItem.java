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
package org.riverock.generic.db.definition;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.exception.GenericException;
import org.riverock.generic.main.CacheFactoryWithDb;
import org.riverock.sql.cache.SqlStatement;

@SuppressWarnings({"UnusedAssignment"})
public class GetMainDbDefinitionItem {
    private static CacheFactoryWithDb cache = new CacheFactoryWithDb(GetMainDbDefinitionItem.class);

    public MainDbDefinitionItemType item = new MainDbDefinitionItemType();

    public boolean isFound = false;

    public GetMainDbDefinitionItem() {
    }

    public static GetMainDbDefinitionItem getInstance(DatabaseAdapter db__, Long id__)
        throws GenericException {
        return (GetMainDbDefinitionItem) cache.getInstanceNew(db__, id__);
    }

    public MainDbDefinitionItemType getData(DatabaseAdapter db_, long id)
        throws Exception {
        GetMainDbDefinitionItem obj = GetMainDbDefinitionItem.getInstance(db_, id);
        if (obj != null)
            return obj.item;

        return new MainDbDefinitionItemType();
    }

    public void copyItem(MainDbDefinitionItemType target) {
        copyItem(this.item, target);
    }

    public static void copyItem(MainDbDefinitionItemType source, MainDbDefinitionItemType target) {
        if (source == null || target == null)
            return;

        target.setIdDbDefinition(
            source.getIdDbDefinition()
        );
        target.setNameDefinition(
            source.getNameDefinition()
        );
        target.setAplayDate(
            source.getAplayDate()
        );
    }

    private static String sql_ = null;

    static {
        sql_ =
            "select * from WM_DB_DEFINITION where ID_DB_DEFINITION=?";


        try {
            SqlStatement.registerSql(sql_, GetMainDbDefinitionItem.class);
        }
        catch (Exception e) {
            // catch register exception
        }
    }

    public GetMainDbDefinitionItem(DatabaseAdapter db_, Long id)
        throws Exception {
        this(db_, id, sql_);
    }

    public GetMainDbDefinitionItem(DatabaseAdapter db_, Long id, String sqlString)
        throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = db_.prepareStatement(sqlString);
            ps.setLong(1, id);

            rs = ps.executeQuery();
            if (rs.next()) {
                isFound = true;

                long tempLong0 = rs.getLong("ID_DB_DEFINITION");
                if (!rs.wasNull())
                    item.setIdDbDefinition(tempLong0);
                String tempString1 = rs.getString("NAME_DEFINITION");
                if (!rs.wasNull())
                    item.setNameDefinition(tempString1);
                java.sql.Timestamp tempTimestamp2 = rs.getTimestamp("APLAY_DATE");
                if (!rs.wasNull())
                    item.setAplayDate(tempTimestamp2);
            }
        }
        catch (Exception e) {
            throw e;
        }
        catch (Error err) {
            throw err;
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }

    }
}
