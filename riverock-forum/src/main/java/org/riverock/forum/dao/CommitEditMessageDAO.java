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

import java.util.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import org.riverock.module.exception.ActionException;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.forum.util.CommonUtils;
import org.riverock.forum.schema.core.WmForumTopicItemType;
import org.riverock.forum.schema.core.WmForumMessageItemType;
import org.riverock.forum.schema.core.WmForumUserItemType;
import org.riverock.forum.schema.core.WmForumConcreteItemType;
import org.riverock.forum.core.InsertWmForumTopicItem;
import org.riverock.forum.core.InsertWmForumMessageItem;
import org.riverock.forum.core.GetWmForumUserItem;
import org.riverock.forum.core.UpdateWmForumUserItem;
import org.riverock.forum.core.GetWmForumConcreteItem;
import org.riverock.forum.core.UpdateWmForumConcreteItem;

/**
 * @author SMaslyukov
 *         Date: 04.05.2005
 *         Time: 15:16:08
 *         $Id$
 */
public class CommitEditMessageDAO {
    private static Logger log = Logger.getLogger(CommitEditMessageDAO.class);

    public void commit(Integer messageId, Long u_id, String content,
        String u_lastip, int tm_iconid, Long forumId) throws ActionException {
        log.debug("in commit()");
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            if (!CommonUtils.checkForumMessageId( adapter, forumId, messageId )){
                return;
            }

            DatabaseManager.runSQL(
                adapter,
                "update WM_FORUM_MESSAGE " +
                "set    COUNT_EDITED=COUNT_EDITED+1, " +
                "       LAST_EDITED_TIME=?," +
                "       M_CONTENT=?, " +
                "       M_ICONID=? " +
                "where  M_ID=? ",
                new Object[] {new Timestamp(System.currentTimeMillis()), content, new Integer(tm_iconid), messageId },
                new int[] { Types.TIMESTAMP, Types.VARCHAR, Types.INTEGER, Types.INTEGER }
            );

            adapter.commit();
        }
        catch (Exception ex) {
            try {
                adapter.rollback();
            }
            catch (SQLException sqle) {
            }
            String es = "Error post topic";
            log.error(es, ex);
            throw new ActionException(es, ex);
        }
        finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
        log.debug("out commit()");
    }
}