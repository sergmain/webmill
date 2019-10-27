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
import java.io.Serializable;

@SuppressWarnings({"UnusedAssignment"})
public class GetWmForumItem implements Serializable
{
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetWmForumItem.class);

    public WmForumItemType item = null;

    public boolean isFound = false;

    public GetWmForumItem(){}

    public static GetWmForumItem getInstance(org.riverock.generic.db.Database db__, Long id__)  throws org.riverock.forum.exception.PersistenceException     {
        try
        {
        return new GetWmForumItem(db__, id__ );
        }
        catch(org.riverock.forum.exception.PersistenceException exc)
        {
            throw new org.riverock.forum.exception.PersistenceException( exc.getMessage(), exc );
        }
    }

    public void copyItem(WmForumItemType target)
    {
        copyItem(this.item, target);
    }

    public static void copyItem(WmForumItemType source, WmForumItemType target)
    {
        if (source==null || target==null)
            return;

        target.setForumId( source.getForumId() );
        target.setSiteId( source.getSiteId() );
        target.setForumName( source.getForumName() );
        target.setIsUseLocale( source.getIsUseLocale() );
        target.setIsDeleted( source.getIsDeleted() );
    }

    public GetWmForumItem(org.riverock.generic.db.Database db_, long id)  throws org.riverock.forum.exception.PersistenceException     {
        this(db_, new Long(id));
    }

    private static String sql_ = 
                "select FORUM_ID, SITE_ID, FORUM_NAME, IS_USE_LOCALE, IS_DELETED from WM_FORUM where FORUM_ID=?";


    public GetWmForumItem(org.riverock.generic.db.Database db_, Long id)  throws org.riverock.forum.exception.PersistenceException     {
        this(db_, id, sql_);
    }

    public GetWmForumItem(org.riverock.generic.db.Database db_, Long id, String sqlString)  throws org.riverock.forum.exception.PersistenceException     {

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

    public static WmForumItemType fillBean(ResultSet rs) throws java.sql.SQLException {
        WmForumItemType item = new WmForumItemType();

                 long tempLong;
                 String tempString;
                 int tempBoolean;
                 tempLong = rs.getLong( "FORUM_ID");
                 item.setForumId( tempLong );
                 tempLong = rs.getLong( "SITE_ID");
                 item.setSiteId( tempLong );
                 tempString = rs.getString( "FORUM_NAME" );
                 item.setForumName( tempString );
                 tempBoolean = rs.getInt( "IS_USE_LOCALE");
                 if (!rs.wasNull())
                     item.setIsUseLocale( tempBoolean==1 );
                 else
                     item.setIsUseLocale( false );
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
