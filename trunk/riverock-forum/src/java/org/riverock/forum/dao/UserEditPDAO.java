package org.riverock.forum.dao;

import java.sql.Types;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.module.exception.ActionException;

/**
 * UserEditPDAO
 */

public class UserEditPDAO {
    private final static Logger log = Logger.getLogger(UserEditPDAO.class);

    public void execute(int u_id, int u_avatar_id, String u_password,
        String u_email, String u_address, String u_sign, String u_lastip)
        throws ActionException {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            DatabaseManager.runSQL(adapter,
                "update WM_FORUM_USER " +
                "set u_avatar_id=?, u_password=?, u_email=?, u_address=?, u_sign=?, u_lastip=? " +
                "where u_id=?",
                new Object[]{new Integer(u_avatar_id), u_password, u_email, u_address, u_sign, u_lastip},
                new int[]{Types.NUMERIC, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR}
            );

//            String sql =
//                "UPDATE lb_user SET u_avatar_id=" + u_avatar_id
//                + ",u_password='" + u_password + "',u_email='" + u_email
//                + "',u_address='" + u_address + "',u_sign='" + u_sign + "'"
//                + ",u_lastip='" + u_lastip + "'";
//            sql += " WHERE u_id=" + u_id;
        }
        catch (Exception e) {
            String es = "Error execute UserEditPDAO";
            log.error(es, e);
            throw new ActionException(es, e);
        }
        finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }
}