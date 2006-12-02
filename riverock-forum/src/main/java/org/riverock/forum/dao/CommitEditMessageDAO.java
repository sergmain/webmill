/*
 * org.riverock.forum - Forum portlet
 *
 * Copyright (C) 2006, Riverock Software, All Rights Reserved.
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
 */
package org.riverock.forum.dao;

import java.sql.SQLException;
import java.sql.Types;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import org.riverock.module.exception.ActionException;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.forum.util.CommonUtils;

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