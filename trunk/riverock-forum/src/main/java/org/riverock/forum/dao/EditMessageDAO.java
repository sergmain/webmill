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

import org.apache.log4j.Logger;

import org.riverock.forum.bean.MessageBean;
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
    private static final Logger log = Logger.getLogger(EditMessageDAO.class);

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
