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
