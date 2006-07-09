/*
 * org.riverock.forum - Forum portlet
 *
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
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
 *
 */
package org.riverock.forum.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.forum.bean.UserBean;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.module.exception.ActionException;

/**
 * UserDAO interface
 */

public class UserDAO {
    private final static Logger log = Logger.getLogger(UserDAO.class);

    public UserBean execute(Integer u_id) throws ActionException {
        if (u_id==null) {
            return null;
        }
        DatabaseAdapter connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DatabaseAdapter.getInstance();
            String sql =
                "SELECT a.u_avatar_id, a.u_sign, a.u_post, a.u_lasttime, a.u_lastip " +
                "       b.DATE_START_WORK, b.ADDRESS, b.EMAIL, " +
                "       b.FIRST_NAME, b.MIDDLE_NAME, b.LAST_NAME, " +
                "FROM   WM_FORUM_USER a, WM_LIST_USER b " +
                "where  a.U_ID=? and a.U_ID=b.ID_USER ";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, u_id.intValue());
            rs = ps.executeQuery();

            UserBean userBean = null;
            if (rs.next()) {
                userBean = new UserBean();
                userBean.setU_id(u_id.intValue());
                userBean.setU_name(
                    StringTools.getUserName( RsetTools.getString(rs, "FIRST_NAME"), RsetTools.getString(rs, "MIDDLE_NAME"), RsetTools.getString(rs, "LAST_NAME") )
                );
                userBean.setU_avatar_id(rs.getInt("u_avatar_id"));
                userBean.setU_password("");
                userBean.setU_email(rs.getString("EMAIL"));
                userBean.setU_regtime(rs.getTimestamp("DATE_START_WORK"));
                userBean.setU_address(rs.getString("ADDRESS"));
                userBean.setU_sign(rs.getString("u_sign"));
                userBean.setU_post(rs.getInt("u_post"));
                userBean.setU_lasttime(rs.getTimestamp("u_lasttime"));
                userBean.setU_lastip(rs.getString("u_lastip"));
            }
            return userBean;

        } catch (Exception ex) {
            String es = "Error execute userDAO";
            log.error(es, ex);
            throw new ActionException(es, ex);
        }
        finally {
            DatabaseManager.close(connection, rs, ps);
            connection = null;
            rs = null;
            ps = null;
        }
    }
}