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

import org.riverock.forum.dao.bean.WmForumMessageItemType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.Serializable;

@SuppressWarnings({"UnusedAssignment"})
public class GetWmForumMessageItem implements Serializable
{
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetWmForumMessageItem.class);

    public WmForumMessageItemType item = null;

    public boolean isFound = false;

    public GetWmForumMessageItem(){}

    public static GetWmForumMessageItem getInstance(org.riverock.generic.db.Database db__, Long id__)  throws org.riverock.forum.exception.PersistenceException     {
        try
        {
        return new GetWmForumMessageItem(db__, id__ );
        }
        catch(org.riverock.forum.exception.PersistenceException exc)
        {
            throw new org.riverock.forum.exception.PersistenceException( exc.getMessage(), exc );
        }
    }

    public void copyItem(WmForumMessageItemType target)
    {
        copyItem(this.item, target);
    }

    public static void copyItem(WmForumMessageItemType source, WmForumMessageItemType target)
    {
        if (source==null || target==null)
            return;

        target.setMIconid( source.getMIconid() );
        target.setMTId( source.getMTId() );
        target.setMId( source.getMId() );
        target.setMContent( source.getMContent() );
        target.setMUId( source.getMUId() );
        target.setMTime( source.getMTime() );
    }

    public GetWmForumMessageItem(org.riverock.generic.db.Database db_, long id)  throws org.riverock.forum.exception.PersistenceException     {
        this(db_, new Long(id));
    }

    private static String sql_ = 
                "select M_ICONID, M_T_ID, M_ID, M_CONTENT, M_U_ID, M_TIME from WM_FORUM_MESSAGE where M_ID=?";


    public GetWmForumMessageItem(org.riverock.generic.db.Database db_, Long id)  throws org.riverock.forum.exception.PersistenceException     {
        this(db_, id, sql_);
    }

    public GetWmForumMessageItem(org.riverock.generic.db.Database db_, Long id, String sqlString)  throws org.riverock.forum.exception.PersistenceException     {

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

    public static WmForumMessageItemType fillBean(ResultSet rs) throws java.sql.SQLException {
        WmForumMessageItemType item = new WmForumMessageItemType();

                 long tempLong;
                 String tempString;
                 java.sql.Timestamp tempTimestamp = null;
                 tempLong = rs.getLong( "M_ICONID");
                 item.setMIconid( tempLong );
                 tempLong = rs.getLong( "M_T_ID");
                 item.setMTId( tempLong );
                 tempLong = rs.getLong( "M_ID");
                 item.setMId( tempLong );
                 tempString = rs.getString( "M_CONTENT" );
                 item.setMContent( tempString );
                 tempLong = rs.getLong( "M_U_ID");
                 item.setMUId( tempLong );
                 tempTimestamp = rs.getTimestamp( "M_TIME" );
                 if (!rs.wasNull())
                     item.setMTime( tempTimestamp );
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
