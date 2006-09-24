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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.forum.bean.ForumIntegrityBean;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;

/**
 * @author Sergei Maslyukov
 *         Date: 29.05.2006
 *         Time: 14:17:52
 */
@SuppressWarnings({"UnusedAssignment"})
public class ForumIntegrityDao {
    private final static Logger log = Logger.getLogger(ForumListManagerDAO.class);

    private static final String sql =
        "" +
            "select a.* " +
            "from ( " +
            "select f_u_id user_id " +
            "from   WM_FORUM_concrete " +
            "union  " +
            "select f_u_id2 " +
            "from   WM_FORUM_concrete " +
            ") a " +
            "where a.user_id not in " +
            "(select id_user from wm_list_user)";

    public ForumIntegrityBean getIntegrityStatus(Long siteId) {
        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ForumIntegrityBean bean = new ForumIntegrityBean();
        try {
            adapter = DatabaseAdapter.getInstance();

            ps = adapter.prepareStatement( sql );
            rs = ps.executeQuery();

            List<Long> list = new ArrayList<Long>();
            while(rs.next()) {
                list.add(RsetTools.getLong(rs, "USER_ID"));
            }
            bean.setLostUserId(list);
            if (!list.isEmpty()) {
                bean.setCountLostUser(list.size());
                bean.setValid(false);
            }

            return bean;
        }
        catch (Exception e) {
            String es = "Error execute homeDAO";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
        finally {
            DatabaseManager.close(adapter, rs, ps);
            adapter = null;
            rs = null;
            ps = null;
        }
    }
}
