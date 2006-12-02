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

import org.riverock.forum.dao.bean.WmForumItemType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@SuppressWarnings({"UnusedAssignment"})
public class InsertWmForumItem 
{
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(InsertWmForumItem.class);

    public InsertWmForumItem(){}

    public static long process(org.riverock.generic.db.DatabaseAdapter db_, WmForumItemType item)  throws org.riverock.forum.exception.PersistenceException     {
        String sql_ =
            "insert into WM_FORUM"+
             "(FORUM_ID, SITE_ID, FORUM_NAME, IS_USE_LOCALE, IS_DELETED)"+
            "values"+
            "( ?,  ?,  ?,  ?,  ?)";

        return process(db_, item, sql_);
    }

    public static long process(org.riverock.generic.db.DatabaseAdapter db_, WmForumItemType item, String sql_)  throws org.riverock.forum.exception.PersistenceException     {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = db_.prepareStatement(sql_);

             ps.setLong(1, item.getForumId() );
             ps.setLong(2, item.getSiteId() );
             ps.setString(3, item.getForumName() );
             ps.setInt(4, item.getIsUseLocale()?1:0 );
             ps.setInt(5, item.getIsDeleted()?1:0 );

             int countInsertRecord = ps.executeUpdate();

             if (log.isDebugEnabled())
                 log.debug("Count of inserted records - "+countInsertRecord);

             return countInsertRecord;

         }
         catch (Exception e) {
             log.error("Item getForumId(), value - "+item.getForumId());
             log.error("Item getSiteId(), value - "+item.getSiteId());
             log.error("Item getForumName(), value - "+item.getForumName());
             log.error("Item getIsUseLocale(), value - "+item.getIsUseLocale());
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
