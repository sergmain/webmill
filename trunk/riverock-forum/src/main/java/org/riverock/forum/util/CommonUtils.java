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
package org.riverock.forum.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import org.apache.log4j.Logger;

import org.riverock.common.tools.RsetTools;
import org.riverock.forum.dao.core.GetWmForumItem;
import org.riverock.forum.dao.bean.WmForumItemType;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;

/**
 * @author SMaslyukov
 *         Date: 14.04.2005
 *         Time: 19:42:03
 *         $Id$
 */
public final class CommonUtils {
    private final static Logger log = Logger.getLogger(CommonUtils.class);

    public static boolean checkForumConcreteId(DatabaseAdapter adapter, Long forumId, Long f_id) {

        if (f_id==null) {
            return false;
        }

        // in this point forumId have trusted value
        try {
            Long count = DatabaseManager.getLongValue(
                adapter,
                "select count(*) " +
                "from   WM_FORUM_CATEGORY a, WM_FORUM_CONCRETE b " +
                "where  a.FORUM_ID=? and a.FORUM_CATEGORY_ID=b.FORUM_CATEGORY_ID and " +
                "       b.F_ID=? ",
                new Object[]{ forumId, f_id },
                new int[] {Types.NUMERIC, Types.NUMERIC}
            );
            if (count==null || count.intValue()==0) {
                return false;
            }
        }
        catch (Exception e) {
            String es = "Failed error check forumId and f_id. forumId: "+forumId+", f_id: "+f_id;
            log.error(es, e);
            return false;
        }

        return true;
    }

    public static boolean checkForumCategoryId(DatabaseAdapter adapter, Long forumId, Long forumCategoryId) {

        if (forumCategoryId==null) {
            return false;
        }

        // in this point forumId have trusted value
        try {
            Long count = DatabaseManager.getLongValue(
                adapter,
                "select count(*) " +
                "from   WM_FORUM_CATEGORY a " +
                "where  a.FORUM_ID=? and a.FORUM_CATEGORY_ID=? ",
                new Object[]{ forumId, forumCategoryId },
                new int[] {Types.NUMERIC, Types.NUMERIC}
            );
            if (count==null || count.intValue()==0) {
                return false;
            }
        }
        catch (Exception e) {
            String es = "Failed error check forumId and forumCategoryId. forumId: "+forumId+", forumCategoryId: "+forumCategoryId;
            log.error(es, e);
            return false;
        }

        return true;
    }

    public static boolean checkForumTopicId(DatabaseAdapter adapter, Long forumId, Long t_id) {

        if (t_id==null) {
            return false;
        }

        // in this point forumId have trusted value
        try {
            Long count = DatabaseManager.getLongValue(
                adapter,
                "select count(*) " +
                "from   WM_FORUM_CONCRETE a, WM_FORUM_TOPIC b, WM_FORUM_CATEGORY d " +
                "where  b.T_F_ID=a.F_ID and b.T_ID=? and " +
                "       a.FORUM_CATEGORY_ID=d.FORUM_CATEGORY_ID and d.FORUM_ID=?",
                new Object[]{ t_id, forumId },
                new int[] {Types.NUMERIC, Types.NUMERIC}
            );
            if (count==null || count.intValue()==0) {
                return false;
            }
        }
        catch (Exception e) {
            String es = "Failed error check forumId and t_id. forumId: "+forumId+", t_id: "+t_id;
            log.error(es, e);
            return false;
        }

        return true;
    }

    public static boolean checkForumMessageId(DatabaseAdapter adapter, Long forumId, Integer messageId) {

        if (messageId==null) {
            return false;
        }

        // in this point forumId have trusted value
        try {
            Long count = DatabaseManager.getLongValue(
                adapter,
                "select count(*) " +
                "from   WM_FORUM_CONCRETE a, WM_FORUM_TOPIC b, WM_FORUM_CATEGORY d, " +
                "       WM_FORUM_MESSAGE e " +
                "where  b.T_F_ID=a.F_ID and b.T_ID=e.M_T_ID and " +
                "       a.FORUM_CATEGORY_ID=d.FORUM_CATEGORY_ID and d.FORUM_ID=? and " +
                "       e.M_ID=? ",
                new Object[]{ forumId, messageId },
                new int[] {Types.NUMERIC, Types.NUMERIC}
            );
            if (count==null || count.intValue()==0) {
                return false;
            }
        }
        catch (Exception e) {
            String es = "Failed check forumId and messageId. forumId: "+forumId+", messageId: "+messageId;
            log.error(es, e);
            return false;
        }

        return true;
    }

    public static WmForumItemType checkForumId(DatabaseAdapter adapter, Long forumId, String serverName) {
        if (forumId==null)
            return null;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = adapter.prepareStatement(
                "select a.* " +
                "from   WM_FORUM a, WM_PORTAL_VIRTUAL_HOST b " +
                "where  a.FORUM_ID=? and a.SITE_ID=b.ID_SITE and b.NAME_VIRTUAL_HOST=? "
            );
            RsetTools.setLong(ps, 1, forumId);
            ps.setString(2, serverName);

            rs = ps.executeQuery();
            if (rs.next()) {
                return GetWmForumItem.fillBean(rs);
            }
            return null;
        }
        catch (Exception e) {
            String es = "error check forumId";
            log.error(es, e);
            return null;
        }
        finally {
            DatabaseManager.close(rs, ps);
            rs=null;
            ps=null;
        }
    }
}
