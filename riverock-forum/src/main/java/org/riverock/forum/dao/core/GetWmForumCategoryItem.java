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

import org.riverock.forum.dao.bean.WmForumCategoryItemType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.Serializable;

@SuppressWarnings({"UnusedAssignment"})
public class GetWmForumCategoryItem implements Serializable
{
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetWmForumCategoryItem.class);

    public WmForumCategoryItemType item = null;

    public boolean isFound = false;

    public GetWmForumCategoryItem(){}

    public static GetWmForumCategoryItem getInstance(org.riverock.generic.db.DatabaseAdapter db__, Long id__)  throws org.riverock.forum.exception.PersistenceException     {
        try
        {
        return new GetWmForumCategoryItem(db__, id__ );
        }
        catch(org.riverock.forum.exception.PersistenceException exc)
        {
            throw new org.riverock.forum.exception.PersistenceException( exc.getMessage(), exc );
        }
    }

    public void copyItem(WmForumCategoryItemType target)
    {
        copyItem(this.item, target);
    }

    public static void copyItem(WmForumCategoryItemType source, WmForumCategoryItemType target)
    {
        if (source==null || target==null)
            return;

        target.setForumCategoryId( source.getForumCategoryId() );
        target.setForumId( source.getForumId() );
        target.setForumCategoryName( source.getForumCategoryName() );
        target.setIsUseLocale( source.getIsUseLocale() );
        target.setIsDeleted( source.getIsDeleted() );
    }

    public GetWmForumCategoryItem(org.riverock.generic.db.DatabaseAdapter db_, long id)  throws org.riverock.forum.exception.PersistenceException     {
        this(db_, new Long(id));
    }

    private static String sql_ = 
                "select FORUM_CATEGORY_ID, FORUM_ID, FORUM_CATEGORY_NAME, IS_USE_LOCALE, IS_DELETED from WM_FORUM_CATEGORY where FORUM_CATEGORY_ID=?";


    public GetWmForumCategoryItem(org.riverock.generic.db.DatabaseAdapter db_, Long id)  throws org.riverock.forum.exception.PersistenceException     {
        this(db_, id, sql_);
    }

    public GetWmForumCategoryItem(org.riverock.generic.db.DatabaseAdapter db_, Long id, String sqlString)  throws org.riverock.forum.exception.PersistenceException     {

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

    public static WmForumCategoryItemType fillBean(ResultSet rs) throws java.sql.SQLException {
        WmForumCategoryItemType item = new WmForumCategoryItemType();

                 long tempLong;
                 String tempString;
                 int tempBoolean;
                 tempLong = rs.getLong( "FORUM_CATEGORY_ID");
                 item.setForumCategoryId( tempLong );
                 tempLong = rs.getLong( "FORUM_ID");
                 item.setForumId( tempLong );
                 tempString = rs.getString( "FORUM_CATEGORY_NAME" );
                 item.setForumCategoryName( tempString );
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
