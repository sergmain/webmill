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

import org.riverock.forum.bean.PostBean;
import org.riverock.forum.core.GetWmForumConcreteItem;
import org.riverock.forum.schema.core.WmForumConcreteItemType;
import org.riverock.forum.util.CommonUtils;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.module.exception.ActionException;

/**
 * PostDAO
 */
public class PostDAO {
    private static final Logger log = Logger.getLogger(PostDAO.class);

    public PostBean post(Long forumId, long f_id) throws ActionException {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            if (!CommonUtils.checkForumConcreteId( adapter, forumId, f_id )){
                return null;
            }

            WmForumConcreteItemType item = new GetWmForumConcreteItem(adapter, f_id).item;

            if (item==null) {
                return null;
            }
            PostBean postBean = new PostBean();
            postBean.setF_id(f_id);
            postBean.setF_name( item.getFName() );
            return postBean;
        }
        catch (Exception ex) {
            String es = "Error post postDAO";
            log.error(es, ex);
            throw new ActionException(es, ex);
        }
        finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public PostBean reply(Long forumId, long t_id) throws ActionException {
        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            if (!CommonUtils.checkForumTopicId( adapter, forumId, t_id )){
                return null;
            }

            String sql =
                "select t_f_id, f_name,t_name,t_locked " +
                "from   WM_FORUM_CONCRETE, WM_FORUM_TOPIC " +
                "where  t_f_id=f_id and t_id=? ";
            ps = adapter.prepareStatement(sql);
            ps.setLong(1, t_id);
            rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }
            else {
                PostBean postBean = new PostBean();
                postBean.setF_id(rs.getLong("t_f_id"));
                postBean.setF_name(rs.getString("f_name"));
                postBean.setT_id(t_id);
                postBean.setT_name(rs.getString("t_name"));
                postBean.setT_locked(rs.getInt("t_locked"));
                return postBean;
            }
        }
        catch (Exception ex) {
            String es = "Error execute topicDAO";
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
}