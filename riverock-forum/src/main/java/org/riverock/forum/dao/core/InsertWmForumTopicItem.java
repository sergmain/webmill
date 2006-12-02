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
package org.riverock.forum.dao.core;

import org.riverock.forum.dao.bean.WmForumTopicItemType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@SuppressWarnings({"UnusedAssignment"})
public class InsertWmForumTopicItem 
{
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(InsertWmForumTopicItem.class);

    public InsertWmForumTopicItem(){}

    public static long process(org.riverock.generic.db.DatabaseAdapter db_, WmForumTopicItemType item)  throws org.riverock.forum.exception.PersistenceException     {
        String sql_ =
            "insert into WM_FORUM_TOPIC"+
             "(T_ORDER, T_LOCKED, T_ICONID, T_F_ID, T_ID, T_NAME, T_U_ID, T_REPLIES, "+
             "T_VIEWS, T_U_ID2, T_LASTTIME)"+
            "values"+
            "( ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  "+ db_.getNameDateBind()+")";

        return process(db_, item, sql_);
    }

    public static long process(org.riverock.generic.db.DatabaseAdapter db_, WmForumTopicItemType item, String sql_)  throws org.riverock.forum.exception.PersistenceException     {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = db_.prepareStatement(sql_);

             ps.setLong(1, item.getTOrder() );
             ps.setLong(2, item.getTLocked() );
             ps.setLong(3, item.getTIconid() );
             ps.setLong(4, item.getTFId() );
             ps.setLong(5, item.getTId() );
             ps.setString(6, item.getTName() );
             ps.setLong(7, item.getTUId() );
             ps.setLong(8, item.getTReplies() );
             ps.setLong(9, item.getTViews() );
             ps.setLong(10, item.getTUId2() );
             db_.bindDate(ps, 11, new java.sql.Timestamp( item.getTLasttime().getTime()) );

             int countInsertRecord = ps.executeUpdate();

             if (log.isDebugEnabled())
                 log.debug("Count of inserted records - "+countInsertRecord);

             return countInsertRecord;

         }
         catch (Exception e) {
             log.error("Item getTOrder(), value - "+item.getTOrder());
             log.error("Item getTLocked(), value - "+item.getTLocked());
             log.error("Item getTIconid(), value - "+item.getTIconid());
             log.error("Item getTFId(), value - "+item.getTFId());
             log.error("Item getTId(), value - "+item.getTId());
             log.error("Item getTName(), value - "+item.getTName());
             log.error("Item getTUId(), value - "+item.getTUId());
             log.error("Item getTReplies(), value - "+item.getTReplies());
             log.error("Item getTViews(), value - "+item.getTViews());
             log.error("Item getTUId2(), value - "+item.getTUId2());
             log.error("Item getTLasttime(), value - "+item.getTLasttime());
             log.error("SQL "+sql_);
             log.error("Exception insert data in db", e);
            throw new org.riverock.forum.exception.PersistenceException( e.getMessage(), e );
        }
        finally {
            _closeRsPs(rs, ps);
            rs = null;
            ps = null;
        }

    }

    private static void _closeRsPs(ResultSet rs, PreparedStatement ps) {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            }
            catch (Exception e01){
                // catch close error
            }
        }
        if (ps != null) {
            try {
                ps.close();
                ps = null;
            }
            catch (Exception e02) {
                // catch close error
            }
        }
    }

}
