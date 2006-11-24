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
import java.sql.Types;
import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.forum.bean.User;
import org.riverock.forum.bean.UserListBean;
import org.riverock.module.exception.ActionException;

/**
 * UserListDAO interface
 */
public class UserListDAO {
    private final static Logger log = Logger.getLogger(UserListDAO.class);

    public UserListBean execute(int start, int range, String keyword) throws ActionException {
        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            UserListBean userListBean = new UserListBean();
            userListBean.setKeyword(keyword);
            adapter = DatabaseAdapter.getInstance();

            final String searchKeyword = "'%" + keyword + "%'";
            Long countRecord = DatabaseManager.getLongValue(adapter,
                "SELECT count(*) count_records " +
                "FROM   WM_FORUM_USER " +
                "WHERE  u_name LIKE ? " +
                "ORDER BY u_post DESC",
                new Object[]{searchKeyword},
                new int[] {Types.VARCHAR}
            );

            String sql =
                "SELECT u_id,u_name,u_avatar_id,u_email,u_regtime, " +
                "       u_address,u_post,u_lasttime,u_name " +
                "FROM   WM_FORUM_USER " +
                "WHERE  u_name LIKE ? " +
                "ORDER BY u_post DESC";
            ps = adapter.prepareStatement(sql);
            ps.setString(1, searchKeyword);
            rs = ps.executeQuery();

            int count = (countRecord != null ? countRecord.intValue() : 0);

            userListBean.setStart(start);
            userListBean.setRange(range);
            userListBean.setCount(count);
            List<User> userList = new ArrayList<User>();
            if (count > 0) {
                if (start > count) {
                    return null;
                }
                int j = 0;
                boolean isNext = true;
                while (isNext && j++ < start) {
                    isNext = rs.next();
                }

                for (int i = 0; isNext && i < range && !rs.isAfterLast(); i++) {
                    User user = new User();
                    user.setU_id(rs.getInt("u_id"));
                    user.setU_name(rs.getString("u_name"));
                    user.setU_avatar_id(rs.getInt("u_avatar_id"));
                    user.setU_email(rs.getString("u_email"));
                    user.setU_regtime(rs.getTimestamp("u_regtime"));
                    user.setU_address(rs.getString("u_address"));
                    user.setU_post(rs.getInt("u_post"));
                    user.setU_lasttime(rs.getTimestamp("u_lasttime"));
                    setUserRoleDescription( user );
                    userList.add(user);
                    rs.next();
                }
            }
            userListBean.setUsers(userList);
            return userListBean;

        }
        catch (Exception ex) {
            String es = "Error execute userListDAO";
            log.error(es, ex);
            throw new ActionException(es, ex);
        }
        finally {
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }

    private void setUserRoleDescription(User user) {
        user.setR_name( "" );
    }
}