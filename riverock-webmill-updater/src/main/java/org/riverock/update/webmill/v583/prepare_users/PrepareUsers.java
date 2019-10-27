package org.riverock.update.webmill.v583.prepare_users;

import org.riverock.dbrevision.db.Database;
import org.riverock.dbrevision.manager.patch.PatchAction;
import org.riverock.dbrevision.manager.patch.PatchStatus;
import org.riverock.dbrevision.utils.DbUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * User: SergeMaslyukov
 * Date: 17.09.2007
 * Time: 21:38:00
 * $Id$
 */
public class PrepareUsers implements PatchAction {
    
    public PatchStatus process(Database database) throws Exception {
        ResultSet rs = null;
        Statement st = null;

        PatchStatus status = new PatchStatus();
        try {
            st = database.getConnection().createStatement();
            rs = st.executeQuery("select ID_USER from WM_LIST_USER where IS_DELETED=1");
            while (rs.next()) {
                Long userId = DbUtils.getLong(rs, "ID_USER");
                deleteAuthInfo(database, userId);
                deleteUser(database, userId);
            }
            database.getConnection().commit();
        }
        catch (SQLException e) {
            status.setStatus(PatchStatus.Status.ERROR);
            status.getMessages().addMessage("Error process template: " + e.toString());
        }
        finally {
            DbUtils.close(rs, st);
        }
        return status;
    }

    private void deleteAuthInfo(Database database, Long userId) throws Exception {
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            ps = database.getConnection().prepareStatement("select ID_AUTH_USER from WM_AUTH_USER where ID_USER=?");
            ps.setLong(1, userId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Long authUserId = DbUtils.getLong(rs, "ID_AUTH_USER");
                deleteAuthRelateRoles(database, authUserId);
                deleteAuthUser(database, authUserId);
            }
            database.getConnection().commit();
        }
        finally {
            DbUtils.close(rs, ps);
        }
    }

    private void deleteAuthRelateRoles(Database database, Long authUserId) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = database.getConnection().prepareStatement("delete from WM_AUTH_RELATE_ACCGROUP where ID_AUTH_USER=?");
            ps.setLong(1, authUserId);
            ps.executeUpdate();
        }
        finally {
            DbUtils.close(ps);
        }
    }

    private void deleteAuthUser(Database database, Long authUserId) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = database.getConnection().prepareStatement("delete from WM_AUTH_USER where ID_AUTH_USER=?");
            ps.setLong(1, authUserId);
            ps.executeUpdate();
        }
        finally {
            DbUtils.close(ps);
        }
    }

    private void deleteUser(Database database, Long userId) throws SQLException {
        PreparedStatement ps = null;
        try {
            ps = database.getConnection().prepareStatement("delete from WM_LIST_USER where ID_USER=?");
            ps.setLong(1, userId);
            ps.executeUpdate();
        }
        finally {
            DbUtils.close(ps);
        }
    }
}
