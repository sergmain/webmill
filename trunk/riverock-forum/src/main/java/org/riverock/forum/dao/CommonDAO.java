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
package org.riverock.forum.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import org.riverock.common.tools.RsetTools;
import org.riverock.forum.bean.ForumSmallBean;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;

@SuppressWarnings({"UnusedAssignment"})
public final class CommonDAO {

    public static long getForumID(DatabaseAdapter adapter) throws SQLException {
        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName( "SEQ_WM_FORUM" );
        seq.setTableName( "WM_FORUM_CATEGORY" );
        seq.setColumnName( "FORUM_CATEGORY_ID" );
        return adapter.getSequenceNextValue( seq );
    }

    public static long getForumCategoryID(DatabaseAdapter adapter) throws SQLException {
        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName( "SEQ_WM_FORUM_CATEGORY" );
        seq.setTableName( "WM_FORUM_CATEGORY" );
        seq.setColumnName( "FORUM_CATEGORY_ID" );
        return adapter.getSequenceNextValue( seq );
    }

    public static long getForumConcreteID(DatabaseAdapter adapter) throws SQLException {
        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName( "SEQ_WM_FORUM_CONCRETE" );
        seq.setTableName( "WM_FORUM_CONCRETE" );
        seq.setColumnName( "F_ID" );
        return adapter.getSequenceNextValue( seq );
    }

    public static long getTopicID(DatabaseAdapter adapter) throws SQLException {
        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName( "SEQ_WM_FORUM_TOPIC" );
        seq.setTableName( "WM_FORUM_TOPIC" );
        seq.setColumnName( "T_ID" );
        return adapter.getSequenceNextValue( seq );
    }

    public static long getMessageID(DatabaseAdapter adapter) throws SQLException {
        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName( "SEQ_WM_FORUM_MESSAGE" );
        seq.setTableName( "WM_FORUM_MESSAGE" );
        seq.setColumnName( "M_ID" );
        return adapter.getSequenceNextValue( seq );
    }

    public static List getForumsSmall(DatabaseAdapter adapter, Long forumId) throws Exception {

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ForumSmallBean> forums = new LinkedList<ForumSmallBean>();
        try {
            ps = adapter.prepareStatement(
                "select a.F_ID, a.F_NAME " +
                "from   WM_FORUM_CONCRETE a, WM_FORUM_CATEGORY b " +
                "where  a.FORUM_CATEGORY_ID=b.FORUM_CATEGORY_ID and b.FORUM_ID=? " +
                "order by F_ORDER ASC"
            );
            ps.setInt(1, forumId.intValue());
            rs = ps.executeQuery();
            while (rs.next()) {
                ForumSmallBean bean = new ForumSmallBean();
                bean.setForumName( rs.getString("f_name") );
                bean.setForumId( rs.getLong("f_id") );
                forums.add( bean );
            }
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
        return forums;
    }

    public static void deleteForumConcrete(DatabaseAdapter adapter, Long forumConcreteId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = adapter.prepareStatement(
                "select a.T_ID from WM_FORUM_TOPIC a where T_F_ID=?"
            );
            ps.setLong(1, forumConcreteId);
            rs = ps.executeQuery();

            while (rs.next()) {
                DatabaseManager.runSQL(
                    adapter,
                    "delete from WM_FORUM_MESSAGE where M_T_ID=? ",
                    new Object[] { RsetTools.getLong(rs, "T_ID") },
                    new int[] { Types.INTEGER }
                );
            }
            DatabaseManager.runSQL(
                adapter,
                "delete from WM_FORUM_TOPIC where T_F_ID=? ",
                new Object[] { forumConcreteId },
                new int[] { Types.INTEGER }
            );
            DatabaseManager.runSQL(
                adapter,
                "delete from WM_FORUM_CONCRETE where F_ID=? ",
                new Object[] { forumConcreteId },
                new int[] { Types.INTEGER }
            );
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }
}