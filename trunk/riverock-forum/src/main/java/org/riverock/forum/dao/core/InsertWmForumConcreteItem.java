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

import org.riverock.forum.dao.bean.WmForumConcreteItemType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

@SuppressWarnings({"UnusedAssignment"})
public class InsertWmForumConcreteItem 
{
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(InsertWmForumConcreteItem.class);

    public InsertWmForumConcreteItem(){}

    public static long process(org.riverock.generic.db.DatabaseAdapter db_, WmForumConcreteItemType item)  throws org.riverock.forum.exception.PersistenceException     {
        String sql_ =
            "insert into WM_FORUM_CONCRETE"+
             "(F_ORDER, F_ID, F_NAME, F_INFO, F_U_ID, F_TOPICS, F_MESSAGES, F_U_ID2, "+
             "F_LASTTIME, FORUM_CATEGORY_ID, IS_DELETED)"+
            "values"+
            "( ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  "+ db_.getNameDateBind()+",  ?,  ?)";

        return process(db_, item, sql_);
    }

    public static long process(org.riverock.generic.db.DatabaseAdapter db_, WmForumConcreteItemType item, String sql_)  throws org.riverock.forum.exception.PersistenceException     {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = db_.prepareStatement(sql_);

             ps.setLong(1, item.getFOrder() );
             ps.setLong(2, item.getFId() );
             ps.setString(3, item.getFName() );
             ps.setString(4, item.getFInfo() );
             if (item.getFUId() != null )
                 ps.setLong(5, item.getFUId() );
             else
                 ps.setNull(5, Types.INTEGER);

             ps.setLong(6, item.getFTopics() );
             ps.setLong(7, item.getFMessages() );
             if (item.getFUId2() != null )
                 ps.setLong(8, item.getFUId2() );
             else
                 ps.setNull(8, Types.INTEGER);

             if (item.getFLasttime()!=null)
                 db_.bindDate(ps, 9, new java.sql.Timestamp( item.getFLasttime().getTime()) );
             else
                 ps.setNull(9, Types.DATE);

             ps.setLong(10, item.getForumCategoryId() );
             ps.setInt(11, item.getIsDeleted()?1:0 );

             int countInsertRecord = ps.executeUpdate();

             if (log.isDebugEnabled())
                 log.debug("Count of inserted records - "+countInsertRecord);

             return countInsertRecord;

         }
         catch (Exception e) {
             log.error("Item getFOrder(), value - "+item.getFOrder());
             log.error("Item getFId(), value - "+item.getFId());
             log.error("Item getFName(), value - "+item.getFName());
             log.error("Item getFInfo(), value - "+item.getFInfo());
             log.error("Item getFUId(), value - "+item.getFUId());
             log.error("Item getFTopics(), value - "+item.getFTopics());
             log.error("Item getFMessages(), value - "+item.getFMessages());
             log.error("Item getFUId2(), value - "+item.getFUId2());
             log.error("Item getFLasttime(), value - "+item.getFLasttime());
             log.error("Item getForumCategoryId(), value - "+item.getForumCategoryId());
             log.error("Item getIsDeleted(), value - "+item.getIsDeleted());
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
