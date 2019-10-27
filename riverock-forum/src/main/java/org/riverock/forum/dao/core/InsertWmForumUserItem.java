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

import org.riverock.forum.dao.bean.WmForumUserItemType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@SuppressWarnings({"UnusedAssignment"})
public class InsertWmForumUserItem 
{
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(InsertWmForumUserItem.class);

    public InsertWmForumUserItem(){}

    public static long process(org.riverock.generic.db.Database db_, WmForumUserItemType item)  throws org.riverock.forum.exception.PersistenceException     {
        String sql_ =
            "insert into WM_FORUM_USER"+
             "(U_ID, U_AVATAR_ID, U_SIGN, U_POST, U_LASTTIME, U_LASTIP)"+
            "values"+
            "( ?,  ?,  ?,  ?,  "+ db_.getNameDateBind()+",  ?)";

        return process(db_, item, sql_);
    }

    public static long process(org.riverock.generic.db.Database db_, WmForumUserItemType item, String sql_)  throws org.riverock.forum.exception.PersistenceException     {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = db_.prepareStatement(sql_);

             ps.setLong(1, item.getUId() );
             ps.setLong(2, item.getUAvatarId() );
             ps.setString(3, item.getUSign() );
             ps.setLong(4, item.getUPost() );
             db_.bindDate(ps, 5, new java.sql.Timestamp( item.getULasttime().getTime()) );
             ps.setString(6, item.getULastip() );

             int countInsertRecord = ps.executeUpdate();

             if (log.isDebugEnabled())
                 log.debug("Count of inserted records - "+countInsertRecord);

             return countInsertRecord;

         }
         catch (Exception e) {
             log.error("Item getUId(), value - "+item.getUId());
             log.error("Item getUAvatarId(), value - "+item.getUAvatarId());
             log.error("Item getUSign(), value - "+item.getUSign());
             log.error("Item getUPost(), value - "+item.getUPost());
             log.error("Item getULasttime(), value - "+item.getULasttime());
             log.error("Item getULastip(), value - "+item.getULastip());
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
