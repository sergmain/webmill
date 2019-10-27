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
import java.io.Serializable;

@SuppressWarnings({"UnusedAssignment"})
public class GetWmForumConcreteItem implements Serializable
{
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetWmForumConcreteItem.class);

    public WmForumConcreteItemType item = null;

    public boolean isFound = false;

    public GetWmForumConcreteItem(){}

    public static GetWmForumConcreteItem getInstance(org.riverock.generic.db.Database db__, Long id__)  throws org.riverock.forum.exception.PersistenceException     {
        try
        {
        return new GetWmForumConcreteItem(db__, id__ );
        }
        catch(org.riverock.forum.exception.PersistenceException exc)
        {
            throw new org.riverock.forum.exception.PersistenceException( exc.getMessage(), exc );
        }
    }

    public void copyItem(WmForumConcreteItemType target)
    {
        copyItem(this.item, target);
    }

    public static void copyItem(WmForumConcreteItemType source, WmForumConcreteItemType target)
    {
        if (source==null || target==null)
            return;

        target.setFOrder( source.getFOrder() );
        target.setFId( source.getFId() );
        target.setFName( source.getFName() );
        target.setFInfo( source.getFInfo() );
        target.setFUId( source.getFUId() );
        target.setFTopics( source.getFTopics() );
        target.setFMessages( source.getFMessages() );
        target.setFUId2( source.getFUId2() );
        target.setFLasttime( source.getFLasttime() );
        target.setForumCategoryId( source.getForumCategoryId() );
        target.setIsDeleted( source.getIsDeleted() );
    }

    public GetWmForumConcreteItem(org.riverock.generic.db.Database db_, long id)  throws org.riverock.forum.exception.PersistenceException     {
        this(db_, new Long(id));
    }

    private static String sql_ = 
                "select F_ORDER, F_ID, F_NAME, F_INFO, F_U_ID, F_TOPICS, F_MESSAGES, F_U_ID2, F_LASTTIME, FORUM_CATEGORY_ID, IS_DELETED from WM_FORUM_CONCRETE where F_ID=?";


    public GetWmForumConcreteItem(org.riverock.generic.db.Database db_, Long id)  throws org.riverock.forum.exception.PersistenceException     {
        this(db_, id, sql_);
    }

    public GetWmForumConcreteItem(org.riverock.generic.db.Database db_, Long id, String sqlString)  throws org.riverock.forum.exception.PersistenceException     {

        if (id==null)
            return;

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(sqlString);
            ps.setLong(1, id);

            rs = ps.executeQuery();
            if (rs.next()) {
                item = fillBean(rs);
                isFound = true;
            }

        }
        catch (Exception e) {
            log.error("Exception create object", e);
            throw new org.riverock.forum.exception.PersistenceException( e.getMessage(), e );
        }
        catch (Error err) {
            log.error("Error create object", err);
            throw err;
        }
        finally {
            _closeRsPs(rs, ps);
            rs = null;
            ps = null;
        }

    }

    public static WmForumConcreteItemType fillBean(ResultSet rs) throws java.sql.SQLException {
        WmForumConcreteItemType item = new WmForumConcreteItemType();

                 long tempLong;
                 String tempString;
                 java.sql.Timestamp tempTimestamp = null;
                 int tempBoolean;
                 tempLong = rs.getLong( "F_ORDER");
                 item.setFOrder( tempLong );
                 tempLong = rs.getLong( "F_ID");
                 item.setFId( tempLong );
                 tempString = rs.getString( "F_NAME" );
                 item.setFName( tempString );
                 tempString = rs.getString( "F_INFO" );
                 item.setFInfo( tempString );
                 tempLong = rs.getLong( "F_U_ID");
                 if (!rs.wasNull())
                     item.setFUId( new Long(tempLong) );
                 tempLong = rs.getLong( "F_TOPICS");
                 item.setFTopics( tempLong );
                 tempLong = rs.getLong( "F_MESSAGES");
                 item.setFMessages( tempLong );
                 tempLong = rs.getLong( "F_U_ID2");
                 if (!rs.wasNull())
                     item.setFUId2( new Long(tempLong) );
                 tempTimestamp = rs.getTimestamp( "F_LASTTIME" );
                 if (!rs.wasNull())
                     item.setFLasttime( tempTimestamp );
                 tempLong = rs.getLong( "FORUM_CATEGORY_ID");
                 item.setForumCategoryId( tempLong );
                 tempBoolean = rs.getInt( "IS_DELETED");
                 if (!rs.wasNull())
                     item.setIsDeleted( tempBoolean==1 );
                 else
                     item.setIsDeleted( false );
        return item;
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
