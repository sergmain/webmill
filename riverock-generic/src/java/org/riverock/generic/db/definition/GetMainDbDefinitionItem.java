/*
 * org.riverock.generic -- Database connectivity classes
 * 
 * Copyright (C) 2004, Riverock Software, All Rights Reserved.
 * 
 * Riverock -- The Open-source Java Development Community
 * http://www.riverock.org
 * 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package org.riverock.generic.db.definition;

import org.riverock.sql.cache.SqlStatement;
import org.riverock.generic.exception.GenericException;
import org.riverock.generic.main.CacheFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetMainDbDefinitionItem
{

//     private static GetMainDbDefinitionItem dummy = new GetMainDbDefinitionItem();

    private static CacheFactory cache = new CacheFactory( GetMainDbDefinitionItem.class.getName() );

     public MainDbDefinitionItemType item = new MainDbDefinitionItemType();

     public boolean isFound = false;

     public GetMainDbDefinitionItem(){}

     public static GetMainDbDefinitionItem getInstance(org.riverock.generic.db.DatabaseAdapter db__, long id__)
         throws Exception
     {
         return getInstance(db__, new Long(id__) );
     }

     public static GetMainDbDefinitionItem getInstance(org.riverock.generic.db.DatabaseAdapter db__, Long id__)
         throws GenericException
     {
         return (GetMainDbDefinitionItem) cache.getInstanceNew(db__, id__);
     }

     public MainDbDefinitionItemType getData(org.riverock.generic.db.DatabaseAdapter db_, long id)
         throws Exception
     {
         GetMainDbDefinitionItem obj = GetMainDbDefinitionItem.getInstance(db_, id);
         if (obj!=null)
              return obj.item;

         return new MainDbDefinitionItemType();
     }

     public void copyItem(MainDbDefinitionItemType target)
     {
         copyItem(this.item, target);
     }

     public static void copyItem(MainDbDefinitionItemType source, MainDbDefinitionItemType target)
     {
         if (source==null || target==null)
             return;

         target.setIdDbDefinition(
             source.getIdDbDefinition()
         );
         target.setNameDefinition(
             source.getNameDefinition()
         );
         target.setAplayDate(
             source.getAplayDate()
         );
     }

    public GetMainDbDefinitionItem(org.riverock.generic.db.DatabaseAdapter db_, long id)
        throws Exception
    {
        this(db_, new Long(id));
    }

    private static String sql_ = null;
    static
    {
         sql_ =
             "select * from WM_DB_DEFINITION where ID_DB_DEFINITION=?";



         try
         {
             SqlStatement.registerSql( sql_, GetMainDbDefinitionItem.class );
         }
         catch(Exception e)
         {
         }
    }

    public GetMainDbDefinitionItem(org.riverock.generic.db.DatabaseAdapter db_, Long id)
        throws Exception
    {
        this(db_, id, sql_);
    }

    public GetMainDbDefinitionItem(org.riverock.generic.db.DatabaseAdapter db_, Long id, String sqlString)
        throws Exception
    {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            ps = db_.prepareStatement(sqlString);
            ps.setLong(1, id.longValue());

            rs = ps.executeQuery();
            if (rs.next())
            {
                isFound = true;

                 long tempLong0 = rs.getLong( "ID_DB_DEFINITION");
                 if (!rs.wasNull())
                     item.setIdDbDefinition(tempLong0);
                 String tempString1 = rs.getString( "NAME_DEFINITION" );
                 if (!rs.wasNull())
                     item.setNameDefinition(tempString1);
                 java.sql.Timestamp tempTimestamp2 = rs.getTimestamp( "APLAY_DATE" );
                 if (!rs.wasNull())
                     item.setAplayDate(tempTimestamp2);
             }
         }
         catch (Exception e)
         {
             throw e;
         }
         catch (Error err)
         {
             throw err;
         }
         finally
         {
             _closeRsPs(rs, ps);
             rs = null;
             ps = null;
         }

     }

    private static void _closeRsPs(ResultSet rs, PreparedStatement ps)
    {
        if (rs != null)
        {
            try
            {
                rs.close();
                rs = null;
            }
            catch (Exception e01)
            {
            }
        }
        if (ps != null)
        {
            try
            {
                ps.close();
                ps = null;
            }
            catch (Exception e02)
            {
            }
        }
    }

}
