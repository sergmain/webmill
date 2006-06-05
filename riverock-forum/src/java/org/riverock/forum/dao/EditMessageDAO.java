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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.riverock.forum.bean.MessageBean;
import org.riverock.forum.bean.PostBean;
import org.riverock.forum.core.GetWmForumMessageItem;
import org.riverock.forum.schema.core.WmForumMessageItemType;
import org.riverock.forum.util.CommonUtils;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.module.exception.ActionException;

/**
 * @author SMaslyukov
 *         Date: 04.05.2005
 *         Time: 13:47:53
 *         $Id$
 */
public class EditMessageDAO {
    private static final Log log = LogFactory.getLog(EditMessageDAO.class);

    public MessageBean get(Long forumId, Long topicId, Integer messageId) throws ActionException {
        DatabaseAdapter adapter = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            if (!CommonUtils.checkForumMessageId( adapter, forumId, messageId )){
                return null;
            }

            String sql =
                "select t_f_id, f_name,t_name,t_locked " +
                "from   WM_FORUM_CONCRETE, WM_FORUM_TOPIC " +
                "where  t_f_id=f_id and t_id=? ";
            ps = adapter.prepareStatement(sql);
            ps.setLong(1, topicId.longValue() );
            rs = ps.executeQuery();

            MessageBean messageBean = new MessageBean();
            if (!rs.next()) {
                return null;
            }
            else {
                messageBean.setForumName( rs.getString("f_name") );
                messageBean.setTopicName( rs.getString("t_name") );
            }

            WmForumMessageItemType item = new GetWmForumMessageItem(adapter, messageId.intValue() ).item;

            if (item==null) {
                return null;
            }

            messageBean.setContent( item.getMContent() );
            messageBean.setMessageId( item.getMId().intValue() );
            messageBean.setIconId( item.getMIconid().intValue() );
            return messageBean;
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
}
