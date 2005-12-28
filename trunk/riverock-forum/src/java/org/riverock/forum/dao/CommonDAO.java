package org.riverock.forum.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.exception.DatabaseException;
import org.riverock.forum.bean.ForumSmallBean;
import org.riverock.common.tools.RsetTools;

public final class CommonDAO {

    public static int getForumID(DatabaseAdapter adapter) throws SQLException {
        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName( "SEQ_WM_FORUM" );
        seq.setTableName( "WM_FORUM_CATEGORY" );
        seq.setColumnName( "FORUM_CATEGORY_ID" );
        return (int)adapter.getSequenceNextValue( seq );
    }

    public static int getForumCategoryID(DatabaseAdapter adapter) throws SQLException {
        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName( "SEQ_WM_FORUM_CATEGORY" );
        seq.setTableName( "WM_FORUM_CATEGORY" );
        seq.setColumnName( "FORUM_CATEGORY_ID" );
        return (int)adapter.getSequenceNextValue( seq );
    }

    public static int getForumConcreteID(DatabaseAdapter adapter) throws SQLException {
        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName( "SEQ_WM_FORUM_CONCRETE" );
        seq.setTableName( "WM_FORUM_CONCRETE" );
        seq.setColumnName( "F_ID" );
        return (int)adapter.getSequenceNextValue( seq );
    }

    public static int getTopicID(DatabaseAdapter adapter) throws SQLException {
        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName( "SEQ_WM_FORUM_TOPIC" );
        seq.setTableName( "WM_FORUM_TOPIC" );
        seq.setColumnName( "T_ID" );
        return (int)adapter.getSequenceNextValue( seq );
    }

    public static int getMessageID(DatabaseAdapter adapter) throws SQLException {
        CustomSequenceType seq = new CustomSequenceType();
        seq.setSequenceName( "SEQ_WM_FORUM_MESSAGE" );
        seq.setTableName( "WM_FORUM_MESSAGE" );
        seq.setColumnName( "M_ID" );
        return (int)adapter.getSequenceNextValue( seq );
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

    public static void deleteForumConcrete(DatabaseAdapter adapter, Integer forumConcreteId) throws SQLException, DatabaseException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = adapter.prepareStatement(
                "select a.T_ID from WM_FORUM_TOPIC a where T_F_ID=?"
            );
            ps.setInt(1, forumConcreteId);
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