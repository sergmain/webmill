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
import java.io.Serializable;

@SuppressWarnings({"UnusedAssignment"})
public class GetWmForumUserItem implements Serializable
{
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetWmForumUserItem.class);

    public WmForumUserItemType item = null;

    public boolean isFound = false;

    public GetWmForumUserItem(){}

    public static GetWmForumUserItem getInstance(org.riverock.generic.db.DatabaseAdapter db__, Long id__)  throws org.riverock.forum.exception.PersistenceException     {
        try
        {
        return new GetWmForumUserItem(db__, id__ );
        }
        catch(org.riverock.forum.exception.PersistenceException exc)
        {
            throw new org.riverock.forum.exception.PersistenceException( exc.getMessage(), exc );
        }
    }

    public void copyItem(WmForumUserItemType target)
    {
        copyItem(this.item, target);
    }

    public static void copyItem(WmForumUserItemType source, WmForumUserItemType target)
    {
        if (source==null || target==null)
            return;

        target.setUId( source.getUId() );
        target.setUAvatarId( source.getUAvatarId() );
        target.setUSign( source.getUSign() );
        target.setUPost( source.getUPost() );
        target.setULasttime( source.getULasttime() );
        target.setULastip( source.getULastip() );
    }

    public GetWmForumUserItem(org.riverock.generic.db.DatabaseAdapter db_, long id)  throws org.riverock.forum.exception.PersistenceException     {
        this(db_, new Long(id));
    }

    private static String sql_ = 
                "select U_ID, U_AVATAR_ID, U_SIGN, U_POST, U_LASTTIME, U_LASTIP from WM_FORUM_USER where U_ID=?";


    public GetWmForumUserItem(org.riverock.generic.db.DatabaseAdapter db_, Long id)  throws org.riverock.forum.exception.PersistenceException     {
        this(db_, id, sql_);
    }

    public GetWmForumUserItem(org.riverock.generic.db.DatabaseAdapter db_, Long id, String sqlString)  throws org.riverock.forum.exception.PersistenceException     {

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

    public static WmForumUserItemType fillBean(ResultSet rs) throws java.sql.SQLException {
        WmForumUserItemType item = new WmForumUserItemType();

                 long tempLong;
                 String tempString;
                 java.sql.Timestamp tempTimestamp = null;
                 tempLong = rs.getLong( "U_ID");
                 item.setUId( tempLong );
                 tempLong = rs.getLong( "U_AVATAR_ID");
                 item.setUAvatarId( tempLong );
                 tempString = rs.getString( "U_SIGN" );
                 item.setUSign( tempString );
                 tempLong = rs.getLong( "U_POST");
                 item.setUPost( tempLong );
                 tempTimestamp = rs.getTimestamp( "U_LASTTIME" );
                 if (!rs.wasNull())
                     item.setULasttime( tempTimestamp );
                 tempString = rs.getString( "U_LASTIP" );
                 item.setULastip( tempString );
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
